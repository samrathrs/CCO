/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

/**
 * @author svytla
 */
public class EntityColumn extends BasePojo {

    protected String legacyTableName;
    protected String legacyColumnName;
    protected String attribute;
    protected String dataType;
    protected boolean mandatory;
    protected boolean unique;
    protected String defaultValue;
    protected int length;
    protected String label;
    protected boolean customAttribute;

    /**
     * @return the legacyTableName
     */
    public String getLegacyTableName() {
        return legacyTableName;
    }

    /**
     * @param legacyTableName
     *            the legacyTableName to set
     */
    public void setLegacyTableName(final String legacyTableName) {
        this.legacyTableName = legacyTableName;
    }

    /**
     * @return the legacyColumnName
     */
    public String getLegacyColumnName() {
        return legacyColumnName;
    }

    /**
     * @param legacyColumnName
     *            the legacyColumnName to set
     */
    public void setLegacyColumnName(final String legacyColumnName) {
        this.legacyColumnName = legacyColumnName;
    }

    /**
     * @return the attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * @param attribute
     *            the attribute to set
     */
    public void setAttribute(final String attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType
     *            the dataType to set
     */
    public void setDataType(final String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * @param mandatory
     *            the mandatory to set
     */
    public void setMandatory(final boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * @return the unique
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * @param unique
     *            the unique to set
     */
    public void setUnique(final boolean unique) {
        this.unique = unique;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue
     *            the defaultValue to set
     */
    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    public boolean isCustomAttribute() {
        return customAttribute;
    }

    public void setCustomAttribute(final boolean customAttribute) {
        this.customAttribute = customAttribute;
    }

}
