/*
 * Created on Mar 7, 2006
 *
 */
package com.transerainc.aim.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.JAXBHelper;

public class ServerConfigDisplayServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerConfigDisplayServlet() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
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
	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		handleRequest(arg0, arg1);
	}

	private void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/xml; charset=UTF-8");
		PrintWriter writer = response.getWriter();

		try {
			ConfigurationManager cmgr =
					(ConfigurationManager) AgentInformationManagerSystem
							.findBean("configManager");
			String xmlString =
					JAXBHelper.getAgentInformationManagerAsString(cmgr
							.getAgentInformationManager(), true);

			writer.write(xmlString);
		} catch (JAXBException e) {
			throw new ServletException(e);
		}
	}
}
