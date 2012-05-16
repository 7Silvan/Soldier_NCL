package ncl.military.controller.handle;

import ncl.military.controller.exceptions.HandlerException;
import ncl.military.controller.handle.executors.Executor;
import ncl.military.dao.DAO;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gural
 * @version 1.0
 *          Date: 13.04.12
 *          Time: 13:52
 */
public class HandlerFactory {

    private static final Logger log = Logger.getLogger("controller");

    private static Map<String, Executable> executors = new HashMap<String, Executable>();
    private static ServletContext context = null;
    private static ServletConfig config = null;

    public static void init(ServletConfig configInit, ServletContext contextInit) {
        config = configInit;
        context = contextInit;
    }

    public static ServletContext getContext() {
        return context;
    }

    public static ServletConfig getConfig() {
        return config;
    }

    public static Handlable getHandler(DAO dao, Map<String, Object> params) throws HandlerException {

        // <userPath>:<action> - get class name
        // <userPath>: - get action name
        // <userPath> - get view name
        // :<action> - get view name
        // action:<action> - get value of action name

        Executable executable = null;
        String view = null;
        log.debug("onGetHandler \nuserPath => " + params.get("userPath") + "\naction => " + params.get("action"));
        if (params.get("action") != null) {
            view = config.getInitParameter(":" + (String) params.get("action"));
        } else {
            params.put("action", config.getInitParameter((String) params.get("userPath") + ":"));
            log.debug("action got => " + params.get("action"));
            view = config.getInitParameter((String) params.get("userPath"));
        }
        log.debug("PATIENT!!!!!!!!!!!!!!!!!!!!!!view got => " + view);

        String executorSpec = params.get("userPath") + ":" + params.get("action");
        log.debug("executorSpec => " + executorSpec);
        List<String> executorClassHierarchy = new ArrayList<String>();
        try {
            String executorClass = null;

            // 0_o
            do {
                if (executorClass != null)
                    executorSpec = "up." + executorSpec;
                executorClass = config.getInitParameter(executorSpec);
                if (executorClass != null)
                    executorClassHierarchy.add(executorClass);
                else
                    break;
            } while (executorClass != null);

            if (executorClassHierarchy.size() == 0)
                throw new IllegalStateException("Descriptor have no match for given param: " + executorSpec);
            log.debug("executorClass => " + executorClass);

            //cyclic creating
            for (String executorClassName : executorClassHierarchy) {
                log.debug("Finding class for: " + executorClassName);
                if (executable == null) {
                    if (executors.containsKey(executorClassName)) {
                        executable = executors.get(executorClassName);
                    } else {
                        Class executor = Class.forName(executorClassName);
                        Constructor executorConstructor = executor.getConstructor(DAO.class);
                        executable = (Executable) executorConstructor.newInstance(dao);
                    }
                } else {
                    if (executors.containsKey(executorClassName)) {
                        // hmm
                        executable = ((Executor) executors.get(executorClassName)).setExecutor((Executor) executable);
                    } else {
                        Class executor = Class.forName(executorClassName);
                        Constructor executorConstructor = executor.getConstructor(Executor.class);
                        executable = (Executable) executorConstructor.newInstance(executable);
                    }
                }
            }

        } catch (IllegalStateException e) {
            log.error(e.getMessage(), e);
            throw new HandlerException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error("Could not load class with given name.", e);
            throw new HandlerException("Could not load class with given name.", e);
        } catch (NoSuchMethodException e) {
            log.error("There is no such constructor in loaded class.", e);
            throw new HandlerException("There is no such constructor in loaded class.", e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage(), e);
            throw new HandlerException(e.getMessage(), e);
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
            throw new HandlerException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new HandlerException(e.getMessage(), e);
        }

        if (executable == null)
            throw new IllegalStateException("Did not matched executor");
        return new Handler(executable, (String) params.get("userPath"), (String) params.get("action"), view);
    }
}
