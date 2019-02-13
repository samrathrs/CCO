/**
 * 
 */
package com.transerainc.aim.pojo;

import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class VirtualTeam implements Serializable, Comparable<VirtualTeam> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String type;
	private String tenantId;
	private int    seratelAcd;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
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

	public int getSeratelAcd() {
		return seratelAcd;
	}

	public void setSeratelAcd(int seratelAcd) {
		this.seratelAcd = seratelAcd;
	}

	public String toString() {
		return "\nVirtual Team Id: " + id + ", Virtual Team Name: " + name
			+ ", Tenant Id: " + tenantId + ", Seratel Acd: " + seratelAcd;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(VirtualTeam o) {
		Integer id1 = NumberUtils.toInt(this.id, 0);
		Integer id2 = NumberUtils.toInt(o.id, 0);
		return id1.compareTo(id2);
	}
}
