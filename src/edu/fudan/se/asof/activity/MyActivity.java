package edu.fudan.se.asof.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import edu.fudan.se.asof.R;
import edu.fudan.se.asof.engine.Engine;
import edu.fudan.se.asof.engine.Template;
import edu.fudan.se.asof.felix.FelixService;
import edu.fudan.se.asof.felix.ServiceInjector;
import edu.fudan.se.asof.util.Log;

public class MyActivity extends Activity {

    private Button getBundle, controlFelix;
    private ServiceInjector injector;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.debug();
            controlFelix.setText(R.string.stop_felix);
            getBundle.setEnabled(true);
            injector = (ServiceInjector) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            controlFelix.setText(R.string.start_felix);
            getBundle.setEnabled(false);
            Log.debug();
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getBundle = (Button) findViewById(R.id.get_bundle);
        controlFelix = (Button) findViewById(R.id.felix_control);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, FelixService.class));
    }

    public void controlFelix(View v) {
        if (getBundle.isEnabled()) {
            unbindService(conn);
        } else {
            Intent intent = new Intent(this, FelixService.class);
            startService(intent);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    public void getBundle(View v) {
        try {
            Class<Template> templateClass = (Class<Template>) Class.forName("edu.fudan.se.asof.template.HelloTemplate");
            Template template = templateClass.newInstance();

            Engine.ParamPackage param = new Engine.ParamPackage();
            param.injector = injector;
            param.template = template;
            param.context = this;
            Engine.getInstance().interpretTemplate(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
