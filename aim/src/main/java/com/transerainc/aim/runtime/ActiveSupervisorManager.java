/**
 * 
 */
package com.transerainc.aim.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.AIMNotification;
import com.transerainc.aim.pojo.ActiveSupervisor;
import com.transerainc.aim.system.AgentInformationManagerSystem;

/**
 * @author rajpal.dangi@transerainc.com
 * 
 */
public class ActiveSupervisorManager {
	private static Logger lgr = Logger.getLogger(ActiveSupervisorManager.class
			.getName());

	private ConcurrentHashMap<String, ActiveSupervisor> activeSupervisorList;

	public ActiveSupervisorManager() {
		activeSupervisorList = new ConcurrentHashMap<String, ActiveSupervisor>();
	}

	public void addSupervisor(ActiveSupervisor supervisor, boolean notify) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Adding supervisor " + supervisor + " to active list");
		}
		activeSupervisorList.put(supervisor.getSessionId(), supervisor);
		if (notify) {
			// Notify the other AIMs of this supervisor.
			AIMNotification notif = new AIMNotification();
			notif.setActiveSupervisor(supervisor);
			notif.setTenantId(supervisor.getTenantId());
			notif.setCommand("SupervisorLoggedIn");
			AIMNotificationManager notifMgr = (AIMNotificationManager) AgentInformationManagerSystem
					.findBean("aimNotifManager");
			notifMgr.sendNotification(notif);
		}
	}

	public void removeSupervisor(ActiveSupervisor supervisor, boolean notify) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Removing supervisor " + supervisor.getSupervisorId()
					+ " from active list");
		}
		activeSupervisorList.remove(supervisor.getSessionId());
		if (notify) {
			// Notify the other AIMs of this agent.
			AIMNotification notif = new AIMNotification();

			notif.setActiveSupervisor(supervisor);
			notif.setTenantId(supervisor.getTenantId());
			notif.setCommand("SupervisorLoggedOut");
			AIMNotificationManager notifMgr = (AIMNotificationManager) AgentInformationManagerSystem
					.findBean("aimNotifManager");
			notifMgr.sendNotification(notif);
		}
	}

	public ActiveSupervisor getActiveSupervisorBySessionId(String sessionId) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Getting supervisor " + sessionId + " from active list");
		}
		return activeSupervisorList.get(sessionId);
	}

	public List<ActiveSupervisor> getActiveSupervisorByTenantId(String tenantId) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Getting supervisor for tenant id: " + tenantId
					+ " from active list of size : "
					+ activeSupervisorList.size());
		}
		List<ActiveSupervisor> actList = new ArrayList<ActiveSupervisor>();

		for (Entry<String, ActiveSupervisor> entry : activeSupervisorList
				.entrySet()) {
			if (tenantId.equals(entry.getValue().getTenantId())) {
				actList.add(entry.getValue());
			}
		}
		return actList;
	}

}
