package ncl.military.controller;

import ncl.military.controller.handle.Handlable;
import ncl.military.controller.handle.HandlerFactory;
import ncl.military.dao.DAO;
import ncl.military.dao.daoFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Silvan
 * Date: 18.04.12
 * Time: 7:44
 */
public class ControllerServlet extends HttpServlet{

    private DAO dao = null;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        dao = daoFactory.getDao();
        dao.init(); // TODO put initParams for dataBase here please (get to know if we can use resources scope from web.xml)

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

        params.put("userPath", userPath);
        params.put("queriedSoldierId", req.getParameter("queriedSoldierId"));
        params.put("action", req.getParameter("action"));

            //params.put("userPath", userPath);
        
        Handlable handle = HandlerFactory.getHandler(dao, params);
        Map<String, ? extends Object> result = handle.execute(params);
        
        //req.getParameterMap().putAll(result);  //java.lang.IllegalStateException: No modifications are allowed to a locked ParameterMap

        for(String key : result.keySet()) {
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
     * @param request servlet request
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
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        perfromAction(request, response);
    }
}
