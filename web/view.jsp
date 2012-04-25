<%--
  User: gural
  Date: 24.04.12
  Time: 12:29
--%>
<h1>[View.jsp]</h1>


<h2>[Some info about viewing subject]</h2>
<br>

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


<div>
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

                        <c:url var="url" value="/viewSoldiers">
                            <c:param name="action" value="getSubsOfSoldier"/>
                            <c:param name="queriedSoldierId"
                                     value="${requestScope.hierarchyList[fn:length(requestScope.hierarchyList)-2].id}"/>
                        </c:url>
                        <td>
                            <a href="${url}">${requestScope.hierarchyList[fn:length(requestScope.hierarchyList)-2].name}</a>
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
    <c:if test="${fn:contains(requestScope.viewType, '/viewSoldiers') && fn:contains(requestScope.actionPerformed,'getSearchResults')}">
        <c:url var="url" value="/viewSoldiers">
            <c:param name="action" value="getSearchResults"/>
        </c:url>
        <div class="span8">
            <form action="${url}" method="post">
                    <%--@declare id="queriedsoldiername"--%><%--@declare id="queriedsoldierunit"--%>
                    <%--@declare id="queriedsoldierlocation"--%><%--@declare id="queriedsoldiercommandername"--%><%--@declare id="queriedsoldierrank"--%>
                <table>
                    <tr>
                        <td><label for="queriedSoldierName">Soldier name: </label></td>
                        <td><label>
                            <input name="queriedSoldierName" type="text"/>
                        </label></td>
                    </tr>
                    <tr>
                        <td><label for="queriedSoldierUnit">Unit: </label></td>
                        <td><label>
                            <input name="queriedSoldierUnit" type="text"/>
                        </label></td>
                    </tr>
                    <tr>
                        <td><label for="queriedSoldierLocation">Location: </label></td>
                        <td><label>
                            <input name="queriedSoldierLocation" type="text"/>
                        </label></td>
                    </tr>
                    <tr>
                        <td><label for="queriedSoldierCommanderName">Commander name: </label></td>
                        <td><label>
                            <input name="queriedSoldierCommanderName" type="text"/>
                        </label></td>
                    </tr>
                    <tr>
                        <td><label for="queriedSoldierRank">Rank: </label></td>
                        <td><label>
                            <input name="queriedSoldierRank" type="text"/>
                        </label></td>
                    </tr>
                    <tr>
                        <td><input type="submit" class="btn primary" value="Find" align="right"/></td>
                    </tr>
                </table>
            </form>
        </div>
    </c:if>
</div>
<!-- repair margin from rigth side of container for table -->
<div class="item_list">
    <h4>[List of values below]</h4>

    <div style="margin: 4px 0 8px;"><a class="btn primary" href="#">View Smth</a></div>
    <div style="margin-top:15px; ">

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

        <!-- <td><button class="btn small">[button for some action]</button></td> -->
        <hr/>
        <h2>testing</h2>
    </div>
</div>

