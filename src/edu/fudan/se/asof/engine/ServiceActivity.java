package edu.fudan.se.asof.engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    protected View findViewById(int id) {
        return activity.findViewById(id);
    }

    protected void finish() {
        activity.finish();
    }

    protected void addContentView(View view, ViewGroup.LayoutParams params) {
        activity.addContentView(view, params);
    }

    protected void setContentView(View view, ViewGroup.LayoutParams params) {
        activity.setContentView(view, params);
    }

    protected Context getContext() {
        return activity;
    }

    protected LayoutInflater getLayoutInflater() {
        return activity.getLayoutInflater();
    }

    protected Resources getResources() {
        return activity.getResources();
    }

    protected Object getSystemService(String name) {
        return activity.getSystemService(name);
    }

    protected Intent getIntent() {
        return activity.getIntent();
    }

}
