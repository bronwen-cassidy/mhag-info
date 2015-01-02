
function buildCharmSkillsTable() {

    var rankVal = $('#lRankId').val();

    var armours = [];
    $(".setup-select").each(function () {
        var selectedOp2 = $(this).find("option:selected").val();
        if (selectedOp2 != '---') {
            armours.push(selectedOp2);
        }
    });

    $(".point-select").each(function () {
        var selectedOpVal = $(this).find("option:selected").val();
        if (selectedOpVal != '---') {
            armours.push(selectedOpVal);
        }
    });

    var items = [];
    $(".skill-select").each(function () {
        var igvar = $(this).attr('disabled');
        if (!igvar) {
            var selectedOp = $(this).find("option:selected").val();
            if (selectedOp != '---') {
                items.push(selectedOp);
            }
        }
    });

    var armourStr2;
    var itemsStr2;

    if (items.length > 0) {
        itemsStr2 = items.join(',');
    }
    if (armours.length > 0) {
        armourStr2 = armours.join(',');
    }
    $.get('skills.htm', {charmx: itemsStr2, amx: armourStr2, rank: rankVal}, function(data) {
        $('#skills-preview-tbbody').html(data);
    });
    var bladeVal = $('#dd-blade:checked').val();
    if (!bladeVal) bladeVal = "G";
    var nameVal = $('#setName').val();

    var selectedOp = $("#talismanselect").find("option:selected").val();
    var pieces = selectedOp.split(':');
    var taliSlots = '0';
    var piece = pieces[0];
    if (piece != '---') {
        taliSlots = piece;
    }
    $.get('urlbuilder.htm', {charmx: itemsStr2, amx: armourStr2, rank: rankVal, type: bladeVal, name: nameVal, numCharmSlots: taliSlots}, function(data) {
        $('#link-panel').html(data);
    });
}

function buildResistencesTable() {
    var rankVal = $('#lRankId').val();

    var armours = [];
    $(".setup-select").each(function () {
        var selectedOp2 = $(this).find("option:selected").val();
        if (selectedOp2 != '---') {
            armours.push(selectedOp2);
        }
    });
    var armourStr2 = armours.join(',');
    // build the resitences table
    if (armours.length > 0) {
        $.get('resistences.htm', {amx: armourStr2, rank: rankVal}, function(data2) {
            $('#res-preview-tbbody').html(data2);
        });
    } else {
        $('#res-preview-tbbody').html("");
    }
}

function talisSelect(myElem) {

    var elemName = $(myElem).attr('name');
    var targetElemId = '#' + elemName + 'targetid';
    var selectedOp = $(myElem).find("option:selected").val();

    var pieces = selectedOp.split(':');
    var rankVal = $('#lRankId').val();

    // clear the slots
    $(targetElemId).html("");

    var pieceId = pieces[0];

    if (pieceId != '---') {
        var slots = pieces[1];

        $.get('jewels.htm', {id: pieceId, numSlots: slots, rank: rankVal, piece: elemName}, function(data) {
            $(targetElemId).html(data);
        });
    }
}

function assignAllSlots(numArmourSlots, selName, selId, selOptionIndex, linkedName) {
    for (var a = 0; a < numArmourSlots; a++) {
        var aSelId = 'as' + a + selName;
        var aSel = $('#' + aSelId).get()[0];
        if (aSelId != selId) {
            aSel.options[selOptionIndex].selected = true;
            aSel.name = aSel.name + linkedName;
            aSel.disabled = true;
            aSel.style.display = 'none';
        }
    }
}

function linkSlots(numSlots, slotsArray, selOptionIndex, linkedName) {
    for (var k = 0; k < numSlots; k++) {
        var availableSel = slotsArray[0];
        slotsArray.splice(0, 1);
        availableSel.options[selOptionIndex].selected = true;
        availableSel.name = availableSel.name + linkedName;
        availableSel.disabled = true;
        availableSel.style.display = 'none';
    }
}

function countAvailableSlots(numArmourSlots, selName) {
    var slotsAvailable = 0;
    for (var m = 0; m < numArmourSlots; m++) {
        var tempSelId = 'as' + m + selName;
        var tempSel = $('#' + tempSelId).get()[0];
        if (tempSel.selectedIndex == 0) {
            slotsAvailable++;
        }
    }
    return slotsAvailable;
}

