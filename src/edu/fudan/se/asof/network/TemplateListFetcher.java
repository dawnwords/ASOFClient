package edu.fudan.se.asof.network;

import edu.fudan.se.asof.util.Parameter;

/**
 * Created by Dawnwords on 2014/4/19.
 */
public class TemplateListFetcher extends NetworkThread<TemplateListFetcher.Response[]> {

    public TemplateListFetcher(NetworkListener<Response[]> listener) {
        super(listener, Response[].class, Parameter.TEMPLATE_LIST_URL);
    }

    @Override
    protected void postExecute(Response[] response) {
        super.postExecute(response);
    }

    public static class Response {
        public String file, template, description;
    }
}
