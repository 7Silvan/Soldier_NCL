package ncl.military.dao.modules;

import ncl.military.dao.DAO;
import ncl.military.dao.contain.SoldierDA;
import ncl.military.dao.exceptions.DataAccessException;
import ncl.military.entity.Soldier;
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
public class OracleModule implements DAO, SoldierDA {

    private OracleDataSource dataSource;

    private static final String SQL_SELECT_ALL =
            "select soldier_id, name, rank, commander, unit, birthdate, headofunit " +
                    "from soldier";
    //"from unit join soldier on unit = unit_id join location on location = loc_id";

    private static final String SQL_SELECT_ONE_BY_ID =
            "select soldier_id, name, rank, commander, unit, birthdate, headofunit " +
                    "from soldier" +
                    "where soldier_id = ?";

    private static final String SQL_SELECT_TOP =
            "select soldier_id, name, rank, commander, unit, birthdate, headofunit " +
                    "from soldier " +
                    "where commander is null";

    private static final String SQL_SELECT_SUBS_OF_BY_ID =
            "select soldier_id, name, rank, commander, unit, birthdate, headofunit " +
                    "from soldier " +
                    "start with commander = ? " +
                    "connect by prior soldier_id = commander " +
                    "order by 1 ";

    private static final String SQL_SELECT_HIERARCHY_OF_BY_ID =
            "select name, level from soldier " +
                    "start with commander = ? " +
                    "connect by prior commander = soldier_id " +
                    "order by level desc";


    public void init(Map<String, String> initParams) {
        try {
            dataSource = (OracleDataSource) new InitialContext().lookup("java:/comp/env/jdbc/soldier");
        } catch (NamingException e) {
            // TODO logging here
            e.printStackTrace();
        }
    }

    public void init() {
        init(null);
    }


    public SetParser performQuery(String query, SetParser parser, String... parameters) throws SQLException {
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

            parser.parse(rs);

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
                Logger.getLogger("Result set was null.");

            if (exception != null) throw exception;
        }

        return parser;
    }

    public List<Soldier> getHierarchy(String idMatch) throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_SELECT_SUBS_OF_BY_ID, new SetParser() {
                List<Soldier> soldiers = new ArrayList<Soldier>();

                public void parse(ResultSet raw) throws SQLException {
                    while (raw.next()) {

                        Soldier sd = new Soldier(raw.getString("soldier_id"),
                                raw.getString("name"),
                                raw.getString("rank"),
                                raw.getString("unit"),
                                raw.getString("commander"),
                                raw.getDate("birthdate"));

                        soldiers.add(sd);
                    }
                }

                public Object getCoocked() {
                    return soldiers;
                }
            },
                    idMatch).getCoocked();
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
    }

    private interface SetParser {
        void parse(ResultSet raw) throws SQLException;
        Object getCoocked();
    }

    public List<Soldier> getAllSoldiers()  throws DataAccessException {
                try {
                    return (List<Soldier>) performQuery(SQL_SELECT_ALL, new SetParser() {
                        List<Soldier> soldiers = new ArrayList<Soldier>();

                        public void parse(ResultSet raw) throws SQLException {
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
                        }

                        public Object getCoocked() {
                            return soldiers;
                        }
                    }).getCoocked();
                } catch (SQLException e) {
                    Logger.getLogger("model").error("Parsing result set error.", e);
                    e.printStackTrace();
                    throw new DataAccessException("Performing data getting failed.", e);
                }
    }

    public List<Soldier> getTopOfSoldiers()  throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_SELECT_TOP, new SetParser() {
                List<Soldier> soldiers = new ArrayList<Soldier>();

                public void parse(ResultSet raw) throws SQLException {
                    while (raw.next()) {

                        Soldier sd = new Soldier(raw.getString("soldier_id"),
                                raw.getString("name"),
                                raw.getString("rank"),
                                raw.getString("unit"),
                                raw.getString("commander"),
                                raw.getDate("birthdate"));

                        soldiers.add(sd);
                    }
                }

                public Object getCoocked() {
                    return soldiers;
                }
            }).getCoocked();
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
    }


    public Soldier getSoldierById(String idMatch) throws DataAccessException {
        try {
            return (Soldier) performQuery(SQL_SELECT_TOP, new SetParser() {
                Soldier soldier;

                public void parse(ResultSet raw) throws SQLException {
                    raw.next();

                        soldier = new Soldier(raw.getString("soldier_id"),
                                raw.getString("name"),
                                raw.getString("rank"),
                                raw.getString("unit"),
                                raw.getString("commander"),
                                raw.getDate("birthdate"));
                }

                public Object getCoocked() {
                    return soldier;
                }
            },
                    idMatch).getCoocked();
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
    }

    public List<Soldier> getSubSoldiersOfByID(String idMatch) throws DataAccessException {
        try {
            return (List<Soldier>) performQuery(SQL_SELECT_SUBS_OF_BY_ID, new SetParser() {
                List<Soldier> soldiers = new ArrayList<Soldier>();

                public void parse(ResultSet raw) throws SQLException {
                    while (raw.next()) {

                        Soldier sd = new Soldier(raw.getString("soldier_id"),
                                raw.getString("name"),
                                raw.getString("rank"),
                                raw.getString("unit"),
                                raw.getString("commander"),
                                raw.getDate("birthdate"));

                        soldiers.add(sd);
                    }
                }

                public Object getCoocked() {
                    return soldiers;
                }
            },
                    idMatch).getCoocked();
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
    }
}
