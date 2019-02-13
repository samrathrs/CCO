/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.AgentTeamMapping;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AgentToTeamMappingStore extends BaseStore {
	private static Logger lgr =
			Logger.getLogger(AgentToTeamMappingStore.class.getName());
	private String dataDir;

	public AgentToTeamMappingStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeAgentToTeamMappings(
			HashMap<String, List<AgentTeamMapping>> theMap) {
		String file = dataDir + "/agent-to-team-mappings.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing agent to team mappings into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
				"Error while storing agent to team mappings to disk.", e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, List<AgentTeamMapping>> retrieveAgentToTeamMappings() {
		HashMap<String, List<AgentTeamMapping>> theMap = null;
		String filename = dataDir + "/agent-to-team-mappings.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving agent to team mapping data from file "
					+ filename);
			}

			theMap =
					(HashMap<String, List<AgentTeamMapping>>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while retrieving agent to "
				+ "team mappings from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}

}
