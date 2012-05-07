package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;
import ncl.military.dao.tools.Filter;
import ncl.military.dao.tools.FilterType;
import ncl.military.entity.Location;
import ncl.military.entity.Soldier;
import ncl.military.entity.Unit;

import java.util.ArrayList;
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

        List<Soldier> soldierList = null;
        List<Filter> filters = new ArrayList<Filter>();//(List<Filter>) params.get("filterList");

        String param = (String) params.get(Soldier.ALIAS.NAME.getLabelAsQueried());
        if (param != null && !param.equals("") && !param.contains(" "))
            filters.add(new Filter(Soldier.ALIAS.NAME.getLabel(), FilterType.LIKE, param));

        param = (String) params.get(Unit.ALIAS.NAME.getLabelAsQueried());
        if (param != null && !param.equals("") && !param.contains(" "))
            filters.add(new Filter(Unit.ALIAS.NAME.getLabel(), FilterType.LIKE, param));

        param = (String) params.get(Location.ALIAS.NAME.getLabelAsQueried());
        if (param != null && !param.equals("") && !param.contains(" "))
            filters.add(new Filter(Location.ALIAS.NAME.getLabel(), FilterType.LIKE, param));

        param = (String) params.get("queried_commander_name");
        if (param != null && !param.equals("") && !param.contains(" "))
            filters.add(new Filter("commander_name", FilterType.LIKE, param));

        param = (String) params.get(Soldier.ALIAS.RANK.getLabelAsQueried());
        if (param != null && !param.equals("") && !param.contains(" "))
            filters.add(new Filter(Soldier.ALIAS.RANK.getLabel(), FilterType.LIKE, param));

        if (filters.size() != 0)
            soldierList = getDao().searchForSoldiers(filters);
        else
            soldierList = getDao().getAllSoldiers();

        result.put("listOfSoldiers", soldierList);


        return result;
    }
}
