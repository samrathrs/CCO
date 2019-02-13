/**
 *
 */
package com.broadsoft.ccone.rest.client.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @author svytla
 */
public enum VirtualTeamType {

    ENTRY_POINT((short) 0, "0"), QUEUE((short) 1, "1"), OUTDIAL_ENTRY_POINT((short) 2, "2"), OUTDIAL_QUEUE((short) 3,
            "3");

    private short value;
    private String name;

    VirtualTeamType(final short value, final String name) {
        this.value = value;
        this.name = name;
    }

    public static VirtualTeamType fromShort(final short value) {
        VirtualTeamType aType = null;
        switch (value) {
        case 0:
            aType = VirtualTeamType.ENTRY_POINT;
            break;
        case 1:
            aType = VirtualTeamType.QUEUE;
            break;
        case 2:
            aType = VirtualTeamType.OUTDIAL_ENTRY_POINT;
            break;
        case 3:
            aType = VirtualTeamType.OUTDIAL_QUEUE;
            break;
        default:
            throw new NullPointerException(String.format("No virutla-team associated with the value %d ", value));
        }

        return aType;
    }

    public static VirtualTeamType fromString(final String fname) {

        VirtualTeamType aType = null;

        final String name = StringUtils.lowerCase(fname);

        switch (name) {
        case "0":
            aType = VirtualTeamType.ENTRY_POINT;
            break;
        case "1":
            aType = VirtualTeamType.QUEUE;
            break;
        case "2":
            aType = VirtualTeamType.OUTDIAL_ENTRY_POINT;
            break;
        case "3":
            aType = VirtualTeamType.OUTDIAL_QUEUE;
            break;
        default:
            throw new NullPointerException(String.format("No virutla-team associated with the value %s ", name));
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
