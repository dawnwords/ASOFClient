package edu.fudan.se.asof.network;

import edu.fudan.se.asof.util.IOUtil;
import edu.fudan.se.asof.util.Parameter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawnwords on 2014/4/19.
 */
public class NetworkThread<Response> extends Thread {

    private NetworkListener<Response> listener;
    private Class<Response> clazz;
    private String url;

    public NetworkThread(NetworkListener<Response> listener, Class<Response> clazz, String url) {
        this.listener = listener;
        this.clazz = clazz;
        this.url = url;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            setNameValuePair(nameValuePairs);

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                Response response = Parameter.getGson().fromJson(reader, clazz);
                postExecute(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure(e.getMessage());
        } finally {
            IOUtil.close(reader);
        }
    }

    protected void setNameValuePair(List<NameValuePair> nameValuePairs){
    }

    protected void postExecute(Response response){
        listener.onSuccess(response);
    }
}
