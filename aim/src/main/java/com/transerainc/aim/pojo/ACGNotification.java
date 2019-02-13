/**
 * 
 */
package com.transerainc.aim.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public abstract class ACGNotification {
	public static final int METHOD_POST = 0;
	public static final int METHOD_GET = 1;

	protected List<String> acgUrlList;
	protected int method = METHOD_POST;
	protected String message;

	/**
	 * @return the acgUrl
	 */
	public List<String> getAcgUrls() {
		return acgUrlList;
	}

	/**
	 * @param acgUrl
	 *            the acgUrl to set
	 */
	public void setAcgUrls(List<String> acgUrlList) {
		this.acgUrlList = acgUrlList;
	}

	public void addAcgUrl(String url) {
		if (acgUrlList == null) {
			acgUrlList = new ArrayList<String>();
		}

		acgUrlList.add(url);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the method
	 */
	public int getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(int method) {
		this.method = method;
	}

	public String toString() {
		return "ACG URLs: " + acgUrlList + ", Message: " + message
			+ ", Method: " + method;
	}
}
