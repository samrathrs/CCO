<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page
	import="com.transerainc.aim.provisioning.handler.AgentProfileHandler"%>
<%@page import="com.transerainc.aim.pojo.AgentProfile"%>
<%@page import="com.transerainc.aim.pojo.Tenant"%>

<html>
<head>
<link href="aim.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="chromestyle2.css" />
<script type="text/javascript" src="chrome.js"></script>
<title>AIM - (<%=request.getServerName()%>:<%=request.getServerPort()%>)</title>
</head>
<%@include file="header.jsp"%>

<%
			ProvisioningManager provMgr =
			(ProvisioningManager) AgentInformationManagerSystem
			.findBean("provManager");

	TenantHandler tHandler = provMgr.getTenantHandler();
	String tenantId = request.getParameter("tenantId");

	if (tenantId == null) {
		Collection<String> tenantIdList = tHandler.getTenantIdList();
		tenantId = tenantIdList.iterator().next();
	}

	Collection<String> tenantIdList = tHandler.getTenantIdList();
	String tenantName = tHandler.getTenant(tenantId).getCompanyName();

	AgentProfileHandler apHandler = provMgr.getAgentProfileHandler();

	List<AgentProfile> agentProfileList =
			apHandler.getAgentProfilesForTenant(tenantId);
	
	if(agentProfileList != null){
		Collections.sort(agentProfileList);
	}
	Set<Tenant> tenantList = tHandler.getTenants();

%>
<body onload="stripe();">
<form name="actionForm"
    action="reloadEntities.jsp?name=agent-profiles" method="POST">
    <input type="hidden" name="tenantId" value="<%=tenantId%>">

<BR clear=all>
    
<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a href="viewAgentProfiles.jsp?tenantId=<%=tenantId %>"
		rel="dropmenu1"><%=tenantId%>. <%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewAgentProfiles.jsp?tenantId=<%=tenant.getTenantId() %>"> <%=String.format("%1$4s.", tenant.getTenantId()).replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>
<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>


<table id="b" class="mytable">
	<caption><span id="desc">Agent Profiles For tenant <%=tenantName%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Agent Profile Id</th>
		<th>Agent Profile Name</th>
		<th>Parent Id</th>
		<th>Parent Type</th>
	</tr>
	<%
		int count = 1;
		for (AgentProfile profile : agentProfileList) {
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=profile.getProfileId()%></td>
		<td><a
			href="viewSpecificAgentProfile.jsp?profileId=<%=profile.getProfileId()%>"><%=profile.getProfileName()%></a></td>
		<td><%=profile.getParentId()%></td>
		<td><%=profile.getParentType()%></td>
	</tr>

	<%
	}
	%>
</table>

<BR clear=all>

<%@include file="footer.jsp"%>