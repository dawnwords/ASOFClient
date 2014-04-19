package edu.fudan.se.asof.network;

import edu.fudan.se.asof.util.IOUtil;
import edu.fudan.se.asof.util.Parameter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.List;

/**
 * Created by Dawnwords on 2014/4/19.
 */
public class TemplateFetcher extends NetworkThread<TemplateFetcher.Response> {

    private String template;

    public TemplateFetcher(String template, NetworkListener<Response> listener) {
        super(listener, Response.class, Parameter.TEMPLATE_DOWNLOAD_URL);
        this.template = template;
    }

    @Override
    protected void setNameValuePair(List<NameValuePair> nameValuePairs) {
        nameValuePairs.add(new BasicNameValuePair("template", template));
    }

    @Override
    protected void postExecute(Response response) {
        String path = Parameter.getInstance().getTemplateDir() + File.separator + template;
        IOUtil.saveToFile(path, response.file);
        super.postExecute(response);
    }

    public static class Response {
        private byte[] file;
        public String clazz;
    }
}
