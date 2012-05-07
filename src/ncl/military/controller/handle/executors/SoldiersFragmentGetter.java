package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;
import ncl.military.entity.Soldier;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 03.05.12
 *          Time: 18:25
 */
public class SoldiersFragmentGetter extends Executor {

    public SoldiersFragmentGetter(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();

        String idMatch = (String) params.get("soldierIdMatch");
        Logger.getLogger("controller").debug("soldierIdMatch got : " + idMatch);

        // TODO make list of soldiers without the queried one
        List<Soldier> soldierList = getDao().getAllSoldiers();
        result.put("listOfSoldiers", soldierList);

        Soldier currentSoldier = getDao().getSoldierById(idMatch);
        result.put("queriedSoldier", currentSoldier);

        return result;
    }
}
