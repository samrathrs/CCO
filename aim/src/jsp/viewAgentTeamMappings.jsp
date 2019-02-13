<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page import="com.transerainc.aim.pojo.Agent"%>
<%@page
	import="com.transerainc.aim.provisioning.handler.AgentToTeamMappingHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.TeamHandler"%>
<%@page import="com.transerainc.aim.pojo.AgentTeamMapping"%>
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

	AgentToTeamMappingHandler attHandler =
			provMgr.getAgentTeamMappingHandler();

	AgentHandler aHandler = provMgr.getAgentHandler();
	List<String> agentIdList = aHandler.getAgentIdsForTenant(tenantId);

	TeamHandler teamHandler = provMgr.getTeamHandler();
	
	Set<Tenant> tenantList = tHandler.getTenants();

	
%>
<body onload="stripe();">
<form name="actionForm"
	action="reloadEntities.jsp?name=agent-team-mappings" method="POST">
<input type="hidden" name="tenantId" value="<%=tenantId%>"> <BR
	clear=all>

<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a
		href="viewAgentTeamMappings.jsp?tenantId=<%=tenantId %>"
		rel="dropmenu1"><%=tenantId%>. <%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewAgentTeamMappings.jsp?tenantId=<%=tenant.getTenantId() %>"> <%=String.format("%1$4s.", tenant.getTenantId()).replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>

<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>

<table id="b" class="mytable">
	<caption><span id="desc">Agent To Team Mappings For
	tenant <%=tenantName%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Agent Id</th>
		<th>Teams</th>
	</tr>
	<%
		int count = 1;
		if (agentIdList != null) {
			for (String agentId : agentIdList) {
				List<AgentTeamMapping> teamList =
						attHandler.getMappingsForAgent(agentId);
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=agentId%></td>
		<td><%=teamList%></td>
	</tr>

	<%
		}
		}
	%>
</table>

<BR clear=all>

<%@include file="footer.jsp"%>