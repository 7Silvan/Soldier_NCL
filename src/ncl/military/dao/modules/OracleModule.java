package ncl.military.dao.modules;

import ncl.military.dao.DAO;
import ncl.military.dao.exceptions.DataAccessException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Silvan
 * Date: 18.04.12
 * Time: 9:05
 */
public class OracleModule implements DAO {

    private interface SetParser {
        Object parse(ResultSet raw) throws SQLException;
    }

    private OracleDataSource dataSource;

    private static final String SQL_SELECT_ALL_SOLDIERS =
            //        "select soldier_id, name, rank, commander, unit, birthdate, headofunit " +
            //                "from soldier";
            // values to take
            // soldier_id
            // soldier_name
            // soldier_rank
            // soldier_commander
            // unit_name
            // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate,location.name as location_name, from unit join soldier on unit = unit_id ";
    //"from unit join soldier on unit = unit_id join location on location = loc_id";

    private static final String SQL_SELECT_SOLDIER_BY_ID =
            "select soldier_id, name, rank, commander, unit, birthdate, headofunit " +
                    "from soldier" +
                    "where soldier_id = ?";


    private static final String SQL_SELECT_TOP_OF_SOLDIERS =
//            "select soldier_id, name, rank, commander, unit, birthdate, headofunit " +
//                    "from soldier " +
//                    "where commander is null";
            // values to take
            // soldier_id
            // soldier_name
            // soldier_rank
            // soldier_commander
            // unit_name
            // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate,location.name as location_name, from unit join soldier on unit = unit_id " +
                    "where commander is null";

    private static final String SQL_SELECT_SUBS_OF_SOLDIER_BY_ID =
//            "select soldier_id, name, rank, commander, unit, birthdate, headofunit " +
//                    "from soldier " +
//                    "start with commander = ? " +
//                    "connect by prior soldier_id = commander " +
//                    "order by 1 ";
            // values to take
            // soldier_id
            // soldier_name
            // soldier_rank
            // soldier_commander
            // unit_name
            // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate,location.name as location_name, from unit join soldier on unit = unit_id " +
                    "start with commander = ? connect by prior soldier_id = commander order by 1";

    private static final String SQL_SELECT_HIERARCHY_OF_SOLDIERS_BY_ID =
//            "select level, soldier_id, name, rank, commander, unit, birthdate, headofunit " +
//                    "from soldier " +
//                    "start with soldier_id = ? " +
//                    "connect by prior commander = soldier_id " +
//                    "order by level desc ";
            // values to take
            // soldier_id
            // soldier_name
            // soldier_rank
            // soldier_commander
            // unit_name
            // soldier_birthdate
            "select soldier_id as soldier_id,soldier.name as soldier_name,soldier.rank as soldier_rank,soldier.commander as soldier_commander,unit.name as unit_name,soldier.birthdate as soldier_birthdate,location.name as location_name, from unit join soldier on unit = unit_id " +
                    "start with soldier_id = ? " +
                    "connect by prior commander = soldier_id " +
                    "order by level desc ";

    private static final String SQL_GET_ALL_UNITS =
            // values to take
            // unit_id
            // unit_name
            // soldier_name
            // location_name
            "select unit_id as unit_id,unit.name as unit_name,soldier.name as soldier_name,location.name as location_name from unit join location on unit.location = location.loc_id join soldier on unit.unit_id = soldier.unit and soldier.headofunit = 1 ";
    private static final String SQL_GET_ALL_LOCATIONS =

    public void init(Map<String, String> initParams) {
        try {
            dataSource = (OracleDataSource) new InitialContext().lookup(initParams.get("jndiPath"));
        } catch (NamingException e) {
            Logger.getLogger("model").error("Cannot find resources over jndi");
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
            prst = conn.prepareStatement(query);
            int currentParameter = 0;
            for (String parameter : parameters) {
                prst.setString(++currentParameter, parameter);
            }

            rs = prst.executeQuery();

            return parser.parse(rs);

        } catch (SQLException e) {
            Logger.getLogger("model").error("Some SQL error occured.", e);
            e.printStackTrace();
        } finally {
            SQLException exception = null;
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    Logger.getLogger("model").error("Result set closing error.", e);
                    exception = e;
                } finally {
                    if (prst != null) {
                        try {
                            prst.close();
                        } catch (SQLException e) {
                            Logger.getLogger("model").error("Prepared statement closing error", e);
                            if (exception == null) exception = e;
                        } finally {
                            if (conn != null) {
                                try {
                                    conn.close();
                                } catch (SQLException e) {
                                    Logger.getLogger("model").error("Connection closing error", e);
                                    if (exception == null) exception = e;
                                }
                            } else
                                Logger.getLogger("model").error("Connection was null.");
                        }
                    } else
                        Logger.getLogger("model").error("PreparedStatement was null.");
                }
            } else
                Logger.getLogger("ResultSet was null.");

