<%@include file="../includes.jsp" %>
<script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>
<script type="text/javascript" charset="UTF-8">
    $(function() {
        $(".skill-select").change(function() {
            buildCharmSkillsTable();

            var selName = $(this).attr('name');
            var selId = $(this).attr('id');
            var selOptionIndex = $('option:selected', '#' + selId).index();
            var currOpVal = $(this).find("option:selected").val();
            var pieceArr = currOpVal.split(':');
            // number of slots this jewel needs
            var numy = parseInt(pieceArr[1]);
            // not to worry about
            if (selName.indexOf("slot", 0) == -1) return;

            // the name of the select field split into an array gives 'slot'-index in row - piecename
            var selNameArr = selName.split('-');
            var selVal = $('#aazzx' + selNameArr[2]).val();
            var numArmourSlots = parseInt(selVal);

            // if armour has only 1 slot nothing to do
            if (numArmourSlots > 1) {
                var slotsArray = new Array();
                var numClearedSlots = 0;
                // clear any linked cells
                var linkedName = "-l" + selNameArr[1];

                for (var m = 0; m < numArmourSlots; m++) {
                    var tempSelId = 'as' + m + selNameArr[2];
                    if(tempSelId == selId) continue;
                    var tempSel = $('#' + tempSelId).get()[0];
                    // reset any disabled cells and finish
                    var tempSelName = tempSel.name;

                    if (tempSelName.indexOf(linkedName) != -1) {
                        // select is linked clear it
                        tempSel.options[0].selected = true;
                        tempSel.name = tempSelName.substring(0, tempSelName.indexOf("-l"));
                        tempSel.disabled = false;
                        tempSel.style.display = 'block';
                    }
					if(!tempSel.disabled) {
						for (var p = 0; p < tempSel.options.length; p++) {
                            tempSel.options[p].style.display = 'block';
                        }
					}
                    // add in any vacant ones to the list
                    if (tempSel.selectedIndex == 0) {
                        slotsArray[numClearedSlots++] = tempSel;
                    }
                }
                // equal to the number of armour slots the easy option
                if (numy == numArmourSlots) {
                    assignAllSlots(numArmourSlots, selNameArr[2], selId, selOptionIndex, linkedName);
                } else {
                    if ((numy-1) > slotsArray.length) {
                        alert("error please start again");
                        return;
                    }
                    // linkSlots
                    linkSlots(numy-1, slotsArray, selOptionIndex, linkedName);
                    // clean up by re-evaluating which options need to be inlined or noned
                    evaluateOptions(numArmourSlots, selNameArr[2], selId);
                }
            }
        });
    });

</script>

<c:if test="${pieceName == null}"><c:set var="pieceName" value="${model.piece}"/></c:if>
<c:if test="${numSlots == null}"><c:set var="numSlots" value="${model.numSlots}"/></c:if>
<c:if test="${slots == null}"><c:set var="slots" value="${model.slots}"/></c:if>

<c:set var="jewels" value="${model.jewels}"/>

<input type="hidden" name="aazzxy<c:out value="${pieceName}"/>"
       id="aazzx<c:out value="${pieceName}"/>"
       value="<c:out value="${numSlots}"/>"/>

<table cellpadding="0" cellspacing="0" class="silent">
    <tr>
        <!-- covers the slots for all items except the talisman -->
        <c:set var="slotsUsed" value="0"/>
        <c:forEach var="slot" items="${slots}" varStatus="indexer">
            <c:set var="linked" value="1"/>            
            <c:set var="linkedVal" value="xyz"/>
            <td>
                <select name="slot-<c:out value="${indexer.index}"/>-<c:out value="${pieceName}"/>"
                        id="as<c:out value="${indexer.index}"/><c:out value="${pieceName}"/>" class="skill-select">
                    
                    <option value="---" <c:if test="${slot == null}">selected</c:if>>---</option>
                    <c:forEach var="jewel" items="${jewels}">
                        <c:if test="${slot.jewelID == jewel.jewelID}"><c:set var="linked" value="${jewel.numSlot}"/></c:if>                        
                        <c:if test="${jewel.numSlot <= numSlots}">
                            <option value="<c:out value="${jewel.jewelID}"/>:<c:out value="${jewel.numSlot}"/>:<c:out value="${pieceName}"/>:<c:out value="${indexer.index}"/>" <c:if test="${slot.jewelID == jewel.jewelID}">selected</c:if>>
                                <c:out value="${jewel.jewelNameSkill}"/>
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
                <c:set var="slotsUsed" value="${slotsUsed + 1}"/>
            </td>
            <c:if test="${linked > 1}">
                <c:set var="numIndex" value="${indexer.index + 1}"/>
                <c:forEach var="x" begin="1" end="${linked - 1}">
                    <td>
                        <select name="<c:out value="slot-${numIndex + 1}-${pieceName}-l${indexer.index}"/>"
                            id="as<c:out value="${numIndex}"/><c:out value="${pieceName}"/>" class="skill-select" style="display:none" disabled>

                            <option value="---" <c:if test="${slot == null}">selected</c:if>>---</option>
                            <c:forEach var="jewel" items="${jewels}">
                                <c:if test="${jewel.numSlot <= numSlots}">
                                    <option value="<c:out value="${jewel.jewelID}"/>:<c:out value="${jewel.numSlot}"/>:<c:out value="${pieceName}"/>:<c:out value="${numIndex}"/>" <c:if test="${slot.jewelID == jewel.jewelID}">selected</c:if>>
                                        <c:out value="${jewel.jewelNameSkill}"/>
                                    </option>
                                </c:if>
                            </c:forEach>
                        </select>
                        <c:set var="numIndex" value="${numIndex + 1}"/>
                        <c:set var="slotsUsed" value="${slotsUsed + 1}"/>
                    </td>
                </c:forEach>
            </c:if>
        </c:forEach>
        <%--handle left overs--%>            
        <c:if test="${numSlots > 0}">
	        <c:forEach var="y" begin="${slotsUsed}" end="${numSlots - 1}">
	            <td>
	                <c:set var="numLeft" value="${numSlots - slotsUsed}"/>
	                <select name="<c:out value="slot-${y}-${pieceName}"/>"
	                    id="as<c:out value="${y}"/><c:out value="${pieceName}"/>" class="skill-select">

	                    <option value="---" selected>---</option>
	                    <c:forEach var="jewel" items="${jewels}">
	                        <c:if test="${jewel.numSlot <= numLeft}">
	                            <option value="<c:out value="${jewel.jewelID}"/>:<c:out value="${jewel.numSlot}"/>:<c:out value="${pieceName}"/>:<c:out value="${y}"/>">
	                                <c:out value="${jewel.jewelNameSkill}"/>
	                            </option>
	                        </c:if>
	                    </c:forEach>
	                </select>
	            </td>
	        </c:forEach>
        </c:if>
    </tr>
</table>