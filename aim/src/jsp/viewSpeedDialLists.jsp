<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page import="com.transerainc.aim.pojo.Agent"%>
<%@page import="com.transerainc.aim.provisioning.handler.TeamHandler"%>
<%@page import="com.transerainc.aim.pojo.Team"%>
<%@page
	import="com.transerainc.aim.provisioning.handler.SpeedDialListHandler"%>
<%@page import="com.transerainc.aim.pojo.SpeedDialList"%>
<%@page import="com.transerainc.aim.pojo.SpeedDialListEntry"%>
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

	String tenantName = tHandler.getTenant(tenantId).getCompanyName();

	SpeedDialListHandler sdlHandler = provMgr.getSpeedDialListHandler();

	List<SpeedDialList> sdlList =
			sdlHandler.getSpeedDialListsForTenant(tenantId);

	Collection<String> tenantIdList = tHandler.getTenantIdList();
	
	Set<Tenant> tenantList = tHandler.getTenants();
%>
<body onload="stripe();">
<form name="actionForm"
	action="reloadEntities.jsp?name=speed-dial-lists" method="POST"><input
	type="hidden" name="tenantId" value="<%=tenantId%>"> <BR
	clear=all>
    
<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a href="viewSpeedDialLists.jsp?tenantId=<%=tenantId %>"
		rel="dropmenu1"><%=tenantId%>. <%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewSpeedDialLists.jsp?tenantId=<%=tenant.getTenantId() %>"> <%=String.format("%1$4s.", tenant.getTenantId()).replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>

<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>


<table id="b" class="mytable">
	<caption><span id="desc">Speed Dial List For tenant <%=tenantName%></span></caption>
	<tr>
		<th rowspan="2">&nbsp;
		<th rowspan="2">Name
		<th rowspan="2">Id
		<th rowspan="2">Parent Id
		<th rowspan="2">Parent Type
		<th colspan="3">Entries
	</tr>
	<tr>
		<th>Name
		<th>Entry Id
		<th>Number
	</tr>
	<%
		int count = 1;
		for (SpeedDialList sdl : sdlList) {
			List<SpeedDialListEntry> sdleList =
			sdl.getSpeedDialListEntryList();
			int rowSpan = 1;
			if (sdleList != null) {
				rowSpan = sdleList.size();
			}
	%>
	<tr>
		<td rowspan="<%=rowSpan%>"><%=count++%></td>
		<td rowspan="<%=rowSpan%>"><%=sdl.getName()%>
		<td rowspan="<%=rowSpan%>"><%=sdl.getListId()%>
		<td rowspan="<%=rowSpan%>"><%=sdl.getParentId()%>
		<td rowspan="<%=rowSpan%>"><%=sdl.getParentType()%> <%
 			if (sdleList != null) {
 			int rowCount = 0;
 			for (SpeedDialListEntry sdle : sdleList) {
 		rowCount++;
 		if (rowCount > 1) {
 			out.println("<tr>");
 		}
 %>
		
		<td><%=sdle.getName()%>
		<td><%=sdle.getEntryId()%>
		<td><%=sdle.getNumber()%> <%
 		}
 		} else {
 %>
		
		<td>&nbsp;
		<td>&nbsp;
		<td>&nbsp;
	</tr>
	<%
		}
		}
	%>
</table>
<BR clear=all>

<%@include file="footer.jsp"%>