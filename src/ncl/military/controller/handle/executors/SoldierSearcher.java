package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;
import ncl.military.dao.searchtool.Filter;
import ncl.military.dao.searchtool.FilterType;
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
        if (params.get("queriedSoldierName") != null)
            filters.add(new Filter(Soldier.ALIAS.NAME.getLabel(), FilterType.LIKE, (String) params.get("queriedSoldierName")));
        if (params.get("queriedSoldierUnit") != null)
            filters.add(new Filter(Unit.ALIAS.NAME.getLabel(), FilterType.LIKE, (String) params.get("queriedSoldierUnit")));
        if (params.get("queriedSoldierLocation") != null)
            filters.add(new Filter(Location.ALIAS.NAME.getLabel(), FilterType.LIKE, (String) params.get("queriedSoldierLocation")));
        if (params.get("queriedSoldierCommanderName") != null)
            filters.add(new Filter("commander_name", FilterType.LIKE, (String) params.get("queriedSoldierCommanderName")));
        if (params.get("queriedSoldierRank") != null)
            filters.add(new Filter(Soldier.ALIAS.RANK.getLabel(), FilterType.LIKE, (String) params.get("queriedSoldierRank")));

        if (filters.size() != 0)
            soldierList = getDao().searchForSoldiers(filters);

        result.put("listOfSoldiers", soldierList);
        //result.put("queriedSoldierName", (String) params.get("queriedSoldierName"));

        return result;
    }
}
