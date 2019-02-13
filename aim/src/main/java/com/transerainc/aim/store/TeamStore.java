/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.Team;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class TeamStore extends BaseStore {
	private static Logger lgr = Logger.getLogger(TeamStore.class.getName());
	private String dataDir;

	public TeamStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeTeams(HashMap<String, Team> theMap) {
		String file = dataDir + "/teams.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing teams into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE, "Error while storing teams to disk.", e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Team> retrieveTeams() {
		HashMap<String, Team> theMap = null;
		String filename = dataDir + "/teams.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving team data from file " + filename);
			}

			theMap = (HashMap<String, Team>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving teams from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}
}
