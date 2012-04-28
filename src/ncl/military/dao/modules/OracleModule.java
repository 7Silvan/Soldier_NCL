package ncl.military.dao.modules;

import ncl.military.dao.DAO;
import ncl.military.dao.exceptions.DataAccessException;
import ncl.military.dao.tools.EntityValue;
import ncl.military.dao.tools.Filter;
import ncl.military.dao.tools.FilterType;
import ncl.military.entity.Location;
import ncl.military.entity.Soldier;
import ncl.military.entity.Unit;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ncl.military.entity.Soldier.ALIAS.getAlias;

/**
 * User: Silvan
 * Date: 18.04.12
 * Time: 9:05
 */
public class OracleModule implements DAO {

    private static final Logger log = Logger.getLogger("model");

    private interface SetParser {
        Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException;
    }

    private OracleDataSource dataSource;

    private static final String SQL_GET_ALL_SOLDIERS_FULL_INFO =
            // values to take // soldier_id// soldier_name // soldier_rank // soldier_commander // unit_name // soldier_birthdate // location_name // commander_name
            "select soldier_id, soldier_name, soldier_rank, soldier_commander, unit_name, soldier_birthdate, location_name, commander.name as commander_name from (select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate, location.name as location_name from unit join soldier on unit = unit_id join location on location.loc_id = unit.location) soldier left join (select soldier_id as id, name from soldier) commander on soldier.soldier_commander = commander.id ";

    private static final String SQL_GET_ALL_SOLDIERS =
            // values to take // soldier_id // soldier_name // soldier_rank // soldier_commander // unit_name // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate from unit join soldier on unit = unit_id ";

    private static final String SQL_GET_SOLDIER_BY_ID_FOR_UPDATE =
            // values to take // soldier_id // soldier_name // soldier_rank // soldier_commander // unit_name // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,soldier.unit as unit_name,soldier.birthdate as soldier_birthdate from soldier " +
                    "where soldier_id = ? ";

    private static final String SQL_GET_SOLDIER_BY_ID =
            // values to take // soldier_id // soldier_name // soldier_rank // soldier_commander // unit_name // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate from unit join soldier on unit = unit_id " +
                    "where soldier_id = ? ";

    private static final String SQL_GET_TOP_OF_SOLDIERS =
// values to take // soldier_id // soldier_name // soldier_rank // soldier_commander // unit_name // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate,location.name as location_name from unit join soldier on unit = unit_id join location on location.loc_id = unit.location " +
                    "where commander is null";

    private static final String SQL_GET_SUBS_OF_SOLDIER_BY_ID =
// values to take // soldier_id // soldier_name // soldier_rank // soldier_commander // unit_name // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate from unit join soldier on unit = unit_id " +
                    "start with commander = ? connect by prior soldier_id = commander and level = 1  order by 1 ";

    private static final String SQL_GET_SOLDIERS_OF_UNIT =
// values to take // soldier_id // soldier_name // soldier_rank // soldier_commander // unit_name // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate from unit join soldier on unit = unit_id " +
                    "where unit_id = ? ";

    private static final String SQL_GET_HIERARCHY_OF_SOLDIERS_BY_ID =
// values to take // soldier_id // soldier_name // soldier_rank // soldier_commander // unit_name // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate from unit join soldier on unit = unit_id " +
                    "start with soldier_id = ? " +
                    "connect by prior commander = soldier_id " +
                    "order by level desc ";

    private static final String SQL_GET_ALL_UNITS =
            // values to take // unit_id // unit_name // soldier_name // location_name
            "select unit_id as unit_id,unit.name as unit_name,soldier.soldier_id as soldier_id,soldier.name as soldier_name,location.name as location_name from unit join location on unit.location = location.loc_id left join soldier on unit.unit_id = soldier.unit and soldier.headofunit = 1";

    private static final String SQL_GET_UNIT_BY_ID =
            // values to take // unit_id // unit_name // soldier_name // location_name
            "select unit_id as unit_id,unit.name as unit_name,soldier.soldier_id as soldier_id,soldier.name as soldier_name,location.name as location_name from unit join location on unit.location = location.loc_id join soldier on unit.unit_id = soldier.unit and soldier.headofunit = 1 where unit_id = ? ";

