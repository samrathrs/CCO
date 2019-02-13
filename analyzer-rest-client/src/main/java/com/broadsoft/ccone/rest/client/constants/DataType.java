/**
 *
 */
package com.broadsoft.ccone.rest.client.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @author svytla
 */
public enum DataType {

    INTEGER, LONG, DOUBLE, DATETIME, STRING, STRING_ARRAY, INT_ARRAY, LONG_ARRAY, DOUBLE_ARRAY;

    public static DataType getDataType(final String type) {

        DataType dataType = DataType.STRING;

        switch (StringUtils.upperCase(type)) {
        case "INTEGER":
        case "INT":
            dataType = DataType.INTEGER;
            break;
        case "LONG":
            dataType = DataType.LONG;
            break;
        case "DOUBLE":
            dataType = DataType.DOUBLE;
            break;
        case "DATETIME":
        case "TIMESTAMP":
            dataType = DataType.DATETIME;
            break;
        case "STRING":
            dataType = DataType.STRING;
            break;
        case "STRING_ARRAY":
            dataType = DataType.STRING_ARRAY;
            break;
        case "INT_ARRAY":
            dataType = DataType.INT_ARRAY;
            break;
        case "LONG_ARRAY":
            dataType = DataType.LONG_ARRAY;
            break;
        case "DOUBLE_ARRAY":
            dataType = DataType.DOUBLE_ARRAY;
            break;
        }

        return dataType;
    }
}
