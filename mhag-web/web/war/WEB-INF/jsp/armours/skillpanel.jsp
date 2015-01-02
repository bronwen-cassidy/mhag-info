<%@include file="../includes.jsp" %>

<div id="preview-panel">
    <fieldset>
        <legend>Skills</legend>
            <table>
                <thead>
                    <tr>
                        <th>Skill Tree</th>
                        <th><img src="images/weapon.png" height="20px" width="20px"/></th>
                        <th><img class="mats-display" id="head_sks" src="images/head.png" height="20px" width="20px"/></th>
                        <th><img class="mats-display" id="chest_sks" src="images/chest.png" height="20px" width="20px"/></th>
                        <th><img class="mats-display" id="arm_sks" src="images/arms.png" height="20px" width="20px"/></th>
                        <th><img class="mats-display" id="waist_sks" src="images/waist.png" height="20px" width="20px"/></th>
                        <th><img class="mats-display" id="leg_sks" src="images/legs.png" height="20px" width="20px"/></th>
                        <th><img class="mats-display" id="talisman_sks" src="images/talisman.png" height="20px" width="20px"/></th>
                        <th>Total</th>
                        <th nowrap>Skills Induced</th>
                    </tr>
                </thead>
                <tbody id="skills-preview-tbbody">
                <%-- in here goes the dynamically generated skills information --%>                    
                </tbody>
            </table>
    </fieldset>
</div>

<div id="dialog">No Information available</div>

