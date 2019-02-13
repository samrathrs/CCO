
<%@page import="com.transerainc.aim.system.AgentInformationManagerSystem"%>
<%@page import="com.transerainc.aim.runtime.AgentLockoutManager"%>
<%@page import="com.transerainc.aim.pojo.AuthenticationFailure"%>

<%
String command = request.getParameter("command");
String agentId = request.getParameter("agentId");
AgentLockoutManager alMgr =
			(AgentLockoutManager) AgentInformationManagerSystem
					.findBean("agentLockoutManager");

if("unlock".equals(command) && agentId != null && agentId.length() > 0) {
	// Unlock and notify other AIMs
	alMgr.clearAgentLockout(agentId, true);
}
response.sendRedirect("viewAuthorizationFailures.jsp");
%>
