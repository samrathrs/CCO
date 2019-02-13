package com.transerainc.aim.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.pojo.ActiveSupervisor;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.runtime.ActiveSupervisorManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.tam.util.HttpStatus;
import com.transerainc.tam.util.HttpUtil;

public class SessionValidationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger lgr = Logger.getLogger(SessionValidationServlet.class
			.getName());
	private static String AGENT = "3";
	private static String SUPERVISOR = "58";

	public SessionValidationServlet() {

	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

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
		String userId = req.getParameter("uid");
		String sessionId = req.getParameter("sid");
		String tenantId = req.getParameter("tid");
		String roleId = req.getParameter("roleId");
		String result = "false";
		lgr.finer("Session validation request received for " + userId
				+ " session id: " + sessionId + " role: " + roleId
				+ " tenantId: " + tenantId);
		if (roleId.equals(SUPERVISOR)) {
			ActiveSupervisorManager asMgr = (ActiveSupervisorManager) AgentInformationManagerSystem
					.findBean("activeSupervisorManager");
			
			ActiveSupervisor supervisor = asMgr.getActiveSupervisorBySessionId(sessionId);
			if(supervisor != null && supervisor.getSupervisorId().equals(userId))
				result = "true";
			
			lgr.fine("Session validation request result: " + result
					+ " for supervisor id:" + userId + " and sessionid: "
					+ sessionId);

		} else if (roleId.equals(AGENT)) {
			ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
					.findBean("activeAgentManager");
			ActiveAgent aa = aaMgr.getActiveAgentByAgentId(tenantId, userId);
			if (aa != null && aa.getAgentSessionId().equals(sessionId)) {
				result = "true";
				lgr.fine(" Agent session validation successful for id:"
						+ userId + " and sessionid: " + sessionId);
				aaMgr.removeInprogressAgentSession(userId, sessionId);
			} else {
				String iprogressSId = aaMgr.getInprogressAgentSession(userId);
				if (iprogressSId != null && iprogressSId.equals(sessionId)) {
					result = "true";
					lgr.warning(" Agent session validation successful using in-progress info for id:"
							+ userId + " and sessionid: " + sessionId);
					aaMgr.removeInprogressAgentSession(userId, sessionId);
				} else {
					lgr.warning(" Agent session validation failed for login:"
							+ userId + " sessionid: " + sessionId);
				}
			}

		}
		resp.setContentType("text/plain; charset=UTF-8");
		resp.getWriter().println(result);
		resp.getWriter().flush();
	}

	private String relayHttpMsg(String url, HashMap<String, String> params,
			int maxRetryCount) {
		String result = "false";
		int retryCount = 0;
		boolean isPosted = false;
		while (!isPosted && retryCount++ < maxRetryCount) {
			try {
				HttpStatus resStatus = HttpUtil.doHttpPost(url, params);
				result = resStatus.getResponse();
				isPosted = true;
			} catch (IOException e) {
				lgr.severe("Failed to relay supervisor session validation request to "
						+ url + " Exception: " + e.toString());
			}
		}
		return result;
	}
}
