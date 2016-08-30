<%@include file="../includes.jsp" %>

<%-- options are partid:numSlots:bodypart --%>
<option value="---" <c:if test="${model.selectedPiece == null}">selected</c:if>>---</option>
<c:forEach var="hPiece" items="${model.armourPieces}">
    <option value="<c:out value="${hPiece.armorID}"/>:<c:out value="${hPiece.numSlot}"/>:<c:out value="${hPiece.bodyPart}"/>:<c:out value="${hPiece.gender}"/>"
            <c:if test="${model.selectedPiece.armorID == hPiece.armorID}">selected</c:if>>
        <spring:escapeBody javaScriptEscape="true"><c:out value="${hPiece.currentArmour}"/></spring:escapeBody>
    </option>
</c:forEach>