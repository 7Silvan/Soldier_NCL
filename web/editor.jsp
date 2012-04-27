<%--
  User: Silvan
  Date: 27.04.12
  Time: 5:47
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<un:useConstants var="FormConst" className="ncl.military.controller.handle.HandlerFactory"/>
<c:if test="${fn:contains(param.userPath, '/viewSoldiers') and (fn:contains(param.action, FormConst.ADD_SOLDIER) or fn:contains(param.action, FormConst.EDIT))}">
    <jsp:useBean id="queriedSoldier" scope="request" class="ncl.military.entity.Soldier"/>

    <form action="/viewSoldiers" method="post">
    <table>
        <input name="soldierIdMatch" value="${param.soldierIdMatch}" type="hidden"/>

        <c:choose>
            <c:when test="${param.action eq FormConst.EDIT}">
                <input name="action" value="${FormConst.EDIT}" type="hidden"/>
            </c:when>
            <c:otherwise>
                <input name="action" value="${FormConst.ADD_SOLDIER}" type="hidden"/>
            </c:otherwise>
        </c:choose>

        <tr>
            <td><label for="queried_soldier_name">Soldier name: </label></td>
            <td><label>
                <input id="queried_soldier_name" name="queried_soldier_name" type="text"
                       value="${queriedSoldier.name}"/>
            </label></td>
        </tr>
        <tr>
            <td><label for="queried_unit_name">Unit: </label></td>
            <td><label>
                    <%--<input id="queried_unit_name" name="queried_unit_name" type="text" value="${queriedSoldier.unit}"/>--%>
                <select id="queried_unit_name" name="queried_unit_name">
                    <c:forEach var="unit" items="${requestScope.listOfUnits}">
                        <c:choose>
                            <c:when test="${queriedSoldier.unit eq unit.name}">
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
                <select id="queried_soldier_commander" name="queried_soldier_commander">
                    <c:forEach var="soldier" items="${requestScope.listOfSoldiers}">
                        <c:choose>
                            <c:when test="${queriedSoldier.commander eq soldier.id}">
                                <option value="${soldier.id}" selected="">${soldier.name}</option>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${queriedSoldier.id ne soldier.id}">
                                    <option value="${soldier.id}">${soldier.name}</option>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
                <!--<input id="queried_commander_name" name="queried_commander_name" type="text"/>-->
            </label></td>
        </tr>

        <tr>
            <td><label for="queried_soldier_rank">Rank: </label></td>
            <td><label>
                <input id="queried_soldier_rank" name="queried_soldier_rank" type="text"
                       value="${queriedSoldier.rank}"/>
            </label></td>
        </tr>
        <tr>
            <td><label for="queried_soldier_birthdate">Rank: </label></td>
            <td><label>
                <input id="queried_soldier_birthdate" name="queried_soldier_birthdate" type="text"
                       value="${queriedSoldier.birthDate}"/>
            </label></td>
        </tr>
        <tr>
            <td colspan="2">
                <c:choose>
                    <c:when test="${param.action eq FormConst.EDIT}">
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
<c:if test="${fn:contains(param.userPath, '/viewUnits') and fn:contains(param.action, FormConst.EDIT)}">
    <jsp:useBean id="queriedUnit" scope="request" class="ncl.military.entity.Unit"/>
    <input name="unitIdMatch" value="${param.unitIdMatch}" type="hidden"/>

    <input name="action" value="${FormConst.EDIT}" type="hidden"/>

    <tr>
        <td><label for="queried_unit_name">Unit: </label></td>
        <td><label>
            <input name="queried_unit_name" type="text" value="${queriedUnit.name}"/>
        </label></td>
    </tr>
    <tr>
        <td>Location:</td>
        <td><label>
            <select name="queried_location_name">
                <c:forEach var="location" items="${requestScope.listOfLocations}">
                    <c:choose>
                        <c:when test="${queriedUnit.location eq location.id}">
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
        <td colspan="2">
            <input type="submit" class="btn primary pull-right" value="Edit"/>
        </td>
    </tr>
    </table>
</c:if>

<c:if test="${fn:contains(param.userPath, '/viewLocations') and fn:contains(param.action, FormConst.EDIT)}">
    <jsp:useBean id="queriedLocation" scope="request" class="ncl.military.entity.Location"/>
    <input name="locationIdMatch" value="${param.locationIdMatch}" type="hidden"/>

    <input name="action" value="${FormConst.EDIT}" type="hidden"/>

    <tr>
        <td>Location:</td>
        <td><label>
            <input name="queried_location_name" value="${queriedLocation.name}" type="text"/>
        </label></td>
    </tr>
    <tr>
        <td>Location:</td>
        <td><label>
            <input name="queried_location_name" value="${queriedLocation.region}" type="text"/>
        </label></td>
    </tr>
    <tr>
        <td>Location:</td>
        <td><label>
            <input name="queried_location_name" value="${queriedLocation.city}" type="text"/>
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