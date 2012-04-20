package ncl.military.dao.contain;

import ncl.military.dao.exceptions.DataAccessException;
import ncl.military.entity.Soldier;

import java.util.List;

/**
 * @author gural
 * @version 1.0
 *          Date: 18.04.12
 *          Time: 18:58
 */
public interface SoldierDA {

    String getHierarchy(String idMatch) throws DataAccessException;
    
    // All about Soldiers
    List<Soldier> getAllSoldiers() throws DataAccessException;
    Soldier getSoldierById(String idMatch) throws DataAccessException;

    List<Soldier> getSubSoldiersOfByID(String idMatch) throws DataAccessException;
    List<Soldier> getTopOfSoldiers() throws DataAccessException;
}
