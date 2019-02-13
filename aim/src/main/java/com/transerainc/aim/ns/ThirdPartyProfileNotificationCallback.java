package com.transerainc.aim.ns;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.pojo.ThirdPartyProfile;
import com.transerainc.aim.pojo.ThirdPartyProfileAddInfo;
import com.transerainc.aim.provisioning.ProvisioningConnection;
import com.transerainc.aim.provisioning.ProvisioningDAO;
import com.transerainc.aim.provisioning.ProvisioningException;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.ThirdPartyProfileHandler;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.provisioning.notificationserver.AttributeUpdate;
import com.transerainc.provisioning.notificationserver.NodeUpdate;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;
import com.transerainc.tam.ns.NotificationCallback;

public class ThirdPartyProfileNotificationCallback implements
		NotificationCallback {
	private static Logger lgr = Logger
			.getLogger(ThirdPartyProfileNotificationCallback.class.getName());
	private String subscriptionId;

	@Override
	public void entityChanged(NotificationServerMessage message) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a Agent Third Party Profile changed/added/removed event.");
		}
		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		SqlStatementManager stmtMgr = (SqlStatementManager) AgentInformationManagerSystem
				.findBean("sqlStatementMgr");
		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");
		ThirdPartyProfileHandler atpHandler = pmgr.getAgentTPProfileHandler();
		ProvisioningConnection provCon = new ProvisioningConnection(cmgr);
		String tppId = null;
		List<NodeUpdate> lst = message.getUpdateNotification().getNodeUpdate();
		if (null != lst && lst.size() > 0) {
			NodeUpdate nodeUpdate = lst.get(0);
			tppId = nodeUpdate.getNodeId();
			if ("add".equals(nodeUpdate.getUpdateType())
					|| "update".equals(nodeUpdate.getUpdateType())) {
				ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
				try {
					HashMap<String, ThirdPartyProfile> atp  = provDAO.getAgentTPProfiles(
							"third.party.profile.query", tppId);
					atpHandler.updateAgentTPProfile(atp, cmgr
							.getAgentInformationManager().getDataDirectory());
					
					//Fetch additional info
						HashMap<String, ThirdPartyProfileAddInfo> addInfoMap = provDAO
								.getAgentTPProfileAdditionalInfo(
										"third.party.profile.additional.info.query", tppId);
								atpHandler.updateAgentTPProfileAdditionalInfo(addInfoMap, cmgr
										.getAgentInformationManager().getDataDirectory());
								
								lgr.finer("Additional info for tppid: " + tppId + " fetched from prov: " + addInfoMap.toString());					

				} catch (ProvisioningException e) {
					lgr.severe("Failed to handled third party notification for id: "
							+ tppId
							+ "unable to read info from prov. Exception: "
							+ e.toString());
				}
			} else if ("remove".equals(nodeUpdate.getUpdateType())) {
				atpHandler.removeAgentTPProfile(tppId, cmgr
						.getAgentInformationManager().getDataDirectory());
			}
		}
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Successfully handled change notification for third profile id: "
					+ tppId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.transerainc.tam.ns.NotificationCallback#subscribed(java.lang.String)
	 */
	public void subscribed(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.transerainc.tam.ns.NotificationCallback#unsubscribed(java.lang.String
	 * )
	 */
	public void unsubscribed(String subscriptionId) {
		if (this.subscriptionId.equals(subscriptionId)) {
			lgr.warning("Subscription " + subscriptionId + " unsubscribed.");
		}
	}

}
