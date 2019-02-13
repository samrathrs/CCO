/**
 * 
 */
package com.transerainc.aim.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.pojo.AIMNotification;
import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.pojo.ActiveSupervisor;
import com.transerainc.aim.pojo.AuthenticationFailure;
import com.transerainc.aim.snmp.TrapManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.tam.pool.seq.SequentialTask;
import com.transerainc.tam.pool.seq.SequentialThreadPoolExecutor;
import com.transerainc.tam.util.HttpStatus;
import com.transerainc.tam.util.HttpUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class AIMNotificationManager extends Thread {
	private static final Logger lgr = Logger
			.getLogger(AIMNotificationManager.class.getName());

	private ConfigurationManager configManager;

	private LinkedBlockingQueue<AIMNotification> notificationQueue;

	private int errorSleepTime = 5000;

	private int maxQueueSize;

	private Object mutex = new Object();

	private long numNotificationsSent;

	private long numNotificationErrors;

	private long numNotificationsLost;

	private long numNotificationsDropped = 0;

	private SequentialThreadPoolExecutor seqThreadPool;

	public AIMNotificationManager(ConfigurationManager cMgr) {
		this.configManager = cMgr;

		maxQueueSize = cMgr.getAgentInformationManager()
				.getAimNotificationMaxQueueSize();

		notificationQueue = new LinkedBlockingQueue<AIMNotification>();

		// TODO - make the pool size configurable
		int corePoolSize = 10;
		int maximumPoolSize = 20;
		seqThreadPool = new SequentialThreadPoolExecutor(corePoolSize,
				maximumPoolSize, 10, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>());
	}

	public LinkedBlockingQueue<AIMNotification> getNotificationQueue() {
		return notificationQueue;
	}

	/**
	 * @return the numNotificationsSent
	 */
	public long getNumNotificationsSent() {
		return numNotificationsSent;
	}

	/**
	 * @return the numNotificationErrors
	 */
	public long getNumNotificationErrors() {
		return numNotificationErrors;
	}

	/**
	 * @return the numNotificationsLost
	 */
	public long getNumNotificationsLost() {
		return numNotificationsLost;
	}

	public long getNumNotificationsDropped() {
		return numNotificationsDropped;
	}

	public int getNotificationQueueSize() {
		return notificationQueue.size();
	}

	public int getMaxQueueSize() {
		return maxQueueSize;
	}

	public int getErrorSleepTime() {
		return errorSleepTime;
	}

	public Iterator<AIMNotification> getElementIterator() {
		return notificationQueue.iterator();
	}

	/**
	 * @param notif
	 */
	public void sendNotification(AIMNotification notif) {
		try {
			synchronized (mutex) {
				if (notificationQueue.size() >= maxQueueSize) {
					try {
						AIMNotification removedNotif = notificationQueue
								.remove();

						lgr.warning("Dropping Node: " + removedNotif);
						numNotificationsDropped++;
					} catch (Exception ex) {
					}
				}

				notificationQueue.put(notif);
			}
		} catch (InterruptedException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		lgr.info("Started the AIM Notification Manager thread.");

		while (true) {
			try {
				AIMNotification notif = notificationQueue.take();
				seqThreadPool.execute(new SequentialTask() {
					@Override
					public void run() {
						postNotification(notif);
					}

					@Override
					public String getName() {
						// Group tasks by the agentId / tenantId
						String prefix = "a-";
						String entityId = notif.getAgentId();
						ActiveAgent activeAgent = notif.getActiveAgent();
						if(activeAgent != null) {
							entityId = activeAgent.getAgentId();
						}
						if (StringUtils.isEmpty(entityId)) {
							prefix = "t-";
							entityId = notif.getTenantId();
						}
						return prefix + entityId;
					}
				});
			} catch (InterruptedException e) {
			}
		}
	}

	private void postNotification(AIMNotification notif) {
		String aimUrl = null;
		try {

			List<String> instanceList = configManager.getInstanceList();
			String context = configManager.getAgentInformationManager()
					.getAimNotificationContext();

			int maxRetryCount = configManager.getAgentInformationManager()
					.getAimNotificationRetryCount();
			errorSleepTime = configManager.getAgentInformationManager()
					.getAimNotificationFailureInterval();

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("tenantId", notif.getTenantId());
			params.put("command", notif.getCommand());
			params.put("source", "aim");

			if (notif.getActiveAgent() != null) {
				ActiveAgent agent = notif.getActiveAgent();
				params.put("agentId", agent.getAgentId());
				params.put("teamId", agent.getTeamId());
				params.put("siteId", agent.getSiteId());
				params.put("timestamp", Long.toString(agent.getTimestamp()));
				params.put("subStatus", agent.getChannelStatus());
				params.put("channelId", agent.getChannelId());
				params.put("channelType", agent.getChannelType());
				params.put("acgUrl", agent.getAcgUrl());
				params.put("agentSessionId", agent.getAgentSessionId());
				params.put("dn", agent.getDn());
			} else if (notif.getAuthenticationFailure() != null) {
				AuthenticationFailure af = notif.getAuthenticationFailure();
				params.put("agentId", af.getAgentId());
				params.put("failureCount",
						String.valueOf(af.getFailureCount()));
				params.put("isLocked", String.valueOf(af.getIsLocked()));
				context = "/aim/authFailureIntf";
			} else if (notif.getActiveSupervisor() != null) {
				ActiveSupervisor as = notif.getActiveSupervisor();
				params.put("userId", as.getSupervisorId());
				params.put("session_id", as.getSessionId());
				params.put("callbackUrl", as.getCallbackUrl());
				params.put("timeStamp", Long.toString(as.getTimestamp()));
				context = "/aim/activeSupervisorIntf";
			} else if ("LoggedOut".equals(notif.getCommand())) {
				params.put("agentId", notif.getAgentId());
				params.put("channelId", notif.getChannelId());
			} else {
				params.put("agentId", notif.getAgentId());
			}

			for (String url : instanceList) {
				aimUrl = url + context;
				if (lgr.isLoggable(Level.INFO)) {
					lgr.info("Notifying AIM @ " + aimUrl + " with data: "
							+ notif);
				}

				HttpStatus status = null;
				int triedCount = 0;
				while (triedCount < maxRetryCount) {
					try {
						status = HttpUtil.doHttpPost(aimUrl, params);

						if (status.getCode() == 200
								|| status.getCode() == 204) {
							if (lgr.isLoggable(Level.FINER)) {
								lgr.finer("Notification sent to peer "
										+ "successfully. Status: " + status);
							}
						} else {
							// Raise a trap
							lgr.warning("Error while notifying remote AIM "
									+ aimUrl + " with data: " + notif
									+ ", Status: " + status);

							TrapManager trapMgr = (TrapManager) AgentInformationManagerSystem
									.findBean("trapManager");
							trapMgr.sendAimNotificationFailed(url,
									params.get("command"),
									params.get("tenantId"),
									params.get("agentId"),
									"Remote AIM returned status code "
											+ status.getCode() + ": "
											+ status.getResponse(),
									-1);
						}

						numNotificationsSent++;
						break;
					} catch (Exception ex) {
						// Raise a trap
						lgr.log(Level.WARNING,
								"Error while sending notification to " + aimUrl
										+ " with data: " + notif,
								ex);

						triedCount++;
						numNotificationErrors++;
						if (triedCount >= 3) {
							numNotificationsLost++;
						}

						TrapManager trapMgr = (TrapManager) AgentInformationManagerSystem
								.findBean("trapManager");
						trapMgr.sendAimNotificationFailed(url,
								params.get("command"), params.get("tenantId"),
								params.get("agentId"), ex.getMessage(),
								triedCount);

						Thread.sleep(errorSleepTime);
					}
				}
			}
		} catch (Exception e) {
			// Raise a trap
			lgr.log(Level.SEVERE, "Error while notifying remote AIM at "
					+ aimUrl + " with notification " + notif, e);

			String agentId = notif.getAgentId();
			if (notif.getActiveAgent() != null) {
				agentId = notif.getActiveAgent().getAgentId();
			}
			TrapManager trapMgr = (TrapManager) AgentInformationManagerSystem
					.findBean("trapManager");
			trapMgr.sendAimNotificationFailed(aimUrl, notif.getCommand(),
					notif.getTenantId(), agentId, e.getMessage(), -1);
		}

	}
}
