package edu.fudan.se.asof.felix;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.engine.Log;
import edu.fudan.se.asof.engine.ResultHolder;
import edu.fudan.se.asof.util.Parameter;
import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FelixService extends Service {

    private Felix felix;
    private Handler handler;
    private ServiceInjector serviceInjector;

    @Override
    public void onCreate() {
        Log.debug();
        super.onCreate();

        createInitBundleDir();
        createInstallBundleDir();
        createFelixCacheDir();
        createTemplateDir();
        createOptimizedDir();

        List<BundleActivator> activatorList = new ArrayList<BundleActivator>();
        activatorList.add(new InitActivator(getResources()));

        Properties configuration = new FelixConfig(getRootPath()).getConfiguration();
        configuration.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, activatorList);

        felix = new Felix(configuration);
        handler = new Handler(Looper.getMainLooper());
        serviceInjector = new ServiceInjector(felix.getBundleContext());
    }

    @Override
    public synchronized void onDestroy() {
        Log.debug();
        super.onDestroy();
        shutdownApplication();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.debug();
        try {
            felix.start();
            felix.getBundleContext().addBundleListener(new BundleListener() {
                @Override
                public void bundleChanged(BundleEvent bundleEvent) {
                    Bundle bundle = bundleEvent.getBundle();
                    int type = bundleEvent.getType();
                    if (type == BundleEvent.STARTED) {
                        String bundleName = new File(bundle.getLocation()).getName();
                        BundleContext bundleContext = bundle.getBundleContext();
                        AbstractService abstractService = (AbstractService) bundleContext.getService(bundle.getRegisteredServices()[0]);
                        serviceInjector.getServiceListener(bundleName).onServiceStart(abstractService);
                    } else if (type == BundleEvent.STOPPED) {
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

    private void shutdownApplication() {
        try {
            final ResultHolder<Void> lock = new ResultHolder<Void>();
            felix.getBundleContext().addFrameworkListener(new FrameworkListener() {
                @Override
                public void frameworkEvent(FrameworkEvent e) {
                    if (e.getType() == FrameworkEvent.STOPPED) {
                        lock.set(null);
                    }
                }
            });
            felix.stop();
            lock.get();
            felix = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createInitBundleDir() {
        Parameter.getInstance().setInitBundleDir(createClearDir("bundle"));
    }

    private void createInstallBundleDir() {
        Parameter.getInstance().setNewBundleDir(createClearDir("newbundle"));
    }

    private void createFelixCacheDir() {
        Parameter.getInstance().setCacheDir(createClearDir("cache"));
    }

    private void createTemplateDir() {
        Parameter.getInstance().setTemplateDir(createClearDir("template"));
    }

    private void createOptimizedDir() {
        Parameter.getInstance().setOptimizedDir(createClearDir("optimized"));
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

    private String getRootPath() {
        return getFilesDir().getAbsolutePath();
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