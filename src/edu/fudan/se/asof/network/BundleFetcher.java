package edu.fudan.se.asof.network;


import edu.fudan.se.asof.engine.ServiceDescription;
import edu.fudan.se.asof.util.IOUtil;
import edu.fudan.se.asof.util.Parameter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.List;

/**
 * Created by Dawnwords on 2014/4/8.
 */
public class BundleFetcher extends NetworkThread<BundleFetcher.Response> {
    private String bundleDir;
    private Request request;

    public BundleFetcher(ServiceDescription bundleDescription, String bundleDir,
                         NetworkListener<Response> listener) {
        super(listener,Response.class,Parameter.BUNDLE_DOWNLOAD_URL);
        this.request = constructRequest(bundleDescription);
        this.bundleDir = bundleDir;
    }

    @Override
    protected void setNameValuePair(List<NameValuePair> nameValuePairs) {
        String json = Parameter.getGson().toJson(request, Request.class);
        nameValuePairs.add(new BasicNameValuePair("service_description", json));
    }

    @Override
    protected void postExecute(Response bundle) {
        super.postExecute(bundle);
        String bundlePath = this.bundleDir + File.separator + bundle.name;
        IOUtil.saveToFile(bundlePath, bundle.bundleFile);
    }

    private Request constructRequest(ServiceDescription bundleDescription) {
        Request result = new Request();
        result.description = bundleDescription.description();
        result.input = bundleDescription.input();
        result.output = bundleDescription.output();
        return result;
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

