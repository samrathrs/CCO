/**
 *
 */
package com.broadsoft.ccone.rest.client.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @author svytla
 */
public enum ChannelType {

    VOICE("telephony"), EMAIL("email"), FAX("fax"), CHAT("chat"), OTHER("other");

    private String name;

    ChannelType(final String name) {
        this.name = name;
    }

    public static ChannelType fromString(final String fname) {

        ChannelType aType = null;

        final String name = StringUtils.lowerCase(fname);

        switch (name) {
        case "telephony":
            aType = ChannelType.VOICE;
            break;
        case "email":
            aType = ChannelType.EMAIL;
            break;
        case "fax":
            aType = ChannelType.FAX;
            break;
        case "chat":
            aType = ChannelType.CHAT;
            break;
        default:
            aType = ChannelType.OTHER;
            break;
        // throw new NullPointerException(String.format("No state associated with the value %s ", fname));
        }

        return aType;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
