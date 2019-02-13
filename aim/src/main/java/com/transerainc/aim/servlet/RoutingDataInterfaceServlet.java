/**
 * 
 */
package com.transerainc.aim.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import com.transerainc.aha.gen.agent.RoutingData;
import com.transerainc.aha.gen.agent.SkillSet;
import com.transerainc.aha.gen.agent.impl.MediaProfileImpl;
import com.transerainc.aha.gen.agent.impl.RoutingDataImpl;
import com.transerainc.aha.gen.agent.impl.SkillImpl;
import com.transerainc.aha.gen.agent.impl.SkillSetImpl;
import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.pojo.MediaProfile;
import com.transerainc.aim.pojo.Skill;
import com.transerainc.aim.pojo.SkillProfile;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.AgentHandler;
import com.transerainc.aim.provisioning.handler.AgentMediaProfileHandler;
import com.transerainc.aim.provisioning.handler.SkillProfileHandler;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.JAXBHelper;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class RoutingDataInterfaceServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger lgr = Logger
			.getLogger(RoutingDataInterfaceServlet.class.getName());

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

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a routing data request for agent " + agentId
					+ " and team " + teamId + " under site " + siteId);
		}

		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");

		RoutingData routingData = new RoutingDataImpl();
		routingData.setAgentId(agentId);
		routingData.setTeamId(teamId);
		com.transerainc.aha.gen.agent.MediaProfile mprofile = getMediaProfileElement(
				pmgr, agentId, teamId, siteId);
		// ChannelSet channelSet = getMediaProfileElement(pmgr, agentId, teamId,
		// siteId);
		SkillSet skillSet = getSkillSetElement(pmgr, agentId, teamId);

		routingData.setMediaProfile(mprofile);
		if (skillSet != null) {
			routingData.setSkillSet(skillSet);
		}
		try {
			String xml = JAXBHelper.getRoutingDataAsString(routingData);
			resp.setContentType("text/xml; charset=UTF-8");
			resp.getWriter().println(xml);
		} catch (JAXBException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @param pmgr
	 * @param agentId
	 * @param teamId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private SkillSet getSkillSetElement(ProvisioningManager pmgr,
			String agentId, String teamId) {
		SkillSet skillSet = null;
		SkillProfileHandler skillProfileHandler = pmgr.getSkillProfileHandler();
		SkillProfile profile = skillProfileHandler.getSkillProfile(agentId,
				teamId);
		if (profile != null) {
			skillSet = new SkillSetImpl();
			skillSet.setProfileName(profile.getProfileName());
			skillSet.setSkillProfileId(profile.getProfileId());
			skillSet.setTenantId(profile.getTenantId());
			skillSet.setAgentId(agentId);
			skillSet.setTeamId(teamId);

			List<Skill> skillList = profile.getSkillList();
			for (Skill skill : skillList) {
				com.transerainc.aha.gen.agent.Skill genSkill = new SkillImpl();
				genSkill.setDescription(skill.getDescription());
				genSkill.setEnumSkillId(skill.getEnumSkillId());
				genSkill.setEnumSkillName(skill.getEnumSkillName());
				genSkill.setId(skill.getId());
				genSkill.setName(skill.getName());
				genSkill.setType(skill.getType());
				genSkill.setTypeName(skill.getTypeName());
				genSkill.setValue(skill.getValue());
				skillSet.getSkill().add(genSkill);
			}
		}

		return skillSet;
	}

	private com.transerainc.aha.gen.agent.MediaProfile getMediaProfileElement(
			ProvisioningManager pmgr, String agentId, String teamId,
			String siteId) {
		AgentHandler agentHandler = pmgr.getAgentHandler();
		AgentMediaProfileHandler mediaProfileHandler = pmgr
				.getAgentMediaProfileHandler();

		MediaProfile profile = mediaProfileHandler.getAgentMediaProfileByAgent(
				agentId, teamId, siteId);

		Agent agent = agentHandler.getAgent(agentId);
		// it is hack and force to set tenantId
		String tenantId = agent != null ? agent.getTenantId() : "-1";

		com.transerainc.aha.gen.agent.MediaProfile mprofile = new MediaProfileImpl();
		mprofile.setTenantId(tenantId);
		mprofile.setAgentId(agentId);
		// mprofile.setTeamId(teamId);
		if (profile != null) {
			mprofile.setId(profile.getMediaProfileId());
			mprofile.setName(profile.getMediaProfileName());
			mprofile.setTenantId(profile.getTenantId());
			mprofile.setTelephony(profile.getNumberOfTelephonyChannels());
			mprofile.setEmail(profile.getNumberOfEmailChannels());
			mprofile.setFax(profile.getNumberOfFaxChannels());
			mprofile.setChat(profile.getNumberOfChatChannels());
			mprofile.setVideo(profile.getNumberOfVideoChannels());
			mprofile.setOther(profile.getNumberOfOtherChannels());
		} else {
			mprofile.setId("");
			mprofile.setName("");
		}
		return mprofile;
	}

	// /**
	// * @param pmgr
	// * @return
	// */
	// private ChannelSet getMediaProfileElement(ProvisioningManager pmgr,
	// String agentId, String teamId, String siteId) {
	// AgentMediaProfileHandler mediaProfileHandler = pmgr
	// .getAgentMediaProfileHandler();
	// MediaProfile profile = mediaProfileHandler.getAgentMediaProfileByAgent(
	// agentId, teamId, siteId);
	// ChannelSet channelSet = null;
	// if (profile != null) {
	// channelSet = new ChannelSetImpl();
	// channelSet.setProfileId(profile.getMediaProfileId());
	// channelSet.setProfileName(profile.getMediaProfileName());
	// channelSet.setTenantId(profile.getTenantId());
	//
	// int count = profile.getNumberOfChatChannels();
	// createChannel(channelSet, agentId, teamId, count, "chat");
	//
	// count = profile.getNumberOfEmailChannels();
	// createChannel(channelSet, agentId, teamId, count, "email");
	//
	// count = profile.getNumberOfFaxChannels();
	// createChannel(channelSet, agentId, teamId, count, "fax");
	//
	// count = profile.getNumberOfOtherChannels();
	// createChannel(channelSet, agentId, teamId, count, "other");
	//
	// count = profile.getNumberOfTelephonyChannels();
	// createChannel(channelSet, agentId, teamId, count, "telephony");
	//
	// count = profile.getNumberOfVideoChannels();
	// createChannel(channelSet, agentId, teamId, count, "video");
	// }
	//
	// return channelSet;
	// }
	//
	// /**
	// * @param channelSet
	// * @param count
	// * @param channelName
	// */
	// @SuppressWarnings("unchecked")
	// private void createChannel(ChannelSet channelSet, String agentId,
	// String teamId, int count, String channelName) {
	// for (int i = 0; i < count; i++) {
	// Channel channel = new ChannelImpl();
	// channel.setId(UUID.randomUUID().toString());
	// channel.setType(channelName);
	// channel.setAgentId(agentId);
	// channel.setTeamId(teamId);
	// channelSet.getChannel().add(channel);
	// }
	// }
}
