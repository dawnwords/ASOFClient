package edu.fudan.se.asof.template;

import edu.fudan.se.asof.engine.AbstractService;
import edu.fudan.se.asof.engine.Log;
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

    @ServiceDescription(
            description = "file choose service",
            input = {"isFile"},
            output = {"path"}
    )
    AbstractService fileChooseService;

    @ServiceDescription(
            description = "file choose service",
            input = {"path", "name", "fileContent"}
    )
    AbstractService fileSaveService;

    @Override
    public void orchestraServices() {
        Log.debug("camera service");
        byte[] imageByte = (byte[]) cameraService.invokeService().get("imageByte");
        Log.debug("file choose service");
        String filePath = (String) fileChooseService.invokeService(false).get("filePath");
        Log.debug("file save service");
        fileSaveService.invokeService(filePath, imageByte);
    }
}
