/**
 *
 */
package com.broadsoft.ccone.rest.client.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.broadsoft.ccone.rest.client.constants.AttributeName;
import com.broadsoft.ccone.rest.client.constants.EntityDataType;
import com.broadsoft.ccone.rest.client.constants.EntityType;
import com.broadsoft.ccone.rest.client.constants.TenantType;
import com.broadsoft.ccone.rest.client.exception.ApiException;
import com.broadsoft.ccone.rest.client.helper.JacksonJsonHelper;
import com.broadsoft.ccone.rest.client.manager.EntityRecordManager;
import com.broadsoft.ccone.rest.client.pojo.ApiResponse;
import com.broadsoft.ccone.rest.client.pojo.CustomerModel;
import com.broadsoft.ccone.rest.client.pojo.EntityModel;
import com.broadsoft.ccone.rest.client.pojo.EntityRecord;
import com.broadsoft.ccone.rest.client.pojo.HttpHeaderInfo;
import com.broadsoft.ccone.rest.client.pojo.Tenant;

/**
 * @author svytla
 */
@Component
public class TenantUserService {

    @Autowired
    protected EntityRecordManager recordManager;

    public EntityRecord findByLoginName(final String loginName, final Set<String> columns) throws ApiException {

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(loginName, true, false);

        final Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put(AttributeName.LOGIN, loginName);

        final List<EntityRecord> list = recordManager.search(EntityDataType.USER_DATA, EntityType.USER, columns,
                attributes, headerInfo);

        if (CollectionUtils.isEmpty(
                list)) { throw new ApiException(String.format("User not found with login name %s", loginName)); }

        return list.get(0);
    }

    @SuppressWarnings("unchecked")
    public CustomerModel findByTenantId(final String tenantId, final Locale locale) throws ApiException {

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(tenantId, false);

        final ApiResponse response = recordManager.findEntityById(EntityDataType.TENANT, tenantId, headerInfo,
                ApiResponse.class);

        final Map<String, Object> map = (Map<String, Object>) response.getDetails().get("tenant");

        final Tenant tenant = JacksonJsonHelper.convertValue(map, Tenant.class);

        final TenantType tenantType = TenantType.getDataType(tenant.getTenantType());

        final String entityType = tenantType == TenantType.CUSTOMER ? EntityType.CUSTOMER : EntityType.SERVICE_PROVIDER;

        final List<EntityRecord> list = recordManager.search(EntityDataType.RESOURCES, entityType, headerInfo);

        if (CollectionUtils.isEmpty(list)) { throw new ApiException(
                String.format("%s not found by tenant id %d", entityType, tenant.getId())); }

        final EntityRecord entityRecord = list.get(0);

        final EntityModel customer = new EntityModel();

        customer.setId(entityRecord.getId());
        customer.setType(entityRecord.getType());
        customer.setAttributes(entityRecord.getAttributes());

        final CustomerModel model = new CustomerModel();

        model.setTenantId(String.valueOf(tenant.getId()));
        model.setName(tenant.getName());
        model.setDescription(tenant.getDescription());
        model.setTenantType(tenant.getTenantType());
        model.setParentId(String.valueOf(tenant.getParentId()));
        model.setTimezone(tenant.getTimezone());
        model.setCustomer(customer);

        return model;
    }

}
