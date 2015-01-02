<%@include file="../includes.jsp" %>
<script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>
<script type="text/javascript" charset="UTF-8">

    $(function() {
        $(".setup-select").change(function() {

            var elemName = $(this).attr('name');
            var elemId = elemName + 'select';
            var targetElemId = '#' + elemName + 'targetid';
            var selectedOp = $(this).find("option:selected").val();

            var pieces = selectedOp.split(':');
            var rankVal = $('#lRankId').val();

            // always clear the target
            $(targetElemId).html("");

            var pieceId = pieces[0];
            if (pieceId != '---') {
                var slots = pieces[1];
                if (slots != '0') {
                    $.get('jewels.htm', {id: pieceId, numSlots: slots, rank: rankVal, piece: elemName}, function(data) {
                        $(targetElemId).html(data);
                    });
                }
            }

            buildCharmSkillsTable();
            buildResistencesTable();

        });
    });

    $(function() {
        $("#talismanselect").change(function() {

            // populate the new set of selectable charms according to the number of slots
            var selectedOpVal = $(this).find("option:selected").val();
            var rankVal = $('#lRankId').val();

            var nSlots = "0";
            if (selectedOpVal != '---') {
                var pieces = selectedOpVal.split(':');
                nSlots = pieces[0];
            }
            // whenever we change the number of slots in the talisman we must clear any other skills
            $.get('charmskills.htm', {index: -1, numSlots: nSlots, rank: rankVal, selectedOption: selectedOpVal}, function(data2) {
                $('#sourcecharmid1').html(data2);
                $('#targetcharmid1').html("");
                $('#sourcecharmid2').html("");
                $('#targetcharmid2').html("");
                buildCharmSkillsTable();
            });
        });
    });

    $(function() {
        $("#charmid1").change(function() {
            var sourceId = 'charmid1';
            var selectedOpVal = $(this).find("option:selected").val();
            if (selectedOpVal == '---') {
                // clear the points
                $('#targetcharmid1').html("");
            } else {

                var rankVal = $('#lRankId').val();
                var numSlotsVal = $('#talismanselect').find("option:selected").val();
                $.get('charmpoints.htm', {skillId: selectedOpVal, source: sourceId, rank: rankVal, numSlots: numSlotsVal, charmIndex : "1"}, function(data) {
                    $('#targetcharmid1').html(data);
                    buildCharmSkillsTable();
                });
            }
        });
    });

</script>

<c:set value="${model.gender}" var="gender" scope="request"/>
<c:set var="armourSet" value="${model.armourSet}" scope="request"/>
<input type="hidden" name="currSetId" id="existingSetIdr" value="<c:out value="${armourSet.id}"/>"/>

