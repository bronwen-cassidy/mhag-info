<%@include file="../includes.jsp" %>

<div id="options-panel" class="ui-widget-container">
    <!-- rank, type, gender, name -->
    <c:import url="../armours/options.jsp"/>
</div>

<div id="content" class="ui-widget-container">
    <c:import url="viewarmourpanel.jsp"/>
</div>

<div id="previewpanelid" class="ui-widget-container">
    <table>
        <tr>
            <td class="equal-width"><c:import url="../armours/skillpanel.jsp"/></td>
            <td class="equal-width"><c:import url="../armours/respanel.jsp"/></td>
        </tr>
    </table>
</div>