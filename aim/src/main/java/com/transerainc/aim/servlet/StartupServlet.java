/*
 * Created on Apr 28, 2005
 *
 */
package com.transerainc.aim.servlet;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.transerainc.aim.system.AgentInformationManagerSystem;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Revision: 1.2 $
 */
public class StartupServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger lgr =
			Logger.getLogger(StartupServlet.class.getName());

	private static int initCount = 0;

	private AgentInformationManagerSystem aimSystem;

	/**
	 * 
	 */
	public StartupServlet() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();

		initCount++;

		lgr.warning("Initializing AIM... Count: " + initCount);

		aimSystem = new AgentInformationManagerSystem();

		try {
			aimSystem.start();
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while startup.", e);
			System.exit(-1);
		}

	}

}
