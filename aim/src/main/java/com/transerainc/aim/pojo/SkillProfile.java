/**
 * 
 */
package com.transerainc.aim.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class SkillProfile implements Serializable, Comparable<SkillProfile> {
	private static final long serialVersionUID = 1L;
	private String profileId;
	private String profileName;
	private String tenantId;
	private List<Skill> skillList;

	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId
	 *            the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the profileName
	 */
	public String getProfileName() {
		return profileName;
	}

	/**
	 * @param profileName
	 *            the profileName to set
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	/**
	 * @return the skillList
	 */
	public List<Skill> getSkillList() {
		return skillList;
	}

	/**
	 * @param skillMap
	 *            the skillMap to set
	 */
	public void setSkillList(List<Skill> skillList) {
		this.skillList = skillList;
	}

	public void addSkill(Skill skill) {
		if (skillList == null) {
			skillList = new ArrayList<Skill>();
		}

		skillList.add(skill);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SkillProfile [profileId=" + profileId + ", profileName="
				+ profileName + ", tenantId=" + tenantId + ", skillList="
				+ skillList + "]";
	}

	public int compareTo(SkillProfile arg0) {
		Integer id1 = NumberUtils.toInt(this.profileId, 0);
		Integer id2 = NumberUtils.toInt(arg0.profileId, 0);

		return id1.compareTo(id2);
	}
}
