package ncl.military.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author gural
 * @version 1.0
 *          Date: 19.04.12
 *          Time: 10:37
 */
public class Log4jInit implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String homeDir = servletContextEvent.getServletContext().getRealPath("/");
        DOMConfigurator.configure(homeDir + "/WEB-INF/log4j.xml");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
