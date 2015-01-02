/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.mhag;

import org.mhag.sets.Page;
import org.mhag.sets.SavedSet;
import org.mhag.sets.Serviceable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-Jul-2011 22:07:26
 */
public class MhagMainFormController extends MultiActionController {

    public ModelAndView armourSetHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = buildModel(request);
        // default information for the first views
        return new ModelAndView("main", "model", model);
    }

    public ModelAndView armourHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // information aquired from the user                            
        Map<String, Object> model = buildModel(request);
        return new ModelAndView("armour", "model", model);
    }

    public ModelAndView urlDisplayHander(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // information aquired from the user
        Map<String, Object> model = new HashMap<String, Object>();

        int numTaliSlots = ServletRequestUtils.getIntParameter(request, "numCharmSlots", 0);

        String armours = ServletRequestUtils.getStringParameter(request, "amx");
        String[] armourIds = StringUtils.delimitedListToStringArray(armours, ",");

        String charms = ServletRequestUtils.getStringParameter(request, "charmx");
        String[] charmIds = StringUtils.delimitedListToStringArray(charms, ",");

        String type = ServletRequestUtils.getStringParameter(request, "type", "blade");
        int rank = evaluateRank(request);

        type = type.equals("blade") ? "B" : "G";

        String name = ServletRequestUtils.getStringParameter(request, "name", UNNAMED_SET);

        String value = mhagFacade.buildUrl(name.intern(), charmIds, armourIds, rank, type.equals("B"), numTaliSlots);
        model.put("urlValue", value);
        model.put("name", name);
        return new ModelAndView("urlbuilder", "model", model);
    }

    private int evaluateRank(HttpServletRequest request) {
        return ServletRequestUtils.getIntParameter(request, "rank", mhagFacade.getDefaultRank());
    }

    public ModelAndView resDisplayHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        String armours = ServletRequestUtils.getStringParameter(request, "amx");
        String[] armourIds = StringUtils.delimitedListToStringArray(armours, ",");
        int rank = evaluateRank(request);

        boolean evaluate = !(armourIds.length == 1 && armourIds[0].endsWith(MhagFacade.TALI));

        if (evaluate) {
            final Map<String, List<Cell>> resistences = mhagFacade.buildResistences(armourIds, rank);
            model.put("resistences", resistences);
        }

        // note order of the res = Fire/Water/Ice/Thunder/Dragon        
        return new ModelAndView("resistences", "model", model);
    }

    public ModelAndView charmSkillDisplayHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int rank = evaluateRank(request);
        int numSlots = extractNumSlots(request);
        int index = ServletRequestUtils.getIntParameter(request, "index", 0);
        String charmIndex = ServletRequestUtils.getStringParameter(request, "charmIndex", "1");
        String selectedTalisman = ServletRequestUtils.getStringParameter(request, "selectedOption", null);

        String exclude = null;
        if ("2".equals(charmIndex)) exclude = "Auto-Guard";
        if (!"---".equals(selectedTalisman)) exclude = "Auto-Guard";

        Map<String, Object> model = new HashMap<String, Object>();
        final List<CharmSkill> skills = mhagFacade.getSkills(rank, numSlots, index, exclude);
        model.put("skills", skills);
        model.put("charmIndex", charmIndex);
        return new ModelAndView("charmskills", "model", model);
    }

    public ModelAndView skillDisplayHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //54:2:head,122:1:head,103:1:arm,50:0:talisman:42
        String charms = ServletRequestUtils.getStringParameter(request, "charmx");
        if (charms == null) {
            charms = ServletRequestUtils.getStringParameter(request, "charmIds");
        }
        // -3:0:wep,7:2:0,4:1:2,29:1:3,42:0:talisman
        String armours = ServletRequestUtils.getStringParameter(request, "amx");
        if (armours == null) {
            armours = ServletRequestUtils.getStringParameter(request, "armourIds");
        }

        final Map<String, Object> result = mhagFacade.buildSkills(
                StringUtils.delimitedListToStringArray(charms, ","),
                StringUtils.delimitedListToStringArray(armours, ",")
        );

        Map<SkillKey, Map<String, Cell>> calculatedSet = (Map<SkillKey, Map<String, Cell>>) result.get(MhagFacade.SET_SKILLS_KEY);

        Map<String, Object> model = new HashMap<String, Object>();
        model.putAll(result);
        TorsoUpSkill s = new TorsoUpSkill(-1, "?");
        SkillKey k = new SkillKey(s, "?");
        if (calculatedSet.get(k) != null) {
            model.put("Torso", Boolean.TRUE);
        }
        // default information for the first views
        return new ModelAndView("skills", "model", model);
    }

    public ModelAndView jewelDisplayHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int numSlots = ServletRequestUtils.getIntParameter(request, "numSlots", 0);
        int rank = evaluateRank(request);
        String piece = ServletRequestUtils.getStringParameter(request, "piece", "charm");

        JewelWrapper[] slots = new JewelWrapper[numSlots];
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("piece", piece);

        if ("talisman".equals(piece)) {
            model.put("skills", mhagFacade.getSkills(rank, numSlots, -1, null));
        }

        List<JewelWrapper> jewels = mhagFacade.getJewels(rank, numSlots, getLanguage(request));
        model.put("jewels", jewels);
        model.put("slots", slots);
        model.put("numSlots", numSlots);
        // default information for the first views
        return new ModelAndView("jewels", "model", model);
    }

    public ModelAndView charmPointsDisplayHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String skillIdStr = ServletRequestUtils.getStringParameter(request, "skillId", "-1");
        String sourceElemIndex = ServletRequestUtils.getStringParameter(request, "source", "charmid1");
        int rank = evaluateRank(request);

        int numSlots = extractNumSlots(request);
        // 2 string values coming through skillId
        String charmIndex = "1";
        if (sourceElemIndex.contains("2")) charmIndex = "2";

        Map<String, Object> model = new HashMap<String, Object>();
        final int skillId = Integer.parseInt(skillIdStr);
        final int[] maxManVals = mhagFacade.getCharmPoints(rank, skillId, numSlots, Integer.parseInt(charmIndex));

        List<CharmPoint> points = new ArrayList<CharmPoint>();
        for (int i = maxManVals[0]; i >= maxManVals[1]; i--) {
            String key = skillId + ":" + i + ":charm";
            if (i == 0) key = "---";
            points.add(new CharmPoint(i, skillId, key));
        }
        model.put("charmIndex", charmIndex);
        model.put("points", points);
        // default information for the first views
        return new ModelAndView("charmpoints", "model", model);
    }

    public ModelAndView listSetsResultsHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, String> parameters = new HashMap<String, String>();
        doSetSearch(request, model, parameters);
        model.put("action", ServletRequestUtils.getStringParameter(request, "actn", "list"));
        return new ModelAndView("listsetresults", "model", model);
    }

    /**
     * Main entry view for the database set list view
     * @param request - the request
     * @param response - the response
     * @return - ModelAndView
     * @throws Exception - unknown exception
     */
    public ModelAndView listSetsHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, String> parameters = new HashMap<String, String>();

        doSetSearch(request, model, parameters);
        model.put("action", "list");
        return new ModelAndView("listsets", "model", model);
    }

    private void doSetSearch(HttpServletRequest request, Map<String, Object> model, Map<String, String> parameters) {
        String skillsParam = ServletRequestUtils.getStringParameter(request, "skillOps", null);
        String setName = ServletRequestUtils.getStringParameter(request, "setName", null);
        String ownerName = ServletRequestUtils.getStringParameter(request, "ownerName", null);
        String orderBy = ServletRequestUtils.getStringParameter(request, "orderByVal", null);
        String direction = ServletRequestUtils.getStringParameter(request, "direction", "desc");
        int first = ServletRequestUtils.getIntParameter(request, "first", 0);

        if (StringUtils.hasText(setName)) {
            parameters.put("label", setName);
        }
        if (StringUtils.hasText(ownerName)) {
            parameters.put("owner", ownerName);
        }
        String[] skills = skillsParam != null ? StringUtils.delimitedListToStringArray(skillsParam, ",") : new String[0];
        addResults(model, parameters, skills, first, Page.MAX_RESULTS, orderBy, direction);
    }

    public ModelAndView adminSearchSetsHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, String> parameters = new HashMap<String, String>();

        String ownerName = ServletRequestUtils.getStringParameter(request, "owner", null);
        String ipAddress = ServletRequestUtils.getStringParameter(request, "ipAddress", null);
        String orderBy = ServletRequestUtils.getStringParameter(request, "orderBy", null);
        String direction = ServletRequestUtils.getStringParameter(request, "direction", "desc");
        boolean duplicateSkillsSearch = ServletRequestUtils.getStringParameter(request, "dupSkills", "F").equals("T");

        if (StringUtils.hasText(ipAddress)) parameters.put("ip_address", ipAddress);
        if (StringUtils.hasText(ownerName)) parameters.put("owner", ownerName);

        if (orderBy == null && parameters.isEmpty()) orderBy = "id";
        List<SavedSet> sets = service.searchSets(parameters, new String[0], orderBy, direction);
        if (duplicateSkillsSearch) {
            sets = MhagUtils.findDuplicateSkills(sets);
        } else {
            model.put("weightColumn", "No");
        }
        
        model.put("sets", sets);
        model.put("orderBy", orderBy);

        return new ModelAndView("searchresults", "model", model);
    }

    public ModelAndView manageSetsHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        HashMap<String, String> parameters = new HashMap<String, String>();

        List<SavedSet> sets = service.searchSets(parameters, new String[0], "id", "desc");

        model.put("weightColumn", "No");
        model.put("sets", sets);
        return new ModelAndView("managesets", "model", model);
    }

    public ModelAndView searchSetsHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("skillEffects", mhagFacade.getSkillEffects(getLanguage(request)));
        model.put("action", "search");
        return new ModelAndView("searchsets", "model", model);
    }

    public ModelAndView setDisplayHander(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();
        String setData = ServletRequestUtils.getStringParameter(request, "s", null);
        if (setData == null) return armourSetHandler(request, response);

        buildArmourSetMode(request, model, setData, null);

        return new ModelAndView("viewset", "model", model);
    }

    public ModelAndView editSavedSetHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        String setData = ServletRequestUtils.getStringParameter(request, "s", null);
        Long id = ServletRequestUtils.getLongParameter(request, "id");

        SavedSet ss = service.findById(id);
        buildArmourSetMode(request, model, setData, id);
        if (ss != null) model.put("ss", ss);
        return new ModelAndView("editsavedset", "model", model);
    }

    public ModelAndView deleteViewSetHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, "id");
        if (id != null) {
            try {
                service.delete(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ModelAndView(new RedirectView("main.htm"));
    }

    public ModelAndView armourPiecesHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int rank = evaluateRank(request);
        boolean blade = "blade".equals(ServletRequestUtils.getStringParameter(request, "blade", "blade"));
        boolean female = "female".equals(ServletRequestUtils.getStringParameter(request, "female", "male"));
        String opts = ServletRequestUtils.getStringParameter(request, "opt", null);
        String piece = ServletRequestUtils.getStringParameter(request, "piece", null);
        //id:numslots:piece
        String[] vals = StringUtils.delimitedListToStringArray(opts, ":");
        Map<String, Object> model = new HashMap<String, Object>();
        final int part = MhagFacade.getIndex(piece);
        model.put("armourPieces", mhagFacade.getArmours(rank, blade, female, part));
        if (vals.length > 1) {
            model.put("selectedPiece", mhagFacade.getArmour(Integer.parseInt(vals[0]), part, female));
        }
        return new ModelAndView("armouropts", "model", model);
    }

    private void addResults(Map<String, Object> model, Map<String, String> parameters, String[] skills, int first,
                            int end, String orderBy, String direction) {
        if(!StringUtils.hasText(orderBy)) {
            orderBy = null;
        }
        if (orderBy == null && parameters.isEmpty() && skills.length < 1) {
            orderBy = "id";
        }

        final Page<SavedSet> page = service.searchSets(parameters, skills, first, end, orderBy, direction);
        final List<SavedSet> sets = new ArrayList<SavedSet>(page.getData());

        if (parameters.isEmpty() && skills.length < 1) {
            model.put("weightColumn", "No");
        }
        model.put("page", page);
        model.put("sets", sets);
        model.put("currentPage", first);
        model.put("orderBy", orderBy);
        model.put("direction", "asc".equals(direction) ? "desc" : "asc");
    }

    private int getLanguage(HttpServletRequest request) {
        String acceptLang = request.getHeader("Accept-Language");
        Locale locale = request.getLocale();
        int language = 0;
        boolean japanese = false;
        if (acceptLang != null) japanese = acceptLang.contains("ja");
        if (locale != null) japanese = locale.toString().contains("ja");
        if (japanese) {
            language = 1;
        }
        return language;
    }

    private int extractNumSlots(HttpServletRequest request) throws ServletRequestBindingException {
        String numSlotsStr = ServletRequestUtils.getStringParameter(request, "numSlots");
        int numSlots = 0;
        if (!"---".equals(numSlotsStr)) {
            String[] values = StringUtils.delimitedListToStringArray(numSlotsStr, ":");
            if (values.length > 0) numSlots = Integer.parseInt(values[0]);
        }
        return numSlots;
    }

    private void buildArmourSetMode(HttpServletRequest request, Map<String, Object> model, String setData, Long id) {
        // remove the name and add it back in again (if it is not there add one
        String name = UNNAMED_SET;
        if (setData.contains(":")) {
            name = setData.substring(0, setData.lastIndexOf(": "));
            setData = setData.substring(name.length() + 2);
        }

        String[] data = StringUtils.delimitedListToStringArray(setData, ".");
        boolean female = false;
        boolean blade = true;
        int rank = mhagFacade.getDefaultRank();

        if (data.length > 1) {
            blade = data[1].equals("B");
            String rankValue = data[0];
            if (rankValue.equals("H")) rank = 1;
            else if (rankValue.equals("L")) rank = 0;
            else rank = 2;
        }

        model.put("gender", "M");
        model.put("type", blade ? "B" : "G");
        model.put("rank", rank);
        model.put("ranks", mhagFacade.getRankValues());
        model.put("head", mhagFacade.getArmours(rank, blade, female, 0));
        model.put("chest", mhagFacade.getArmours(rank, blade, female, 1));
        model.put("arms", mhagFacade.getArmours(rank, blade, female, 2));
        model.put("waist", mhagFacade.getArmours(rank, blade, female, 3));
        model.put("legs", mhagFacade.getArmours(rank, blade, female, 4));
        model.put("highRank", rank == 0 ? Boolean.FALSE : Boolean.TRUE);

        // see if we can get the language from the request
        int language = getLanguage(request);

        String parsedName = name;
        if (parsedName.contains(":")) {
            parsedName = StringUtils.replace(parsedName, ":", ";");
        }

        ArmourSet armourSet = mhagFacade.buildArmourSet(parsedName + ": " + setData, language);
        armourSet.setName(name.trim());
        if (id != null) {
            armourSet.setId(id);
        }
        int slots = armourSet.getNumCharmSlots();
        model.put("skills", mhagFacade.getSkills(rank, slots, -1, null));
        model.put("jewels", mhagFacade.getJewels(rank, 3, getLanguage(request)));
        model.put("armourSet", armourSet);
    }

    private Map<String, Object> buildModel(HttpServletRequest request) {
        int rank = evaluateRank(request);
        boolean blade = "blade".equals(ServletRequestUtils.getStringParameter(request, "blade", "blade"));
        boolean female = "female".equals(ServletRequestUtils.getStringParameter(request, "female", "male"));
        int numSlots = ServletRequestUtils.getIntParameter(request, "numSlots", 0);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("head", mhagFacade.getArmours(rank, blade, female, 0));
        model.put("chest", mhagFacade.getArmours(rank, blade, female, 1));
        model.put("arms", mhagFacade.getArmours(rank, blade, female, 2));
        model.put("waist", mhagFacade.getArmours(rank, blade, female, 3));
        model.put("legs", mhagFacade.getArmours(rank, blade, female, 4));
        model.put("ranks", mhagFacade.getRankValues());
        model.put("highRank", rank == 0 ? Boolean.FALSE : Boolean.TRUE);
        model.put("skills", mhagFacade.getSkills(rank, numSlots, -1, null));
        model.put("gender", female ? "female" : "male");
        model.put("charmIndex", "1");
        return model;
    }

    public void setMhagFacade(MhagFacade mhagFacade) {
        this.mhagFacade = mhagFacade;
    }

    public void setService(Serviceable service) {
        this.service = service;
    }

    private MhagFacade mhagFacade;
    private Serviceable service;
    public static final String UNNAMED_SET = "Unnamed Set ";
}
