/**
 * 
 */
package com.transerainc.aim;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.springframework.util.StringUtils;
import org.xml.sax.InputSource;

import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.tpgintf.ActiveAgentListType;
import com.transerainc.aim.tpgintf.ActiveAgentType;
import com.transerainc.aim.util.JAXBHelper;
import com.transerainc.tam.util.HttpUtil;

/**
 * Date of Creation: Date: Jul 9, 2010<br>
 * 
 * @author <a href=mailto:suresh@transerainc.com>Suresh Kumar</a>
 * @version $Revision: 1.2 $
 */

public class SiteFinderTest {

	public static void main(String... strings) throws FileNotFoundException,
			JAXBException {

		for (int i = 1; i <= 72; i++) {
			String url = "http://lb01-dmz-net.lax.transerainc.net:8786/aim/lookupActiveAgentsIntf?tenantId="
					+ i
					+ "&subStatus=Available,Idle,WrapUp,Connected,AvailableConsulting,IdleConsulting,WrapUpConsulting,Consulting,consult,ConsultConnected,ConnectedConsultReserved,ConnectedConsulting,WrapUpConsultReserved,AvailableConsultReserved,IdleConsultReserved";

			try {
				String resp = HttpUtil.doHttpGet(url, "aim");

				ActiveAgentListType aaListType = JAXBHelper
						.getActiveAgentListType(new InputSource(
								new StringReader(resp)));

				List<ActiveAgent> aaList = new ArrayList<ActiveAgent>();
				Set<String> agentIdset = new HashSet<String>();
				List<ActiveAgentType> aaTypeList = aaListType.getActiveAgent();
				for (ActiveAgentType aaType : aaTypeList) {
					ActiveAgent agent = new ActiveAgent();
					agent.setAcgUrl(aaType.getAcgUrl());
					agent.setAgentId(aaType.getAgentId());
					// agent.setSubStatus(aaType.getSubStatus());
					agent.setTeamId(aaType.getTeamId());
					agent.setSiteId(aaType.getSiteId());
					agent.setTenantId(aaType.getTenantId());
					agent.setTimestamp(aaType.getTimestamp());
					agent.setAgentSessionId(aaType.getAgentSessionId());
					agent.setDn(aaType.getDn());

					if (!StringUtils.hasText(agent.getSiteId())) {
						agentIdset.add(agent.getAgentId());
					}
					aaList.add(agent);
				}
				if (!agentIdset.isEmpty()) {
					System.out.println("TenantId=" + i + " agent list "
							+ agentIdset.toString());
				}
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}
}
