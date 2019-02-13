/**
 *
 */
package com.broadsoft.ccone.rest.client.exception;

import java.util.Set;

import com.broadsoft.ccone.rest.client.pojo.ErrorMessageInfo;

/**
 * @author svytla
 */
public class ConversionException extends ApiException {

    /**
     *
     */
    private static final long serialVersionUID = -5749799473205078728L;

    public ConversionException(final String message, final int code, final ErrorMessageInfo errorInfo,
            final Throwable cause) {
        super(message, code, errorInfo, cause);
    }

    public ConversionException(final String message, final int code, final ErrorMessageInfo errorInfo) {
        super(message, code, errorInfo);
    }

    public ConversionException(final String message, final int code, final Set<ErrorMessageInfo> errors,
            final Throwable cause) {
        super(message, code, errors, cause);
    }

    public ConversionException(final String message, final int code, final Set<ErrorMessageInfo> errors) {
        super(message, code, errors);
    }

    public ConversionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConversionException(final String message) {
        super(message);
    }

    public ConversionException(final Throwable cause) {
        super(cause);
    }

}
