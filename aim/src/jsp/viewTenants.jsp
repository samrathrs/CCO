<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
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

	ConfigurationManager cmgr =
			(ConfigurationManager) AgentInformationManagerSystem
					.findBean("configManager");

	TenantHandler tHandler = provMgr.getTenantHandler();
	Collection<String> tenantIdList = tHandler.getTenantIdList();
	Set<Tenant> tenantSet = tHandler.getTenants();
%>
<body onload="stripe();">
<form name="actionForm" action="reloadEntities.jsp?name=tenants"
	method="POST">

<center><input type="submit" name="reload" value="Reload">
</center>
<table id="b" class="mytable">
	<caption><span id="desc">Tenant List</span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Tenant Id</th>
		<th>Company Name</th>
		<th>Login Domain</th>
		<th>CAD Hidden From Agent</th>
		<th>Auto Wrapup Interval</th>
		<th>Tenant XML URL</th>
		<th>Login Precedence</th>
		<th>Address Book Id</th>
	</tr>
	<%
		int count = 1;
		for (Tenant tenant : tenantSet) {
			String classValue = "";

			if (cmgr.isTenantShutdown(tenant.getTenantId())) {
				classValue =
						"style=\"color: #EE0000;  font-weight: bolder;\"";
			}
	%>
	<tr <%=classValue%>>
		<td><%=count++%></td>
		<td><%=tenant.getTenantId()%></td>
		<td><%=tenant.getCompanyName()%></td>
		<td><%=tenant.getLoginDomain()%></td>
		<td><%=tenant.getCadHiddenFromAgent()%></td>
		<td><%=tenant.getAutoWrapupInterval()%></td>
		<td><%=tenant.getTenantXmlUrl()%></td>
		<td><%=(tenant.isFirstLoginPrecedence()) ? "First"
						: "Second"%>
		<td><%=tenant.getAddressBookId()%></td>
	</tr>

	<%
		}
	%>
</table>
<BR clear=all>

<%@include file="footer.jsp"%>