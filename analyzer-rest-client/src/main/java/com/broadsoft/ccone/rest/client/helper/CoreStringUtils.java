/**
 *
 */
package com.broadsoft.ccone.rest.client.helper;

import org.apache.commons.lang3.StringUtils;

/**
 * @author svytla
 */
public class CoreStringUtils {

    public static final String EMPTY = "empty";
    public static final String NULL = "null";

    public static boolean isNotBlank(final String value) {
        return StringUtils.isNotBlank(value) && !StringUtils.equalsIgnoreCase(value, NULL)
                && !StringUtils.equalsIgnoreCase(value, EMPTY);
    }

    public static boolean isBlank(final String value) {
        return StringUtils.isBlank(value) || StringUtils.equalsIgnoreCase(value, NULL)
                || StringUtils.equalsIgnoreCase(value, EMPTY);
    }

    public static String trim(final String value) {
        return StringUtils.isBlank(value) || StringUtils.equalsIgnoreCase(value, NULL)
                || StringUtils.equalsIgnoreCase(value, EMPTY) ? "" : StringUtils.trim(value);
    }

}
