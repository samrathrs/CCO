/**
 *
 */
package com.broadsoft.ccone.rest.client.exception;

/**
 * @author swadeyar
 */
public class XmlException extends Exception {
    /**
    *
    */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public XmlException(final String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public XmlException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
