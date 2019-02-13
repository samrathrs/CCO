/**
 * 
 */
package com.transerainc.aim.pojo;

import java.io.Serializable;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class Skill implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private int type;
	private String name;
	private String typeName;
	private String enumSkillName;
	private String enumSkillId;
	private String value;
	private String description;

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
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
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
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName
	 *            the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the enumSkillName
	 */
	public String getEnumSkillName() {
		return enumSkillName;
	}

	/**
	 * @param enumSkillName
	 *            the enumSkillName to set
	 */
	public void setEnumSkillName(String enumSkillName) {
		this.enumSkillName = enumSkillName;
	}

	/**
	 * @return the enumSkillId
	 */
	public String getEnumSkillId() {
		return enumSkillId;
	}

	/**
	 * @param enumSkillId
	 *            the enumSkillId to set
	 */
	public void setEnumSkillId(String enumSkillId) {
		this.enumSkillId = enumSkillId;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Skill [description=" + description + ", enumSkillId="
			+ enumSkillId + ", enumSkillName=" + enumSkillName + ", id=" + id
			+ ", name=" + name + ", type=" + type + ", typeName=" + typeName
			+ ", value=" + value + "]";
	}
}
