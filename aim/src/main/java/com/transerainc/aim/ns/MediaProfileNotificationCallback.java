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
import com.transerainc.aim.pojo.MediaProfile;
import com.transerainc.aim.provisioning.ProvisioningConnection;
import com.transerainc.aim.provisioning.ProvisioningDAO;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.AgentMediaProfileHandler;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.provisioning.notificationserver.NodeUpdate;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;
import com.transerainc.tam.ns.NotificationCallback;

/**
 * @author Rajpal Dangi
 * 
 */
public class MediaProfileNotificationCallback implements NotificationCallback {
	private static Logger lgr = Logger
			.getLogger(MediaProfileNotificationCallback.class.getName());
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
			lgr.info("Got a Agent Media Profile changed/added/removed event.");
		}
		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		SqlStatementManager stmtMgr = (SqlStatementManager) AgentInformationManagerSystem
				.findBean("sqlStatementMgr");
		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");
		ProvisioningConnection provCon = new ProvisioningConnection(cmgr);

		String profileId = getMediaProfileId(message);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("The media profile id that has been changed is "
					+ profileId);
		}

		// Handle the delta part of channel and notify ACGs
		if (StringUtils.hasText(profileId)) {
			// if (cmgr.getOperationMode() !=
			// ACGNotificatonDelegator.PRIMARY_MODE) {
			// lgr
			// .info("This instance is not primary aim, so not notifying ACG with channel delta. Operation mode is :"
			// + cmgr.getOperationMode());
			// ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
			// pmgr.initializeAgentMediaProfileHandler(provDAO);
			// return;
			// }
			ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
			pmgr.initializeAgentMediaProfileHandler(provDAO);
			// get the latest AgentMediaProfileHandler
			AgentMediaProfileHandler amph = pmgr.getAgentMediaProfileHandler();
			MediaProfile mediaProfile = amph.getAgentMediaProfile(profileId);
			if (mediaProfile != null) {
				MediaProfileNotificationHelper helper = new MediaProfileNotificationHelper(
						mediaProfile);
				helper.handleProfileModificationChanges();
			}
		} else {
			lgr
					.info("Couldn't find the media profile id from the notification.");
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

	private String getMediaProfileId(NotificationServerMessage message) {
		String profileId = null;
		List<NodeUpdate> lst = message.getUpdateNotification().getNodeUpdate();
		if (null != lst && lst.size() > 0) {
			NodeUpdate nodeUpdate = lst.get(0);
			profileId = nodeUpdate.getNodeId();
		}
		return profileId;
	}

}
