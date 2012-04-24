package ncl.military.dao;

import ncl.military.dao.exceptions.DAOInitException;
import ncl.military.dao.exceptions.DataAccessException;
import ncl.military.entity.Location;
import ncl.military.entity.Soldier;
import ncl.military.entity.Unit;

import java.util.List;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:53
 */
public interface DAO {
    // special for dao
    void init(Map<String, String> initParams) throws DAOInitException;

    void init() throws DAOInitException;

    // TODO implement FINDER using filters

    // it's just for soldiers
    List<Soldier> getHierarchy(String idMatch) throws DataAccessException;

    // All about Soldiers
    List<Soldier> getAllSoldiers() throws DataAccessException;

    Soldier getSoldierById(String idMatch) throws DataAccessException;

    List<Soldier> getSubSoldiersOfByID(String idMatch) throws DataAccessException;

    List<Soldier> getTopOfSoldiers() throws DataAccessException;

    // All about Units
    List<Unit> getAllUnits() throws DataAccessException;

    Unit getUnitById(String idMatch) throws DataAccessException;

    List<Soldier> getSoldiersOfUnit(String unitIdMatch) throws DataAccessException;

    // All about Locations
    List<Location> getAllLocations() throws DataAccessException;

    Location getLocationById(String idMatch) throws DataAccessException;

    List<Unit> getAllUnitsOfLocation(String locationIdMatch) throws DataAccessException;

}
