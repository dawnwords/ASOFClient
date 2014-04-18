package edu.fudan.se.asof.network;


import edu.fudan.se.asof.engine.ServiceDescription;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawnwords on 2014/4/8.
 */
public class BundleFetcher extends Thread {
    private String bundleDir;
    private Request request;
    private NetworkListener<Response> listener;

    public BundleFetcher(ServiceDescription bundleDescription, String bundleDir,
                         NetworkListener<Response> listener) {
        this.request = constructRequest(bundleDescription);
        this.bundleDir = bundleDir;
        this.listener = listener;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Parameter.BUNDLE_DOWNLOAD_URL);
            httpPost.setEntity(new UrlEncodedFormEntity(getRequestNVPairs(), HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                Response bundle = Parameter.getGson().fromJson(reader, Response.class);
                listener.onSuccess(bundle);
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
        final String bundlePath = this.bundleDir + File.separator + bundle.name;

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

