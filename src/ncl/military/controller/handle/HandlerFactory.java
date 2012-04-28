package ncl.military.controller.handle;

import ncl.military.controller.handle.executors.*;
import ncl.military.dao.DAO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:52
 */
public class HandlerFactory {
    private static Map<String, Handlable> handlers;
    private static Map<String, Executable> executors = new HashMap<String, Executable>();
    private static Map<String, String> config;


    // path
    public static final String PATH_SOLDIER = "/viewSoldiers";
    public static final String PATH_UNIT = "/viewUnits";
    public static final String PATH_LOCATION = "/viewLocations";

    // common actions
    public static final String GET_ALL = "getAll";
    public static final String GET_TOP = "getTop";
    public static final String EDIT = "edit";
    public static final String GET = "getOne";
    public static final String GET_SEARCH_RESULTS = "getSearchResults";

    // actions for soldiers
    public static final String GET_SUBS_OF_SOLDIER = "getSubsOfSoldier";
    public static final String ADD_SOLDIER = "addSoldier";

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
    public static final String VIEW_EDIT = "/editor.jsp";
    private static final String VIEW_HOME = "/index.jsp";

    public static Handlable getHandler(DAO dao, Map<String, Object> params) {

        Executable executable = null;
        String view = null;
        if ((PATH_SOLDIER).equals((String) params.get("userPath"))) {
            view = VIEW_MAIN;
            if (params.get("action") == null) params.put("action", GET_TOP);
            if ((GET_TOP).equals((String) params.get("action"))) {
                executable = executors.get(TopOfSoldiersGetter.class.getName());
                if (executable == null) {
                    executable = new TopOfSoldiersGetter(dao);
                    executors.put(TopOfSoldiersGetter.class.getName(), executable);
                }
            }
            if ((GET_SUBS_OF_SOLDIER).equals((String) params.get("action"))) {
                executable = executors.get(SubsOfSoldierGetter.class.getName());
                if (executable == null) {
                    executable = new SubsOfSoldierGetter(dao);
                    executors.put(SubsOfSoldierGetter.class.getName(), executable);
                }
            }
            if ((GET_SEARCH_RESULTS).equals((String) params.get("action"))) {
                executable = executors.get(SoldierSearcher.class.getName());
                if (executable == null) {
                    executable = new SoldierSearcher(dao);
                    executors.put(SoldierSearcher.class.getName(), executable);
                }
            }
            if ((GET_SOLDIERS_OF_UNIT).equals((String) params.get("action"))) {
                executable = executors.get(AllSoldiersOfUnitGetter.class.getName());
                if (executable == null) {
                    executable = new AllSoldiersOfUnitGetter(dao);
                    executors.put(AllSoldiersOfUnitGetter.class.getName(), executable);
                }
            }
            if ((ADD_SOLDIER).equals((String) params.get("action"))) {
                view = VIEW_EDIT;
                executable = executors.get(SoldierAdder.class.getName());
                if (executable == null) {
                    executable = new SoldierAdder(dao);
                    executors.put(SoldierAdder.class.getName(), executable);
                }
            }
            if ((EDIT).equals((String) params.get("action"))) {
                view = VIEW_EDIT;
                executable = executors.get(SoldierChanger.class.getName());
                if (executable == null) {
                    executable = new SoldierChanger(dao);
                    executors.put(SoldierAdder.class.getName(), executable);
                }
            }
        }
        if ((PATH_UNIT).equals((String) params.get("userPath"))) {
            view = VIEW_MAIN;
            if (params.get("action") == null) params.put("action", GET_ALL);
            if ((GET_ALL).equals((String) params.get("action"))) {
                executable = executors.get(AllUnitsGetter.class.getName());
                if (executable == null) {
                    executable = new AllUnitsGetter(dao);
                    executors.put(AllUnitsGetter.class.getName(), executable);
                }
            }
            if ((GET_SEARCH_RESULTS).equals((String) params.get("action"))) {
                executable = executors.get(UnitSearcher.class.getName());
                if (executable == null) {
                    executable = new UnitSearcher(dao);
                    executors.put(UnitSearcher.class.getName(), executable);
                }
            }
            if ((GET_UNITS_OF_LOCATION).equals((String) params.get("action"))) {
                executable = executors.get(AllUnitsOfLocationGetter.class.getName());
                if (executable == null) {
                    executable = new AllUnitsOfLocationGetter(dao);
                    executors.put(AllUnitsOfLocationGetter.class.getName(), executable);
                }
            }
            if ((EDIT).equals((String) params.get("action"))) {
                view = VIEW_EDIT;
                executable = executors.get(UnitChanger.class.getName());
                if (executable == null) {
                    executable = new UnitChanger(dao);
                    executors.put(UnitChanger.class.getName(), executable);
                }
            }
        }
        if ((PATH_LOCATION).equals((String) params.get("userPath"))) {
            view = VIEW_MAIN;
            if (params.get("action") == null) params.put("action", GET_ALL);
            if ((GET_ALL).equals((String) params.get("action"))) {
                executable = executors.get(AllLocationsGetter.class.getName());
                if (executable == null) {
                    executable = new AllLocationsGetter(dao);
                    executors.put(AllLocationsGetter.class.getName(), executable);
                }
            }
            if ((GET_SEARCH_RESULTS).equals((String) params.get("action"))) {
                executable = executors.get(LocationSearcher.class.getName());
                if (executable == null) {
                    executable = new LocationSearcher(dao);
                    executors.put(LocationSearcher.class.getName(), executable);
                }
            }
            if ((EDIT).equals((String) params.get("action"))) {
                view = VIEW_EDIT;
                executable = executors.get(LocationChanger.class.getName());
                if (executable == null) {
                    executable = new LocationChanger(dao);
                    executors.put(LocationChanger.class.getName(), executable);
                }
            }
        }
        if (executable == null)
            throw new IllegalStateException("Did not matched executor");
        return new Handler(executable, (String) params.get("userPath"), (String) params.get("action"), view);
    }
}
