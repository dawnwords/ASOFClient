package edu.fudan.se.asof.engine;

/**
 * Created by Dawnwords on 2014/4/22.
 */
public class ActivityResult {
    private ResultHolder serviceActivityResultHolder;

    private static ActivityResult ourInstance = new ActivityResult();

    public static ActivityResult getInstance() {
        return ourInstance;
    }

    private ActivityResult() {
    }

    public ResultHolder getServiceActivityResultHolder() {
        return serviceActivityResultHolder;
    }

    public void setServiceActivityResultHolder(ResultHolder serviceActivityResultHolder) {
        this.serviceActivityResultHolder = serviceActivityResultHolder;
    }
}
