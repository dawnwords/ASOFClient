package edu.fudan.se.asof.engine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.fudan.se.asof.R;

/**
 * Created by Dawnwords on 2014/4/8.
 */
public abstract class Template {
    protected interface OnUserInputListener {
        void onUserInput(String input);
    }

    protected interface OnUserConfirmListener {
        void onUserConfirm(boolean confirm);
    }

    protected interface OnUserChooseListener {
        void onUserChoose(int choice);
    }

    private Context context;

    public abstract void orchestraServices();

    protected void requestUserInput(String message, final OnUserInputListener listener) {
        final Object lock = new Object();
        final EditText input = new EditText(context);
        input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        getBuilder(message).setView(input)
                .setPositiveButton(R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText() != null) {
                            listener.onUserInput(input.getText().toString());
                        }
                    }
                }).create().show();
    }

    protected void requestUserConfirm(String message, final OnUserConfirmListener listener) {
        TextView messageTextView = new TextView(context);
        messageTextView.setText(message);
        getBuilder("Confirm").setView(messageTextView)
                .setPositiveButton(R.string.ok,new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onUserConfirm(true);
                    }
                })
                .setNegativeButton(R.string.cancel,new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onUserConfirm(false);
                    }
                })
                .create().show();
    }

    protected void requestUserChoose(String message, String[] item, final OnUserChooseListener listener) {
        getBuilder(message).setItems(item,new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onUserChoose(which);
            }
        }).create().show();
    }

    protected void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private AlertDialog.Builder getBuilder(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder.setTitle(message).setCancelable(false);
    }
}

