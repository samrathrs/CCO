/**
 * 
 */
package com.transerainc.aim.provisioning.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.SkillProfile;
import com.transerainc.aim.store.SkillProfileStore;

/**
 * @author Rajpal Dangi
 * 
 */
public class SkillProfileHandler {
	
	private static Logger lgr =
			Logger.getLogger(SkillProfileHandler.class.getName());

	private HashMap<String, SkillProfile> skillProfileMap;
	private HashMap<String, HashMap<String, String>> agentTeamProfileMap;
	private String dataDir;
	
	public SkillProfileHandler(String dataDir) {
		this.dataDir = dataDir;
		SkillProfileStore apStore = new SkillProfileStore(dataDir);
		skillProfileMap = apStore.retrieveSkillProfiles();
		agentTeamProfileMap = apStore.retrieveAgentTeamProfile();
	}

	public SkillProfileHandler(HashMap<String, SkillProfile> skillProfileMap,
			HashMap<String, HashMap<String, String>> agentTeamProfileMap,
			String dataDir) {
		this.dataDir = dataDir;
		this.skillProfileMap = skillProfileMap;
		this.agentTeamProfileMap = agentTeamProfileMap == null ? new HashMap<String, HashMap<String, String>>()
				: agentTeamProfileMap;
		SkillProfileStore apStore = new SkillProfileStore(dataDir);
		apStore.storeSkillProfiles(this.skillProfileMap);
		apStore.storeAgentTeamProfile(this.agentTeamProfileMap);
	}
	
	public void updateAgentTeamSkillProfileIdMap(
			HashMap<String, HashMap<String, String>> agentTeamProfileMap) {
		this.agentTeamProfileMap = agentTeamProfileMap == null ? new HashMap<String, HashMap<String, String>>()
				: agentTeamProfileMap;
		SkillProfileStore apStore = new SkillProfileStore(dataDir);
		apStore.storeAgentTeamProfile(this.agentTeamProfileMap);
	}

	public SkillProfile getSkillProfile(String profileId) {
		SkillProfile skillProfile = skillProfileMap.get(profileId);
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for agent skill profile " + profileId
				+ " returned - " + skillProfile);
		}
		return skillProfile;
	}

	public SkillProfile getSkillProfile(String agentId, String teamId) {
		SkillProfile profile = null;
		Map<String, String> teamProfileMap = agentTeamProfileMap != null ? agentTeamProfileMap
				.get(agentId)
				: null;
		if (null != teamProfileMap && teamProfileMap.containsKey(teamId)) {
			profile = skillProfileMap.get(teamProfileMap.get(teamId));
		}
		return profile;
	}

	public String getSkillProfileId(String agentId, String teamId){
		SkillProfile profile = getSkillProfile(agentId, teamId);
		return null != profile ? profile.getProfileId() : "";
	}
	
	public String getSkillProfileIdName(String agentId, String teamId) {
		SkillProfile profile = getSkillProfile(agentId, teamId);
		return null != profile ? "(" + profile.getProfileId() + ")"
				+ profile.getProfileName() : "";
	}
	
	public List<SkillProfile> getSkillProfilesForTenant(String tenantId) {
		List<SkillProfile> profileList = new ArrayList<SkillProfile>();

		for (SkillProfile profile : skillProfileMap.values()) {
			if (profile.getTenantId().equals(tenantId)) {
				profileList.add(profile);
			}
		}

		if(!profileList.isEmpty()){
			Collections.sort(profileList);
		}
		
		if (lgr.isLoggable(Level.FINE)) {
			lgr.fine("Lookup for skill profiles for tenant :" + tenantId
				+ " returning - " + profileList.size() + " skill profiles");
		}

		return profileList;
	}
	
	public Set<String> getAgentsOfSkillProfile(String profileId) {
		Set<String> agentIdSet = new HashSet<String>();
		for (Map.Entry<String, HashMap<String, String>> entry : this.agentTeamProfileMap
				.entrySet()) {
			if (entry.getValue() != null
					&& entry.getValue().containsValue(profileId)) {
				agentIdSet.add(entry.getKey());
			}
		}
		return agentIdSet;
	}

	// This is used by admin gui
	public HashMap<String, HashMap<String, String>> getProfileAssignmentMap(
			String tenantId) {
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		if (this.agentTeamProfileMap != null) {
			for (Map.Entry<String, HashMap<String, String>> entry : agentTeamProfileMap
					.entrySet()) {
				Map<String, String> teamProfileMap = entry.getValue();
				if (teamProfileMap != null) {
					HashMap<String, String> entityMap = new HashMap<String, String>();
					for (Map.Entry<String, String> teamEntry : teamProfileMap
							.entrySet()) {
						SkillProfile profile = skillProfileMap.get(teamEntry
								.getValue());
						if (profile != null
								&& profile.getTenantId().equals(tenantId)) {
							entityMap.put(teamEntry.getKey(), teamEntry
									.getValue());
						}
					}
					map.put(entry.getKey(), entityMap);
				}
			}
		}
		return map;
	}
}
