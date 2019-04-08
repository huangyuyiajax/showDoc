package com.api.javaParser.xdoc.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by leaf on 2018/6/22.
 */
public class Constant {

    public static final String NOT_EN = "N";
    public static final String NOT_ZH = "非必填";
    public static final String YES_EN = "Y";
    public static final String YES_ZH = "必填";

    public static final List<String> TYPE = Arrays.asList("int","short","boolean","Float","float",
            "double","Long","long","BigDecimal","Integer","Short","Boolean");

    public static final List<String> DATA_TYPE = Arrays.asList("Array","int","short","boolean","Float","float","char","StringBuffer","Text","HashMap","HashSet","Hashtable","Hash",
            "double","Long","long","byte","object","Date","time","BigDecimal","String","Integer","Short","Boolean","Byte","Object","Timestamp","StringBuilder"
    ,"InputStream","List","Math","Map","Year");
}
