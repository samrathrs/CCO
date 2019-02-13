/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.VirtualTeam;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class VirtualTeamStore extends BaseStore {
	private static Logger lgr =
			Logger.getLogger(VirtualTeamStore.class.getName());
	private String dataDir;

	public VirtualTeamStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeVirtualTeams(HashMap<String, VirtualTeam> theMap) {
		String file = dataDir + "/virtualteams.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing virtualteams into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE, "Error while storing virtualteams to disk.",
				e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, VirtualTeam> retrieveVirtualTeams() {
		HashMap<String, VirtualTeam> theMap = null;
		String filename = dataDir + "/virtualteams.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving virtualteams data from file " + filename);
			}

			theMap = (HashMap<String, VirtualTeam>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving virtualteams from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}
}
