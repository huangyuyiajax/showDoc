package com.api.showDoc.javaParser.xdoc.tag;


/**
 * 对@Param注释的封装
 * <p>
 * Created by huangyuyi on 2017/3/4.
 */
public class ParamTagImpl extends DocTag<String> {

    /**
     * 参数名
     */
    private String paramName;

    private String paramValue;

    /**
     * 参数描述
     */
    private String paramDesc;

    /**
     * 是否必填,默认false
     */
    private boolean require;

    /**
     * 参数类型
     */
    private String paramType;

    public ParamTagImpl(String tagName, String paramName,String paramValue, String paramDesc, String paramType, boolean require) {
        super(tagName);
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.paramDesc = paramDesc;
        this.paramType = paramType;
        this.require = require;
    }

    @Override
    public String getValues() {
        return paramName + " " + this.paramDesc;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public boolean isRequire() {
        return require;
    }

    public void setRequire(boolean require) {
        this.require = require;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }
}
