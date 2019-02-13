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

import com.transerainc.aim.pojo.SpeedDialList;
import com.transerainc.aim.pojo.SpeedDialListEntry;
import com.transerainc.aim.store.SpeedDialListStore;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class SpeedDialListHandler {
	private static Logger lgr =
			Logger.getLogger(SpeedDialListHandler.class.getName());
	private HashMap<String, SpeedDialList> speedDialListMap;
	private HashMap<String, List<String>> tenantToSpeedDialListMap;
	private HashMap<String, String> entryToListMap;

	public SpeedDialListHandler(String dataDir) {
		SpeedDialListStore store = new SpeedDialListStore(dataDir);
		speedDialListMap = store.retrieveSpeedDialLists();

		createTenantToSpeedDialListMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant to Speed Dial List Mapping: "
				+ tenantToSpeedDialListMap);
		}

		mapEntryIdToList();
	}

	public SpeedDialListHandler(HashMap<String, SpeedDialList> sdlMap,
			String dataDir) {
		this.speedDialListMap = sdlMap;

		createTenantToSpeedDialListMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant to Speed Dial List Mapping: "
				+ tenantToSpeedDialListMap);
		}

		SpeedDialListStore store = new SpeedDialListStore(dataDir);
		store.storeSpeedDialLists(speedDialListMap);

		mapEntryIdToList();
	}

	public SpeedDialList getSpeedDialList(String id) {
		SpeedDialList sdl = speedDialListMap.get(id);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for speed dial list " + id + " returned - " + sdl);
		}

		return sdl;
	}

	public Collection<String> getSpeedDialListIds() {
		Collection<String> keys = speedDialListMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for speed dial list ids returned - " + keys);
		}

		return keys;
	}

	public List<String> getSpeedDialListIdsForTenant(String tenantId) {
		List<String> idList = tenantToSpeedDialListMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following speed dial list ids - " + idList);
		}

		return idList;
	}

	public List<SpeedDialList> getSpeedDialListsForTenant(String tenantId) {
		List<String> idList = getSpeedDialListIdsForTenant(tenantId);

		List<SpeedDialList> sdlList = new ArrayList<SpeedDialList>();
		if (idList != null) {
			for (String id : idList) {
				SpeedDialList sdl = speedDialListMap.get(id);

				sdlList.add(sdl);
			}
		} else {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Could not find any speed dial lists for tenant "
					+ tenantId);
			}
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following speed dial lists - " + sdlList);
		}

		return sdlList;
	}

	/**
	 * 
	 */
	private void createTenantToSpeedDialListMapping() {
		this.tenantToSpeedDialListMap = new HashMap<String, List<String>>();

		for (String sdlId : speedDialListMap.keySet()) {
			Object obj = speedDialListMap.get(sdlId);
			System.out.println("Object Type: " + obj.getClass().getName());

			SpeedDialList sdl = speedDialListMap.get(sdlId);
			String tenantId = sdl.getTenantId();

			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Adding speed dial list " + sdlId + " to tenant "
					+ tenantId);
			}

			List<String> sdlIdList = null;
			if (tenantToSpeedDialListMap.containsKey(tenantId)) {
				sdlIdList = tenantToSpeedDialListMap.get(tenantId);
			} else {
				sdlIdList = new ArrayList<String>();
				tenantToSpeedDialListMap.put(tenantId, sdlIdList);
			}

			sdlIdList.add(sdlId);
		}
	}

	public String getListIdForEntry(String entryId) {
		return entryToListMap.get(entryId);
	}

	/**
	 * 
	 */
	private void mapEntryIdToList() {
		HashMap<String, String> theMap = new HashMap<String, String>();
		for (String sdId : speedDialListMap.keySet()) {
			SpeedDialList sdList = speedDialListMap.get(sdId);
			if (sdList.getSpeedDialListEntryList() != null) {
				for (SpeedDialListEntry sdlEntry : sdList
						.getSpeedDialListEntryList()) {
					if (sdlEntry != null) {
						theMap.put(sdlEntry.getEntryId(), sdId);
					}
				}
			}
		}

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Entry to List mapping: " + theMap);
		}

		entryToListMap = theMap;
	}

	/**
	 * @param entityId
	 * @return
	 */
	public SpeedDialList getSpeedDialListForEntry(String entityId) {
		String sdlId = entryToListMap.get(entityId);
		SpeedDialList theList = null;
		if (sdlId != null) {
			theList = getSpeedDialList(sdlId);
		}

		return theList;
	}
}
