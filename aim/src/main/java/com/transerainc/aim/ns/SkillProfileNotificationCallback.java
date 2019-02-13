/**
 * 
 */
package com.transerainc.aim.ns;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.util.StringUtils;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.pojo.SkillProfile;
import com.transerainc.aim.provisioning.ProvisioningConnection;
import com.transerainc.aim.provisioning.ProvisioningDAO;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.SkillProfileHandler;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.JAXBHelper;
import com.transerainc.provisioning.notificationserver.NodeUpdate;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;
import com.transerainc.tam.ns.NotificationCallback;

/**
 * Date of Creation: Date: Jun 8, 2010<br>
 * 
 * @author <a href=mailto:suresh@transerainc.com>Suresh Kumar</a>
 * @version $Revision: 1.7 $
 */

public class SkillProfileNotificationCallback implements NotificationCallback {
	private static Logger lgr = Logger
			.getLogger(SkillProfileNotificationCallback.class.getName());
	private String subscriptionId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.transerainc.tam.ns.NotificationCallback#entityChanged(com.transerainc
	 * .provisioning.notificationserver.NotificationServerMessage)
	 */
	public void entityChanged(NotificationServerMessage message) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a Skill Profile changed/added/removed event.");
		}

		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		SqlStatementManager stmtMgr = (SqlStatementManager) AgentInformationManagerSystem
				.findBean("sqlStatementMgr");
		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");
		ProvisioningConnection provCon = new ProvisioningConnection(cmgr);
		ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
		pmgr.initializeSkillProfileHandler(provDAO);

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

		String skillProfileId = getSkillProfileId(message);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Skill profile that has changed is : " + skillProfileId);
		}

		// if (cmgr.getOperationMode() != ACGNotificatonDelegator.PRIMARY_MODE)
		// {
		// lgr
		// .info("This instance is not primary aim, so discarding the notification of type "
		// + " SkillProfileNotificationCallback."
		// + " Operation mode is :" + cmgr.getOperationMode());
		// return;
		// }

		if (StringUtils.hasText(skillProfileId)) {
			SkillProfileHandler skpHandler = pmgr.getSkillProfileHandler();
			SkillProfile profile = skpHandler.getSkillProfile(skillProfileId);
			if (profile != null) {
				SkillProfileNotificationHelper helper = new SkillProfileNotificationHelper(
						profile);
				helper.handleSkillProfileModificationChanges();
			} else {
				lgr.info("Skill profile not found from the agent_skills_view of profile "
						+ skillProfileId);
			}
		} else {
			lgr.info("Couldn't get the skill profile id from the notificaiton.");
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

	private String getSkillProfileId(NotificationServerMessage message) {
		String profileId = null;
		List<NodeUpdate> lst = message.getUpdateNotification().getNodeUpdate();
		if (null != lst && lst.size() > 0) {
			NodeUpdate nodeUpdate = lst.get(0);
			profileId = nodeUpdate.getNodeId();
		}
		return profileId;
	}
}
