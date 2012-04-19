package ncl.military.dao.contain;

import ncl.military.entity.Location;

import java.util.List;

/**
 * @author gural
 * @version 1.0
 *          Date: 18.04.12
 *          Time: 18:58
 */
public interface LocationDA {

    // All about Locations
    List<Location> getAllLocations();
    List<Location> getLocationsById(String idMatch);
}
