package ncl.military.dao;

import ncl.military.dao.contain.LocationDA;
import ncl.military.dao.contain.SoldierDA;
import ncl.military.dao.contain.UnitDA;

import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:53
 */
public interface DAO {
    // special for dao
    void init(Map<String, String> initParams);
    void init();
    
    // TODO implement FINDER using filters
}
