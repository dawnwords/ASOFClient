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
        requestUserInput("Input Your Name", new OnUserInputListener() {
            @Override
            public void onUserInput(String userName) {
                helloService.invokeService(userName);
            }
        });
    }
}
