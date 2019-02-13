/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author svytla
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tenant extends BasePojo {

    private long id;
    private String name;
    private String displayName;
    private String description;
    private String tenantType;
    private long parentId;
    private String timezone;
    private int active;
    private int maxCompRecords;
    private String domain;
    private String externalId;
    private String orgId;
    private UserRecord administrator;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
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

    public long getParentId() {
        return parentId;
    }

    public void setParentId(final long parentId) {
        this.parentId = parentId;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    public int getActive() {
        return active;
    }

    public void setActive(final int active) {
        this.active = active;
    }

    public UserRecord getAdministrator() {
        return administrator;
    }

    public void setAdministrator(final UserRecord administrator) {
        this.administrator = administrator;
    }

    public int getMaxCompRecords() {
        return maxCompRecords;
    }

    public void setMaxCompRecords(final int maxCompRecords) {
        this.maxCompRecords = maxCompRecords;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(final String externalId) {
        this.externalId = externalId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(final String orgId) {
        this.orgId = orgId;
    }

}
