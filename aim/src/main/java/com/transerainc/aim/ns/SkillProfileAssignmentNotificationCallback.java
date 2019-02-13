/**
 * 
 */
package com.transerainc.aim.ns;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.pojo.EntityAssignment;
import com.transerainc.aim.provisioning.ProvisioningConnection;
import com.transerainc.aim.provisioning.ProvisioningDAO;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.JAXBHelper;
import com.transerainc.provisioning.notificationserver.AttributeUpdateType;
import com.transerainc.provisioning.notificationserver.NodeUpdate;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;
import com.transerainc.tam.ns.NotificationCallback;

/**
 * Date of Creation: Date: Jun 8, 2010<br>
 * 
 * @author <a href=mailto:suresh@transerainc.com>Suresh Kumar</a>
 * @version $Revision: 1.7 $
 */

public class SkillProfileAssignmentNotificationCallback implements
		NotificationCallback {
	private static Logger lgr = Logger
			.getLogger(SkillProfileAssignmentNotificationCallback.class
					.getName());
	private String subscriptionId;

	private static final String REMOVE = "remove";
	private static final String ADD = "add";
	private static final String UPDATE = "update";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.transerainc.tam.ns.NotificationCallback#entityChanged(com.transerainc
	 * .provisioning.notificationserver.NotificationServerMessage)
	 */
	public void entityChanged(NotificationServerMessage message) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a Skill Profile assignment change event.");
		}

		try {
			if (lgr.isLoggable(Level.FINE)) {
				String txtMsg = JAXBHelper
						.getNotificationServerMessageAsString(message);
				lgr.fine("Notification message ... " + txtMsg);
			}
		} catch (Exception e) {
			lgr.log(Level.WARNING,
					"Exception occured while converting message to xml", e);
		}

		// 1. Refresh the skill profile data and skill assignment data
		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		SqlStatementManager stmtMgr = (SqlStatementManager) AgentInformationManagerSystem
				.findBean("sqlStatementMgr");
		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");
		ProvisioningConnection provCon = new ProvisioningConnection(cmgr);
		ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
		pmgr.initializeSkillProfileHandler(provDAO);
		
		// 2. handle notification part
		EntityAssignment assignment = getSkillAssignmentInfo(message);
		if (assignment != null) {
			SkillProfileNotificationHelper helper = new SkillProfileNotificationHelper();
			helper.handleSkillProfileAssignmentChanges(assignment);
		} else {
			lgr.info("Couldn't get skill assignemnt info ..");
		}

		// String agentId = getAgentId(message);
		//
		// if (lgr.isLoggable(Level.INFO)) {
		// lgr.info("Skill profile assignemnt changed for agent : " + agentId);
		// }
		//
		// // if (cmgr.getOperationMode() !=
		// ACGNotificatonDelegator.PRIMARY_MODE)
		// // {
		// // lgr
		// //
		// .info("This instance is not primary aim, so discarding the notification of type "
		// // + " SkillProfileNotificationCallback."
		// // + " Operation mode is :" + cmgr.getOperationMode());
		// // return;
		// // }
		// if (StringUtils.hasText(agentId)) {
		// ProvisioningConnection provCon = new ProvisioningConnection(cmgr);
		// ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
		// pmgr.updateTeamSkillProfileIdMap(provDAO, agentId);
		// SkillProfileNotificationHelper helper = new
		// SkillProfileNotificationHelper();
		// helper.handleSkillProfileAssignmentChanges(agentId);
		// } else {
		// lgr.info("Couldn't get the agent id from the notificaiton.");
		// }
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

	// private String getAgentId(NotificationServerMessage message) {
	// String agentId = null;
	// List<NodeUpdate> lst = message.getUpdateNotification().getNodeUpdate();
	// if (null != lst && lst.size() > 0) {
	// NodeUpdate nodeUpdate = lst.get(0);
	// agentId = nodeUpdate.getNodeId();
	// }
	// return agentId;
	// }

	private EntityAssignment getSkillAssignmentInfo(
			NotificationServerMessage message) {
		EntityAssignment assignment = null;
		List<NodeUpdate> lst = message.getUpdateNotification().getNodeUpdate();
		if (null != lst && lst.size() > 0) {
			NodeUpdate nodeUpdate = lst.get(0);
			if (REMOVE.equalsIgnoreCase(nodeUpdate.getUpdateType())) {
				String entityId = "";
				String entityType = "";
				String tenantId = "";
				List<AttributeUpdateType> attrList = nodeUpdate
						.getAttributeUpdate();
				if (attrList != null && !attrList.isEmpty()) {
					for (AttributeUpdateType attr : attrList) {
						if ("entityId".equals(attr.getName())) {
							entityId = attr.getOldValue();
						} else if ("entityType".equals(attr.getName())) {
							entityType = attr.getOldValue();
						} else if ("tenantId".equals(attr.getName())) {
							tenantId = attr.getOldValue();
						}
					}
				}
				assignment = new EntityAssignment(entityId, entityType, tenantId);

			} else if (ADD.equalsIgnoreCase(nodeUpdate.getUpdateType())
					|| UPDATE.equalsIgnoreCase(nodeUpdate.getUpdateType())) {

				String entityId = "";
				String entityType = "";
				String tenantId = "";
				List<AttributeUpdateType> attrList = nodeUpdate
						.getAttributeUpdate();
				if (attrList != null && !attrList.isEmpty()) {
					for (AttributeUpdateType attr : attrList) {
						if ("entityId".equals(attr.getName())) {
							entityId = attr.getNewValue();
						} else if ("entityType".equals(attr.getName())) {
							entityType = attr.getNewValue();
						} else if ("tenantId".equals(attr.getName())) {
							tenantId = attr.getNewValue();
						}
					}
				}
				assignment = new EntityAssignment(entityId, entityType, tenantId);

				// SqlStatementManager stmtMgr = (SqlStatementManager)
				// AgentInformationManagerSystem
				// .findBean("sqlStatementMgr");
				// ConfigurationManager cmgr = (ConfigurationManager)
				// AgentInformationManagerSystem
				// .findBean("configManager");
				// ProvisioningConnection provCon = new ProvisioningConnection(
				// cmgr);
				//
				// ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr,
				// provCon);
				// String nodeId = nodeUpdate.getNodeId();
				// try {
				// assignment = provDAO.getSkillAssignemnt(
				// "skill.profile.assignment.id.query", nodeId);
				// } catch (Exception e) {
				// lgr.log(Level.WARNING,
				// "Exception occured while getting skill profile assignemnt for "
				// + nodeId, e);
				// }
			}
		}

		return assignment;
	}

}
