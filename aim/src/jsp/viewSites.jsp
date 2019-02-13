<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page import="com.transerainc.aim.pojo.Agent"%>
<%@page import="com.transerainc.aim.provisioning.handler.SiteHandler"%>
<%@page import="com.transerainc.aim.pojo.Site"%>

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

	SiteHandler siteHandler = provMgr.getSiteHandler();

	List<Site> siteList = siteHandler.getSitesForTenant(tenantId);

	Collection<String> tenantIdList = tHandler.getTenantIdList();
	
	Set<Tenant> tenantList = tHandler.getTenants();
	
	if(siteList != null){
		Collections.sort(siteList);
	}
%>
<body onload="stripe();">
<form name="actionForm" action="reloadEntities.jsp?name=sites"
	method="POST"><input type="hidden" name="tenantId"
	value="<%=tenantId%>"> <BR clear=all>

<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a
		href="viewSites.jsp?tenantId=<%=tenantId %>" rel="dropmenu1"><%=tenantId%>.
	<%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewSites.jsp?tenantId=<%=tenant.getTenantId() %>"> <%=String.format("%1$4s.", tenant.getTenantId()).replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>

<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>


<table id="b" class="mytable">
	<caption><span id="desc">Sites For tenant <%=tenantName%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Site Id</th>
		<th>Site Name</th>
	</tr>
	<%
		int count = 1;
		for (Site site : siteList) {
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=site.getSiteId()%></td>
		<td><%=site.getSiteName()%></td>
	</tr>

	<%
	}
	%>
</table>
<BR clear=all>

<%@include file="footer.jsp"%>