/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.Tenant;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class TenantStore extends BaseStore {
	private static Logger lgr = Logger.getLogger(TenantStore.class.getName());
	private String dataDir;

	public TenantStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeTenants(HashMap<String, Tenant> theMap) {
		String file = dataDir + "/tenants.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing tenants into file " + file);
			}

			writeToFile(theMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE, "Error while storing tenants to disk.", e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Tenant> retrieveTenants() {
		HashMap<String, Tenant> theMap = null;
		String filename = dataDir + "/tenants.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving tenant data from file " + filename);
			}

			theMap = (HashMap<String, Tenant>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving tenants from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}
}
