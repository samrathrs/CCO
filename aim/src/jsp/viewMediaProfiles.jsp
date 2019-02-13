<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page
	import="com.transerainc.aim.provisioning.handler.AgentProfileHandler"%>
<%@page import="com.transerainc.aim.pojo.AgentProfile"%>

<%@page import="com.transerainc.aim.provisioning.handler.AgentMediaProfileHandler"%>
<%@page import="com.transerainc.aim.pojo.MediaProfile"%>
<%@page import="com.transerainc.aim.pojo.Tenant"%>
<%@page import="com.transerainc.aim.runtime.ActiveAgentManager"%><html>
<head>
<link href="aim.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="chromestyle2.css" />
<script type="text/javascript" src="chrome.js"></script>
<title>AIM - (<%=request.getServerName()%>:<%=request.getServerPort()%>)</title>
</head>
<%@include file="header.jsp"%>

<%
	ProvisioningManager provMgr = (ProvisioningManager) AgentInformationManagerSystem
			.findBean("provManager");

	TenantHandler tHandler = provMgr.getTenantHandler();
	String tenantId = request.getParameter("tenantId");

	if (tenantId == null) {
		Collection<String> tenantIdList = tHandler.getTenantIdList();
		tenantId = tenantIdList.iterator().next();
	}

	Collection<String> tenantIdList = tHandler.getTenantIdList();
	String tenantName = tHandler.getTenant(tenantId).getCompanyName();
	Set<Tenant> tenantList = tHandler.getTenants();
	AgentMediaProfileHandler ampHandler = provMgr
			.getAgentMediaProfileHandler();
	ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
			.findBean("activeAgentManager");

	Set<MediaProfile> agentMediaProfileList = ampHandler
			.getAgentMediaProfilesForTenant(tenantId);

	HashMap<String, HashMap<String, String>> assignmentMap = ampHandler.getProfileAssignmentMap(tenantId);
	
%>
<body onload="stripe();">
<form name="actionForm"
    action="reloadEntities.jsp?name=agent-media-profiles" method="POST">
    <input type="hidden" name="tenantId" value="<%=tenantId%>">

<BR clear=all>
    
<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a href="viewMediaProfiles.jsp?tenantId=<%=tenantId%>"
		rel="dropmenu1"><%=tenantId%>. <%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewMediaProfiles.jsp?tenantId=<%=tenant.getTenantId() %>"> <%=String.format("%1$4s.", tenant.getTenantId()).replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>
<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>


<table id="b" class="mytable">
	<caption><span id="desc">Agent Media Profiles For tenant <%=tenantName%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Media Profile Id</th>
		<th>Profile Name</th>
		<th>Blending Mode</th>
		<th>Number of Phone Calls</th>
        <th>Number of Emails</th>
        <th>Number of Faxes</th>
        <th>Number of Chats</th>
        <th>Number of Videos</th>
        <th>Number of Other</th>
	</tr>
	<%
		int count = 1;
		for (MediaProfile profile : agentMediaProfileList) {
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=profile.getMediaProfileId()%></td>
		<td><%=profile.getMediaProfileName()%></td>
		<td><%=profile.getBlendingMode()%></td>
		<td><%=profile.getNumberOfTelephonyChannels()%></td>
		<td><%=profile.getNumberOfEmailChannels()%></td>
		<td><%=profile.getNumberOfFaxChannels()%></td>
		<td><%=profile.getNumberOfChatChannels()%></td>
		<td><%=profile.getNumberOfVideoChannels()%></td>
		<td><%=profile.getNumberOfOtherChannels()%></td>
	</tr>

	<%
		}
	%>
</table>

<table id="c" class="mytable">
	<caption><span id="desc">Entity Media Profile Assignment</span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Entity Id</th>
		<th>Entity Type</th>
		<th>Media Profile Id</th>
	</tr>
	<%
		count = 1;
		for (Map.Entry<String, HashMap<String, String>> entry : assignmentMap.entrySet()) {
			String entityType = "Unknown";
			if("3".equals(entry.getKey())) {
				entityType = "Site";
			} else if("4".equals(entry.getKey())) {
				entityType = "Team";
			} else if("5".equals(entry.getKey())) {
				entityType = "Agent";
			}
			HashMap<String, String> entityMap = entry.getValue();
			if(entityMap != null){
				for(Map.Entry<String, String> entity : entityMap.entrySet()){ %>
					<tr>
						<td><%=count++%></td>
						<td><%=entity.getKey()%></td>
						<td><%=entityType%></a></td>
						<td><%=entity.getValue()%></td>
					</tr>
				<%}
			}
		}
	%>
</table>


<table id="d" class="mytable">
	<caption><span id="desc">Media Profiles used by Logged in Agents</span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Profile Id</th>
		<th>Profile Name</th>
		<th>Agents</th>
	</tr>
	<%
		count = 1;
		for (MediaProfile profile : agentMediaProfileList) {
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=profile.getMediaProfileId()%></td>
		<td><%=profile.getMediaProfileName()%></td>
		<td><%=aaMgr.getActiveAgentsForMediaProfile(profile
								.getMediaProfileId()) != null ? aaMgr
						.getActiveAgentsForMediaProfile(
								profile.getMediaProfileId()).toString()
						: "[none]"%></a></td>
	</tr>
	<%
		}
	%>
</table>

<BR clear=all>

<%@include file="footer.jsp"%>