function evaluateOptions(numArmourSlots, selName, selId) {
    var slotsAvailable = countAvailableSlots(numArmourSlots, selName);
    // all available slots are those with a --- and your own
    for (var h = 0; h < numArmourSlots; h++) {
        var mySelId = 'as' + h + selName;
        var mySel = $('#' + mySelId).get()[0];

        if (mySelId == selId) continue;

        // if the currently selected option uses 2 slots then leave 2 as an option
        var selOption = mySel.options[mySel.selectedIndex].value;
        var slotCount = slotsAvailable;
        if (selOption != '---') {
            slotCount = slotsAvailable + parseInt(selOption.split(":")[1]);
        }

        for (var f = 0; f < mySel.options.length; f++) {
            var op = mySel.options[f];
            var opVal = op.value;
            if (opVal == '---') continue;
            var slotsReq = parseInt(opVal.split(":")[1]);
            if (slotsReq > slotCount) {
                // hide the option
                op.style.display = 'none';
            }
        }
    }
}

function handleAutoGuard(elemId) {

    var selectOpText = $(elemId).find("option:selected").text();
    if (selectOpText) {
        selectOpText = selectOpText.replace(/^\s+|\s+$/g, '');
        if (selectOpText == 'Auto-Guard') {
            // clear the other fields
            $('#sourcecharmid2').html("");
            $('#targetcharmid2').html("");
            buildCharmSkillsTable();
        }
    }
}

function trim(elemTxt) {
    return elemTxt.replace(/^\s+|\s+$/g, '');
}

function showAllOptions(opts) {
    for (var i = 0; i < opts.length; i++) {
        opts[i].disabled = false;
        opts[i].style.display = 'block';
    }
}

function hideOption(sel, optVal) {
    for (var i = 0; i < sel.options.length; i++) {
        if (sel.options[i].value == optVal) {
            sel.options[i].disabled = true;
            sel.options[i].style.display = 'none';
        }
    }
}

function saveSet() {

    var name = $('#nameXnfFld').val();

    if(!name) {
        name = prompt("Please enter a name for all your saved sets", "No Name");
        if (name == null || name == "") {
            alert("Invalid name, Save Cancelled");
        }
    }

    if (name) {
        var data = $('#setInfoIdx').val();
        var nameVal = $('#setName').val();
        var setIdVal = $('#existingSetIdr').val();
        var uId = $('#userXnfFld').val();
        // line below does the same as $('#wepselect').find("option:selected").val(), ty jQuery
        var wepSlotOp = $('#wepselect').val();
        var numWepSlots = 0;
        if (wepSlotOp != '---') {
            numWepSlots = parseInt(wepSlotOp.split(":")[1]);
        }
        var skills = [];
        $(".calculated").each(function () {
            var skillText = $(this).html();
            skills.push(trim(skillText));
        });

        $.get('saveset.htm', {setdata: data, setName: nameVal, owner: name, setId: setIdVal, wepSlots: numWepSlots, uid: uId, skillList: skills}, function(data) {

            if (data.indexOf("Error") < 0) {
                $('#existingSetIdr').val(data);
                $('#stsvest').val(data);
                $('#delset').attr("style", "display:inline");
                $('#upset').attr("style", "display:inline");
                $('#success_info').html("Set Saved");
            } else {
                $('#success_info').html(data);
            }
        }, "text");
    }
}

function updateSet() {

    var setIdVal = $('#existingSetIdr').val();
    var data = $('#setInfoIdx').val();
    var nameVal = $('#setName').val();
    var wepSlotOp = $('#wepselect').find("option:selected").val();
    var numWepSlots = 0;
    if (wepSlotOp != '---') {
        numWepSlots = parseInt(wepSlotOp.split(":")[1]);
    }
    var skills = [];
    $(".calculated").each(function () {
        var skillText = $(this).html();
        skills.push(trim(skillText));
    });

    if (setIdVal) {
        $.get('updateset.htm', {setdata: data, setName: nameVal, id: setIdVal, wepSlots: numWepSlots, skillList: skills}, function(data) {
            if (data.indexOf('Error') != -1) {
                $('#success_info').html('Successfully updated');
            } else {
                $('#success_info').html(data);
            }
        })
    }
}

var searchDirArray = [];
searchDirArray[0] = 'asc';
searchDirArray[1] = 'asc';
searchDirArray[2] = 'asc';

