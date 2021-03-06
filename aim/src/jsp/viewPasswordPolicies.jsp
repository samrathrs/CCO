<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page import="com.transerainc.aim.pojo.Agent"%>
<%@page import="com.transerainc.aim.provisioning.handler.TeamHandler"%>
<%@page import="com.transerainc.aim.pojo.Team"%>
<%@page
	import="com.transerainc.aim.provisioning.handler.PasswordPolicyHandler"%>
<%@page import="com.transerainc.aim.pojo.PasswordPolicy"%>
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

	PasswordPolicyHandler ppHandler =
			provMgr.getPasswordPolicyHandler();

	List<PasswordPolicy> ppList =
			ppHandler.getPasswordPolicysForTenant(tenantId);

	Collection<String> tenantIdList = tHandler.getTenantIdList();
	
	Set<Tenant> tenantList = tHandler.getTenants();

%>
<body onload="stripe();">
<form name="actionForm"
	action="reloadEntities.jsp?name=password-policies" method="POST"><input
	type="hidden" name="tenantId" value="<%=tenantId%>"> <BR
	clear=all>
    
<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a href="viewPasswordPolicies.jsp?tenantId=<%=tenantId %>"
		rel="dropmenu1"><%=tenantId%>. <%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewPasswordPolicies.jsp?tenantId=<%=tenant.getTenantId() %>"> <%=String.format("%1$4s.", tenant.getTenantId()).replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>

<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>

<table id="b" class="mytable">
	<caption><span id="desc">Password Policies For tenant
	<%=tenantName%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Id</th>
		<th>Name</th>
		<th>Expire Period</th>
		<th>Max Invalid Attempts</th>
		<th>Default</th>
	</tr>
	<%
		int count = 1;
		for (PasswordPolicy policy : ppList) {
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=policy.getId()%></td>
		<td><%=policy.getName()%></td>
		<td><%=policy.getPwdExpirePeriod()%></td>
		<td><%=policy.getMaxInvalidAttempts()%></td>
		<td><%=policy.getIsDefault()%></td>
	</tr>

	<%
	}
	%>
</table>
<BR clear=all>

<%@include file="footer.jsp"%>