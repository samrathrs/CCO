<%@ page import="java.util.*"%>

<%@page import="com.transerainc.aim.system.AgentInformationManagerSystem"%>
<%@page import="com.transerainc.aim.conf.xsd.AgentInformationManager"%>
<%@page import="com.transerainc.aim.provisioning.ProvisioningManager"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.TenantHandler"%>
<%@page import="com.transerainc.aim.pojo.Agent"%>
<%@page import="com.transerainc.aim.runtime.ActiveAgentManager"%>
<%@page import="com.transerainc.aim.pojo.ActiveAgent"%>
<%@page import="com.transerainc.aim.provisioning.handler.TeamHandler"%>
<%@page import="com.transerainc.aim.pojo.Team"%>
<%@page import="com.transerainc.aim.provisioning.handler.SkillProfileHandler"%>
<%@page import="com.transerainc.aim.provisioning.handler.AgentMediaProfileHandler"%>

<html>
<head>
<link rel="stylesheet" href="aim.css" type="text/css">
<link rel="stylesheet" href="chromestyle2.css" type="text/css" />
<link rel="stylesheet" href="style.css" type="text/css"  >
<link rel="stylesheet" href="demos.css" media="screen" type="text/css">
<link rel="stylesheet" href="window.css" media="screen" type="text/css">
<link rel="stylesheet" href="resize.css" media="screen" type="text/css">
<script type="text/javascript" src="ajax.js"></script>
<script type="text/javascript" src="chrome.js"></script>
<script type="text/javascript" src="dhtml-suite-for-applications-without-comments.js"></script>
<title>AIM - (<%=request.getServerName()%>:<%=request.getServerPort()%>)</title>

<script type="text/javascript">
// this function is needed to work around 
 // a bug in IE related to element attributes
 function hasClass(obj) {
    var result = false;
    if (obj.getAttributeNode("class") != null) {
        result = obj.getAttributeNode("class").value;
    }
    return result;
 }   

function stripe() {

   // if arguments are provided to specify the colours
   // of the even & odd rows, then use the them;
   // otherwise use the following defaults:
   var evenColor = "#F0F5FE";
   var oddColor = "#FFFFFF";

   // obtain a reference to the desired table
   // if no such table exists, abort
   var tables = document.getElementsByTagName("table");
   if (! tables) { return; }

   // by definition, tables can have more than one tbody
   // element, so we'll have to get the list of child
   // &lt;tbody&gt;s
   //var tbodies = table.getElementsByTagName("table");

   // and iterate through them...
   for (var h = 0; h < tables.length; h++) {
     var table = tables[h];
     
     // the flag we'll use to keep track of
     // whether the current row is odd or even
     var even = false;

    // find all the &lt;tr&gt; elements...
     var trs = table.getElementsByTagName("tr");

     // ... and iterate through them
     for (var i = 0; i < trs.length; i++) {

       // avoid rows that have a class attribute
       // or backgroundColor style
       if (! hasClass(trs[i]) &&
           ! trs[i].style.backgroundColor) {

         // get all the cells in this row...
         var tds = trs[i].getElementsByTagName("td");

         // and iterate through them...
         for (var j = 0; j < tds.length; j++) {

           var mytd = tds[j];

           // avoid cells that have a class attribute
           // or backgroundColor style
           if (! hasClass(mytd) &&
               ! mytd.style.backgroundColor) {

             mytd.style.backgroundColor =
               even ? evenColor : oddColor;

           }
         }
       }
       // flip from odd to even, or vice-versa
       even =  ! even;
     }
   }
 }
</script>

</head>



<%
	ProvisioningManager provMgr =
			(ProvisioningManager) AgentInformationManagerSystem.findBean("provManager");
	String tenantId = request.getParameter("tenantId");
	String agentId = request.getParameter("agentId");
	TenantHandler tHandler = provMgr.getTenantHandler();
	AgentHandler aHandler = provMgr.getAgentHandler();
	String tenantName = tHandler.getTenant(tenantId).getCompanyName();
	Agent agent = aHandler.getAgent(agentId);
	ActiveAgentManager aaMgr =
			(ActiveAgentManager) AgentInformationManagerSystem
					.findBean("activeAgentManager");
	Set<String> activeChannelList = aaMgr.getActiveChannelForAgent(agentId);
	ActiveAgent activeAgent = null;
	int count = 1;
%>

<body onload="stripe();">
<form>

<table id="b" class="mytable">
	<caption><span id="desc">Channels for Agent (<%=agent.getAgentId()%>) <%=agent.getAgentName()%></span></caption>
	<tr>
		<th>&nbsp;</th>
		<th>Channel Id</th>
		<th>Channel Type</th>
		<th>Channel Status</th>
		<th>Event Time Stamp</th>
	</tr>

<%for(String activeChannelId : activeChannelList){
	activeAgent = aaMgr.getActiveAgentByChannelId(tenantId, activeChannelId);
	if (activeAgent != null) {%>
		<tr>
			<td><%=count++%></td>
			<td><%=activeAgent.getChannelId()%></td>
			<td><%=activeAgent.getChannelType()%></td>
			<td><%=activeAgent.getChannelStatus()%></td>
			<td><%=new Date(activeAgent.getTimestamp())%></td>
		</tr>		
	<%}
}%>
</table>

<a href="#" onclick="toggleTabs()" > Show/Hide Channel Tabs</a>

<div id="myWindow" windowProperties="title:Active channels for agent (<%=agent.getAgentId()%>) <%=agent.getAgentName()%>,resizable:true,closable:true,maxWidth:700,maxHeight:350,dragable:true,cookieName:myWindowCookie1,xPos:200,yPos:200,minWidth:500,minHeight:300,activeTabId:windowContent1">
<%
count = 1;
for(String activeChannelId : activeChannelList){
	activeAgent = aaMgr.getActiveAgentByChannelId(tenantId, activeChannelId);
	if (activeAgent != null) {%>
	<div id="windowContent<%=count%>" class="DHTMLSuite_windowContent" tabProperties="tabTitle:<%=activeAgent.getChannelType()%>">
		<table id="b" class="mytable">
		<caption><span id="desc">Channel <%=activeAgent.getChannelId()%></span></caption>
		<tr>
			<th>&nbsp;</th>
			<th>Channel Id</th>
			<th>Channel Type</th>
			<th>Channel Status</th>
			<th>Event Time Stamp</th>
		</tr>
		<tr>
			<td><%=count++%></td>
			<td><%=activeAgent.getChannelId()%></td>
			<td><%=activeAgent.getChannelType()%></td>
			<td><%=activeAgent.getChannelStatus()%></td>
			<td><%=new Date(activeAgent.getTimestamp())%></td>
		</tr>		
		</table>
	</div>
	<%}
}%>
</div>
<script type="text/javascript">
var windowModel = new DHTMLSuite.windowModel();
windowModel.createWindowModelFromMarkUp('myWindow');
var myWindow = new DHTMLSuite.windowWidget();
myWindow.addWindowModel(windowModel);
myWindow.init();
myWindow.setStatusBarText('Agent channels');
myWindow.close();

var isWindowOpen = false;

function toggleTabs(){
	if(isWindowOpen){
		myWindow.close();
		isWindowOpen = false;
	}else{
		myWindow.show();
		isWindowOpen = true;
	}
}
</script>
<BR clear=all>
<%@include file="footer.jsp"%>