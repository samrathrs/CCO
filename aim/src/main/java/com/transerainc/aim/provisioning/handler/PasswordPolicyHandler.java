/**
 * 
 */
package com.transerainc.aim.provisioning.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.pojo.PasswordPolicy;
import com.transerainc.aim.pojo.PasswordPolicyResult;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.snmp.TrapManager;
import com.transerainc.aim.store.PasswordPolicyStore;
import com.transerainc.aim.system.AgentInformationManagerSystem;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class PasswordPolicyHandler {
	private static Logger lgr = Logger.getLogger(PasswordPolicyHandler.class
			.getName());
	private HashMap<String, PasswordPolicy> passwordPolicyMap;
	private HashMap<String, List<String>> tenantToPolicyMap;

	public PasswordPolicyHandler(String dataDir) {
		PasswordPolicyStore ppStore = new PasswordPolicyStore(dataDir);
		passwordPolicyMap = ppStore.retrievePolicies();

		createTenantToPolicyMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant to Password Policy Mapping: "
					+ tenantToPolicyMap);
		}

	}

	public PasswordPolicyHandler(HashMap<String, PasswordPolicy> policyMap,
			String dataDir) {
		this.passwordPolicyMap = policyMap;

		createTenantToPolicyMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant to Password Policy Mapping: "
					+ tenantToPolicyMap);
		}

		PasswordPolicyStore ppStore = new PasswordPolicyStore(dataDir);
		ppStore.storePasswordPolicies(passwordPolicyMap);
	}

	public List<String> getPasswordPolicyIdsForTenant(String tenantId) {
		List<String> idList = tenantToPolicyMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
					+ " returned the following password policy ids - " + idList);
		}

		return idList;
	}

	public List<PasswordPolicy> getPasswordPolicysForTenant(String tenantId) {
		List<String> idList = getPasswordPolicyIdsForTenant(tenantId);

		List<PasswordPolicy> ppList = new ArrayList<PasswordPolicy>();
		if (idList != null) {
			for (String id : idList) {
				PasswordPolicy pp = passwordPolicyMap.get(id);

				ppList.add(pp);
			}
		} else {
			if (lgr.isLoggable(Level.SEVERE)) {
				lgr.severe("No password policy assigned to tenant: " + tenantId);
			}
		}
		return ppList;
	}

	public Collection<String> getPasswordPolicyIds() {
		Collection<String> keys = passwordPolicyMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for password policy ids returned - " + keys);
		}

		return keys;
	}

	public PasswordPolicy getPasswordPolicy(String id) {
		PasswordPolicy pp = passwordPolicyMap.get(id);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for password policy " + id + " returned - " + pp);
		}

		return pp;
	}

	public PasswordPolicy getDefaultPasswordPolicyForTenant(String tenantId) {
		List<String> idList = getPasswordPolicyIdsForTenant(tenantId);
		PasswordPolicy retPwdPolicy = null;
		if (idList != null) {
			for (String id : idList) {
				PasswordPolicy pp = passwordPolicyMap.get(id);
				if (1 == pp.getIsDefault()) {
					retPwdPolicy = pp;
					break;
				}

			}
		} else {
			if (lgr.isLoggable(Level.SEVERE)) {
				lgr.severe("No password policy assigned to tenant: " + tenantId);
			}
		}
		return retPwdPolicy;
	}

	/**
	 * 
	 */
	private void createTenantToPolicyMapping() {
		this.tenantToPolicyMap = new HashMap<String, List<String>>();

		for (String policyId : passwordPolicyMap.keySet()) {
			PasswordPolicy pPolicy = passwordPolicyMap.get(policyId);
			String tenantId = pPolicy.getTenantId();

			List<String> ppIdList = null;
			if (tenantToPolicyMap.containsKey(tenantId)) {
				ppIdList = tenantToPolicyMap.get(tenantId);
			} else {
				ppIdList = new ArrayList<String>();
				tenantToPolicyMap.put(tenantId, ppIdList);
			}

			ppIdList.add(policyId);
		}
	}

	public int getMaxInvalidAttempts(String ppId) {
		int maxInvalidAttempts = -1;

		PasswordPolicy pp = passwordPolicyMap.get(ppId);
		maxInvalidAttempts = pp.getMaxInvalidAttempts();
		if (maxInvalidAttempts == -1) {
			if (lgr.isLoggable(Level.FINER)) {
				lgr.finer("MAX_INVALID_ATTEMPTS not configured for password policy id: "
						+ ppId);
			}
		}

		if (lgr.isLoggable(Level.FINER)) {
			lgr.finer("Max invalid attempts for password policy id( " + ppId
					+ " ) is: " + maxInvalidAttempts);
		}
		return maxInvalidAttempts;
	}

	public boolean getIsPasswordPolicySet(String ppId) {
		PasswordPolicy pp = passwordPolicyMap.get(ppId);
		if (pp.getPwdExpirePeriod() > 0)
			return true;
		else
			return false;
	}

	public boolean applyPasswordPolicy(Agent agent, String tenantId, PasswordPolicyResult ppResult ) {
		
		ProvisioningManager pMgr = (ProvisioningManager) AgentInformationManagerSystem
		                                                         .findBean("provManager");
		
		AgentProfileHandler apHandler = pMgr.getAgentProfileHandler();

		// First, check if agent must change password irrespective of pwd policy
		// assigned

		if (agent.isMustChangePassword()) {
			lgr.warning("Agent: " + agent.getAgentId()
					+ " must change password to persue further");
			ppResult.setFailureReason("ForcedPasswordChange");
			ppResult.setStatus(true);
			return false;
		}

		String profileId = agent.getProfileId();
		PasswordPolicy pp = null;
		String ppId = apHandler.getAgentProfilePwdPolicyId(profileId);
		if (ppId != null && !ppId.equals("0")) {
			pp = getPasswordPolicy(ppId);
		} else {
			lgr.warning("No password policy assigned to agent profile id: "
					+ profileId);
			
			//FIXME:: For the time being ignore this case and try applying
			//default pwd policy
			pp = getDefaultPasswordPolicyForTenant(tenantId);
//			ppResult.setFailureReason("InternalPwdPolicyError");
//          send trap
//			return false;						
		}

		if (pp != null) {
			if (lgr.isLoggable(Level.INFO))
				lgr.info("Password policy-id: " + pp.getId()
						+ " is assigned to " + "agent: " + agent.getAgentId());

			// Second, check if password policy is enforced i.e. pwd expire
			// period is non-zero
			// proceed further only if its set

			if (0 == pp.getPwdExpirePeriod()) {
				if (lgr.isLoggable(Level.INFO))
					lgr.info("Inactive Password policy-id: " + pp.getId()
							+ " is assigned to agent: " + agent.getAgentId());
				ppResult.setStatus(false);
				ppResult.setId(pp.getId());
				return true;
			}

			// Here means, password policy is set for agent

			// Third
			if (0 == agent.getPasswordLastModifiedTimestamp()) {
				if (lgr.isLoggable(Level.INFO))
					lgr.info("No password modified timestamp stored "
							+ "for agent: " + agent.getAgentId());
				
				ppResult.setStatus(true);
				ppResult.setPwdRemainingDays(pp.getPwdExpirePeriod());
				ppResult.setId(pp.getId());
				return true;
			}

			// Fourth
			int remainingDays = pp.getPwdExpirePeriod();
			long modMilli = System.currentTimeMillis()
					- agent.getPasswordLastModifiedTimestamp();
			if (modMilli > 0) {
				int modifyDays = (int) (modMilli / 86400000);// 1000*60*60*24				
				remainingDays = pp.getPwdExpirePeriod() - modifyDays;
				if (remainingDays < 0) {					
					ppResult.setFailureReason("passwordExpired");
					ppResult.setStatus(true);
					ppResult.setId(pp.getId());
					lgr.warning("Agent id:" + agent.getAgentId() + " name: "
					              + agent.getAgentName() + " password has expired, it was" +
					              		"last modifed " + modifyDays + "days back");
					return false;
				} else {
					if (lgr.isLoggable(Level.FINER))
						lgr.finer("Password for agent:" + agent.getAgentId() + " has not been modified " +
						"since" + modifyDays + " days, its valid for another " + remainingDays + " days" );
				}
			}		
			ppResult.setStatus(true);
			ppResult.setPwdRemainingDays(remainingDays);
			ppResult.setId(pp.getId());
			return true;
			
		} else {
			lgr.severe("Neither password policy assigned to agent: " + agent.getAgentId() +
					" nor default password policy exists for tenant: " + tenantId);
			ppResult.setFailureReason("InternalPwdPolicyError");
			ppResult.setStatus(false);
				try {
					TrapManager trapMgr = (TrapManager) AgentInformationManagerSystem
							.findBean("trapManager");
					trapMgr.sendAimPasswordPolicyNotProvisioned(tenantId,
							agent.getAgentId(), "Missing password policy");
				} catch (Exception e) {
					lgr.log(Level.SEVERE, "Error while getting the trap manager", e);
				}
			return false;
		}
	}

}
