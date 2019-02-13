package com.broadsoft.ccone.rest.client.pojo;

import java.util.Arrays;
import java.util.Map;

public class ApiResponse {

    private int code;
    private Map<String, Object> details;
    private Link[] links;
    private boolean internal;

    public ApiResponse() {
        super();
    }

    public ApiResponse(final int code) {
        super();
        this.code = code;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(final int code) {
        this.code = code;
    }

    /**
     * @return the details
     */
    public Map<String, Object> getDetails() {
        return details;
    }

    /**
     * @param details
     *            the details to set
     */
    public void setDetails(final Map<String, Object> details) {
        this.details = details;
    }

    /**
     * @return the links
     */
    public Link[] getLinks() {
        return links;
    }

    /**
     * @param links
     *            the links to set
     */
    public void setLinks(final Link[] links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "Response [code=" + code + ", details=" + details + ", links=" + Arrays.toString(links) + "]";
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(final boolean internal) {
        this.internal = internal;
    }
}
