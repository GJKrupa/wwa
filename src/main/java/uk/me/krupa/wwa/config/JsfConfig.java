package uk.me.krupa.wwa.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import uk.me.krupa.wwa.ui.jsf.MobileDeviceDetectorFilter;

import javax.faces.webapp.FacesServlet;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by krupagj on 19/03/2014.
 */
public class JsfConfig implements WebApplicationInitializer {

    @Override
    public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(MasterConfig.class);

        ServletRegistration.Dynamic jsf = servletContext.addServlet("facesServlet", FacesServlet.class);
        jsf.setLoadOnStartup(1);
        jsf.addMapping("*.xhtml");

        FilterRegistration.Dynamic filter = servletContext.addFilter("mobileResolver", new MobileDeviceDetectorFilter());
        filter.addMappingForUrlPatterns(null, false, "/");

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setAsyncSupported(true);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        dispatcher.addMapping("*.do");

        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));
    }

}
