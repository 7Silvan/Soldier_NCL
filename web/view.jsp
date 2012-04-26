<%--
  User: gural
  Date: 24.04.12
  Time: 12:29
--%>
<h1>[View.jsp]</h1>

<%--this is info block--%>
<div class="container-fluid">
    <div class="span-one-third">
        <%--this is soldier's infoblock with url to it's commander page, some other blocke relates on this--%>
        <c:if test="${fn:contains(requestScope.viewType, '/viewSoldiers') && fn:contains(requestScope.actionPerformed,'getSubsOfSoldier')}">
            <h3>Current Soldier</h3>
            <jsp:useBean id="currentSoldier" scope="request" class="ncl.military.entity.Soldier"/>
            <%-- TODO check existance of currentSoldier--%>
            <table class="condensed-table borderless">
                <tr>
                    <th width="180">ID</th>
                    <td>${currentSoldier.id}</td>
                </tr>
                <tr>
                    <th>Name</th>
                    <td>${currentSoldier.name}</td>
                </tr>
                <tr>
                    <th>Rank</th>
                    <td>${currentSoldier.rank}</td>
                </tr>
                <tr>
                    <th>Commander</th>
                    <c:choose>
                        <c:when test="${requestScope.hierarchyList ne null && fn:length(requestScope.hierarchyList) > 1}">

                            <c:url var="commanderUrl" value="/viewSoldiers">
                                <c:param name="action" value="getSubsOfSoldier"/>
                                <c:param name="queriedSoldierId"
                                         value="${requestScope.hierarchyList[fn:length(requestScope.hierarchyList)-2].id}"/>
                            </c:url>
                            <td>
                                <a href="${commanderUrl}">${requestScope.hierarchyList[fn:length(requestScope.hierarchyList)-2].name}</a>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td>${currentSoldier.commander}</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <th>Unit</th>
                    <td>${currentSoldier.unit}</td>
                </tr>
                <tr>
                    <th>Birthday</th>
                    <td>${currentSoldier.birthDate}</td>
                </tr>
            </table>
        </c:if>
    </div>
    <div class="span-one-third">
        <%--this is input form for search of soldiers on filters--%>
        <c:if test="${fn:contains(requestScope.viewType, '/viewSoldiers') && fn:contains(requestScope.actionPerformed,'getSearchResults')}">
            <c:url var="url" value="/viewSoldiers">
                <c:param name="action" value="getSearchResults"/>
            </c:url>
            <div class="span8">
                <form action="${url}" method="post">
                    <table>
                        <tr>
                            <td><label for="queried_soldier_name">Soldier name: </label></td>
                            <td><label>
                                <input id="queried_soldier_name" name="queried_soldier_name" type="text"/>
                            </label></td>
                        </tr>
                        <tr>
                            <td><label for="queried_unit_name">Unit: </label></td>
                            <td><label>
                                <input id="queried_unit_name" name="queried_unit_name" type="text"/>
                            </label></td>
                        </tr>
                        <tr>
                            <td><label for="queried_location_name">Location: </label></td>
                            <td><label>
                                <input id="queried_location_name" name="queried_location_name" type="text"/>
                            </label></td>
                        </tr>
                        <tr>
                            <td><label for="queried_commander_name">Commander name: </label></td>
                            <td><label>
                                <input id="queried_commander_name" name="queried_commander_name" type="text"/>
                            </label></td>
                        </tr>
                        <tr>
                            <td><label for="queried_soldier_rank">Rank: </label></td>
                            <td><label>
                                <input id="queried_soldier_rank" name="queried_soldier_rank" type="text"/>
                            </label></td>
                        </tr>
                        <tr>
                            <td colspan="2"><input type="submit" class="btn primary pull-right" value="Find"/></td>
                        </tr>
                    </table>

                        <%--<jsp:include page="jspf/soldierForm.jspf" flush="true"/>--%>
                </form>
            </div>
        </c:if>
    </div>
