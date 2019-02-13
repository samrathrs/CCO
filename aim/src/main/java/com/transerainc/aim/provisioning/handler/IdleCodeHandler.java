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
import com.transerainc.aim.store.IdleCodeStore;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class IdleCodeHandler {
	private static Logger lgr =
			Logger.getLogger(IdleCodeHandler.class.getName());
	private HashMap<String, AuxilaryCode> idleCodeMap;
	private HashMap<String, List<String>> tenantToAuxCodeMap;

	public IdleCodeHandler(String dataDir) {
		IdleCodeStore icStore = new IdleCodeStore(dataDir);

		idleCodeMap = icStore.retrieveIdleCodes();

		createTenantToAuxCodeMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To Auxilary Code: " + tenantToAuxCodeMap);
		}
	}

	public IdleCodeHandler(HashMap<String, AuxilaryCode> auxCodeMap,
			String dataDir) {
		this.idleCodeMap = new HashMap<String, AuxilaryCode>();
		for (String codeId : auxCodeMap.keySet()) {
			AuxilaryCode aCode = auxCodeMap.get(codeId);
			if ("idle".equals(aCode.getCodeType())) {
				idleCodeMap.put(codeId, aCode);
			}
		}

		createTenantToAuxCodeMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To Aux Code Mapping: " + tenantToAuxCodeMap);
		}

		IdleCodeStore icStore = new IdleCodeStore(dataDir);
		icStore.storeIdleCodes(idleCodeMap);
	}

	public AuxilaryCode getIdleCode(String id) {
		AuxilaryCode code = idleCodeMap.get(id);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for Idle Code " + id
				+ " returned the following - " + code);
		}

		return code;
	}

	public Collection<String> getIdleCodeIds() {
		Collection<String> keys = idleCodeMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for IdleCode ids returned - " + keys);
		}

		return keys;
	}

	public List<String> getIdleCodeIdsForTenant(String tenantId) {
		List<String> idList = tenantToAuxCodeMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following IdleCode ids - " + idList);
		}

		return idList;
	}

	public List<AuxilaryCode> getIdleCodesForTenant(String tenantId) {
		List<String> idList = getIdleCodeIdsForTenant(tenantId);

		List<AuxilaryCode> icList = new ArrayList<AuxilaryCode>();
		if (idList != null) {
			for (String id : idList) {
				AuxilaryCode code = idleCodeMap.get(id);

				icList.add(code);
			}
		} else {
			lgr.warning("Cannot find IdleCodes for tenant " + tenantId);
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following IdleCodes - " + icList);
		}

		return icList;
	}

	/**
	 * 
	 */
	private void createTenantToAuxCodeMapping() {
		this.tenantToAuxCodeMap = new HashMap<String, List<String>>();
		for (String codeId : idleCodeMap.keySet()) {
			AuxilaryCode code = idleCodeMap.get(codeId);
			String tenantId = code.getTenantId();
			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Adding IdleCode " + codeId + " to tenant "
					+ tenantId);
			}

			List<String> idleIdList = null;
			if (tenantToAuxCodeMap.containsKey(tenantId)) {
				idleIdList = tenantToAuxCodeMap.get(tenantId);
			} else {
				idleIdList = new ArrayList<String>();
				tenantToAuxCodeMap.put(tenantId, idleIdList);
			}

			idleIdList.add(codeId);
		}
	}

}
