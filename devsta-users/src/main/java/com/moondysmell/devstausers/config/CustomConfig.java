package com.moondysmell.devstausers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class CustomConfig {

    @Value("${my-config.name}")
    private  String configName;

    public String getConfigName(){
        return configName;
    }
}
