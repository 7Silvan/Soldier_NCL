package ncl.military.dao.modules;

import ncl.military.dao.DAO;
import ncl.military.dao.contain.SoldierDA;
import ncl.military.dao.exceptions.DataAccessException;
import ncl.military.entity.Soldier;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: Silvan
 * Date: 18.04.12
 * Time: 9:05
 */
public class OracleModule implements DAO, SoldierDA {

    private OracleDataSource dataSource;

    private String message = "Not connected.";

    public String toString() {
        return message;
    }

    public String getMessage() {
        return message;
    }

    public void test() {
        Connection conn = null;
        ResultSet rst = null;
        Statement stmt = null;
        try {

            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            OracleDataSource ds = (OracleDataSource) envContext.lookup("jdbc/soldier");

            if (envContext == null) throw new Exception("Error: No Context");
            if (ds == null) throw new Exception("Error: No DataSource");
            if (ds != null) conn = ds.getConnection();
            if (conn != null) {
                message = "Got Connection " + conn.toString() + ", ";
                stmt = conn.createStatement();
                rst = stmt.executeQuery("SELECT 'successful connection' FROM DUAL");
            }
            if (rst.next()) message = rst.getString(1);

            rst.close();
            rst = null;
            stmt.close();
            stmt = null;
            conn.close(); // Return to connection pool
            conn = null; // Make sure we don't close it twice
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Always make sure result sets and statements are closed,
            // and the connection is returned to the pool

            try {
                if (rst != null) rst.close();
            } catch (SQLException e) {
                ;
            } finally {
                try {
                    if (stmt != null) stmt.close();
                } catch (SQLException e) {
                    ;
                } finally {
                    try {
                        if (conn != null) conn.close();
                    } catch (SQLException e) {
                        ;
                    }
                }
            }
        }
    }

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
            "select sys_connect_by_path(name, \'/\') from soldier_id " +
                    "start with commander = ? " +
                    "connect by prior commander = soldier_id " +
                    "order by 1 ";

    public String getHierarchy(String idMatch) throws DataAccessException {
        try {
            return (String) performQuery(SQL_SELECT_HIERARCHY_OF_BY_ID, new SetParser() {
                
                String result;
                
                public void parse(ResultSet raw) throws SQLException {
                    raw.next();
                    result = raw.getString(1);
                }

                public Object getCoocked() {
                    return result;
                }
            },
                    idMatch).getCoocked();
        } catch (SQLException e) {
            Logger.getLogger("model").error("Parsing result set error.", e);
            e.printStackTrace();
            throw new DataAccessException("Performing data getting failed.", e);
        }
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


//    public List<Soldier> getTopOfSoldiers() {
//        List<Soldier> soldiers = new ArrayList<Soldier>();
//        Statement st = null;
//        Connection conn = null;
//        try {
//            conn = dataSource.getConnection();
//            st = conn.createStatement();
//
//            ResultSet rs = st.executeQuery(SQL_SELECT_TOP);
//
//            while (rs.next()) {
//                Soldier sd = new Soldier(rs.getString("soldier_id"),rs.getString("name"),rs.getString("rank"), rs.getString("unit"),rs.getString("commander"), rs.getDate("birthdate"));
//
//                soldiers.add(sd);
//            }
//        } catch (SQLException e) {
//            // TODO logging and throwing custom exc
//            e.printStackTrace();
//        }  finally {
//            try {
//                if (st != null)
//                    st.close();
//            } catch (SQLException e) {
//                // TODO logging and throwing custom exc
//                e.printStackTrace();
//            }
//        }
//        return soldiers;
//    }


}
