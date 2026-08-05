package com.ibrahimahmads.github.vbooth.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
public class InitializationConfig {
    @PostConstruct
    public void init(){
        initTimezone();
    }

    void initTimezone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));
    }
}
