package edu.fudan.se.asof.util;

import com.google.gson.Gson;

import java.io.File;

/**
 * Created by Dawnwords on 2014/4/12.
 */
public class Parameter {
    private static final String SERVER_BASE_URL = "http://10.131.253.211:8080/BundleService/";
    public static final String BUNDLE_DOWNLOAD_URL = SERVER_BASE_URL + "BundleServlet";
    public static final String TEMPLATE_LIST_URL = SERVER_BASE_URL + "ListTemplateServlet";
    public static final String TEMPLATE_DOWNLOAD_URL = SERVER_BASE_URL + "TemplateServlet";

    private static Parameter ourInstance = new Parameter();
    private static Gson gson;

    private File initBundleDir;
    private File newBundleDir;
    private File cacheDir;
    private File templateDir;
    private File optimizedDir;

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

    public File getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(File templateDir) {
        this.templateDir = templateDir;
    }

    public File getOptimizedDir() {
        return optimizedDir;
    }

    public void setOptimizedDir(File optimizedDir) {
        this.optimizedDir = optimizedDir;
    }
}
