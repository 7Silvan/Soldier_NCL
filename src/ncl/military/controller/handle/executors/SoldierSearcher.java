package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;
import ncl.military.dao.searchtool.Filter;
import ncl.military.entity.Soldier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 24.04.12
 *          Time: 13:02
 */
public class SoldierSearcher extends Executor {

    public SoldierSearcher(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();

        List<Filter> filters = (List<Filter>) params.get("filterList");
        if (filters != null) {

        } else {
            List<Soldier> soldierList = getDao().getAllSoldiers();
            result.put("listOfSoldiers", soldierList);
        }

        return result;
    }
}
