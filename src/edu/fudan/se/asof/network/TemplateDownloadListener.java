package edu.fudan.se.asof.network;

import dalvik.system.DexClassLoader;
import edu.fudan.se.asof.engine.Engine;
import edu.fudan.se.asof.engine.Engine.ParamPackage;
import edu.fudan.se.asof.engine.Template;
import edu.fudan.se.asof.util.Parameter;

import java.io.File;

/**
 * Created by Dawnwords on 2014/4/19.
 */
public class TemplateDownloadListener implements NetworkListener<TemplateFetcher.Response> {
    private String templatePath;
    private ParamPackage param;

    public TemplateDownloadListener(String template, ParamPackage param) {
        this.templatePath = Parameter.getInstance().getTemplateDir() + File.separator + template;
        this.param = param;
    }

    @Override
    public void onSuccess(TemplateFetcher.Response response) {
        File sourceFile = new File(templatePath);
        File file = Parameter.getInstance().getOptimizedDir();
        DexClassLoader classLoader = new DexClassLoader(
                sourceFile.getAbsolutePath(),
                file.getAbsolutePath(),
                null,
                param.context.getClassLoader());

        try {
            Class templateClass = classLoader.loadClass(response.clazz);
            param.template = (Template) templateClass.newInstance();
            Engine.getInstance().interpretTemplate(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String reason) {

    }
}
