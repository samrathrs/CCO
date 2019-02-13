package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.ThirdPartyProfile;
import com.transerainc.aim.pojo.MediaProfile;
import com.transerainc.aim.pojo.ThirdPartyProfileAddInfo;
import com.transerainc.aim.util.StoreUtil;

public class AgentTPProfileStore extends BaseStore {
	private static Logger lgr =
			Logger.getLogger(AgentTPProfileStore.class.getName());
	private String dataDir;
	
	public AgentTPProfileStore(String dataDir){
		this.dataDir = dataDir;
	}
	
	public void storeAgentTPProfiles(
			HashMap<String, ThirdPartyProfile> tpProfileMap) {
		String file = dataDir + "/agent-tp-profiles.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing agent third party profiles into file " + file);
			}

			writeToFile(tpProfileMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
					"Error while storing agent third party profiles to disk.", e);
			trapManager.sendStorageError(file, this.getClass().getName(),
					"write");
		}
	}
	
	public void storeAgentTPProfilesAddInfo(
			HashMap<String, ThirdPartyProfileAddInfo> tpProfileMap) {
		String file = dataDir + "/agent-tp-profiles-add-info.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing agent third party profiles into file " + file);
			}

			writeToFile(tpProfileMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
					"Error while storing agent third party profiles to disk.", e);
			trapManager.sendStorageError(file, this.getClass().getName(),
					"write");
		}
	}
	@SuppressWarnings("unchecked")
	public HashMap<String, ThirdPartyProfile> retrieveAgentTPProfiles() {
		HashMap<String, ThirdPartyProfile> theMap = null;
		String filename = dataDir + "/agent-tp-profiles.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving agent third party profile data from file "
						+ filename);
			}

			theMap = (HashMap<String, ThirdPartyProfile>) readFromFile(filename);
		} catch (Exception e) {
			lgr
					.log(
							Level.SEVERE,
							"Error while retrieving agent third party profiles from local disk",
							e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
					"read");
		}
		return theMap;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, ThirdPartyProfileAddInfo> retrieveAgentTPProfileAddInfo() {
		HashMap<String, ThirdPartyProfileAddInfo> theMap = null;
		String filename = dataDir + "/agent-tp-profiles-add-info.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving agent third party profile data from file "
						+ filename);
			}

			theMap = (HashMap<String, ThirdPartyProfileAddInfo>) readFromFile(filename);
		} catch (Exception e) {
			lgr
					.log(
							Level.SEVERE,
							"Error while retrieving agent third party profiles from local disk",
							e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
					"read");
		}
		return theMap;
	}
	public void storeEntityTPProfileMapping(
			HashMap<String, HashMap<String, String>> theMap) {
		String file = dataDir + "/agent-tp-profiles-mapping.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing third party profile mapping into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
					"Error while storing third party profile mapping to disk.", e);
			trapManager.sendStorageError(file, this.getClass().getName(),
					"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, HashMap<String, String>> retrieveEntityTPProfileMapping() {
		HashMap<String, HashMap<String, String>> theMap = null;
		String filename = dataDir + "/agent-tp-profiles-mapping.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr
						.info("Retrieving entity third party profile mapping data from file "
								+ filename);
			}

			theMap = (HashMap<String, HashMap<String, String>>) readFromFile(filename);
		} catch (Exception e) {
			lgr
					.log(
							Level.SEVERE,
							"Error while retrieving third party profiles mapping from local disk",
							e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
					"read");
		}
		return theMap;
	}
	
}
