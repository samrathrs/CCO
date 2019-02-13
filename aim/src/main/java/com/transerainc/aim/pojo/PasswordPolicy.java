/**
 * 
 */
package com.transerainc.aim.pojo;

import java.io.Serializable;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class PasswordPolicy implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  name;
	private String  id;
	private String  tenantId;
	private int     pwdExpirePeriod;
	private int     maxInvalidAttempts;	
	private int     isDefault;
	

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}



	public String getTenantId() {
		return tenantId;
	}



	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}





	public int getMaxInvalidAttempts() {
		return maxInvalidAttempts;
	}





	public void setMaxInvalidAttempts(int maxInvalidAttempts) {
		this.maxInvalidAttempts = maxInvalidAttempts;
	}





	public int getPwdExpirePeriod() {
		return pwdExpirePeriod;
	}





	public void setPwdExpirePeriod(int pwdExpirePeriod) {
		this.pwdExpirePeriod = pwdExpirePeriod;
	}





	public int getIsDefault() {
		return isDefault;
	}





	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}


	public String toString() {
		return "Id: " + id + ", name: " + name + ", tenantId: " + tenantId + ", Max Invalid Attempts: "
			+ maxInvalidAttempts + ", Password Expiration Period: " + pwdExpirePeriod
			+ ", default: " + isDefault;
	}
}
