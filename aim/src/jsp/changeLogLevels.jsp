<%@ page import="com.transerainc.tam.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.logging.*"%>
<%@ page import="java.io.*"%>


<html>
<head>
<link href="aim.css" rel="stylesheet" type="text/css">
<title>AIM - Log Levels - (<%=request.getServerName()%>:<%=request.getServerPort()%>)</title>
</head>
<%
request.setAttribute("pageType", 11);
%><%@include file="header.jsp"%>
<body onload="stripe();">

<form name="logForm" action="changeLogLevels.jsp" method="POST">
<%
	LogManager lmgr = LogManager.getLogManager();
	Map reqMap = request.getParameterMap();
	for (Iterator itr = reqMap.keySet().iterator(); itr.hasNext();) {
		String key = (String) itr.next();

		if (key.startsWith("logLevels")) {
			System.out.println("Found " + key);
			String value = request.getParameter(key);

			String lgrName = key.substring(9);
			Logger lgr = lmgr.getLogger(lgrName);
			if (!value.equals("NONE")) {
		Level lvl = Level.parse(value);

		System.out.println("Setting log level for " + lgrName
			+ " to " + lvl + ", lgr --> " + lgr);

		lgr.setLevel(lvl);
			} else {
		lgr.setLevel(null);
			}
		}
	}
%>

<table id="a" class="mytable">
	<tr>
		<th>Logger Name</th>
		<th>Log Level</th>
	</tr>
	<tr>
		<td colspan="2" align="center"><input class="button"
			type="submit" name="submit" value="Change Levels"></td>
	</tr>



	<%
				String[] nonNullLogLevels =
				{ "NONE", "ALL", "FINEST", "FINER", "FINE", "CONFIG",
				"INFO", "WARNING", "SEVERE" };

		Enumeration en = lmgr.getLoggerNames();
		List enList = Collections.list(en);
		Collections.sort(enList);

		for (int lgrI = 0; lgrI < enList.size(); lgrI++) {
			String lgrName = (String) enList.get(lgrI);

			Logger lgr = lmgr.getLogger(lgrName);

			String dispLgrName = lgrName;
			if (dispLgrName == null || dispLgrName.length() < 2) {
				dispLgrName = "GLOBAL LOGGING LEVEL";
			}
	%>

	<tr>
		<td align="left"><%=dispLgrName%></td>
		<td align="center">
		<%
				Level lvl = lgr.getLevel();
				if (lvl == null) {
					out.println("<select class=\"select\" name=\"logLevels"
				+ lgr.getName() + "\">");
					out.println("<option selected>NONE</option>");
					out.println("<option>ALL</option>");
					out.println("<option>FINEST</option>");
					out.println("<option>FINER</option>");
					out.println("<option>FINE</option>");
					out.println("<option>CONFIG</option>");
					out.println("<option>INFO</option>");
					out.println("<option>WARNING</option>");
					out.println("<option>SEVERE</option>");
					out.println("</select>");
				} else {
					out.println("<select class=\"select\" name=\"logLevels"
				+ lgr.getName() + "\">");

					String lvlName = lvl.getName();

					for (int i = 0; i < nonNullLogLevels.length; i++) {
				out.print("<option");

				String nnllStr = nonNullLogLevels[i];
				if (nnllStr.equals(lvlName)) {
					out.print(" selected");
				}

				out.print(">");
				out.print(nnllStr);
				out.println("</option>");
					}

					out.println("</select>");
				}
		%>
		</td>
	</tr>

	<%
	}
	%>

	<tr>
		<td colspan="2" align="center"><input class="button"
			type="submit" name="submit" value="Change Levels"></td>
	</tr>

</table>

<%@include file="footer.jsp"%>