<%@ page import="com.transerainc.tam.ns.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.concurrent.*"%>

<html>
<head>
<link href="aim.css" rel="stylesheet" type="text/css">
<title>AIM - Notification Server Resubscribe - (<%=request.getServerName()%>:<%=request.getServerPort()%>)</title>
</head>
<%@include file="header.jsp"%>
<body>
<form>


<%String eId = request.getParameter("enterpriseId");

            if (eId == null || eId.equals("all")) {
                NotificationManager.resubscribeAll();
            } else {
                NotificationManager.resubscribeAll(eId);
            }

            response.sendRedirect("aim.jsp");

        %>

<%@include file="footer.jsp"%>