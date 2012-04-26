package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;
import ncl.military.dao.tools.Filter;
import ncl.military.entity.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 24.04.12
 *          Time: 13:15
 */
//TODO remove this class
public class LocationSearcher extends Executor {
    public LocationSearcher(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();

        List<Filter> filters = (List<Filter>) params.get("filterList");
        if (filters != null) {

        } else {
            List<Location> locationList = getDao().getAllLocations();
            result.put("listOfLocations", locationList);
        }

        return result;
    }
}
