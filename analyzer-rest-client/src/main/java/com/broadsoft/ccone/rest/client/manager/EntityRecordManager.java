/**
 *
 */
package com.broadsoft.ccone.rest.client.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.broadsoft.ccone.rest.client.constants.EntityDataType;
import com.broadsoft.ccone.rest.client.constants.EntityType;
import com.broadsoft.ccone.rest.client.constants.MessageKeys;
import com.broadsoft.ccone.rest.client.constants.TenantType;
import com.broadsoft.ccone.rest.client.exception.ApiException;
import com.broadsoft.ccone.rest.client.exception.ValidationException;
import com.broadsoft.ccone.rest.client.helper.FliterGroupBuilder;
import com.broadsoft.ccone.rest.client.helper.JacksonJsonHelper;
import com.broadsoft.ccone.rest.client.pojo.ApiResponse;
import com.broadsoft.ccone.rest.client.pojo.Customer;
import com.broadsoft.ccone.rest.client.pojo.EntityRecord;
import com.broadsoft.ccone.rest.client.pojo.ErrorMessageInfo;
import com.broadsoft.ccone.rest.client.pojo.FilterGroup;
import com.broadsoft.ccone.rest.client.pojo.HttpHeaderInfo;
import com.broadsoft.ccone.rest.client.pojo.Link;
import com.broadsoft.ccone.rest.client.pojo.Query;
import com.broadsoft.ccone.rest.client.pojo.Tenant;
import com.broadsoft.ccone.rest.client.pojo.UserRecord;
import com.broadsoft.ccone.rest.client.service.EntityApiService;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author svytla
 */
