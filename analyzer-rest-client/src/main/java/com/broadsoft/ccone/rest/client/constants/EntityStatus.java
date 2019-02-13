/**
 *
 */
package com.broadsoft.ccone.rest.client.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @author svytla
 */
public enum EntityStatus {

    ACTIVE((short) 1, "active"), SUSPEND((short) 2, "suspend"), DELETE((short) 3, "delete");

    private short value;
    private String name;

    EntityStatus(final short value, final String name) {
        this.value = value;
        this.name = name;
    }

    public static EntityStatus fromShort(final short value) {
        EntityStatus aType = null;
        switch (value) {
        case 1:
            aType = EntityStatus.ACTIVE;
            break;
        case 2:
            aType = EntityStatus.SUSPEND;
            break;
        case 3:
            aType = EntityStatus.DELETE;
            break;
        default:
            throw new NullPointerException(String.format("No entity status associated with the value %d ", value));
        }

        return aType;
    }

    public static EntityStatus fromString(final String fname) {

        EntityStatus aType = null;

        final String name = StringUtils.lowerCase(fname);

        switch (name) {
        case "active":
            aType = EntityStatus.ACTIVE;
            break;
        case "suspend":
        case "suspended":
            aType = EntityStatus.SUSPEND;
            break;
        case "delete":
        case "deleted":
            aType = EntityStatus.DELETE;
            break;
        default:
            throw new NullPointerException(String.format("No entity status associated with the value %s ", fname));
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
