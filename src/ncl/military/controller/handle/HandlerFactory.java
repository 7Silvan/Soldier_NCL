package ncl.military.controller.handle;

import ncl.military.controller.handle.executors.SoldierExecutor;
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
    
    public Handlable getHandler(DAO dao, Map<String, Object> params) {
        //if (executors.containsKey(servletPath))
        return new Handler(dao, new SoldierExecutor(), );
    }

    public static Handlable getHandler(DAO dao, String action) {
    }
}
