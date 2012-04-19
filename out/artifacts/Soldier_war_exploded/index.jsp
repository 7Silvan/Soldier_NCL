<%@ page import="ncl.military.dao.DAO" %>
<%@ page import="ncl.military.dao.modules.oracleModule" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Hello world</title>
      <link rel="stylesheet" type="text/css" href="styles.css" />
  </head>
  <body>
    <h3 class="message"><c:out value="Hello from JSTL"/></h3>
    <% DAO dao = new oracleModule();
        dao.init(); %>
    <h2>Results</h2>
    Message: <%= dao.getMessage() %>
  </body>
</html>