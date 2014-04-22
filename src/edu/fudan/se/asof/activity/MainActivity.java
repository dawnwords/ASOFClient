package edu.fudan.se.asof.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import edu.fudan.se.asof.R;
import edu.fudan.se.asof.engine.Engine;
import edu.fudan.se.asof.felix.FelixService;
import edu.fudan.se.asof.felix.ServiceInjector;
import edu.fudan.se.asof.network.NetworkListener;
import edu.fudan.se.asof.network.TemplateDownloadListener;
import edu.fudan.se.asof.network.TemplateFetcher;
import edu.fudan.se.asof.network.TemplateListFetcher;
import edu.fudan.se.asof.ui.TemplateAdapter;
import edu.fudan.se.asof.engine.Log;

import java.util.Arrays;

public class MainActivity extends Activity {

    private Button getTemplateList, controlFelix;
    private TemplateAdapter adapter;
    private ServiceInjector injector;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.debug();
            controlFelix.setText(R.string.stop_felix);
            getTemplateList.setEnabled(true);
            injector = (ServiceInjector) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            controlFelix.setText(R.string.start_felix);
            getTemplateList.setEnabled(false);
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
        getTemplateList = (Button) findViewById(R.id.get_template_list);
        controlFelix = (Button) findViewById(R.id.felix_control);
        adapter = new TemplateAdapter(this);
        initTemplateList();
    }

    private void initTemplateList() {
        ListView templateList = (ListView) findViewById(R.id.template_list);
        templateList.setAdapter(adapter);
        templateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = adapter.getItem(position).file;
                Engine.ParamPackage param = new Engine.ParamPackage();
                param.injector = injector;
                param.context = MainActivity.this;
                TemplateDownloadListener listener = new TemplateDownloadListener(name, param);
                new TemplateFetcher(name, listener).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    public void controlFelix(View v) {
        if (getTemplateList.isEnabled()) {
            unbindService(conn);
        } else {
            Intent intent = new Intent(this, FelixService.class);
            startService(intent);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    public void getTemplates(View view) {
        new TemplateListFetcher(new NetworkListener<TemplateListFetcher.Response[]>() {
            @Override
            public void onSuccess(TemplateListFetcher.Response[] data) {
                adapter.setTemplates(Arrays.asList(data));
            }

            @Override
            public void onFailure(String reason) {

            }
        }).start();
    }

}