            if (exception != null) throw new DataAccessException(exception);
        }

        return parser;
    }

    public List<Soldier> getHierarchy(String idMatch) throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_SELECT_HIERARCHY_OF_SOLDIERS_BY_ID, new SetParser() {
                public Object parse(ResultSet raw) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {
                        Soldier sd = new Soldier(raw.getString("soldier_id"),
                                raw.getString("name"),
                                raw.getString("rank"),
                                raw.getString("unit"),
                                raw.getString("commander"),
                                raw.getDate("birthdate"));
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            },
                    idMatch);
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
    }

    public List<Soldier> getAllSoldiers() throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_SELECT_ALL_SOLDIERS, new SetParser() {
                public Object parse(ResultSet raw) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {
                        Soldier sd = new Soldier();
                        sd.setId(raw.getString("soldier_id"));
                        sd.setName(raw.getString("soldier.name"));
                        sd.setRank(raw.getString("rank"));
                        sd.setUnit(raw.getString("unit.name"));
                        sd.setCommander(raw.getString("commander"));
                        sd.setBirthDate(raw.getDate("birthdate"));
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            });
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
    }

    public List<Soldier> getTopOfSoldiers() throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_SELECT_TOP_OF_SOLDIERS, new SetParser() {
                public Object parse(ResultSet raw) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {
                        Soldier sd = new Soldier(raw.getString("soldier_id"),
                                raw.getString("name"),
                                raw.getString("rank"),
                                raw.getString("unit"),
                                raw.getString("commander"),
                                raw.getDate("birthdate"));
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            });
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
    }

    public List<Unit> getAllUnits() throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_SELECT_TOP_OF_SOLDIERS, new SetParser() {
                public Object parse(ResultSet raw) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {
                        Soldier sd = new Soldier(raw.getString("soldier_id"),
                                raw.getString("name"),
                                raw.getString("rank"),
                                raw.getString("unit"),
                                raw.getString("commander"),
                                raw.getDate("birthdate"));
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            });
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
    }

    public Unit getUnitById(String idMatch) throws DataAccessException {
    }

    public List<Soldier> getSoldiersOfUnit(String unitIdMatch) throws DataAccessException {
    }

    public List<Location> getAllLocations() throws DataAccessException {
    }

    public Location getLocationById(String idMatch) throws DataAccessException {
    }

    public List<Unit> getAllUnitsOfLocation(String locationIdMatch) throws DataAccessException {
    }

    public Soldier getSoldierById(String idMatch) throws DataAccessException {
        try {
            return (Soldier) performQuery(SQL_SELECT_TOP_OF_SOLDIERS, new SetParser() {
                public Object parse(ResultSet raw) throws SQLException {
                    Soldier soldier = null;
                    if (raw.next())
                        soldier = new Soldier(raw.getString("soldier_id"),
                                raw.getString("name"),
                                raw.getString("rank"),
                                raw.getString("unit"),
                                raw.getString("commander"),
                                raw.getDate("birthdate"));
                    return soldier;
                }
            },
                    idMatch);
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
    }

    public List<Soldier> getSubSoldiersOfByID(String idMatch) throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_SELECT_SUBS_OF_SOLDIER_BY_ID, new SetParser() {
                public Object parse(ResultSet raw) throws SQLException {
                    List<Soldier> soldiers = new ArrayList<Soldier>();
                    while (raw.next()) {
                        Soldier sd = new Soldier(raw.getString("soldier_id"),
                                raw.getString("name"),
                                raw.getString("rank"),
                                raw.getString("unit"),
                                raw.getString("commander"),
                                raw.getDate("birthdate"));
                        soldiers.add(sd);
                    }
                    return soldiers;
                }
            },
                    idMatch);
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
    }
}
