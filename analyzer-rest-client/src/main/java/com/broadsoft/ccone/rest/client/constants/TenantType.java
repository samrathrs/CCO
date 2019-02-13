/**
 *
 */
package com.broadsoft.ccone.rest.client.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @author svytla
 */
public enum TenantType {

    SERVICE_PROVIDER, CUSTOMER;

    public static TenantType getDataType(final String type) {

        TenantType dataType = TenantType.SERVICE_PROVIDER;

        switch (StringUtils.upperCase(type)) {

        case "SERVICE_PROVIDER":
            dataType = TenantType.SERVICE_PROVIDER;
            break;

        case "CUSTOMER":
            dataType = TenantType.CUSTOMER;
            break;
        }

        return dataType;
    }
}
