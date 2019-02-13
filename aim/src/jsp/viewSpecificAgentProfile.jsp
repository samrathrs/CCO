<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page import="com.transerainc.aim.pojo.Tenant"%>
<%@page
	import="com.transerainc.aim.provisioning.handler.AgentProfileHandler"%>
<%@page import="com.transerainc.aim.pojo.AgentProfile"%>
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

	String profileId = request.getParameter("profileId");
	AgentProfileHandler profileHandler =
			provMgr.getAgentProfileHandler();

	AgentProfile profile = profileHandler.getAgentProfile(profileId);
%>
<body onload="stripe();">
<form>
<table id="b" class="mytable">
	<caption><span id="desc">Profile Text</span></caption>
	<tr>
		<td><textarea><%=profile.getProfileText()%></textarea>
	</tr>
</table>
<BR clear=all>

<%@include file="footer.jsp"%>