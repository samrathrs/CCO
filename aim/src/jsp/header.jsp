<%@page import="com.transerainc.aim.conf.ConfigurationManager"%>
<%@page
	import="com.transerainc.aim.system.AgentInformationManagerSystem"%>
<%@page import="com.transerainc.aim.conf.xsd.AgentInformationManager"%>
<script>
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
<script type="text/javascript" src="dhtml-suite-for-applications-without-comments.js"></script>
<link rel="stylesheet" href="css/menu-bar.css" media="screen"
	type="text/css">
<link rel="stylesheet" href="css/menu-item.css" media="screen"
	type="text/css">
<%
			Class headerC =
			com.transerainc.aim.system.AgentInformationManagerSystem.class;
	Package headerPack = headerC.getPackage();

	ConfigurationManager confmgr =
			(ConfigurationManager) AgentInformationManagerSystem
			.findBean("configManager");
	AgentInformationManager aim = confmgr.getAgentInformationManager();
%>


<div id="menuContent">
<ul id="menuModel" style="display: none">
	<li id="50000" itemIcon="images/system.gif"><a href="#"
		title="System Menu">System</a>
	<ul>
		<li id="500001" itemIcon="/aim/images/home.gif"><a href="aim.jsp">Home</a></li>
		<li id="500005" itemType="separator"></li>
		<li id="500003" itemIcon="images/config.gif"><a
			href="/aim/configListener">Reload Config</a></li>
		<li id="500004" itemIcon="images/reload.gif"><a
			href="/manager/reload?path=/aim">Reload App</a></li>
	</ul>
	</li>
	<li id="50001" itemIcon="images/edit.gif"><a href="#"
		title="Edit System Variable">Edit</a>
	<ul>
		<li id="500011" itemIcon="images/disk.gif"><a
			href="changeLogLevels.jsp">Log Levels</a></li>
	</ul>
	</li>
	<li id="50002" itemIcon="images/view.gif"><a href="#"
		title="View System Information">View</a>
	<ul>
		<li id="500021" itemIcon="/aim/images/agent.gif"><a
			href="viewAgents.jsp">Agents</a></li>
		<li id="500033" itemIcon="/aim/images/members.gif"><a
			href="viewAgentTeamMappings.jsp">Agent Team Mappings</a></li>
		<li id="500022" itemIcon="/aim/images/members.gif"><a
			href="viewTeams.jsp">Teams</a></li>
        <li id="500034" itemIcon="/aim/images/site.gif"><a
            href="viewSites.jsp">Sites</a></li>
        <li id="500035" itemIcon="/aim/images/members.gif"><a
            href="viewVirtualTeams.jsp">Virtual Teams</a></li>
		<li id="500023" itemIcon="/aim/images/star-red.gif"><a
			href="viewTenants.jsp">Tenants</a></li>
		<li id="500024" itemIcon="/aim/images/profile.gif"><a
			href="viewAgentProfiles.jsp">Agent Profiles</a></li>
		<li id="500025" itemIcon="/aim/images/properties.gif"><a
			href="viewAdhocDialProperties.jsp">Adhoc Dial Properties</a></li>
		<li id="500026" itemIcon="/aim/images/password.gif"><a
			href="viewPasswordPolicies.jsp">Password Policies</a></li>
		<li id="500027" itemIcon="/aim/images/lists.gif"><a
			href="viewSpeedDialLists.jsp">Speed Dial Lists</a></li>
		<li id="500028" itemIcon="/aim/images/agent.gif"><a
			href="viewAuthorizationFailures.jsp">Authorization Failures</a></li>
        <li id="500037" itemIcon="/aim/images/profile.gif"><a
            href="viewMediaProfiles.jsp">Media Profiles</a></li>
        <li id="500038" itemIcon="/aim/images/profile.gif"><a
            href="viewSkillProfiles.jsp">Skill Profiles</a></li>
		<li id="500029" itemType="separator"></li>
		<li id="500030" itemIcon="/aim/images/idle.gif"><a
			href="viewIdleCodes.jsp">Idle Codes</a></li>
		<li id="500031" itemIcon="/aim/images/idle.gif"><a
			href="viewWrapups.jsp">Wrapup Codes</a></li>
		<li id="500040" itemType="separator"></li>
		<li id="500041" itemIcon="/aim/images/vendor.gif"><a
			href="viewVendors.jsp">Vendors</a></li>
		<li id="500042" itemIcon="/aim/images/third_party_profile.gif"><a
			href="viewThirdPartyProfiles.jsp">Third Party Profiles</a></li>
		<li id="500043" itemIcon="/aim/images/agent.gif"><a
			href="viewActiveSupervisors.jsp">Active Supervisors</a></li>
		<li id="500032" itemType="separator"></li>
		<li id="500036" itemIcon="/aim/images/status.gif"><a
			href="status.jsp">Status</a></li>
	</ul>
	</li>
</ul>
</div>


<div id="bg-cha">
<div id="topright">Instance Name - <%=confmgr.getInstanceName()%><br>
Up since - <%=new java.sql.Timestamp(
						AgentInformationManagerSystem.UP_TIMESTAMP)%><BR>
Version - <%=headerPack.getImplementationVersion()%><BR>
Current Timestamp - <%=System.currentTimeMillis()%>(<%=new java.sql.Timestamp(System.currentTimeMillis())%>)<br>
</div>
</div>

<div id="menuDiv"></div>
<script type="text/javascript">
	var menuModel = new DHTMLSuite.menuModel();
	menuModel.addItemsFromMarkup('menuModel');
	//menuModel.setMainMenuGroupWidth(00);	
	menuModel.init();
	
	var menuBar = new DHTMLSuite.menuBar();
	menuBar.addMenuItems(menuModel);
	menuBar.setTarget('menuDiv');
	
	menuBar.init();
</script>
<br clear="all">


<div id="content">