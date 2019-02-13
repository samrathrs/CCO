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
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ActiveAgentServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger lgr = Logger.getLogger(ActiveAgentServlet.class
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
		String channelId = req.getParameter("channelId");
		String channelType = req.getParameter("channelType");
		String command = req.getParameter("command");
		String tenantId = req.getParameter("tenantId");
		String teamId = req.getParameter("teamId");
		String acgUrl = req.getParameter("acgUrl");
		String tsStr = req.getParameter("timestamp");
		String state = req.getParameter("subStatus");
		String source = req.getParameter("source");
		String agentSessionId = req.getParameter("agentSessionId");
		String dn = req.getParameter("dn");
		String siteId = req.getParameter("siteId");
		String externalIpAddress = req.getParameter("externalIpAddress");
		String hostIpAddress = req.getParameter("hostIpAddress");

		boolean oldACG = false;

		boolean notify = true;
		if (source != null && source.equals("aim")) {
			notify = false;
		}

		if (!StringUtils.hasText(channelId)) {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Got request without channelId, for backward compatibility setting channelId and channelType to defaults for agent sessionId : "
						+ agentSessionId);
			}
			oldACG = true;
			channelId = command.equals("LoggedOut") ? "" : agentSessionId;
			channelType = "telephony";
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a " + command + ":" + state + " for agent " + agentId
					+ " for channel id " + channelId + " channel type "
					+ channelType + " for tenant " + tenantId + " in team "
					+ teamId + ", ACG URL: " + acgUrl + ", Timestamp " + tsStr
					+ ", SID: " + agentSessionId + ", DN: " + dn
					+ ", in site: " + siteId + " and source " + source
					+ ", externalIpAddress " + externalIpAddress
					+ ", hostIpAddress " + hostIpAddress);
		}

		ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
				.findBean("activeAgentManager");

		if (command.equals("LoggedIn")) {
			
			if (oldACG) {
				handleCompatibility(tenantId, agentId, channelId, aaMgr);
			}
			
			ActiveAgent agent = aaMgr.getActiveAgentByChannelId(tenantId,
					channelId);

			if (agent == null) {
				agent = new ActiveAgent();
			}

			agent.setChannelId(channelId);
			agent.setChannelType(channelType);
			agent.setChannelStatus(state);
			agent.setAcgUrl(acgUrl);
			agent.setAgentId(agentId);
			agent.setTeamId(teamId);
			agent.setTenantId(tenantId);
			agent.setAgentSessionId(agentSessionId);
			agent.setDn(dn);
			agent.setSiteId(siteId);
			agent.setExternalIpAddress(externalIpAddress);
			agent.setHostIpAddress(hostIpAddress);

			long ts;
			if (tsStr == null) {
				ts = System.currentTimeMillis();
			} else {
				ts = NumberUtils.toLong(tsStr);
			}

			agent.setTimestamp(ts);

			if (lgr.isLoggable(Level.FINE)) {
				lgr.fine("Going to invoke ActiveAgentManager.addAgent: "
						+ agent);
			}

			ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
					.findBean("configManager");
			if (cmgr.isSubStatusAllowed(tenantId, state)) {
				aaMgr.addAgent(tenantId, agent, notify);
			} else {
				// Return a tenant shutdown message and ask TPG to log the agent
				// out.
				resp.getWriter().println(
						"<error>Tenant In Shutdown Mode</error>");
				aaMgr.removeAgent(tenantId, agentId, channelId, notify);
			}
		} else if (command.equals("LoggedOut")) {
			aaMgr.removeAgent(tenantId, agentId, channelId, notify);
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
