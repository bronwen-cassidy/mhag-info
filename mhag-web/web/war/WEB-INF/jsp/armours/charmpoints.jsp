<%@include file="../includes.jsp" %>

<script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>

<script type="text/javascript" charset="UTF-8">

    $(function() {

        // get the first select if it is autoguard build the skills table
        handleAutoGuard('#charmid1');

        $("#pointid1").change(function() {

            var rankVal = $('#lRankId').val();

            var selectedOpVal = $(this).find("option:selected").val();
            var selectedSkillId = selectedOpVal.split(':')[0];
            var numSlotsVal = $('#talismanselect').find("option:selected").val();

            // populate the next skill select only if this is not autoguard read this information from the option text
            var selectOpText = $(this).find("option:selected").text();
            selectOpText = selectOpText.replace(/^\s+|\s+$/g, '');
            var targetExists = $('#charmid2').find("option:selected").val();
            if (selectOpText != 'Auto-Guard' && !targetExists) {

				var sourceVal = 'pointid1';
                $.get('charmskills.htm', {index: selectedSkillId, numSlots: numSlotsVal, source: sourceVal, rank: rankVal, charmIndex : "2"}, function(data2) {
                    $('#sourcecharmid2').html(data2);
                });
            }

            buildCharmSkillsTable();
        });

        $("#pointid2").change(function() {

            var rankVal = $('#lRankId').val();
            // calculate the skills
            buildCharmSkillsTable();
        });
    });
</script>

<c:if test="${charmIndex == null}"><c:set var ="charmIndex" value="${model.charmIndex}"/></c:if>
<c:if test="${points == null}"><c:set var ="points" value="${model.points}"/></c:if>
<c:if test="${targetPoint == null}"><c:set var ="targetPoint" value="0"/></c:if>

<select name="pointidx<c:out value="${charmIndex}"/>" id="pointid<c:out value="${model.charmIndex}"/>" class="point-select">
    <c:forEach var="cPoint" items="${points}">
        <option class="point-aligned" value="<c:out value="${cPoint.key}"/>"
                <c:if test="${cPoint.skillPoint == targetPoint}">selected</c:if>>
            <c:out value="${cPoint.skillPoint}"/>
        </option>
    </c:forEach>
</select>
