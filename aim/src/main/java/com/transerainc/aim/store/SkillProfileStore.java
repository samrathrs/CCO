/**
 * 
 */
package com.transerainc.aim.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.SkillProfile;
import com.transerainc.aim.util.StoreUtil;

/**
 * @author Rajpal Dangi
 * 
 */

public class SkillProfileStore extends BaseStore {
	private static Logger lgr = Logger.getLogger(SkillProfileStore.class
			.getName());

	private String dataDir;

	public SkillProfileStore(String dataDir) {
		this.dataDir = dataDir;
	}

	public void storeSkillProfiles(HashMap<String, SkillProfile> skillProfileMap) {
		String file = dataDir + "/skill-profiles.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing agent skill profiles into file " + file);
			}

			writeToFile(skillProfileMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
					"Error while storing agent skill profiles to disk.", e);
			trapManager.sendStorageError(file, this.getClass().getName(),
					"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, SkillProfile> retrieveSkillProfiles() {
		HashMap<String, SkillProfile> theMap = null;
		String filename = dataDir + "/skill-profiles.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving agent skill profile data from file "
						+ filename);
			}

			theMap = (HashMap<String, SkillProfile>) readFromFile(filename);
		} catch (Exception e) {
			lgr
					.log(
							Level.SEVERE,
							"Error while retrieving agent skill profiles from local disk",
							e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
					"read");
		}
		return theMap;
	}

	public void storeAgentTeamProfile(
			HashMap<String, HashMap<String, String>> agentTeamProfileMap) {
		String file = dataDir + "/agent-team-skill-profile-mapping.dat";
		try {
			StoreUtil.checkFolder(dataDir);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Storing agent team skill profile into file " + file);
			}

			writeToFile(agentTeamProfileMap, file);
		} catch (IOException e) {
			lgr.log(Level.SEVERE,
					"Error while storing agent team skill profile to disk.", e);
			trapManager.sendStorageError(file, this.getClass().getName(),
					"write");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, HashMap<String, String>> retrieveAgentTeamProfile() {
		HashMap<String, HashMap<String, String>> theMap = null;
		String filename = dataDir + "/agent-team-skill-profile-mapping.dat";

		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Retrieving agent team skill profile data from file "
						+ filename);
			}

			theMap = (HashMap<String, HashMap<String, String>>) readFromFile(filename);
		} catch (Exception e) {
			lgr
					.log(
							Level.SEVERE,
							"Error while retrieving agent team skill profile from local disk",
							e);

			// Raise a trap here
			trapManager.sendStorageError(filename, this.getClass().getName(),
					"read");
		}
		return theMap;
	}

}
