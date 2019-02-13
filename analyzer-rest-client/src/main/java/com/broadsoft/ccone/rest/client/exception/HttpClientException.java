/**
 *
 */
package com.broadsoft.ccone.rest.client.exception;

/**
 * @author svytla
 */
public class HttpClientException extends Exception {

    /**
    *
    */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public HttpClientException(final String message) {
        super(message);
    }

    public HttpClientException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public HttpClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
