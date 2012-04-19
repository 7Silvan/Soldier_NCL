package ncl.military.dao.contain;

import ncl.military.entity.Soldier;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gural
 * @version 1.0
 *          Date: 18.04.12
 *          Time: 18:56
 */

// is not using now
public class SoldierContainer implements SoldierDA{
    
    private Connection conn;
    
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

    public SoldierContainer(Connection conn) {
        super();
        this.conn = conn;
    }

    public void close() throws SQLException {
        conn.close();
    }

    public String getHierarchy(String idMatch) {
        String result = null;
        PreparedStatement prst = null;
        ResultSet rs = null;

        try {
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
        try {
            st = conn.createStatement();
       
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL);

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
            st.close();
          } catch (SQLException e) {
              // TODO logging and throwing custom exc
              e.printStackTrace();
          }
        }
        return soldiers;
    }

    public Soldier getSoldierById(String idMatch) {
        throw new UnsupportedOperationException("not implemented yet.");
        //return null;//To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Soldier> getSubSoldiersOfByID(String idMatch) {
        List<Soldier> soldiers = new ArrayList<Soldier>();
        PreparedStatement prst = null;
        try {

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

    public List<Soldier> getSoldiersById(String idMatch) {
        throw new UnsupportedOperationException("not implemented yet.");
        //return null;//To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Soldier> getSoldiersByName(String nameMatch) {
        throw new UnsupportedOperationException("not implemented yet.");
        //return null;//To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Soldier> getSoldiersByRank(String rankMatch) {
        throw new UnsupportedOperationException("not implemented yet.");
        //return null;//To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Soldier> getSoldiersByUnit(String unitMatch) {
        throw new UnsupportedOperationException("not implemented yet.");
        //return null;//To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Soldier> getSoldiersByCommander(String commanderMatch) {
        throw new UnsupportedOperationException("not implemented yet.");
        //return null;//To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Soldier> getSoldiersByBirthDate(String BirthDateMatch) {
        throw new UnsupportedOperationException("not implemented yet.");
        //return null;//To change body of implemented methods use File | Settings | File Templates.
    }
}
