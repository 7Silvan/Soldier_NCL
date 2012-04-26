package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 26.04.12
 *          Time: 16:39
 */
public class LocationChanger extends Executor {

    public LocationChanger(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        // TODO implement
//        List<Soldier> soldierList = getDao().getAllSoldiers();
//        result.put("listOfSoldiers", soldierList);

        return result;
    }
}
