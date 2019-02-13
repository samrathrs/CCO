/**
 * 
 */
package com.transerainc.aim.login.test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ResponseCapture extends Thread {
	@SuppressWarnings("unused")
	private static Logger lgr =
			Logger.getLogger(ResponseCapture.class.getName());
	private PrintWriter writer;
	private LinkedBlockingQueue<String> respQueue;

	public ResponseCapture() throws FileNotFoundException {
		respQueue = new LinkedBlockingQueue<String>();

		writer =
				new PrintWriter(TestLogins
						.getProperty("response.capture.filename"));
	}

	public void capture(String response) {
		respQueue.add(response);
	}

	public void run() {
		while (true) {
			try {
				String response = respQueue.take();

				writer.println(response);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
