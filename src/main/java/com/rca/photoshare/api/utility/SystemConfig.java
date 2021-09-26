package com.rca.photoshare.api.utility;

import org.springframework.stereotype.Component;

@Component
public class SystemConfig {
    public String getConfiguration(String name) {
        return System.getenv(name);
    }
}
