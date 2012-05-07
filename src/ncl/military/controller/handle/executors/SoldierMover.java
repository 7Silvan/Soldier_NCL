package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Silvan
 * Date: 07.05.12
 * Time: 8:56
 */
public class SoldierMover extends Executor {
    public SoldierMover(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();

        String action = (String) params.get("action");

        String soldierIdMatch = (String) params.get("soldierIdMatch");

        result.put("soldierIdMatch", soldierIdMatch);
        result.put("listOfSoldiers", getDao().getAllSoldiers());

        return result;
    }
}
