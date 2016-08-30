<%@include file="../includes.jsp" %>
<script type="text/javascript" src="<c:url value="/scripts/functions.js"/>" charset="UTF-8"></script>

<div id="search-panel" class="ui-widget-container">

    <form name="searchSetsFrm" method="post" action="">

        <fieldset id="searchfrmid">
            <legend>Search Fields</legend>
            <table width="100%">
                <tr>
                    <td>
                        <label for="dd-owner">Owned By&nbsp;:&nbsp;</label>
                        <input type="text" id="dd-owner" name="ownerField"/>
                    </td>
                    <td>
                        <label for="dd-name">Set Name&nbsp;:&nbsp;</label>
                        <input type="text" id="dd-name" name="nameField"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span class="infolabel">Please select skills you want to search with:</span>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table width="100%">
                            <tr>
                                <td nowrap>
                                    <select name="skone" class="skill_sel" onchange="addSkills(this, 'selected_skills_1')">
                                        <option value="---">Please Select</option>
                                        <c:forEach var="sk" items="${model.skillEffects}" varStatus="indexer" end="60">
                                            <option value="<c:out value="${sk.effectName}"/>"><c:out value="${sk.effectName}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td nowrap>
                                    <select name="skone" class="skill_sel" onchange="addSkills(this, 'selected_skills_2')">
                                        <option value="---">Please Select</option>
                                        <c:forEach var="sk" items="${model.skillEffects}" varStatus="indexer" begin="61" end="121">
                                            <option value="<c:out value="${sk.effectName}"/>"><c:out value="${sk.effectName}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td nowrap>
                                    <select name="skone" class="skill_sel" onchange="addSkills(this, 'selected_skills_3')">
                                        <option value="---">Please Select</option>
                                        <c:forEach var="sk" items="${model.skillEffects}" varStatus="indexer" begin="122">
                                            <option value="<c:out value="${sk.effectName}"/>"><c:out value="${sk.effectName}"/></option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td id="selected_skills_1" nowrap>

                                </td>
                                <td id="selected_skills_2" nowrap>

                                </td>
                                <td id="selected_skills_3" nowrap>

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input id="dd-search-btn" type="button" name="search" value="Search" onclick="loadPage('0');"/>
                        <input id="dd-reset-btn" type="reset" name="reset" value="Reset" onclick="removeChecks();"/>
                    </td>
                </tr>
            </table>
        </fieldset>
    </form>
    <div id="search-results-panel" class="ui-widget-container">
    
    </div>
</div>