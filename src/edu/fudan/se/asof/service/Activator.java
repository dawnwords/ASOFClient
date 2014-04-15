package edu.fudan.se.asof.service;

import edu.fudan.se.asof.engine.AbstractService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by Dawnwords on 2014/4/15.
 */
public class Activator implements AbstractService, BundleActivator {
    @Override
    public Object invoke(Object... input) {
        String caller = (String) input[0];
        System.out.println("Hello " + caller + "!");
        return null;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        bundleContext.registerService(AbstractService.class.getName(), this, null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}
