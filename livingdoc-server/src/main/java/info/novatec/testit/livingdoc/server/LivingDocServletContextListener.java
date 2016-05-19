package info.novatec.testit.livingdoc.server;

import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.dom4j.DocumentException;

import info.novatec.testit.livingdoc.server.configuration.ServerConfiguration;
import info.novatec.testit.livingdoc.server.database.hibernate.BootstrapData;
import info.novatec.testit.livingdoc.server.database.hibernate.HibernateSessionService;
import info.novatec.testit.livingdoc.util.URIUtil;


public class LivingDocServletContextListener implements ServletContextListener {
    private static String CONFIG_FILE = "livingdoc-server.cfg.xml";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        ctx.log("******* Mounting up LivingDoc-Server");
        try {
            URL url = LivingDocServletContextListener.class.getClassLoader().getResource(CONFIG_FILE);
            Properties sProperties = ServerConfiguration.load(url).getProperties();
            injectAdditionalProperties(ctx, sProperties);

            HibernateSessionService service = new HibernateSessionService(sProperties);
            ctx.setAttribute(ServletContextKeys.SESSION_SERVICE, service);

            ctx.log("Bootstrapping data");
            new BootstrapData(service, sProperties).execute();

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (LivingDocServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        HibernateSessionService service = ( HibernateSessionService ) ctx.getAttribute(ServletContextKeys.SESSION_SERVICE);
        if (service != null) {
            service.close();
        }
    }

    private void injectAdditionalProperties(ServletContext ctx, Properties sProperties) {
        String dialect = ctx.getInitParameter("hibernate.dialect");
        if (dialect != null) {
            sProperties.setProperty("hibernate.dialect", dialect);
        }
        if (ctx.getRealPath("/") != null) {
            sProperties.setProperty("baseUrl", URIUtil.decoded(ctx.getRealPath("/")));
        }
    }
}
