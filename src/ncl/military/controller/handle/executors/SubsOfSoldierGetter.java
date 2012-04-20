package ncl.military.controller.handle.executors;

import ncl.military.controller.handle.Executable;
import ncl.military.dao.DAO;
import ncl.military.entity.Soldier;

import java.util.HashMap;
import java.util.List;
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

        String idMatch = (String) params.get("queriedSoldierId");
        List<Soldier> soldierList = dao.getSubSoldiersOfByID(idMatch);
        result.put("listOfSoldiers", soldierList);
        
        return result;
    }
}
