<%@include file="../includes.jsp" %>

<script type="text/javascript">
    $(document).ready(function () {
        //$('#skillListAttr').on("keyup paste", function() {
            // get the information
            $.get('skilllist.htm', function(data) {
                $('#skilldatalist').html(data);
            });
        //});
    });
    $(document).ready(function () {
        $('#skillsearchbtn').click(function() {
            // first skill
            var skillN = $('#skillListAttr').val();
            var skillVal =  $('#pieceskillval').val();
            var op = $('#pieceskillop').val();

            // todo second skill
            /*var skillN1 = $('#skillListAttr1').val();
            var skillVal1 =  $('#pieceskillval1').val();
            var op1 = $('#pieceskillop1').val();*/

            var apiece = $('#piecesel').val();
            var krank = $('#lRankIdx').val();
            var bladeVal = $('#lBladeIdx').val();

            var currRank = $('#lRankId').val();
            var currBlade = $('#dd-blade:checked').val();
            if(!currBlade) currBlade = 'G';
             // todo pass through skills as an array
            $.get('skillsearch.htm', {
                x: $.now(), skillName: skillN, skillValue: skillVal, oper: op, piece: apiece,
                rank: krank, blade: bladeVal, currentRank: currRank, currentBlade: currBlade
            },
            function(data){
                $('#resultspanelid').html(data);
                $('#resultspanelid').show();
            });

        });
    });
</script>

<div id="options-panel" class="ui-widget-container">
    <%-- table to record search scope --%>
    <table cellspacing="0" cellpadding="0" width="100%">
        <tr>
            <td>
                <div id="rankpanel">
                    <fieldset class="fieldset-ops">
                        <legend>Rank</legend>
                        <span>
                            <select class="x-type-options" id="lRankIdx" name="xrankSelect">
                                <option value="">All</option>
                                <c:forEach var="rank" items="${model.ranks}">
                                    <option value="<c:out value="${rank.index}"/>"><c:out value="${rank.label}"/></option>
                                </c:forEach>
                            </select>
                        </span>
                    </fieldset>
                </div>
            </td>
            <td>
                <div id="typepanel">
                    <fieldset class="fieldset-ops">
                        <legend>Hunter Type</legend>
                        <span>
                            <select class="x-type-options" id="lBladeIdx" name="xbladeSelect">
                                <option value="B">Blade Master</option>
                                <option value="G">Gunner</option>
                            </select>
                        </span>
                    </fieldset>
                </div>
            </td>
        </tr>
     </table>
 </div>

<div id="content" class="ui-widget-container">
    <table class="ui-widget-content" width="100%">
        <thead>
        <tr class="ui-widget-header">
            <th>Armour Piece</th><th>Skill Value</th><th>Action</th>
        </tr>
        </thead>
        <tr>
            <td>
                <select name="piecex" id="piecesel">
                    <%-- options are partid:numSlots:bodypart --%>
                    <option value="" selected>All Pieces</option>
                    <option value="0">Head</option>
                    <option value="1">Chest</option>
                    <option value="2">Arms</option>
                    <option value="3">Waist</option>
                    <option value="4">Legs</option>
                </select>
            </td>
            <td nowrap>
                <input class="datalist" id="skillListAttr" list="skilldatalist" type="text" placeholder="Type Something">
                <select name="skillvalopx" id="pieceskillop">
                    <%-- options are partid:numSlots:bodypart --%>
                    <option value="gt">&nbsp;&gt;&nbsp;</option>
                    <option value="lt">&nbsp;&lt;&nbsp;</option>
                    <option value="eq">&nbsp;=&nbsp;</option>
                </select>
                <select name="skillvaluex" id="pieceskillval">
                    <c:forEach begin="0" end="20" var="num">
                        <option value="<c:out value="${num}"/>"><c:out value="${num}"/></option>
                    </c:forEach>
                    <c:forEach begin="1" end="5" var="num">
                        <option value="-<c:out value="${num}"/>">-<c:out value="${num}"/></option>
                    </c:forEach>
                </select>
                <%--todo <span>AND</span>
                <input class="datalist" id="skillListAttr1" list="skilldatalist" type="text" placeholder="Type Something">
                <select name="skillvalopx" id="pieceskillop1">
                    &lt;%&ndash; options are partid:numSlots:bodypart &ndash;%&gt;
                    <option value="gt">&nbsp;&gt;&nbsp;</option>
                    <option value="lt">&nbsp;&lt;&nbsp;</option>
                    <option value="eq">&nbsp;=&nbsp;</option>
                </select>
                <select name="skillvaluex" id="pieceskillval1">
                    <c:forEach begin="0" end="20" var="num">
                        <option value="<c:out value="${num}"/>"><c:out value="${num}"/></option>
                    </c:forEach>
                    <c:forEach begin="1" end="5" var="num">
                        <option value="-<c:out value="${num}"/>">-<c:out value="${num}"/></option>
                    </c:forEach>
                </select>--%>
            </td>
            <td>
                <button id="skillsearchbtn"
                        class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"
                        role="button" aria-disabled="false">
                    <span class="ui-button-text">Go</span>
                </button>
            </td>
        </tr>
    </table>
</div>

<div id="resultspanelid" class="ui-widget-container" style="display: none">
    <%-- resides our results --%>
</div>



<datalist id="skilldatalist">

</datalist>
