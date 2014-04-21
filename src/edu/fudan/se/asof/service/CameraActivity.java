package edu.fudan.se.asof.service;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.Toast;
import edu.fudan.se.asof.engine.ServiceActivity;

/**
 * Created by Dawnwords on 2014/4/21.
 */
public class CameraActivity extends ServiceActivity {

    private Camera camera;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(getContext(), "No camera on this device", Toast.LENGTH_LONG).show();
        } else {
            camera = Camera.open();
        }
    }

//    public void onClick(View view) {
//        final ResultHolder<byte[]> picture = new ResultHolder<byte[]>();
//        camera.takePicture(null, null, new Camera.PictureCallback() {
//            @Override
//            public void onPictureTaken(byte[] data, Camera camera) {
//                picture.set(data);
//            }
//        });
//    }

    @Override
    public void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

}
