<%--
  User: gural
  Date: 24.04.12
  Time: 12:29
--%>

<h1>Hello dear \${username}!</h1>

This page will help you to get started with Soldiers(alpha).

<fieldset>
    <legend>Getting XML Data from Server, Building HTML Table</legend>
    <form action="#">
        <input name="soldierIdMatch" value="1000005" type="hidden"/>
        <input type="button" value="Show Soldiers"
        <%--onclick="xmlSoldiersTable('1000005', 'xml-soldiers-table')"/>--%>
               onclick="clientTable('xml-soldiers-table')"/>
    </form>
    <a href="#" class="btn" onclick='xmlSoldiersTable("1000005", "xml-soldiers-table")'>soldiers</a>
    <a href="#" class="btn" onclick='clientTable("xml-soldiers-table")'>test</a>

    <p/>

    <div id="xml-soldiers-table"></div>
</fieldset>