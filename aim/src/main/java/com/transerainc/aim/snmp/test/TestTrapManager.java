/**
 * 
 */
package com.transerainc.aim.snmp.test;

import junit.framework.TestCase;

import com.transerainc.aim.snmp.TrapManager;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class TestTrapManager extends TestCase {

	public void testTrapManager() throws Exception {
		TrapManager tmgr =
				new TrapManager("test", "miami.transerainc.com", 705, 3,
						"test", true);
		tmgr.start();

		// Thread.sleep(3000);
		tmgr.sendAgentDoesNotExist("ramesh", "fuck you", "100");

		Thread.sleep(10000);
	}
}
