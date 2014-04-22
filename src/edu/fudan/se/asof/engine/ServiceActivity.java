package edu.fudan.se.asof.engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dawnwords on 2014/4/21.
 */
public abstract class ServiceActivity {
    private Activity activity;

    void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onRestart() {
    }

    public void onDestroy() {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onTouchEvent(MotionEvent event) {
    }

    public void finish() {
        activity.finish();
    }

    public void finish(Object value) {
        Log.debug(value.toString());
        finish();
        ResultHolder holder = ActivityResult.getInstance().getServiceActivityResultHolder();
        if (holder == null) {
            throw new IllegalAccessError("ResultHolder is Null!");
        }
        holder.set(value);
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        activity.addContentView(view, params);
    }

    public void setContentView(View view) {
        activity.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        activity.setContentView(view, params);
    }

    public Context getContext() {
        return activity;
    }

    public Context getApplicationContext() {
        return activity.getApplicationContext();
    }

    public LayoutInflater getLayoutInflater() {
        return activity.getLayoutInflater();
    }

    public Resources getResources() {
        return activity.getResources();
    }

    public Object getSystemService(String name) {
        return activity.getSystemService(name);
    }

    public Object getParameter(String name) {
        return activity.getIntent().getBundleExtra(AdapterActivity.EXTRA_BUNDLE).get(name);
    }

    public PackageManager getPackageManager() {
        return activity.getPackageManager();
    }

    public void runOnUiThread(Runnable r) {
        activity.runOnUiThread(r);
    }
}
