/**
 * 
 */
package com.transerainc.aim.system;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class BeanInitializer {
	private static Logger lgr =
			Logger.getLogger(BeanInitializer.class.getName());
	private List<String> beanList;

	public BeanInitializer(String beanIds) {
		String[] beanIdArr = beanIds.split(",");
		beanList = Arrays.asList(beanIdArr);
	}

	public void initialize() {
		for (String beanId : beanList) {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Initializing bean " + beanId);
			}

			AgentInformationManagerSystem.findBean(beanId);
		}
	}
}
