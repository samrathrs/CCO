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
public class AdhocDialProperty implements Serializable, Comparable<AdhocDialProperty> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String tenantId;
	private String name;
	private String regularExpression;
	private String prefix;
	private String strippedChars;

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
	 * @return the regularExpression
	 */
	public String getRegularExpression() {
		return regularExpression;
	}

	/**
	 * @param regularExpression
	 *            the regularExpression to set
	 */
	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the strippedChars
	 */
	public String getStrippedChars() {
		return strippedChars;
	}

	/**
	 * @param strippedChars
	 *            the strippedChars to set
	 */
	public void setStrippedChars(String strippedChars) {
		this.strippedChars = strippedChars;
	}

	public String toString() {
		return "Id: " + id + ", Tenant Id: " + tenantId + ", Name: " + name
			+ ", Regular Expression: " + regularExpression + ", Prefix: "
			+ prefix + ", Stripped Chars: " + strippedChars;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(AdhocDialProperty o) {
		Integer id1 = NumberUtils.toInt(this.id, 0);
		Integer id2 = NumberUtils.toInt(o.id, 0);
		return id1.compareTo(id2);
	}
}
