<%@include file="../includes.jsp" %>

<c:forEach var="entry" items="${model.resistences}">
    <tr>
        <td class="aligned">
            <c:if test="${entry.key == 'Defence'}">Defence</c:if>
            <c:if test="${entry.key != 'Defence'}"><img src="images/<c:out value="${entry.key}"/>.png" height="20px" width="20px"/></c:if>
        </td>
        <c:forEach var="reses" items="${entry.value}">
            <td class="aligned">
                <c:choose>
                    <c:when test="${entry.key != 'Defence'}"><c:out value="${reses.resDisplayValue}"/></c:when>
                    <c:otherwise><c:out value="${reses.defenseDisplayValue}"/></c:otherwise>
                </c:choose>
            </td>
        </c:forEach>
    </tr>
</c:forEach>