package com.broadsoft.ccone.rest.client.pojo;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatformMetadata extends BasePojo {

    private String id;
    private String auxiliaryDataType;
    private String type;
    private Map<String, Object> attributes;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Map<String, Object> getAttributes() {
        if (attributes == null) {
            attributes = new LinkedHashMap<>();
        }
        return attributes;
    }

    public void setAttributes(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the auxiliaryDataType
     */
    public String getAuxiliaryDataType() {
        return auxiliaryDataType;
    }

    /**
     * @param auxiliaryDataType
     *            the auxiliaryDataType to set
     */
    public void setAuxiliaryDataType(final String auxiliaryDataType) {
        this.auxiliaryDataType = auxiliaryDataType;
    }

}
