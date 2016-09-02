<%@include file="../includes.jsp" %>
<script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>

<script type="text/javascript" charset="UTF-8">
    $(function() {
        $(".type-options").change(function() {

            // rank, blade, female
            var lowVal = $('#lRankId').val();
            var bladeVal = $('#dd-blade:checked').val();
            var maleVal = $('#dd-male:checked').val();
            if(bladeVal) {
                $('#skillSearchHT').html("Blade");
            } else {
                $('#skillSearchHT').html("Gunner");
            }

            if (!bladeVal) bladeVal = "gun";
            if (!maleVal) maleVal = "female";



            $.get('armour.htm', {rank: lowVal, blade: bladeVal, female: maleVal}, function(data) {
                $('#content').html(data);
                $('#skills-preview-tbbody').html("");
                $('#res-preview-tbbody').html("");
            });
        });
    });

    $(function() {
        $(".gender-options").click(function() {
            var clearSkills = false;

            $(".setup-select").each(function() {
                var sel = $(this).get()[0];
                var selId = sel.id;
                if (selId != 'wepselect') {

                    var lowVal = $('#lRankId').val();
                    var bladeVal = $('#dd-blade:checked').val();
                    var maleVal = $('#dd-male:checked').val();

                    if (!bladeVal) bladeVal = "gun";
                    if (!maleVal) maleVal = "female";

                    var pieceName = sel.name;
                    var selIndex = sel.selectedIndex;
                    var selOpVal = sel.options[selIndex].value;

                    var opsArray = selOpVal.split(':');

                    if(opsArray.length > 3) {
                        // grab the gender if female only clear the skills
                        var genderOp = opsArray[3];
                        if('A' != genderOp) {
                            clearSkills = true;
                            sel.selectedIndex = 0;
                            $('#' + pieceName + 'targetid').html("");
                        }
                    }

                    $.get('armouropts.htm', {rank: lowVal, blade: bladeVal, female: maleVal, opt: selOpVal, piece: pieceName}, function(data) {
                        $('#' + selId).html(data);
                    });
                }
            });
            if(clearSkills) {
                buildCharmSkillsTable();
                buildResistencesTable();
            }
        });
    });

</script>

<table cellspacing="0" cellpadding="0" width="100%">
    <tr>
        <td colspan="4" width="100%">
            <div id="reg-panel">
                <c:import url="admin/reglogin.jsp"/>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <div id="rankpanel">
                <fieldset class="fieldset-ops">
                    <legend>Rank</legend>
                    <span>
                        <select class="type-options" id="lRankId" name="rankSelect">
                            <c:forEach var="rank" items="${model.ranks}">
                                <option value="<c:out value="${rank.index}"/>" <c:if test="${model.rank == rank.index}">selected</c:if>><c:out value="${rank.label}"/></option>
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
                        <input class="type-options" id="dd-blade" type="radio" name="weptype" value="blade"
                               <c:if test="${model.type== 'B' || model.type == null}">checked</c:if> >Blade Master<br/>
                        <input class="type-options" id="dd-gun" type="radio" name="weptype" value="gunner" <c:if test="${model.type== 'G'}">checked</c:if>>Gunner
                    </span>
                </fieldset>
            </div>
        </td>
        <td>
            <div id="genderpanel">
                <fieldset class="fieldset-ops">
                    <legend>Gender</legend>
                    <span>
                        <input class="gender-options" id="dd-male" type="radio" name="gender" value="male"
                               checked>Male<br/>
                        <input class="gender-options" id="dd-female" type="radio" name="gender" value="female"
                               <c:if test="${model.gender== 'F'}">checked</c:if>>Female
                    </span>
                </fieldset>
            </div>
        </td>
        <td>
            <div id="namepanel">
                <fieldset class="fieldset-ops">
                    <legend>Set Name</legend>
                    <span>
                        <c:set var="setName" value="Unnamed Set"/>
                        <c:if test="${model.armourSet != null}"><c:set var="setName" value="${model.armourSet.name}"/></c:if>
                        <input style="margin-top:8px;" type="text" id="setName" name="armourSetName" value="<c:out value="${setName}"/>" onmouseout="buildCharmSkillsTable();"/>
                    </span>
                    <span>
                        <c:if test="${model.ss == null}">
                            <input type="button" id="sveset" name="saveMySet" value="Save" title="saves current set as new" onclick="saveSet();"/>
                            <input type="button" style="display:none;" id="upset" name="updateMySet" value="Update" title="updates the current set only" onclick="updateSet();"/>
                            <input type="button" style="display:none;" id="delset" name="deleteMySet" value="Delete" title="deletes current set only" onclick="document.forms.delxyz.submit();"/>
                        </c:if>
                        <c:if test="${model.ss != null}">
                            <input type="button" id="delsetr" name="deleteMySet" value="Delete" title="deletes current set only" onclick="document.forms.delabc.submit();"/>
                            <input type="button" id="upset" name="updateMySet" value="Update" title="updates the current set only" onclick="updateSet();"/>
                        </c:if>
                        <div id="success_info"></div>
                    </span>
                </fieldset>
            </div>
        </td>
    </tr>
    <tr>
        <td colspan="4">
            <div id="link-panel" class="ui-widget-header">
                <!-- place holder for dynamically generated url -->
            </div>
        </td>
        <td> 
            <%-- todo to save set as txt file ala ASS link --%>
            
        </td>
    </tr>
</table>

<form name="delxyz" method="post" action="deleteviewset.htm">
    <input type="hidden" name="id" id="stsvest" value="<c:out value="${model.armourSet.id}"/>"/>
</form>
<c:if test="${model.ss != null}">
    <form name="delabc" method="post" action="deleteviewset.htm">
        <input type="hidden" name="id" value="<c:out value="${model.ss.id}"/>"/>
    </form>
</c:if>