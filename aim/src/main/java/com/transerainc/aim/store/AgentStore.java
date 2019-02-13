/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AgentStore extends BaseStore {
	private static Logger lgr = Logger.getLogger(AgentStore.class.getName());
	private String dataDir;

	public AgentStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeAgents(HashMap<String, Agent> theMap) {
		String file = dataDir + "/agents.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing agents into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE, "Error while storing agents to disk.", e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Agent> retrieveAgents() {
		HashMap<String, Agent> theMap = null;
		String filename = dataDir + "/agents.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving agent data from file " + filename);
			}

			theMap = (HashMap<String, Agent>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving agents from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}
}
