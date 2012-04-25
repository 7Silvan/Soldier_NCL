package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;
import ncl.military.entity.Soldier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Silvan
 * Date: 25.04.12
 * Time: 5:39
 */
public class AllUnitsOfLocationGetter extends Executor {

    public AllUnitsOfLocationGetter(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();

        String unitIdMatch = (String) params.get("queriedUnitId");
        List<Soldier> soldierList = getDao().getSoldiersOfUnit(unitIdMatch);
        result.put("listOfSoldiers", soldierList);

        return result;
    }
}
