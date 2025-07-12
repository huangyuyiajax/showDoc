package com.api.showDoc.javaParser.xdoc.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangyuyi on 2017/4/3 0003.
 */
public class ClassMapperUtils {

    private static Map<String, String> classPath = new HashMap<String, String>();

    public static void put(String name, String path) {
        classPath.put(name, path);
    }

    public static String getPath(String name) {
        return classPath.get(name);
    }
}
