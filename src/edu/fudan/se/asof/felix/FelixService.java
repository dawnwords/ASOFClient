package edu.fudan.se.asof.felix;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.util.Log;
import edu.fudan.se.asof.util.Parameter;
import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FelixService extends Service {
    private static final long SHUT_DOWN_WAITING_TIME = 10000;

    private Felix felix;
    private ServiceInjector serviceInjector;

    @Override
    public void onCreate() {
        Log.debug();
        super.onCreate();

        createInitBundleDir();
        createInstallBundleDir();
        createFelixCacheDir();

        // activator which loads from Res and installs to files dir and starts bundles
        InitActivator instFromR = new InitActivator(this.getResources(), getRootPath());

        // host activator for connection host app to framework
        HostActivator hostActivator = new HostActivator();

        List<BundleActivator> activatorList = new ArrayList<BundleActivator>();
        activatorList.add(hostActivator);
        activatorList.add(instFromR);

        // load Properties (from class, not from config.properties here)
        Properties configuration = new FelixConfig(getRootPath()).getConfiguration();

        // add list of activators which shall be started with system bundle to config
        configuration.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, activatorList);

        felix = new Felix(configuration);
        serviceInjector = new ServiceInjector(felix.getBundleContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.debug();

        // start felix with configProps
        try {
            felix.start();
            felix.getBundleContext().addBundleListener(new BundleListener() {
                @Override
                public void bundleChanged(BundleEvent bundleEvent) {
                    Bundle bundle = bundleEvent.getBundle();
                    int type = bundleEvent.getType();
                    if (type == BundleEvent.STARTED) {
                        String bundleLocation = bundle.getLocation();
                        Log.debug(bundleLocation);
                        BundleContext bundleContext = bundle.getBundleContext();
                        ServiceReference<AbstractService> reference = (ServiceReference<AbstractService>) bundleContext.getServiceReference(AbstractService.class.getName());
                        serviceInjector.getServiceListener(bundleLocation).onServiceStart(bundleContext.getService(reference));
                    } else if(type == BundleEvent.STOPPED){
                        try {
                            bundle.uninstall();
                        } catch (BundleException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception ex) {
            Log.debug("Could not create framework: " + ex.getMessage());
            ex.printStackTrace();
        }

        return serviceInjector;
    }

    @Override
    public synchronized void onDestroy() {
        Log.debug();
        super.onDestroy();
        shutdownApplication();
        felix = null;
    }

    private void createInitBundleDir() {
        Parameter.getInstance().setCacheDir(createClearDir("bundle"));
    }

    private void createInstallBundleDir() {
        Parameter.getInstance().setNewBundleDir(createClearDir("newbundle"));
    }

    private void createFelixCacheDir() {
        Parameter.getInstance().setCacheDir(createClearDir("cache"));
    }

    private File createClearDir(String name) {
        File dir = new File(getRootPath() + File.separator + "felix" + File.separator + name);
        if (dir.exists()) {
            this.delete(dir);
        }
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Unable to create " + name + " dir");
        }
        return dir;
    }

    private void shutdownApplication() {
        // Shut down the felix framework when stopping the host application.
        try {
            felix.stop();
            felix.waitForStop(SHUT_DOWN_WAITING_TIME);
        } catch (BundleException e) {
            e.printStackTrace();
            Log.debug("Cannot stop HostApplication");
        } catch (InterruptedException e) {
            Log.debug("Thread has waited and was then interrupted");
            e.printStackTrace();
        }
    }

    private String getRootPath() {
        return this.getFilesDir().getAbsolutePath();
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File f : files) {
                    delete(f);
                }
        }
        file.delete();
    }

}