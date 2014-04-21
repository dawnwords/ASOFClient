package edu.fudan.se.asof.service;

import android.os.Bundle;
import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.engine.ReturnType;

/**
 * Created by Dawnwords on 2014/4/15.
 */
public class HelloServiceActivator extends AbstractService {

    @Override
    public ReturnType invoke(Object... input) {
        final String caller = (String) input[0];

        Bundle bundle = new Bundle();
        bundle.putString("caller", caller);
        startServiceActivity(bundle);
        return null;
    }
}