<table>
    <tr>
        <td width="85%">
            <fieldset>
                <legend>Set Up</legend>
                <table>
                    <tr>
                        <td style="width:30px;">
                            <img src="images/weapon.png" height="20px" width="20px"/>
                        </td>
                        <td id="wepsourceid">
                            <select name="wep" id="wepselect" class="setup-select">
                                <%-- options are partid:numSlots:bodypart --%>
                                <option value="---" <c:if test="${armourSet == null || armourSet.numWepSlots == 0}">selected</c:if>>No Slot
                                </option>
                                <option value="-4:1:wep" <c:if test="${armourSet.numWepSlots == 1}">selected</c:if>>One Slot</option>
                                <option value="-5:2:wep" <c:if test="${armourSet.numWepSlots == 2}">selected</c:if>>Two Slots</option>
                                <option value="-6:3:wep" <c:if test="${armourSet.numWepSlots == 3}">selected</c:if>>Three Slots</option>
                            </select>
                        </td>
                        <td id="weptargetid">
                            <c:if test="${armourSet.numWepSlots > 0}">
                                <c:set var="pieceName" value="wep" scope="request"/>
                                <c:set var="numSlots" value="${armourSet.numWepSlots}" scope="request"/>
                                <c:set var="slots" value="${armourSet.wepJewels}" scope="request"/>
                                <c:import url="../armours/jewels.jsp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:30px;">
                            <img src="images/head.png" height="20px" width="20px"/>
                        </td>
                        <td id="headsourceid">
                            <select name="head" id="headselect" class="setup-select">
                                <%-- options are partid:numSlots:bodypart --%>
                                <option value="---" <c:if test="${armourSet.headArmour == null}">selected</c:if>>---</option>
                                <c:forEach var="hPiece" items="${model.head}">
                                    <option value="<c:out value="${hPiece.armorID}"/>:<c:out value="${hPiece.numSlot}"/>:0"
                                            <c:if test="${armourSet.headArmour.armorID == hPiece.armorID}">selected</c:if>>
                                        <spring:escapeBody javaScriptEscape="true"><c:out value="${hPiece.currentArmour}"/></spring:escapeBody>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td id="headtargetid">
                            <c:if test="${armourSet.headArmour != null}">
                                <c:set var="pieceName" value="head" scope="request"/>
                                <c:set var="numSlots" value="${armourSet.headArmour.numSlot}" scope="request"/>
                                <c:set var="slots" value="${armourSet.headJewels}" scope="request"/>
                                <c:import url="../armours/jewels.jsp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:30px;">
                            <img src="images/chest.png" height="20px" width="20px"/>
                        </td>
                        <td id="chestsourceid">
                            <select name="chest" id="chestselect" class="setup-select">
                                <option value="---" <c:if test="${armourSet.chestArmour == null}">selected</c:if>>---</option>
                                <c:forEach var="cPiece" items="${model.chest}">
                                    <option value="<c:out value="${cPiece.armorID}"/>:<c:out value="${cPiece.numSlot}"/>:1"
                                            <c:if test="${armourSet.chestArmour.armorID == cPiece.armorID}">selected</c:if>>
                                        <spring:escapeBody javaScriptEscape="true"><c:out value="${cPiece.currentArmour}"/></spring:escapeBody>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td id="chesttargetid">
                            <c:if test="${armourSet.chestArmour != null}">
                                <c:set var="pieceName" value="chest" scope="request"/>
                                <c:set var="numSlots" value="${armourSet.chestArmour.numSlot}" scope="request"/>
                                <c:set var="slots" value="${armourSet.chestJewels}" scope="request"/>
                                <c:import url="../armours/jewels.jsp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:30px;">
                            <img src="images/arms.png" height="20px" width="20px"/>
                        </td>
                        <td id="armssourceid">
                            <select name="arms" id="armselect" class="setup-select">
                                <option value="---" <c:if test="${armourSet.armArmour == null}">selected</c:if>>---</option>
                                <c:forEach var="aPiece" items="${model.arms}">
                                    <option value="<c:out value="${aPiece.armorID}"/>:<c:out value="${aPiece.numSlot}"/>:2"
                                            <c:if test="${armourSet.armArmour.armorID == aPiece.armorID}">selected</c:if>>
                                        <spring:escapeBody javaScriptEscape="true"><c:out value="${aPiece.currentArmour}"/></spring:escapeBody>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td id="armstargetid">
                            <c:if test="${armourSet.armArmour != null}">
                                <c:set var="pieceName" value="arms" scope="request"/>
                                <c:set var="numSlots" value="${armourSet.armArmour.numSlot}" scope="request"/>
                                <c:set var="slots" value="${armourSet.armJewels}" scope="request"/>
                                <c:import url="../armours/jewels.jsp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:30px;">
                            <img src="images/waist.png" height="20px" width="20px"/>
                        </td>
                        <td id="waistsourceid">
                            <select name="waist" id="waistselect" class="setup-select">
                                <option value="---" <c:if test="${armourSet.waistArmour == null}">selected</c:if>>---</option>
                                <c:forEach var="wPiece" items="${model.waist}">
                                    <option value="<c:out value="${wPiece.armorID}"/>:<c:out value="${wPiece.numSlot}"/>:3"
                                            <c:if test="${armourSet.waistArmour.armorID == wPiece.armorID}">selected</c:if>>
                                        <spring:escapeBody javaScriptEscape="true"><c:out value="${wPiece.currentArmour}"/></spring:escapeBody>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td id="waisttargetid">
                            <c:if test="${armourSet.waistArmour != null}">
                                <c:set var="pieceName" value="waist" scope="request"/>
                                <c:set var="numSlots" value="${armourSet.waistArmour.numSlot}" scope="request"/>
                                <c:set var="slots" value="${armourSet.waistJewels}" scope="request"/>
                                <c:import url="../armours/jewels.jsp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:30px;">
                            <img src="images/legs.png" height="20px" width="20px"/>
                        </td>
                        <td id="legssourceid">
                            <select name="legs" id="legselect" class="setup-select">
                                <option value="---" <c:if test="${armourSet.legArmour == null}">selected</c:if>>---</option>
                                <c:forEach var="lPiece" items="${model.legs}">
                                    <option value="<c:out value="${lPiece.armorID}"/>:<c:out value="${lPiece.numSlot}"/>:4"
                                            <c:if test="${armourSet.legArmour.armorID == lPiece.armorID}">selected</c:if>>
                                        <spring:escapeBody javaScriptEscape="true"><c:out value="${lPiece.currentArmour}"/></spring:escapeBody>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td id="legstargetid">
                            <c:if test="${armourSet.legArmour != null}">
                                <c:set var="pieceName" value="legs" scope="request"/>
                                <c:set var="numSlots" value="${armourSet.legArmour.numSlot}" scope="request"/>
                                <c:set var="slots" value="${armourSet.legJewels}" scope="request"/>
                                <c:import url="../armours/jewels.jsp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td style="width:30px;">
                            <img src="images/talisman.png" height="20px" width="20px"/>
                        </td>
                        <td id="talismansourceid">
                            <select name="talisman" id="talismanselect" class="ignore-setup-select" onchange="talisSelect(this);">
                                <option value="---" <c:if test="${armourSet == null || armourSet.numCharmSlots == 0}">selected</c:if>>No
                                    Slots
                                </option>
                                <option value="1:1:talisman" <c:if test="${armourSet.numCharmSlots == 1}">selected</c:if>>One Slot</option>
                                <option value="2:2:talisman" <c:if test="${armourSet.numCharmSlots == 2}">selected</c:if>>Two Slots</option>
                                <c:if test="${model.highRank}">
                                    <option value="3:3:talisman" <c:if test="${armourSet.numCharmSlots == 3}">selected</c:if>>Three Slots</option>
                                </c:if>
                            </select>
                        </td>
                        <td id="talismantargetid">
                            <!-- tali jewels here -->
                            <c:if test="${armourSet.numCharmSlots > 0}">
                                <c:set var="pieceName" value="talisman" scope="request"/>
                                <c:set var="numSlots" value="${armourSet.numCharmSlots}" scope="request"/>
                                <c:set var="slots" value="${armourSet.charmJewels}" scope="request"/>
                                <c:import url="../armours/jewels.jsp"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <table cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="skill-label">
                                        Charm Skill
                                    </td>
                                    <td id="sourcecharmid1">
                                        <select name="charmidx1" id="charmid1" class="charm-select">
                                            <option value="---"
                                                    <c:if test="${armourSet == null || armourSet.firstCharmSkill == null}">selected</c:if>>
                                                ---
                                            </option>
                                            <c:forEach var="c" items="${model.skills}">
                                                <option value="<c:out value="${c.skill.skillID}"/>"
                                                        <c:if test="${c.skill.skillID == armourSet.firstCharmSkill.skillID}">selected</c:if>>
                                                    <c:out value="${c.skill.skillName}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td id="targetcharmid1" style="width:25px">
                                        <!-- the number range for first one-->
                                        <c:if test="${armourSet.firstCharmSkill != null}">
                                            <c:set var="charmIndex" value="1" scope="request"/>
                                            <c:set var="points" value="${armourSet.firstCharmSkill.charmPoints}" scope="request"/>
                                            <c:set var="targetPoint" value="${armourSet.firstCharmSkill.points}" scope="request"/>
                                            <c:import url="../armours/charmpoints.jsp"/>
                                        </c:if>
                                    </td>
                                    <td id="sourcecharmid2">
                                        <!-- the skills for second one-->
                                        <c:if test="${armourSet.firstCharmSkill != null}">
                                            <c:set var="charmIndex" value="2" scope="request"/>
                                            <c:set var="selectedSkill" value="${armourSet.secondCharmSkill.skillID}" scope="request"/>
                                            <c:import url="../armours/charmskills.jsp"/>
                                        </c:if>
                                    </td>
                                    <td id="targetcharmid2" style="width:25px">
                                        <!-- the number range for second one-->
                                        <c:if test="${armourSet.secondCharmSkill != null}">
                                            <c:set var="charmIndex" value="2" scope="request"/>
                                            <c:set var="points" value="${armourSet.secondCharmSkill.charmPoints}" scope="request"/>
                                            <c:set var="targetPoint" value="${armourSet.secondCharmSkill.points}" scope="request"/>
                                            <c:import url="../armours/charmpoints.jsp"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </fieldset>
        </td>
        <td valign="top">
            <span class="logo"><img class="logo" src="styles/images/MH_logo.png" alt="mh logo"/></span>
        </td>
    </tr>
</table>