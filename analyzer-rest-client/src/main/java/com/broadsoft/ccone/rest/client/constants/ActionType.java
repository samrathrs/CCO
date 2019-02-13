package com.broadsoft.ccone.rest.client.constants;

import org.apache.commons.lang3.StringUtils;

public enum ActionType {

    CREATE, UPDATE, READ, DELETE, COPY, RESTORE, PATCH, GET, QUERY, SUSPEND;

    public static ActionType fromString(final String value) {

        ActionType type = CREATE;

        final String action = StringUtils.upperCase(value);

        switch (action) {

        case "CREATE":
            type = CREATE;
            break;

        case "UPDATE":
            type = UPDATE;
            break;

        case "READ":
            type = READ;
            break;

        case "DELETE":
            type = DELETE;
            break;

        case "COPY":
            type = COPY;
            break;

        case "RESTORE":
            type = RESTORE;
            break;

        case "PATCH":
            type = PATCH;
            break;

        case "GET":
            type = GET;
            break;

        case "QUERY":
            type = QUERY;
            break;

        case "SUSPEND":
            type = SUSPEND;
            break;

        }

        return type;
    }

}
