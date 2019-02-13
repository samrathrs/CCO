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

import com.transerainc.aim.pojo.AdhocDialProperty;
import com.transerainc.aim.store.AdhocDialPropertiesStore;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AdhocDialPropetiesHandler {
	private static Logger lgr =
			Logger.getLogger(AdhocDialPropetiesHandler.class.getName());
	private HashMap<String, AdhocDialProperty> adhocDialPropertiesMap;
	private HashMap<String, List<String>> tenantToAdhocDialPropertiesMap;

	public AdhocDialPropetiesHandler(String dataDir) {
		// If this constructor is invoked, load the data from local store

		AdhocDialPropertiesStore adpStore =
				new AdhocDialPropertiesStore(dataDir);

		adhocDialPropertiesMap = adpStore.retrieveAdhocDialProperties();

		createTenantToADPMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant to Agent Dial Properties Mapping "
				+ tenantToAdhocDialPropertiesMap);
		}

	}

	public AdhocDialPropetiesHandler(HashMap<String, AdhocDialProperty> adpMap,
			String dataDir) {
		this.adhocDialPropertiesMap = adpMap;

		createTenantToADPMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant to Agent Dial Properties Mapping "
				+ tenantToAdhocDialPropertiesMap);
		}

		// Serialize the two maps here.
		AdhocDialPropertiesStore adpStore =
				new AdhocDialPropertiesStore(dataDir);
		adpStore.storeAdhocDialProperties(adhocDialPropertiesMap);
	}

	public AdhocDialProperty getAdhocDialProperty(String id) {
		AdhocDialProperty adp = adhocDialPropertiesMap.get(id);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for adhoc dial property " + id
				+ " returned the following - " + adp);
		}

		return adp;
	}

	public List<String> getAdhocDialPropertyIdsForTenant(String tenantId) {
		List<String> idList = tenantToAdhocDialPropertiesMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following adhoc dial property ids: " + idList);
		}

		return idList;
	}

	public List<AdhocDialProperty> getAdhocDialPropertiesForTenant(
			String tenantId) {
		List<AdhocDialProperty> adpList = new ArrayList<AdhocDialProperty>();

		List<String> idList = getAdhocDialPropertyIdsForTenant(tenantId);

		if (idList != null) {
			for (String id : idList) {
				AdhocDialProperty adp = adhocDialPropertiesMap.get(id);
				adpList.add(adp);
			}
		} else {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("No Adhoc Dial Properties found for tenant "
					+ tenantId);
			}
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following adhoc dial properties - " + adpList);
		}

		return adpList;
	}

	public Collection<String> getAdhocDialPropertyIds() {
		Collection<String> keys = adhocDialPropertiesMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for adhoc dial property keys returned: " + keys);
		}

		return keys;
	}

	/**
	 * 
	 */
	private void createTenantToADPMapping() {
		this.tenantToAdhocDialPropertiesMap =
				new HashMap<String, List<String>>();
		for (String adpId : adhocDialPropertiesMap.keySet()) {
			AdhocDialProperty adp = adhocDialPropertiesMap.get(adpId);
			String tenantId = adp.getTenantId();

			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Adding adhoc dial property " + adpId
					+ " to tenant " + tenantId);
			}

			List<String> adpIdList = null;
			if (tenantToAdhocDialPropertiesMap.containsKey(tenantId)) {
				adpIdList = tenantToAdhocDialPropertiesMap.get(tenantId);
			} else {
				adpIdList = new ArrayList<String>();
				tenantToAdhocDialPropertiesMap.put(tenantId, adpIdList);
			}

			adpIdList.add(adpId);
		}
	}
}
