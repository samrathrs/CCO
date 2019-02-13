<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page import="com.transerainc.aim.pojo.Agent"%>
<%@page import="com.transerainc.aim.runtime.ActiveAgentManager"%>
<%@page import="com.transerainc.aim.pojo.ActiveAgent"%>
<%@page import="com.transerainc.aim.provisioning.handler.TeamHandler"%>
<%@page import="com.transerainc.aim.pojo.Team"%>
<%@page import="com.transerainc.aim.provisioning.handler.SkillProfileHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentMediaProfileHandler"%>
<%@page import="com.transerainc.aim.pojo.ThirdPartyProfile"%>
<%@page import="com.transerainc.aim.provisioning.handler.ThirdPartyProfileHandler"%>


<%@page import="com.transerainc.aim.pojo.Tenant"%><html>
<head>
<link rel="stylesheet" type="text/css" href="aim.css"  >
<link rel="stylesheet" type="text/css" href="chromestyle2.css"  />
<link rel="stylesheet" type="text/css" href="style.css" >
<link rel="stylesheet" type="text/css" href="subModal.css" />

<script type="text/javascript" src="chrome.js"></script>
<script type="text/javascript" src="common-google.js"></script>
<script type="text/javascript" src="subModal.js" defer="defer"></script>

<title>AIM - (<%=request.getServerName()%>:<%=request.getServerPort()%>)</title>
</head>
<%@include file="header.jsp"%>


<%
	ProvisioningManager provMgr = (ProvisioningManager) AgentInformationManagerSystem
			.findBean("provManager");

	TenantHandler tHandler = provMgr.getTenantHandler();
	SkillProfileHandler skillProfileHandler = provMgr
			.getSkillProfileHandler();
	AgentMediaProfileHandler mediaProfileHandler = provMgr
			.getAgentMediaProfileHandler();
	String tenantId = request.getParameter("tenantId");
	out.println("");

	if (tenantId == null) {
		Collection<String> tenantIdList = tHandler.getTenantIdList();
		tenantId = tenantIdList.iterator().next();
	}

	Collection<String> tenantIdList = tHandler.getTenantIdList();
	String tenantName = tHandler.getTenant(tenantId).getCompanyName();

	AgentHandler aHandler = provMgr.getAgentHandler();
	TeamHandler teamHandler = provMgr.getTeamHandler();

	List<Agent> agentList = aHandler.getAgentsForTenant(tenantId);

	ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
			.findBean("activeAgentManager");
	Set<Tenant> tenantList = tHandler.getTenants();
%>


<body onload="stripe();">
<form name="actionForm" action="reloadEntities.jsp?name=agents"
	method="POST"><input type="hidden" name="tenantId"
	value="<%=tenantId%>"> <BR clear=all>

<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a
		href="viewAgents.jsp?tenantId=<%=tenantId%>" rel="dropmenu1"><%=tenantId%>.
	<%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewAgents.jsp?tenantId=<%=tenant.getTenantId()%>"> <%=String.format("%1$4s.", tenant.getTenantId())
								.replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>

<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>

