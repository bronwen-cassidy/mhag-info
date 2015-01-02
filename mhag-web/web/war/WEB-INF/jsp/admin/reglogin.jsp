<%@include file="../includes.jsp" %>
<script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>

<c:choose>
    <c:when test="${!requestScope.loggedIn}">
        <form method="post" action="" enctype="multipart/form-data" accept-charset="UTF-8">
            <table class="ui-state-default" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                    <td><label for="dd-username">Username</label><input type="text" name="user_u" id="dd-username"/></td>
                    <td><label for="dd-password">Password</label><input type="password" name="user_p" id="dd-password"/></td>
                    <td>
                        <a href="javascript:reglogin('login');">Login</a>&nbsp;|&nbsp;
                        <a href="javascript:reglogin('register');">Register</a>
                    </td>
                </tr>
                <c:if test="${model.errorMsg != null}">
                    <tr>
                        <td colspan="3">
                            <div class="ui-state-error-text">
                                <c:out value="${model.errorMsg}"/>
                            </div>
                        </td>
                    </tr>
                </c:if>
            </table>
        </form>
    </c:when>
    <c:otherwise>
        <table class="ui-state-default" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td width="100%">
                    Logged In: <c:out value="${requestScope.username}"/>
                    <input type="hidden" id="userXnfFld" name="xid" value="<c:out value="${requestScope.userId}"/>"/>
                    <input type="hidden" id="nameXnfFld" name="xname" value="<c:out value="${requestScope.username}"/>"/>
                    <input type="button" name="lgout" class="ui-widget" style="float:right;" value="Logout" onclick="regLogout();"/>
                </td>
            </tr>
        </table>
    </c:otherwise>
</c:choose>