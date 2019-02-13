/**
 * 
 */
package com.transerainc.aim.system.test;

import junit.framework.TestCase;

import com.transerainc.aim.system.AgentInformationManagerSystem;

public class AgentInformationManagerTest extends TestCase {

	public void testAgentInformationManager() throws Exception {
		AgentInformationManagerSystem aam = new AgentInformationManagerSystem();
		aam.start();

		AgentInformationManagerSystem.findBean("provManager");

		byte[] b = new byte[10];
		System.in.read(b);

		System.out.println("Read Bytes: " + (new String(b)));
	}
}
