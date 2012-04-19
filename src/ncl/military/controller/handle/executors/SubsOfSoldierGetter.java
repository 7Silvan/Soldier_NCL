package ncl.military.controller.handle.executors;

import ncl.military.controller.handle.Executable;
import ncl.military.dao.DAO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 19.04.12
 *          Time: 14:44
 */
public class SubsOfSoldierGetter implements Executable {
    private DAO dao;

    public SubsOfSoldierGetter(DAO dao) {
        this.dao = dao;
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();


        return result;
    }
}
