<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page import="com.transerainc.aim.runtime.ActiveSupervisorManager"%>
<%@page import="com.transerainc.aim.pojo.ActiveSupervisor"%>
<%@page import="com.transerainc.aim.pojo.Tenant"%>

<html>
<head>
<link href="aim.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="chromestyle2.css" />
<script type="text/javascript" src="chrome.js"></script>
<title>AIM - (<%=request.getServerName()%>:<%=request.getServerPort()%>)
</title>
</head>
<%@include file="header.jsp"%>

<%
	ProvisioningManager provMgr = (ProvisioningManager) AgentInformationManagerSystem
			.findBean("provManager");

	ActiveSupervisorManager supervisorMgr = (ActiveSupervisorManager) AgentInformationManagerSystem
			.findBean("activeSupervisorManager");

	TenantHandler tHandler = provMgr.getTenantHandler();
	String tenantId = request.getParameter("tenantId");

	if (tenantId == null) {
		Collection<String> tenantIdList = tHandler.getTenantIdList();
		tenantId = tenantIdList.iterator().next();
	}

	Collection<String> tenantIdList = tHandler.getTenantIdList();
	String tenantName = tHandler.getTenant(tenantId).getCompanyName();

	Set<Tenant> tenantList = tHandler.getTenants();

	List<ActiveSupervisor> supervisorList = supervisorMgr
			.getActiveSupervisorByTenantId(tenantId);

	if (supervisorList != null) {
		Collections.sort(supervisorList);
	}
%>
<body onload="stripe();">
	<form name="actionForm"
		action="reloadEntities.jsp?name=active-supervisors" method="POST">
		<input type="hidden" name="tenantId" value="<%=tenantId%>"> <BR
			clear=all>

		<div class="chromestyle" id="chromemenu">
			<ul>
				<li><a
					href="viewActiveSupervisors.jsp?tenantId=<%=tenantId%>"
					rel="dropmenu1"><%=tenantId%>. <%=tenantName%></a></li>
			</ul>
		</div>

		<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>


		<!--1st drop down menu -->
		<div id="dropmenu1" class="dropmenudiv">
			<%
				for (Tenant tenant : tenantList) {
					String tName = tenant.getCompanyName();
			%>
			<a
				href="viewActiveSupervisors.jsp?tenantId=<%=tenant.getTenantId()%>">
				<%=String.format("%1$4s.", tenant.getTenantId())
						.replaceAll(" ", "&nbsp;")%><%=tName%></a>
			<%
				}
			%>
		</div>

		<table id="b" class="mytable">
			<caption>
				<span id="desc">Active Supervisors For tenant <%=tenantName%></span>
			</caption>
			<tr>
				<th>&nbsp;</th>
				<th>Supervisor Id</th>
				<th>Session Id</th>
				<th>Server URL</th>
				<th>Timestamp</th>
			</tr>
			<%
				int count = 1;
				for (ActiveSupervisor supervisor : supervisorList) {
			%>
			<tr>
				<td><%=count++%></td>
				<td><%=supervisor.getSupervisorId()%></td>
				<td><%=supervisor.getSessionId()%></td>
				<td><%=supervisor.getCallbackUrl()%></td>
				<td><%=new Date(supervisor.getTimestamp())%></td>
			</tr>

			<%
				}
			%>
		</table>

		<BR clear=all>

		<%@include file="footer.jsp"%>