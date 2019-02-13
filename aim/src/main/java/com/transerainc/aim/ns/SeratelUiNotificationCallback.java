package com.transerainc.aim.ns;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;
import com.transerainc.tam.ns.NotificationCallback;

public class SeratelUiNotificationCallback implements NotificationCallback {
	private static Logger lgr = Logger
			.getLogger(SeratelUiNotificationCallback.class.getName());
	private String subscriptionId;
	
	@Override
	public void entityChanged(NotificationServerMessage message) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a pop changed/added/removed event.");
		}
		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");
		try {
			cmgr.readSeratelUiServerMapping();
		} catch (Exception e) {
			lgr.log(Level.WARNING,
					"Exception occured while refreshing the icrm server-mapping.", e);
		}		
	}

	@Override
	public void subscribed(String subscriptionId) {
		this.subscriptionId = subscriptionId;
		
	}

	@Override
	public void unsubscribed(String subscriptionId) {
		if (this.subscriptionId.equals(subscriptionId)) {
			lgr.warning("Subscription " + subscriptionId + " unsubscribed.");
		}
		
	}

}
