/**
 * 
 */
package com.transerainc.aim.login.test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ThreadDoneListener extends Thread {
	private static Logger lgr =
			Logger.getLogger(ThreadDoneListener.class.getName());
	private LinkedBlockingQueue<Integer> respQueue;
	private int numThreads;
	private int threadsDone = 0;

	public ThreadDoneListener(int numThreads) {
		respQueue = new LinkedBlockingQueue<Integer>();
		this.numThreads = numThreads;
	}

	public void threadDone(int i) {
		respQueue.add(i);
	}

	public void run() {
		while (true) {
			try {
				int i = respQueue.take();

				lgr.info("Thread " + i + " done out of " + numThreads);

				threadsDone++;

				if (threadsDone == numThreads) {
					System.exit(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
