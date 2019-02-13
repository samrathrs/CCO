/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.MediaProfile;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author Rajpal Dangi
 * 
 */
public class AgentMediaProfileStore extends BaseStore {
	private static Logger lgr = Logger.getLogger(AgentMediaProfileStore.class
			.getName());
	private String dataDir;

	public AgentMediaProfileStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeAgentMediaProfiles(
			HashMap<String, MediaProfile> mediaProfileMap) {
		String file = dataDir + "/agent-media-profiles.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing agent media profiles into file " + file);
			}

			writeToFile(mediaProfileMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
					"Error while storing agent media profiles to disk.", e);
			trapManager.sendStorageError(file, this.getClass().getName(),
					"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, MediaProfile> retrieveAgentMediaProfiles() {
		HashMap<String, MediaProfile> theMap = null;
		String filename = dataDir + "/agent-media-profiles.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving agent media profile data from file "
						+ filename);
			}

			theMap = (HashMap<String, MediaProfile>) readFromFile(filename);
		} catch (Exception e) {
			lgr
					.log(
							Level.SEVERE,
							"Error while retrieving agent media profiles from local disk",
							e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
					"read");
		}
		return theMap;
	}

	public void storeEntityMediaProfileMapping(
			HashMap<String, HashMap<String, String>> theMap) {
		String file = dataDir + "/agent-media-profiles-mapping.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing media profile mapping into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
					"Error while storing media profile mapping to disk.", e);
			trapManager.sendStorageError(file, this.getClass().getName(),
					"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, HashMap<String, String>> retrieveEntityMediaProfileMapping() {
		HashMap<String, HashMap<String, String>> theMap = null;
		String filename = dataDir + "/agent-media-profiles-mapping.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr
						.info("Retrieving entity media profile mapping data from file "
								+ filename);
			}

			theMap = (HashMap<String, HashMap<String, String>>) readFromFile(filename);
		} catch (Exception e) {
			lgr
					.log(
							Level.SEVERE,
							"Error while retrieving media profiles mapping from local disk",
							e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
					"read");
		}
		return theMap;
	}
}
