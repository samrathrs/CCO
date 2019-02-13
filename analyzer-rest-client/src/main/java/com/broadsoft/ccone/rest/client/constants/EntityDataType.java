package com.broadsoft.ccone.rest.client.constants;

import org.apache.commons.lang3.StringUtils;

public enum EntityDataType {

    USER_DATA((short) 1, "user-data"), RESOURCES((short) 2, "resources"), METADATA((short) 3, "metadata"), TENANT(
            (short) 4, "tenant"), USER((short) 5, "user"), BLOB_METADATA((short) 6, "blob-metadata"), BLOB_RESOURCES(
                    (short) 7, "blob-resources"), BLOB_USER_DATA((short) 7, "blob-user-data");

    private short value;
    private String name;

    EntityDataType(final short value, final String name) {
        this.value = value;
        this.name = name;
    }

    public static EntityDataType fromShort(final short value) {
        EntityDataType aType = null;
        switch (value) {
        case 1:
            aType = EntityDataType.USER_DATA;
            break;
        case 2:
            aType = EntityDataType.RESOURCES;
            break;
        case 3:
            aType = EntityDataType.METADATA;
            break;
        case 4:
            aType = EntityDataType.TENANT;
            break;
        case 5:
            aType = EntityDataType.USER;
            break;
        case 6:
            aType = EntityDataType.BLOB_METADATA;
            break;
        case 7:
            aType = EntityDataType.BLOB_RESOURCES;
            break;
        case 8:
            aType = EntityDataType.BLOB_USER_DATA;
            break;
        default:
            throw new NullPointerException(String.format("No resource data associated with the value %d ", value));
        }

        return aType;
    }

    public static EntityDataType fromString(final String fname) {

        EntityDataType aType = null;

        final String name = StringUtils.lowerCase(fname);

        switch (name) {
        case "user-data":
            aType = EntityDataType.USER_DATA;
            break;
        case "resources":
            aType = EntityDataType.RESOURCES;
            break;
        case "metadata":
            aType = EntityDataType.METADATA;
            break;
        case "tenant":
            aType = EntityDataType.TENANT;
            break;
        case "user":
            aType = EntityDataType.USER;
            break;
        case "blob-metadata":
            aType = EntityDataType.BLOB_METADATA;
            break;
        case "blob-resources":
            aType = EntityDataType.BLOB_RESOURCES;
            break;
        case "blob-user-data":
            aType = EntityDataType.BLOB_USER_DATA;
            break;
        case "api-key":
            aType = EntityDataType.BLOB_USER_DATA;
            break;
        default:
            throw new NullPointerException(String.format("No resource data associated with the value %s ", fname));

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
