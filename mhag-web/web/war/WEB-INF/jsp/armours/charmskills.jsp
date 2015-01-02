<%@include file="../includes.jsp" %>
<script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>
<script type="text/javascript" charset="UTF-8">
    $(function() {
        $(".charm-select").change(function() {

            var sourceId = $(this).attr('id');
            var targetId = 'target' + sourceId;
            var selectedOpVal = $(this).find("option:selected").val();
            // make sure we clear the second whenever the first changes
            if(sourceId == 'charmid1') {
                if( $('#charmid2') ) {
                    // clear this completely
                    $('#sourcecharmid2').html("");
                    $('#targetcharmid2').html("");
                    buildCharmSkillsTable();
                }
            }
            if(selectedOpVal == '---') {
                // clear the points
                $('#' + targetId).html("");
                buildCharmSkillsTable();
            }
            if(selectedOpVal != '---') {
                // need to ensure the the other skill selects are updated, so whenever we change a skill
                var rankVal = $('#lRankId').val();

                var numSlotsVal = $('#talismanselect').find("option:selected").val();
                $.get('charmpoints.htm', {skillId: selectedOpVal, source: sourceId, rank: rankVal, numSlots: numSlotsVal}, function(data) {
                    $('#' + targetId).html(data);
                });
            }

        });

        $('#charmid2').change(function() {

            // get the selected option and hide the option in the other select
            var charm1 = $('#charmid1').get()[0];
            // skill id
            var selectedOpVal = $(this).find("option:selected").val();

            // enable elments
            showAllOptions(charm1.options);
            hideOption(charm1, selectedOpVal);
        });
    });
</script>

<c:if test="${charmIndex == null}"><c:set var="charmIndex" value="${model.charmIndex}"/></c:if>

<select name="charmidx<c:out value="${charmIndex}"/>" id="charmid<c:out value="${charmIndex}"/>" class="charm-select">
    <option value="---" <c:if test="${selectedSkill == null}">selected</c:if>>---</option>
    <c:forEach var="c" items="${model.skills}">
        <option value="<c:out value="${c.skill.skillID}"/>" <c:if test="${c.skill.skillID == selectedSkill}">selected</c:if>>
            <c:out value="${c.skill.skillName}"/>
        </option>
    </c:forEach>
</select>