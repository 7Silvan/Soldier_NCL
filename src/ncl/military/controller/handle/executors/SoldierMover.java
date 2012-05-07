package ncl.military.controller.handle.executors;

import ncl.military.controller.handle.HandlerFactory;
import ncl.military.dao.DAO;

import javax.servlet.http.HttpSession;
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
        //result.put("subAction", params.get("subAction"));
        result.put("subAction", HandlerFactory.MOVE_SOLDIER);

        String soldierIdMatch = (String) params.get("soldierIdMatch");

        result.put("soldierIdMatch", soldierIdMatch);
        result.put("listOfSoldiers", getDao().getTopOfSoldiers());

        return result;
    }
}
