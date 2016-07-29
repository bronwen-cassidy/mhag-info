<%@include file="../includes.jsp" %>
<script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>

 <script type="text/javascript" charset="UTF-8">

     function delRow(ssId, rowId) {
         $.get('deleteset.htm', {id:ssId}, function(data) {
             $('#' + rowId).attr("style", "display:none");
         });
     }

</script>

<c:choose>
    <c:when test="${empty model.sets}">
        <p align="center" class="ui-widget-header"> No Sets were found with given search criteria please try again </p>
    </c:when>
    <c:otherwise>

        <div id="list-results-panel" class="ui-widget-container">
            <c:import url="searchresults.jsp"/>
        </div>

    </c:otherwise>
</c:choose>