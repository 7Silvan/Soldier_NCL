<%--
  Created by IntelliJ IDEA.
  User: gural
  Date: 19.04.12
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>

<div class="content page-content" id="page-content">
    <div id="banner-top">
        <div id="banner-path">
            <a href="/viewSoldiers">ROOT</a>
            <c:out value=">>"/>
            <c:forEach items="${requestScope.hierarchyList}" var="soldier">
                <a href="/viewSoldiers?action=getSubsOf&queriedSoldierId=${soldier.id}">${soldier.name}</a>
                <c:out value=">>"/>
            </c:forEach>
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
                            <td>
                                <a href="/viewSoldiers?action=getSubsOf&queriedSoldierId=${soldier.id}">${soldier.name}</a>
                            </td>
                            <td>${soldier.rank}</td>
                            <td>${soldier.commander}</td>
                            <td>${soldier.unit}</td>
                            <td>${soldier.birthDate}</td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>

                <!-- <td><button class="btn small">[button for some action]</button></td> -->
                <hr/>
                <h2>testing</h2>
            </div>
        </div>
    </div>
</div>
</div>