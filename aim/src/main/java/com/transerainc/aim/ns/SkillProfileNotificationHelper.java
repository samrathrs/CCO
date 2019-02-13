/**
 * 
 */
package com.transerainc.aim.ns;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aha.gen.agent.SkillSet;
import com.transerainc.aha.gen.agent.impl.SkillImpl;
import com.transerainc.aha.gen.agent.impl.SkillSetImpl;
import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.notifier.ACGNotificationManager;
import com.transerainc.aim.pojo.ACGNotification;
import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.pojo.NSNotification;
import com.transerainc.aim.pojo.Skill;
import com.transerainc.aim.pojo.EntityAssignment;
import com.transerainc.aim.pojo.SkillProfile;
import com.transerainc.aim.provisioning.ProvisioningConnection;
import com.transerainc.aim.provisioning.ProvisioningDAO;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.SkillProfileHandler;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.JAXBHelper;

/**
 * Date of Creation: Date: Jun 10, 2010<br>
 * 
 * @author <a href=mailto:suresh@transerainc.com>Suresh Kumar</a>
 * @version $Revision: 1.5 $
 */

public class SkillProfileNotificationHelper {

	private static Logger lgr = Logger
			.getLogger(SkillProfileNotificationHelper.class.getName());

	private static final String ENTITY_AGENT = "0";
	private static final String ENTITY_TEAM = "1";

	private SkillProfile profile;

	public SkillProfileNotificationHelper(SkillProfile profile) {
		this.profile = profile;
	}

	public SkillProfileNotificationHelper() {

	}

	public void handleSkillProfileAssignmentChanges(EntityAssignment assignment) {

		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
				.findBean("activeAgentManager");
		
		// SqlStatementManager stmtMgr = (SqlStatementManager)
		// AgentInformationManagerSystem
		// .findBean("sqlStatementMgr");
		// ConfigurationManager cmgr = (ConfigurationManager)
		// AgentInformationManagerSystem
		// .findBean("configManager");
		// ProvisioningConnection provCon = new ProvisioningConnection(cmgr);
		//
		// ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
		//
		// boolean result = pmgr.updateAgentTeamSkillProfileIdMap(provDAO);
		//
		// if (!result) {
		// lgr.warning("Skill assignments are not updated, so not notifying ACG for entity type "
		// + assignment.getEntityType()
		// + " and entity "
		// + assignment.getEntityId());
		// return;
		// }

		SkillProfileHandler handler = pmgr.getSkillProfileHandler();

		if (ENTITY_AGENT.equals(assignment.getEntityType())) {
			// get active agent
			ActiveAgent aagent = aaMgr.getActiveAgentByAgentId(assignment
					.getEntityId());
			if (aagent == null) {
				lgr.fine("Couldn't find active agent for agent "
						+ assignment.getEntityId());
			} else {
				sendNotificationForActiveAgent(aagent, handler);
			}
		} else if (ENTITY_TEAM.equals(assignment.getEntityType())) {
			// get all active agents by team
			Set<ActiveAgent> activeAgentSet = aaMgr.getActiveAgentsByTeam(
					assignment.getTenantId(), assignment.getEntityId());
			if (!activeAgentSet.isEmpty()) {
				for (ActiveAgent aagent : activeAgentSet) {
					sendNotificationForActiveAgent(aagent, handler);
				}
			} else {
				lgr.fine("Couldn't find active agnets for team "
						+ assignment.getEntityId());
			}
		}
	}

	public void handleSkillProfileModificationChanges() {
		ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
				.findBean("activeAgentManager");
		Set<String> agentIdSet = getAgentsOfProfile();
		Set<ActiveAgent> agents = new HashSet<ActiveAgent>();
		if (lgr.isLoggable(Level.FINE)) {
			lgr.fine("The currently associated agents for the skill profile "
					+ profile.getProfileId() + " are : "
					+ agentIdSet.toString());
		}
		for (String agentId : agentIdSet) {
			ActiveAgent agent = aaMgr.getActiveAgentByAgentId(
					profile.getTenantId(), agentId);
			if (agent != null) {
				agents.add(agent);
			}
		}

		for (ActiveAgent activeAgent : agents) {
			sendSkillSetForAgent(activeAgent.getAgentId(),
					activeAgent.getTeamId(), activeAgent.getTenantId());
		}
	}

	private Set<String> getAgentsOfProfile() {
		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		SkillProfileHandler handler = pmgr.getSkillProfileHandler();
		return handler.getAgentsOfSkillProfile(profile.getProfileId());
	}

	private void sendNotificationForActiveAgent(ActiveAgent aagent,
			SkillProfileHandler handler) {
		this.profile = handler.getSkillProfile(aagent.getAgentId(),
				aagent.getTeamId());
		if (this.profile == null) {
			lgr.fine("There is (no skill profile found)/(profile is removed) for active agent "
					+ aagent.getAgentId() + " and team " + aagent.getTeamId());
		}
		sendSkillSetForAgent(aagent.getAgentId(), aagent.getTeamId(),
				aagent.getTenantId());
	}

	private void sendSkillSetForAgent(String agentId, String teamId,
			String tenantId) {
		SkillSet skillSet = new SkillSetImpl();
		skillSet.setTenantId(tenantId);
		skillSet.setAgentId(agentId);
		skillSet.setTeamId(teamId);

		if (profile != null) {
			skillSet.setSkillProfileId(profile.getProfileId());
			skillSet.setProfileName(profile.getProfileName());
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
		// else {
		// skillSet.setSkillProfileId("");
		// skillSet.setProfileName("");
		// }
		notifyAcgs(skillSet);
	}

	private void notifyAcgs(SkillSet skillSet) {

		// Notify TPGs with the change
		ACGNotificationManager notifMgr = (ACGNotificationManager) AgentInformationManagerSystem
				.findBean("acgNotificationManager");
		try {
			ActiveAgentManager activeAgentMgr = (ActiveAgentManager) AgentInformationManagerSystem
					.findBean("activeAgentManager");

			ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
					.findBean("configManager");

			List<String> urlList = activeAgentMgr.getAllAcgUrls();
			ACGNotification notif = new NSNotification(cmgr
					.getAgentInformationManager()
					.getNotificationAcgUpdateSkillProfileContext());
			notif.setAcgUrls(urlList);
			String txtMsg = JAXBHelper.getSkillSetAsString(skillSet);

			notif.setMessage(txtMsg);
			notif.setMethod(ACGNotification.METHOD_POST);

			notifMgr.sendNotification(notif);

			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Skill profile notificaiton message " + txtMsg);
			}

		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while sending skill profile notification to ACG", e);
		}
	}

}
