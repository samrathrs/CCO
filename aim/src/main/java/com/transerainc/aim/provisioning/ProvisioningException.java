/**
 * 
 */
package com.transerainc.aim.provisioning;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ProvisioningException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ProvisioningException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ProvisioningException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ProvisioningException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ProvisioningException(Throwable cause) {
		super(cause);
	}

}
