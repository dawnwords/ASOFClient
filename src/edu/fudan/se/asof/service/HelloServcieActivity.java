package edu.fudan.se.asof.service;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import edu.fudan.se.asof.engine.ServiceActivity;

/**
 * Created by Dawnwords on 2014/4/21.
 */
public class HelloServcieActivity extends ServiceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String caller = getIntent().getStringExtra("caller");
        TextView hello = new TextView(getContext());
        hello.setText(caller);
        hello.setTextSize(20);
        System.out.println("setContentView");
        setContentView(hello, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
}
