<html>
<body>
<%@include file="header.jsp"%>

<H1>Notifying Garbage Collector</H1>
<%
System.gc();
%>
<H1>Done.</H1>
</body>
</html>