/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.AdhocDialProperty;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AdhocDialPropertiesStore extends BaseStore {
	private static Logger lgr =
			Logger.getLogger(AdhocDialPropertiesStore.class.getName());
	private String dataDir;

	public AdhocDialPropertiesStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeAdhocDialProperties(
			HashMap<String, AdhocDialProperty> adhocDialPropertiesMap) {
		String file = dataDir + "/adhoc-dial-properties.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing adhoc dial properties into file " + file);
			}

			writeToFile(adhocDialPropertiesMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
				"Error while store adhoc dial properties to disk.", e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, AdhocDialProperty> retrieveAdhocDialProperties() {
		HashMap<String, AdhocDialProperty> theMap = null;
		String filename = dataDir + "/adhoc-dial-properties.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving adhoc dial property data from file "
					+ filename);
			}

			theMap =
					(HashMap<String, AdhocDialProperty>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving adhoc dial properties from local disk",
				e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}
}
