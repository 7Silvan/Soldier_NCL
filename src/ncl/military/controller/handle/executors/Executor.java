package ncl.military.controller.handle.executors;

import ncl.military.controller.handle.Executable;
import ncl.military.dao.DAO;

/**
 * @author gural
 * @version 1.0
 *          Date: 20.04.12
 *          Time: 11:37
 */
public abstract class Executor implements Executable {

    private DAO dao;

    public Executor(DAO dao) {
        this.dao = dao;
    }

    protected DAO getDao() {
        return dao;
    }

}
