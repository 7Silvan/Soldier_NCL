package ncl.military.controller.handle.executors;

import ncl.military.dao.DAO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 24.04.12
 *          Time: 13:13
 */
public class AllSoldiersOfOnitGetter extends Executor {
    public AllSoldiersOfOnitGetter(DAO dao) {
        super(dao);
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<String, Object>();

        // TODO implement Unit's Soldiers Getter

        return result;
    }
}
