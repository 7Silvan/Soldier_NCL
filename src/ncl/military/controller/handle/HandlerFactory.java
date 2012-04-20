package ncl.military.controller.handle;

import ncl.military.controller.handle.executors.SubsOfSoldierGetter;
import ncl.military.controller.handle.executors.TopOfSoldiersGetter;
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

    // actions
    public static final String GET_ALL = "getAll";
    public static final String GET_TOP = "getTop";
    public static final String GET_SUBS_OF = "getSubsOf";

    // views
    public static final String VIEW_SOLDIER = "/viewSoldiers.jsp";

    public static Handlable getHandler(DAO dao, Map<String, Object> params) {

        Executable executable = null;
        String view = null;
        //if (executors.containsKey(servletPath))  // it's for cash

        if ((PATH_SOLDIER).equals((String) params.get("userPath"))) {
            view = VIEW_SOLDIER;
            if (params.get("action") == null) params.put("action", GET_TOP);
            if ((GET_TOP).equals((String) params.get("action")))
                executable = new TopOfSoldiersGetter(dao);
            if ((GET_SUBS_OF).equals((String) params.get("action")))
                executable = new SubsOfSoldierGetter(dao);
        }
        return new Handler(executable, (String) params.get("userPath"), (String) params.get("action"), view);
    }
}
