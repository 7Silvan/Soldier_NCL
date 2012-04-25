package ncl.military.controller;

import ncl.military.controller.handle.Handlable;
import ncl.military.controller.handle.HandlerFactory;
import ncl.military.dao.DAO;
import ncl.military.dao.DAOFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Silvan
 * Date: 18.04.12
 * Time: 7:44
 */
public class ControllerServlet extends HttpServlet {

    private DAO dao = null;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        Map<String, String> initParams = new HashMap<String, String>();
        Enumeration names = servletConfig.getServletContext().getInitParameterNames();
        while (names.hasMoreElements()) {
            String parameter = (String) names.nextElement();
            initParams.put(parameter, servletConfig.getServletContext().getInitParameter(parameter));
        }


        dao = DAOFactory.getDao();
        dao.init(initParams);

        Logger.getLogger("controller").info("ControllerServlet initialized");
    }

    public void perfromAction(HttpServletRequest req, HttpServletResponse res) {
        String userPath = req.getServletPath();
        HttpSession session = req.getSession();

        Map<String, Object> params = new HashMap<String, Object>();

//        while (req.getParameterNames().hasMoreElements()) {
//            String key = (String) req.getParameterNames().nextElement();
//            params.put(key, req.getParameter(key));
//        }

        // TODO delete hard-code
        params.put("userPath", userPath);
        params.put("queriedSoldierId", req.getParameter("queriedSoldierId"));
        params.put("queriedUnitId", req.getParameter("queriedUnitId"));
        params.put("queriedLocationId", req.getParameter("queriedLocationId"));
        params.put("queriedSoldierName", req.getParameter("queriedSoldierName"));
        params.put("action", req.getParameter("action"));

        //params.put("userPath", userPath);

        Handlable handle = HandlerFactory.getHandler(dao, params);
        Map<String, ? extends Object> result = handle.execute(params);

        //req.getParameterMap().putAll(result);  //java.lang.IllegalStateException: No modifications are allowed to a locked ParameterMap

        for (String key : result.keySet()) {
            req.setAttribute(key, result.get(key));
        }

        try {
            req.getRequestDispatcher(handle.getView()).forward(req, res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        perfromAction(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        perfromAction(request, response);
    }
}
