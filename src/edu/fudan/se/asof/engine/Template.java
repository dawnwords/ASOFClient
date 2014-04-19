package edu.fudan.se.asof.engine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.text.Editable;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Dawnwords on 2014/4/8.
 */
public abstract class Template {

    private Context context;
    private Handler uiHandler;

    public abstract void orchestraServices();

    protected String requestUserInput(final String message) {
        final ResultHolder<String> result = new ResultHolder<String>();
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                final EditText input = new EditText(context);
                input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                getBuilder(message).setView(input)
                        .setPositiveButton("OK", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Editable text = input.getText();
                                result.set(text == null ? "" : text.toString());
                            }
                        }).create().show();
            }
        });
        return result.get();
    }

    protected boolean requestUserConfirm(final String message) {
        final ResultHolder<Boolean> result = new ResultHolder<Boolean>();
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView messageTextView = new TextView(context);
                messageTextView.setText(message);
                getBuilder("Confirm").setView(messageTextView)
                        .setPositiveButton("OK", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.set(true);
                            }
                        })
                        .setNegativeButton("Cancel", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.set(false);
                            }
                        })
                        .create().show();
            }
        });
        return result.get();
    }

    protected int requestUserChoose(final String message, final String[] item) {
        final ResultHolder<Integer> result = new ResultHolder<Integer>();
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                getBuilder(message).setItems(item, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.set(which);
                    }
                }).create().show();
            }
        });
        return result.get();
    }

    protected void showMessage(final String message) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private AlertDialog.Builder getBuilder(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder.setTitle(message).setCancelable(false);
    }
}

