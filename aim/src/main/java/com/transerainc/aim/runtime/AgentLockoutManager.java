/**
 * 
 */
package com.transerainc.aim.runtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.pojo.AIMNotification;
import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.pojo.AuthenticationFailure;
import com.transerainc.aim.pojo.AuthenticationFailureResult;
import com.transerainc.aim.pojo.PasswordPolicy;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.AgentProfileHandler;
import com.transerainc.aim.provisioning.handler.PasswordPolicyHandler;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.tam.util.HttpStatus;
import com.transerainc.tam.util.HttpUtil;

/**
 * @author Prashanth Gujjeti
 */
public class AgentLockoutManager {
	private static Logger lgr = Logger.getLogger(AgentLockoutManager.class
			.getName());

	private Map<String, AuthenticationFailure> agentMap;

	private final ConfigurationManager configMgr;

	public AgentLockoutManager(ConfigurationManager cmgr) {
		this.configMgr = cmgr;
		agentMap = new ConcurrentHashMap<String, AuthenticationFailure>();
	}

	public AuthenticationFailureResult authenticationFailed(Agent agent) {
		ProvisioningManager pMgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		PasswordPolicyHandler ppHandler = pMgr.getPasswordPolicyHandler();
		AgentProfileHandler apHandler = pMgr.getAgentProfileHandler();
		
		PasswordPolicy pp = null;
		AuthenticationFailure af = null;
		AuthenticationFailureResult ar = null;
		String ppId = apHandler.getAgentProfilePwdPolicyId(agent.getProfileId());
		if(ppId != null){
			pp = ppHandler.getPasswordPolicy(ppId);
		}else{
			
			//FIXME:: Need to remove it later once its assured that all the 
			//agent profiles would have pwd policy associated
			
			lgr.warning("Agent lockout manager using default password policy for agent: " + agent.getAgentId() +
					" tenant: " + agent.getTenantId());
			
			pp = ppHandler.getDefaultPasswordPolicyForTenant(agent.getTenantId());
			
		}
		
		if(pp == null){
			lgr.severe("Neither password policy associated with agent: " + agent.getAgentId() + 
					" nor default pwd policy for tenant: " + agent.getTenantId());
			return ar;
		}		
		int maxInvalidAttempts = pp.getMaxInvalidAttempts();
		if (maxInvalidAttempts <= 0) {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Enterprise (" + agent.getTenantId()
						+ ") does not enforce MAX INVALID ATTEMPTS. Agent Id: "
						+ agent.getAgentId());
			}
			return ar;
		}

		String agentId = agent.getAgentId();
		af = agentMap.get(agentId);
		if (af == null) {
			af = new AuthenticationFailure();
			af.setAgentId(agentId);
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Adding entry for agent " + agentId);
			}
			agentMap.put(agentId, af);
		}
		int failureCount = af.getFailureCount();
		failureCount++;
		af.setFailureCount(failureCount);

		if (failureCount >= maxInvalidAttempts) {
			lgr.warning("Agent " + agentId + " reached max invalid attempts ("
					+ maxInvalidAttempts + "). Locking the agent.");
			af.setIsLocked(true);
			sendAgentLockoutToProv(agent);
		}
		notifyPeer("updateAgentLockout", af);
		ar = new AuthenticationFailureResult();
		ar.setMaxInvalid(maxInvalidAttempts);
        ar.setCurrentInavlid(failureCount);
		return ar;
	}

	private void sendAgentLockoutToProv(Agent agent) {
		String provUrl = configMgr.getAgentInformationManager()
				.getProvisioningUrl();
		int maxRetries = configMgr.getAgentInformationManager()
				.getProvisioningNotificationRetryCount();
		String url = provUrl + "/provbe/lockAgent";
		Map<String, String> params = new HashMap<String, String>();
		params.put("agentID", agent.getAgentId());
		params.put("tenantID", agent.getTenantId());

		// POST the data
		int retryCount = 0;
		while (retryCount < maxRetries) {
			try {
				HttpStatus status = HttpUtil.doHttpPost(url, params);
				if(status.getCode() >= 200 && status.getCode() < 300) {
					if (lgr.isLoggable(Level.INFO)) {
						lgr.info("Prov notified to lockout agent "
								+ agent.getAgentId());
					}
				} else {
					lgr.warning("Notification to prov failed with status: "
							+ status);
				}
				break;
			} catch (IOException e) {
				lgr.log(Level.WARNING, "Exception while updating Provisioning",
						e);
				retryCount++;
			}
		}
	}

	public boolean isAgentLocked(Agent agent) {
		boolean isLocked = agent.getIsLocked();
		if (!isLocked) {
			String agentId = agent.getAgentId();
			AuthenticationFailure af = agentMap.get(agentId);
			if (af != null) {
				isLocked = af.getIsLocked();
			}
		}
		return isLocked;
	}

	public void clearAgentLockout(String agentId) {
		clearAgentLockout(agentId, false);
	}

	public void clearAgentLockout(String agentId, boolean notify) {
		if (agentMap.containsKey(agentId)) {
			AuthenticationFailure af = agentMap.remove(agentId);
			lgr.warning("Agent " + agentId + " has been unlocked");
			if (notify) {
				// Notify peer
				notifyPeer("releaseAgentLockout", af);
			}
		}
	}

	private void notifyPeer(String command, AuthenticationFailure af) {
		AIMNotificationManager aimNotifMgr = (AIMNotificationManager) AgentInformationManagerSystem
				.findBean("aimNotifManager");
		AIMNotification notif = new AIMNotification();
		notif.setCommand(command);
		notif.setAuthenticationFailure(af);
		aimNotifMgr.sendNotification(notif);
	}

	public AuthenticationFailure getAuthorizationFailureForAgent(String agentId) {
		return agentMap.get(agentId);
	}

	public void addAuthenitcationFailureForAgent(AuthenticationFailure af) {
		agentMap.put(af.getAgentId(), af);
	}

	public List<AuthenticationFailure> getAuthenticationFailureList() {
		return new ArrayList<AuthenticationFailure>(agentMap.values());
	}

	public void recoverAuthFailures(List<AuthenticationFailure> afList) {
		if(lgr.isLoggable(Level.INFO)) {
			lgr.info("Recovering the auth failures from " + afList);
		}
		for (AuthenticationFailure af : afList) {
			agentMap.put(af.getAgentId(), af);
		}
	}

}
