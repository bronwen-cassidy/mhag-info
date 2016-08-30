<%@include file="../includes.jsp" %>

<input type="hidden" id="setInfoIdx" value="<c:out value="${model.urlValue}"/>"/>
Share your set &nbsp;:&nbsp;
<a class="armor-link" href="<c:out value="${model.urlValue}"/>"><c:out value="${model.urlValue}" escapeXml="false"/></a>