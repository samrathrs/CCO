/**
 * 
 */
package com.transerainc.aim.snmp;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import snmp.Notification;

import agentx.subagent.AgentX_SubAgent;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class TrapSender implements Runnable {
	private static Logger lgr = Logger.getLogger(TrapSender.class.getName());
	private AgentX_SubAgent subAgent;
	private LinkedBlockingQueue<Notification> notifQueue;

	public TrapSender(AgentX_SubAgent subAgent) {
		this.subAgent = subAgent;
		notifQueue = new LinkedBlockingQueue<Notification>(100);
	}

	public void sendTrap(Notification notif) {
		notifQueue.add(notif);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (true) {
			Notification notif = null;
			try {
				notif = notifQueue.take();

				if (subAgent.getSnmpSessionId() == null) {
					lgr.warning("A session to the SNMP daemon does"
						+ " not seem to be established. Waiting...");
					Thread.sleep(3000);
					lgr.warning("SNMP Session Id: "
						+ subAgent.getSnmpSessionId());
				}

				subAgent.sendNotification(notif);
			} catch (Exception ex) {
				lgr.log(Level.WARNING, "Exception while sending the trap: "
					+ notif, ex);
			}
		}
	}

}
