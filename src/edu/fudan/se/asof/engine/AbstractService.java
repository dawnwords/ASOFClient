package edu.fudan.se.asof.engine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Arrays;

/**
 * Created by Dawnwords on 2014/4/6.
 */
public abstract class AbstractService implements BundleActivator {
    private int[] inputMatch, outputMatch;
    private String[] originParaName;
    private String activityClass;
    private Context context;
    private Handler uiHandler;

    public ReturnType invokeService(Object... input) {
        Log.debug(Arrays.toString(input));
        ReturnType type = invoke(getInputAfterMatching(input));
        return type == null ? null : type.getMatched(outputMatch, originParaName);
    }

    protected abstract ReturnType invoke(Object... input);// TODO throw Exception to repick service

    protected void onStart(BundleContext context) {
    }

    protected void onStop(BundleContext bundleContext) {
    }

    protected Context getContext() {
        return context;
    }

    protected void postUIRunnable(Runnable runnable) {
        uiHandler.post(runnable);
    }

    protected void startServiceActivity(final Bundle extraBundle) {
        Log.debug();
        if (activityClass != null) {
            postUIRunnable(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, AdapterActivity.class);
                    intent.putExtra(AdapterActivity.SERVICE_ACTIVITY_CLASS, activityClass);
                    intent.putExtra(AdapterActivity.EXTRA_BUNDLE, extraBundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    protected <T> T startServiceActivityForResult(final Bundle extraBundle) {
        Log.debug();
        ResultHolder<T> resultHolder = new ResultHolder<T>();
        ActivityResult.getInstance().setServiceActivityResultHolder(resultHolder);
        startServiceActivity(extraBundle);
        return resultHolder.get();
    }

    void setInputMatch(int[] inputMatch) {
        this.inputMatch = inputMatch;
    }

    void setOutputMatch(int[] outputMatch) {
        this.outputMatch = outputMatch;
    }

    void setActivityClass(String activityClass) {
        this.activityClass = activityClass;
    }

    void setContext(Context context) {
        this.context = context;
    }

    void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    void setOriginParaName(String[] originParaName) {
        this.originParaName = originParaName;
    }

    private Object[] getInputAfterMatching(Object[] input) {
        if (inputMatch != null && input.length > 0) {
            Object[] inputAfterMatching = new Object[input.length];
            for (int i = 0; i < input.length; i++) {
                inputAfterMatching[i] = input[inputMatch[i]];
            }
            return inputAfterMatching;
        } else {
            return input;
        }
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        bundleContext.registerService(AbstractService.class.getName(), this, null);
        onStart(bundleContext);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        onStop(bundleContext);
    }

}

