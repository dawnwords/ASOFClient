package edu.fudan.se.asof.engine;

import edu.fudan.se.asof.felix.ServiceInjector;
import edu.fudan.se.asof.network.BundleFetcher;
import edu.fudan.se.asof.network.NetworkListener;
import edu.fudan.se.asof.util.Log;
import edu.fudan.se.asof.util.Parameter;

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

        for (Field service : services) {
            injectDependency(template, service, injector);
        }
    }

    private void injectDependency(final Template template, final Field service, ServiceInjector injector) {
        ServiceDescription description = service.getAnnotation(ServiceDescription.class);
        final String bundlePath = Parameter.getInstance().getNewBundleDir().getAbsolutePath();

        new BundleFetcher(description, bundlePath, service, template, injector, new NetworkListener<BundleFetcher.Response>() {
            @Override
            public void onSuccess(BundleFetcher.Response response) {
                Log.debug(response.name);
                Log.debug(response.inputMatch);
                Log.debug(response.outputMatch);
                ConcurrentLinkedQueue<Field> services = templateServicesMap.get(template);
                services.remove(service);
                if(services.size() == 0){
                    template.orchestraServices();
                }
            }

            @Override
            public void onFailure(String reason) {

            }
        }).start();
    }
}
