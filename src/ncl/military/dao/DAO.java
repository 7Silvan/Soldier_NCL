package ncl.military.dao;

import ncl.military.dao.exceptions.DAOInitException;
import ncl.military.dao.exceptions.DataAccessException;
import ncl.military.dao.tools.EntityValue;
import ncl.military.dao.tools.Filter;
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

    ///////////
    //GETTERS//
    ///////////
    List<Soldier> getHierarchy(String idMatch) throws DataAccessException;

    // All about Soldiers
    List<Soldier> getAllSoldiers() throws DataAccessException;

    Soldier getSoldierById(String idMatch) throws DataAccessException;

    List<Soldier> getSubSoldiersOfByID(String idMatch) throws DataAccessException;

    List<Soldier> getTopOfSoldiers() throws DataAccessException;

    List<String> getAllSoldiersIds() throws DataAccessException;

    // All about Units
    List<Unit> getAllUnits() throws DataAccessException;

    Unit getUnitById(String unitIdMatch) throws DataAccessException;

    List<Soldier> getSoldiersOfUnit(String unitIdMatch) throws DataAccessException;

    // All about Locations
    List<Location> getAllLocations() throws DataAccessException;

    Location getLocationById(String locationIdMatch) throws DataAccessException;

    List<Unit> getUnitsOfLocation(String locationIdMatch) throws DataAccessException;

    // Searchers
    List<Soldier> searchForSoldiers(List<Filter> filters) throws DataAccessException;

    List<Unit> searchForUnits(List<Filter> filters) throws DataAccessException;

    List<Location> searchForLocations(List<Filter> filters) throws DataAccessException;

    ///////////
    //SETTERS//
    ///////////
    // Soldier sets
    Soldier setSoldierAttributes(String soldierIdMatch, List<EntityValue> values) throws DataAccessException;

    // Location sets
    Location setLocationAttributes(String locationIdMatch, List<EntityValue> values) throws DataAccessException;

    // Unit sets
    Unit setUnitAttributes(String unitIdMatch, List<EntityValue> values) throws DataAccessException;

    //////////
    //ADDERS//
    //////////
    // Soldier adder
    Soldier addSoldier(String soldierIdMatch, List<EntityValue> values) throws DataAccessException;
}
