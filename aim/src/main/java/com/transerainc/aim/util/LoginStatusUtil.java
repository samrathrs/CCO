/**
 * 
 */
package com.transerainc.aim.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.transerainc.aim.pojo.AdhocDialProperty;
import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.pojo.AgentProfile;
import com.transerainc.aim.pojo.AgentTeamMapping;
import com.transerainc.aim.pojo.AuxilaryCode;
import com.transerainc.aim.pojo.LoginStatus;
import com.transerainc.aim.pojo.PasswordPolicy;
import com.transerainc.aim.pojo.PasswordPolicyResult;
import com.transerainc.aim.pojo.Site;
import com.transerainc.aim.pojo.SpeedDialList;
import com.transerainc.aim.pojo.SpeedDialListEntry;
import com.transerainc.aim.pojo.Team;
import com.transerainc.aim.pojo.Tenant;
import com.transerainc.aim.pojo.VirtualTeam;
import com.transerainc.aim.tpgintf.AdhocDialPropertiesType;
import com.transerainc.aim.tpgintf.AgentProfileType;
import com.transerainc.aim.tpgintf.AgentTeamMappingType;
import com.transerainc.aim.tpgintf.AgentType;
import com.transerainc.aim.tpgintf.IdleCodeType;
import com.transerainc.aim.tpgintf.LoginStatusType;
import com.transerainc.aim.tpgintf.PasswordPolicyType;
import com.transerainc.aim.tpgintf.SiteType;
import com.transerainc.aim.tpgintf.SpeedDialListType;
import com.transerainc.aim.tpgintf.TeamType;
import com.transerainc.aim.tpgintf.TenantType;
import com.transerainc.aim.tpgintf.VirtualTeamType;
import com.transerainc.aim.tpgintf.WrapupCodeType;
import com.transerainc.aim.tpgintf.SpeedDialListType.EntryType;
import com.transerainc.aim.tpgintf.impl.AdhocDialPropertiesTypeImpl;
import com.transerainc.aim.tpgintf.impl.AgentProfileTypeImpl;
import com.transerainc.aim.tpgintf.impl.AgentTeamMappingTypeImpl;
import com.transerainc.aim.tpgintf.impl.AgentTypeImpl;
import com.transerainc.aim.tpgintf.impl.IdleCodeTypeImpl;
import com.transerainc.aim.tpgintf.impl.LoginStatusImpl;
import com.transerainc.aim.tpgintf.impl.PasswordPolicyTypeImpl;
import com.transerainc.aim.tpgintf.impl.SiteTypeImpl;
import com.transerainc.aim.tpgintf.impl.SpeedDialListTypeImpl;
import com.transerainc.aim.tpgintf.impl.TeamTypeImpl;
import com.transerainc.aim.tpgintf.impl.TenantTypeImpl;
import com.transerainc.aim.tpgintf.impl.VirtualTeamImpl;
import com.transerainc.aim.tpgintf.impl.WrapupCodeTypeImpl;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class LoginStatusUtil {
	private static Logger lgr =
			Logger.getLogger(LoginStatusUtil.class.getName());

	@SuppressWarnings("unchecked")
	public static String getLoginStatusAsXmlString(LoginStatus status)
			throws JAXBException {
		LoginStatusType lsType = new LoginStatusImpl();

		lsType.setSuccessful(status.isLoggedIn());

		if (!status.isLoggedIn()) {
			lsType.setSuccessful(false);
			lsType.setCode(status.getCode());
			lsType.setReason(status.getErrorMessage());
			if(status.getMaxInvalidAttempts() > 0){
			   lsType.setMaxInvAtps(status.getMaxInvalidAttempts());
			   lsType.setCurInvAtps(status.getCurrentInvalidAttempts());
			}
		} else {
			Tenant tenant = status.getTenant();
			lsType.setTenant(getTenant(tenant));

			List<VirtualTeam> vtList = status.getVirtualTeamList();
			lsType.getVirtualTeam().addAll(getVirtualTeams(vtList));

			Site site = status.getSite();
			lsType.setSite(getSite(site));

			Agent agent = status.getAgent();
			lsType.setAgent(getAgent(agent, status.getPwdPolicyResult()));

			AgentProfile profile = status.getAgentProfile();
			lsType.setAgentProfile(getAgentProfile(profile));

			List<AuxilaryCode> idleCodeList = status.getIdleCodeList();
			List<IdleCodeType> icTypeList = getIdleCodeList(idleCodeList);
			if (icTypeList != null) {
				lsType.getIdleCode().addAll(icTypeList);
			}

			List<AuxilaryCode> wrapupCodeList = status.getWrapupList();
			List<WrapupCodeType> wcTypeList = getWrapupCodeList(wrapupCodeList);
			if (wcTypeList != null) {
				lsType.getWrapupCode().addAll(wcTypeList);
			}

			PasswordPolicy pp = status.getPasswordPolicy();
			List<PasswordPolicyType> ppTypeList = getPasswordPolicyList(pp);
			if (ppTypeList != null) {				
				lsType.getPasswordPolicy().addAll(ppTypeList);
			}

			List<Team> teamList = status.getTeamList();
			List<TeamType> tTypeList = getTeamTypeList(teamList);
			if (tTypeList != null) {
				lsType.getTeam().addAll(tTypeList);
			}

			List<AgentTeamMapping> agentTeamList =
					status.getAgentTeamMappingList();
			List<AgentTeamMappingType> atmTypeList =
					getAgentTeamMappingTypeList(agentTeamList);
			if (atmTypeList != null) {
				lsType.getAgentTeamMapping().addAll(atmTypeList);
			} else {
			}

			List<AdhocDialProperty> adpList = status.getAdhocDialPropertyList();
			List<AdhocDialPropertiesType> adpTypeList =
					getAdhocDialPropertiesList(adpList);
			if (adpTypeList != null) {
				lsType.getAdhocDialProperties().addAll(adpTypeList);
			}

			List<SpeedDialList> sdlList = status.getSpeedDialLists();
			List<SpeedDialListType> sdlTypeList = getSpeedDialLists(sdlList);
			if (sdlTypeList != null) {
				lsType.getSpeedDialList().addAll(sdlTypeList);
			}
		}

		String str = JAXBHelper.getLoginStatusAsString(lsType, false);

		if (lgr.isLoggable(Level.FINEST)) {
			lgr.finest("Returning Login Status: " + str);
		}

		return str;
	}

	/**
	 * @param vtList
	 * @return
	 */
	private static List<VirtualTeamType> getVirtualTeams(
			List<VirtualTeam> vtList) {
		List<VirtualTeamType> vtTypeList = new ArrayList<VirtualTeamType>();
		if (vtList != null) {
			for (VirtualTeam vteam : vtList) {
				VirtualTeamImpl vtImpl = new VirtualTeamImpl();
				vtImpl.setId(vteam.getId());
				vtImpl.setName(vteam.getName());
				vtImpl.setType(vteam.getType());
				vtImpl.setSeratelAcd(vteam.getSeratelAcd());

				vtTypeList.add(vtImpl);
			}
		}

		return vtTypeList;
	}

	/**
	 * @param tenant
	 * @return
	 */
	private static TenantType getTenant(Tenant tenant) {
		TenantType tType = null;
		if (tenant != null) {
			tType = new TenantTypeImpl();
			tType.setTenantId(tenant.getTenantId());
			tType.setTenantName(tenant.getCompanyName());
			tType.setAddressBookId(tenant.getAddressBookId());
			tType.setAutoWrapupInterval(tenant.getAutoWrapupInterval());
			tType.setCadHiddenFromAgent(tenant.getCadHiddenFromAgent());
			tType.setTenantXmlUrl(tenant.getTenantXmlUrl());
		}

		return tType;
	}

	/**
	 * @param tenant
	 * @return
	 */
	private static SiteType getSite(Site site) {
		SiteType sType = null;
		if (site != null) {
			sType = new SiteTypeImpl();
			sType.setSiteId(site.getSiteId());
			sType.setSiteName(site.getSiteName());
		}

		return sType;
	}

	/**
	 * @param sdlList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<SpeedDialListType> getSpeedDialLists(
			List<SpeedDialList> sdlList) {
		List<SpeedDialListType> sdlTypeList = null;
		if (sdlList != null) {
			sdlTypeList = new ArrayList<SpeedDialListType>();

			for (SpeedDialList sdl : sdlList) {
				SpeedDialListType sdlType = new SpeedDialListTypeImpl();

				sdlType.setListId(sdl.getListId());
				sdlType.setName(sdl.getName());
				sdlType.setTenantId(sdl.getTenantId());

				List<SpeedDialListEntry> entryList =
						sdl.getSpeedDialListEntryList();
				List<EntryType> entryTypeList = sdlType.getEntry();
				if (entryList != null) {
					for (SpeedDialListEntry sdlEntry : entryList) {
						EntryType eType =
								new SpeedDialListTypeImpl.EntryTypeImpl();
						eType.setEntryId(sdlEntry.getEntryId());
						eType.setName(sdlEntry.getName());
						eType.setNumber(sdlEntry.getNumber());

						entryTypeList.add(eType);
					}
				}

				sdlTypeList.add(sdlType);
			}
		}

		return sdlTypeList;
	}

	/**
	 * @param adpList
	 * @return
	 */
	private static List<AdhocDialPropertiesType> getAdhocDialPropertiesList(
			List<AdhocDialProperty> adpList) {
		List<AdhocDialPropertiesType> adpTypeList = null;
		if (adpList != null) {
			adpTypeList = new ArrayList<AdhocDialPropertiesType>();

			for (AdhocDialProperty adp : adpList) {
				AdhocDialPropertiesType adpType =
						new AdhocDialPropertiesTypeImpl();
				adpType.setName(adp.getName());
				adpType.setPrefix(adp.getPrefix());
				adpType.setPropertyId(adp.getId());
				adpType.setRegularExpression(adp.getRegularExpression());
				adpType.setStroppedChars(adp.getStrippedChars());
				adpType.setTenantId(adp.getTenantId());

				adpTypeList.add(adpType);
			}
		}

		return adpTypeList;
	}

	/**
	 * @param teamList
	 * @return
	 */
	private static List<TeamType> getTeamTypeList(List<Team> teamList) {
		List<TeamType> tTypeList = null;
		if (teamList != null) {
			tTypeList = new ArrayList<TeamType>();

			for (Team team : teamList) {
				if (team != null) {
					TeamType tType = new TeamTypeImpl();
					tType.setTeamId(team.getTeamId());
					tType.setTeamName(team.getTeamName());

					tTypeList.add(tType);
				}
			}
		}

		return tTypeList;
	}

	/**
	 * @param agentTeamMappingList
	 * @return
	 */
	private static List<AgentTeamMappingType> getAgentTeamMappingTypeList(
			List<AgentTeamMapping> agentTeamMappingList) {
		List<AgentTeamMappingType> atmTypeList = null;
		if (agentTeamMappingList != null) {
			atmTypeList = new ArrayList<AgentTeamMappingType>();

			for (AgentTeamMapping agentTeamMapping : agentTeamMappingList) {
				if (agentTeamMapping != null) {
					AgentTeamMappingType atmType =
							new AgentTeamMappingTypeImpl();
					atmType.setMapId(agentTeamMapping.getId());
					atmType.setTeamId(agentTeamMapping.getTeamId());

					atmTypeList.add(atmType);
				}
			}
		}

		return atmTypeList;
	}

	/**
	 * @param ppList
	 * @return
	 */
	private static List<PasswordPolicyType> getPasswordPolicyList( PasswordPolicy pp) {
		List<PasswordPolicyType> ppTypeList = null;
		if (pp != null) {
			ppTypeList = new ArrayList<PasswordPolicyType>();
			PasswordPolicyType ppExpType = new PasswordPolicyTypeImpl();
			ppExpType.setPolicyId(pp.getId());
			ppExpType.setTenantId(pp.getTenantId());
			ppExpType.setPolicyType("All");			
			ppExpType.setAttributeName("PWD_EXPIRY_PERIOD");
			ppExpType.setAttributeValue(Integer.toString(pp.getPwdExpirePeriod()));
			ppTypeList.add(ppExpType);
					
		}

		return ppTypeList;
	}

	/**
	 * @param wcList
	 * @return
	 */
	private static List<WrapupCodeType> getWrapupCodeList(
			List<AuxilaryCode> wcList) {
		List<WrapupCodeType> wcTypeList = null;
		if (wcList != null) {
			wcTypeList = new ArrayList<WrapupCodeType>();

			for (AuxilaryCode code : wcList) {
				WrapupCodeType wcType = new WrapupCodeTypeImpl();

				wcType.setCodeId(code.getCodeId());
				wcType.setCodeName(code.getCodeName());
				wcType.setDefault(code.isDefaultAuxCode());
				wcType.setTenantId(code.getTenantId());

				wcTypeList.add(wcType);
			}
		}

		return wcTypeList;
	}

	/**
	 * @param idleCodeList
	 * @return
	 */
	private static List<IdleCodeType> getIdleCodeList(
			List<AuxilaryCode> idleCodeList) {
		List<IdleCodeType> icTypeList = null;
		if (idleCodeList != null) {
			icTypeList = new ArrayList<IdleCodeType>();

			for (AuxilaryCode code : idleCodeList) {
				IdleCodeType icType = new IdleCodeTypeImpl();

				icType.setCodeId(code.getCodeId());
				icType.setOmniChannelAuxId(code.getOmniChannelAuxId());
				icType.setCodeName(code.getCodeName());
				icType.setDefault(code.isDefaultAuxCode());
				icType.setTenantId(code.getTenantId());
				icType.setSystemAux(code.isSystemAux());
				icTypeList.add(icType);
			}
		}

		return icTypeList;
	}

	/**
	 * @param profile
	 * @return
	 */
	private static AgentProfileType getAgentProfile(AgentProfile profile) {
		AgentProfileType apType = null;
		if (profile != null) {
			apType = new AgentProfileTypeImpl();

			apType.setProfileId(profile.getProfileId());
			apType.setProfileName(profile.getProfileName());
			apType.setProfileText(profile.getProfileText());
		}

		return apType;
	}

	/**
	 * @param agent
	 * @return
	 */
	private static AgentType getAgent(Agent agent, PasswordPolicyResult ppResult) {
		AgentType aType = null;
		if (agent != null) {
			aType = new AgentTypeImpl();
			aType.setAgentId(agent.getAgentId());
			aType.setAgentLogin(agent.getLogin());
			aType.setAgentName(agent.getAgentName());
			aType.setExternalId(agent.getExternalId());
			aType.setPasswordLastModified(agent
					.getPasswordLastModifiedTimestamp());
			aType.setIsDefault(agent.isMustChangePassword());
			aType.setDefaultDn(agent.getDefaultDn());
			if (ppResult != null) {
				aType.setPwdPolicyStatus(ppResult.getStatus());
				aType.setPwdRemainingDays(ppResult.getPwdRemainingDays());
			}
		}

		return aType;
	}	
}
