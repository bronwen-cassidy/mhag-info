<%@include file="../includes.jsp" %>

<div id="resistence-panel">
    <fieldset>
        <legend>Resistences/Defense</legend>
            <table>
                <thead>
                    <tr>
                        <th>Armour Piece</th>
                        <th><img src="images/head.png" height="20px" width="20px"/></th>
                        <th><img src="images/chest.png" height="20px" width="20px"/></th>
                        <th><img src="images/arms.png" height="20px" width="20px"/></th>
                        <th><img src="images/waist.png" height="20px" width="20px"/></th>
                        <th><img src="images/legs.png" height="20px" width="20px"/></th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody id="res-preview-tbbody">
                <%-- in here goes the dynamically generated skills information --%>
                </tbody>
            </table>
    </fieldset>
</div>