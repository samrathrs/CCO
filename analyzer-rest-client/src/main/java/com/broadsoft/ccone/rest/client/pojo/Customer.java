/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

/**
 * @author svytla
 */
public class Customer extends EntityRecord {

    private String tenantId;
    private String name;
    private String description;
    private String tenantType;
    private String timezone;
    private String parentId;

    public Customer() {

    }

    public Customer(final EntityRecord record) {
        id = record.id;
        type = record.type;
        auxiliaryDataType = record.auxiliaryDataType;
        attributes = record.attributes;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getTenantType() {
        return tenantType;
    }

    public void setTenantType(final String tenantType) {
        this.tenantType = tenantType;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(final String parentId) {
        this.parentId = parentId;
    }

}
