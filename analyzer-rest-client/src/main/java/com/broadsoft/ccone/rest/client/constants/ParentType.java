package com.broadsoft.ccone.rest.client.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @author mreddy
 */
public enum ParentType {

    TENANT((short) 1, "1"), SITE((short) 2, "2");

    private short value;
    private String name;

    ParentType(final short value, final String name) {
        this.value = value;
        this.name = name;
    }

    public static ParentType fromShort(final short value) {
        ParentType aType = null;
        switch (value) {
        case 1:
            aType = ParentType.TENANT;
            break;
        case 2:
            aType = ParentType.SITE;
            break;
        default:
            throw new NullPointerException(String.format("No parent-type associated with the value %d ", value));
        }

        return aType;
    }

    public static ParentType fromString(final String fname) {

        ParentType aType = null;

        final String name = StringUtils.lowerCase(fname);

        switch (name) {
        case "1":
            aType = ParentType.TENANT;
            break;
        case "2":
            aType = ParentType.SITE;
            break;
        default:
            throw new NullPointerException(String.format("No parent-type associated with the value %s ", name));
        }

        return aType;
    }

    public short getValue() {
        return value;
    }

    public void setValue(final short value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
