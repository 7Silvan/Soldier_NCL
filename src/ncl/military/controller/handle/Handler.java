package ncl.military.controller.handle;

import ncl.military.dao.DAO;

import java.util.Map;


/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 17:06
 */
public class Handler implements Handlable {
    private DAO dao;
    private Executable executor;
    private String viewName;
    private String action;

    public DAO getDao() {
        return dao;
    }

    public Executable getExecutor() {
        return executor;
    }

    public Handler(DAO dao, Executable executor, String viewName) {
        this.dao = dao;
        this.executor = executor;
        this.viewName = viewName;
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        params.put("dao", (Object) dao);
        return executor.execute(params);
    }

    public String returnView() {
        return viewName;
    }
}
