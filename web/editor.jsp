<%--
  User: Silvan
  Date: 27.04.12
  Time: 5:47
--%>
<c:if test="${fn:contains(requestScope.success, 'true')}">
    <c:choose>
        <c:when test="${fn:contains(requestScope.action, 'addSoldier')}">
            <c:url var="redirectUrl" value="/viewSoldiers">
                <c:choose>
                    <c:when test="${requestScope.commanderIdMatch ne ''}">
                        <c:param name="action" value="getSubsOfSoldier"/>
                        <c:param name="queriedSoldierId" value="${requestScope.commanderIdMatch}"/>
                    </c:when>
                    <c:otherwise>
                        <c:param name="action" value="getTop"/>
                    </c:otherwise>
                </c:choose>
            </c:url>

            <script type="text/javascript">
                setTimeout('location.replace("${redirectUrl}")', 1000);
            </script>
            <h1>Added!</h1>
        </c:when>
        <c:when test="${fn:contains(requestScope.viewType, '/viewSoldiers') and fn:contains(requestScope.action, 'edit')}">
            <c:url var="redirectUrl" value="/viewSoldiers">
                <c:param name="action" value="getSubsOfSoldier"/>
                <c:param name="queriedSoldierId" value="${requestScope.queriedSoldier.id}"/>
            </c:url>
            <script type="text/javascript">
                setTimeout('location.replace("${redirectUrl}")', 1000);
            </script>
            <h1>Saved!</h1>
        </c:when>
        <c:when test="${fn:contains(requestScope.viewType, '/viewUnits')}">
            <c:url var="redirectUrl" value="/viewUnits"/>
            <script type="text/javascript">
                setTimeout('location.replace("${redirectUrl}")', 1000);
            </script>
            <h1>Saved!</h1>
        </c:when>
        <c:when test="${requestScope.viewType eq '/viewLocations'}">
            <c:url var="redirectUrl" value="/viewLocations"/>
            <script type="text/javascript">
                setTimeout('location.replace("${redirectUrl}")', 1000);
            </script>
            <h1>Saved!</h1>
        </c:when>
        <c:otherwise>
            <script type="text/javascript">
                setTimeout('location.replace("/viewSoldiers?action=getTop")', 1000);
            </script>
        </c:otherwise>

    </c:choose>


</c:if>


