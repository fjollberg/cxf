package org.apache.sse;

import org.apache.cxf.cdi.CXFCdiServlet;
import org.apache.cxf.jaxrs.sse.atmosphere.SseAtmosphereInterceptor;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.handler.ReflectorServletProcessor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.weld.environment.servlet.BeanManagerResourceBindingListener;
import org.jboss.weld.environment.servlet.Listener;

public class StatsServer {    
    public static void main( final String[] args ) throws Exception {
        final Server server = new Server( 8686 );

        final ServletHolder staticHolder = new ServletHolder(new DefaultServlet());
        final ServletContextHandler staticContext = new ServletContextHandler();
        staticContext.setContextPath( "/static" );
        staticContext.addServlet(staticHolder, "/*");
        staticContext.setResourceBase(StatsServer.class.getResource("/web-ui").toURI().toString());

         // Register and map the dispatcher servlet
        final CXFCdiServlet cxfServlet = new CXFCdiServlet();
        
        final AtmosphereServlet atmosphereServlet = new AtmosphereServlet(true);
        atmosphereServlet.framework().addAtmosphereHandler("/*", new ReflectorServletProcessor(cxfServlet));
        atmosphereServlet.framework().interceptor(new SseAtmosphereInterceptor());
        
        ServletHolder atmosphereServletHolder = new ServletHolder(atmosphereServlet);
        atmosphereServletHolder.setInitParameter(ApplicationConfig.PROPERTY_NATIVE_COMETSUPPORT, "true");
        atmosphereServletHolder.setInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");
        atmosphereServletHolder.setInitParameter(ApplicationConfig.DISABLE_ATMOSPHEREINTERCEPTOR, "true");
        atmosphereServletHolder.setInitParameter(ApplicationConfig.CLOSE_STREAM_ON_CANCEL, "true");
        atmosphereServletHolder.setAsyncSupported(true);
       
        final ServletHolder servletHolder = new ServletHolder( cxfServlet );
        final ServletContextHandler context = new ServletContextHandler();
        context.setContextPath( "/" );
        context.addEventListener( new Listener() );
        context.addEventListener( new BeanManagerResourceBindingListener() );
        context.addServlet(atmosphereServletHolder, "/rest/*");

        servletHolder.setInitParameter("redirects-list", "/ /index.html /.*[.]js");
        servletHolder.setInitParameter("redirect-servlet-name", staticHolder.getName());
        servletHolder.setInitParameter("redirect-attributes", "javax.servlet.include.request_uri");

        HandlerList handlers = new HandlerList();
        handlers.addHandler(staticContext);
        handlers.addHandler(context);
        
        server.setHandler(handlers);
        server.start();        
        server.join();    
    }
}

