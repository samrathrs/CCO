/**
 *
 */
package com.broadsoft.ccone.rest.client.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.broadsoft.ccone.rest.client.constants.ActionType;
import com.broadsoft.ccone.rest.client.constants.CoreConstants;
import com.broadsoft.ccone.rest.client.exception.ApiException;
import com.broadsoft.ccone.rest.client.helper.AnalyzerAccessHelper;
import com.broadsoft.ccone.rest.client.helper.ApiPropertiesHelper;
import com.broadsoft.ccone.rest.client.helper.RestClientHelper;
import com.broadsoft.ccone.rest.client.pojo.EntityRequest;
import com.broadsoft.ccone.rest.client.pojo.HttpHeaderInfo;
import com.transerainc.tide.pojo.Query;

/**
 * @author svytla
 */
public abstract class TemporalApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemporalApiService.class);

    @Autowired
    protected RestClientHelper restClientHelper;

    @Autowired
    protected ApiPropertiesHelper propertiesHelper;

    @Autowired
    protected AnalyzerAccessHelper analyzerAccessHelper;

    protected enum AgentActivytHeaders {
        __ROW_TYPE, __INTERVAL, currentState__s, _0
    }

    protected enum ContactActivytHeaders {
        __ROW_TYPE, __INTERVAL, channelType__s, _0
    }

    protected enum AuditHeaders {
        ID, _0, _1, _2, _3, _4, _5, _6, _7, _8, _9
    }

    protected String createUrl(final String uri) {
        final String baseUrl = analyzerAccessHelper.getAnalyzerAccessDetails().getBaseUrl();
        final String url = String.format("%s%s", baseUrl, uri);
        LOGGER.debug("BASE URL : {} and URL {}", baseUrl, url);
        return url;
    }

    protected String search(final EntityRequest req, final Query query) throws ApiException {

        LOGGER.debug("Sending request to rest api with query {} and url {}", query, req.getUrl());

        final Map<String, Object> params = new HashMap<>();
        params.put("anchorId", query.getAnchorId());
        params.put("q", query);

        String data;
        try {
            data = restClientHelper.doSearch(req, params);
        } catch (final ApiException e) {
            final String msg = String.format("Exception in retrieving records of type %s using url %s", req.getType(),
                    req.getUrl());
            LOGGER.error(msg, e);
            throw e;
        }

        String result = StringUtils.substringAfterLast(data, CoreConstants.SEG_PROFILE);

        result = StringUtils.replace(result, "\"__ROW_TYPE\"", "__ROW_TYPE");

        result = StringUtils.trim(result);

        LOGGER.debug("Response received for query {} using url {} is {}", query, req.getUrl(), result);

        return result;

    }

    protected EntityRequest createRequest(final HttpHeaderInfo headerInfo, final ActionType actionType,
            final String url) {
        final EntityRequest request = new EntityRequest();
        request.setHeaderInfo(headerInfo);
        request.setActionType(actionType);
        request.setUrl(url);
        return request;
    }

}
