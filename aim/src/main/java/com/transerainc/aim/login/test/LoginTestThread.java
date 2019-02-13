/**
 * 
 */
package com.transerainc.aim.login.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class LoginTestThread extends Thread {
	@SuppressWarnings("unused")
	private static Logger lgr =
			Logger.getLogger(LoginTestThread.class.getName());
	private int interval;
	private int repetations;
	private String cname;
	private int threadId;
	private ResponseCapture responseCapture;
	private ThreadDoneListener tdListener;

	public LoginTestThread(ResponseCapture respCapture,
			ThreadDoneListener tdListener, int threadId, String cname,
			int interval, int repetations) {
		this.responseCapture = respCapture;
		this.tdListener = tdListener;
		this.threadId = threadId;
		this.cname = cname;
		this.interval = interval;
		this.repetations = repetations;

		setDaemon(true);
	}

	public void run() {
		int loopCount = 0;
		while (loopCount < repetations) {
			loopCount++;
			String url = null;
			String login = null;
			try {
				// Send the Login request to AIM
				login = TestLogins.getRandomLogin();
				String passwd = TestLogins.getProperty("login." + login);
				url =
						TestLogins.getProperty("aim.url") + "?login="
							+ URLEncoder.encode(login.trim(), "UTF-8")
							+ "&cname=" + cname + "&password="
							+ URLEncoder.encode(passwd, "UTF-8");

				long ts1 = System.currentTimeMillis();
				String resp = doConnect(url);
				long ts2 = System.currentTimeMillis();

				if (resp.length() > 128) {
					resp = resp.substring(0, 128);
				}

				responseCapture.capture(getResponseCaptureString(url, resp,
					(ts2 - ts1)));

				if (loopCount % 100 == 0) {
					System.out.println((new Date()).toString() + " Thread "
						+ threadId + " finished " + loopCount + " requests.");
				}

				Thread.sleep(interval);
			} catch (Exception e) {
				responseCapture.capture(getResponseCaptureErrorString(url, e
						.getMessage(), 0));
				System.out.println("Error for URL: " + url
					+ " for agent login " + login);
				e.printStackTrace();
			}
		}

		responseCapture.capture("Thread " + threadId + " done.");
		tdListener.threadDone(threadId);
	}

	public String getResponseCaptureErrorString(String url, String response,
			long timeTaken) {
		return threadId + ", " + timeTaken + ", " + url + " RETURNED[ERROR] "
			+ response;
	}

	public String getResponseCaptureString(String url, String response,
			long timeTaken) {
		return threadId + ", " + timeTaken + ", " + url + " RETURNED "
			+ response;
	}

	private String doConnect(String url) throws Exception {
		URL conURL = new URL(url);

		URLConnection con = conURL.openConnection();
		con.connect();

		StringBuffer buffer = new StringBuffer();
		InputStream is = con.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;

		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}

		return buffer.toString();
	}
}
