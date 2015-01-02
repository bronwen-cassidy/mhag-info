<%@include file="../includes.jsp" %>

<%@include file="../pagination.jsp"%>

<table border="1" class="ui-widget-content" width="100%">
    <thead>
    <tr class="ui-widget-header">
        <th onclick="loadPage('<c:out value="${model.currentPage}"/>', '<c:out value="${model.action}"/>', 'owner', true)" style="cursor:pointer;">Added By</th>
        <th onclick="loadPage('<c:out value="${model.currentPage}"/>', '<c:out value="${model.action}"/>', 'label', true)" style="cursor:pointer;">Set Name</th>
        <th>Skills</th>
        <c:if test="${model.weightColumn != 'No'}">
            <th onclick="loadPage('<c:out value="${model.currentPage}"/>', '<c:out value="${model.action}"/>', '', true)" style="cursor: pointer;">Weighting</th>
        </c:if>
        <th onclick="loadPage('<c:out value="${model.currentPage}"/>', '<c:out value="${model.action}"/>', 'num_up_votes', true)" style="cursor:pointer; width:8px;">Votes</th>
        <c:if test="${requestScope.loggedIn}"><th>Actions</th></c:if>
    </tr>
    </thead>
    <c:forEach var="s" items="${model.sets}" varStatus="indexer">
        <tr id="aaa<c:out value="${indexer.index}"/>">
            <td><c:out value="${s.owner}"/></td>
            <td>
                <a href="<c:url value="viewset.htm"><c:param name="s" value="${s.displaySetCode}"/></c:url>" target="_blank" title="click to view set details"><c:out value="${s.name}"/></a>
            </td>
            <td><c:out value="${s.skillsDisplayValue}"/></td>
            <c:if test="${model.weightColumn != 'No'}">
                <td><c:out value="${s.weighting}"/></td>
            </c:if>
            <td>
                <span id="ssnv<c:out value="${s.id}"/>"><c:out value="${s.numUpVotes}"/></span><img class="clickable" src="<c:url value="/images/Thumbs-up-icon.png"/>" alt="Yes" width="30" height="26" onclick="addVote('<c:out value="${s.id}"/>', '<c:out value="${requestScope.userId}"/>')"/>&nbsp;&nbsp;
                    <%--<span id="ssdv<c:out value="${s.id}"/>"><c:out value="${s.numDownVotes}"/></span><img class="clickable" src="<c:url value="/images/Thumbs-down-icon.png"/>" alt="No" width="30" height="26" onclick="removeVote('<c:out value="${s.id}"/>', '<c:out value="${requestScope.userId}"/>')"/>--%>
            </td>
            <c:if test="${requestScope.loggedIn}">
                <td>
                    <c:if test="${requestScope.userId == s.userId}">
                        <a href="<c:url value="editsavedset.htm"><c:param name="s" value="${s.displaySetCode}"/><c:param name="id" value="${s.id}"/></c:url>" title="click to edit set details">Edit</a>
                        &nbsp; | &nbsp;
                        <input type="button" name="delbtnId" value="X" title="delete row" onclick="delRow('<c:out value="${s.id}"/>', 'aaa<c:out value="${indexer.index}"/>')"/>
                    </c:if>
                </td>
            </c:if>
        </tr>
    </c:forEach>
</table>

<%@include file="../pagination.jsp"%>