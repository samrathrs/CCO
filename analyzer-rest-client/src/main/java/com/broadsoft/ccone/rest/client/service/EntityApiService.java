/**
 *
 */
package com.broadsoft.ccone.rest.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.broadsoft.ccone.rest.client.constants.ActionType;
import com.broadsoft.ccone.rest.client.constants.CoreConstants;
import com.broadsoft.ccone.rest.client.constants.EntityDataType;
import com.broadsoft.ccone.rest.client.constants.MessageKeys;
import com.broadsoft.ccone.rest.client.exception.ApiException;
import com.broadsoft.ccone.rest.client.exception.JsonException;
import com.broadsoft.ccone.rest.client.helper.ApiPropertiesHelper;
import com.broadsoft.ccone.rest.client.helper.CoreStringUtils;
import com.broadsoft.ccone.rest.client.helper.FliterGroupBuilder;
import com.broadsoft.ccone.rest.client.helper.JacksonJsonHelper;
import com.broadsoft.ccone.rest.client.pojo.ApiMetadataResponse;
import com.broadsoft.ccone.rest.client.pojo.ApiRecordResponse;
import com.broadsoft.ccone.rest.client.pojo.ApiResponse;
import com.broadsoft.ccone.rest.client.pojo.EntityAttribute;
import com.broadsoft.ccone.rest.client.pojo.EntityMetadata;
import com.broadsoft.ccone.rest.client.pojo.EntityRecord;
import com.broadsoft.ccone.rest.client.pojo.EntityRequest;
import com.broadsoft.ccone.rest.client.pojo.ErrorMessageInfo;
import com.broadsoft.ccone.rest.client.pojo.FilterGroup;
import com.broadsoft.ccone.rest.client.pojo.HttpHeaderInfo;
import com.broadsoft.ccone.rest.client.pojo.PlatformMetadata;
import com.broadsoft.ccone.rest.client.pojo.Query;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author svytla
 */
