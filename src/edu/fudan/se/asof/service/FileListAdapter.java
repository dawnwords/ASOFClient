package edu.fudan.se.asof.service;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.fudan.se.asof.engine.ServiceActivity;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dawnwords on 2014/4/22.
 */
public class FileListAdapter extends BaseAdapter {
    private ServiceActivity activity;

    public FileListAdapter(ServiceActivity activity) {
        this.activity = activity;
    }

    private List<File> files = new LinkedList<File>();

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public File getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File file = getItem(position);
        if (convertView == null) {
            convertView = new TextView(activity.getContext());
        }
        TextView fileView = (TextView) convertView;
        fileView.setTextSize(20);
        if (file.isDirectory()) {
            fileView.setTypeface(null, Typeface.BOLD);
        }

        return convertView;
    }

    public void setFiles(List<File> files) {
        this.files = files;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }


}
