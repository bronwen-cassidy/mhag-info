<%@include file="../includes.jsp" %>
<table border="1" class="ui-widget-content" width="100%" id="skillResTable">
    <thead>
    <tr class="ui-widget-header">
        <th>Piece</th><th>Armour Name</th><th>Slots</th><th>Skill</th><th>Value</th><th>Add To Armour Sets</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="piece" items="${model.armourPieces}">
        <tr>
            <td><img src="images/<c:out value="${piece.imageValue}"/>.png" height="20px" width="20px"/></td>
            <td><c:out value="${piece.armourName}"/></td>
            <td>
                <c:forEach begin="1" end="${piece.numSlots}">
                    <c:out value="O"/>
                </c:forEach>
                <c:if test="${piece.numSlots == 0}">-</c:if>
            </td>
            <td colspan="2">
                <table style="border-collapse: collapse;" width="100%">
                    <c:forEach var="ent" items="${piece.skills}" varStatus="ct">
                        <c:set var="cmr" value="odd"/>
                        <c:if test="${ent.key == model.skillName}"><c:set var="cmr" value="even"/></c:if>
                        <tr class="<c:out value="${cmr}"/>">
                            <td><c:out value="${ent.key}"/></td>
                            <td width="20%"><c:out value="${ent.value}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
            <td>
                <%-- add this selected to the armour set builder
                <c:out value="${cPiece.armorID}"/>:<c:out value="${cPiece.numSlot}"/>:1:<c:out value="${cPiece.gender}"/>
                --%>
                <input type="checkbox"
                       <c:if test="${model.currentRank < piece.rank || (model.currentBlade != piece.bladeGunner && piece.imageValue != 'head')}">
                           disabled
                           title="change rank and blade on set builder to enable"
                       </c:if>
                       onclick="displayOnSet(this,
                               '<c:out value="${piece.selectValue}"/>select',
                               '<c:out value="${piece.armourId}"/>:<c:out value="${piece.numSlots}"/>:<c:out value="${piece.piece}"/>:${piece.gender}');"/>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty model.armourPieces}">
        <tr>
            <td colspan="6">No peices found to match your criteria. Please try again.</td>
        </tr>
    </c:if>
    </tbody>
</table>