package com.broadsoft.ccone.rest.client.pojo;

import java.util.LinkedHashMap;
import java.util.Map;

import com.broadsoft.ccone.rest.client.constants.EntityDataType;

public class EntityData extends BasePojo {

    protected String id;
    protected String type;
    protected EntityDataType entityDataType;
    // protected String tenantId;
    protected Map<String, Object> attributes;

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
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(final String type) {
        this.type = type;
    }

    // /**
    // * @return the tenantId
    // */
    // public String getTenantId() {
    // return tenantId;
    // }
    //
    // /**
    // * @param tenantId
    // * the tenantId to set
    // */
    // public void setTenantId(final String tenantId) {
    // this.tenantId = tenantId;
    // }

    /**
     * @return the attributes
     */
    public Map<String, Object> getAttributes() {
        if (attributes == null) {
            attributes = new LinkedHashMap<>();
        }
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public EntityDataType getEntityDataType() {
        return entityDataType;
    }

    public void setEntityDataType(final EntityDataType entityDataType) {
        this.entityDataType = entityDataType;
    }

}
