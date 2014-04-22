package edu.fudan.se.asof.service;

import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.engine.ReturnType;

/**
 * Created by Dawnwords on 2014/4/20.
 */
public class CameraServiceActivator extends AbstractService {
    @Override
    protected ReturnType invoke(Object... input) {
        ReturnType result = new ReturnType();
        byte[] imageByte = startServiceActivityForResult(null);
        result.put("imageByte", imageByte);
        return result;
    }

}
