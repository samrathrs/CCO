/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.AuxilaryCode;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class WrapupStore extends BaseStore {
	private static Logger lgr = Logger.getLogger(WrapupStore.class.getName());
	private String dataDir;

	public WrapupStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeWrapups(HashMap<String, AuxilaryCode> theMap) {
		String file = dataDir + "/wrapups.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing wrapups into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE, "Error while storing wrapups to disk.", e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, AuxilaryCode> retrieveWrapups() {
		HashMap<String, AuxilaryCode> theMap = null;
		String filename = dataDir + "/wrapups.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving wrapup data from file " + filename);
			}

			theMap = (HashMap<String, AuxilaryCode>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving wrapups from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}
}
