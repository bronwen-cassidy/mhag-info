<%@include file="../includes.jsp" %>

<c:forEach var="setEntry" items="${model.set}">
    <tr>
        <td class="aligned"><c:out value="${setEntry.key.skillName}"/></td>
        <c:forEach var="entry" items="${setEntry.value}">
            <td class="aligned <c:if test="${entry.value.skillDisplay}">calculated</c:if> <c:if test="${entry.value.skillDisplay}">torsoskill</c:if>">                
                <c:choose>
                    <c:when test="${entry.value.torsoUpPiece}">
                        <img src="images/torso-up.png" height="20px" width="20px"/>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${entry.value.displayValue}" escapeXml="false"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </c:forEach>
    </tr>
</c:forEach>

<tr><td colspan="10"><hr/></td></tr>
<c:forEach var="armorMatsEntry" items="${model.armorMaterials}">
    <tr>
        <td class="aligned"><c:out value="${armorMatsEntry.key}"/>:</td>
        <td colspan="9"><c:out value="${armorMatsEntry.value}"/></td>
    </tr>
</c:forEach>
<c:forEach var="jewelMatsEntry" items="${model.jewelMaterials}">
    <tr>
        <td class="aligned"><c:out value="${jewelMatsEntry.key}"/>:</td>
        <td colspan="9"><c:out value="${jewelMatsEntry.value}"/></td>
    </tr>
</c:forEach>