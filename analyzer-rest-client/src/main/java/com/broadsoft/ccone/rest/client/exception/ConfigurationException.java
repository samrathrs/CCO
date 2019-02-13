/**
 *
 */
package com.broadsoft.ccone.rest.client.exception;

/**
 * @author svytla
 */
public class ConfigurationException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public ConfigurationException(final String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ConfigurationException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
