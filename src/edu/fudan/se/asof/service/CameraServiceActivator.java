package edu.fudan.se.asof.service;

import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.engine.ReturnType;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by Dawnwords on 2014/4/20.
 */
public class CameraServiceActivator extends AbstractService implements BundleActivator {
    @Override
    protected ReturnType invoke(Object... input) {
        return null;
    }

    @Override
    public void start(BundleContext context) throws Exception {

    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
