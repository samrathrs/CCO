/**
 *
 */
package com.broadsoft.ccone.rest.client.exception;

import java.util.LinkedHashSet;
import java.util.Set;

import com.broadsoft.ccone.rest.client.pojo.ApiResponse;
import com.broadsoft.ccone.rest.client.pojo.ErrorMessageInfo;

/**
 * @author svytla
 */
public class ApiException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 6606267780920822926L;

    protected int code;

    protected Set<ErrorMessageInfo> errors = new LinkedHashSet<>();

    protected ApiResponse apiResponse;

    public ApiException(final String message) {
        super(message);
    }

    public ApiException(final Throwable cause) {
        super(cause);
    }

    public ApiException(final String message, final int code) {
        super(message);
        this.code = code;
    }

    public ApiException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ApiException(final String message, final int code, final ErrorMessageInfo errorInfo,
            final ApiResponse apiResponse, final Throwable cause) {
        super(message, cause);
        this.code = code;
        errors.add(errorInfo);
        this.apiResponse = apiResponse;
    }

    public ApiException(final String message, final int code, final Set<ErrorMessageInfo> errors,
            final ApiResponse apiResponse, final Throwable cause) {
        super(message, cause);
        this.code = code;
        this.errors = errors;
        this.apiResponse = apiResponse;
    }

    public ApiException(final String message, final int code, final ErrorMessageInfo errorInfo,
            final ApiResponse apiResponse) {
        super(message);
        this.code = code;
        errors.add(errorInfo);
        this.apiResponse = apiResponse;
    }

    public ApiException(final String message, final int code, final Set<ErrorMessageInfo> errors,
            final ApiResponse apiResponse) {
        super(message);
        this.code = code;
        this.errors = errors;
        this.apiResponse = apiResponse;
    }

    public ApiException(final String message, final int code, final ErrorMessageInfo errorInfo, final Throwable cause) {
        super(message, cause);
        this.code = code;
        errors.add(errorInfo);
    }

    public ApiException(final String message, final int code, final Set<ErrorMessageInfo> errors,
            final Throwable cause) {
        super(message, cause);
        this.code = code;
        this.errors = errors;
    }

    public ApiException(final String message, final int code, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ApiException(final String message, final int code, final ErrorMessageInfo errorInfo) {
        super(message);
        this.code = code;
        errors.add(errorInfo);
    }

    public ApiException(final String message, final int code, final Set<ErrorMessageInfo> errors) {
        super(message);
        this.code = code;
        this.errors = errors;
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(final ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the errors
     */
    public Set<ErrorMessageInfo> getErrors() {
        return errors;
    }

    /**
     * @param errors
     *            the errors to set
     */
    public void setErrors(final Set<ErrorMessageInfo> errors) {
        this.errors = errors;
    }

}
