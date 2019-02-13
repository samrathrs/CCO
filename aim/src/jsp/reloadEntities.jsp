<%@ page import="com.transerainc.tam.ns.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.concurrent.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.db.SqlStatementManager"%>
<%@page import="com.transerainc.aim.provisioning.ProvisioningConnection"%>
<%@page import="com.transerainc.aim.provisioning.ProvisioningDAO"%>
<html>
<head>
<link href="aim.css" rel="stylesheet" type="text/css">
<title>AIM - Reload Provisioning Data - (<%=request.getServerName()%>:<%=request.getServerPort()%>)</title>
</head>
<%@include file="header.jsp"%>
<body>
<form>
<%
	String entityType = request.getParameter("name");
	if (entityType != null) {
%>

<center>
<h2>Reoading entities - <%=entityType%></h2>

<%
	ProvisioningManager pmgr =
				(ProvisioningManager) AgentInformationManagerSystem
						.findBean("provManager");
		SqlStatementManager stmtMgr =
				(SqlStatementManager) AgentInformationManagerSystem
						.findBean("sqlStatementMgr");
		ConfigurationManager cmgr =
				(ConfigurationManager) AgentInformationManagerSystem
						.findBean("configManager");
		ProvisioningConnection provCon =
				new ProvisioningConnection(cmgr);
		ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
		if (entityType.equals("adhoc-dial-properties")) {
			pmgr.initializeAdhocDialPropertiesHandler(provDAO);
		} else if (entityType.equals("agent-profiles")) {
			pmgr.initializeAgentProfileHandler(provDAO);
        } else if (entityType.equals("agent-media-profiles")) {
            pmgr.initializeAgentMediaProfileHandler(provDAO);
        } else if (entityType.equals("skill-profiles")) {
            pmgr.initializeSkillProfileHandler(provDAO);
		} else if (entityType.equals("agents")) {
			pmgr.initializeAgentHandler(provDAO);
		} else if (entityType.equals("aux-codes")) {
			pmgr.initializeAuxCodeHandlers(provDAO);
		} else if (entityType.equals("agent-team-mappings")) {
			pmgr.initializeAgentToTeamMappingHandler(provDAO);
		} else if (entityType.equals("password-policies")) {
			pmgr.initializePasswordPolicyHandler(provDAO);
		} else if (entityType.equals("speed-dial-lists")) {
			pmgr.initializeSpeedDialListHandler(provDAO);
		} else if (entityType.equals("teams")) {
			pmgr.initializeTeamHandler(provDAO);
		} else if (entityType.equals("tenants")) {
			pmgr.initializeTenantHandler(provDAO);
		} else if (entityType.equals("sites")) {
			pmgr.initializeSiteHandler(provDAO);
		} else if (entityType.equals("virtual-teams")) {
			pmgr.initializeVirtualTeamHandler(provDAO);
		} else if (entityType.equals("third-party-profiles")) {
			pmgr.initializeAgentTPProfileHandler(provDAO);
		} else if (entityType.equals("third-party-vendors")) {
			pmgr.initializeThirdPartyVendorsHandler(provDAO);
		}else if (entityType.equals("active-supervisors")) {
			//pmgr.initializeThirdPartyVendorsHandler(provDAO);
		}else {
			out.println("Unknown entity type - " + entityType);
		}
%>

<h2>Done.</h2>
<%
	}
%> <%@include file="footer.jsp"%>