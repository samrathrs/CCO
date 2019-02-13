package com.transerainc.aim.servlet;


import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.transerainc.aim.pojo.MediaProfile;
import com.transerainc.aim.pojo.ThirdPartyProfileResponse;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.AgentMediaProfileHandler;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;

public class AgentThirdPartyProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger lgr = Logger
			.getLogger(AgentThirdPartyProfileServlet.class.getName());

	public AgentThirdPartyProfileServlet() {
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
		
		String agentId = req.getParameter("agentId");
		String teamId = req.getParameter("teamId");
		String siteId = req.getParameter("siteId");
		String sessionId = req.getParameter("sId");
		lgr.fine("Agent third party profile request received for " + agentId
				+ " team id: " + teamId + " site id: " + siteId);
		
		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		
		AgentMediaProfileHandler mediaProfileHandler = pmgr
				.getAgentMediaProfileHandler();
		
		MediaProfile profile = mediaProfileHandler.getAgentMediaProfileByAgent(
				agentId, teamId, siteId);

		if (profile != null) {
			lgr.fine("Multimedia profile retrieved: " + profile.toString());

			String payload = null;
			try {
				payload = prepareResponse(profile, pmgr);
			} catch (Exception e) {
				lgr.severe("Failed to handle tpp request: " + e.toString());
				// FIXME:: error code, if needed
			}

			ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
					.findBean("activeAgentManager");
			aaMgr.addInprogressAgentSession(agentId, sessionId);
			
			lgr.fine("Outgoing payload: " + payload);
			resp.getWriter().println(payload);
		}
	}

	private String prepareResponse(MediaProfile profile, ProvisioningManager pmgr)
			throws IOException, ClassNotFoundException {
		ThirdPartyProfileResponse tprsp = new ThirdPartyProfileResponse();
		tprsp.setId(profile.getMediaProfileId());
		tprsp.setName(profile.getMediaProfileName());
		tprsp.setChatChannels(profile.getNumberOfChatChannels());
		tprsp.setEmailChannels(profile.getNumberOfEmailChannels());
		tprsp.setTeleChannels(profile.getNumberOfTelephonyChannels());
		tprsp.setBlendingMode(profile.getBlendingMode());
		Gson gson = new Gson();
		return gson.toJson(tprsp, ThirdPartyProfileResponse.class);
	}
}
