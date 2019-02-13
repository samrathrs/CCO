package com.broadsoft.ccone.rest.client.pojo;

import java.util.Set;

public class ResultInfo<D> extends BasePojo {

    private int status;
    private String message;
    private Set<String> errors;
    private D data;

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(final int status) {
        this.status = status;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * @return the errors
     */
    public Set<String> getErrors() {
        return errors;
    }

    /**
     * @param errors
     *            the errors to set
     */
    public void setErrors(final Set<String> errors) {
        this.errors = errors;
    }

    /**
     * @return the data
     */
    public D getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(final D data) {
        this.data = data;
    }

}