<table id="b" class="mytable">
	<caption><span id="desc">Agents For tenant <%=tenantName%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Agent Id</th>
		<th>Session</th>
		<th>Dn</th>
		<th>Agent Name</th>
		<th>External Id</th>
		<th>Login</th>
		<th>Locked?</th>
		<th>Password Last Modified On</th>
		<th>Site</th>
		<th>Team</th>
		<th>Profile Id</th>
		<th>Media Profile</th>
		<th>Skill Profile</th>
		<th>Third Party Profile</th>
		<th>TPG URL</th>
		<th>External IP Address</th>
		<th>Host IP Address</th>
	</tr>
	<%
		Set<Agent> activeSet = new TreeSet<Agent>();
		Set<Agent> inactiveSet = new TreeSet<Agent>();
		List<Agent> sortedAgentList = new ArrayList<Agent>();
		for (Agent agent : agentList) {
			Set<String> activeChannelList = aaMgr
					.getActiveChannelForAgent(agent.getAgentId());
			if (null != activeChannelList && !activeChannelList.isEmpty()) {
				activeSet.add(agent);
			} else {
				inactiveSet.add(agent);
			}
		}
		sortedAgentList.addAll(activeSet);
		sortedAgentList.addAll(inactiveSet);

		int count = 1;
		for (Agent agent : sortedAgentList) {
			String classValue = "";
			Set<String> activeChannelList = aaMgr
					.getActiveChannelForAgent(agent.getAgentId());
			String acgUrl = "&nbsp;";
			String displayTeam = "&nbsp;";
			Date lastEvtDate = null;
			String dn = "&nbsp;";
			String agentSessionId = "&nbsp;";
			ActiveAgent activeAgent = null;
			String mediaProfileId = "&nbsp;";
			String skillProfileId = "&nbsp;";
                        ThirdPartyProfile atp = provMgr.getAgentTPProfileHandler()
					.getAgentTPProfileByAgent(agent.getAgentId(), agent.getSiteId());
			String teamId = null;
			String externalIpAddress = null;
			String hostIpAddress = null;
			StringBuilder sb = new StringBuilder();
			Formatter formatter = new Formatter(sb, Locale.US);
			boolean hasActiveChannels = false;
			if (null != activeChannelList && !activeChannelList.isEmpty()) {
				classValue = "style=\"color: #008B00; \"";
				hasActiveChannels = true;
				int chCounter = 1;
				for (String activeChannelId : activeChannelList) {
					activeAgent = aaMgr.getActiveAgentByChannelId(agent
							.getTenantId(), activeChannelId);
					if (activeAgent != null) {
						acgUrl = activeAgent.getAcgUrl();
						teamId = activeAgent.getTeamId();
						externalIpAddress = activeAgent.getExternalIpAddress();
						hostIpAddress = activeAgent.getHostIpAddress();
						displayTeam = "(" + teamId + ")";
						Team team = teamHandler.getTeam(teamId);
						if (team != null) {
							displayTeam += team.getTeamName();
						}
						dn = activeAgent.getDn();
						agentSessionId = activeAgent.getAgentSessionId();
						lastEvtDate = new Date(activeAgent.getTimestamp());
						//formatter.format("%1$s.[Id : %2$2s Type : %3$2s Status : %4$2s Time : %5$s]<br/>", chCounter++, activeAgent.getChannelId(), activeAgent.getChannelType(), activeAgent.getChannelStatus(), lastEvtDate);
					}
				}
			}
	%>
			<tr <%=classValue%>>
				<td><%=count++%></td>
				<td><%=hasActiveChannels ? "<a style='cursor:pointer' onClick='showPopWin(\"viewAgentChannels.jsp?agentId="
								+ agent.getAgentId()
								+ "&tenantId="
								+ agent.getTenantId()
								+ "\", 800, 450, null); return false;' >"
								+ agent.getAgentId()
								+ "</a> "
								: agent.getAgentId()%></td>
				<td><%=agentSessionId%></td>
				<td><%=dn%></td>
				<td><%=agent.getAgentName()%></td>
				<td><%=agent.getExternalId()%></td>
				<td><%=agent.getLogin()%></td>
				<td style='<%=agent.getIsLocked() ? "background-color: #8B0000;"
								: ""%>'><%=agent.getIsLocked()%></td>
				<td><%=new Date(agent.getPasswordLastModifiedTimestamp())%></td>
				<td><%=agent.getSiteId()%></td>
				<td><%=displayTeam%></td>
				<td><a href="viewSpecificAgentProfile.jsp?profileId=<%=agent.getProfileId()%>"><%=agent.getProfileId()%></a></td>
				<td> <%=teamId != null ? mediaProfileHandler
						.getAgentMediaProfileIdNameByAgent(agent.getAgentId(),
								teamId, agent.getSiteId()) : "&nbsp;"%></td>
				<td> <%=teamId != null ? skillProfileHandler
						.getSkillProfileIdName(agent.getAgentId(), teamId)
						: "&nbsp;"%></td>
				<td> <%=atp != null ? atp.getTpProfileId():"&nbsp;"%></td>
				<td><%=acgUrl%></td>
				<td><%= externalIpAddress != null ? externalIpAddress : "" %></td>
				<td><%= hostIpAddress != null ? hostIpAddress : "" %></td>
			</tr>
		<%
			}
		%>
</table>

<BR clear=all>

<%@include file="footer.jsp"%>
