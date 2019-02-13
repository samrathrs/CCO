/**
 * 
 */
package com.transerainc.aim.login.test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class TestLogins {
	@SuppressWarnings("unused")
	private static Logger lgr = Logger.getLogger(TestLogins.class.getName());
	private static Properties props;
	private static List<String> loginIdList;
	private static Random rand;
	private static int maxRandNum;
	private static ThreadDoneListener tdListener;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String filename = args[0];

		props = new Properties();
		props.loadFromXML(new FileInputStream(filename));

		int numThreads = Integer.parseInt(props.getProperty("num.threads"));
		int timeBetweenThreads =
				Integer.parseInt(props.getProperty("time.between.threads"));
		int timeBetweenLogins =
				Integer.parseInt(props.getProperty("time.between.logins"));
		int numRepetations =
				Integer.parseInt(props.getProperty("number.of.repetations"));

		loginIdList = getLoginIdList(props);
		rand = new Random();
		maxRandNum = loginIdList.size();

		tdListener = new ThreadDoneListener(numThreads);
		tdListener.start();

		ResponseCapture rc = new ResponseCapture();
		rc.start();

		for (int i = 0; i < numThreads; i++) {
			LoginTestThread thread =
					new LoginTestThread(rc, tdListener, i, props
							.getProperty("tenant"), timeBetweenLogins,
							numRepetations);

			thread.start();

			Thread.sleep(timeBetweenThreads);
		}
	}

	public static String getProperty(String key) {
		String value = props.getProperty(key);
		if (value != null) {
			value = value.trim();
		}

		return value;
	}

	public static String getRandomLogin() {
		int i = rand.nextInt(maxRandNum);
		return loginIdList.get(i);
	}

	/**
	 * @param props
	 * @return
	 */
	private static List<String> getLoginIdList(Properties props) {
		List<String> loginList = new ArrayList<String>();

		for (Object obj : props.keySet()) {
			String key = (String) obj;
			if (key.startsWith("login.")) {
				String[] tokens = key.split("\\.");

				loginList.add(tokens[1]);
			}

		}

		return loginList;
	}

}
