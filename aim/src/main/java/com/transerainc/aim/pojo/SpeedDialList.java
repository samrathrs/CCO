/**
 * 
 */
package com.transerainc.aim.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class SpeedDialList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String listId;
	private String tenantId;
	private String parentId;
	private String parentType;
	private List<SpeedDialListEntry> speedDialListEntryList;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the listId
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @param listId
	 *            the listId to set
	 */
	public void setListId(String listId) {
		this.listId = listId;
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
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the parentType
	 */
	public String getParentType() {
		return parentType;
	}

	/**
	 * @param parentType
	 *            the parentType to set
	 */
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	/**
	 * @return the speedDialListEntryList
	 */
	public List<SpeedDialListEntry> getSpeedDialListEntryList() {
		return speedDialListEntryList;
	}

	/**
	 * @param speedDialListEntryList
	 *            the speedDialListEntryList to set
	 */
	public void setSpeedDialListEntryList(
			List<SpeedDialListEntry> speedDialListEntryList) {
		this.speedDialListEntryList = speedDialListEntryList;
	}

	/**
	 * @param sdlEntry
	 */
	public void addEntry(SpeedDialListEntry sdlEntry) {
		if (speedDialListEntryList == null) {
			speedDialListEntryList = new ArrayList<SpeedDialListEntry>();
		}

		speedDialListEntryList.add(sdlEntry);
	}

	public String toString() {
		return "Name: " + name + ", Id: " + listId + ", Tenant Id: " + tenantId
			+ ", Parent Id: " + parentId + ", Parent Type: " + parentType
			+ ", Speed Dial List Entrys: " + speedDialListEntryList;
	}

}