@Service
public class EntityApiService extends BaseEntityApiService<EntityRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityApiService.class);

    @Autowired
    private ApiPropertiesHelper propertiesHelper;

    public Map<String, EntityMetadata> getMetadata(final HttpHeaderInfo headerInfo) throws ApiException {

        final String uri = propertiesHelper.getYmlProperty(CoreConstants.METADATA_URI);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(headerInfo, ActionType.READ, url);

        return getObjectsMetadata(entityRequest);
    }

    public EntityMetadata getMetadata(final String objectType, final HttpHeaderInfo headerInfo) throws ApiException {

        final String uri = propertiesHelper.getYmlProperty(CoreConstants.METADATA_URI);

        final String url = String.format("%s/{type}", createUrl(uri));

        final EntityRequest entityRequest = transformRequest(objectType, headerInfo, ActionType.READ, url);

        return getMetadata(entityRequest);
    }

    public List<PlatformMetadata> getMetadataRecords(final String objectType, final HttpHeaderInfo headerInfo)
            throws ApiException {

        final String uri = propertiesHelper.getYmlProperty(CoreConstants.METADATA_URI);

        final String url = String.format("%s/{type}", createUrl(uri));

        final EntityRequest entityRequest = transformRequest(objectType, headerInfo, ActionType.READ, url);

        return getPlatformMetadata(entityRequest);
    }

    public String createSecuredEntity(final EntityDataType entityDataType, final Object entity) throws ApiException {

        final String apiKey = String.format(CoreConstants.CREATE_URI, StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, ActionType.CREATE, url);

        String data;
        try {
            data = restClientHelper.doPost(entityRequest, entity);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in creating record for object %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        return data;
    }

    public String createSecuredEntity(final EntityDataType entityDataType, final Object entity,
            final HttpHeaderInfo headerInfo) throws ApiException {

        final String apiKey = String.format(CoreConstants.CREATE_URI, StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.CREATE, url);

        String data;
        try {
            data = restClientHelper.doPost(entityRequest, entity);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in creating record for object %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        return data;
    }

    public boolean updateSecuredEntity(final EntityDataType entityDataType, final Object entity,
            final HttpHeaderInfo headerInfo) throws ApiException {

        final String apiKey = String.format(CoreConstants.UPDATE_URI, StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.UPDATE, url);

        try {
            restClientHelper.doPut(entityRequest, entity);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in updating record for object %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        return true;
    }

    public <T> T findSecuredEntityById(final EntityDataType entityDataType, final String id,
            final HttpHeaderInfo headerInfo, final Class<T> clazz) throws ApiException {

        final String apiKey = String.format(CoreConstants.RETRIEVE_URI,
                StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", id);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.READ, url);

        String data;
        try {
            data = restClientHelper.doGet(entityRequest, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving record of object %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        T object;
        try {
            object = JacksonJsonHelper.deserialize(data, clazz);
        } catch (final JsonException e) {

            final ApiException apiException = createException(data, e);
            throw apiException;
        }

        return object;
    }
    
    public <T> T findSecuredEntityByOrgId(final EntityDataType entityDataType, final String orgId,
            final HttpHeaderInfo headerInfo, final Class<T> clazz) throws ApiException {

        final String apiKey = String.format(CoreConstants.RETRIEVE_ORG_URI,
                StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("orgId", orgId);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.READ, url);

        String data;
        try {
            data = restClientHelper.doGet(entityRequest, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving record of object %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        T object;
        try {
            object = JacksonJsonHelper.deserialize(data, clazz);
        } catch (final JsonException e) {

            final ApiException apiException = createException(data, e);
            throw apiException;
        }

        return object;
    }
    
    public <T> T findSecuredEntityByCommonIdentity(final EntityDataType entityDataType, final String id,
            final HttpHeaderInfo headerInfo, final Class<T> clazz) throws ApiException {

        final String apiKey = String.format(CoreConstants.RETRIEVE_COMMON_IDENTITY_URI,
                StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", id);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.READ, url);

        String data;
        try {
            data = restClientHelper.doGet(entityRequest, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving record of object %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        T object;
        try {
            object = JacksonJsonHelper.deserialize(data, clazz);
        } catch (final JsonException e) {

            final ApiException apiException = createException(data, e);
            throw apiException;
        }

        return object;
    }
    
    public <T> T findTenantByQueryParameters(EntityDataType entityDataType, final Map<String,Object> params,HttpHeaderInfo headerInfo,
			Class<T> clazz)  throws ApiException{
		
    	 final String apiKey = String.format(CoreConstants.RETRIEVE_PARAMS_URI,
                 StringUtils.lowerCase(entityDataType.getName()));

         final String uri = propertiesHelper.getYmlProperty(apiKey);

         final String url = createUrl(uri);
         
         final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.READ, url);

         String data;
         try {
             data = restClientHelper.doGetByParams(entityRequest, params);
         } catch (final ApiException e) {
             final String msg = String.format("Exception in retrieving record of object %s using url %s",
                     entityDataType.name(), url);
             LOGGER.error(msg, e);
             throw e;
         }

         T object;
         try {
             object = JacksonJsonHelper.deserialize(data, clazz);
         } catch (final JsonException e) {

             final ApiException apiException = createException(data, e);
             throw apiException;
         }

         return object;
	}

    public <T> T findSecuredEntity(final EntityDataType entityDataType, final HttpHeaderInfo headerInfo,
            final Class<T> clazz) throws ApiException {

        final String apiKey = String.format(CoreConstants.SEARCH_URI, StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.READ, url);

        String data;
        try {
            data = restClientHelper.doGet(entityRequest);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving record of object %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        T object;
        try {
            object = JacksonJsonHelper.deserialize(data, clazz);
        } catch (final JsonException e) {

            final ApiException apiException = createException(data, e);
            throw apiException;
        }

        return object;
    }

    public <T> T searchSecuredEntities(final EntityDataType entityDataType, final HttpHeaderInfo headerInfo,
            final Class<T> clazz) throws ApiException {

        final String apiKey = String.format(CoreConstants.SEARCH_URI, StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.READ, url);

        String data;
        try {
            data = restClientHelper.doGet(entityRequest);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving record of object %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        if (CoreStringUtils.isBlank(data)) {
            final ApiException apiException = new ApiException("Resonse received from API is null/blank");
            throw apiException;
        }

        T object;
        try {
            object = JacksonJsonHelper.deserialize(data, clazz);
        } catch (final JsonException e) {

            final ApiException apiException = createException(data, e);
            throw apiException;
        }

        return object;
    }

    // Tenants by Parent Id List
    public <T> T searchSecuredEntitiesByParent(final EntityDataType entityDataType, final String parentId,
            final HttpHeaderInfo headerInfo, final Class<T> clazz) throws ApiException {

        final String apiKey = String.format(CoreConstants.SEARCH_CHILDREN_URI,
                StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.READ, url);

        final Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("parentId", parentId);

        String data;
        try {
            data = restClientHelper.doGet(entityRequest, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving record of object %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        if (CoreStringUtils.isBlank(data)) {
            final ApiException apiException = new ApiException("Resonse received from API is null/blank");
            throw apiException;
        }

        T object;
        try {
            object = JacksonJsonHelper.deserialize(data, clazz);
        } catch (final JsonException e) {

            final ApiException apiException = createException(data, e);
            throw apiException;
        }

        return object;
    }

    public String create(final EntityDataType entityDataType, final String objectType,
            final List<EntityRecord> entities, final HttpHeaderInfo headerInfo) throws ApiException {

        final String apiKey = String.format(CoreConstants.CREATE_URI, StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        // uri = String.format(uri, objectType);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(headerInfo, entities, entityDataType, objectType,
                ActionType.CREATE, url);

        final Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("type", objectType);

        return create(entityRequest, uriVariables);
    }

    public boolean update(final EntityDataType entityDataType, final String objectType,
            final List<EntityRecord> entities, final HttpHeaderInfo headerInfo) throws ApiException {

        final String apiKey = String.format(CoreConstants.UPDATE_URI, StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        // uri = String.format(uri, objectType);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(headerInfo, entities, entityDataType, objectType,
                ActionType.UPDATE, url);

        final Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("type", objectType);

        return update(entityRequest, uriVariables);
    }

    public boolean activate(final EntityDataType entityDataType, final String objectType, final String id,
            final HttpHeaderInfo headerInfo) throws ApiException {

        final String apiKey = String.format(CoreConstants.ACTIVATE_URI,
                StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(objectType, headerInfo, ActionType.PATCH, url);

        final Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", id);
        uriVariables.put("tenantId", headerInfo.getTenantId());

        LOGGER.debug("Activating tenant {} with url {} and variables {}", headerInfo.getTenantId(), url, uriVariables);

        return activate(entityRequest, uriVariables);
    }

    public EntityRecord findById(final EntityDataType entityDataType, final String objectType, final String id,
            final HttpHeaderInfo headerInfo) throws ApiException {

        final String apiKey = String.format(CoreConstants.RETRIEVE_URI,
                StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        // uri = String.format(uri, type, id);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, objectType, headerInfo, ActionType.READ,
                url);

        final Map<String, Object> uriVariables = new LinkedHashMap<>();
        uriVariables.put("type", objectType);
        uriVariables.put("id", id);

        return retrieve(entityRequest, uriVariables);
    }

    public List<EntityRecord> findByIds(final EntityDataType entityDataType, final String objectType,
            final Set<String> ids, final HttpHeaderInfo headerInfo) throws ApiException {

        final String apiKey = String.format(CoreConstants.RETRIEVE_URI,
                StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, objectType, headerInfo, ActionType.READ,
                url);

        final Map<String, Object> uriVariables = new LinkedHashMap<>();
        uriVariables.put("type", objectType);
        uriVariables.put("id", StringUtils.join(ids, ","));

        return findByIds(entityRequest, uriVariables);
    }

    public List<EntityRecord> search(final EntityDataType entityDataType, final String objectType,
            final Map<String, Object> attributes, final HttpHeaderInfo headerInfo) throws ApiException {

        LOGGER.debug("Searching records of entity {}, type {}, attributes {} with header params", entityDataType,
                objectType, attributes, headerInfo);

        final Query query = new Query();
        query.setAuxiliaryDataType(entityDataType);
        query.setObjectType(objectType);

        setQueryFilters(attributes, query);

        final List<EntityRecord> rows = this.search(query, headerInfo);

        LOGGER.debug("Records received for search query {} are {}", query, rows);

        return rows;
    }

    public List<EntityRecord> search(final EntityDataType entityDataType, final String objectType,
            final Set<String> columns, final Map<String, Object> attributes, final HttpHeaderInfo headerInfo)
            throws ApiException {

        LOGGER.debug("Searching records of entity {}, type {}, attributes {} with header params", entityDataType,
                objectType, attributes, headerInfo);

        final Query query = new Query();
        query.setAuxiliaryDataType(entityDataType);
        query.setObjectType(objectType);

        if (CollectionUtils.isNotEmpty(columns)) {
            query.setColumns(columns.toArray(new String[columns.size()]));
        }

        setQueryFilters(attributes, query);

        final List<EntityRecord> rows = this.search(query, headerInfo);

        LOGGER.debug("Records received for search query {} are {}", query, rows);

        return rows;
    }

    public List<EntityRecord> search(final Query query, final HttpHeaderInfo headerInfo) throws ApiException {

        final String apiKey = String.format(CoreConstants.SEARCH_URI,
                StringUtils.lowerCase(query.getAuxiliaryDataType().getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(query.getAuxiliaryDataType(), query.getObjectType(),
                headerInfo, ActionType.READ, url);

        return search(entityRequest, query);
    }

    public boolean delete(final EntityDataType entityDataType, final String objectType, final String id,
            final HttpHeaderInfo headerInfo) throws ApiException {

        final String apiKey = String.format(CoreConstants.DELETE_URI, StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, objectType, headerInfo, ActionType.DELETE,
                url);

        final Map<String, Object> uriVariables = new LinkedHashMap<>();
        uriVariables.put("type", objectType);
        uriVariables.put("id", id);
        // uriVariables.put("status", status.getValue());

        return delete(entityRequest, uriVariables);
    }

    @Override
    public EntityMetadata getMetadata(final EntityRequest req) throws ApiException {

        final String values = propertiesHelper.getYmlProperty(CoreConstants.METADATA_VALUE_OBJECTS);

        final Set<String> valueObjects = new HashSet<>(Arrays.asList(StringUtils.split(values, ",")));

        final Map<String, Object> uriVariables = new LinkedHashMap<>();
        uriVariables.put("type", req.getType());

        String data;
        try {
            data = restClientHelper.doGet(req, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving platform objects metadata using url %s",
                    req.getUrl());
            LOGGER.error(msg, e);
            throw e;
        }

        // LOGGER.debug("Received the metadata of object {} : {}", req.getType(), data);

        // final String queryResult = StringUtils.substringAfter(data, QUERY_RESULTS);
        //
        // if (StringUtils.isBlank(queryResult)) {
        //
        // final ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value());
        //
        // final ErrorMessageInfo info = new ErrorMessageInfo(MessageKeys.ERROR_INTERNAL_SERVER);
        //
        // throw new ApiException(String.format("Error in finding query results from data %s", data),
        // apiResponse.getCode(), info, apiResponse);
        // }

        PlatformMetadata metadata = null;

        EntityMetadata entityMetadata;

        EntityAttribute column;

        Map<String, EntityAttribute> attributes;

        attributes = new LinkedHashMap<>();

        final Set<String> ignoreAttributes = getMetadataIgnoreAttributes();

        try {

            final ApiMetadataResponse response = JacksonJsonHelper.deserialize(data, ApiMetadataResponse.class);

            if (CollectionUtils.isEmpty(response.getAuxiliaryDataList())) {

                final ApiResponse apiResponse = new ApiResponse(HttpStatus.NO_CONTENT.value());

                final ErrorMessageInfo info = new ErrorMessageInfo(MessageKeys.ERROR_RETRIEVE_METADATA);

                throw new ApiException(
                        String.format("Error in retrieving metadata of objects and response is : %s", data),
                        apiResponse.getCode(), info, apiResponse);

            }

            // metadata = response.getAuxiliaryDataList().get(0);

            for (final PlatformMetadata pmetadata : response.getAuxiliaryDataList()) {

                for (final Map.Entry<String, Object> entry : pmetadata.getAttributes().entrySet()) {

                    if (StringUtils.endsWith(entry.getKey(), "Def__s")) {

                        metadata = pmetadata;

                        break;
                    }
                }

                if (metadata != null) {
                    break;
                }
            }

            if (metadata == null || valueObjects.contains(metadata.getType())) { throw new ApiException(
                    String.format("%s is value object, so there is no metadata", req.getType())); }

            for (final Map.Entry<String, Object> entry : metadata.getAttributes().entrySet()) {

                if (!ignoreAttributes.contains(entry.getKey())) {

                    column = JacksonJsonHelper.deserialize(String.valueOf(entry.getValue()), EntityAttribute.class);

                    attributes.put(column.getAttribute(), column);
                }
            }

            entityMetadata = new EntityMetadata(metadata.getId(), metadata.getType(), attributes);

            return entityMetadata;

        } catch (final JsonException e) {
            final ApiException apiException = createException(data, e);
            throw apiException;
        }

    }

    public Map<String, EntityMetadata> getObjectsMetadata(final EntityRequest req) throws ApiException {

        final Map<String, EntityMetadata> map = new LinkedHashMap<>();

        final String values = propertiesHelper.getYmlProperty(CoreConstants.METADATA_VALUE_OBJECTS);

        final Set<String> valueObjects = new HashSet<>(Arrays.asList(StringUtils.split(values, ",")));

        String data;
        try {
            data = restClientHelper.doGet(req);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving platform objects metadata using url %s",
                    req.getUrl());
            LOGGER.error(msg, e);
            throw e;
        }

        // LOGGER.debug("Got platform objects metadata from API : {}", data);

        try {

            final ApiMetadataResponse response = JacksonJsonHelper.deserialize(data, ApiMetadataResponse.class);

            if (CollectionUtils.isEmpty(response.getAuxiliaryDataList())) {

                final ApiResponse apiResponse = new ApiResponse(HttpStatus.NO_CONTENT.value());

                final ErrorMessageInfo info = new ErrorMessageInfo(MessageKeys.ERROR_RETRIEVE_METADATA);

                throw new ApiException(
                        String.format("Error in retrieving metadata of objects and response is : %s", data),
                        apiResponse.getCode(), info, apiResponse);
            }

            EntityMetadata entityMetadata;

            EntityAttribute column;

            Map<String, EntityAttribute> attributes;

            final Set<String> ignoreAttributes = getMetadataIgnoreAttributes();

            for (final PlatformMetadata metadata : response.getAuxiliaryDataList()) {

                if (valueObjects.contains(metadata.getType())) {
                    continue;
                }

                attributes = new LinkedHashMap<>();

                // if (StringUtils.equalsIgnoreCase("portal-objects", metadata.getType())) {
                // continue;
                // }

                for (final Map.Entry<String, Object> entry : metadata.getAttributes().entrySet()) {

                    if (!ignoreAttributes.contains(entry.getKey())
                            && StringUtils.contains(String.valueOf(entry.getValue()), "objectName")) {

                        try {
                            column = JacksonJsonHelper.deserialize(String.valueOf(entry.getValue()),
                                    EntityAttribute.class);
                            if (column != null) {
                                attributes.put(column.getAttribute(), column);
                            }
                        } catch (final JsonException e) {
                            LOGGER.error("Error in deserializing entity attribute", e);
                        }
                    }
                }

                entityMetadata = new EntityMetadata(metadata.getId(), metadata.getType(), attributes);

                map.put(metadata.getType(), entityMetadata);

            }

        } catch (final JsonException e) {
            final ApiException apiException = createException(data, e);
            throw apiException;
        }

        return map;
    }

    @Override
    public List<PlatformMetadata> getPlatformMetadata(final EntityRequest req) throws ApiException {

        final List<PlatformMetadata> list = new ArrayList<>();

        final Map<String, Object> uriVariables = new LinkedHashMap<>();
        uriVariables.put("type", req.getType());

        String data;
        try {
            data = restClientHelper.doGet(req, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving platform objects metadata using url %s",
                    req.getUrl());
            LOGGER.error(msg, e);
            throw e;
        }

        // LOGGER.debug("Got response from API for platform objects metadata : {}", data);

        try {

            final ApiMetadataResponse response = JacksonJsonHelper.deserialize(data, ApiMetadataResponse.class);

            final Set<String> ignoreAttributes = getMetadataIgnoreAttributes();

            if (CollectionUtils.isNotEmpty(response.getAuxiliaryDataList())) {

                Map<String, Object> attributes;

                for (final PlatformMetadata metadata : response.getAuxiliaryDataList()) {

                    attributes = new LinkedHashMap<>();

                    for (final Map.Entry<String, Object> entry : metadata.getAttributes().entrySet()) {

                        if (!ignoreAttributes.contains(entry.getKey())
                                && !StringUtils.contains(String.valueOf(entry.getValue()), "objectName")) {

                            attributes.put(entry.getKey(), entry.getValue());
                        }
                    }

                    metadata.setAttributes(attributes);

                    if (MapUtils.isNotEmpty(attributes)) {
                        list.add(metadata);
                    }
                }

            }
        } catch (final JsonException e) {
            final ApiException apiException = createException(data, e);
            throw apiException;
        }

        return list;
    }

    @Override
    public String create(final EntityRequest req, final Map<String, Object> uriVariables) throws ApiException {

        String data;
        try {
            data = restClientHelper.doPost(req, req.getEntities(), uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in creating record for object %s of type %s using url %s",
                    req.getEntityDataType().name(), req, req.getUrl());
            LOGGER.error(msg, e);
            throw e;
        }

        return data;
    }

    @Override
    public boolean update(final EntityRequest req, final Map<String, Object> uriVariables) throws ApiException {

        try {
            restClientHelper.doPut(req, req.getEntities(), uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in updating record for object %s of type %s using url %s",
                    req.getEntityDataType().name(), req.getType(), req.getUrl());
            LOGGER.error(msg, e);
            throw e;
        }

        return true;
    }

    public boolean activate(final EntityRequest req, final Map<String, Object> uriVariables) throws ApiException {

        try {
            restClientHelper.doPatch(req, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in activating sp/customer using url %s", req.getUrl());
            LOGGER.error(msg, e);
            throw e;
        }

        return true;
    }

    @Override
    public EntityRecord retrieve(final EntityRequest req, final Map<String, Object> uriVariables) throws ApiException {

        String data;
        try {
            data = restClientHelper.doGet(req, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format(
                    "Exception in retrieving record of object %s of type %s using url %s with params %s",
                    req.getEntityDataType().name(), req.getType(), req.getUrl(), uriVariables.toString());
            LOGGER.error(msg, e);
            throw e;
        }

        try {

            final EntityRecord entity = JacksonJsonHelper.deserialize(data, EntityRecord.class);

            return entity;

        } catch (final JsonException e) {

            final ApiException apiException = createException(data, e);
            throw apiException;
        }
    }

    @Override
    public boolean delete(final EntityRequest req, final Map<String, Object> uriVariables) throws ApiException {

        try {
            restClientHelper.doDelete(req, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format(
                    "Exception in deleting record of object %s of type %s using url %s with params %s",
                    req.getEntityDataType().name(), req.getType(), req.getUrl(), uriVariables.toString());
            LOGGER.error(msg, e);
            throw e;
        }

        return true;
    }

    @Override
    public List<EntityRecord> search(final EntityRequest req, final Query query) throws ApiException {

        final Map<String, Object> params = new HashMap<>();
        params.put("q", query);

        String data;
        try {
            data = restClientHelper.doGetSearch(req, params);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving records of object %s of type %s using url %s",
                    req.getEntityDataType().name(), req.getType(), req.getUrl());
            LOGGER.error(msg, e);
            throw e;
        }

        if (CoreStringUtils.isBlank(data)) {
            final ApiException apiException = new ApiException(
                    String.format("Resonse received from API is null/blank - %s", data));
            throw apiException;
        }

        // LOGGER.debug("Response received from API is : {}", data);

        // final List<EntityRecord> list = new ArrayList<>();

        try {

            final ApiRecordResponse response = JacksonJsonHelper.deserialize(data, ApiRecordResponse.class);

            return response.getAuxiliaryDataList();

        } catch (final JsonException e) {

            final ApiException apiException = createException(data, e);
            throw apiException;
        }

    }

    public List<EntityRecord> findByIds(final EntityRequest req, final Map<String, Object> uriVariables)
            throws ApiException {

        String data;
        try {
            data = restClientHelper.doGet(req, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving records of object %s of type %s using url %s",
                    req.getEntityDataType().name(), req.getType(), req.getUrl());
            LOGGER.error(msg, e);
            throw e;
        }

        if (CoreStringUtils.isBlank(data)) {
            final ApiException apiException = new ApiException("Resonse received from API is null/blank");
            throw apiException;
        }

        try {
            // response from rest api is not identical
            if (!isJSONValid(data)) {
                final List<String> list = new ArrayList<>();
                list.add(data);
                data = list.toString();
            }
            final TypeReference<?> typeRef = new TypeReference<List<EntityRecord>>() {
            };
            final List<EntityRecord> response = JacksonJsonHelper.deserialize(data, typeRef);
            return response;

        } catch (final JsonException e) {
            final ApiException apiException = createException(data, e);
            throw apiException;
        }
    }

    private void setQueryFilters(final Map<String, Object> attributes, final Query query) {

        if (MapUtils.isNotEmpty(attributes)) {

            final FliterGroupBuilder builder = FliterGroupBuilder.builder(attributes);
            final FilterGroup[] filterGroups = builder.build();

            query.setFilterGroups(filterGroups);
        }
    }

}
