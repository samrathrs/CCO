/**
 * 
 */
package com.transerainc.aim.snmp;

import java.util.logging.Level;
import java.util.logging.Logger;

import snmp.Notification;
import snmp.mibs.XeraAimMib;
import agentx.subagent.AgentX_SubAgent;

/**
 * @author prashanth
 * 
 */
public class TrapManager {
	private Logger lgr = Logger.getLogger(TrapManager.class.getName());

	private AgentX_SubAgent subAgent;

	private String processName = "aim";

	private String snmpHost = "127.0.0.1";

	private boolean verbose;

	private int timeout;

	private String agentName;

	private int port;

	private TrapSender trapSender;

	public TrapManager(String applicationName, String snmpHost, int port,
			int timeout, String agentName, boolean verbose) {
		subAgent = new AgentX_SubAgent();
		processName = applicationName;
		this.port = port;
		this.snmpHost = snmpHost;
		this.timeout = timeout;
		this.agentName = agentName;
		this.verbose = verbose;

		this.trapSender = new TrapSender(subAgent);
		Thread t = new Thread(trapSender);
		t.start();
	}

	public void start() {
		lgr.info("Starting a session with the SNMP Daemon.");
		subAgent.startSession(snmpHost, port, timeout, agentName, verbose);
	}

	/**
	 * @return the snmpHost
	 */
	public String getSnmpHost() {
		return snmpHost;
	}

	/**
	 * @param snmpHost
	 *            the snmpHost to set
	 */
	public void setSnmpHost(String snmpHost) {
		this.snmpHost = snmpHost;
	}

	public void sendTrap(Notification notification) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Sending the trap: " + notification);
		}

		trapSender.sendTrap(notification);
	}

	public void sendAgentDoesNotExist(String login, String companyName,
			String tenantId) {
		Notification notification;
		try {
			notification =
					new XeraAimMib.XeraAimAgentDoesNotExist(processName, login,
							companyName, tenantId);
			sendTrap(notification);
		} catch (Exception e) {
			lgr.log(Level.WARNING,
				"Exception while sending the AgentDoesNotExist trap", e);
		}
	}

	/**
	 * @param agentId
	 * @param agentName2
	 * @param login
	 * @param tenantId
	 */
	public void sendIncorrectPassword(String agentId, String agentName,
			String login, String tenantId) {
		Notification notification;
		try {
			notification =
					new XeraAimMib.XeraAimIncorrectPassword(processName,
							agentId, agentName, login, tenantId);
			sendTrap(notification);
		} catch (Exception e) {
			lgr.log(Level.WARNING,
				"Exception while sending the IncorrectPassword trap", e);
		}
	}

	/**
	 * @param login
	 * @param name
	 * @param ipAddr
	 */
	public void sendInvalidLogin(String login, String cName, String ipAddr) {
		Notification notification;
		try {
			notification =
					new XeraAimMib.XeraAimInvalidLogin(processName, login,
							cName, ipAddr);
			sendTrap(notification);
		} catch (Exception e) {
			lgr.log(Level.WARNING,
				"Exception while sending the InvalidLogin trap", e);
		}
	}

	/**
	 * @param agentId
	 * @param agentName2
	 * @param login
	 * @param companyName
	 * @param tenantId
	 */
	public void sendWrongTenantName(String agentId, String agentName,
			String login, String companyName, String tenantId) {
		Notification notification;
		try {
			notification =
					new XeraAimMib.XeraAimWrongTenantName(processName, agentId,
							agentName, login, companyName, tenantId);
			sendTrap(notification);
		} catch (Exception e) {
			lgr.log(Level.WARNING,
				"Exception while sending the WrongTenantName trap", e);
		}
	}

	/**
	 * @param file
	 * @param name
	 * @param string
	 */
	public void sendStorageError(String file, String name, String operation) {
		Notification notification;
		try {
			notification =
					new XeraAimMib.XeraAimStorageError(processName, file, name,
							operation);
			sendTrap(notification);
		} catch (Exception e) {
			lgr.log(Level.WARNING,
				"Exception while sending the StorageError trap", e);
		}
	}

	/**
	 * @param string
	 * @param provisioningDatabaseUrl
	 * @param provisioningDatabaseUsername
	 */
	public void sendProvisioningDataFetchFailed(String dataType,
			String provisioningDatabaseUrl,
			String provisioningDatabaseUsername, String message) {
		Notification notification;
		try {
			notification =
					new XeraAimMib.XeraAimProvisioningDataFetchFailed(
							processName, dataType, provisioningDatabaseUrl,
							provisioningDatabaseUsername, message);
			sendTrap(notification);
		} catch (Exception e) {
			lgr.log(Level.WARNING,
				"Exception while sending the StorageError trap", e);
		}
	}

	/**
	 * @param string
	 * @param hostname
	 * @param configAgentUrl
	 * @param xpath
	 * @param xsltFile
	 * @param message
	 */
	public void sendServerConfigurationError(String fname, String hostname,
			String configAgentUrl, String xpath, String xsltFile, String message) {
		Notification notification;
		try {
			notification =
					new XeraAimMib.XeraAimServerConfigError(processName, fname,
							hostname, configAgentUrl, xpath, xsltFile, message);
			sendTrap(notification);
		} catch (Exception e) {
			lgr.log(Level.WARNING,
				"Exception while sending the StorageError trap", e);
		}
	}

	public void sendAcgNotificationFailed(String acgUrl, String message,
			String errorMsg, int retryCount) {
		Notification notification;
		try {
			notification =
					new XeraAimMib.XeraAimAcgNotificationFailed(processName,
							acgUrl, message, errorMsg, retryCount);
			sendTrap(notification);
		} catch (Exception e) {
			lgr.log(Level.WARNING,
				"Exception while sending the StorageError trap", e);
		}
	}

	public void sendAimNotificationFailed(String aimUrl, String command,
			String tenantId, String agentId, String errorMsg, int retryCount) {
		Notification notification;
		try {
			notification =
					new XeraAimMib.XeraAimNotificationFailed(processName,
							command, aimUrl, tenantId, agentId, errorMsg,
							retryCount);
			sendTrap(notification);
		} catch (Exception e) {
			lgr.log(Level.WARNING,
				"Exception while sending the StorageError trap", e);
		}
	}

	public void sendAimPasswordPolicyNotProvisioned( String tenantId, 
                                                         String agentId, String errorMsg) {
		Notification notification;
		try {
			notification =
					new XeraAimMib.XeraAimPasswordPolicyNotProvisioned(processName,
							tenantId, agentId, errorMsg);
			sendTrap(notification);
		} catch (Exception e) {
			lgr.log(Level.WARNING,
				"Exception while sending the StorageError trap", e);
		}
	}
}
