/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import java.util.HashMap;
import java.util.Map;

import com.broadsoft.ccone.rest.client.constants.EntityDataType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author ravi.shankar
 */
public class EntityModel extends BasePojo {

    protected String id;
    protected String type;

    @JsonIgnore
    protected String tenantId;

    @JsonIgnore
    protected EntityDataType entityDataType;

    protected Map<String, Object> attributes;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public EntityDataType getEntityDataType() {
        return entityDataType;
    }

    public void setEntityDataType(final EntityDataType entityDataType) {
        this.entityDataType = entityDataType;
    }

    public Map<String, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    public void setAttributes(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
