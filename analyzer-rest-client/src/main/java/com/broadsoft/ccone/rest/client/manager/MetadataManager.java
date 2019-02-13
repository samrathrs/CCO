/**
 *
 */
package com.broadsoft.ccone.rest.client.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.broadsoft.ccone.rest.client.exception.ApiException;
import com.broadsoft.ccone.rest.client.pojo.EntityMetadata;
import com.broadsoft.ccone.rest.client.pojo.HttpHeaderInfo;
import com.broadsoft.ccone.rest.client.pojo.PlatformMetadata;
import com.broadsoft.ccone.rest.client.service.EntityApiService;

/**
 * @author svytla
 */
public class MetadataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataManager.class);

    @Autowired
    private EntityApiService apiService;

    public EntityMetadata getMetadataByUser(final String type, final String loginName) throws ApiException {

        LOGGER.debug("Going to get metadata of type {} using login {}", type, loginName);

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(loginName, true, false);

        final EntityMetadata metadata = apiService.getMetadata(type, headerInfo);

        LOGGER.debug("{} object metadata is {}", type, metadata);

        return metadata;
    }

    public EntityMetadata getMetadata(final String type, final String tenantId) throws ApiException {

        LOGGER.debug("Going to get metadata of tenant {} and type {}", tenantId, type);

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(tenantId, false);

        final EntityMetadata metadata = apiService.getMetadata(type, headerInfo);

        LOGGER.debug("{} object metadata is {}", type, metadata);

        return metadata;
    }

    public List<Map<String, String>> getMetadataRecords(final String type, final String tenantId) throws ApiException {

        final List<PlatformMetadata> records = getPlatformMetadataRecords(type, tenantId);

        final List<Map<String, String>> list = new ArrayList<>();

        Map<String, String> attributes;
        for (final PlatformMetadata record : records) {

            attributes = new LinkedHashMap<>();

            for (final Map.Entry<String, Object> entry : record.getAttributes().entrySet()) {
                attributes.put(entry.getKey(), String.valueOf(entry.getValue()));
            }

            list.add(attributes);

        }

        return list;

    }

    public List<PlatformMetadata> getPlatformMetadataRecords(final String type, final String tenantId)
            throws ApiException {

        LOGGER.debug("Going to get platform metadata values of tenant {} and type {}", tenantId, type);

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(tenantId, false);

        final List<PlatformMetadata> records = apiService.getMetadataRecords(type, headerInfo);

        Collections.sort(records, (e1, e2) -> new CompareToBuilder().append(e1.getId(), e2.getId()).toComparison());

        LOGGER.debug("{} object metadata is {}", type, records);

        return records;

    }

    public List<PlatformMetadata> getPlatformMetadataRecords(final String type, final String tenantId,
            final String parentId) throws ApiException {

        LOGGER.debug("Going to get platform metadata values of tenant {} and type {}", tenantId, type);

        final HttpHeaderInfo headerInfo = new HttpHeaderInfo(parentId, false);

        final List<PlatformMetadata> records = apiService.getMetadataRecords(type, headerInfo);

        Collections.sort(records, (e1, e2) -> new CompareToBuilder().append(e1.getId(), e2.getId()).toComparison());

        LOGGER.debug("{} object metadata is {}", type, records);

        return records;

    }

}
