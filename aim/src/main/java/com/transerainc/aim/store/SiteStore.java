/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.Site;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class SiteStore extends BaseStore {
	private static Logger lgr = Logger.getLogger(SiteStore.class.getName());
	private String dataDir;

	public SiteStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeSites(HashMap<String, Site> theMap) {
		String file = dataDir + "/sites.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing sites into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE, "Error while storing sites to disk.", e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Site> retrieveSites() {
		HashMap<String, Site> theMap = null;
		String filename = dataDir + "/sites.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving site data from file " + filename);
			}

			theMap = (HashMap<String, Site>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving sites from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}
}
