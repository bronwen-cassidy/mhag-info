<%@include file="../includes.jsp" %>
<c:forEach var="sk" items="${model.skills}">
    <option value="<c:out value="${sk.skillName}"/>"></option>
</c:forEach>