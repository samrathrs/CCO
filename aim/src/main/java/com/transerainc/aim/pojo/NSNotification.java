/**
 * 
 */
package com.transerainc.aim.pojo;

import java.util.List;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class NSNotification extends ACGNotification {
	private String context;

	public NSNotification(String context) {
		super();

		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.transerainc.aim.pojo.ACGNotification#setAcgUrls(java.util.List)
	 */
	@Override
	public void setAcgUrls(List<String> acgUrlList) {
		if (acgUrlList != null) {
			for (String url : acgUrlList) {
				addAcgUrl(url);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.transerainc.aim.pojo.ACGNotification#addAcgUrl(java.lang.String)
	 */
	@Override
	public void addAcgUrl(String url) {
		super.addAcgUrl((url + context));
	}

}
