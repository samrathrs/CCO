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

import com.transerainc.aim.pojo.Team;
import com.transerainc.aim.store.TeamStore;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class TeamHandler {
	private static Logger lgr = Logger.getLogger(TeamHandler.class.getName());
	private HashMap<String, Team> teamMap;
	private HashMap<String, List<String>> tenantToTeamMap;

	public TeamHandler(String dataDir) {
		TeamStore store = new TeamStore(dataDir);
		teamMap = store.retrieveTeams();

		createTenantToTeamMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To Team Mapping: " + tenantToTeamMap);
			lgr.config("Teams: " + teamMap);
		}
	}

	public TeamHandler(HashMap<String, Team> teamMap, String dataDir) {
		this.teamMap = teamMap;

		createTenantToTeamMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To Team Mapping: " + tenantToTeamMap);
			lgr.config("Teams: " + teamMap);
		}

		TeamStore store = new TeamStore(dataDir);
		store.storeTeams(teamMap);
	}

	public Team getTeam(String id) {
		Team team = teamMap.get(id);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for team " + id + " returned - " + team);
		}

		return team;
	}

	public Collection<String> getTeamIds() {
		Collection<String> keys = teamMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for team ids returned - " + keys);
		}

		return keys;
	}

	public List<String> getTeamIdsForTenant(String tenantId) {
		List<String> idList = tenantToTeamMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following team ids - " + idList
				+ ", from the map " + tenantToTeamMap);
		}

		return idList;
	}

	public List<Team> getTeamsForTenant(String tenantId) {
		List<String> idList = getTeamIdsForTenant(tenantId);
		List<Team> teamList = new ArrayList<Team>();

		if (idList != null) {
			for (String id : idList) {
				Team team = teamMap.get(id);

				teamList.add(team);
			}
		} else {
			lgr.warning("No teams found for tenant " + tenantId);
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following teams - " + teamList);
		}

		return teamList;
	}

	/**
	 * 
	 */
	private void createTenantToTeamMapping() {
		this.tenantToTeamMap = new HashMap<String, List<String>>();

		for (String teamId : teamMap.keySet()) {
			Team team = teamMap.get(teamId);
			String tenantId = team.getTenantId();
			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Adding team " + teamId + " to tenant " + tenantId);
			}

			List<String> teamIdList = tenantToTeamMap.get(tenantId);
			if (tenantToTeamMap.containsKey(tenantId)) {
				teamIdList = tenantToTeamMap.get(tenantId);
			} else {
				teamIdList = new ArrayList<String>();
				tenantToTeamMap.put(tenantId, teamIdList);
			}

			teamIdList.add(teamId);
		}
	}
}
