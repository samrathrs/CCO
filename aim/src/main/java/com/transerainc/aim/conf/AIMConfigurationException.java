/**
 * 
 */
package com.transerainc.aim.conf;

public class AIMConfigurationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AIMConfigurationException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AIMConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public AIMConfigurationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AIMConfigurationException(Throwable cause) {
		super(cause);
	}

}
