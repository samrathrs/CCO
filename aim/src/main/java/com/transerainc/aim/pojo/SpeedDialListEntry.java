/**
 * 
 */
package com.transerainc.aim.pojo;

import java.io.Serializable;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class SpeedDialListEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String entryId;
	private String number;

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
	 * @return the entryId
	 */
	public String getEntryId() {
		return entryId;
	}

	/**
	 * @param entryId
	 *            the entryId to set
	 */
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	public String toString() {
		return "Name: " + name + ", Entry Id: " + entryId + ", Number: "
			+ number;
	}

}
