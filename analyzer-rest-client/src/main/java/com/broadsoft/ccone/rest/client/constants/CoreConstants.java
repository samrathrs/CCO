package com.broadsoft.ccone.rest.client.constants;

/**
 * @author svytla
 */
public interface CoreConstants {

    String UTF8 = "UTF-8";

    String UTC_TIMEZONE = "UTC";

    String YAML_FILE_NAME = "smbp.yml";

    String CONFIG_DIR = "config.dir";

    String PROPERTIES_FILE_NAME = "properties.file.name";

    String TPM_PATH = "/nondmz/tpm";

    String PING_PATH = TPM_PATH + "/pingIntf";

    String SHUTDOWN_PATH = TPM_PATH + "/shutdownIntf";

    String CACHE_KEY_SEPERATOR = "|";

    String REST_CLIENT_YML_FILE_NAME = "rest-client.yml";

    String ID = "id";

    String TEXT = "text";

    String LIST_ID = "listId__s";

    String NAME = "name";

    String DESCRIPTION = "description";

    String TENANT_TYPE = "tenantType";

    String TENANT_ATTRIBUTE_PARENT_ID = "parentId";

    String TENANT_ATTRIBUTE_TIMEZONE = "timezone";

    String ATTRIBUTE_TENANT = "tenantId__s";

    String ATTRIBUTE_NAME = "name__s";

    String ATTRIBUTE_STATUS = "status__i";

    String ATTRIBUTE_TEAM_IDS = "teamIds__sa";

    String ATTRIBUTE_DEFAULT = "isDefault__i";

    String ATTRIBUTE_STATE = "state__s";

    String TFN_ALL = "-1";

    String NONE = "0";

    String NULL = "null";

    String TRUNCATED_CHARACTERS = "...";

    /**
     * API URI keys
     */
    String METADATA_URI = "api.entity.metadata.uri";

    String TYPES_URI = "api.%s.types.uri";

    String COLUMNS_URI = "api.%s.columns.uri";

    String CREATE_URI = "api.%s.create.uri";

    String UPDATE_URI = "api.%s.update.uri";

    String DELETE_URI = "api.%s.delete.uri";

    String RETRIEVE_URI = "api.%s.retrieve.uri";

    String RETRIEVE_ORG_URI = "api.%s.retrieve.org.uri";
    
    String RETRIEVE_PARAMS_URI  = "api.%s.retrieve.params.uri";
    
    String RETRIEVE_COMMON_IDENTITY_URI = "api.%s.retrieve.identity.uri";
    
    String RETRIEVE_ALL_URI = "api.%s.retrieve.all.uri";

    String SEARCH_URI = "api.%s.search.uri";

    String SEARCH_CHILDREN_URI = "api.%s.search.children.uri";

    String ACTIVATE_URI = "api.%s.activate.uri";

    String API_DELAY_IN_RESPONSE = "api.delay.in.response";

    String SUBSCRIPTION_DELAY = "subscription.delay";

    String QUERY_RESULTS = "QUERY_RESULTS";

    String SEG_PROFILE = "SEG_PROFILE";

    int DEFAULT_DELAY = 5000;

    int TRUE = 1;

    int FALSE = 0;

    String PERMISSION = "permission";

    String FEATURES = "features";

    String METADATA_VALUE_OBJECTS = "metadata.value.objects";

    String METADATA_IGNORE_ATTRIBUTES = "metadata.ignore.attributes";

    String HEADER_FROM = "From";

    String HEADER_AUTHORIZATION = "Authorization";

    String PARAM_TIMEZONE_ID = "timezone-id";

    String CONTENT_TYPE = "Content-Type";

    String CONTENT_TYPE_JSON = "application/json";

    String CONTENT_TYPE_XML = "text/xml";

    String APPLICATION_JSON = "application/json;charset=UTF-8";

}
