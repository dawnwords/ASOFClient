package edu.fudan.se.asof.service;

import android.os.Bundle;
import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.engine.ReturnType;

/**
 * Created by Dawnwords on 2014/4/22.
 */
public class FileChooserActivator extends AbstractService {
    @Override
    protected ReturnType invoke(Object... input) {
        boolean isFile = (Boolean) input[0];
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFile", isFile);
        ReturnType returnType = new ReturnType();
        returnType.put("filePath", startServiceActivityForResult(bundle));
        return returnType;
    }
}
