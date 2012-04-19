package ncl.military.dao.contain;

import ncl.military.entity.Unit;

import java.util.List;

/**
 * @author gural
 * @version 1.0
 *          Date: 18.04.12
 *          Time: 18:59
 */
public interface UnitDA {

    // All about Units
    List<Unit> getAllUnits();
    List<Unit> getUnitsById(String idMatch);
    List<Unit> getUnitsByName(String nameMatch);
    List<Unit> getUnitsByHead(String headMatch);
    List<Unit> getUnitsByLocation(String locationMatch);
}
