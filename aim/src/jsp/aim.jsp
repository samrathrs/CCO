<%@ page import="com.transerainc.tam.config.*"%>
<%@ page import="com.transerainc.tam.ns.*"%>
<%@ page import="com.transerainc.provisioning.notificationserver.*"%>
<%@ page import="java.util.*"%>
<%@page
	import="com.transerainc.aim.system.AgentInformationManagerSystem"%>
<%@page import="com.transerainc.aim.runtime.AIMNotificationManager"%>
<%@page import="com.transerainc.aim.conf.ConfigurationManager"%>

<%
	ConfigurationManager cmgr =
			(ConfigurationManager) AgentInformationManagerSystem
					.findBean("configManager");
%>

<%@page import="com.transerainc.aim.pojo.AIMNotification"%>
<html>
<head>
<link href="aim.css" rel="stylesheet" type="text/css">
<title>Agent Information Manager - <%=cmgr.getInstanceName()%> -
(<%=request.getServerName()%>:<%=request.getServerPort()%>)</title>
<%@include file="header.jsp"%>
<script language="javascript">
		function doResubscribe() {
			window.status= "Resubscribing all known notification server registrations.";
	        if(confirm("Are you sure you want to resubscribe with the Notification Server?")) {
	            window.location="nsResubscribe.jsp?enterpriseId=" + document.forms["aimMainPageForm"].enterpriseId.value;
	        }
		}
	</script>
<script language="JavaScript" type="text/javascript">
      var panels = new Array('panel1', 'panel2', 'panel3');
      var selectedTab = null;
      function showPanel(tab, name)
      {
        if (selectedTab) 
        {
          selectedTab.style.backgroundColor = '';
          selectedTab.style.background = 'url(tbar.gif)';
          selectedTab.style.paddingTop = '';
          selectedTab.style.paddingBottom = '';
        }
        selectedTab = tab;
        selectedTab.style.backgroundColor = 'white';
        selectedTab.style.background = 'url(tab3_bg.gif)';
        selectedTab.style.paddingTop = '';
        for(i = 0; i < panels.length; i++)
        {
          document.getElementById(panels[i]).style.display = (name == panels[i]) ? 'block':'none';
        }
        return false;
      }
    </script>
</head>
<body
	onload="showPanel(document.getElementById('tab1'), 'panel1');stripe();">

<form name="aimMainPageForm" method="POST"><a href="" class="tab"
	onclick="return showPanel(this, 'panel1');" id="tab1">System</a> <a
	href="" class="tab" onclick="return showPanel(this, 'panel2');">Notification
Subscriptions</a> <a href="" class="tab"
	onclick="return showPanel(this, 'panel3');">Configuration</a> <%
 	AIMNotificationManager notifMgr =
 			(AIMNotificationManager) AgentInformationManagerSystem
 					.findBean("aimNotifManager");
 %>

<div class="panel" id="panel1" style="display: block">

<table id="a" class="mytable">
	<caption><span id="desc">Instance Parameters</span></caption>
	<tr>
		<th>Param Name</th>
		<th>Param Value</th>
	</tr>
	<tr>
		<td align="left">Host Name</td>
		<td align="left"><%=cmgr.getHostname()%></td>
	</tr>
	<tr>
		<td align="left">Instance Name</td>
		<td align="left"><%=cmgr.getInstanceName()%></td>
	</tr>
	<tr>
		<td align="left">My URL</td>
		<td align="left"><%=cmgr.getMyUrl()%></td>
	</tr>
	<tr>
		<td align="left">AIM Notification Error Sleep Time</td>
		<td align="left"><%=notifMgr.getErrorSleepTime()%></td>
	</tr>
	<tr>
		<td align="left">AIM Notification Max Queue Size</td>
		<td align="left"><%=notifMgr.getMaxQueueSize()%></td>
	</tr>
	<tr>
		<td align="left">AIM Notification Current Queue Size</td>
		<td align="left"><%=notifMgr.getNotificationQueueSize()%></td>
	</tr>
	<tr>
		<td align="left">AIM Notifications Sent</td>
		<td align="left"><%=notifMgr.getNumNotificationsSent()%></td>
	</tr>
	<tr>
		<td align="left">AIM Notifications Lost</td>
		<td align="left"><%=notifMgr.getNumNotificationsLost()%></td>
	</tr>
	<tr>
		<td align="left">AIM Notification Errors</td>
		<td align="left"><%=notifMgr.getNumNotificationErrors()%></td>
	</tr>
	<tr>
		<td align="left">Provisioning URL</td>
		<td align="left"><%=confmgr.getAgentInformationManager().getProvisioningUrl()%></td>
	</tr>
	<tr>
		<td align="left">Peer AIMs</td>
		<td align="left">
		<%List<String> peerList = confmgr.getInstanceList();
		for(String peer : peerList) {
		%>
			<%= peer %> <br/>
		<%} %>
		</td>
	</tr>
</table>
<BR>
<BR>
<BR clear=all>


</div>
<!-- tab div -->

<div class="panel" id="panel2" style="display: none">

<table id="c" class="mytable">
	<caption><span id="desc">Notification Server
	Subscriptions</span></caption>
	<tr>
		<th>Subscription Id</th>
		<th>Enterprise Id</th>
		<th>Subscription Type</th>
		<th>Notification Server Url</th>
		<th>XPath</th>
		<th>Attribute List</th>
	</tr>

	<%
		HashMap nsMap = NotificationManager.getSubscriptions();
		for (Iterator nsItr = nsMap.keySet().iterator(); nsItr.hasNext();) {
			String sid = (String) nsItr.next();
			NotificationServerMessageRequest nsmr =
					(NotificationServerMessageRequest) nsMap.get(sid);
			NotificationServerMessage msg =
					nsmr.getNotificationServerMessage();

			String subType = "&nbsp;";
			if (nsmr.getMessageType() == 1) {
				subType = "Subscription";
			} else {
				subType = "Unsubscription";
			}
	%>
	<tr>
		<td><%=sid%></td>
		<td align="center"><%=nsmr.getEnterpriseId()%></td>
		<td><%=subType%></td>
		<td><%=nsmr.getNotificationServerUrl()%></td>
		<td><%=msg.getSubscribe().getPath()%></td>
		<td><%=msg.getSubscribe().getAttributeList()%></td>
	</tr>

	<%
		}
	%>
	<tr>
		<td colspan="6">Re-subscribe Registrations for Enterprise:
		&nbsp;&nbsp;&nbsp;&nbsp; <select class="select" name="enterpriseId">
			<option value="all" SELECTED>All</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<button type="button" class="button" onclick="doResubscribe()">&nbsp;&nbsp;&nbsp;&nbsp;Go&nbsp;&nbsp;&nbsp;&nbsp;</button>
		</td>
	</tr>
</table>

</div>
<!-- tab div -->

<div class="panel" id="panel3" style="display: none">
<center>
<H1>Current Configuration:</H1>

<IFRAME WIDTH="90%" HEIGHT="500" MARGINWIDTH="1" MARGINHEIGHT="1"
	HSPACE="0" VSPACE="0" FRAMEBORDER="0" SCROLLING="auto"
	BORDERCOLOR="#000000" src="serverConfigDisplay"> </IFRAME></center>
</div>
<!-- tab div --> <%@include file="footer.jsp"%>