</div>
<%--this is for viewing items--%>
<div class="item_list">
    <h4>[List of values below]</h4>
    <c:if test="${not empty commanderUrl}">
        <div style="margin: 4px 0 8px;"><a class="btn primary" href="${commanderUrl}">Level up!</a></div>
    </c:if>
    <div style="margin-top:15px; ">
        <%--view of soldiers--%>
        <c:if test="${fn:contains(requestScope.viewType, '/viewSoldiers')}">
            <table class="condensed-table bordered-table zebra-striped">
                <thead>
                <tr>
                        <%--<th>ID</th>--%>
                    <th>NAME</th>
                    <th>RANK</th>
                        <%--<th>COMMANDER</th>--%>
                    <th>UNIT</th>
                    <th>BIRTHDATE</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="soldier" items="${requestScope.listOfSoldiers}">
                    <tr>
                            <%--<td>${soldier.id}</td>--%>
                        <td>
                            <c:url var="url" value="/viewSoldiers">
                                <c:param name="action" value="getSubsOfSoldier"/>
                                <c:param name="queriedSoldierId" value="${soldier.id}"/>
                            </c:url>
                            <a href="${url}">${soldier.name}</a>
                        </td>
                        <td>${soldier.rank}</td>
                            <%--<td>${soldier.commander}</td>--%>
                        <td>${soldier.unit}</td>
                        <td>${soldier.birthDate}</td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>

        </c:if>
        <%--view of locations--%>
        <c:if test="${fn:contains(requestScope.viewType, '/viewLocations' )}">
            <table class="condensed-table bordered-table zebra-striped">
                <thead>
                <tr>
                        <%--<th>ID</th>--%>
                    <th>NAME</th>
                    <th>REGION</th>
                    <th>CITY</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="location" items="${requestScope.listOfLocations}">
                    <tr>
                            <%--<td>${location.id}</td>--%>
                        <td>
                            <c:url var="url" value="/viewUnits">
                                <c:param name="action" value="getUnitsOfLocations"/>
                                <c:param name="queriedLocationId" value="${location.id}"/>
                            </c:url>
                            <a href="${url}">${location.name}</a>
                        </td>
                        <td>${location.region}</td>
                        <td>${location.city}</td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>

        </c:if>
        <%--view of units--%>
        <c:if test="${fn:contains(requestScope.viewType, '/viewUnits' )}">
            <table class="condensed-table bordered-table zebra-striped">
                <thead>
                <tr>
                        <%--<th>ID</th>--%>
                    <th>NAME</th>
                    <th>HEAD</th>
                    <th>LOCATION</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="unit" items="${requestScope.listOfUnits}">
                    <tr>
                            <%--<td>${unit.id}</td>--%>
                        <td>
                            <c:url var="url" value="/viewSoldiers">
                                <c:param name="action" value="getSoldiersOfUnit"/>
                                <c:param name="queriedUnitId" value="${unit.id}"/>
                            </c:url>
                            <a href="${url}">${unit.name}</a>
                        </td>

                        <td>
                            <c:url var="url" value="/viewSoldiers">
                                <c:param name="action" value="getSubsOfSoldier"/>
                                <c:param name="queriedSoldierId" value="${unit.headId}"/>
                            </c:url>
                            <a href="${url}">${unit.head}</a>
                        </td>
                        <td>${unit.location}</td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </c:if>

        <hr/>
        <h2>testing</h2>
        <c:forEach items="${requestScope}" var="attr">
            ${attr.key}=${attr.value}<br>
            <%-- <c:if test="${attr.key eq 'listOfSoldiers' }">
                 <c:forEach items="${requestScope.listOfSoldiers}" var="soldier">
                     <hr />
                     Name : ${soldier.name}
                     Unit : ${soldier.unit}
                     <hr />
                 </c:forEach>
            </c:if>--%>
        </c:forEach>

        <c:out value="${requestScope.viewType}"/>
        <c:out value="${pageContext.request.servletPath}"/>
    </div>
</div>

