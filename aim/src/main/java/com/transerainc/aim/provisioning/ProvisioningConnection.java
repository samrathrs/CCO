/**
 * 
 */
package com.transerainc.aim.provisioning;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.conf.xsd.AgentInformationManager;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ProvisioningConnection {
	private static Logger lgr =
			Logger.getLogger(ProvisioningConnection.class.getName());
	private ConfigurationManager configMgr;
	private Connection con;

	public ProvisioningConnection(ConfigurationManager cmgr) {
		this.configMgr = cmgr;
	}

	public synchronized Connection getConnection() throws ProvisioningException {
		if (con == null) {
			try {
				AgentInformationManager aim =
						configMgr.getAgentInformationManager();
				Class.forName(aim.getProvisioningDatabaseDriverName())
						.newInstance();

				con =
						DriverManager.getConnection(aim
								.getProvisioningDatabaseUrl(), aim
								.getProvisioningDatabaseUsername(), aim
								.getPriovisioningDatabasePassword());
			} catch (Exception e) {
				con = null;
				lgr.log(Level.SEVERE, "Error while creating a connection "
					+ "to the provisioning database", e);
				throw new ProvisioningException(e);
			}
		}

		return con;
	}

	public void closeConnection() {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				lgr.log(Level.SEVERE, "Error while closing the connection "
					+ "to the provisioning database.", e);
			}
		}
	}
}
