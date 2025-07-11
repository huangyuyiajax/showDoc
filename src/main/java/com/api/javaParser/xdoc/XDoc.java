package com.api.javaParser.xdoc;

import com.api.javaParser.xdoc.model.ApiDoc;
import com.api.javaParser.xdoc.resolver.DocTagResolver;
import com.api.javaParser.xdoc.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.api.javaParser.xdoc.framework.Framework;
import com.api.javaParser.xdoc.model.ApiModule;
import com.api.javaParser.xdoc.resolver.javaparser.JavaParserDocTagResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * XDoc主入口,核心处理从这里开始
 *
 * @author huangyuyi
 * @date 2017-03-03 16:25
 */
public class XDoc {

    private static final String CHARSET = "utf-8";

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 源码路径
     */
    private List<String> srcPaths;

    /**
     * api框架类型
     */
    private Framework framework;

    /**
     * 默认的java注释解析器实现
     * <p>
     * 备注:基于sun doc的解析方式已经废弃,若需要请参考v1.0之前的版本
     *
     * @see JavaParserDocTagResolver
     */
    private DocTagResolver docTagResolver = new JavaParserDocTagResolver();

    /**
     * 构建XDoc对象
     *
     * @param srcPath 源码路径
     */
    public XDoc(String srcPath, Framework framework) {
        this(Arrays.asList(srcPath), framework);
    }

    /**
     * 构建XDoc对象
     *
     * @param srcPaths 源码路径,支持多个
     */
    public XDoc(List<String> srcPaths, Framework framework) {
        this.srcPaths = srcPaths;
        this.framework = framework;
    }

    /**
     * 解析源码并返回对应的接口数据
     *
     * @return API接口数据
     */
    public ApiDoc resolve() {
        List<String> allFiles = FileUtils.getAllJavaFiles(new File("."));
        List<String> files = new ArrayList<String>();
        for (String srcPath : this.srcPaths) {
            File dir = new File(srcPath);
            log.info("解析源码路径:{}", dir.getAbsolutePath());
            files.addAll(FileUtils.getAllJavaFiles(dir));
        }

        List<ApiModule> apiModules = this.docTagResolver.resolve(allFiles,files, framework);

        if (framework != null) {
            apiModules = framework.extend(apiModules);
        }
        return new ApiDoc(apiModules);
    }

    public static String getCHARSET() {
        return CHARSET;
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public List<String> getSrcPaths() {
        return srcPaths;
    }

    public void setSrcPaths(List<String> srcPaths) {
        this.srcPaths = srcPaths;
    }

    public Framework getFramework() {
        return framework;
    }

    public void setFramework(Framework framework) {
        this.framework = framework;
    }

    public DocTagResolver getDocTagResolver() {
        return docTagResolver;
    }

    public void setDocTagResolver(DocTagResolver docTagResolver) {
        this.docTagResolver = docTagResolver;
    }
}
