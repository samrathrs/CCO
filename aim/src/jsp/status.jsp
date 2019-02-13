<html>
<head>
<link href="aim.css" rel="stylesheet" type="text/css">
<title>AIM - VM Status - (<%=request.getServerName()%>:<%=request.getServerPort()%>)</title>
</head>
<%request.setAttribute("pageType", 12);

            %>
<%@include file="header.jsp"%>
<body onload="stripe();">

<center><IFRAME WIDTH=90% HEIGHT=80% MARGINWIDTH=1 MARGINHEIGHT=1
	HSPACE=0 VSPACE=0 FRAMEBORDER=0 SCROLLING=auto BORDERCOLOR="#000000"
	src="/manager/status" /></center>
</body>
</html>
