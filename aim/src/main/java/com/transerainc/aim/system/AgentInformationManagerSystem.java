/**
 * 
 */
package com.transerainc.aim.system;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import com.transerainc.aim.conf.AIMConfigurationException;
import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.notifier.RemoteSynchronizer;
import com.transerainc.aim.provisioning.ProvisioningException;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.tam.tpm.TPMFacade;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AgentInformationManagerSystem {
	public static final long UP_TIMESTAMP = System.currentTimeMillis();
	private static Logger lgr =
			Logger.getLogger(AgentInformationManagerSystem.class.getName());
	private static XmlBeanFactory factory;
	private static TPMFacade tpmFacade;

	public void start() throws AIMConfigurationException {
		String springConfigFile =
				System.getProperty("config.dir") + "/aim-spring.xml";

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Reading spring config file: " + springConfigFile);
		}

		FileSystemResource resource = new FileSystemResource(springConfigFile);

		factory = new XmlBeanFactory(resource);

		findBean("beanInitializer");

		// Now go sync up with the peer (if any) wrt active agents.
		RemoteSynchronizer rs = new RemoteSynchronizer();
		rs.synchronizeWithPeerAims();

		String tpmUrl = System.getProperty("tpm.url");
		String pid = null;
		if (tpmUrl != null && tpmUrl.length() > 0) {
			pid = System.getProperty("process.id");
		}

		ConfigurationManager cMgr =
				(ConfigurationManager) findBean("configManager");
		tpmFacade = TPMFacade.getInstance();
		tpmFacade.register(System.getProperty("application.name"), pid, cMgr
				.getMyUrl()
			+ "/doPing", cMgr.getMyUrl() + "/doShutdown");

	}

	public static Object findBean(String name) {
		Object obj = factory.getBean(name);

		if (lgr.isLoggable(Level.FINE)) {
			lgr.fine("Got a request for " + name + ": " + obj);
		}

		return obj;
	}

	/**
	 * @throws AIMConfigurationException
	 * @throws ProvisioningException
	 * 
	 */
	public static void reloadConfiguration() throws AIMConfigurationException,
			ProvisioningException {
		ConfigurationManager cmgr =
				(ConfigurationManager) findBean("configManager");
		cmgr.readConfiguration();

		SqlStatementManager stmtMgr =
				(SqlStatementManager) findBean("sqlStatementMgr");
		stmtMgr.init(cmgr);

		ProvisioningManager provMgr =
				(ProvisioningManager) findBean("provManager");
		provMgr.init();

		// Request a logout of all agents in the given logout states.
		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Going to request logout for active "
				+ "agents in the logout-able states");
		}

		ActiveAgentManager aaMgr =
				(ActiveAgentManager) findBean("activeAgentManager");
		aaMgr.logoutShutdownTenantAgents(cmgr);
	}

	/**
	 * @return
	 */
	public static TPMFacade getTPMFacade() {
		return tpmFacade;
	}
}
