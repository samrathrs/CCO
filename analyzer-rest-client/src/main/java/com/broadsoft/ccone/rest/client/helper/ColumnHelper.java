/**
 *
 */
package com.broadsoft.ccone.rest.client.helper;

import org.apache.commons.lang3.text.WordUtils;

import com.broadsoft.ccone.rest.client.constants.DataType;
import com.broadsoft.ccone.rest.client.exception.ApiException;

/**
 * @author svytla
 */
public class ColumnHelper {

    public static String getColumnName(final String dispName, final String dataType) throws ApiException {

        String colName = null;
        final DataType dType = DataType.getDataType(dataType);
        switch (dType) {
        case STRING:
            colName = packDisplayName(dispName, "__s");
            break;
        case INTEGER:
            colName = packDisplayName(dispName, "__i");
            break;
        case DOUBLE:
            colName = packDisplayName(dispName, "__d");
            break;
        case LONG:
            colName = packDisplayName(dispName, "__l");
            break;
        case DATETIME:
            colName = packDisplayName(dispName, "__l");
            break;
        case STRING_ARRAY:
            colName = packDisplayName(dispName, "__sa");
            break;
        case INT_ARRAY:
            colName = packDisplayName(dispName, "__ia");
            break;
        case LONG_ARRAY:
            colName = packDisplayName(dispName, "__la");
            break;
        case DOUBLE_ARRAY:
            colName = packDisplayName(dispName, "__da");
            break;
        default:
            throw new ApiException(String.format("%s not supported", dType.name()));
        }

        return colName;
    }

    public static String getColumnName(final String dispName, final DataType dType) throws ApiException {

        String colName = null;

        switch (dType) {
        case STRING:
            colName = packDisplayName(dispName, "__s");
            break;
        case INTEGER:
            colName = packDisplayName(dispName, "__i");
            break;
        case DOUBLE:
            colName = packDisplayName(dispName, "__d");
            break;
        case LONG:
            colName = packDisplayName(dispName, "__l");
            break;
        case DATETIME:
            colName = packDisplayName(dispName, "__l");
            break;
        case STRING_ARRAY:
            colName = packDisplayName(dispName, "__sa");
            break;
        case INT_ARRAY:
            colName = packDisplayName(dispName, "__ia");
            break;
        case LONG_ARRAY:
            colName = packDisplayName(dispName, "__la");
            break;
        case DOUBLE_ARRAY:
            colName = packDisplayName(dispName, "__da");
            break;
        default:
            throw new ApiException(String.format("%s not supported", dType.name()));
        }

        return colName;
    }

    private static String packDisplayName(final String dispName, final String suffix) {
        final String[] tokens = dispName.split(" ");
        final StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String token : tokens) {
            token = token.trim();
            if (first) {
                first = false;
                builder.append(token.toLowerCase());
                continue;
            }
            String s = token.toLowerCase();
            s = WordUtils.capitalize(s);
            builder.append(s);
        }

        builder.append(suffix);

        return builder.toString();
    }
}
