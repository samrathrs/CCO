/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.SpeedDialList;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class SpeedDialListStore extends BaseStore {
	private static Logger lgr =
			Logger.getLogger(SpeedDialListStore.class.getName());
	private String dataDir;

	public SpeedDialListStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeSpeedDialLists(HashMap<String, SpeedDialList> theMap) {
		String file = dataDir + "/speed-dial-lists.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing speed dial lists into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
				"Error while storing speed dial list to disk.", e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, SpeedDialList> retrieveSpeedDialLists() {
		HashMap<String, SpeedDialList> theMap = null;
		String filename = dataDir + "/speed-dial-lists.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving speed dial list data from file "
					+ filename);
			}

			theMap = (HashMap<String, SpeedDialList>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving speed dial lists from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}
}
