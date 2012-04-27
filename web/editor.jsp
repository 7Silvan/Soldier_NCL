<%--
  User: Silvan
  Date: 27.04.12
  Time: 5:47
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<un:useConstants var="FormConst" className="ncl.military.controller.handle.HandlerFactory"/>
<c:if test="${fn:contains(param.action, FormConst.ADD_SOLDIER) or fn:contains(param.action, FormConst.EDIT)}">
    <jsp:useBean id="queriedSoldier" scope="request" class="ncl.military.entity.Soldier"/>
    <form action="/viewSoldiers" method="post">
    <table>
        <input name="soldierIdMatch" value="${param.soldierIdMatch}" type="hidden"/>
        <input name="action" value="edit" type="hidden"/>
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
                            <c:when test="${queriedSoldier.unit eq unit.id}">
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
            <td colspan="2"><input type="submit" class="btn primary pull-right" value="${param.action}"/></td>
        </tr>
    </table>
</c:if>
</form>