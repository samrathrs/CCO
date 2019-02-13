<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page
	import="com.transerainc.aim.provisioning.handler.AgentProfileHandler"%>
<%@page import="com.transerainc.aim.pojo.AgentProfile"%>

<%@page import="com.transerainc.aim.provisioning.handler.AgentMediaProfileHandler"%>
<%@page import="com.transerainc.aim.pojo.MediaProfile"%>
<%@page import="com.transerainc.aim.provisioning.handler.SkillProfileHandler"%>
<%@page import="com.transerainc.aim.pojo.SkillProfile"%>
<%@page import="com.transerainc.aim.pojo.Skill"%>
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

	SkillProfileHandler spHandler = provMgr.getSkillProfileHandler();

	List<SkillProfile> skillProfileList =
	spHandler.getSkillProfilesForTenant(tenantId);
	
	Set<Tenant> tenantList = tHandler.getTenants();
	HashMap<String, HashMap<String, String>> assignmentMap = spHandler.getProfileAssignmentMap(tenantId);
	
	
%>
<body onload="stripe();">
<form name="actionForm"
    action="reloadEntities.jsp?name=skill-profiles" method="POST">
    <input type="hidden" name="tenantId" value="<%=tenantId%>">

<BR clear=all>
    
<div class="chromestyle" id="chromemenu">
<ul>
	<li><input type="submit" name="reload" value="Reload"><a href="viewSkillProfiles.jsp?tenantId=<%=tenantId%>"
		rel="dropmenu1"><%=tenantId%>. <%=tenantName%></a></li>
</ul>
</div>

<!--1st drop down menu -->
<div id="dropmenu1" class="dropmenudiv">
<%
	for (Tenant tenant : tenantList) {
		String tName = tenant.getCompanyName();
%> 	<a href="viewSkillProfiles.jsp?tenantId=<%=tenant.getTenantId() %>"> <%=String.format("%1$4s.", tenant.getTenantId()).replaceAll(" ", "&nbsp;")%><%=tName%></a>
<%
	}
%>
</div>
<script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script>


<table id="b" class="mytable">
	<caption><span id="desc">Agent Skill Profiles For tenant <%=tenantName%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Profile Id</th>
		<th>Profile Name</th>
		<th>Skill Id</th>
		<th>Skill Name</th>
		<th>Skill Value</th>
        <th>Enum Skill Id</th>
        <th>Enum Skill Name</th>
        <th>Skill Type</th>
        <th>Skill Type Name</th>
        <th>Skill Description</th>
	</tr>
	<%
		int count = 1;
			for (SkillProfile profile : skillProfileList) {
				List<Skill> skillList = profile.getSkillList();
				if(skillList != null) {
				for(Skill skill : skillList) {
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=profile.getProfileId()%></td>
		<td><%=profile.getProfileName()%></td>
		<td><%=skill.getId() %></td>
		<td><%=skill.getName() %></td>
		<td><%=skill.getValue() %></td>
		<td><%=skill.getEnumSkillId() %></td>
		<td><%=skill.getEnumSkillName()%></td>
		<td><%=skill.getType()%></td>
		<td><%=skill.getTypeName() %></td>
        <td><%=skill.getDescription() %></td>
	</tr>

	<%
			}}}
	%>
</table>


<table id="c" class="mytable">
	<caption><span id="desc">Agent Team Skill Profile Mapping </span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Agent Id</th>
		<th>Team Id</th>
		<th>Profile Id</th>
	</tr>
	<%
			count = 1;
			for (Map.Entry<String, HashMap<String, String>> entry : assignmentMap.entrySet()) {
				Map<String, String> teamProfileMap = entry.getValue();
				for (Map.Entry<String, String> teamEntry : teamProfileMap
							.entrySet()) {
	%>
	<tr>
		<td><%=count++%></td>
		<td><%=entry.getKey()%></td>
		<td><%=teamEntry.getKey()%></td>
		<td><%=teamEntry.getValue()%></td>
	</tr>

	<%
			
				}
			}
	%>
</table>

<BR clear=all>

<%@include file="footer.jsp"%>