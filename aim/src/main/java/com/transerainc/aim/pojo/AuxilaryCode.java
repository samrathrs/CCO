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
public class AuxilaryCode implements Serializable, Comparable<AuxilaryCode> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codeId;
	private String omniChannelAuxId;
	private String codeName;
	private String tenantId;
	private String codeType;
	private boolean defaultAuxCode;
	private boolean isSystemAux;

	public String getOmniChannelAuxId() {
		return omniChannelAuxId;
	}

	public void setOmniChannelAuxId(String omniChannelAuxId) {
		this.omniChannelAuxId = omniChannelAuxId;
	}
	
	public boolean isSystemAux() {
		return isSystemAux;
	}

	public void setSystemAux(boolean isSystemAux) {
		this.isSystemAux = isSystemAux;
	}

	/**
	 * @return the codeId
	 */
	public String getCodeId() {
		return codeId;
	}

	/**
	 * @param codeId
	 *            the codeId to set
	 */
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	/**
	 * @return the codeName
	 */
	public String getCodeName() {
		return codeName;
	}

	/**
	 * @param codeName
	 *            the codeName to set
	 */
	public void setCodeName(String codeName) {
		this.codeName = codeName;
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
	 * @return the codeType
	 */
	public String getCodeType() {
		return codeType;
	}

	/**
	 * @param codeType
	 *            the codeType to set
	 */
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	/**
	 * @return the defaultAuxCode
	 */
	public boolean isDefaultAuxCode() {
		return defaultAuxCode;
	}

	/**
	 * @param defaultAuxCode
	 *            the defaultAuxCode to set
	 */
	public void setDefaultAuxCode(boolean defaultAuxCode) {
		this.defaultAuxCode = defaultAuxCode;
	}

	public String toString() {
		return "Code Id: " + codeId + ", Name: " + codeName + ", TenantId: "
				+ tenantId + ", Code Type: " + codeType + ", Is Default? "
				+ defaultAuxCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(AuxilaryCode o) {
		Integer id1 = NumberUtils.toInt(this.codeId, 0);
		Integer id2 = NumberUtils.toInt(o.codeId, 0);
		return id1.compareTo(id2);
	}
}