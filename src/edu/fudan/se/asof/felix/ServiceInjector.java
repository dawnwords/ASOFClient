package edu.fudan.se.asof.felix;

import android.os.Binder;
import edu.fudan.se.asof.engine.AbstractService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dawnwords on 2014/4/15.
 */
public final class ServiceInjector extends Binder {
    public static interface ServiceStartListener {
        void onServiceStart(AbstractService service);
    }

    private ConcurrentHashMap<String, ServiceStartListener> bundleListenerMap;
    private BundleContext context;

    ServiceInjector(BundleContext context) {
        this.context = context;
        bundleListenerMap = new ConcurrentHashMap<String, ServiceStartListener>();
    }

    ServiceStartListener getServiceListener(String bundlePath) {
        return bundleListenerMap.get(bundlePath);
    }

    public void registerServiceListener(String bundlePath, ServiceStartListener listener) {
        bundleListenerMap.put(bundlePath, listener);
    }

    public void registerServiceListener(String bundlePath) {
        bundleListenerMap.remove(bundlePath);
        Bundle bundle = context.getBundle(bundlePath);
        try {
            bundle.stop();
        } catch (BundleException e) {
            e.printStackTrace();
        }
    }

}