<c:if test="${fn:contains(requestScope.userPath, '/viewSoldiers') and (fn:contains(requestScope.action, FormConst.ADD_SOLDIER) or fn:contains(requestScope.action, FormConst.EDIT))}">
    <%--<jsp:useBean id="queriedSoldier" scope="request" class="ncl.military.entity.Soldier"/>--%>
    <c:choose>
        <c:when test="${requestScope.action eq FormConst.EDIT}">
            <form id="checkoutForm" class="cmxform" action="/viewSoldiers" method="post">
        </c:when>
        <c:otherwise>
            <form id="checkoutForm" name="addSoldierForm" onsubmit="return validateForm();" class="cmxform" action="/viewSoldiers" method="post">
        </c:otherwise>
    </c:choose>
    <table>
        <input name="soldierIdMatch" value="${pageContext.request.parameterMap.soldierIdMatch[0]}"
               type="hidden"/>
        <% log.debug("Checking parameter soldierIdMatch : " + pageContext.getRequest().getParameter("soldierIdMatch")); %>

        <c:choose>
            <c:when test="${requestScope.action eq FormConst.EDIT}">
                <input name="action" value="${FormConst.EDIT}" type="hidden"/>
            </c:when>
            <c:otherwise>
                <input name="action" value="${FormConst.ADD_SOLDIER}" type="hidden"/>
            </c:otherwise>
        </c:choose>

        <tr>
            <td><label for="queried_soldier_name">Soldier name: </label></td>
            <td><label>
                <input id="queried_soldier_name" name="queried_soldier_name" type="text" class="requiered"
                       value="${requestScope.queriedSoldier.name}"/>
            </label></td>
        </tr>
        <tr>
            <td><label for="queried_unit_name">Unit: </label></td>
            <td><label>
                    <%--<input id="queried_unit_name" name="queried_unit_name" type="text" value="${queriedSoldier.unit}"/>--%>
                <select id="queried_unit_name" name="queried_unit_name" class="required">
                    <c:forEach var="unit" items="${requestScope.listOfUnits}">
                        <c:choose>
                            <c:when test="${requestScope.queriedSoldier.unit eq unit.name}">
                                <option value="${unit.id}" selected="">${unit.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${unit.id}">${unit.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </label></td>
        </tr>
        <tr>
            <td><label for="queried_soldier_commander">Commander name: </label></td>
            <td><label>
                <select id="queried_soldier_commander" name="queried_soldier_commander" class="required">
                    <option value="">--Top of commanding--</option>
                    <c:forEach var="soldier" items="${requestScope.listOfSoldiers}">
                        <c:choose>
                            <c:when test="${requestScope.queriedSoldier.commander eq soldier.id or
                                            requestScope.queriedCommanderId eq soldier.id}">
                                <option value="${soldier.id}" selected="">${soldier.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${soldier.id}">${soldier.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </label></td>
        </tr>

        <tr>
            <td><label for="queried_soldier_rank">Rank: </label></td>
            <td><label>
                <input id="queried_soldier_rank" name="queried_soldier_rank" type="text" class="required"
                       value="${requestScope.queriedSoldier.rank}"/>
            </label></td>
        </tr>
        <tr>
            <td><label>Date: </label></td>
            <td><label>
                <input id="date" name="queried_soldier_birthdate" type="text" class="required" autocomplete="off"
                       value="${requestScope.queriedSoldier.birthDate}"/>
            </label>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <c:choose>
                    <c:when test="${requestScope.action eq FormConst.EDIT}">
                        <input type="submit" class="btn primary pull-right" value="Edit"/>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" class="btn primary pull-right" value="Add Soldier"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</c:if>
<c:if test="${fn:contains(requestScope.userPath, '/viewUnits') and fn:contains(requestScope.action, FormConst.EDIT)}">
    <%--<jsp:useBean id="queriedUnit" scope="request" class="ncl.military.entity.Unit"/>--%>
    <form action="/viewUnits" method="post">

    <input name="unitIdMatch" value="${requestScope.unitIdMatch}" type="hidden"/>

    <input name="action" value="${FormConst.EDIT}" type="hidden"/>
    <table>
        <tr>
            <td><label for="queried_unit_name">Unit: </label></td>
            <td><label>
                <input name="queried_unit_name" type="text" value="${requestScope.queriedUnit.name}"/>
            </label></td>
        </tr>
        <tr>
            <td><label>Location:</label></td>
            <td><label>
                <select name="queried_location_name">
                    <c:forEach var="location" items="${requestScope.listOfLocations}">
                        <c:choose>
                            <c:when test="${requestScope.queriedUnit.location eq location.name}">
                                <option value="${location.id}" selected="">${location.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${location.id}">${location.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </label></td>
        </tr>


        <tr>
            <td><label for="queried_head_of_unit">Head name: </label></td>
            <td><label>
                <select id="queried_head_of_unit" name="queried_head_of_unit" class="required">
                    <c:forEach var="soldier" items="${requestScope.listOfSoldiers}">
                        <c:choose>
                            <c:when test="${requestScope.queriedUnit.headId eq soldier.id}">
                                <option value="${soldier.id}" selected="">${soldier.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${soldier.id}">${soldier.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
                <!--<input id="queried_commander_name" name="queried_commander_name" type="text"/>-->
            </label></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" class="btn primary pull-right" value="Edit"/>
            </td>
        </tr>
    </table>
</c:if>

<c:if test="${fn:contains(requestScope.userPath, '/viewLocations') and fn:contains(requestScope.action, FormConst.EDIT)}">
    <%--<jsp:useBean id="queriedLocation" scope="request" class="ncl.military.entity.Location"/>--%>
    <form action="/viewLocations" method="post">

    <input name="locationIdMatch" value="${requestScope.locationIdMatch}" type="hidden"/>

    <input name="action" value="${FormConst.EDIT}" type="hidden"/>
    <table>
        <tr>
            <td name="queried_location_name">Location</td>
            <td><label>
                <input name="queried_location_name" value="${requestScope.queriedLocation.name}" type="text"/>
            </label></td>
        </tr>
        <tr>
            <td name="queried_location_region">Region</td>
            <td><label>
                <input name="queried_location_region" value="${requestScope.queriedLocation.region}" type="text"/>
            </label></td>
        </tr>
        <tr>
            <td>City</td>
            <td><label>
                <input name="queried_location_city" value="${requestScope.queriedLocation.city}" type="text"/>
            </label></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" class="btn primary pull-right" value="Edit"/>
            </td>
        </tr>
    </table>
</c:if>
</form>