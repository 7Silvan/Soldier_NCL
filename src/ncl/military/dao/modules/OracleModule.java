package ncl.military.dao.modules;

import ncl.military.dao.DAO;
import ncl.military.entity.Location;
import ncl.military.entity.Soldier;
import ncl.military.entity.Unit;
import oracle.jdbc.pool.OracleDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * User: Silvan
 * Date: 18.04.12
 * Time: 9:05
 */
public class OracleModule implements DAO, ServletContextListener, ServletRequestListener {

    private DataSource dataSource;

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
        OracleDataSource ds = (OracleDataSource) new InitialContext().lookup("java:/comp/env/jdbc/soldier");
        } catch (NamingException e) {
            // TODO logging here
            e.printStackTrace();
        }
    }

    public void init() {
        init(null);
    }
}
