package com.broadsoft.ccone.rest.client.pojo;

import java.util.List;

import com.broadsoft.ccone.rest.client.constants.ActionType;
import com.broadsoft.ccone.rest.client.constants.EntityDataType;

public class EntityRequest extends BasePojo {

    private String id;
    private EntityDataType entityDataType;
    private String type;
    private ActionType actionType;
    private List<EntityRecord> entities;
    private HttpHeaderInfo headerInfo;
    private String url;

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
     * @return the entityDataType
     */
    public EntityDataType getEntityDataType() {
        return entityDataType;
    }

    /**
     * @param entityDataType
     *            the entityDataType to set
     */
    public void setEntityDataType(final EntityDataType entityDataType) {
        this.entityDataType = entityDataType;
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

    /**
     * @return the actionType
     */
    public ActionType getActionType() {
        return actionType;
    }

    /**
     * @param actionType
     *            the actionType to set
     */
    public void setActionType(final ActionType actionType) {
        this.actionType = actionType;
    }

    /**
     * @return the entities
     */
    public List<EntityRecord> getEntities() {
        return entities;
    }

    /**
     * @param entities
     *            the entities to set
     */
    public void setEntities(final List<EntityRecord> entities) {
        this.entities = entities;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    public HttpHeaderInfo getHeaderInfo() {
        return headerInfo;
    }

    public void setHeaderInfo(final HttpHeaderInfo headerInfo) {
        this.headerInfo = headerInfo;
    }

}
