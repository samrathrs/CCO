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

import javax.xml.bind.JAXBException;

import com.transerainc.aim.pojo.AgentProfile;
import com.transerainc.aim.store.AgentProfileStore;
import com.transerainc.aim.util.JAXBHelper;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AgentProfileHandler {
	private static Logger lgr = Logger.getLogger(AgentProfileHandler.class
			.getName());
	private HashMap<String, AgentProfile> profileMap;
	private HashMap<String, List<String>> tenantToProfileMap;
	private HashMap<String, String> agentProfileToPwdPolicyIdMap;

	public AgentProfileHandler(String dataDir) {
		AgentProfileStore apStore = new AgentProfileStore(dataDir);

		profileMap = apStore.retrieveAgentProfiles();

		createTenantToProfileMapping();
		createAgentProfileToPwdPolicyIdMapping();
	}

	public AgentProfileHandler(HashMap<String, AgentProfile> profileMap,
			String dataDir) {
		this.profileMap = profileMap;

		createTenantToProfileMapping();

		AgentProfileStore apStore = new AgentProfileStore(dataDir);
		apStore.storeAgentProfiles(profileMap);
		createAgentProfileToPwdPolicyIdMapping();
	}

	public List<String> getAgentProfileIdsForTenant(String tenantId) {
		List<String> idList = tenantToProfileMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
					+ " returned the following agent profile ids - " + idList);
		}

		return idList;
	}

	public List<AgentProfile> getAgentProfilesForTenant(String tenantId) {
		List<String> idList = getAgentProfileIdsForTenant(tenantId);

		List<AgentProfile> apList = new ArrayList<AgentProfile>();
		if (idList != null) {
			for (String id : idList) {
				AgentProfile profile = profileMap.get(id);

				apList.add(profile);
			}
		} else {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Cannot find agent profiles for tenant " + tenantId);
			}
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
					+ " returned the following agent profiles - " + apList);
		}

		return apList;
	}

	public Collection<String> getAgentProfileIds() {
		Collection<String> keys = profileMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for agent profile ids returned - " + keys);
		}

		return keys;
	}

	public AgentProfile getAgentProfile(String id) {
		AgentProfile profile = profileMap.get(id);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for agent profile " + id + " returned - "
					+ profile);
		}

		return profile;
	}

	/**
	 * 
	 */
	private void createTenantToProfileMapping() {
		this.tenantToProfileMap = new HashMap<String, List<String>>();
		for (String profileId : profileMap.keySet()) {
			AgentProfile profile = profileMap.get(profileId);
			String tenantId = profile.getTenantId();

			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Addint profile " + profileId + " to tenant "
						+ tenantId);
			}

			List<String> profileList = null;
			if (tenantToProfileMap.containsKey(tenantId)) {
				profileList = tenantToProfileMap.get(tenantId);
			} else {
				profileList = new ArrayList<String>();
				tenantToProfileMap.put(tenantId, profileList);
			}

			profileList.add(profileId);
		}
	}

	/**
	 * 
	 */

	private synchronized void createAgentProfileToPwdPolicyIdMapping() {
		this.agentProfileToPwdPolicyIdMap = new HashMap<String, String>();
		for (String profileId : profileMap.keySet()) {
			com.transerainc.aim.pojo.AgentProfile ap = profileMap.get(profileId);
			try {
				com.transerainc.agent.profile.AgentProfile apText = JAXBHelper
						.getAgentProfile(ap.getProfileText());
				List<com.transerainc.agent.profile.ProfileAttribute> 
				                            apAttList = apText.getProfileAttribute();

				for (com.transerainc.agent.profile.ProfileAttribute attribute : apAttList) 
				{
					if ("passwordPolicy".equals(attribute.getName())) {
						String ppId = attribute.getValue();
						if (lgr.isLoggable(Level.INFO)) {
							lgr.info("Received password policy id: " + ppId + " prof-id:" + profileId);
						}					
						agentProfileToPwdPolicyIdMap.put(profileId, ppId);
						break;
					}
				}
			} catch (JAXBException jxe) {
                          lgr.log(Level.WARNING,
                                  "Exception while reading password policy id from agent profile", jxe);

			}
			 
		}//for

	}

	public  synchronized String getAgentProfilePwdPolicyId(String profileId) {
		return agentProfileToPwdPolicyIdMap.get(profileId);
	}

}
