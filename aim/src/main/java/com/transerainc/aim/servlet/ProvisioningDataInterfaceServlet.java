/**
 * 
 */
package com.transerainc.aim.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.ws.jaxme.impl.JMMarshallerImpl;

import com.transerainc.aim.pojo.AdhocDialProperty;
import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.pojo.AgentProfile;
import com.transerainc.aim.pojo.AgentTeamMapping;
import com.transerainc.aim.pojo.AuxilaryCode;
import com.transerainc.aim.pojo.PasswordPolicy;
import com.transerainc.aim.pojo.Site;
import com.transerainc.aim.pojo.SpeedDialList;
import com.transerainc.aim.pojo.SpeedDialListEntry;
import com.transerainc.aim.pojo.Team;
import com.transerainc.aim.pojo.Tenant;
import com.transerainc.aim.pojo.VirtualTeam;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.AdhocDialPropetiesHandler;
import com.transerainc.aim.provisioning.handler.AgentHandler;
import com.transerainc.aim.provisioning.handler.AgentProfileHandler;
import com.transerainc.aim.provisioning.handler.AgentToTeamMappingHandler;
import com.transerainc.aim.provisioning.handler.IdleCodeHandler;
import com.transerainc.aim.provisioning.handler.PasswordPolicyHandler;
import com.transerainc.aim.provisioning.handler.SiteHandler;
import com.transerainc.aim.provisioning.handler.SpeedDialListHandler;
import com.transerainc.aim.provisioning.handler.TeamHandler;
import com.transerainc.aim.provisioning.handler.TenantHandler;
import com.transerainc.aim.provisioning.handler.VirtualTeamHandler;
import com.transerainc.aim.provisioning.handler.WrapupHandler;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.tpgintf.AdhocDialPropertiesType;
import com.transerainc.aim.tpgintf.AgentProfileType;
import com.transerainc.aim.tpgintf.AgentType;
import com.transerainc.aim.tpgintf.IdleCodeType;
import com.transerainc.aim.tpgintf.PasswordPolicyType;
import com.transerainc.aim.tpgintf.SiteType;
import com.transerainc.aim.tpgintf.SpeedDialListType;
import com.transerainc.aim.tpgintf.TeamType;
import com.transerainc.aim.tpgintf.TenantType;
import com.transerainc.aim.tpgintf.VirtualTeamType;
import com.transerainc.aim.tpgintf.WrapupCodeType;
import com.transerainc.aim.tpgintf.SpeedDialListType.EntryType;
import com.transerainc.aim.tpgintf.impl.AdhocDialPropertiesImpl;
import com.transerainc.aim.tpgintf.impl.AgentImpl;
import com.transerainc.aim.tpgintf.impl.AgentProfileImpl;
import com.transerainc.aim.tpgintf.impl.IdleCodeImpl;
import com.transerainc.aim.tpgintf.impl.PasswordPolicyImpl;
import com.transerainc.aim.tpgintf.impl.SiteImpl;
import com.transerainc.aim.tpgintf.impl.SpeedDialListImpl;
import com.transerainc.aim.tpgintf.impl.SpeedDialListTypeImpl;
import com.transerainc.aim.tpgintf.impl.TeamImpl;
import com.transerainc.aim.tpgintf.impl.TenantImpl;
import com.transerainc.aim.tpgintf.impl.VirtualTeamImpl;
import com.transerainc.aim.tpgintf.impl.WrapupCodeImpl;
import com.transerainc.aim.util.JAXBHelper;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class ProvisioningDataInterfaceServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger lgr =
			Logger.getLogger(ProvisioningDataInterfaceServlet.class.getName());

	public ProvisioningDataInterfaceServlet() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
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
	 */
	private void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		String entityType = req.getParameter("entityType");
		String entityId = req.getParameter("entityId");

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a request for entity " + entityType + " and id "
				+ entityId);
		}

		ProvisioningManager pmgr =
				(ProvisioningManager) AgentInformationManagerSystem
						.findBean("provManager");

		String entityAsString = null;
		try {
			if (entityType.equals("adhoc-dial-properties")) {
				AdhocDialPropetiesHandler adpHandler =
						pmgr.getAdhocDialPropetiesHandler();
				AdhocDialProperty entity =
						adpHandler.getAdhocDialProperty(entityId);
				entityAsString = getAdhocDialPropertiesAsString(entity);
			} else if (entityType.equals("agent")) {
				AgentHandler aHandler = pmgr.getAgentHandler();
				Agent agent = aHandler.getAgent(entityId);
				entityAsString = getAgentAsString(agent);
			} else if (entityType.equals("agent-profile")) {
				AgentProfileHandler apHandler = pmgr.getAgentProfileHandler();
				AgentProfile profile = apHandler.getAgentProfile(entityId);
				entityAsString = getAgentProfileAsString(profile);
			} else if (entityType.equals("agent-to-team-mapping")) {
				AgentToTeamMappingHandler aHandler =
						pmgr.getAgentTeamMappingHandler();
				TeamHandler tHandler = pmgr.getTeamHandler();
				List<AgentTeamMapping> lst =
						aHandler.getMappingsForAgent(entityId);
				List<Team> teamList = new ArrayList<Team>();
				if (lst != null) {
					for (AgentTeamMapping atMapping : lst) {
						String teamId = atMapping.getTeamId();
						Team team = tHandler.getTeam(teamId);
						if (!teamList.contains(team)) {
							teamList.add(team);
						}
					}
				}

				entityAsString = getAgentToTeamMappingsAsString(teamList);
			} else if (entityType.equals("idle-code")) {
				IdleCodeHandler handler = pmgr.getIdleCodeHandler();
				AuxilaryCode code = handler.getIdleCode(entityId);
				entityAsString = getIdleCodeAsString(code);
			} else if (entityType.equals("wrapup-code")) {
				WrapupHandler handler = pmgr.getWrapupHandler();
				AuxilaryCode code = handler.getWrapupCode(entityId);
				entityAsString = getWrapupCodeAsString(code);
			} else if (entityType.equals("password-policy")) {
				PasswordPolicyHandler handler = pmgr.getPasswordPolicyHandler();
				PasswordPolicy policy = handler.getPasswordPolicy(entityId);
				entityAsString = getPasswordPolicyAsString(policy);
			} else if (entityType.equals("speed-dial-list")) {
				SpeedDialListHandler handler = pmgr.getSpeedDialListHandler();
				SpeedDialList sdl = handler.getSpeedDialList(entityId);
				entityAsString = getSpeedDialListAsString(sdl);
			} else if (entityType.equals("speed-dial-list-entry")) {
				SpeedDialListHandler handler = pmgr.getSpeedDialListHandler();
				SpeedDialList sdl = handler.getSpeedDialListForEntry(entityId);
				entityAsString = getSpeedDialListAsString(sdl);
			} else if (entityType.equals("tenant")) {
				TenantHandler handler = pmgr.getTenantHandler();
				Tenant tenant = handler.getTenant(entityId);
				entityAsString = getTenantAsString(tenant);
			} else if (entityType.equals("team")) {
				TeamHandler handler = pmgr.getTeamHandler();
				Team team = handler.getTeam(entityId);
				entityAsString = getTeamAsString(team);
			} else if (entityType.equals("site")) {
				SiteHandler handler = pmgr.getSiteHandler();
				Site site = handler.getSite(entityId);
				entityAsString = getSiteAsString(site);
			} else if (entityType.equals("virtual-team")) {
				VirtualTeamHandler handler = pmgr.getVirtualTeamHandler();
				VirtualTeam vteam = handler.getVirtualTeam(entityId);
				entityAsString = getVirtualTeamAsString(vteam);
			}

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Returning Entity XML: " + entityAsString);
			}

			if (entityAsString != null) {
				//resp.setContentType("application/xml");
				resp.setContentType("text/xml; charset=UTF-8");
				resp.getWriter().println(entityAsString);
			} else {
				resp.setStatus(404);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getVirtualTeamAsString(VirtualTeam entity)
			throws JAXBException {
		String entityAsString = null;
		if (entity != null) {
			VirtualTeamType tType = new VirtualTeamImpl();
			tType.setId(entity.getId());
			tType.setName(entity.getName());
			tType.setType(entity.getType());
			tType.setTenantId(entity.getTenantId());
			tType.setSeratelAcd(entity.getSeratelAcd());

			entityAsString = getEntityAsString(tType);
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getSiteAsString(Site entity) throws JAXBException {
		String entityAsString = null;
		if (entity != null) {
			SiteType tType = new SiteImpl();
			tType.setSiteId(entity.getSiteId());
			tType.setSiteName(entity.getSiteName());
			tType.setTenantId(entity.getTenantId());

			entityAsString = getEntityAsString(tType);
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getTeamAsString(Team entity) throws JAXBException {
		String entityAsString = null;
		if (entity != null) {
			TeamType tType = new TeamImpl();
			tType.setTeamId(entity.getTeamId());
			tType.setTeamName(entity.getTeamName());
			tType.setTenantId(entity.getTenantId());

			entityAsString = getEntityAsString(tType);
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getTenantAsString(Tenant entity) throws JAXBException {
		String entityAsString = null;
		if (entity != null) {
			TenantType tType = new TenantImpl();
			tType.setAddressBookId(entity.getAddressBookId());
			tType.setAutoWrapupInterval(entity.getAutoWrapupInterval());
			tType.setCadHiddenFromAgent(entity.getCadHiddenFromAgent());
			tType.setTenantXmlUrl(entity.getTenantXmlUrl());

			entityAsString = getEntityAsString(tType);
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	private String getSpeedDialListAsString(SpeedDialList entity)
			throws JAXBException {
		String entityAsString = null;
		if (entity != null) {
			SpeedDialListType sdlType = new SpeedDialListImpl();
			sdlType.setListId(entity.getListId());
			sdlType.setName(entity.getName());
			sdlType.setTenantId(entity.getTenantId());

			List<SpeedDialListEntry> entryList =
					entity.getSpeedDialListEntryList();
			if (entryList != null) {
				for (SpeedDialListEntry sdlEntry : entryList) {
					EntryType entry = new SpeedDialListTypeImpl.EntryTypeImpl();
					entry.setEntryId(sdlEntry.getEntryId());
					entry.setName(sdlEntry.getName());
					entry.setNumber(sdlEntry.getNumber());

					sdlType.getEntry().add(entry);
				}
			}

			entityAsString = getEntityAsString(sdlType);
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getPasswordPolicyAsString(PasswordPolicy entity)
			throws JAXBException {
		String entityAsString = null;
		if (entity != null) {
//			PasswordPolicyType ppType = new PasswordPolicyImpl();
//			ppType.setAttributeName(entity.getAttributeName());
//			ppType.setAttributeValue(entity.getAttributeValue());
//			ppType.setPolicyId(entity.getId());
//			ppType.setPolicyType(entity.getPolicyType());
//			ppType.setTenantId(entity.getTenantId());
//			entityAsString = getEntityAsString(ppType);
			
			entityAsString = entity.toString();
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getWrapupCodeAsString(AuxilaryCode entity)
			throws JAXBException {
		String entityAsString = null;
		if (entity != null) {
			WrapupCodeType icType = new WrapupCodeImpl();
			icType.setCodeId(entity.getCodeId());
			icType.setCodeName(entity.getCodeName());
			icType.setDefault(entity.isDefaultAuxCode());
			icType.setTenantId(entity.getTenantId());

			entityAsString = getEntityAsString(icType);
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getIdleCodeAsString(AuxilaryCode entity)
			throws JAXBException {
		String entityAsString = null;
		if (entity != null) {
			IdleCodeType icType = new IdleCodeImpl();
			icType.setCodeId(entity.getCodeId());
			icType.setOmniChannelAuxId(entity.getOmniChannelAuxId());
			icType.setCodeName(entity.getCodeName());
			icType.setDefault(entity.isDefaultAuxCode());
			icType.setTenantId(entity.getTenantId());
			icType.setSystemAux(entity.isSystemAux());
			entityAsString = getEntityAsString(icType);
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getAgentToTeamMappingsAsString(List<Team> teamList)
			throws JAXBException {
		String entityAsString = null;
		if (teamList != null) {
			for (Team team : teamList) {
				TeamType tType = new TeamImpl();
				tType.setTeamId(team.getTeamId());
				tType.setTeamName(team.getTeamName());
				tType.setTenantId(team.getTenantId());

				if (entityAsString == null) {
					entityAsString = getEntityAsString(tType);
				} else {
					entityAsString += getEntityAsString(tType);
				}
			}
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getAgentProfileAsString(AgentProfile entity)
			throws JAXBException {
		String entityAsString = null;
		if (entity != null) {
			AgentProfileType aType = new AgentProfileImpl();
			aType.setProfileId(entity.getProfileId());
			aType.setProfileName(entity.getProfileName());
			aType.setProfileText(entity.getProfileText());

			entityAsString = getEntityAsString(aType);
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getAgentAsString(Agent entity) throws JAXBException {
		String entityAsString = null;
		if (entity != null) {
			AgentType aType = new AgentImpl();
			aType.setAgentId(entity.getAgentId());
			aType.setAgentName(entity.getAgentName());
			aType.setExternalId(entity.getExternalId());
			aType.setPasswordLastModified(entity
					.getPasswordLastModifiedTimestamp());
			aType.setDefaultDn(entity.getDefaultDn());
			aType.setTenantId(entity.getTenantId());

			entityAsString = getEntityAsString(aType);
		}

		return entityAsString;
	}

	/**
	 * @param entity
	 * @throws JAXBException
	 */
	private String getAdhocDialPropertiesAsString(AdhocDialProperty entity)
			throws JAXBException {
		String entityAsString = null;

		if (lgr.isLoggable(Level.FINEST)) {
			lgr.finest("Transforming property " + entity + " into xml.");
		}

		if (entity != null) {
			AdhocDialPropertiesType adpType = new AdhocDialPropertiesImpl();
			adpType.setName(entity.getName());
			adpType.setPrefix(entity.getPrefix());
			adpType.setPropertyId(entity.getId());
			adpType.setRegularExpression(entity.getRegularExpression());
			adpType.setStroppedChars(entity.getStrippedChars());
			adpType.setTenantId(entity.getTenantId());

			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Property Name: " + adpType.getName());
			}

			entityAsString = getEntityAsString(adpType);
		}

		if (lgr.isLoggable(Level.FINEST)) {
			lgr.finest("Returning xml string: " + entityAsString);
		}

		return entityAsString;
	}

	public String getEntityAsString(Object entity) throws JAXBException {
		JAXBContext context = JAXBHelper.getTPGContext();

		StringWriter sw = new StringWriter();
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
			Boolean.TRUE);
		marshaller.marshal(entity, sw);

		return sw.toString();
	}
}
