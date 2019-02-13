/*
 * Created on Sep 28, 2004
 *
 */
package com.transerainc.aim.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.tam.tpm.TPMFacade;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba </a>
 * @version $Revision: 1.1 $
 */
public class ShutdownServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger lgr =
			Logger.getLogger("com.transerinc.cha.servlet.tpm");

	/**
	 * 
	 */
	public ShutdownServlet() {
	}

	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		handleRequest(arg0, arg1);
	}

	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		handleRequest(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws IOException
	 */
	private void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		lgr.severe("Got a shutdown from " + request.getRemoteHost());

		TPMFacade tpmFacade = AgentInformationManagerSystem.getTPMFacade();

		String xml = tpmFacade.respondToShutdown("0");

		response.getWriter().write(xml);

		response.getWriter().flush();

		System.exit(0);
	}
}
