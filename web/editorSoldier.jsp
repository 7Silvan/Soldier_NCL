<%--
  User: Silvan
  Date: 11.05.12
  Time: 12:17
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
            <h1>Added! forwarding to Commander.</h1>
        </c:when>
        <c:when test="${fn:contains(requestScope.action, 'editSoldier')}">
            <c:url var="redirectUrl" value="/viewSoldiers">
                <c:param name="action" value="getSubsOfSoldier"/>
                <c:param name="queriedSoldierId" value="${requestScope.queriedSoldier.id}"/>
            </c:url>
            <script type="text/javascript">
                setTimeout('location.replace("${redirectUrl}")', 1000);
            </script>
            <h1>Saved! forwarding to Commander.</h1>
        </c:when>
        <c:when test="${fn:contains(requestScope.action, 'getSubsOfSoldier')}">
            <c:url var="redirectUrl" value="/viewSoldiers">
                <c:param name="action" value="getSubsOfSoldier"/>
                <c:param name="queriedSoldierId" value="${requestScope.queriedSoldier.commander}"/>
            </c:url>
            <script type="text/javascript">
                setTimeout('location.replace("${redirectUrl}")', 1000);
            </script>
            <h1>Saved! forwarding to Commander.</h1>
        </c:when>
        <c:otherwise>
            <script type="text/javascript">
                setTimeout('location.replace("/viewSoldiers?action=getTop")', 1000);
            </script>
            <h1>Saved! forwarding go Top of Soldiers.</h1>
        </c:otherwise>
    </c:choose>
</c:if>
<c:if test="${fn:contains(requestScope.userPath, '/viewSoldiers') and (fn:contains(requestScope.action, FormConst.ADD_SOLDIER) or fn:contains(requestScope.action, FormConst.EDIT_SOLDIER))}">
    <%--<jsp:useBean id="queriedSoldier" scope="request" class="ncl.military.entity.Soldier"/>--%>
    <c:choose>
        <c:when test="${requestScope.action eq FormConst.EDIT_SOLDIER}">
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
            <c:when test="${requestScope.action eq FormConst.EDIT_SOLDIER}">
                <input name="action" value="${FormConst.EDIT_SOLDIER}" type="hidden"/>
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
                    <c:if test="${!fn:contains(requestScope.action, 'editSoldier')}">
                        <option value="">--Top of commanding--</option>
                    </c:if>
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
                    <c:when test="${requestScope.action eq FormConst.EDIT_SOLDIER}">
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
</form>