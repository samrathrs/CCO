/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.AgentProfile;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AgentProfileStore extends BaseStore {
	private static Logger lgr =
			Logger.getLogger(AgentProfileStore.class.getName());
	private String dataDir;

	public AgentProfileStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeAgentProfiles(HashMap<String, AgentProfile> profileMap) {
		String file = dataDir + "/agent-profiles.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing agent profiles into file " + file);
			}

			writeToFile(profileMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
				"Error while storing agent profiles to disk.", e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, AgentProfile> retrieveAgentProfiles() {
		HashMap<String, AgentProfile> theMap = null;
		String filename = dataDir + "/agent-profiles.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving agent profile data from file " + filename);
			}

			theMap = (HashMap<String, AgentProfile>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving agent profiles from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}

}
