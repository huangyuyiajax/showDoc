package com.api.javaParser.spring.framework;

import com.api.javaParser.xdoc.model.ApiAction;
import com.api.javaParser.xdoc.model.ObjectInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyuyi on 2017/3/4.
 */
@Data
public class SpringApiAction extends ApiAction {

    /**
     * 访问的uri地址
     */
    private List<String> uris;

    /**
     * shiro RequiresPermissions
     */
    private List<String> requiresPermissions;

    /**
     * 允许的访问方法:POST,GET,DELETE,PUT等, 如果没有,则无限制
     */
    private List<String> methods;

    /**
     * 入参
     */
    private List<ParamInfo> param = new ArrayList<ParamInfo>(0);

    /**
     * 返回对象
     */
    private ObjectInfo returnObj;

    /**
     * 出参
     */
    private List<ParamInfo> respParam = new ArrayList<ParamInfo>(0);

    /**
     * 返回描述
     */
    private String returnDesc;

    /**
     * 返回的数据
     */
    private String respbody;

    /**
     * 是否返回json
     */
    private boolean json;

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public List<String> getRequiresPermissions() {
        return requiresPermissions;
    }

    public void setRequiresPermissions(List<String> requiresPermissions) {
        this.requiresPermissions = requiresPermissions;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<ParamInfo> getParam() {
        return param;
    }

    public void setParam(List<ParamInfo> param) {
        this.param = param;
    }

    public ObjectInfo getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(ObjectInfo returnObj) {
        this.returnObj = returnObj;
    }

    public List<ParamInfo> getRespParam() {
        return respParam;
    }

    public void setRespParam(List<ParamInfo> respParam) {
        this.respParam = respParam;
    }

    public String getReturnDesc() {
        return returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }

    public String getRespbody() {
        return respbody;
    }

    public void setRespbody(String respbody) {
        this.respbody = respbody;
    }

    public boolean isJson() {
        return json;
    }

    public void setJson(boolean json) {
        this.json = json;
    }


}
