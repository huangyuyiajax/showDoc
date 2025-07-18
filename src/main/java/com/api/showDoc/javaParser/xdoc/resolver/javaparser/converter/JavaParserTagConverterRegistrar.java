package com.api.showDoc.javaParser.xdoc.resolver.javaparser.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * JavaParserTagConverter注册器,注册管理这些转换器
 *
 * @author huangyuyi
 * @date 2017/4/3 0003
 */
public class JavaParserTagConverterRegistrar {

    private static JavaParserTagConverterRegistrar INSTANCE = new JavaParserTagConverterRegistrar();

    /**
     * 所有转存器的存储在这里面
     */
    private Map<String, JavaParserTagConverter> registrar = new HashMap<String, JavaParserTagConverter>(5);

    /**
     * 默认解析转换器
     */
    private JavaParserTagConverter defaultTagConverter = new DefaultJavaParserTagConverterImpl();

    private JavaParserTagConverterRegistrar() {
        //注册特殊标签的转换器,其它默认使用DefaultJavaParserTagConverterImpl
        registerConverter("@param", new ParamTagConverter());
        registerConverter("@see", new SeeTagConverter());
        registerConverter("@resp", new RespTagConverter());
        registerConverter("@params", new ParamsTagConverter());
        registerConverter("@respbody", new RespbodyTagConverter());
    }

    /**
     * 注册转换器
     *
     * @param tagName      要解析转换的标签
     * @param tagConverter 转换器
     */
    public void registerConverter(String tagName, JavaParserTagConverter tagConverter) {
        registrar.put(tagName, tagConverter);
    }

    /**
     * 获取标签转换器,如果没有特殊定制的,则返回默认的转换器DefaultJavaParserTagConverterImpl
     *
     * @param tagName 要转换的标签名称
     * @return 匹配到的标签转换器
     */
    public JavaParserTagConverter getConverter(String tagName) {
        for (Map.Entry<String, JavaParserTagConverter> entry : registrar.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(tagName)) {
                return entry.getValue();
            }
        }
        return defaultTagConverter;
    }

    /**
     * 获取该注册器的单例对象
     */
    public static JavaParserTagConverterRegistrar getInstance() {
        return INSTANCE;
    }
}
