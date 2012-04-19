package ncl.military.dao.contain;

import ncl.military.entity.Soldier;

import java.util.List;

/**
 * @author gural
 * @version 1.0
 *          Date: 18.04.12
 *          Time: 18:58
 */
public interface SoldierDA {

    String getHierarchy(String idMatch);
    
    // All about Soldiers
    List<Soldier> getAllSoldiers();
    List<Soldier> getSoldiersById(String idMatch);
    List<Soldier> getSoldiersByName(String nameMatch);
    List<Soldier> getSoldiersByRank(String rankMatch);
    List<Soldier> getSoldiersByUnit(String unitMatch);
    List<Soldier> getSoldiersByCommander(String commanderMatch);
    List<Soldier> getSoldiersByBirthDate(String BirthDateMatch);

    List<Soldier> getSubSoldiersOfByID(String idMatch);
}
