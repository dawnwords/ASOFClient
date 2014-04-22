package edu.fudan.se.asof.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
import edu.fudan.se.asof.engine.ServiceActivity;

import java.io.File;

/**
 * Created by Dawnwords on 2014/4/22.
 */
public class FileChooserActivity extends ServiceActivity {

    private FileListAdapter adapter;
    private boolean isFile;
    private ListView fileListView;
    private LinearLayout functionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isFile = (Boolean) getParameter("isFile");
        initFileListView();
        initFunctionButton();
        initRoot();
    }

    private void initFileListView() {
        adapter = new FileListAdapter();
        adapter.setCurrentDir(Environment.getExternalStorageDirectory());

        fileListView = new ListView(getContext());
        fileListView.setLayoutParams(getMatchParentParams());
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = adapter.getItem(position);
                if (file.isDirectory()) {
                    adapter.setCurrentDir(file);
                }
            }
        });
        fileListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                File file = adapter.getItem(position);
                if (isFile == file.isFile()) {
                    finish(file.getAbsolutePath());
                }
                return true;
            }
        });
        fileListView.setAdapter(adapter);
    }


    private void initFunctionButton() {
        functionButton = new LinearLayout(getContext());
        functionButton.setOrientation(LinearLayout.HORIZONTAL);
        functionButton.setLayoutParams(getWrapContentParams());

        Button newFolder = getFunctionButton("New Folder");
        newFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFolder();
            }
        });
        Button back = getFunctionButton("Back");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.back();
            }
        });

        functionButton.addView(newFolder);
        functionButton.addView(back);
    }

    private void createNewFolder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText folderName = new EditText(getContext());
        folderName.setLayoutParams(getWrapContentParams());
        builder.setTitle("Input Folder Name").setView(folderName)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (folderName.getText() != null) {
                            File newFolder = new File(adapter.currentDir + File.separator + folderName.getText().toString());
                            if (newFolder.mkdir()) {
                                adapter.setCurrentDir(adapter.currentDir);
                            }
                        }
                    }
                }).create().show();
    }

    private Button getFunctionButton(String content) {
        Button result = new Button(getContext());
        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        result.setText(content);
        result.setLayoutParams(params);
        return result;
    }

    private void initRoot() {
        LinearLayout root = new LinearLayout(getContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(getMatchParentParams());
        root.addView(functionButton);
        root.addView(fileListView);
        setContentView(root);
    }

    private LayoutParams getWrapContentParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    private LayoutParams getMatchParentParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    private class FileListAdapter extends BaseAdapter {
        File[] files;
        File currentDir;

        @Override
        public int getCount() {
            return files.length;
        }

        @Override
        public File getItem(int position) {
            return files[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            File file = getItem(position);
            if (convertView == null) {
                convertView = new TextView(getContext());
            }
            TextView fileView = (TextView) convertView;
            fileView.setTextSize(20);
            fileView.setText(file.getName());
            int padding = dp2px(20);
            fileView.setPadding(padding, padding, padding, padding);
            if (file.isDirectory()) {
                fileView.setTypeface(null, Typeface.BOLD);
            }

            return convertView;
        }

        void setCurrentDir(File currentDir) {
            this.currentDir = currentDir;
            this.files = currentDir.listFiles();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }

        void back() {
            currentDir = currentDir.getParentFile();
            setCurrentDir(currentDir);
        }
    }
}
