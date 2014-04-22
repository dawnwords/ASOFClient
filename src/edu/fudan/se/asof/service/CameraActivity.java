package edu.fudan.se.asof.service;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import edu.fudan.se.asof.engine.Log;
import edu.fudan.se.asof.engine.ServiceActivity;

import java.io.IOException;

/**
 * Created by Dawnwords on 2014/4/21.
 */
public class CameraActivity extends ServiceActivity implements SurfaceHolder.Callback,
        Camera.ShutterCallback, Camera.PictureCallback, View.OnClickListener {

    private Camera camera;
    private SurfaceView preview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initUI();
        camera = Camera.open();
    }

    private void initUI() {
        RelativeLayout root = new RelativeLayout(getContext());
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        preview = new SurfaceView(getContext());
        preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        preview.getHolder().addCallback(this);

        Button takePhoto = new Button(getContext());
        takePhoto.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        takePhoto.setGravity(Gravity.CENTER);
        takePhoto.setText("Take Photo");
        takePhoto.setOnClickListener(this);

        root.addView(preview);
        root.addView(takePhoto);

        setContentView(root);
    }

    @Override
    public void onClick(View v) {
        camera.takePicture(this, null, null, this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            Camera.Parameters params = camera.getParameters();
            Camera.Size selected = params.getSupportedPreviewSizes().get(0);
            Log.debug(String.format("width:%d,height:%d", selected.width, selected.height));
            params.setPreviewSize(selected.width, selected.height);
            camera.setParameters(params);
            camera.setPreviewDisplay(preview.getHolder());
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(preview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        finish(data);
    }

    @Override
    public void onShutter() {

    }
}
