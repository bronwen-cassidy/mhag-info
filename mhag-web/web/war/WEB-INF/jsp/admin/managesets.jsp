<%@include file="../includes.jsp" %>
<script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>

<script type="text/javascript" charset="UTF-8">

    function delRow(ssId, rowId) {

        $('#' + ssId).attr("checked", true);
        $.get('deletesets.htm', {ids: ssId}, function(data) {
            if('error' == data) {
                alert('An error has occurred, please research to determine if the set has been removed');
            }
            else {
                $('#' + rowId).attr("style", "display:none");
            }
        });
    }

    function deleteRows() {

        var ids = new Array();
        var rowIds = new Array();

        $('.input-checkbox').each(function(index) {
            var chcked = $(this).is(':checked');
            if(chcked) {
                ids.push($(this).attr('id'));
                rowIds.push('abb' + index);
            }
        });

        var idStr;
        if (ids.length > 0) {
            idStr = ids.join(',');
            $.get('deletesets.htm', {ids: idStr}, function(data) {
                if('error' == data) {
                    alert('An error has occurred, please research to determine if the set has been removed');
                }
                else {
                    for(var i = 0; i < rowIds.length; i++) {
                        $('#' + rowIds[0]).attr("style", "display:none");
                    }
                }
            });
        }
    }


</script>

<div id="admin-panel" class="ui-widget-container">
    <c:choose>
        <c:when test="${requestScope.adminLogin}">

            <table border="1" class="ui-widget-content" width="100%">
                <tr class="ui-widget-header">
                    <td width="100%">Logged In &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;<c:out value="${requestScope.loggedIn}"/></td>
                </tr>
            </table>

            <table border="1" class="ui-widget-content" width="100%">
                <tr>
                    <td><label for="dd-ipaddress">IP Address</label><input type="text" id="dd-ipaddress" name="ipAddress"/></td>
                    <td><label for="dd-owner">Owner</label><input type="text" id="dd-owner" name="owner"/></td>
                    <td><label for="dd-skills">Duplicate skills</label><input type="checkbox" id="dd-skills" name="duplicateSkills"/></td>
                </tr>
                <tr>
                    <td colspan="3">
                        <input class="ui-widget" type="button" name="srchbtn" value="Search" onclick="adminSearch('0', 'id')"/>
                    	<input type="button" name="chkAll" value="Delete Selected" onclick="deleteRows();"/>
                    </td>
                </tr>
            </table>
            <div id="admin-set-content">
                <c:import url="admin/searchresults.jsp"/>
            </div>
        </c:when>
        <c:otherwise>
            <form method="post" action="managesets.htm">
                <table cellpadding="0" cellspacing="0" class="ui-widget-content" width="100%">
                    <tr>
                        <td><label for="dd-username">Username</label><input type="text" name="admin_u" id="dd-username"/></td>
                        <td><label for="dd-password">Password</label><input type="password" name="admin_p" id="dd-password"/></td>
                        <td><input type="submit" value="Submit" name="sub"/></td>
                    </tr>
                </table>
            </form>
        </c:otherwise>
    </c:choose>
</div>