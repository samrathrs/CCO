<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page import="com.transerainc.aim.pojo.Agent"%>
<%@page import="com.transerainc.aim.provisioning.handler.TeamHandler"%>
<%@page import="com.transerainc.aim.pojo.Team"%>
<%@page import="com.transerainc.aim.provisioning.handler.WrapupHandler"%>
<%@page import="com.transerainc.aim.pojo.AuxilaryCode"%>
<%@page
	import="com.transerainc.aim.provisioning.handler.IdleCodeHandler"%>

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

	IdleCodeHandler wHandler = provMgr.getIdleCodeHandler();

	List<AuxilaryCode> acList =
			wHandler.getIdleCodesForTenant(tenantId);

	if(acList != null){
		Collections.sort(acList);
	}
	
	Collection<String> tenantIdList = tHandler.getTenantIdList();
	
	Set<Tenant> tenantList = tHandler.getTenants();

	
%>
<body onload="stripe();">
<form name="actionForm" action="reloadEntities.jsp?name=aux-codes"
	method="POST"><input type="hidden" name="tenantId"
	value="<%=tenantId%>"> <BR clear=all>

<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a
		href="viewIdleCodes.jsp?tenantId=<%=tenantId %>" rel="dropmenu1"><%=tenantId%>.
	<%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewIdleCodes.jsp?tenantId=<%=tenant.getTenantId() %>"> <%=String.format("%1$4s.", tenant.getTenantId()).replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>

<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>


<table id="b" class="mytable">
	<caption><span id="desc">Idle Codes For tenant <%=tenantName%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Id</th>
		<th>Name</th>
		<th>Is Default?</th>
	</tr>
	<%
		int count = 1;
		for (AuxilaryCode aCode : acList) {
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=aCode.getCodeId()%></td>
		<td><%=aCode.getCodeName()%></td>
		<td><%=aCode.isDefaultAuxCode()%></td>
	</tr>

	<%
	}
	%>
</table>
<BR clear=all>

<%@include file="footer.jsp"%>