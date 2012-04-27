package ncl.military.controller.handle.executors;

import ncl.military.controller.handle.HandlerFactory;
import ncl.military.dao.DAO;
import ncl.military.dao.tools.EntityValue;
import ncl.military.entity.Location;
import ncl.military.entity.Soldier;
import ncl.military.entity.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Silvan
 * Date: 27.04.12
 * Time: 6:06
 */
public class SoldierAdder extends Executor {

    public SoldierAdder(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();

        String action = (String) params.get("action");

        List<Soldier> soldierList = getDao().getAllSoldiers();
        result.put("listOfSoldiers", soldierList);
        List<Unit> unitList = getDao().getAllUnits();
        result.put("listOfUnits", unitList);

        List<EntityValue> values = null;
        if ((HandlerFactory.EDIT).equals(action)) {
            values = new ArrayList<EntityValue>();
            String param = (String) params.get(Soldier.ALIAS.NAME.getLabelAsQueried());
            if (param != null && !param.equals("") && !param.contains(" "))
                values.add(new EntityValue(Soldier.ALIAS.NAME.getLabel(), param));

            param = (String) params.get(Unit.ALIAS.NAME.getLabelAsQueried());
            if (param != null && !param.equals("") && !param.contains(" "))
                values.add(new EntityValue(Unit.ALIAS.NAME.getLabel(), param));

            param = (String) params.get(Location.ALIAS.NAME.getLabelAsQueried());
            if (param != null && !param.equals("") && !param.contains(" "))
                values.add(new EntityValue(Location.ALIAS.NAME.getLabel(), param));

            param = (String) params.get(Soldier.ALIAS.RANK.getLabelAsQueried());
            if (param != null && !param.equals("") && !param.contains(" "))
                values.add(new EntityValue(Soldier.ALIAS.RANK.getLabel(), param));

            getDao().addSoldier(values);
        }

        return result;
    }
}
