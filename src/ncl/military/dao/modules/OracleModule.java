package ncl.military.dao.modules;

import ncl.military.dao.DAO;
import ncl.military.dao.contain.SoldierDA;
import ncl.military.entity.Soldier;
import oracle.jdbc.pool.OracleDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            //"select * from unit join soldier on unit = unit_id join location on location = loc_id ";
            "select * from soldier ";

    private static final String SQL_SELECT_ONE_BY_ID =
            "select * from soldier " +
                    "where soldier_id = ?";

    private static final String SQL_SELECT_SUBS_OF_BY_ID =
            "select * from soldier " +
                    "start with commander = ? " +
                    "connect by prior soldier_id = commander " +
                    "order by 1 ";

    private static final String SQL_SELECT_HIERARCHY_OF_BY_ID =
            "select sys_connect_by_path(name, \'/\') from soldier_id " +
                    "start with commander = ? " +
                    "connect by prior commander = soldier_id " +
                    "order by 1 ";

    public String getHierarchy(String idMatch) {
        String result = null;
        PreparedStatement prst = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            prst = conn.prepareStatement(SQL_SELECT_HIERARCHY_OF_BY_ID);
            prst.setString(1, idMatch);
            rs = prst.executeQuery();
            rs.next();

            result = rs.getString(1);
        } catch (SQLException e) {
            // TODO logging and throwing custom exc
            e.printStackTrace();
        }

        return result;
    }

    public List<Soldier> getAllSoldiers() {
        List<Soldier> soldiers = new ArrayList<Soldier>();
        Statement st = null;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();

            ResultSet rs = st.executeQuery(SQL_SELECT_ALL);

            while (rs.next()) {
                Soldier sd = new Soldier();
                /*sd.setName(rs.getString("name"));
                sd.setRank(rs.getString("rank"));
                sd.setUnit(rs.getString("unit"));
                sd.setCommander(rs.getString("commander"));*/
                String name = rs.getString("name");
                sd.setName(name);
                String rank = rs.getString("rank");
                sd.setRank(rank);
                String unit = rs.getString("unit");
                sd.setUnit(unit);
                String commander = rs.getString("commander");
                sd.setCommander(commander);
                Date date = rs.getDate("birthdate");
                sd.setBirthDate(date);

                soldiers.add(sd);
            }
        } catch (SQLException e) {
            // TODO logging and throwing custom exc
            e.printStackTrace();
        }  finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException e) {
                // TODO logging and throwing custom exc
                e.printStackTrace();
            }
        }
        return soldiers;
    }

    public Soldier getSoldierById(String idMatch) {
        Soldier soldier = null;
        Statement st = null;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();

            ResultSet rs = st.executeQuery(SQL_SELECT_ALL);

            rs.next();

                soldier = new Soldier();
                soldier.setName(rs.getString("name"));
                soldier.setRank(rs.getString("rank"));
                soldier.setUnit(rs.getString("unit"));
                soldier.setCommander(rs.getString("commander"));
                String dateString = rs.getString("birthdate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = (Date) sdf.parse(dateString);
                soldier.setBirthDate(date);

        } catch (SQLException e) {
            // the first exception is more important than the last one
            // TODO logging and throwing custom exc
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO logging and throwing custom exc
            e.printStackTrace();
        }  finally {
            try {
                st.close();
            } catch (SQLException e) {
                // TODO logging and throwing custom exc
                e.printStackTrace();
            } finally {
                if (conn != null)
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        // TODO logging and throwing custom exc
                        e.printStackTrace();
                    }
            }
        }
        return soldier;
    }

    public List<Soldier> getSubSoldiersOfByID(String idMatch) {
        List<Soldier> soldiers = new ArrayList<Soldier>();
        PreparedStatement prst = null;
        Connection conn = null;
        try {

            conn = dataSource.getConnection();
            prst = conn.prepareStatement(SQL_SELECT_SUBS_OF_BY_ID);
            prst.setString(1, idMatch);
            ResultSet rs = prst.executeQuery();

            while (rs.next()) {
                Soldier sd = new Soldier();
                sd.setName(rs.getString("name"));
                sd.setRank(rs.getString("rank"));
                sd.setUnit(rs.getString("unit"));
                sd.setCommander(rs.getString("commander"));
                String dateString = rs.getString("birthdate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = (Date) sdf.parse(dateString);
                sd.setBirthDate(date);

                soldiers.add(sd);
            }
        } catch (SQLException e) {
            // TODO logging and throwing custom exc
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO logging and throwing custom exc
            e.printStackTrace();
        }  finally {
            try {
                prst.close();
            } catch (SQLException e) {
                // TODO logging and throwing custom exc
                e.printStackTrace();
            }
        }
        return soldiers;
    }
}
