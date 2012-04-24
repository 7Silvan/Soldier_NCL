package ncl.military.controller.handle;

import ncl.military.controller.handle.executors.*;
import ncl.military.dao.DAO;

import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:52
 */
public class HandlerFactory {
    private static Map<String, Handlable> handlers;
    private static Map<String, Executable> executors;
    private static Map<String, String> config;


    // path
    public static final String PATH_SOLDIER = "/viewSoldiers";
    public static final String PATH_UNIT = "/viewUnits";
    public static final String PATH_LOCATION = "/viewLocations";

    // common actions
    public static final String GET_ALL = "getAll";
    public static final String GET_TOP = "getTop";
    public static final String GET_SEARCH_RESULTS = "getSearchResults";

    // actions for soldiers
    public static final String GET_SUBS_OF_SOLDIER = "getSubsOfSoldier";

    // actions for units
    public static final String GET_SOLDIERS_OF_UNIT = "getSoldiersOfUnit";

    // actions for locations
    public static final String GET_UNITS_OF_LOCATION = "getUnitsOfLocations";

    // type of view
    public static final String VIEW_TYPE_SOLDIER = "Soldiers";
    public static final String VIEW_TYPE_UNIT = "Units";
    public static final String VIEW_TYPE_LOCATION = "Locations";

    //view
    public static final String VIEW_MAIN = "/view.jsp";

    public static Handlable getHandler(DAO dao, Map<String, Object> params) {

        Executable executable = null;
        String view = null;
        //if (executors.containsKey(servletPath))  // it's for cash

        if ((PATH_SOLDIER).equals((String) params.get("userPath"))) {
            view = VIEW_MAIN;
            if (params.get("action") == null) params.put("action", GET_TOP);
            if ((GET_TOP).equals((String) params.get("action")))
                executable = new TopOfSoldiersGetter(dao);
            if ((GET_SUBS_OF_SOLDIER).equals((String) params.get("action")))
                executable = new SubsOfSoldierGetter(dao);
            if ((GET_SEARCH_RESULTS).equals((String) params.get("action")))
                executable = new SoldierSearcher(dao);
        }
        if ((PATH_UNIT).equals((String) params.get("userPath"))) {
            view = VIEW_MAIN;
            if (params.get("action") == null) params.put("action", GET_ALL);
            if ((GET_ALL).equals((String) params.get("action")))
                executable = new AllOfUnitsGetter(dao);
            if ((GET_SEARCH_RESULTS).equals((String) params.get("action")))
                executable = new UnitSearcher(dao);
            if ((GET_SOLDIERS_OF_UNIT).equals((String) params.get("action")))
                executable = new AllSoldiersOfOnitGetter(dao);
        }
        if ((PATH_LOCATION).equals((String) params.get("userPath"))) {
            view = VIEW_MAIN;
            if (params.get("action") == null) params.put("action", GET_ALL);
            if ((GET_ALL).equals((String) params.get("action")))
                executable = new AllOfLocationsGetter(dao);
            if ((GET_SEARCH_RESULTS).equals((String) params.get("action")))
                executable = new LocationSearcher(dao);
            if ((GET_SOLDIERS_OF_UNIT).equals((String) params.get("action")))
                executable = new AllSoldiersOfOnitGetter(dao);
        }
        return new Handler(executable, (String) params.get("userPath"), (String) params.get("action"), view);
    }
}