function loadPage(pgeNum, action, searchVal, swapDirection) {

    var index = 0;
    if(searchVal == 'label') {
        index = 1;
    } else if (searchVal == 'num_up_votes') {
        index = 2;
    }

    if (swapDirection) {
        searchDirArray[index] = (searchDirArray[index] == 'asc') ? 'desc' : 'asc';
    }
    var dirVal = searchDirArray[index];

    if("list" == action) {
        loadPageList(pgeNum, searchVal, dirVal);
    } else {
        $('#search-results-panel').html("<div class=\"ui-state-default\">Loading...</div>");

        var set_name = $('#dd-name').val();
        var owner_name = $('#dd-owner').val();

        var skills = [];
        $(".skill-search").each(function () {
            if ($(this).attr('checked')) {
                var skillVal = $(this).val();
                skills.push(trim(skillVal));
            }
        });

        var skillsStr = "";
        // build the resitences table
        if (skills.length > 0) {
            skillsStr = skills.join(',');
        }

        $.get('listsetresults.htm', {setName:set_name,ownerName:owner_name,skillOps:skillsStr,first:pgeNum,actn:'search',orderByVal:searchVal,direction:dirVal }, function(data) {
            $('#search-results-panel').html(data);
        });
    }
}

function loadPageList(pgeNum, searchVal, dirVal) {
    $('#list-results-panel').html("<div class=\"ui-state-default\">Loading...</div>");

    $.get('listsetresults.htm', {first: pgeNum, actn: 'list',orderByVal: searchVal, direction:dirVal }, function(data) {
        $('#list-results-panel').html(data);
    });
}

function adminSearch(firstVal, searchVal) {
    var dirVal = 'asc';  // label  num_votes
    var index = 0;
    if(searchVal == 'label') {
        index = 1;
    } else if (searchVal == 'ip_address') {
        index = 2;
    }
    dirVal = searchDirArray[index];
    searchDirArray[index] = (dirVal == 'asc') ? 'desc' : 'asc';

    var ip = $('#dd-ipaddress').val();
    var own = $('#dd-owner').val();
    var skillS = $('#dd-skills').is(':checked') ? 'T' : 'F';

    $.get('searchresults.htm', {ipAddress:ip, owner:own, dupSkills:skillS,direction:dirVal,first:firstVal, orderBy:searchVal}, function(data) {
        $('#admin-set-content').html(data);
    });
}

function checkAll() {
    $('.input-checkbox').each(function() {
        $(this).attr('checked', !this.checked);
    });
}

function getElem(elemId) {
    if (document.all) {
        return document.all(elemId);
    }
    return document.getElementById(elemId);
}

function reglogin(regVal) {
    var usrNme = $('#dd-username').val();
    var psswd = $('#dd-password').val();

    $.get('reglogin.htm', {username: usrNme, password: psswd, register: regVal}, function(data){
        $('#reg-panel').html(data);
    });
}

function regLogout() {
    $.get('logout.htm', function(data){
        $('#reg-panel').html(data);
    });
}

function addVote(setId, userId) {
    $.get('vote.htm', {sId: setId, uId: userId, f:'add'}, function(data) {
        var votesArr = data.split(':');
        $('#ssnv' + setId).html(votesArr[0]);
        $('#ssdv' + setId).html(votesArr[1]);
    });
}

function removeVote(setId, userId) {
    $.get('vote.htm', {sId: setId, uId: userId, f:'remove'}, function(data) {
        var votesArr = data.split(':');
        $('#ssnv' + setId).html(votesArr[0]);
        $('#ssdv' + setId).html(votesArr[1]);
    });
}

function addSkills(elem, targetElemId) {
    var displayVal = elem.options[elem.selectedIndex].value;
    $('#' + targetElemId).append('<input type="checkbox" class="skill-search" value="' + displayVal +'" checked />' + displayVal + '<br/>');
}

function removeChecks() {
    $('#selected_skills_1').html("");
    $('#selected_skills_2').html("");
    $('#selected_skills_3').html("");
}

function displayOnSet(elem, selId, optVal) {
    var sel = $('#'+selId);
    setTimeout(function() {
        if(!elem.checked) {
            optVal = '---';
        }
        sel.find('option').each(function(){
            if($(this).val() == optVal){
                $(this).attr('selected','selected');
                sel.trigger('change');
                return false;
            }
        });
    }, 0);

}
