/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author svytla
 */
public class ExceptionInfo {

    protected String message;
    protected Throwable cause;

    protected int code;
    protected Set<ErrorMessageInfo> errors = new LinkedHashSet<>();
    protected ApiResponse apiResponse;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(final Throwable cause) {
        this.cause = cause;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public Set<ErrorMessageInfo> getErrors() {
        return errors;
    }

    public void setErrors(final Set<ErrorMessageInfo> errors) {
        this.errors = errors;
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(final ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

}
