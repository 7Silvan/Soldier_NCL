<%--
  Created by IntelliJ IDEA.
  User: gural
  Date: 19.04.12
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html lang="en-US">
<head>
    <meta charset="utf-8"/>
    <title>Soldiers</title>
    <link rel="stylesheet" href="assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="assets/css/main.css"/>
    <link rel="stylesheet" href="assets/css/font-awesome.css"/>
    <script type="text/javascript" src="assets/js/jquery.js"></script>
    <script src="assets/js/bootstrap-modal.js" type="text/javascript"></script>
    <!-- <script src="assets/js/modal_focus.js" type="text/javascript"></script> -->
    <!-- <link rel="icon" href="assets/img/favicon.ico" /> -->
</head>
<body>
<a href="#page-content" class="hidden">Skip Navigation</a>

<div class="topbar">
    <div class="topbar-inner">
        <div class="container-fluid"><a target="_new" href="#" class="brand"> Soldiers <span
                style="background-color:#387690;padding-top:100px" class="label warning">Alpha</span> </a>
            <ul class="nav secondary-nav">
                <li><a href="#">[reference 1]</a></li>
                <li><a href="#">[reference 2]</a></li>
                <li><a href="#">[reference 3]</a></li>
            </ul>
        </div>
    </div>
</div>
<div class="container-fluid">
    <div class="sidebar page-sidebar" id="page-sidebar">
        <ul style="list-style-type:none;margin-left:0;">
            <li><a href="#">
                <p class="navbar_item navbar_home">Home </p>
            </a></li>
            <li><a href="#">
                <p class="navbar_item navbar_wiki">[Some reference]</p>
            </a></li>
        </ul>
    </div>
    <div class="content page-content" id="page-content">
        <div id="banner-top">
            <div id="banner-path">
                [path here]
            </div>
            <!--  <div id="banner-info" >
             <div class="my-name">Roman Gural</div>
             <div >Spring 2012</div>
           </div> -->
        </div>

        <h2>[Some info about viewing subject]</h2>
        <br>

        <div>
            <table class="condensed-table borderless">
                <tr>
                    <th width="180">[info att name 1]</th>
                    <td>[info att value 1]</td>
                </tr>
                <tr>
                    <th>[info att name 2]</th>
                    <td>[info att value 2]</td>
                </tr>
            </table>
        </div>
        <!-- repair margin from rigth side of container for table -->
        <div class="item_list">
            <h4>[List of values below]</h4>

            <div style="margin: 4px 0 8px;"><a class="btn primary" href="#">View Smth</a></div>
            <div style="margin-top:15px; ">
                <div>
                    <table class="condensed-table bordered-table zebra-striped">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>NAME</th>
                            <th>RANK</th>
                            <th>COMMANDER</th>
                            <th>UNIT</th>
                            <th>BIRTHDATE</th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach var="soldier" items="${requestScope.listOfSoldiers}">
                            <tr>
                                <td>${soldier.id}</td>
                                <td><a href="/viewSoldiers?action=getSubsOf&queriedSoldierId=${soldier.id}">${soldier.name}</a></td>
                                <td>${soldier.rank}</td>
                                <td>${soldier.commander}</td>
                                <td>${soldier.unit}</td>
                                <td>${soldier.birthDate}</td>
                            </tr>
                        </c:forEach>

                        </tbody>
                    </table>

                    <!-- <td><button class="btn small">[button for some action]</button></td> -->
                    <h2>data from soldier:</h2>

                    <%-- Use Database > DB Report in the Palette --%>
                    <sql:query var="result" dataSource="jdbc/soldier">
                        SELECT * FROM soldier
                    </sql:query>

                    <table border="1">
                        <!-- column headers -->
                        <tr>
                            <c:forEach var="columnName" items="${result.columnNames}">
                                <th><c:out value="${columnName}"/></th>
                            </c:forEach>
                        </tr>
                        <!-- column data -->
                        <c:forEach var="row" items="${result.rowsByIndex}">
                            <tr>
                                <c:forEach var="column" items="${row}">
                                    <td><c:out value="${column}"/></td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

