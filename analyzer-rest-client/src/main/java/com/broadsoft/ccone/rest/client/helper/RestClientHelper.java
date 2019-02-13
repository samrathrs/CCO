/**
 *
 */
package com.broadsoft.ccone.rest.client.helper;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.broadsoft.ccone.rest.client.constants.CoreConstants;
import com.broadsoft.ccone.rest.client.constants.MessageKeys;
import com.broadsoft.ccone.rest.client.exception.ApiException;
import com.broadsoft.ccone.rest.client.exception.CacheException;
import com.broadsoft.ccone.rest.client.exception.JsonException;
import com.broadsoft.ccone.rest.client.pojo.ApiKeyInfo;
import com.broadsoft.ccone.rest.client.pojo.ApiResponse;
import com.broadsoft.ccone.rest.client.pojo.EntityRequest;
import com.broadsoft.ccone.rest.client.pojo.ErrorMessageInfo;
import com.broadsoft.ccone.rest.client.pojo.HttpHeaderInfo;

/**
 * @author svytla
 */
@Component
public class RestClientHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientHelper.class);

    private static final String HEADER_FROM = "From";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_X_BSFT_UNIQUE_CONSTRAINT = "X-BSFT-Unique-Constraint";

    @Autowired
    private ApiPropertiesHelper propertiesHelper;

    @Autowired
    private AnalyzerAccessHelper sytemApiKeyHelper;

    @Autowired
    private RestTemplate restTemplate;

    public String doPost(final EntityRequest request, final Object data) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(data, httpHeaders);

            LOGGER.debug("Sending create entity request to url {} with request data {} and headers {}",
                    request.getUrl(), JacksonJsonHelper.serialize(data), httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.POST, entity,
                    String.class);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in creating entity using url %s ", request.getUrl());

                final ErrorMessageInfo msgInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_CREATION);

                throw new ApiException(msg, response.getStatusCode().value(), msgInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doPost(final EntityRequest request, final Object data, final Map<String, Object> uriVariables)
            throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(data, httpHeaders);

            LOGGER.debug("Sending create entity request to url {} with request data {} and params {}, headers {}",
                    request.getUrl(), JacksonJsonHelper.serialize(data), uriVariables, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.POST, entity,
                    String.class, uriVariables);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in creating entity using url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_CREATION);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doPost(final EntityRequest request, final Map<String, Object> uriVariables) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            LOGGER.debug("Sending create entity request to url {} with params {} and headers {}", request.getUrl(),
                    uriVariables, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.POST, entity,
                    String.class, uriVariables);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in creating entity using url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_CREATION);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doPut(final EntityRequest request, final Object data) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(data, httpHeaders);

            LOGGER.debug("Sending update entity request to url {} with request data {} and headers {}",
                    request.getUrl(), JacksonJsonHelper.serialize(data), httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.PUT, entity,
                    String.class);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in updating entity using url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_UPDATION);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doPut(final EntityRequest request, final Map<String, Object> uriVariables) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            LOGGER.debug("Sending update entity request to url {} with params {} and headers {}", request.getUrl(),
                    uriVariables, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.PUT, entity,
                    String.class, uriVariables);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in updating entity using url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_UPDATION);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doPutBlob(final EntityRequest request, final byte[] data, final Map<String, Object> uriVariables)
            throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(data, httpHeaders);

            LOGGER.debug("Sending update entity request to url {} with uri params {} and headers {}", request.getUrl(),
                    uriVariables, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.PUT, entity,
                    String.class, uriVariables);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in updating entity using url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_UPDATION);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doPut(final EntityRequest request, final Object data, final Map<String, Object> uriVariables)
            throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(data, httpHeaders);

            LOGGER.debug("Sending update entity request to url {} with request data {}, uri params {} and headers {}",
                    request.getUrl(), JacksonJsonHelper.serialize(data), uriVariables, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.PUT, entity,
                    String.class, uriVariables);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in updating entity using url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_UPDATION);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public void doPatch(final EntityRequest request, final Map<String, Object> uriVariables) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            LOGGER.debug("Sending  patch entity request to url {} with params {} and headers {}", request.getUrl(),
                    uriVariables, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.PATCH, entity,
                    String.class, uriVariables);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in updating entity using url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_UPDATION);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            // return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doGet(final EntityRequest request, final Object data) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(data, httpHeaders);

            LOGGER.debug("Retrieving data from url {} with request data {} and headers {}", request.getUrl(),
                    JacksonJsonHelper.serialize(data), httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.GET, entity,
                    String.class);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in retrieving data from url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_RETRIEVAL);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doGet(final EntityRequest request, final Map<String, Object> uriVariables) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            LOGGER.debug("Retrieving data from url {} with uri params {} and headers {}", request.getUrl(),
                    uriVariables, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.GET, entity,
                    String.class, uriVariables);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in retrieving data from url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_RETRIEVAL);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public byte[] doGetBlob(final EntityRequest request, final Map<String, Object> uriVariables) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            LOGGER.debug("Retrieving data from url {} with uri params {} and headers {}", request.getUrl(),
                    uriVariables, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<byte[]> response = restTemplate.exchange(request.getUrl(), HttpMethod.GET, entity,
                    byte[].class, uriVariables);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(new String(response.getBody()),
                        ApiResponse.class);

                final String msg = String.format("Error in retrieving data from url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_RETRIEVAL);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doGetByParams(final EntityRequest request, final Map<String, Object> queryParams)
            throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(request.getUrl());

            if (MapUtils.isNotEmpty(queryParams)) {
                for (final Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    builder.queryParam(entry.getKey(), entry.getValue());
                }
            }

            LOGGER.debug("Retrieving data from url {} with params {} and headers {}", request.getUrl(), queryParams,
                    httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate
                    .exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in retrieving data from url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_RETRIEVAL);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException  e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doGetSearch(final EntityRequest request, final Map<String, Object> queryParams) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(request.getUrl());

            if (MapUtils.isNotEmpty(queryParams)) {
                for (final Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    builder.queryParam(entry.getKey(), JacksonJsonHelper.serialize(entry.getValue()));
                }
            }

            final URI uri = builder.build().encode().toUri();

            LOGGER.debug("Retrieving data from url {} with params {} and headers {}", uri.toString(), queryParams,
                    httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", uri.toString(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in retrieving data from url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_RETRIEVAL);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doSearch(final EntityRequest request, final Map<String, Object> queryParams) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(request.getUrl());

            if (MapUtils.isNotEmpty(queryParams)) {
                for (final Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    builder.queryParam(entry.getKey(), JsonHelper.serialize(entry.getValue()));
                }
            }

            final URI uri = builder.build().encode().toUri();

            LOGGER.debug("Retrieving data from url {} with params {} and headers {}", uri.toString(), queryParams,
                    httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", uri.toString(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in retrieving data from url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_RETRIEVAL);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doGet(final EntityRequest request) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            LOGGER.debug("Retrieving data from url {} and headers {}", request.getUrl(), httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.GET, entity,
                    String.class);

            LOGGER.debug("Response received from url {} is {} and it took {} ms", request.getUrl(), response.getBody(),
                    System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in retrieving data from url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_RETRIEVAL);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public void doDelete(final EntityRequest request, final Object data, final Map<String, Object> uriVariables)
            throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(data, httpHeaders);

            LOGGER.debug("Sending delete entity request to url {} with request data {} and params {}, headers {}",
                    request.getUrl(), JacksonJsonHelper.serialize(data), uriVariables, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.DELETE, entity,
                    String.class, uriVariables);

            LOGGER.debug("Response received for delete entity from url {} is {} and it took {} ms", request.getUrl(),
                    response.getBody(), System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in deleting entity with url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_DELETION);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            // return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public String doDelete(final EntityRequest request, final Map<String, Object> uriVariables) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            LOGGER.debug("Sending delete entity request to url {} with params {} and headers {}", request.getUrl(),
                    uriVariables, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.DELETE, entity,
                    String.class, uriVariables);

            LOGGER.debug("Response received for delete entity from url {} is {} and it took {} ms", request.getUrl(),
                    response.getBody(), System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in deleting entity with url %s ", request.getUrl());

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_DELETION);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    public void doDelete(final EntityRequest request, final String url) throws ApiException {
        try {

            final ApiKeyInfo keyInfo = getSystemApiInfo();

            final HttpHeaders httpHeaders = createHeaders(keyInfo, request);

            final HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

            LOGGER.debug("Sending delete entity request to url {} and headers {}", url, httpHeaders);

            final long start = System.currentTimeMillis();

            final ResponseEntity<String> response = restTemplate.exchange(request.getUrl(), HttpMethod.DELETE, entity,
                    String.class);

            LOGGER.debug("Response received for delete entity from url {} is {} and it took {} ms", request.getUrl(),
                    response.getBody(), System.currentTimeMillis() - start);

            if (response.getStatusCode().value() >= HttpStatus.MULTIPLE_CHOICES.value()) {

                final ApiResponse apiResponse = JacksonJsonHelper.deserialize(response.getBody(), ApiResponse.class);

                final String msg = String.format("Error in deleting entity with url %s ", url);

                final ErrorMessageInfo errorInfo = new ErrorMessageInfo(MessageKeys.ERROR_ENTITY_DELETION);

                throw new ApiException(msg, response.getStatusCode().value(), errorInfo, apiResponse);
            }

            delayResponse(request.getHeaderInfo());

            // return response.getBody();

        } catch (final RestClientException | CacheException | JsonException e) {
            final ApiException ae = createException(e);
            throw ae;
        }
    }

    private ApiKeyInfo getSystemApiInfo() throws CacheException {
        return sytemApiKeyHelper.getInfo();
    }

    private HttpHeaders createHeaders(final ApiKeyInfo keyInfo, final EntityRequest request) throws ApiException {

        // validate signed key
        if (StringUtils.isBlank(keyInfo.getIssuedTo())
                || StringUtils.isBlank(keyInfo.getSignature())) { throw new ApiException(
                        String.format("Key info not found for tenant %s", keyInfo.getTenantId())); }

        final HttpHeaderInfo headerInfo = request.getHeaderInfo();
        String auth = null;

        if (headerInfo != null) {

            if (CoreStringUtils.isBlank(headerInfo.getLoginName())) {
                auth = String.format("%s; tenantId=%s", keyInfo.getSignature(), headerInfo.getTenantId());
            } else {
                auth = CoreStringUtils.isBlank(headerInfo.getTenantId())
                        ? String.format("%s; user=%s", keyInfo.getSignature(), headerInfo.getLoginName())
                        : String.format("%s; user=%s; tenantId=%s", keyInfo.getSignature(), headerInfo.getLoginName(),
                                headerInfo.getTenantId());
            }

        } else {
            auth = String.format("%s", keyInfo.getSignature());
        }

        final Map<String, String> map = new LinkedHashMap<>();
        map.put(HEADER_FROM, keyInfo.getIssuedTo());
        map.put(HEADER_AUTHORIZATION, auth);

        if (headerInfo != null && CollectionUtils.isNotEmpty(headerInfo.getUniqueueColumns())) {
            map.put(HEADER_X_BSFT_UNIQUE_CONSTRAINT, StringUtils.join(headerInfo.getUniqueueColumns(), ","));
        }

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        for (final Map.Entry<String, String> param : map.entrySet()) {
            httpHeaders.set(StringUtils.trim(param.getKey()), StringUtils.trim(param.getValue()));
        }

        return httpHeaders;
    }

    private ApiException createException(final Exception e) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = "";
        ErrorMessageInfo info = null;
        if (e instanceof HttpClientErrorException) {
            status = ((HttpClientErrorException) e).getStatusCode().value();
            message = ((HttpClientErrorException) e).getResponseBodyAsString();
            info = new ErrorMessageInfo(MessageKeys.ERROR_INTERNAL_SERVER);
        } else if (e instanceof ResourceAccessException) {
            status = HttpStatus.REQUEST_TIMEOUT.value();
            message = e.getMessage();
            info = new ErrorMessageInfo(MessageKeys.ERROR_COMMUNICATION_INTERRUPTION);
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            message = e.getMessage();
            info = new ErrorMessageInfo(MessageKeys.ERROR_INTERNAL_SERVER);
        }
        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(status);
        return new ApiException(message, status, info, apiResponse, e);
    }

    private void delayResponse(final HttpHeaderInfo headerInfo) {

        if (headerInfo != null && headerInfo.isDelayResonse()) {

            final long delay = NumberUtils.toLong(propertiesHelper.getYmlProperty(CoreConstants.API_DELAY_IN_RESPONSE),
                    CoreConstants.DEFAULT_DELAY);

            try {
                Thread.sleep(delay);
            } catch (final Exception e) {
                LOGGER.debug("Error in pausing response", e);
            }
        }
    }
}
