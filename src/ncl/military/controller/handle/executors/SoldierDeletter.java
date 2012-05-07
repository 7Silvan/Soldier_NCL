package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 03.05.12
 *          Time: 14:42
 */
public class SoldierDeletter extends Executor {

    public SoldierDeletter(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();

        String soldierIdMatch = (String) params.get("soldierIdMatch");

        result.put("success", getDao().deleteSoldierById(soldierIdMatch));

        result.put("listOfSoldiers", getDao().getTopOfSoldiers());

        return result;
    }
}
