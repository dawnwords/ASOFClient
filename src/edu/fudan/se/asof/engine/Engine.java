package edu.fudan.se.asof.engine;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import edu.fudan.se.asof.felix.ServiceInjector;
import edu.fudan.se.asof.network.BundleFetcher;
import edu.fudan.se.asof.network.NetworkListener;
import edu.fudan.se.asof.util.Parameter;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Dawnwords on 2014/4/8.
 */
public class Engine {
    public static final class ParamPackage {
        public Template template;
        public Handler handler;
        public ServiceInjector injector;
        public Context context;
    }

    private ConcurrentHashMap<Template, ConcurrentLinkedQueue<Field>> templateServicesMap;

    private static Engine instance = new Engine();

    private Engine() {
        templateServicesMap = new ConcurrentHashMap<Template, ConcurrentLinkedQueue<Field>>();
    }

    public static Engine getInstance() {
        return instance;
    }

    public void interpretTemplate(ParamPackage param) {
        ConcurrentLinkedQueue<Field> services = new ConcurrentLinkedQueue<Field>();
        templateServicesMap.put(param.template, services);

        for (Field field : param.template.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ServiceDescription.class)) {
                services.add(field);
            }
        }

        param.handler = new Handler(Looper.getMainLooper());
        for (Field service : services) {
            injectDependency(param, service);
        }
    }

    private void injectDependency(ParamPackage param, Field serviceField) {
        ServiceDescription description = serviceField.getAnnotation(ServiceDescription.class);
        String bundleDir = Parameter.getInstance().getNewBundleDir().getAbsolutePath();
        new BundleFetcher(description, bundleDir, new NWListener(param, serviceField)).start();
    }

    private class NWListener implements NetworkListener<BundleFetcher.Response> {
        private ParamPackage param;
        private Field serviceField;

        private NWListener(ParamPackage param, Field serviceField) {
            this.param = param;
            this.serviceField = serviceField;
        }

        @Override
        public void onSuccess(BundleFetcher.Response response) {
            Log.debug(response.name);
            Log.debug(response.inputMatch);
            Log.debug(response.outputMatch);

            SSListener listener = new SSListener(param, response, serviceField);
            param.injector.registerServiceListener(response.name, listener);
        }

        @Override
        public void onFailure(String reason) {

        }
    }

    private class SSListener implements ServiceInjector.ServiceStartListener {
        private ParamPackage param;
        private BundleFetcher.Response response;
        private Field serviceField;

        private SSListener(ParamPackage param, BundleFetcher.Response response, Field serviceField) {
            this.param = param;
            this.response = response;
            this.serviceField = serviceField;
        }

        @Override
        public void onServiceStart(AbstractService service) {
            try {
                service.setContext(param.context);
                service.setUiHandler(param.handler);
                service.setActivityClass(response.activityClass);
                service.setInputMatch(response.inputMatch);
                service.setOutputMatch(response.outputMatch);
                serviceField.setAccessible(true);
                serviceField.set(param.template, service);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ConcurrentLinkedQueue<Field> services = templateServicesMap.get(param.template);
            services.remove(serviceField);
            if (services.size() == 0) {
                injectTemplateField("context", param.context);
                injectTemplateField("uiHandler", param.handler);
                param.template.orchestraServices();
            }
        }

        private void injectTemplateField(String fieldName, Object value) {
            try {
                Field templateContext = Template.class.getDeclaredField(fieldName);
                templateContext.setAccessible(true);
                templateContext.set(param.template, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
