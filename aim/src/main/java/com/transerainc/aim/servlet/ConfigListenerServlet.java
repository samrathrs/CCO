/*
 * Created on Dec 2, 2004
 *
 */
package com.transerainc.aim.servlet;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.transerainc.aim.system.AgentInformationManagerSystem;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba </a>
 * @version $Revision: 1.1 $
 */
public class ConfigListenerServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger lgr =
			Logger.getLogger(ConfigListenerServlet.class.getName());

	/**
	 * 
	 */
	public ConfigListenerServlet() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		handleRequest(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		handleRequest(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws IOException
	 * @throws ServletException
	 */
	private void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		try {
			lgr.warning("Got Config Change event from : "
				+ request.getRemoteHost());

			AgentInformationManagerSystem.reloadConfiguration();

			response.getWriter().println("Reload Configuration Successful.");
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while updating server configuration: ", e);
			throw new ServletException(e);
		}
	}
}
