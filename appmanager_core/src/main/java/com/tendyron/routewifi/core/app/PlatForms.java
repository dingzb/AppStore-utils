package com.tendyron.routewifi.core.app;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Neo on 2017/1/5.
 */
public class PlatForms {
    private static Map<String, String> platforms = new HashMap<>();

    public String get(String userAgent) {
//        return platforms.get(userAgent);
        return userAgent;
    }
}
