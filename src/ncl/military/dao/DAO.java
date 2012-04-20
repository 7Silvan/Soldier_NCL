package ncl.military.dao;

import ncl.military.dao.contain.SoldierDA;
import ncl.military.dao.exceptions.DAOInitException;

import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:53
 */
public interface DAO extends SoldierDA{
    // special for dao
    void init(Map<String, String> initParams) throws DAOInitException;
    void init() throws DAOInitException;
    
    // TODO implement FINDER using filters
}
