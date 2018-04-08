
/**

package edu.neu.bsds.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import edu.neu.bsds.resources.Assignment2Server;
import edu.neu.bsds.resources.HelloWorld;

/**
 * Created by jessicamalloy on 4/2/18.

public class CustomApplication extends Application {
    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> resources = new HashSet<Class<?>>();

        //register REST modules
        resources.add(HelloWorld.class);
        resources.add(Assignment2Server.class);

        //Manually adding MOXyJSONFeature
        resources.add(org.glassfish.jersey.moxy.json.MoxyJsonFeature.class);


        return resources;
    }
}
**/