/**
 * 
 */
package com.transerainc.aim.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class StoreUtil {
	private static Logger lgr = Logger.getLogger(StoreUtil.class.getName());

	public static void checkFolder(String baseFolder) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Checking for folder existance " + baseFolder);
		}

		File f = new File(baseFolder);

		if (f.exists() && f.isFile()) {
			lgr.warning("Folder " + baseFolder
				+ " exists but is not a folder. Deleting it.");
			f.delete();

			f = new File(baseFolder);
		}

		if (!f.exists()) {
			lgr.info("Creating the folder " + baseFolder);
			f.mkdir();
		}
	}
}
