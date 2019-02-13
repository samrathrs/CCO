/**
 * 
 */
package com.transerainc.aim.ns;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.notifier.ACGNotificatonDelegator;
import com.transerainc.aim.provisioning.ProvisioningConnection;
import com.transerainc.aim.provisioning.ProvisioningDAO;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.provisioning.notificationserver.NodeUpdate;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;
import com.transerainc.tam.ns.NotificationCallback;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AgentToTeamMappingNotificationCallback implements
		NotificationCallback {
	private static Logger lgr = Logger
			.getLogger(AgentToTeamMappingNotificationCallback.class.getName());
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
			lgr.info("Got a team changed/added/removed event.");
		}

		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		SqlStatementManager stmtMgr = (SqlStatementManager) AgentInformationManagerSystem
				.findBean("sqlStatementMgr");
		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");

		ProvisioningConnection provCon = new ProvisioningConnection(cmgr);
		ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
		pmgr.initializeAgentToTeamMappingHandler(provDAO);

		// Notify TPGs with the change
		ACGNotificatonDelegator delegator = new ACGNotificatonDelegator(
				AgentToTeamMappingNotificationCallback.class.getName());
		delegator.initNotificaitonToACG(message);
		//		
		// // Notify TPGs with the change
		// ACGNotificationManager notifMgr =
		// (ACGNotificationManager) AgentInformationManagerSystem
		// .findBean("acgNotificationManager");
		//
		// try {
		// ActiveAgentManager activeAgentMgr =
		// (ActiveAgentManager) AgentInformationManagerSystem
		// .findBean("activeAgentManager");
		// List<String> urlList = activeAgentMgr.getAllAcgUrls();
		// ACGNotification notif =
		// new NSNotification(cmgr.getAgentInformationManager()
		// .getNotificationAcgContext());
		// notif.setAcgUrls(urlList);
		// String txtMsg =
		// JAXBHelper.getNotificationServerMessageAsString(message);
		//
		// notif.setMessage(txtMsg);
		// notif.setMethod(ACGNotification.METHOD_POST);
		//
		// notifMgr.sendNotification(notif);
		// } catch (Exception e) {
		// lgr.log(Level.SEVERE, "Error while sending notification to ACG", e);
		// }
	}

	/**
	 * @param agentIdList
	 * @return
	 */
	// private String getAgentIdList(List<String> agentIdList) {
	// StringBuilder builder = new StringBuilder();
	// boolean first = true;
	// if (agentIdList != null && agentIdList.size() > 0) {
	// for (String agentId : agentIdList) {
	// if (!first) {
	// builder.append(",");
	// first = false;
	// }
	//
	// first = false;
	// builder.append(agentId);
	// }
	// }
	//
	// return builder.toString();
	// }
	/**
	 * @param message
	 * @return
	 */
	@SuppressWarnings( { "unchecked", "unused" })
	private List<String> getIdList(NotificationServerMessage message) {
		List<String> lst = new ArrayList<String>();

		List<NodeUpdate> updateList = message.getUpdateNotification()
				.getNodeUpdate();
		for (NodeUpdate nodeUpd : updateList) {
			lst.add(nodeUpd.getNodeId());
		}

		return lst;
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
