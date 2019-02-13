package com.transerainc.aim.ns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.pojo.ThirdPartyProfile;
import com.transerainc.aim.pojo.EntityAssignment;
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

public class ThirdPartyProfileAssignmentNotificationCallback implements
		NotificationCallback {
	private static Logger lgr = Logger
			.getLogger(ThirdPartyProfileAssignmentNotificationCallback.class
					.getName());
	private String subscriptionId;

	@Override
	public void entityChanged(NotificationServerMessage message) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a Agent Third Party Profile assignment changed/added/removed event.");
		}
		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		SqlStatementManager stmtMgr = (SqlStatementManager) AgentInformationManagerSystem
				.findBean("sqlStatementMgr");
		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");
		ThirdPartyProfileHandler atpHandler = pmgr.getAgentTPProfileHandler();
		ProvisioningConnection provCon = new ProvisioningConnection(cmgr);
		String dataDir = cmgr.getAgentInformationManager().getDataDirectory();

		String nodeId = null;
		String entityId = null;
		String entityType = null;
		String thirdPartyProfileId = null;
		
		String oldEntityId = null;
		String oldEntityType = null;
		String oldThirdPartyProfileId = null;
		
		List<NodeUpdate> lst = message.getUpdateNotification().getNodeUpdate();
		if (null != lst && lst.size() > 0) {
			NodeUpdate nodeUpdate = lst.get(0);
			nodeId = nodeUpdate.getNodeId();
			EntityAssignment entry = null;
			List<AttributeUpdate> attList = nodeUpdate.getAttributeUpdate();
			for (AttributeUpdate au : attList) {
				if ("entityType".equals(au.getName())) {
					entityType = au.getNewValue();
					oldEntityType = au.getOldValue();
				} else if ("entityId".equals(au.getName())) {
					entityId = au.getNewValue();
					oldEntityId = au.getOldValue();
				}else if ("thirdPartyProfileId".equals(au.getName())) {
					thirdPartyProfileId = au.getNewValue();
					oldThirdPartyProfileId = au.getOldValue();
				}
			}
			if ("add".equals(nodeUpdate.getUpdateType())
					|| "update".equals(nodeUpdate.getUpdateType())) {
				ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
				try {
					lgr.finer("Getting data from prov for node-id: " + nodeId + " entity type:" + entityType +
							" entity id: " + entityId +" and tpid: " + thirdPartyProfileId);
					HashMap<String, String> entryMap = provDAO
							.getAgentTPProfileAssignments(
									"third.party.profile.assignment.query", nodeId)
							.get(entityType);					
                                        List<String> values = new ArrayList(entryMap.values());
					for (int i = 0; i < values.size(); i++) {
						HashMap<String, ThirdPartyProfileAddInfo> addInfoMap = provDAO
								.getAgentTPProfileAdditionalInfo(
										"third.party.profile.additional.info.query", values.get(i));
								atpHandler.updateTPProfileAssignment(entityType, entryMap, dataDir);
								atpHandler.updateAgentTPProfileAdditionalInfo(addInfoMap, dataDir);
								lgr.finer("Additional info for tpid: " + values.get(i) + " fetched from prov: " + addInfoMap.toString());					
					}
					

				} catch (ProvisioningException e) {
					lgr.severe("Failed to handled third party notification for node id: "
							+ nodeId
							+ "unable to read info from prov. Exception: "
							+ e.toString());
				}
			} else if ("remove".equals(nodeUpdate.getUpdateType())) {				
//				atpHandler.removeTPProfileAssignment(entityType, entityId, dataDir);
				lgr.info("Remove data for node-id: " + nodeId + " entity type:" + oldEntityType +
						" entity id: " + oldEntityId +" and tpid: " + oldThirdPartyProfileId);
				atpHandler.removeTPProfileAssignment(oldEntityType, oldEntityId, dataDir);

			} else {
				lgr.warning("Failed to handled third party assignment notification for id: "
						+ nodeId + " of type:" + nodeUpdate.getUpdateType());
			}
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
