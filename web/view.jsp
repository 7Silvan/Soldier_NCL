<%--
  User: gural
  Date: 24.04.12
  Time: 12:29
--%>

<%--this is info block--%>
<div class="container-fluid">

<%--this is soldier's infoblock with url to it's commander page, some other blocke relates on this--%>
<c:if test="${fn:contains(requestScope.viewType, '/viewSoldiers') and fn:contains(requestScope.action,'getSubsOfSoldier')}">
<div class="span-one-third">
    <h3>Current Soldier</h3>
    <jsp:useBean id="queriedSoldier" scope="request" class="ncl.military.entity.Soldier"/>

    <table class="condensed-table borderless">
            <%--<tr>
                <th width="180">ID</th>
                <td>${queriedSoldier.id}</td>
            </tr>--%>
        <tr>
            <th>Name</th>
            <td>${queriedSoldier.name}</td>
        </tr>
        <tr>
            <th>Rank</th>
            <td>${queriedSoldier.rank}</td>
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
                    <c:url var="commanderUrl" value="/viewSoldiers">
                        <c:param name="action" value="getTop"/>
                    </c:url>
                    <td>${queriedSoldier.commander}</td>
                </c:otherwise>
            </c:choose>
        </tr>
        <tr>
            <th>Unit</th>
            <td>${queriedSoldier.unit}</td>
        </tr>
        <tr>
            <th>Birthday</th>
            <td>${queriedSoldier.birthDate}</td>
        </tr>
    </table>
</div>
</c:if>


<%--this is input form for search of soldiers on filters--%>
<c:if test="${fn:contains(requestScope.viewType, '/viewSoldiers') && fn:contains(requestScope.action,'getSearchResults')}">
<h2>Search Tool</h2>

<div class="span-one-third">
    <c:url var="url" value="/viewSoldiers">
        <c:param name="action" value="getSearchResults"/>
    </c:url>
    <div class="span8">
        <form action="${url}" method="post">
            <table>
                <tr>
                    <td><label for="queried_soldier_name">Soldier name: </label></td>
                    <td><label>
                        <input id="queried_soldier_name" name="queried_soldier_name" type="text"
                               value="${requestScope.queried_soldier_name}"/>
                    </label></td>
                </tr>
                <tr>
                    <td><label for="queried_unit_name">Unit: </label></td>
                    <td><label>
                        <input id="queried_unit_name" name="queried_unit_name" type="text"
                               value="${requestScope.queried_unit_name}"/>
                    </label></td>
                </tr>
                <tr>
                    <td><label for="queried_location_name">Location: </label></td>
                    <td><label>
                        <input id="queried_location_name" name="queried_location_name" type="text"
                               value="${requestScope.queried_location_name}"/>
                    </label></td>
                </tr>
                <tr>
                    <td><label for="queried_commander_name">Commander name: </label></td>
                    <td><label>
                        <input id="queried_commander_name" name="queried_commander_name" type="text"
                               value="${requestScope.queried_commander_name}"/>
                    </label></td>
                </tr>
                <tr>
                    <td><label for="queried_soldier_rank">Rank: </label></td>
                    <td><label>
                        <input id="queried_soldier_rank" name="queried_soldier_rank" type="text"
                               value="${requestScope.queried_soldier_rank}"/>
                    </label></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" class="btn primary pull-right" value="Find"/></td>
                </tr>
            </table>

                <%--<jsp:include page="jspf/soldierForm.jspf" flush="true"/>--%>
        </form>
    </div>
</div>
</c:if>


<%--this is for viewing items--%>
<div class="item_list">
<c:if test="${not empty commanderUrl}">
    <div style="margin: 4px 0 8px;"><a class="btn primary" href="${commanderUrl}">Level up!</a></div>
