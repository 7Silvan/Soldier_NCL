package ncl.military.controller.handle;

import java.util.Map;


/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 17:06
 */
public class Handler implements Handlable {
    private Executable executor;
    private String view;
    private String path;
    private String action;

    public Executable getExecutor() {
        return executor;
    }

    public Handler(Executable executor, String path, String action, String view) {
        this.executor = executor;
        this.path = path;
        this.view = view;
        this.action = action;
    }

    public Map<String, Object> execute(Map<String, Object> params) {
        return executor.execute(params);
    }

    public String getView() {
        return view;
    }
}
