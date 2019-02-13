/**
 *
 */
package com.broadsoft.ccone.rest.client.pojo;

public class Link {

    private String rel;
    private String method;
    private String href;

    /**
     * @return the rel
     */
    public String getRel() {
        return rel;
    }

    /**
     * @param rel
     *            the rel to set
     */
    public void setRel(final String rel) {
        this.rel = rel;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method
     *            the method to set
     */
    public void setMethod(final String method) {
        this.method = method;
    }

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * @param href
     *            the href to set
     */
    public void setHref(final String href) {
        this.href = href;
    }
}
