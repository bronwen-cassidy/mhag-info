<%@include file="../includes.jsp" %>

<table border="1" class="ui-widget-content" width="100%">
    <thead>
        <tr class="ui-widget-header">
            <th onclick="adminSearch('0', 'owner')" style="cursor:pointer">Added By</th>
            <th onclick="adminSearch('0', 'ip_address')" style="cursor:pointer">IP Address</th>
            <th onclick="adminSearch('0', 'label')" style="cursor:pointer">Set Name</th>
            <th>Skills</th>
            <th>Action</th>
        </tr>
    </thead>
    <c:forEach var="s" items="${model.sets}" varStatus="indexer">
        <tr id="abb<c:out value="${indexer.index}"/>">
            <td><c:out value="${s.owner}"/></td>
            <td><c:out value="${s.ipAddress}"/></td>
            <td><a href="<c:url value="/viewset.htm"><c:param name="s" value="${s.displaySetCode}"/></c:url>" target="_blank" title="click to view set details"><c:out value="${s.name}"/></a></td>
            <td><c:out value="${s.skillsDisplayValue}"/></td>
            <td>
                <input class="input-checkbox" id="<c:out value="${s.id}"/>" type="checkbox" name="ids" value="<c:out value="${s.id}"/>" title="check to delete"/>
                <input type="button" name="delbtnId" value="X" title="delete row" onclick="delRow('<c:out value="${s.id}"/>', 'abb<c:out value="${indexer.index}"/>')"/>
            </td>
        </tr>
    </c:forEach>
</table>