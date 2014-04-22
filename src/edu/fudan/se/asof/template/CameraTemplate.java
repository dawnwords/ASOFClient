package edu.fudan.se.asof.template;

import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.engine.ReturnType;
import edu.fudan.se.asof.engine.ServiceDescription;
import edu.fudan.se.asof.engine.Template;

/**
 * Created by Dawnwords on 2014/4/22.
 */
public class CameraTemplate extends Template {
    @ServiceDescription(
            description = "camera service",
            output = {"imageByte"}
    )
    AbstractService cameraService;


    @Override
    public void orchestraServices() {
        ReturnType result = cameraService.invokeService();
        byte[] imageByte = (byte[]) result.get("imageByte");
        showMessage("" + imageByte.length);
    }
}
