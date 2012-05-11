package ncl.military.controller.handle.executors;

import ncl.military.controller.handle.HandlerFactory;
import ncl.military.dao.DAO;
import ncl.military.dao.tools.Alias;
import ncl.military.dao.tools.EntityValue;
import ncl.military.entity.Soldier;
import ncl.military.entity.Unit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 26.04.12
 *          Time: 16:38
 */
public class SoldierChanger extends Executor {

    public SoldierChanger(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();

        String action = (String) params.get("action");
        String subAction = (String) params.get("subAction");
        String soldierIdMatch = (String) params.get("soldierIdMatch");
        Boolean setTop = false;

        List<Soldier> soldierList = getDao().getNotSubsSoldiers(soldierIdMatch);
        List<Unit> unitList = getDao().getAllUnits();

        if (((HandlerFactory.EDIT_SOLDIER).equals(action) || (HandlerFactory.MOVE_UNDER_THIS_SOLDIER).equals(action)) && soldierIdMatch != null) {

            List<EntityValue> values = getValuesOfSolider(params);

            if (values.size() != 0) {
                result.put("success", getDao().setSoldierAttributes(soldierIdMatch, values));
                if (subAction != null && (HandlerFactory.MOVE_UNDER_THIS_SOLDIER).equals(subAction)) {
                    subAction = null;
                    soldierIdMatch = null;
                }
            }

            if ((HandlerFactory.MOVE_UNDER_THIS_SOLDIER).equals(action)) {
                String commanderId = (String) params.get(Alias.SOLDIER_COMMANDER.getLabelAsQueried());
                action = HandlerFactory.GET_SUBS_OF_SOLDIER;
                result.put("action", action);

                soldierList = getDao().getSubSoldiersOfByID(commanderId);

                List<Soldier> hierarchyList = getDao().getHierarchy(commanderId);
                result.put("hierarchyList", hierarchyList);

                Soldier currentSoldier = getDao().getSoldierById(commanderId);
                result.put("queriedSoldier", currentSoldier);
            }
        }

        Soldier soldier = getDao().getSoldierById(soldierIdMatch);

        result.put("listOfSoldiers", soldierList);
        result.put("listOfUnits", unitList);

        result.put("queriedSoldier", soldier);
        result.put("subAction", subAction);
        result.put("soldierIdMatch", soldierIdMatch);
        return result;
    }
}