    private static final String SQL_GET_UNITS_OF_LOCATION =
            // values to take // unit_id // unit_name // soldier_name // location_name
            "select unit_id as unit_id,unit.name as unit_name,soldier.soldier_id as soldier_id,soldier.name as soldier_name,location.name as location_name from unit join location on unit.location = location.loc_id join soldier on unit.unit_id = soldier.unit and soldier.headofunit = 1 where location.loc_id = ? ";

    private static final String SQL_GET_ALL_LOCATIONS =
            // values to take // location_id // location_name // location_region // location_city
            "select loc_id as location_id, name as location_name, region as location_region, city as location_city from location ";

    private static final String SQL_GET_LOCATION_BY_ID =
            // values to take // location_id // location_name // location_region // location_city
            "select loc_id as location_id, name as location_name, region as location_region, city as location_city from location where loc_id = ? ";

    private static final String SQL_GET_UNIT_BY_ID_FOR_UPDATE = // values to take // unit_id // unit_name // soldier_name // location_name
            "select unit_id as unit_id,unit.name as unit_name,soldier.soldier_id as soldier_id,soldier.name as soldier_name,location.name as location_name from unit join location on unit.location = location.loc_id join soldier on unit.unit_id = soldier.unit and soldier.headofunit = 1 where unit_id = ? ";
    ;

    public void init(Map<String, String> initParams) {
        try {
            dataSource = (OracleDataSource) new InitialContext().lookup(initParams.get("jndiPath"));
        } catch (NamingException e) {
            log.error("Cannot find resources over jndi");
            e.printStackTrace();
        }
    }

    public void init() {
        init(null);
    }


