package edu.fudan.se.asof.template;

import edu.fudan.se.asof.engine.*;

/**
 * Created by Dawnwords on 2014/4/9.
 */
public class TestTemplate extends Template {
    @ServiceDescription(
            description = "hello service",
            input = {"caller"}
    )
    AbstractService helloService;


    @Override
    public void orchestraServices() {
        String userName = requestUserInput("Input Your Name");
        helloService.invokeService(userName);
    }
}
