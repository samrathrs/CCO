package com.broadsoft.ccone.rest.client.pojo;

import java.util.Arrays;

import com.broadsoft.ccone.rest.client.constants.EntityDataType;

public class Query {

    private String anchorId;
    private String[] columns;
    protected FilterGroup[] filterGroups;
    protected EntityDataType auxiliaryDataType = EntityDataType.USER_DATA;
    protected String objectType;

    /**
     * @return the anchorId
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * @param anchorId
     *            the anchorId to set
     */
    public void setAnchorId(final String anchorId) {
        this.anchorId = anchorId;
    }

    /**
     * @return the columns
     */
    public String[] getColumns() {
        return columns;
    }

    /**
     * @param columns
     *            the columns to set
     */
    public void setColumns(final String[] columns) {
        this.columns = columns;
    }

    /**
     * @return the filterGroups
     */
    public FilterGroup[] getFilterGroups() {
        return filterGroups;
    }

    /**
     * @param filterGroups
     *            the filterGroups to set
     */
    public void setFilterGroups(final FilterGroup[] filterGroups) {
        this.filterGroups = filterGroups;
    }

    /**
     * @return the auxiliaryDataType
     */
    public EntityDataType getAuxiliaryDataType() {
        return auxiliaryDataType;
    }

    /**
     * @param auxiliaryDataType
     *            the auxiliaryDataType to set
     */
    public void setAuxiliaryDataType(final EntityDataType auxiliaryDataType) {
        this.auxiliaryDataType = auxiliaryDataType;
    }

    /**
     * @return the objectType
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * @param objectType
     *            the objectType to set
     */
    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Query [anchorId=" + anchorId + ", columns=" + Arrays.toString(columns) + ", filterGroups="
                + Arrays.toString(filterGroups) + ", auxiliaryDataType=" + auxiliaryDataType + ", objectType="
                + objectType + "]";
    }

}
