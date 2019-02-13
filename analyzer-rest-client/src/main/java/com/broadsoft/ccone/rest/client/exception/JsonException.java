/**
 *
 */
package com.broadsoft.ccone.rest.client.exception;

/**
 * @author svytla
 */
public class JsonException extends ApiException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public JsonException(final String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public JsonException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
