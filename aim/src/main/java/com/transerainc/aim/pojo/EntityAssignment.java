/**
 * 
 */
package com.transerainc.aim.pojo;

/**
 * Date of Creation: Date: Jun 28, 2010<br>
 * @author <a href=mailto:suresh@transerainc.com>Suresh Kumar</a>
 * @version $Revision: 1.1 $
 */

public class EntityAssignment {

	private String id;
	private String entityId;
	private String entityType;
	private String tenantId;
	
	public EntityAssignment(){
		
	}
	
	public EntityAssignment(String entityId, String entityType, String tenantId) {
		super();
		this.entityId = entityId;
		this.entityType = entityType;
		this.tenantId = tenantId;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}
	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	/**
	 * @return the entityType
	 */
	public String getEntityType() {
		return entityType;
	}
	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	
	
	
}
