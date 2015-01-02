<%@include file="../includes.jsp" %>
<script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>
<c:if test="${model.armourSet != null}">
    <script type="text/javascript" charset="UTF-8">
        $(function() {
            buildCharmSkillsTable();
            buildResistencesTable();
        });
    </script>
</c:if>

<div id="tabs">
    <ul>
        <li><a href="#tabs-1">Armour Sets</a></li>
        <%--<li><a href="<c:url value="skillsearch.htm"/>" title="tabs-3">Skill Finder</a></li>--%>
        <li><a href="#tabs-2">Set Planner</a></li>
        <li><a href="<c:url value="searchsets.htm"/>" title="tabs-3">Search</a></li>
        <li><a href="<c:url value="listsets.htm"/>" title="tabs-4">MHAG Database</a></li>
    </ul>
    <div id="tabs-1">
        <div id="options-panel" class="ui-widget-container">
            <!-- rank, type, gender, name -->
            <c:import url="armours/options.jsp"/>
        </div>

        <div id="content" class="ui-widget-container">
            <c:import url="armours/armourpanel.jsp"/>
        </div>

        <div id="previewpanelid" class="ui-widget-container">
            <table>
                <tr>
                    <td class="equal-width"><c:import url="armours/skillpanel.jsp"/></td>
                    <td class="equal-width"><c:import url="armours/respanel.jsp"/></td>
                </tr>
            </table>
        </div>
    </div>
    <div id="tabs-2">
        <c:import url="armours/skillsearchpanel.jsp"/>
    </div>
    <div id="tabs-3">

    </div>
    <div id="tabs-4">

    </div>
</div>