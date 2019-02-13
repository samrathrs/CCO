/**
 *
 */
package com.broadsoft.ccone.rest.client.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.broadsoft.ccone.rest.client.constants.ActionType;
import com.broadsoft.ccone.rest.client.constants.CoreConstants;
import com.broadsoft.ccone.rest.client.constants.DataType;
import com.broadsoft.ccone.rest.client.constants.EntityDataType;
import com.broadsoft.ccone.rest.client.constants.MessageKeys;
import com.broadsoft.ccone.rest.client.exception.ApiException;
import com.broadsoft.ccone.rest.client.exception.JsonException;
import com.broadsoft.ccone.rest.client.helper.AnalyzerAccessHelper;
import com.broadsoft.ccone.rest.client.helper.ApiPropertiesHelper;
import com.broadsoft.ccone.rest.client.helper.RestClientHelper;
import com.broadsoft.ccone.rest.client.pojo.ApiResponse;
import com.broadsoft.ccone.rest.client.pojo.EntityAttribute;
import com.broadsoft.ccone.rest.client.pojo.EntityMetadata;
import com.broadsoft.ccone.rest.client.pojo.EntityRecord;
import com.broadsoft.ccone.rest.client.pojo.EntityRequest;
import com.broadsoft.ccone.rest.client.pojo.EntityResponse;
import com.broadsoft.ccone.rest.client.pojo.ErrorMessageInfo;
import com.broadsoft.ccone.rest.client.pojo.HttpHeaderInfo;
import com.broadsoft.ccone.rest.client.pojo.PlatformMetadata;
import com.broadsoft.ccone.rest.client.pojo.Query;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * @author svytla
 */
public abstract class BaseEntityApiService<Request> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEntityApiService.class);

    @Autowired
    protected RestClientHelper restClientHelper;

    @Autowired
    protected ApiPropertiesHelper propertiesHelper;

    @Autowired
    protected AnalyzerAccessHelper analyzerAccessHelper;

    protected String createUrl(final String uri) {
        final String baseUrl = analyzerAccessHelper.getAnalyzerAccessDetails().getBaseUrl();
        final String url = String.format("%s%s", baseUrl, uri);
        LOGGER.trace("BASE URL : {} and URL {}", baseUrl, url);
        return url;
    }

    protected <T> EntityResponse<T> createErrorResponse(final ApiResponse apiResponse) {
        final EntityResponse<T> response = new EntityResponse<>();
        response.setStatus(apiResponse.getCode());
        response.setApiResponse(apiResponse);
        return response;
    }

    protected <T> EntityResponse<T> createResponse(final T data) {
        final EntityResponse<T> response = new EntityResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(data);
        return response;
    }

    protected EntityRequest transformRequest(final HttpHeaderInfo headerInfo, final List<EntityRecord> entities,
            final EntityDataType entityDataType, final String type, final ActionType actionType, final String url) {
        final EntityRequest request = new EntityRequest();
        request.setHeaderInfo(headerInfo);
        request.setEntities(entities);
        request.setEntityDataType(entityDataType);
        request.setType(type);
        request.setActionType(actionType);
        request.setUrl(url);
        return request;
    }

    protected EntityRequest transformRequest(final EntityDataType entityDataType, final String type,
            final HttpHeaderInfo headerInfo, final ActionType actionType, final String url) {
        final EntityRequest request = new EntityRequest();
        request.setHeaderInfo(headerInfo);
        request.setEntityDataType(entityDataType);
        request.setType(type);
        request.setActionType(actionType);
        request.setUrl(url);
        return request;
    }

    protected EntityRequest transformRequest(final EntityDataType entityDataType, final HttpHeaderInfo headerInfo,
            final ActionType actionType, final String url) {
        final EntityRequest request = new EntityRequest();
        request.setHeaderInfo(headerInfo);
        request.setEntityDataType(entityDataType);
        request.setActionType(actionType);
        request.setUrl(url);
        return request;
    }

    protected EntityRequest transformRequest(final EntityDataType entityDataType, final ActionType actionType,
            final String url) {
        final EntityRequest request = new EntityRequest();
        request.setActionType(actionType);
        request.setUrl(url);
        return request;
    }

    protected EntityRequest transformRequest(final HttpHeaderInfo headerInfo, final ActionType actionType,
            final String url) {
        final EntityRequest request = new EntityRequest();
        request.setHeaderInfo(headerInfo);
        request.setActionType(actionType);
        request.setUrl(url);
        return request;
    }

    protected EntityRequest transformRequest(final String type, final HttpHeaderInfo headerInfo,
            final ActionType actionType, final String url) {
        final EntityRequest request = new EntityRequest();
        request.setType(type);
        request.setHeaderInfo(headerInfo);
        request.setActionType(actionType);
        request.setUrl(url);
        return request;
    }

    protected EntityAttribute createIdentifierAttribute() {

        final EntityAttribute attr = new EntityAttribute();
        attr.setAttribute("id");
        attr.setCustomAttribute(false);
        attr.setDataType(DataType.LONG.name());
        attr.setDefaultValue("0");
        attr.setLabel("Id");
        attr.setMandatory(true);
        attr.setUnique(true);

        return attr;
    }

    /**
     * @param data
     * @param e
     * @throws ApiException
     */
    protected ApiException createException(final String data, final JsonException e) throws ApiException {
        final ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value());

        final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_INTERNAL_SERVER);

        return new ApiException(String.format("Error in parsing entity data : %s", data), apiResponse.getCode(),
                errorInfo, apiResponse, e);
    }

    protected Set<String> getMetadataIgnoreAttributes() {

        final String values = propertiesHelper.getYmlProperty(CoreConstants.METADATA_IGNORE_ATTRIBUTES);

        final Set<String> ignoreAttributes = new HashSet<>(Arrays.asList(StringUtils.split(values, ",")));

        return ignoreAttributes;
    }

    protected boolean isJSONValid(final String data) {
        final JsonParser jsonParser = new JsonParser();
        final JsonElement jsonElement = jsonParser.parse(data);
        if (jsonElement.isJsonArray()) { return true; }
        return false;
    }

    public abstract EntityMetadata getMetadata(final EntityRequest req) throws ApiException;

    public abstract List<PlatformMetadata> getPlatformMetadata(final EntityRequest req) throws ApiException;

    public abstract String create(Request req, final Map<String, Object> uriVariables) throws ApiException;

    public abstract boolean update(Request req, final Map<String, Object> uriVariables) throws ApiException;

    public abstract boolean delete(Request req, final Map<String, Object> uriVariables) throws ApiException;

    public abstract EntityRecord retrieve(Request req, final Map<String, Object> uriVariables) throws ApiException;

    public abstract List<EntityRecord> search(Request req, Query query) throws ApiException;
}
