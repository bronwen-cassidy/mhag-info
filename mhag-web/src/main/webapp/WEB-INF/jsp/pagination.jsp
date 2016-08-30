<%@include file="includes.jsp" %>

<c:if test="${model.page.totalResults > 50}">
    <table border="1" class="ui-state-default" width="100%">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${model.page.first > 0}">
                        <a href="#" onclick="loadPage('0', '<c:out value="${model.action}"/>', '<c:out value="${model.orderBy}"/>', false)">&lt;&lt; first</a>&nbsp;&nbsp;
                        <a href="#" onclick="loadPage('<c:out value="${model.page.prev}"/>', '<c:out value="${model.action}"/>', '<c:out value="${model.orderBy}"/>', false)">&lt; prev</a>&nbsp;&nbsp;
                    </c:when>
                    <c:otherwise>
                        &lt;&lt; first&nbsp;&nbsp;&lt; prev&nbsp;&nbsp;
                    </c:otherwise>
                </c:choose>
                <c:forEach begin="0" end="${model.page.numPages}" var="pg">
                    <c:set var="num" value="${pg * 50}"/>
                    <c:choose>
                        <c:when test="${model.page.first == num}">
                            <c:out value="${pg}"/><c:if test="${pg < model.page.numPages}"><c:out value=", "/></c:if>
                        </c:when>
                        <c:otherwise>
                            <a href="#" onclick="loadPage('<c:out value="${num}"/>','<c:out value="${model.action}"/>', '<c:out value="${model.orderBy}"/>', false)"><c:out value="${pg}"/><c:if test="${pg < model.page.numPages}"><c:out value=", "/></c:if></a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:choose>
                    <c:when test="${model.page.first == model.page.last}">
                        &nbsp;&nbsp;next &gt;&nbsp;&nbsp;last &gt;&gt;
                    </c:when>
                    <c:otherwise>
                        &nbsp;&nbsp;<a href="#" onclick="loadPage('<c:out value="${model.page.next}"/>','<c:out value="${model.action}"/>', '<c:out value="${model.orderBy}"/>', false)">next &gt;</a>
                        &nbsp;&nbsp;<a href="#" onclick="loadPage('<c:out value="${model.page.last}"/>','<c:out value="${model.action}"/>', '<c:out value="${model.orderBy}"/>', false)">last &gt;&gt;</a>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</c:if>
