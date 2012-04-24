package ncl.military.dao;

import ncl.military.dao.modules.OracleModule;

/**
 * User: Silvan
 * Date: 18.04.12
 * Time: 9:04
 */
public class DAOFactory {
    public static DAO getDao() {
        return new OracleModule();
    }
}
