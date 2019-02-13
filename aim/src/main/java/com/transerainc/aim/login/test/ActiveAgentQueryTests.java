/**
 * 
 */
package com.transerainc.aim.login.test;

import java.util.Date;

import com.transerainc.tam.pool.TAMThreadPool;
import com.transerainc.tam.util.HttpUtil;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class ActiveAgentQueryTests extends TestCase {
	public void testActiveAgentQuery() throws Exception {
		// String url =
		// "http://miami:8786/aim/lookupActiveAgentsIntf?tenantId=2&subStatus=Available,Connected,Idle,WrapUp";
		String url = "http://miami:8786/aim/lookupActiveAgentsIntf?tenantId=2";
		TAMThreadPool tpool = new TAMThreadPool(5);
		tpool.execute(new URLExecutor1(url));
		// tpool.execute(new URLExecutor1(url));
		// tpool.execute(new URLExecutor1(url));
		// tpool.execute(new URLExecutor1(url));
		// tpool.execute(new URLExecutor1(url));

		Thread.sleep(1200000);
	}
}

class URLExecutor1 implements Runnable {
	String url;

	public URLExecutor1(String url) {
		this.url = url;
	}

	public void run() {
		try {
			int longest = 0;
			int shortest = Integer.MAX_VALUE;
			long totalTime = 0;
			long tsAll1 = System.currentTimeMillis();
			int max = 100;
			for (int i = 0; i < max; i++) {
				long ts1 = System.currentTimeMillis();
				HttpUtil.doHttpGet(url, 5000, url);
				long ts2 = System.currentTimeMillis();

				int diff = (int) (ts2 - ts1);
				totalTime += diff;

				if (diff < shortest) {
					shortest = diff;
				}

				if (diff > longest) {
					longest = diff;
				}

				if (i % 10 == 0) {
					System.out.println((new Date()).toString() + " Done " + i);
				}
			}

			long tsAll2 = System.currentTimeMillis();

			System.out.println("Total (" + (tsAll2 - tsAll1) + ":" + totalTime
				+ ") " + ", longest " + longest + ", shortest " + shortest
				+ ", avg " + (totalTime / max));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}