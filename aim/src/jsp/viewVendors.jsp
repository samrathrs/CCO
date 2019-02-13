<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page
	import="com.transerainc.aim.provisioning.handler.ThirdPartyVendorHandler"%>
<%@page import="com.transerainc.aim.pojo.ThirdPartyVendor"%>
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

	ThirdPartyVendorHandler vndrHandler = provMgr.getThirdPartyVendorHandler();

	List<ThirdPartyVendor> vendorList =
			vndrHandler.getThirdPartyVendorForTenant(tenantId);
	
	if(vendorList != null){
		Collections.sort(vendorList);
	}
	Set<Tenant> tenantList = tHandler.getTenants();

%>
<body onload="stripe();">
<form name="actionForm"
    action="reloadEntities.jsp?name=third-party-vendors" method="POST">
    <input type="hidden" name="tenantId" value="<%=tenantId%>">

<BR clear=all>
    
<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a href="viewVendors.jsp?tenantId=<%=tenantId %>"
		rel="dropmenu1"><%=tenantId%>. <%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewVendors.jsp?tenantId=<%=tenant.getTenantId() %>"> <%=String.format("%1$4s.", tenant.getTenantId()).replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>
<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>


<table id="b" class="mytable">
	<caption><span id="desc">Multimedia Enablers For Tenant <%=tenantName%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Id</th>
		<th>Name</th>
		<th>Agent URL</th>
		<th>Provisioning URL</th>
	</tr>
	<%
		int count = 1;
		for (ThirdPartyVendor vendor : vendorList) {
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=vendor.getVendorId()%></td>
		<td><%=vendor.getVendorName()%></td>
		<td><%=vendor.getAgentUrl()%></td>
		<td><%=vendor.getProvUrl()%></td>
	</tr>

	<%
	}
	%>
</table>

<BR clear=all>

<%@include file="footer.jsp"%>
