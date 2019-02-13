/**
 * 
 */
package com.transerainc.aim.servlet;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.StringUtils;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.pojo.ActiveSupervisor;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.runtime.ActiveSupervisorManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;

/**
 * @author rajpal.dangi@transerainc.com"
 * 
 */
public class ActiveSupervisorServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger lgr = Logger.getLogger(ActiveSupervisorServlet.class
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
		String supervisorId = req.getParameter("userId");
		String tenantId = req.getParameter("tenantId");
		String sessionId = req.getParameter("session_id");
		String command = req.getParameter("command");
		String source = req.getParameter("source");
		String callbkUrl = req.getParameter("callbackUrl");
		String tsStr = req.getParameter("timeStamp");

		boolean notify = true;
		if (source != null && source.equals("aim")) {
			notify = false;
		}

		if (!StringUtils.hasText(supervisorId)
				|| !StringUtils.hasText(tenantId)
				|| !StringUtils.hasText(sessionId)
				|| !StringUtils.hasText(command)
				|| !StringUtils.hasText(source)) {
			if (lgr.isLoggable(Level.SEVERE)) {
				lgr.severe("Unable to process request, mssing manadatory parameters "
						+ req.getParameterMap().toString());
				return;
			}
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a " + command + ":" + " for supervisor: "
					+ supervisorId + "  for tenant " + tenantId
					+ " session id: " + sessionId + " from: " + callbkUrl + " at: " + tsStr 
					+ "and source " + source);
		}
		ActiveSupervisorManager asMgr = (ActiveSupervisorManager) AgentInformationManagerSystem
				.findBean("activeSupervisorManager");
		
		ActiveSupervisor supervisor = asMgr.getActiveSupervisorBySessionId(sessionId);
		if (command.equals("SupervisorLoggedIn")) {
			
			if (supervisor == null) {
				supervisor = new ActiveSupervisor();
			}
			supervisor.setSupervisorId(supervisorId);
			supervisor.setSessionId(sessionId);
			supervisor.setTenantId(tenantId);
			supervisor.setCallbackUrl(callbkUrl);
			long ts;
			if (tsStr == null) {
				ts = System.currentTimeMillis();
			} else {
				ts = NumberUtils.toLong(tsStr);
			}
			supervisor.setTimestamp(ts);
			
			if (lgr.isLoggable(Level.FINE)) {
				lgr.fine("Going to invoke ActiveSupervisorManager.addSupervisor: "
						+ supervisor);
			}
			
			asMgr.addSupervisor(supervisor, notify);
			
		} else if (command.equals("SupervisorLoggedOut")) {
			asMgr.removeSupervisor(supervisor, notify);
		}
	}

	/**
	 * @param agentId
	 * @param channelId
	 * @param command
	 * @param tenantId
	 * @param oldACG
	 * @param aaMgr
	 */
	private void handleCompatibility(String tenantId, String agentId,
			String channelId, ActiveAgentManager aaMgr) {
		long t1 = System.currentTimeMillis();
		// if the request from backward compatibility ACG get all the
		// active agents
		Set<ActiveAgent> set = aaMgr
				.getActiveAgentsByAgentId(tenantId, agentId);
		if (lgr.isLoggable(Level.FINE)) {
			lgr.fine("Request from old ACG and found number of active agents for agent "
					+ agentId
					+ " are : "
					+ set
					+ " removing all the active agents other than with channelid "
					+ channelId);
		}
		// remove all the active agents other than current channelId
		for (ActiveAgent aagent : set) {
			if (!StringUtils.hasText(aagent.getChannelId())
					|| (StringUtils.hasText(aagent.getChannelId()) && !aagent
							.getChannelId().equals(channelId))) {
				aaMgr.removeAgent(tenantId, agentId, "", true);
			}
		}

		long t2 = System.currentTimeMillis();
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("cleanup activity for old acg took " + (t2 - t1) + " ms");
		}
	}
}
