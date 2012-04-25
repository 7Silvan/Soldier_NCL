package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;
import ncl.military.entity.Soldier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 17:04
 */
public class AllSoldiersGetter extends Executor {

    public AllSoldiersGetter(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();

        List<Soldier> soldierList = getDao().getAllSoldiers();
        result.put("listOfSoldiers", soldierList);

        return result;
    }
}
