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

import com.transerainc.aim.pojo.AuxilaryCode;
import com.transerainc.aim.store.WrapupStore;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class WrapupHandler {
	private static Logger lgr = Logger.getLogger(WrapupHandler.class.getName());
	private HashMap<String, AuxilaryCode> wrapupMap;
	private HashMap<String, List<String>> tenantToAuxCodeMap;

	public WrapupHandler(String dataDir) {
		WrapupStore wStore = new WrapupStore(dataDir);

		wrapupMap = wStore.retrieveWrapups();

		createTenantToAuxCodeMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To Auxilary Code: " + tenantToAuxCodeMap);
		}
	}

	public WrapupHandler(HashMap<String, AuxilaryCode> auxCodeMap,
			String dataDir) {
		this.wrapupMap = new HashMap<String, AuxilaryCode>();
		for (String codeId : auxCodeMap.keySet()) {
			AuxilaryCode aCode = auxCodeMap.get(codeId);
			if ("wrapUp".equals(aCode.getCodeType())) {
				wrapupMap.put(codeId, aCode);
			}
		}

		createTenantToAuxCodeMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To Aux Code Mapping: " + tenantToAuxCodeMap);
		}

		WrapupStore wStore = new WrapupStore(dataDir);
		wStore.storeWrapups(wrapupMap);
	}

	public AuxilaryCode getWrapupCode(String id) {
		AuxilaryCode code = wrapupMap.get(id);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for Wrapup Code " + id
				+ " returned the following - " + code);
		}

		return code;
	}

	public Collection<String> getWrapupIds() {
		Collection<String> keys = wrapupMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for wrapup ids returned - " + keys);
		}

		return keys;
	}

	public List<String> getWrapupIdsForTenant(String tenantId) {
		List<String> idList = tenantToAuxCodeMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following wrapup ids - " + idList);
		}

		return idList;
	}

	public List<AuxilaryCode> getWrapupsForTenant(String tenantId) {
		List<String> idList = getWrapupIdsForTenant(tenantId);

		List<AuxilaryCode> wrapupList = new ArrayList<AuxilaryCode>();
		if (idList != null) {
			for (String id : idList) {
				AuxilaryCode code = wrapupMap.get(id);

				wrapupList.add(code);
			}
		} else {
			lgr.warning("Cannot find wrapups for tenant " + tenantId);
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following wrapups - " + wrapupList);
		}

		return wrapupList;
	}

	/**
	 * 
	 */
	private void createTenantToAuxCodeMapping() {
		this.tenantToAuxCodeMap = new HashMap<String, List<String>>();
		for (String codeId : wrapupMap.keySet()) {
			AuxilaryCode code = wrapupMap.get(codeId);
			String tenantId = code.getTenantId();
			if (lgr.isLoggable(Level.FINEST)) {
				lgr
						.finest("Adding wrapup " + codeId + " to tenant "
							+ tenantId);
			}

			List<String> wrapupIdList = null;
			if (tenantToAuxCodeMap.containsKey(tenantId)) {
				wrapupIdList = tenantToAuxCodeMap.get(tenantId);
			} else {
				wrapupIdList = new ArrayList<String>();
				tenantToAuxCodeMap.put(tenantId, wrapupIdList);
			}

			wrapupIdList.add(codeId);
		}
	}

}
