/**
 * 
 */
package com.transerainc.aim.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import com.transerainc.aim.conf.xsd.AuthenticationFailureListType;
import com.transerainc.aim.conf.xsd.AuthenticationFailureType;
import com.transerainc.aim.conf.xsd.impl.AuthenticationFailureImpl;
import com.transerainc.aim.conf.xsd.impl.AuthenticationFailureListImpl;
import com.transerainc.aim.pojo.AuthenticationFailure;
import com.transerainc.aim.runtime.AgentLockoutManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.JAXBHelper;

public class AuthenticationFailureInterfaceServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger lgr =
			Logger.getLogger(AuthenticationFailureInterfaceServlet.class
					.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	/**
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String agentId = req.getParameter("agentId");
		String command = req.getParameter("command");
		String failureCount = req.getParameter("failureCount");
		String isLocked = req.getParameter("isLocked");

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a " + command + " for agent " + agentId
				+ " with failureCount " + failureCount + " and isLocked "
				+ isLocked);
		}

		AgentLockoutManager alMgr =
				(AgentLockoutManager) AgentInformationManagerSystem
						.findBean("agentLockoutManager");

		if ("updateAgentLockout".equals(command)) {
			AuthenticationFailure af =
					alMgr.getAuthorizationFailureForAgent(agentId);
			if (af == null) {
				af = new AuthenticationFailure(agentId);
				alMgr.addAuthenitcationFailureForAgent(af);
			}
			af.setFailureCount(Integer.valueOf(failureCount));
			af.setIsLocked(Boolean.valueOf(isLocked));
		} else if ("releaseAgentLockout".equals(command)) {
			alMgr.clearAgentLockout(agentId, false);
		} else if ("list".equals(command)) {
			try {
				handleAuthFailureList(alMgr, resp);
			} catch (JAXBException e) {
				lgr.log(Level.WARNING, "Exception handling Auth Failure List",
					e);
				throw new ServletException(e);
			}
		}
	}

	private void handleAuthFailureList(AgentLockoutManager alMgr,
			HttpServletResponse resp) throws IOException, JAXBException {
		List<AuthenticationFailure> afList =
				alMgr.getAuthenticationFailureList();
		AuthenticationFailureListType xmlAfList =
				new AuthenticationFailureListImpl();
		for (AuthenticationFailure af : afList) {
			AuthenticationFailureType aft = new AuthenticationFailureImpl();
			aft.setAgentId(af.getAgentId());
			aft.setNumberOfFailures(af.getFailureCount());
			aft.setIsLocked(af.getIsLocked());
		}
		String xmlString =
				JAXBHelper
						.getAuthorizationFailureListAsString(xmlAfList, false);
		resp.setContentType("text/xml; charset=UTF-8");
		//resp.setContentType("text/xml");
		PrintWriter out = resp.getWriter();
		out.print(xmlString);
		out.flush();
	}
}
