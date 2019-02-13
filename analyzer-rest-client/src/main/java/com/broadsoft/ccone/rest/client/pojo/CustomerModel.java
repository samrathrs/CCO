/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import org.apache.commons.lang3.StringUtils;

/**
 * @author svytla
 */
public class CustomerModel extends BasePojo {

    private String tenantId;
    private String name;
    private String description;
    private String tenantType;
    private String timezone;
    private String parentId;

    protected EntityModel customer;

    public String getName() {
        return StringUtils.trim(name);
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return StringUtils.trim(description);
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getTenantType() {
        return StringUtils.trim(tenantType);
    }

    public void setTenantType(final String tenantType) {
        this.tenantType = tenantType;
    }

    public String getTimezone() {
        return StringUtils.trim(timezone);
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

    public EntityModel getCustomer() {
        return customer;
    }

    public void setCustomer(final EntityModel customer) {
        this.customer = customer;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String id) {
        tenantId = id;
    }

}