@Component
public class EntityRecordManager extends MetadataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRecordManager.class);

    @Autowired
    private EntityApiService apiService;

    @SuppressWarnings("unchecked")
    public Tenant findTenantById(final String tenantId) throws ApiException {

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(tenantId, false);

        final ApiResponse response = findEntityById(EntityDataType.TENANT, tenantId, headerInfo, ApiResponse.class);

        final Map<String, Object> map = (Map<String, Object>) response.getDetails().get("tenant");

        final Tenant tenant = JacksonJsonHelper.convertValue(map, Tenant.class);

        return tenant;
    }
    
    @SuppressWarnings("unchecked")
    public Tenant findTenantByOrgId(final String tenantId, final String orgId) throws ApiException {
        
        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(tenantId, false);
        
        final ApiResponse response = findEntityByOrgId(EntityDataType.TENANT, orgId, headerInfo, ApiResponse.class);
        
        final Map<String, Object> map = (Map<String, Object>) response.getDetails().get("tenant");
        
        final Tenant tenant = JacksonJsonHelper.convertValue(map, Tenant.class);

        return tenant;
        
    }
    
    public ApiResponse findTenantByQueryParameters(final String tenantId, final Map<String,Object> params) throws ApiException{
    	
    	 final HttpHeaderInfo headerInfo = new HttpHeaderInfo(tenantId, false);
    	 
         final ApiResponse response = findTenantByQueryParameters(EntityDataType.TENANT, params, headerInfo, ApiResponse.class);
         
         return response;
    }
    
	@SuppressWarnings("unchecked")
    public UserRecord findUserByCommonIdentity(final String tenantId, final String id) throws ApiException {
        
        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(tenantId, false);
        
        final ApiResponse response = findEntityByCommonIdentity(EntityDataType.USER, id, headerInfo, ApiResponse.class);
        
        final Map<String, Object> map = (Map<String, Object>) response.getDetails().get("user");
        
        final UserRecord userRecord = JacksonJsonHelper.convertValue(map, UserRecord.class);

        return userRecord;
        
    }

    @SuppressWarnings("unchecked")
    public Customer findCustomerByTenantId(final String tenantId) throws ApiException {

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(tenantId, false);

        final ApiResponse response = findEntityById(EntityDataType.TENANT, tenantId, headerInfo, ApiResponse.class);

        final Map<String, Object> map = (Map<String, Object>) response.getDetails().get("tenant");

        final Tenant tenant = JacksonJsonHelper.convertValue(map, Tenant.class);

        // check if it is a service provider
        final TenantType tenantType = TenantType.getDataType(tenant.getTenantType());

        final String entityType = tenantType == TenantType.CUSTOMER ? EntityType.CUSTOMER : EntityType.SERVICE_PROVIDER;

        final List<EntityRecord> list = this.search(EntityDataType.RESOURCES, entityType, headerInfo);

        if (CollectionUtils.isEmpty(list)) { throw new ApiException(
                String.format("Could not retrieve %s with id %s", entityType, tenantId)); }

        final EntityRecord entityData = list.get(0);

        final Customer customer = new Customer(entityData);

        customer.setTenantId(String.valueOf(tenant.getId()));
        customer.setName(tenant.getName());
        customer.setDescription(tenant.getDescription());
        customer.setTenantType(tenant.getTenantType());
        customer.setParentId(String.valueOf(tenant.getParentId()));
        customer.setTimezone(tenant.getTimezone());

        return customer;
    }

    @SuppressWarnings("unchecked")
    public Set<Tenant> findAllServiceProvidersAndTenants(final String cconeTenantId) throws ApiException {

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(cconeTenantId, false);

        final Set<Tenant> tenants = new HashSet<>();

        final ApiResponse response = this.searchEntities(EntityDataType.TENANT, headerInfo, ApiResponse.class);

        final List<Map<String, Object>> records = (List<Map<String, Object>>) response.getDetails().get("tenants");

        Tenant tenant;

        for (final Map<String, Object> record : records) {

            tenant = JacksonJsonHelper.convertValue(record, Tenant.class);

            if (tenant != null) {
                tenants.add(tenant);
            }
        }

        return tenants;
    }

    @SuppressWarnings("unchecked")
    public Set<Tenant> findAllTenants(final String cconeTenantId) throws ApiException {

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(cconeTenantId, false);

        final Set<Tenant> tenants = new HashSet<>();

        final ApiResponse response = this.searchEntities(EntityDataType.TENANT, headerInfo, ApiResponse.class);

        final List<Map<String, Object>> records = (List<Map<String, Object>>) response.getDetails().get("tenants");

        Tenant tenant;

        TenantType tenantType;

        for (final Map<String, Object> record : records) {

            tenant = JacksonJsonHelper.convertValue(record, Tenant.class);

            if (tenant != null) {
                tenantType = TenantType.getDataType(tenant.getTenantType());

                if (tenantType == TenantType.CUSTOMER) {
                    tenants.add(tenant);
                }
            }
        }

        return tenants;
    }

    @SuppressWarnings("unchecked")
    public Set<Tenant> findAllServiceProviders(final String cconeTenantId) throws ApiException {

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(cconeTenantId, false);

        final Set<Tenant> tenants = new HashSet<>();

        final ApiResponse response = this.searchEntities(EntityDataType.TENANT, headerInfo, ApiResponse.class);

        final List<Map<String, Object>> records = (List<Map<String, Object>>) response.getDetails().get("tenants");

        Tenant tenant;

        TenantType tenantType;

        for (final Map<String, Object> record : records) {

            tenant = JacksonJsonHelper.convertValue(record, Tenant.class);

            if (tenant != null) {
                tenantType = TenantType.getDataType(tenant.getTenantType());

                if (tenantType == TenantType.SERVICE_PROVIDER) {
                    tenants.add(tenant);
                }
            }
        }

        return tenants;
    }

    public Set<String> findAllTenantIds(final String cconeTenantId) throws ApiException {

        final Set<Tenant> tenants = findAllTenants(cconeTenantId);

        final Set<String> ids = new HashSet<>();

        for (final Tenant tenant : tenants) {

            ids.add(String.valueOf(tenant.getId()));
        }

        return ids;
    }

    public ApiResponse findTenants(final String cconeTenantId) throws ApiException {

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(cconeTenantId, false);

        final ApiResponse response = this.searchEntities(EntityDataType.TENANT, headerInfo, ApiResponse.class);

        return response;
    }
    
    public String createTenant(final EntityDataType entityDataType, final Object entity) throws ApiException {

        LOGGER.debug("Creating record of entity {} is :  {}", entityDataType, entity);

        final String result = apiService.createSecuredEntity(entityDataType, entity);

        final ApiResponse apiResponse = JacksonJsonHelper.deserialize(result, ApiResponse.class);

        String id = "";

        final Link[] links = apiResponse.getLinks();

        for (final Link link : links) {
            id = getId(link);
        }

        LOGGER.debug("Response received for creating entity {} is {} and id {}", entityDataType, result, id);

        return id;
    }

    public String createEntity(final EntityDataType entityDataType, final Object entity,
            final HttpHeaderInfo headerInfo) throws ApiException {

        LOGGER.debug("Creating record {} of entity {} with header params {}", entity, entityDataType, headerInfo);

        final String result = apiService.createSecuredEntity(entityDataType, entity, headerInfo);

        final ApiResponse apiResponse = JacksonJsonHelper.deserialize(result, ApiResponse.class);

        String id = "";

        final Link[] links = apiResponse.getLinks();

        for (final Link link : links) {
            id = getId(link);
        }

        LOGGER.debug("Record has been created for entity {} with id {}, response is {}", entityDataType, id, result);

        return id;
    }

    public boolean updateEntity(final EntityDataType entityDataType, final Object entity,
            final HttpHeaderInfo headerInfo) throws ValidationException, ApiException {

        LOGGER.debug("Updating record {} of entity {} with header params {}", entity, entityDataType, headerInfo);

        final boolean result = apiService.updateSecuredEntity(entityDataType, entity, headerInfo);

        LOGGER.debug("Record has been updated for entity {} with data {}, response is {}", entityDataType, entity,
                result);

        return result;
    }

    public <T> T findEntityById(final EntityDataType entityDataType, final String id, final HttpHeaderInfo headerInfo,
            final Class<T> clazz) throws ApiException {

        LOGGER.debug("Retrieving record of entity {} with id {} and header params {}", entityDataType, id, headerInfo);

        final T result = apiService.findSecuredEntityById(entityDataType, id, headerInfo, clazz);

        LOGGER.debug("Response received for retrieving record of entity {} by id {} is {}", entityDataType, id, result);

        return result;
    }
    
    public <T> T findEntityByOrgId(final EntityDataType entityDataType, final String orgId, final HttpHeaderInfo headerInfo,
            final Class<T> clazz) throws ApiException {

        LOGGER.debug("Retrieving record of entity {} with id {} and header params {}", entityDataType, orgId, headerInfo);

        final T result = apiService.findSecuredEntityByOrgId(entityDataType, orgId,headerInfo, clazz);

        LOGGER.debug("Response received for retrieving record of entity {} by id {} is {}", entityDataType, orgId, result);

        return result;
    }
    
    public <T> T findEntityByCommonIdentity(final EntityDataType entityDataType, final String id, final HttpHeaderInfo headerInfo,
            final Class<T> clazz) throws ApiException {

        LOGGER.debug("Retrieving record of entity {} with id {} and header params {}", entityDataType, id, headerInfo);

        final T result = apiService.findSecuredEntityByCommonIdentity(entityDataType, id, headerInfo, clazz);

        LOGGER.debug("Response received for retrieving record of entity {} by id {} is {}", entityDataType, id, result);

        return result;
    }

    public <T> T findEntity(final EntityDataType entityDataType, final HttpHeaderInfo headerInfo, final Class<T> clazz)
            throws ApiException {

        LOGGER.debug("Retrieving record of entity {} with header params {}", entityDataType, headerInfo);

        final T result = apiService.findSecuredEntity(entityDataType, headerInfo, clazz);

        LOGGER.debug("Response received for retrieving record of entity {} is {}", entityDataType, result);

        return result;
    }
    
    private  <T> T findTenantByQueryParameters(EntityDataType entityDataType, Map<String, Object> params,
			HttpHeaderInfo headerInfo, Class<T> clazz) throws ApiException {
    	
    	LOGGER.debug("Retrieving record of entity {} with id {} and header params {}", entityDataType, params, headerInfo);
    	
    	final T result = apiService.findTenantByQueryParameters(entityDataType, params, headerInfo, clazz);
    	
		return result;
	}

    public <T> T searchEntities(final EntityDataType entityDataType, final HttpHeaderInfo headerInfo,
            final Class<T> clazz) throws ApiException {

        LOGGER.debug("Retrieving records of entity {} with header params {}", entityDataType, headerInfo);

        final T result = apiService.searchSecuredEntities(entityDataType, headerInfo, clazz);

        LOGGER.debug("Response received for retrieving record of entity {} is {}", entityDataType, result);

        return result;
    }

    public List<String> create(final EntityDataType entityDataType, final String type, final List<EntityRecord> rows,
            final HttpHeaderInfo headerInfo) throws ValidationException, ApiException {

        LOGGER.debug("Creating records {} of entity {}, type {} with header prams {}", rows, entityDataType, type,
                headerInfo);

        // validate entity data type, type, tenantId
        validate(entityDataType, type, headerInfo.getTenantId());

        // validate records
        validate(type, headerInfo.getTenantId(), rows);

        final String result = apiService.create(entityDataType, type, rows, headerInfo);

        final TypeReference<?> typeRef = new TypeReference<List<ApiResponse>>() {
        };

        final List<ApiResponse> apiResponses = JacksonJsonHelper.deserialize(result, typeRef);

        final List<String> ids = new ArrayList<>();

        String id;

        for (final ApiResponse apiResponse : apiResponses) {

            final Link[] links = apiResponse.getLinks();

            for (final Link link : links) {
                id = getId(link);
                ids.add(id);
            }
        }

        LOGGER.debug("Response received for creating records of entity {} type {} with ids {} is {}", entityDataType,
                type, ids, result);

        return ids;
    }

    public List<String> create(final EntityDataType entityDataType, final String type, final String parentId,
            final List<EntityRecord> rows, final HttpHeaderInfo headerInfo) throws ValidationException, ApiException {

        LOGGER.debug("Creating records {} of entity {}, type {} with header prams {}", rows, entityDataType, type,
                headerInfo);

        // validate entity data type, type, tenantId
        validate(entityDataType, type, headerInfo.getTenantId());

        // validate records
        validate(type, parentId, rows);

        final String result = apiService.create(entityDataType, type, rows, headerInfo);

        final TypeReference<?> typeRef = new TypeReference<List<ApiResponse>>() {
        };

        final List<ApiResponse> apiResponses = JacksonJsonHelper.deserialize(result, typeRef);

        final List<String> ids = new ArrayList<>();

        String id;

        for (final ApiResponse apiResponse : apiResponses) {

            final Link[] links = apiResponse.getLinks();

            for (final Link link : links) {
                id = getId(link);
                ids.add(id);
            }
        }

        LOGGER.debug("Response received for creating records of entity {} type {} with ids {} is {}", entityDataType,
                type, ids, result);

        return ids;
    }

    public boolean update(final EntityDataType entityDataType, final String type, final List<EntityRecord> rows,
            final HttpHeaderInfo headerInfo) throws ValidationException, ApiException {

        LOGGER.debug("Updating records {} of entity {}, type {} with header prams {}", rows, entityDataType, type,
                headerInfo);

        // validate entity data type, type, tenantId
        validate(entityDataType, type, headerInfo.getTenantId());

        // validate records
        validate(type, headerInfo.getLoginName(), rows);

        final boolean result = apiService.update(entityDataType, type, rows, headerInfo);

        LOGGER.debug("Response received for updating records of entity {} type {} is {}", entityDataType, type, result);

        return result;
    }

    public boolean activate(final EntityDataType entityDataType, final String type, final String id,
            final HttpHeaderInfo headerInfo) throws ValidationException, ApiException {

        LOGGER.debug("Activating entity {}, type {} with id {} and header params {}", entityDataType, type, id,
                headerInfo);

        // validate entity data type, type, tenantId
        validate(entityDataType, type, headerInfo.getTenantId());

        final boolean result = apiService.activate(entityDataType, type, id, headerInfo);

        LOGGER.debug("Response received for activating entity {}, type {} is {}", entityDataType, type, result);

        return result;
    }

    public EntityRecord findById(final EntityDataType entityDataType, final String type, final String id,
            final HttpHeaderInfo headerInfo) throws ApiException {

        LOGGER.debug("Retrieving record of entity {}, type {} by id {} with header params {}", entityDataType, type, id,
                headerInfo);

        final EntityRecord result = apiService.findById(entityDataType, type, id, headerInfo);

        LOGGER.debug("Response received to find record of entity {}, type {} by id {} is {}", entityDataType, type, id,
                result);

        return result;
    }

    public List<EntityRecord> findByIds(final EntityDataType entityDataType, final String type, final Set<String> ids,
            final HttpHeaderInfo headerInfo, final int partitionSize) throws ApiException {

        LOGGER.debug("Retrieving total records of entity {}, type {} by ids {} with header params {}", entityDataType,
                type, ids, headerInfo);

        LOGGER.debug("batch size of findByIds query : {}", partitionSize);

        final ArrayList<String> entityIds = new ArrayList<>(ids);

        final List<List<String>> idsChunk = ListUtils.partition(entityIds, partitionSize);

        final List<EntityRecord> recordsList = new ArrayList<>();

        List<EntityRecord> batchRecords;

        Set<String> subIdsSet;

        for (final List<String> idList : idsChunk) {

            subIdsSet = new HashSet<>(idList);

            batchRecords = this.findByIds(entityDataType, type, subIdsSet, headerInfo);

            if (CollectionUtils.isNotEmpty(batchRecords)) {
                recordsList.addAll(batchRecords);
            }
        }

        LOGGER.debug("Records received for the entity {}, type {} by ids {} are {}", entityDataType, type, ids,
                recordsList);

        return recordsList;
    }

    public List<EntityRecord> search(final EntityDataType entityDataType, final String type,
            final HttpHeaderInfo headerInfo) throws ApiException {

        LOGGER.debug("Searching records of entity {}, type {} with header params {}", entityDataType, type, headerInfo);

        final Query query = new Query();
        query.setAuxiliaryDataType(entityDataType);
        query.setObjectType(type);

        final List<EntityRecord> rows = apiService.search(query, headerInfo);

        LOGGER.debug("Response received for search query is : {}", rows);

        return rows;
    }

    public List<EntityRecord> search(final EntityDataType entityDataType, final String type,
            final Map<String, Object> attributes, final HttpHeaderInfo headerInfo) throws ApiException {

        LOGGER.debug("Searching records of entity {}, type {}, attributes {} with header params", entityDataType, type,
                attributes, headerInfo);

        final Query query = new Query();
        query.setAuxiliaryDataType(entityDataType);
        query.setObjectType(type);

        setQueryFilters(attributes, query);

        final List<EntityRecord> rows = apiService.search(query, headerInfo);

        LOGGER.debug("Records received for search query {} are {}", query, rows);

        return rows;
    }

    public List<EntityRecord> search(final EntityDataType entityDataType, final String type, final Query query,
            final HttpHeaderInfo headerInfo) throws ApiException {

        LOGGER.debug("Searching records of entity {}, type {}, query {} with header params", entityDataType, type,
                query, headerInfo);

        final List<EntityRecord> rows = apiService.search(query, headerInfo);

        LOGGER.debug("Records received for search query {} are {}", query, rows);

        return rows;
    }

    private void setQueryFilters(final Map<String, Object> attributes, final Query query) {

        if (MapUtils.isNotEmpty(attributes)) {

            final FliterGroupBuilder builder = FliterGroupBuilder.builder(attributes);

            final FilterGroup[] filterGroups = builder.build();

            query.setFilterGroups(filterGroups);
        }
    }

    public List<EntityRecord> search(final EntityDataType entityDataType, final String type, final Set<String> columns,
            final Map<String, Object> attributes, final HttpHeaderInfo headerInfo) throws ApiException {

        LOGGER.debug("Searching records of entity {}, type {}, columns {}, attributes {} with header params {}",
                entityDataType, type, columns, attributes, headerInfo);

        final Query query = new Query();
        query.setAuxiliaryDataType(entityDataType);
        query.setObjectType(type);

        if (CollectionUtils.isNotEmpty(columns)) {
            query.setColumns(columns.toArray(new String[columns.size()]));
        }

        setQueryFilters(attributes, query);

        final List<EntityRecord> rows = apiService.search(query, headerInfo);

        LOGGER.debug("Records received for search query {} are {}", query, rows);

        return rows;
    }

    public boolean delete(final EntityDataType entityDataType, final String type, final String id,
            final HttpHeaderInfo headerInfo) throws ApiException {

        LOGGER.debug("Deleting record of entity {}, type {}, id {} with header params {}", entityDataType, type, id,
                headerInfo);

        // validate entity data type, type, tenantId
        validate(entityDataType, type, headerInfo.getTenantId());

        final boolean result = apiService.delete(entityDataType, type, id, headerInfo);

        LOGGER.debug("Response received for deleting record of entity {}, type {}, id {} is {}", entityDataType, type,
                id, result);

        return result;

    }

    private List<EntityRecord> findByIds(final EntityDataType entityDataType, final String type, final Set<String> ids,
            final HttpHeaderInfo headerInfo) throws ApiException {

        LOGGER.debug("Retrieving record of entity {}, type {} by ids {} with header params {}", entityDataType, type,
                ids, headerInfo);

        final List<EntityRecord> records = apiService.findByIds(entityDataType, type, ids, headerInfo);

        LOGGER.debug("Records received to find records of entity {}, type {} by ids {} are {}", entityDataType, type,
                ids, records);

        return records;
    }

    private void validate(final EntityDataType entityDataType, final String type, final String tenantId)
            throws ValidationException {

        ErrorMessageInfo info;

        if (entityDataType == null) {
            info = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_DATA_TYPE_REQUIRED);
            throw new ValidationException("Validation error", 101, info);
        }

        // entity type
        if (StringUtils.isBlank(type)) {
            info = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_TYPE_REQUIRED);
            throw new ValidationException("Validation error", 101, info);
        }

        // tenant
        if (StringUtils.isBlank(tenantId)) {
            info = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_TENANT_REQUIRED);
            throw new ValidationException("Validation error", 101, info);
        }

    }

    private void validate(final String type, final String tenantId, final List<EntityRecord> records)
            throws ValidationException {

        // records
        if (CollectionUtils.isEmpty(records)) {
            final ErrorMessageInfo info = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_RECORDS_REQUIRED);
            throw new ValidationException("Validation error", 101, info);
        }

    }

    private String getId(final Link link) {

        // String id = "";
        // final Pattern p = Pattern.compile("/(\\d+)");
        // final Matcher m = p.matcher(link.getHref());
        //
        // while (m.find()) {
        // id = m.group();
        // }
        //
        return StringUtils.substringAfterLast(link.getHref(), "/");

    }

}
