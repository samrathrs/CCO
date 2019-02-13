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

import com.transerainc.aim.pojo.VirtualTeam;
import com.transerainc.aim.store.VirtualTeamStore;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class VirtualTeamHandler {
	private static Logger lgr =
			Logger.getLogger(VirtualTeamHandler.class.getName());
	private HashMap<String, VirtualTeam> vteamMap;
	private HashMap<String, List<String>> tenantToVirtualTeamMap;

	public VirtualTeamHandler(String dataDir) {
		VirtualTeamStore store = new VirtualTeamStore(dataDir);
		vteamMap = store.retrieveVirtualTeams();

		createTenantToVirtualTeamMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To VirtualTeam Mapping: "
				+ tenantToVirtualTeamMap);
			lgr.config("VirtualTeams: " + vteamMap);
		}
	}

	public VirtualTeamHandler(HashMap<String, VirtualTeam> vteamMap,
			String dataDir) {
		this.vteamMap = vteamMap;

		createTenantToVirtualTeamMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To VirtualTeam Mapping: "
				+ tenantToVirtualTeamMap);
			lgr.config("VirtualTeams: " + vteamMap);
		}

		VirtualTeamStore store = new VirtualTeamStore(dataDir);
		store.storeVirtualTeams(vteamMap);
	}

	public VirtualTeam getVirtualTeam(String id) {
		VirtualTeam vteam = vteamMap.get(id);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for VirtualTeam " + id + " returned - " + vteam);
		}

		return vteam;
	}

	public Collection<String> getVirtualTeamIds() {
		Collection<String> keys = vteamMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for VirtualTeam ids returned - " + keys);
		}

		return keys;
	}

	public List<String> getVirtualTeamIdsForTenant(String tenantId) {
		List<String> idList = tenantToVirtualTeamMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following VirtualTeam ids - " + idList
				+ ", from the map " + tenantToVirtualTeamMap);
		}

		return idList;
	}

	public List<VirtualTeam> getVirtualTeamsForTenant(String tenantId) {
		List<String> idList = getVirtualTeamIdsForTenant(tenantId);
		List<VirtualTeam> vteamList = new ArrayList<VirtualTeam>();

		if (idList != null) {
			for (String id : idList) {
				VirtualTeam vteam = vteamMap.get(id);

				vteamList.add(vteam);
			}
		} else {
			lgr.warning("No VirtualTeams found for tenant " + tenantId);
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following VirtualTeams - " + vteamList);
		}

		return vteamList;
	}

	/**
	 * 
	 */
	private void createTenantToVirtualTeamMapping() {
		this.tenantToVirtualTeamMap = new HashMap<String, List<String>>();

		for (String vtId : vteamMap.keySet()) {
			VirtualTeam vteam = vteamMap.get(vtId);
			String tenantId = vteam.getTenantId();
			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Adding VirtualTeam " + vtId + " to tenant "
					+ tenantId);
			}

			List<String> vteamIdList = tenantToVirtualTeamMap.get(tenantId);
			if (tenantToVirtualTeamMap.containsKey(tenantId)) {
				vteamIdList = tenantToVirtualTeamMap.get(tenantId);
			} else {
				vteamIdList = new ArrayList<String>();
				tenantToVirtualTeamMap.put(tenantId, vteamIdList);
			}

			vteamIdList.add(vtId);
		}
	}
}
