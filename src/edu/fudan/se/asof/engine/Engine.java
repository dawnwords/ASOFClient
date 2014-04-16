package edu.fudan.se.asof.engine;

import android.os.Handler;
import edu.fudan.se.asof.felix.ServiceInjector;
import edu.fudan.se.asof.network.BundleFetcher;
import edu.fudan.se.asof.network.NetworkListener;
import edu.fudan.se.asof.util.Log;
import edu.fudan.se.asof.util.Parameter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Dawnwords on 2014/4/8.
 */
public class Engine {
    private ConcurrentHashMap<Template, ConcurrentLinkedQueue<Field>> templateServicesMap;

    private static Engine instance = new Engine();

    private Engine() {
        templateServicesMap = new ConcurrentHashMap<Template, ConcurrentLinkedQueue<Field>>();
    }

    public static Engine getInstance() {
        return instance;
    }

    public void interpretTemplate(Template template, ServiceInjector injector) {
        ConcurrentLinkedQueue<Field> services = new ConcurrentLinkedQueue<Field>();
        templateServicesMap.put(template, services);

        for (Field field : template.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ServiceDescription.class)) {
                services.add(field);
            }
        }

        Handler handler = new Handler();
        for (Field service : services) {
            injectDependency(template, service, injector, handler);
        }
    }

    private void injectDependency(final Template template, final Field serviceField, final ServiceInjector injector, final Handler handler) {
        ServiceDescription description = serviceField.getAnnotation(ServiceDescription.class);
        final String bundleDir = Parameter.getInstance().getNewBundleDir().getAbsolutePath();

        new BundleFetcher(description, bundleDir, new NetworkListener<BundleFetcher.Response>() {
            @Override
            public void onSuccess(BundleFetcher.Response response) {
                Log.debug(response.name);
                Log.debug(response.inputMatch);
                Log.debug(response.outputMatch);

                String bundlePath = bundleDir + File.separator + response.name;
                SSListener listener = new SSListener(response.inputMatch, response.outputMatch, serviceField, template, handler);
                injector.registerServiceListener(bundlePath, listener);
            }

            @Override
            public void onFailure(String reason) {

            }
        }).start();
    }

    private class SSListener implements ServiceInjector.ServiceStartListener {

        private int[] inputMatch, outputMatch;
        private Field serviceField;
        private Template template;
        private Handler handler;

        private SSListener(int[] inputMatch, int[] outputMatch, Field serviceField, Template template, Handler handler) {
            this.inputMatch = inputMatch;
            this.outputMatch = outputMatch;
            this.serviceField = serviceField;
            this.template = template;
            this.handler = handler;
        }

        @Override
        public void onServiceStart(AbstractService service) {
            try {
                service.setInputMatch(inputMatch);
                service.setOutputMatch(outputMatch);
                serviceField.setAccessible(true);
                serviceField.set(template, service);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            ConcurrentLinkedQueue<Field> services = templateServicesMap.get(template);
            services.remove(serviceField);
            if (services.size() == 0) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        template.orchestraServices();
                    }
                });
            }
        }
    }
}
