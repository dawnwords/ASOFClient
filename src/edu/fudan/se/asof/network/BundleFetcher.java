package edu.fudan.se.asof.network;


import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.engine.ServiceDescription;
import edu.fudan.se.asof.engine.Template;
import edu.fudan.se.asof.felix.ServiceInjector;
import edu.fudan.se.asof.util.Parameter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawnwords on 2014/4/8.
 */
public class BundleFetcher extends Thread {
    private String bundlePath;
    private Field serviceField;
    private Template template;
    private Request request;
    private ServiceInjector injector;
    private NetworkListener<Response> listener;

    public BundleFetcher(ServiceDescription bundleDescription, String bundlePath,
                         Field serviceField, Template template, ServiceInjector injector,
                         NetworkListener<Response> listener) {
        this.serviceField = serviceField;
        this.injector = injector;
        this.template = template;
        this.request = constructRequest(bundleDescription);
        this.bundlePath = bundlePath;
        this.listener = listener;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Parameter.BUNDLE_DOWNLOAD_SERVER_URL);
            httpPost.setEntity(new UrlEncodedFormEntity(getRequestNVPairs(), HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                Response bundle = Parameter.getGson().fromJson(reader, Response.class);
                saveToFile(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure(e.getMessage());
        } finally {
            close(reader);
        }
    }

    private List<NameValuePair> getRequestNVPairs() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        String json = Parameter.getGson().toJson(request, Request.class);
        nameValuePairs.add(new BasicNameValuePair("service_description", json));
        return nameValuePairs;
    }

    private Request constructRequest(ServiceDescription bundleDescription) {
        Request result = new Request();
        result.description = bundleDescription.description();
        result.input = bundleDescription.input();
        result.output = bundleDescription.output();
        return result;
    }

    private void saveToFile(final Response bundle) {
        FileOutputStream out = null;
        final String bundlePath = this.bundlePath + File.separator + bundle.name;
        injector.registerServiceListener(bundlePath, new ServiceInjector.ServiceListener() {
            @Override
            public void onServiceStart(AbstractService service) {
                try {
                    serviceField.setAccessible(true);
                    serviceField.set(template, service);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                listener.onSuccess(bundle);
            }
        });

        try {
            out = new FileOutputStream(bundlePath);
            out.write(bundle.bundleFile);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(out);
            bundle.bundleFile = null;
        }
    }

    private void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Response {
        public int[] inputMatch, outputMatch;
        public String name;
        private byte[] bundleFile;
    }

    class Request {
        String description;
        String[] input, output;
    }
}

