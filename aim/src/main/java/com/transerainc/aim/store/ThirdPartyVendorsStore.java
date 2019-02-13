package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.pojo.ThirdPartyVendor;
import com.transerainc.aim.util.StoreUtil;

public class ThirdPartyVendorsStore extends BaseStore{
	private static Logger lgr = Logger.getLogger(ThirdPartyVendorsStore.class.getName());
	
	private String dataDir;
	
	public ThirdPartyVendorsStore(String dataDir){
		this.dataDir = dataDir;
	}

	public void storeThirdPartyVendors(HashMap<String, ThirdPartyVendor> theMap) {
		String file = dataDir + "/thirdpartyvendors.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing third party vendors into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE, "Error while storing third party vendors to disk.", e);

			// Raise a trap here
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, ThirdPartyVendor> retrieveThirdPartyVendors() {
		HashMap<String, ThirdPartyVendor> theMap = null;
		String filename = dataDir + "/thirdpartyvendors.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving ThirdPartyVendors data from file " + filename);
			}

			theMap = (HashMap<String, ThirdPartyVendor>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving ThirdPartyVendors from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}
}
