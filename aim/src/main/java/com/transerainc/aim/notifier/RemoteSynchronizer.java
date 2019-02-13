/**
 * 
 */
package com.transerainc.aim.notifier;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.util.StringUtils;
import org.xml.sax.InputSource;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.conf.xsd.AuthenticationFailureListType;
import com.transerainc.aim.conf.xsd.AuthenticationFailureType;
import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.pojo.AuthenticationFailure;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.runtime.AgentLockoutManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.tpgintf.ActiveAgentListType;
import com.transerainc.aim.tpgintf.ActiveAgentType;
import com.transerainc.aim.util.JAXBHelper;
import com.transerainc.tam.util.HttpUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class RemoteSynchronizer {
	private static final Logger lgr =
			Logger.getLogger(RemoteSynchronizer.class.getName());

	public RemoteSynchronizer() {

	}

	public void synchronizeWithPeerAims() {
		ConfigurationManager configManager =
				(ConfigurationManager) AgentInformationManagerSystem
						.findBean("configManager");
		List<String> instanceList = configManager.getInstanceList();
		
		recoverActiveAgents(instanceList);
		recoverAuthenticationFailures(instanceList);
	}

	@SuppressWarnings("unchecked")
	private void recoverAuthenticationFailures(List<String> instanceList) {
		List<AuthenticationFailure> afList = new ArrayList<AuthenticationFailure>();
		String authContext = "/aim/authFailureIntf";
		for (String url : instanceList) {
			String peerUrl = url + authContext + "?command=list";
			try {
				String resp = HttpUtil.doHttpGet(peerUrl, url);
				if (lgr.isLoggable(Level.FINE)) {
					lgr.info("Peer " + peerUrl + " returned: " + resp);
				}
				AuthenticationFailureListType authListType =
						JAXBHelper.getAuthorizationFailureListType(new InputSource(
								new StringReader(resp)));
				List<AuthenticationFailureType> afTypeList = authListType.getAuthenticationFailure();
				for (AuthenticationFailureType afType : afTypeList) {
					AuthenticationFailure af = new AuthenticationFailure();
					af.setAgentId(afType.getAgentId());
					af.setFailureCount(afType.getNumberOfFailures());
					af.setIsLocked(afType.isIsLocked());

					afList.add(af);
				}
			} catch (Exception e) {
				lgr.log(Level.SEVERE, "Error while contacting peer AIM: "
					+ peerUrl, e);
			}
		}

		AgentLockoutManager alMgr =
				(AgentLockoutManager) AgentInformationManagerSystem
						.findBean("agentLockoutManager");

		alMgr.recoverAuthFailures(afList);
	}

	@SuppressWarnings("unchecked")
	private void recoverActiveAgents(List<String> instanceList) {
		String context = "/aim/lookupActiveAgentsIntf";

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Will contact peer AIMs to get the active agents - "
				+ instanceList);
		}

		List<ActiveAgent> aaList = new ArrayList<ActiveAgent>();
		for (String url : instanceList) {
			String peerUrl = url + context;
			try {
				String resp = HttpUtil.doHttpGet(peerUrl, url);

				if (lgr.isLoggable(Level.FINE)) {
					lgr.info("Peer " + peerUrl + " returned: " + resp);
				}

				ActiveAgentListType aaListType =
						JAXBHelper.getActiveAgentListType(new InputSource(
								new StringReader(resp)));

				List<ActiveAgentType> aaTypeList = aaListType.getActiveAgent();
				for (ActiveAgentType aaType : aaTypeList) {
					ActiveAgent agent = new ActiveAgent();
					agent.setAcgUrl(aaType.getAcgUrl());
					agent.setAgentId(aaType.getAgentId());
					agent.setSiteId(aaType.getSiteId());
					agent.setTeamId(aaType.getTeamId());
					agent.setTenantId(aaType.getTenantId());
					agent.setTimestamp(aaType.getTimestamp());
					agent.setAgentSessionId(aaType.getAgentSessionId());
					agent.setDn(aaType.getDn());
					// Suresh : For the backward compatibility with pre-mojito aim
					if (!StringUtils.hasText(aaType.getChannelId())) {
						agent.setChannelId(aaType.getAgentSessionId());
						agent.setChannelType("telephony");
						agent.setChannelStatus(aaType.getSubStatus());
						agent.setExternalIpAddress("");
						agent.setHostIpAddress("");
					} else {
						agent.setChannelId(aaType.getChannelId());
						agent.setChannelType(aaType.getChannelType());
						agent.setChannelStatus(aaType.getChannelStatus());
						agent.setExternalIpAddress(aaType.getExternalIpAddress());
						agent.setHostIpAddress(aaType.getHostIpAddress());
					}
					aaList.add(agent);
				}
			} catch (Exception e) {
				lgr.log(Level.SEVERE, "Error while contacting peer AIM: "
					+ peerUrl, e);
			}
		}

		ActiveAgentManager aaMgr =
				(ActiveAgentManager) AgentInformationManagerSystem
						.findBean("activeAgentManager");

		aaMgr.recoverAgents(aaList);
	}
}