    public Object performQuery(String query, SetParser parser, String... parameters) throws SQLException, DataAccessException {
        Connection conn = null;
        PreparedStatement prst = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            prst = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            log.debug("Preparing statement with query : " + query);

            int currentParameter = 0;
            for (String parameter : parameters) {
                prst.setString(++currentParameter, parameter);
                log.info("assigning parameter #" + currentParameter + " with value: " + parameter);
            }

            rs = prst.executeQuery();

            return parser.parse(rs, prst, conn);
        } catch (SQLException e) {
            log.error("Some SQL error occured.", e);
            e.printStackTrace();
        } finally {
            SQLException exception = null;
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("Result set closing error.", e);
                    exception = e;
                } finally {
                    if (prst != null) {
                        try {
                            prst.close();
                        } catch (SQLException e) {
                            log.error("Prepared statement closing error", e);
                            if (exception == null) exception = e;
                        } finally {
                            if (conn != null) {
                                try {
                                    conn.close();
                                } catch (SQLException e) {
                                    log.error("Connection closing error", e);
                                    if (exception == null) exception = e;
                                }
                            } else
                                log.error("Connection was null.");
                        }
                    } else
                        log.error("PreparedStatement was null.");
                }
            } else
                Logger.getLogger("ResultSet was null.");

            if (exception != null) throw new DataAccessException(exception);
        }

        return parser;
    }

    // probably new generalization of querying 
    public List<Soldier> getSoldiersListCustomQuery(String query) throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(query, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {
                        Soldier sd = new Soldier(
                                raw.getString(Soldier.ALIAS.ID.getLabel()),
                                raw.getString(Soldier.ALIAS.NAME.getLabel()),
                                raw.getString(Soldier.ALIAS.RANK.getLabel()),
                                raw.getString(Soldier.ALIAS.UNIT.getLabel()),
                                raw.getString(Soldier.ALIAS.COMMANDER.getLabel()),
                                raw.getDate(Soldier.ALIAS.BIRTHDATE.getLabel())
                        );
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            });
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public List<Unit> getUnitsListCustomQuery(String query) throws DataAccessException {
        try {
            return (List<Unit>) performQuery(query, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Unit> units = new ArrayList<Unit>();
                    while (raw.next()) {
                        Unit unit = new Unit(raw.getString(Unit.ALIAS.ID.getLabel()),
                                raw.getString(Unit.ALIAS.HEAD.getLabel()),
                                raw.getString(Unit.ALIAS.LOCATION.getLabel()),
                                raw.getString(Unit.ALIAS.NAME.getLabel()),
                                raw.getString(Unit.ALIAS.HEADID.getLabel()));

                        units.add(unit);
                    }
                    return units;
                }
            });
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public List<Location> getLocationsListCustomQuery(String query) throws DataAccessException {
        try {
            return (List<Location>) performQuery(SQL_GET_ALL_UNITS, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Location> locations = new ArrayList<Location>();
                    while (raw.next()) {
                        Location location = new Location(
                                raw.getString(Location.ALIAS.ID.getLabel()),
                                raw.getString(Location.ALIAS.NAME.getLabel()),
                                raw.getString(Location.ALIAS.REGION.getLabel()),
                                raw.getString(Location.ALIAS.CITY.getLabel())
                        );

                        locations.add(location);
                    }
                    return locations;
                }
            });
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public List<Soldier> getHierarchy(String idMatch) throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_GET_HIERARCHY_OF_SOLDIERS_BY_ID, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {
                        Soldier sd = new Soldier(
                                raw.getString(Soldier.ALIAS.ID.getLabel()),
                                raw.getString(Soldier.ALIAS.NAME.getLabel()),
                                raw.getString(Soldier.ALIAS.RANK.getLabel()),
                                raw.getString(Soldier.ALIAS.UNIT.getLabel()),
                                raw.getString(Soldier.ALIAS.COMMANDER.getLabel()),
                                raw.getDate(Soldier.ALIAS.BIRTHDATE.getLabel())
                        );
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            },
                    idMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public List<Soldier> getAllSoldiers() throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_GET_ALL_SOLDIERS, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {
                        Soldier sd = new Soldier();
                        sd.setId(raw.getString(Soldier.ALIAS.ID.getLabel()));
                        sd.setName(raw.getString(Soldier.ALIAS.NAME.getLabel()));
                        sd.setRank(raw.getString(Soldier.ALIAS.RANK.getLabel()));
                        sd.setUnit(raw.getString(Soldier.ALIAS.UNIT.getLabel()));
                        sd.setCommander(raw.getString(Soldier.ALIAS.COMMANDER.getLabel()));
                        sd.setBirthDate(raw.getDate(Soldier.ALIAS.BIRTHDATE.getLabel()));
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            });
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public List<Soldier> getTopOfSoldiers() throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_GET_TOP_OF_SOLDIERS, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {

                        Soldier sd = new Soldier(
                                raw.getString(Soldier.ALIAS.ID.getLabel()),
                                raw.getString(Soldier.ALIAS.NAME.getLabel()),
                                raw.getString(Soldier.ALIAS.RANK.getLabel()),
                                raw.getString(Soldier.ALIAS.UNIT.getLabel()),
                                raw.getString(Soldier.ALIAS.COMMANDER.getLabel()),
                                raw.getDate(Soldier.ALIAS.BIRTHDATE.getLabel())
                        );
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            });
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public List<String> getAllSoldiersIds() throws DataAccessException {
        throw new UnsupportedOperationException("not implemented yet.");
        //return null;//To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Unit> getAllUnits() throws DataAccessException {
        try {
            return (List<Unit>) performQuery(SQL_GET_ALL_UNITS, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Unit> units = new ArrayList<Unit>();
                    while (raw.next()) {
                        Unit unit = new Unit(raw.getString(Unit.ALIAS.ID.getLabel()),
                                raw.getString(Unit.ALIAS.HEAD.getLabel()),
                                raw.getString(Unit.ALIAS.LOCATION.getLabel()),
                                raw.getString(Unit.ALIAS.NAME.getLabel()),
                                raw.getString(Unit.ALIAS.HEADID.getLabel()));

                        units.add(unit);
                    }
                    return units;
                }
            });
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public Unit getUnitById(String unitIdMatch) throws DataAccessException {
        try {
            return (Unit) performQuery(SQL_GET_UNIT_BY_ID, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    Unit unit = null;
                    if (raw.next()) {
                        unit = new Unit(raw.getString(Unit.ALIAS.ID.getLabel()),
                                raw.getString(Unit.ALIAS.HEAD.getLabel()),
                                raw.getString(Unit.ALIAS.LOCATION.getLabel()),
                                raw.getString(Unit.ALIAS.NAME.getLabel()),
                                raw.getString(Unit.ALIAS.HEADID.getLabel()));
                    }
                    return unit;
                }
            },
                    unitIdMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public List<Soldier> getSoldiersOfUnit(String unitIdMatch) throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_GET_SOLDIERS_OF_UNIT, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {
                        Soldier sd = new Soldier(
                                raw.getString(Soldier.ALIAS.ID.getLabel()),
                                raw.getString(Soldier.ALIAS.NAME.getLabel()),
                                raw.getString(Soldier.ALIAS.RANK.getLabel()),
                                raw.getString(Soldier.ALIAS.UNIT.getLabel()),
                                raw.getString(Soldier.ALIAS.COMMANDER.getLabel()),
                                raw.getDate(Soldier.ALIAS.BIRTHDATE.getLabel())
                        );
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            },
                    unitIdMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public List<Location> getAllLocations() throws DataAccessException {
        try {
            return (List<Location>) performQuery(SQL_GET_ALL_LOCATIONS, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Location> locations = new ArrayList<Location>();
                    while (raw.next()) {
                        Location location = new Location(
                                raw.getString(Location.ALIAS.ID.getLabel()),
                                raw.getString(Location.ALIAS.NAME.getLabel()),
                                raw.getString(Location.ALIAS.REGION.getLabel()),
                                raw.getString(Location.ALIAS.CITY.getLabel())
                        );

                        locations.add(location);
                    }
                    return locations;
                }
            });
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public Location getLocationById(String locationIdMatch) throws DataAccessException {
        try {
            return (Location) performQuery(SQL_GET_LOCATION_BY_ID, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    Location location = null;
                    if (raw.next()) {
                        location = new Location(
                                raw.getString(Location.ALIAS.ID.getLabel()),
                                raw.getString(Location.ALIAS.NAME.getLabel()),
                                raw.getString(Location.ALIAS.REGION.getLabel()),
                                raw.getString(Location.ALIAS.CITY.getLabel())
                        );
                    }
                    return location;
                }
            },
                    locationIdMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public List<Unit> getUnitsOfLocation(String locationIdMatch) throws DataAccessException {
        try {
            return (List<Unit>) performQuery(SQL_GET_UNITS_OF_LOCATION, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Unit> units = new ArrayList<Unit>();
                    while (raw.next()) {
                        Unit unit = new Unit(raw.getString(Unit.ALIAS.ID.getLabel()),
                                raw.getString(Unit.ALIAS.HEAD.getLabel()),
                                raw.getString(Unit.ALIAS.LOCATION.getLabel()),
                                raw.getString(Unit.ALIAS.NAME.getLabel()),
                                raw.getString(Unit.ALIAS.HEADID.getLabel()));

                        units.add(unit);
                    }
                    return units;
                }
            },
                    locationIdMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    private String searchStatementsAppender(String attribute, FilterType type, String value) {
        if (type == FilterType.LIKE) {
            return "UPPER(" + attribute + ")" + FilterType.LIKE.toString() + "UPPER('" + value + "%')";
        } else {
            return attribute + type.toString() + value;
        }
    }

    public List<Soldier> searchForSoldiers(List<Filter> filters) throws DataAccessException {
        List<Soldier> soldiers = new ArrayList<Soldier>();
        if (filters.size() != 0) {
            StringBuilder searchQuery = new StringBuilder("select * from ( " + SQL_GET_ALL_SOLDIERS_FULL_INFO + " ) inner_table ");
            searchQuery.append(" where ");
            boolean first = true;

            for (Filter f : filters) {
                if (!first) {
                    searchQuery.append(" and ");
                } else
                    first = false;

                try {
                    searchQuery.append(
                            searchStatementsAppender(
                                    f.getAttribute(),
                                    f.getTypeOfComparison(),
                                    f.getValue())
                    );

                    soldiers = getSoldiersListCustomQuery(searchQuery.toString());

                } catch (IllegalArgumentException ex) {
                    log.error("Wrong attribute name");

                }
            }
        }
        return soldiers;
    }

    public List<Unit> searchForUnits(List<Filter> filters) throws DataAccessException {
        List<Unit> units = new ArrayList<Unit>();
        if (filters.size() != 0) {
            StringBuilder searchQuery = new StringBuilder(SQL_GET_ALL_UNITS);
            searchQuery.append(" where ");
            boolean first = true;

            for (Filter f : filters) {
                if (!first) {
                    searchQuery.append(" and ");
                } else
                    first = false;

                try {
                    searchQuery.append(
                            searchStatementsAppender(
                                    getAlias(f.getAttribute()).getLabel(),
                                    f.getTypeOfComparison(),
                                    f.getValue())
                    );

                    units = getUnitsListCustomQuery(searchQuery.toString());

                } catch (IllegalArgumentException ex) {
                    log.error("Wrong attribute name");
                }
            }
        }
        return units;
    }

    public List<Location> searchForLocations(List<Filter> filters) throws DataAccessException {
        List<Location> locations = new ArrayList<Location>();
        if (filters.size() != 0) {
            StringBuilder searchQuery = new StringBuilder(SQL_GET_ALL_LOCATIONS);
            searchQuery.append(" where ");
            boolean first = true;

            for (Filter f : filters) {
                if (!first) {
                    searchQuery.append(" and ");
                } else
                    first = false;

                try {
                    searchQuery.append(
                            searchStatementsAppender(
                                    getAlias(f.getAttribute()).getLabel(),
                                    f.getTypeOfComparison(),
                                    f.getValue())
                    );

                    locations = getLocationsListCustomQuery(searchQuery.toString());

                } catch (IllegalArgumentException ex) {
                    log.error("Wrong attribute name");
                }
            }
        }
        return locations;
    }

    public Boolean setSoldierAttributes(String soldierIdMatch, final List<EntityValue> values) throws DataAccessException {
        try {
            return (Boolean) performQuery(SQL_GET_SOLDIER_BY_ID_FOR_UPDATE, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    boolean updated = false;
                    if (raw.next()) {
                        for (EntityValue value : values) {
                            try {
                                String alias = value.getKey();
                                if (alias.equals(Soldier.ALIAS.NAME.getLabel())
                                        || alias.equals(Soldier.ALIAS.RANK.getLabel()))
                                    raw.updateString(value.getKey(), value.getValue());

                                if (alias.equals(Soldier.ALIAS.ID.getLabel())
                                        || alias.equals(Soldier.ALIAS.UNIT.getLabel())
                                        || alias.equals(Soldier.ALIAS.COMMANDER.getLabel()))
                                    raw.updateInt(value.getKey(), Integer.parseInt(value.getValue()));

                                if (alias.equals(Soldier.ALIAS.BIRTHDATE.getLabel())) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    java.util.Date date = sdf.parse(value.getValue());
                                    raw.updateDate(value.getKey(), new java.sql.Date(date.getTime()));
                                }

                            } catch (NumberFormatException e) {
                                log.error("Parsing data from given value error", e);
                                e.printStackTrace();
                                throw new DataAccessException("Parsing data from given value error", e);
                            } catch (SQLException e) {
                                log.error("Didn\'t updated column :" + value.getKey() + " with value : " + value.getValue(), e);
                                e.printStackTrace();
                                throw e;
                            } catch (ParseException e) {
                                log.error("Parsing data from given value error", e);
                                e.printStackTrace();
                                throw new DataAccessException("Parsing data from given value error", e);
                            }
                        }
                        raw.updateRow();
                        updated = raw.rowUpdated();
                        conn.commit();
                    }
                    return updated;
                }
            }, soldierIdMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public Boolean setNewCommander(String soldierIdMatch, final String commanderIdMatch) throws DataAccessException {
        try {
            return (Boolean) performQuery(SQL_GET_SOLDIER_BY_ID_FOR_UPDATE, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    boolean updated = false;
                    if (raw.next()) {
                        try {
                            raw.updateString(Soldier.ALIAS.COMMANDER.getLabel(), commanderIdMatch);
                        } catch (SQLException e) {
                            log.error("Didn\'t updated column :" + Soldier.ALIAS.COMMANDER.getLabel() +
                                    " with value : " + commanderIdMatch, e);
                            e.printStackTrace();
                            throw e;
                        }
                    }
                    raw.updateRow();
                    updated = raw.rowUpdated();
                    conn.commit();
                    return updated;
                }
            }, soldierIdMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public Boolean setLocationAttributes(String locationIdMatch, final List<EntityValue> values) throws DataAccessException {
        try {
            return (Boolean) performQuery(SQL_GET_LOCATION_BY_ID, new SetParser() {
                boolean updated = false;

                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    if (raw.next()) {
                        for (EntityValue value : values) {
                            try {
                                raw.updateString(value.getKey(), value.getValue());
                            } catch (SQLException e) {
                                log.error("Didn\'t updated column :" + value.getKey() + " with value : " + value.getValue(), e);
                                e.printStackTrace();
                                throw e;
                            }
                        }
                        raw.updateRow();
                        updated = raw.rowUpdated();
                        conn.commit();
                    }
                    return updated;
                }
            },
                    locationIdMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public Boolean setUnitAttributes(String unitIdMatch, final List<EntityValue> values) throws DataAccessException {
        try {
            return (Boolean) performQuery(SQL_GET_UNIT_BY_ID_FOR_UPDATE, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    boolean updated = false;
                    if (raw.next()) {
                        for (EntityValue value : values) {
                            try {
                                String alias = value.getKey();
                                if (alias.equals(Unit.ALIAS.NAME.getLabel()))
                                    raw.updateString(value.getKey(), value.getValue());

                                if (alias.equals(Unit.ALIAS.LOCATION.getLabel()))
                                    raw.updateInt(value.getKey(), Integer.parseInt(value.getValue()));

                            } catch (NumberFormatException e) {
                                log.error("Parsing data from given value error", e);
                                e.printStackTrace();
                                throw new DataAccessException("Parsing data from given value error", e);
                            } catch (SQLException e) {
                                log.error("Didn\'t updated column :" + value.getKey() + " with value : " + value.getValue(), e);
                                e.printStackTrace();
                                throw e;
                            }
                        }
                        raw.updateRow();
                        updated = raw.rowUpdated();
                        conn.commit();
                    }
                    return updated;
                }
            },
                    unitIdMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public Boolean addSoldier(final List<EntityValue> values) throws DataAccessException {
        try {
            return (Boolean) performQuery(SQL_GET_SOLDIER_BY_ID_FOR_UPDATE, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    boolean added = false;
                    if (raw.next()) {
                        raw.moveToInsertRow();
                        for (EntityValue value : values) {
                            try {
                                String alias = value.getKey();
                                if (alias.equals(Soldier.ALIAS.NAME.getLabel())
                                        || alias.equals(Soldier.ALIAS.RANK.getLabel()))
                                    raw.updateString(value.getKey(), value.getValue());

                                if (alias.equals(Soldier.ALIAS.ID.getLabel())
                                        || alias.equals(Soldier.ALIAS.UNIT.getLabel())
                                        || alias.equals(Soldier.ALIAS.COMMANDER.getLabel()))
                                    raw.updateInt(value.getKey(), Integer.parseInt(value.getValue()));

                                if (alias.equals(Soldier.ALIAS.BIRTHDATE.getLabel())) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    java.util.Date date = sdf.parse(value.getValue());
                                    raw.updateDate(value.getKey(), new java.sql.Date(date.getTime()));
                                }

                            } catch (NumberFormatException e) {
                                log.error("Parsing data from given value error", e);
                                e.printStackTrace();
                                throw new DataAccessException("Parsing data from given value error", e);
                            } catch (SQLException e) {
                                log.error("Didn\'t updated column :" + value.getKey() + " with value : " + value.getValue(), e);
                                e.printStackTrace();
                                throw e;
                            } catch (ParseException e) {
                                log.error("Parsing data from given value error", e);
                                e.printStackTrace();
                                throw new DataAccessException("Parsing data from given value error", e);
                            }
                        }
                        raw.insertRow();
                        added = raw.rowInserted();
                        raw.moveToCurrentRow();
                        conn.commit();
                    }
                    return added;
                }
            });
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public Soldier getSoldierById(String idMatch) throws DataAccessException {
        try {
            return (Soldier) performQuery(SQL_GET_SOLDIER_BY_ID, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    Soldier soldier = null;
                    if (raw.next())
                        soldier = new Soldier(
                                raw.getString(Soldier.ALIAS.ID.getLabel()),
                                raw.getString(Soldier.ALIAS.NAME.getLabel()),
                                raw.getString(Soldier.ALIAS.RANK.getLabel()),
                                raw.getString(Soldier.ALIAS.UNIT.getLabel()),
                                raw.getString(Soldier.ALIAS.COMMANDER.getLabel()),
                                raw.getDate(Soldier.ALIAS.BIRTHDATE.getLabel())
                        );
                    return soldier;
                }
            },
                    idMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }

    public List<Soldier> getSubSoldiersOfByID(String idMatch) throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_GET_SUBS_OF_SOLDIER_BY_ID, new SetParser() {
                public Object parse(ResultSet raw, PreparedStatement prst, Connection conn) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {
                        Soldier sd = new Soldier(
                                raw.getString(Soldier.ALIAS.ID.getLabel()),
                                raw.getString(Soldier.ALIAS.NAME.getLabel()),
                                raw.getString(Soldier.ALIAS.RANK.getLabel()),
                                raw.getString(Soldier.ALIAS.UNIT.getLabel()),
                                raw.getString(Soldier.ALIAS.COMMANDER.getLabel()),
                                raw.getDate(Soldier.ALIAS.BIRTHDATE.getLabel())
                        );
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            },
                    idMatch);
        } catch (SQLException e) {
            log.error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data operations failed.", e);
        }
    }
}
