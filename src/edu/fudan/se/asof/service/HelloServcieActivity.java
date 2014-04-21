package edu.fudan.se.asof.service;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import edu.fudan.se.asof.engine.ServiceActivity;

/**
 * Created by Dawnwords on 2014/4/21.
 */
public class HelloServcieActivity extends ServiceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String caller = (String) getParameter("caller");
        TextView hello = new TextView(getContext());
        hello.setText("Hello " + caller + "!");
        hello.setTextSize(20);
        hello.setGravity(Gravity.CENTER);
        hello.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setContentView(hello);
    }
}
