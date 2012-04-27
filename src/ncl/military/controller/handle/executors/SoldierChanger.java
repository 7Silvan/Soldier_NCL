package ncl.military.controller.handle.executors;

import ncl.military.controller.handle.HandlerFactory;
import ncl.military.dao.DAO;
import ncl.military.dao.tools.EntityValue;
import ncl.military.entity.Soldier;
import ncl.military.entity.Unit;

import java.util.ArrayList;
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
        String soldierIdMatch = (String) params.get("soldierIdMatch");
        List<Soldier> soldierList = getDao().getAllSoldiers();
        result.put("listOfSoldiers", soldierList);
        List<Unit> unitList = getDao().getAllUnits();
        result.put("listOfUnits", unitList);
        result.put("currentSoldier", soldierIdMatch);


        List<EntityValue> values = null;
        if ((HandlerFactory.EDIT).equals(action) && soldierIdMatch != null) {
            values = new ArrayList<EntityValue>();
            String param = (String) params.get(Soldier.ALIAS.NAME.getLabelAsQueried());
            if (param != null && !param.equals("") && !param.contains(" "))
                values.add(new EntityValue(Soldier.ALIAS.NAME.getLabel(), param));

            param = (String) params.get(Soldier.ALIAS.UNIT.getLabelAsQueried());
            if (param != null && !param.equals("") && !param.contains(" "))
                values.add(new EntityValue(Soldier.ALIAS.UNIT.getLabel(), param));

            param = (String) params.get(Soldier.ALIAS.RANK.getLabelAsQueried());
            if (param != null && !param.equals("") && !param.contains(" "))
                values.add(new EntityValue(Soldier.ALIAS.RANK.getLabel(), param));

            param = (String) params.get(Soldier.ALIAS.COMMANDER.getLabelAsQueried());
            if (param != null && !param.equals("") && !param.contains(" "))
                values.add(new EntityValue(Soldier.ALIAS.COMMANDER.getLabel(), param));

            param = (String) params.get(Soldier.ALIAS.BIRTHDATE.getLabelAsQueried());
            if (param != null && !param.equals("") && !param.contains(" "))
                values.add(new EntityValue(Soldier.ALIAS.BIRTHDATE.getLabel(), param));

            if (values.size() != 0)
                getDao().setSoldierAttributes(soldierIdMatch, values);
        }
        Soldier soldier = getDao().getSoldierById(soldierIdMatch);
        result.put("queriedSoldier", soldier);
        return result;
    }
}
