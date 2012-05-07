<%--
  User: Silvan
  Date: 06.05.12
  Time: 20:29
--%>
<div class="modal-header" id="modal-header">
    <a href="#" class="close">×</a>

    <h3>Add new Soldier</h3>
</div>
<form id="checkoutForm" class="cmxform" action="/viewSoldiers" method="post">
    <div class="modal-body">
        <table>

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
                                <c:when test="${requestScope.queriedSoldier.commander eq soldier.id}">
                                    <option value="${soldier.id}" selected="">${soldier.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${requestScope.queriedSoldier.id ne soldier.id}">
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
                    <input id="queried_soldier_rank" name="queried_soldier_rank" type="text" class="required"
                           value="${requestScope.queriedSoldier.rank}"/>
                </label></td>
            </tr>
            <tr>
                <td><label>Date: </label></td>
                <td><label>
                    <input id="date" name="queried_soldier_birthdate" type="text" class="required"
                           value="${requestScope.queriedSoldier.birthDate}"/>
                </label></td>
            </tr>
        </table>
    </div>
    <div class="modal-footer">
        <input name="soldierIdMatch" value="${pageContext.request.parameterMap.soldierIdMatch[0]}"
               type="hidden"/>
        <input name="action" value="${FormConst.ADD_SOLDIER}" type="hidden"/>
        <input type="submit" class="btn primary pull-right" value="Add Soldier"/>
    </div>
</form>