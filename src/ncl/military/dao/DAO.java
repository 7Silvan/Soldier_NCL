package ncl.military.dao;

import ncl.military.dao.contain.SoldierDA;

import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:53
 */
public interface DAO extends SoldierDA{
    // special for dao
    void init(Map<String, String> initParams);
    void init();
    
    // TODO implement FINDER using filters
}
