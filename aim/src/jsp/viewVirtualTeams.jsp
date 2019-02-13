<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page import="com.transerainc.aim.pojo.VirtualTeam"%>
<%@page
	import="com.transerainc.aim.provisioning.handler.VirtualTeamHandler"%>

<%@page import="com.transerainc.aim.pojo.Tenant"%><html>
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

	String tenantName = tHandler.getTenant(tenantId).getCompanyName();

	VirtualTeamHandler vtHandler = provMgr.getVirtualTeamHandler();

	List<VirtualTeam> vtList =
			vtHandler.getVirtualTeamsForTenant(tenantId);

	Collection<String> tenantIdList = tHandler.getTenantIdList();
	
	Set<Tenant> tenantList = tHandler.getTenants();
	
	if(null != vtList){
		Collections.sort(vtList);
	}
%>
<body onload="stripe();">
<form name="actionForm" action="reloadEntities.jsp?name=virtual-teams"
	method="POST"><input type="hidden" name="tenantId"
	value="<%=tenantId%>"> <BR clear=all>

<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a
		href="viewVirtualTeams.jsp?tenantId=<%=tenantId %>" rel="dropmenu1"><%=tenantId%>.
	<%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewVirtualTeams.jsp?tenantId=<%=tenant.getTenantId() %>"> <%=String.format("%1$4s.", tenant.getTenantId()).replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>

<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>


<table id="b" class="mytable">
	<caption><span id="desc">Virtual Teams For tenant <%=tenantName%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Virtual Team Id</th>
		<th>Virtual Team Name</th>
		<th>Virtual Team Type</th>
		<th>Seratel ACD</th>
	</tr>
	<%
		int count = 1;
		for (VirtualTeam vteam : vtList) {
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=vteam.getId()%></td>
		<td><%=vteam.getName()%></td>
		<td><%=vteam.getType()%></td>
		<td><%=vteam.getSeratelAcd()%></td>
	</tr>

	<%
		}
	%>
</table>
<BR clear=all>

<%@include file="footer.jsp"%>