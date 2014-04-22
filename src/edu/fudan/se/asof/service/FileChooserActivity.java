package edu.fudan.se.asof.service;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import edu.fudan.se.asof.engine.ServiceActivity;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Dawnwords on 2014/4/22.
 */
public class FileChooserActivity extends ServiceActivity {

    private FileListAdapter adapter;
    private boolean isFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isFile = (Boolean) getParameter("isFile");
        initFileListAdapter();
        initFileListView();
    }

    private void initFileListAdapter() {
        adapter = new FileListAdapter(this);
        adapter.setFiles(Arrays.asList(Environment.getExternalStorageDirectory().listFiles()));
    }

    private void initFileListView() {
        ListView fileListView = new ListView(getContext());
        fileListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = adapter.getItem(position);
                if (file.isDirectory()) {
                    adapter.setFiles(Arrays.asList(file.listFiles()));
                }
            }
        });
        fileListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                File file = adapter.getItem(position);
                if(isFile == file.isFile()) {
                    finish(file.getAbsolutePath());
                }
                return true;
            }
        });
        fileListView.setAdapter(adapter);
    }
}
