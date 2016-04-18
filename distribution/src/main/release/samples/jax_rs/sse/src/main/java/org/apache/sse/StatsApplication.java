package org.apache.sse;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.cxf.jaxrs.sse.OutboundSseEventBodyWriter;
import org.apache.cxf.jaxrs.sse.SseEventOutputProvider;
import org.apache.cxf.jaxrs.sse.atmosphere.SseAtmosphereContextProvider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@ApplicationPath( "api" )
public class StatsApplication extends Application {
    @Inject private StatsRestServiceImpl statsRestService;
    
    @Override
    public Set<Object> getSingletons() {
        final Set<Object> singletons = new HashSet<>();
        singletons.add(new SseAtmosphereContextProvider());
        singletons.add(new SseEventOutputProvider());
        singletons.add(statsRestService);
        singletons.add(new OutboundSseEventBodyWriter());
        singletons.add(new JacksonJsonProvider());
        return singletons;
    }
}
