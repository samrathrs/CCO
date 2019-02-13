package com.transerainc.aim.provisioning.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.AgentProfile;
import com.transerainc.aim.pojo.ThirdPartyVendor;
import com.transerainc.aim.store.ThirdPartyVendorsStore;

public class ThirdPartyVendorHandler {
	private static Logger lgr = Logger.getLogger(ThirdPartyVendorHandler.class
			.getName());

	HashMap<String, ThirdPartyVendor> vendorMap;

	public ThirdPartyVendorHandler(String dataDir) {
		ThirdPartyVendorsStore tpvStore = new ThirdPartyVendorsStore(dataDir);
		vendorMap = tpvStore.retrieveThirdPartyVendors();
	}

	public ThirdPartyVendorHandler(HashMap<String, ThirdPartyVendor> vendorMap,
			String dataDir) {
		this.vendorMap = vendorMap;

		ThirdPartyVendorsStore tpvStore = new ThirdPartyVendorsStore(dataDir);
		tpvStore.storeThirdPartyVendors(this.vendorMap);
	}
	
	public ThirdPartyVendor getThirdPartyVendor(String vendorId) {
		ThirdPartyVendor vendor = this.vendorMap.get(vendorId);
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for third party vendor " + vendorId
					+ " returned - " + vendor);
		}
		return vendor;
	}
	
	public List<ThirdPartyVendor> getThirdPartyVendorForTenant(String tenantId) {
		List<ThirdPartyVendor> vndrList = new ArrayList<ThirdPartyVendor>();		
		for(String key: vendorMap.keySet()){
			ThirdPartyVendor vdr = vendorMap.get(key);
			if(vdr.getTenantId().equals(tenantId))
				vndrList.add(vdr);
		}
		return vndrList;
	}
	
}
