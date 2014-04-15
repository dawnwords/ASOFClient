package edu.fudan.se.asof.util;

import android.util.Log;
import com.google.gson.Gson;

import java.io.File;

/**
 * Created by Dawnwords on 2014/4/12.
 */
public class Parameter {
    public static final String BUNDLE_DOWNLOAD_SERVER_URL = "http://10.131.253.211:8080/BundleService/BundleServlet";

    private static Parameter ourInstance = new Parameter();
    private static Gson gson;

    private File initBundleDir, newBundleDir, cacheDir;

    public static Gson getGson() {
        return gson;
    }

    public static Parameter getInstance() {
        return ourInstance;
    }



    private Parameter() {
        gson = new Gson();
    }

    public File getInitBundleDir() {
        return initBundleDir;
    }

    public void setInitBundleDir(File initBundleDir) {
        this.initBundleDir = initBundleDir;
    }

    public File getNewBundleDir() {
        return newBundleDir;
    }

    public void setNewBundleDir(File newBundleDir) {
        this.newBundleDir = newBundleDir;
    }

    public File getCacheDir() {
        return cacheDir;
    }

    public void setCacheDir(File cacheDir) {
        this.cacheDir = cacheDir;
    }
}
