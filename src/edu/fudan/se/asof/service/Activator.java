package edu.fudan.se.asof.service;

import android.widget.Toast;
import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.engine.ReturnType;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by Dawnwords on 2014/4/15.
 */
public class Activator extends AbstractService implements BundleActivator {
    @Override
    public ReturnType invoke(Object... input) {
        String caller = (String) input[0];
        Toast.makeText(getContext(),"Hello " + caller,Toast.LENGTH_SHORT).show();
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
