<%@page import="java.util.List"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentHandler"%>
<%@page import="com.transerainc.aim.runtime.AgentLockoutManager"%>
<%@page import="com.transerainc.aim.pojo.AuthenticationFailure"%>
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

	AgentHandler aHandler = provMgr.getAgentHandler();

	AgentLockoutManager alMgr =
			(AgentLockoutManager) AgentInformationManagerSystem
					.findBean("agentLockoutManager");
	List<AuthenticationFailure> afList =
			alMgr.getAuthenticationFailureList();
%>
<body onload="stripe();">

	<BR clear=all>

<table id="b" class="mytable">
	<caption><span id="desc">Authorization failures</span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Agent Id</th>
		<th>Failure Count</th>
		<th>Is Locked?</th>
	</tr>
	<%
		int count = 1;
		for (AuthenticationFailure af : afList) {
	%>
	<tr style="color: #008B00; ">
		<td><%=count++%></td>
		<td><%=af.getAgentId()%></td>
		<td><%=af.getFailureCount()%></td>
		<td><%=af.getIsLocked()%> <%= af.getIsLocked() ? "<a href=\"authFailureIntf?command=releaseAgentLockout&agentId=" + af.getAgentId() + "\">Unlock</a>" : "" %></td>
		</td>
	</tr>

	<%
		}
	%>
</table>

<BR clear=all>

<%@include file="footer.jsp"%>