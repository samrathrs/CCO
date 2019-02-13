/**
 *
 */
package com.broadsoft.ccone.rest.client.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.broadsoft.ccone.rest.client.constants.ActionType;
import com.broadsoft.ccone.rest.client.constants.CoreConstants;
import com.broadsoft.ccone.rest.client.constants.EntityDataType;
import com.broadsoft.ccone.rest.client.exception.ApiException;
import com.broadsoft.ccone.rest.client.helper.ApiPropertiesHelper;
import com.broadsoft.ccone.rest.client.pojo.EntityMetadata;
import com.broadsoft.ccone.rest.client.pojo.EntityRecord;
import com.broadsoft.ccone.rest.client.pojo.EntityRequest;
import com.broadsoft.ccone.rest.client.pojo.HttpHeaderInfo;
import com.broadsoft.ccone.rest.client.pojo.PlatformMetadata;
import com.broadsoft.ccone.rest.client.pojo.Query;

/**
 * @author svytla
 */
@Service
public class BlobApiService extends BaseEntityApiService<EntityRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlobApiService.class);

    @Autowired
    private ApiPropertiesHelper propertiesHelper;

    public String createOrUpdate(final EntityDataType entityDataType, final byte[] data,
            final Map<String, Object> uriVariables, final HttpHeaderInfo headerInfo) throws ApiException {

        final String apiKey = String.format(CoreConstants.CREATE_URI, StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.CREATE, url);

        String response;
        try {
            response = restClientHelper.doPutBlob(entityRequest, data, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in creating blob for data-type %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        return response;
    }

    public byte[] findById(final EntityDataType entityDataType, final String id, final HttpHeaderInfo headerInfo)
            throws ApiException {

        final String apiKey = String.format(CoreConstants.RETRIEVE_URI,
                StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("blobId", id);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.READ, url);

        byte[] data;
        try {
            data = restClientHelper.doGetBlob(entityRequest, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving blob of data-type %s using url %s",
                    entityDataType.name(), url);
            LOGGER.error(msg, e);
            throw e;
        }

        return data;
    }

    public boolean delete(final EntityDataType entityDataType, final String id, final HttpHeaderInfo headerInfo)
            throws ApiException {

        final String apiKey = String.format(CoreConstants.DELETE_URI, StringUtils.lowerCase(entityDataType.getName()));

        final String uri = propertiesHelper.getYmlProperty(apiKey);

        final String url = createUrl(uri);

        final EntityRequest entityRequest = transformRequest(entityDataType, headerInfo, ActionType.DELETE, url);

        final Map<String, Object> uriVariables = new LinkedHashMap<>();
        uriVariables.put("blobId", id);

        return delete(entityRequest, uriVariables);
    }

    @Override
    public EntityMetadata getMetadata(final EntityRequest req) throws ApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PlatformMetadata> getPlatformMetadata(final EntityRequest req) throws ApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String create(final EntityRequest req, final Map<String, Object> uriVariables) throws ApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean update(final EntityRequest req, final Map<String, Object> uriVariables) throws ApiException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(final EntityRequest req, final Map<String, Object> uriVariables) throws ApiException {

        try {
            restClientHelper.doDelete(req, uriVariables);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in deleting blob of data-type %s using url %s with params %s",
                    req.getEntityDataType().name(), req.getUrl(), uriVariables.toString());
            LOGGER.error(msg, e);
            throw e;
        }

        return true;
    }

    @Override
    public EntityRecord retrieve(final EntityRequest req, final Map<String, Object> uriVariables) throws ApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<EntityRecord> search(final EntityRequest req, final Query query) throws ApiException {
        // TODO Auto-generated method stub
        return null;
    }

}
