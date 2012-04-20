<%@ page import="ncl.military.dao.modules.OracleModule" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- <jsp:useBean id="dao" type="ncl.military.dao.modules.OracleModule" scope="request" /> --%>
<html>
<head>
    <title>Hello world</title>
</head>
<body>
<h3 style="color: green; text-align: center;"><c:out value="Hello from JSTL"/></h3>
<% OracleModule dao = new OracleModule();
    dao.test(); %>
<h2>Results</h2>
Message: <%= dao.toString() %>
</body>
</html>