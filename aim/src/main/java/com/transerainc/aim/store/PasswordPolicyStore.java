/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.PasswordPolicy;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class PasswordPolicyStore extends BaseStore {
	private static Logger lgr =
			Logger.getLogger(PasswordPolicyStore.class.getName());
	private String dataDir;

	public PasswordPolicyStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storePasswordPolicies(
			HashMap<String, PasswordPolicy> passwordPolicyMap) {
		String file = dataDir + "/password-policy.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing password policies into file " + file);
			}

			writeToFile(passwordPolicyMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
				"Error while storing password policies to disk.", e);

			// Raise a trap here
			// storageError(filename, datatype, "write");
			trapManager.sendStorageError(file, this.getClass().getName(),
				"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, PasswordPolicy> retrievePolicies() {
		HashMap<String, PasswordPolicy> theMap = null;
		String filename = dataDir + "/password-policy.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving password policy data from file "
					+ filename);
			}

			theMap = (HashMap<String, PasswordPolicy>) readFromFile(filename);
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while retrieving password policies from local disk", e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
				"read");
		}

		return theMap;
	}
}
