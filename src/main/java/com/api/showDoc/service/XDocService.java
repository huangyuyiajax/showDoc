package com.api.showDoc.service;

import com.api.showDoc.javaParser.spring.framework.SpringApiAction;
import com.api.showDoc.javaParser.spring.framework.SpringApiModule;
import com.api.showDoc.javaParser.spring.framework.SpringWebFramework;
import com.api.showDoc.javaParser.xdoc.XDoc;
import com.api.showDoc.javaParser.xdoc.model.ApiAction;
import com.api.showDoc.javaParser.xdoc.model.ApiDoc;
import com.api.showDoc.javaParser.xdoc.model.ApiModule;
import com.api.showDoc.javaParser.xdoc.tag.*;
import com.api.showDoc.javaParser.xdoc.utils.JsonFormatUtils;
import com.api.showDoc.model.ResultModel;
import com.api.showDoc.model.ShowdocModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * XDoc的Spring Web入口
 *
 */
@Service
public class XDocService {

    private Logger log = LoggerFactory.getLogger(XDocService.class);

    @Value("${xDoc:false}")
    private Boolean xDoc;

    @Value("${xDocPath:}")
    private String xDocPath;

    public static int paramLevel = 4;

    public static int respbodyLevel = 2;

    private static ApiDoc apiDoc;

    @Value("${paramLevel:4}")
    public void setParamLevel(int val) {
        paramLevel = val;
    }

    @Value("${respbodyLevel:4}")
    public void setRespbodyLevel(int val) {
        respbodyLevel = val;
    }

    /**
     * 获取所有文档api
     *
     * @return
     */
    public Object apis() {
        if (!xDoc) {
            return "没打开开关";
        }
        if (StringUtils.isBlank(xDocPath)) {
            xDocPath = ".";//默认为当前目录
            System.out.println(xDocPath);
        }
        List<String> paths = Arrays.asList(xDocPath.split(","));
        log.info("开始解析 XDoc, 路径 path:{}", paths);
        try {
            XDoc xDoc = new XDoc(paths, new SpringWebFramework());
            apiDoc = xDoc.resolve();
            List<ApiModule> apiModules = apiDoc.getApiModules();//所有API模块   相当一个controller类
            List<ResultModel> list = new ArrayList<>();
            for(ApiModule apiModule:apiModules){
                ResultModel resultModel = new ResultModel();
                String comment = apiModule.getComment();//业务模块的描述
                resultModel.setComment(comment);
                resultModel.setControName(apiModule.getType().getSimpleName());
                List<ApiAction> apiActions = apiModule.getApiActions();//此业务模块下有哪些接口  相当一个controller类 下一个方法
                SpringApiModule springApiModule = (SpringApiModule)apiModule;
                List<ShowdocModel> showdocModels = new ArrayList();
//                String canonicalName = apiModule.getType().getCanonicalName();
                for(ApiAction apiAction:apiActions){
                    String page_title = StringUtils.isNotBlank(apiAction.getComment())?apiAction.getComment():apiAction.getName();
                    try{
                        ShowdocModel showdocModel = new ShowdocModel();
                        showdocModel.setPageTitle(page_title);
                        showdocModel.setFuntionName(apiAction.getName());
                        for(DocTag docTag: apiAction.getDocTags()){
                            switch(docTag.getTagName()){
                                case "@author":
                                    showdocModel.setAuthor(String.valueOf(docTag.getValues()));
                                    break;
                                case "@date":
                                    showdocModel.setDate(String.valueOf(docTag.getValues()));
                                    break;
                                case "@description":
                                    showdocModel.setDescription(String.valueOf(docTag.getValues()));
                                    break;
                                case "@param"://入参参数
                                    ParamTagImpl paramTag = (ParamTagImpl)docTag;
                                    showdocModel.getParamTag().add(paramTag);
                                    break;
                                case "@params"://多个参数
                                    SeeTagImpl seeTag1 = (SeeTagImpl)docTag;
                                    showdocModel.getParamFieldInfos().addAll(seeTag1.getValues().getFieldInfos());
                                    break;
                                case "@resp"://返回参数
                                    showdocModel.getRespParam().add((RespTagImpl)docTag);
                                    break;
                                case "@see"://实体参数
                                    SeeTagImpl seeTag = (SeeTagImpl)docTag;
                                    showdocModel.getFieldInfos().addAll(seeTag.getValues().getFieldInfos());
                                    break;
                            }
                        }
                        SpringApiAction springApiAction = (SpringApiAction)apiAction;
                        if(springApiAction.getRequiresPermissions()!=null){
                            showdocModel.setRequiresPermissions(springApiAction.getRequiresPermissions());
                        }
                        if(springApiAction.getMethods()!=null){
                            showdocModel.setMethods(springApiAction.getMethods());
                        }
                        if(springApiAction.getRespbody()!=null){
                            showdocModel.setRespbody(JsonFormatUtils.formatJson(springApiAction.getRespbody()));
                        }
                        if(springApiAction.getReturnDesc()!=null){
                            showdocModel.setRespData(springApiAction.getReturnDesc());
                        }
                        List<String> urls = new ArrayList<String>();
                        if(springApiModule.getUris()!=null){
                            for(String parentUrl:springApiModule.getUris()){
                                for(String url:springApiAction.getUris()){
                                    urls.add((String.valueOf(parentUrl.charAt(0)).equals("/") ?"":"/" )+parentUrl + (String.valueOf(url.charAt(0)).equals("/") ?"":"/" ) + url);
                                }
                            }
                        }else if(springApiAction.getUris()!=null){
                            for(String url:springApiAction.getUris()){
                                urls.add((String.valueOf(url.charAt(0)).equals("/") ?"":"/" ) + url);
                            }
                        }
                        showdocModel.setUrl(urls);
                        showdocModels.add(showdocModel);
                    } catch (Exception e) {
                        log.error(apiAction.getName()+"接口生成文档失败", e);
                    }
                    resultModel.setList(showdocModels);
                }
                list.add(resultModel);
            }
            return list;
        } catch (Exception e) {
            log.error("start up XDoc error", e);
            return "执行失败"+e;
        }
    }

}
