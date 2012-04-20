package ncl.military.controller;

import ncl.military.controller.handle.Handlable;
import ncl.military.controller.handle.HandlerFactory;
import ncl.military.dao.DAO;
import ncl.military.dao.daoFactory;
import oracle.jdbc.pool.OracleDataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Locale;
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
        dao.init();

        //Logging here
        System.out.println("ControllerServlet initializatied");
    }

    public void perfromAction(HttpServletRequest req, HttpServletResponse res) {
        String userPath = req.getServletPath();
        HttpSession session = req.getSession();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userPath", userPath);
        params.put("queriedSoldierId", req.getParameter("queriedSoldierId"));
        params.put("action", req.getParameter("action"));
        
        Handlable handle = HandlerFactory.getHandler(dao, params);
        Map<String, ? extends Object> result = handle.execute(params);
        
        for(String key : result.keySet()) {
            req.setAttribute(key, result.get(key));
            //req.getSession().setAttribute(key, result.get(key));
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
