package ncl.military.controller.handle.executors;

import ncl.military.controller.handle.Executable;
import ncl.military.dao.DAO;
import ncl.military.entity.Soldier;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 17:04
 */
public class AllSoldierGetter implements Executable{

    private DAO dao;
    
    public AllSoldierGetter(DAO dao) {
        this.dao = dao;
    }
    
    public Map<String, Object> execute(Map<String, Object> params) {
           Map<String, Object> result = new HashMap<String, Object>();

        List<Soldier> soldierList = dao.getAllSoldiers();
        result.put("listOfSoldiers", soldierList);

        return result;
    }
}