</c:if>
<div style="margin-top:15px; ">
<%--view of soldiers--%>
<c:if test="${fn:contains(requestScope.viewType, '/viewSoldiers')}">
    <h3> View of Soldiers</h3>
    <table id="resultTable" class="condensed-table bordered-table zebra-striped">
        <thead>
        <tr>
                <%--<th>ID</th>--%>
            <th>NAME</th>
            <th>RANK</th>
                <%--<th>COMMANDER</th>--%>
            <th>UNIT</th>
            <th>BIRTHDATE</th>
            <th>ACTIONS</th>
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
                <td>
                    <c:url var="url" value="/viewSoldiers">
                        <c:param name="action" value="getSearchResults"/>
                        <c:param name="queried_soldier_rank" value="${soldier.rank}"/>
                    </c:url>
                    <a href="${url}">${soldier.rank}</a>
                </td>
                    <%--<td>${soldier.commander}</td>--%>
                <td>
                    <c:url var="url" value="/viewSoldiers">
                        <c:param name="action" value="getSearchResults"/>
                        <c:param name="queried_unit_name" value="${soldier.unit}"/>
                    </c:url>
                    <a href="${url}">${soldier.unit}</a>
                </td>
                <td>
                        ${soldier.birthDate}
                </td>
                <td>
                    <c:choose>

                        <c:when test="${fn:contains(requestScope.action, 'moveSoldier' )}">

                            <c:if test="${requestScope.soldierIdMatch ne soldier.id}">
                                <c:url var="moveUnderThisUrl" value="/viewSoldiers">
                                    <c:param name="soldierIdMatch" value="${requestScope.soldierIdMatch}"/>
                                    <c:param name="queried_soldier_commander" value="${soldier.id}"/>
                                    <c:param name="action" value="${FormConst.MOVE_UNDER_THIS_SOLDIER}"/>
                                </c:url>
                                <a class="btn primary" href="${moveUnderThisUrl}">Select</a>
                            </c:if>
                        </c:when>
                        <c:otherwise>

                            <c:url var="editUrl" value="/viewSoldiers">
                                <c:param name="soldierIdMatch" value="${soldier.id}"/>
                                <c:param name="action" value="${FormConst.EDIT}"/>
                            </c:url>
                            <a class="btn primary" href="${editUrl}">Edit</a>

                            <c:url var="deleteUrl" value="/viewSoldiers">
                                <c:param name="soldierIdMatch" value="${soldier.id}"/>
                                <c:param name="action" value="${FormConst.DELETE_SOLDIER}"/>
                            </c:url>
                            <a class="btn primary" href="${deleteUrl}">Delete</a>

                            <c:url var="moveUrl" value="/viewSoldiers">
                                <c:param name="soldierIdMatch" value="${soldier.id}"/>
                                <c:param name="action" value="${FormConst.MOVE_SOLDIER}"/>
                            </c:url>
                            <a class="btn primary" href="${moveUrl}">Move</a>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<%--view of locations--%>
<c:if test="${fn:contains(requestScope.viewType, '/viewLocations' )}">
    <h3> View of Locations</h3>
    <table id="resultTable" class="condensed-table bordered-table zebra-striped">
        <thead>
        <tr>
                <%--<th>ID</th>--%>
            <th>NAME</th>
            <th>REGION</th>
            <th>CITY</th>
            <th>ACTION</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach var="location" items="${requestScope.listOfLocations}">
            <tr>
                    <%--<td>${location.id}</td>--%>
                <td>
                    <c:url var="url" value="/viewUnits">
                        <c:param name="action" value="getUnitsOfLocations"/>
                        <c:param name="locationIdMatch" value="${location.id}"/>
                    </c:url>
                    <a href="${url}">${location.name}</a>
                </td>
                <td>${location.region}</td>
                <td>${location.city}</td>
                <td>
                    <c:url var="editUrl" value="/viewLocations">
                        <c:param name="locationIdMatch" value="${location.id}"/>
                        <c:param name="action" value="edit"/>
                    </c:url>
                    <a class="btn primary" href="${editUrl}">Edit</a>
                </td>
            </tr>
        </c:forEach>

        </tbody>
    </table>

</c:if>
<%--view of units--%>
<c:if test="${fn:contains(requestScope.viewType, '/viewUnits' )}">
    <h3> View of Units</h3>
    <table id="resultTable" class="condensed-table bordered-table zebra-striped">
        <thead>
        <tr>
                <%--<th>ID</th>--%>
            <th>NAME</th>
            <th>HEAD</th>
            <th>LOCATION</th>
            <th>ACTION</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach var="unit" items="${requestScope.listOfUnits}">
            <tr>
                    <%--<td>${unit.id}</td>--%>
                <td>
                    <c:url var="url" value="/viewSoldiers">
                        <c:param name="action" value="getSoldiersOfUnit"/>
                        <c:param name="unitIdMatch" value="${unit.id}"/>
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
                <td>
                    <c:url var="url" value="/viewSoldiers">
                        <c:param name="action" value="getSearchResults"/>
                        <c:param name="queried_location_name" value="${unit.location}"/>
                    </c:url>
                    <a href="${url}">${unit.location}</a>
                </td>
                <td>
                    <c:url var="editUrl" value="/viewUnits">
                        <c:param name="unitIdMatch" value="${unit.id}"/>
                        <c:param name="action" value="${FormConst.EDIT}"/>
                    </c:url>
                    <a class="btn primary" href="${editUrl}">Edit</a>
                </td>
            </tr>
        </c:forEach>

        </tbody>
    </table>
</c:if>

<small>* To sort values in the table press the headers.</small>

<%--<hr/>
<h2>testing</h2>
<c:forEach items="${requestScope}" var="attr">
   ${attr.key}=${attr.value}<br>
   &lt;%&ndash; <c:if test="${attr.key eq 'listOfSoldiers' }">
        <c:forEach items="${requestScope.listOfSoldiers}" var="soldier">
            <hr />
            Name : ${soldier.name}
            Unit : ${soldier.unit}
            <hr />
        </c:forEach>
   </c:if>&ndash;%&gt;
</c:forEach>

<c:out value="${requestScope.viewType}"/>
<c:out value="${pageContext.request.servletPath}"/>--%>

</div>
</div>

