/**
 *
 */
package com.broadsoft.ccone.rest.client.exception;

/**
 * @author svytla
 */
public class CacheException extends ApiException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public CacheException(final String message) {
        super(message);
    }

    public CacheException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public CacheException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
