/**
 * 
 */
package com.transerainc.aim.monitor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.tam.util.HttpStatus;
import com.transerainc.tam.util.HttpUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class AgentStateMonitorManager extends Thread {
	private static final Logger lgr =
			Logger.getLogger(AgentStateMonitorManager.class.getName());
	private ConfigurationManager configMgr;

	public AgentStateMonitorManager(ConfigurationManager cMgr) {
		this.configMgr = cMgr;
	}

	public void run() {
		while (true) {
			try {
				int delay =
						configMgr.getAgentInformationManager()
								.getStatusCheckFrequency();
				Thread.sleep(delay);

				if (lgr.isLoggable(Level.INFO)) {
					lgr.info("Monitoring thread woke up to check agent status");
				}

				ActiveAgentManager aaMgr =
						(ActiveAgentManager) AgentInformationManagerSystem
								.findBean("activeAgentManager");

				List<ActiveAgent> activeAgentList =
						aaMgr.getAgentsWithNoActivityFor(delay);

				for (ActiveAgent agent : activeAgentList) {
					String acgUrl = agent.getAcgUrl();
					acgUrl +=
							configMgr.getAgentInformationManager()
									.getStatusCheckAcgContext()
								+ "&agentId="
								+ agent.getAgentId()
								+ "&channelId="
								+ agent.getChannelId()
								+ "&enterpriseId="
								+ agent.getTenantId()
								+ "&agentSessionId="
								+ agent.getAgentSessionId();

					if (lgr.isLoggable(Level.INFO)) {
						lgr.info("Going to check status using URL: " + acgUrl);
					}

					HttpStatus status =
							HttpUtil.doGet(acgUrl, 5000, agent.getAgentId());
					String resp = status.getResponse();
					if (resp != null) {
						if (resp.contains("LoggedIn")) {
							// Agent is still Logged In. Get the substatus
							String[] tokens = resp.split(":");
							if (tokens.length == 2) {
								String subStatus = tokens[1];
								agent.setChannelStatus(subStatus);
							} else {
								lgr.warning("ACG returned an unexpected "
									+ "response for check status: " + resp);
							}
						} else {
							if (lgr.isLoggable(Level.INFO)) {
								lgr.info("ACG says that the agent "
									+ "is no longer active (" + agent
									+ "). Cleaning up.");
							}

							aaMgr.removeAgent(agent.getTenantId(), agent
									.getAgentId(), agent.getChannelId(), true);
						}
					}

				}
			} catch (Exception e) {
				lgr.log(Level.SEVERE,
					"Error while synchronizing with the TPGs", e);
			}
		}
	}
}
