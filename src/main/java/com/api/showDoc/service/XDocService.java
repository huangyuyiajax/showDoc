package com.api.showDoc.service;

import com.api.common.XDocProperties;
import com.api.javaParser.spring.framework.SpringApiAction;
import com.api.javaParser.spring.framework.SpringApiModule;
import com.api.javaParser.spring.framework.SpringWebFramework;
import com.api.javaParser.xdoc.XDoc;
import com.api.javaParser.xdoc.model.ApiAction;
import com.api.javaParser.xdoc.model.ApiDoc;
import com.api.javaParser.xdoc.model.ApiModule;
import com.api.javaParser.xdoc.model.FieldInfo;
import com.api.javaParser.xdoc.tag.*;
import com.api.javaParser.xdoc.utils.JsonFormatUtils;
import com.api.showDoc.model.ShowdocModel;
import com.api.util.GroupTemplateHelper;
import com.api.util.HttpHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private XDocProperties xDocProperties;

    private static ApiDoc apiDoc;

    /**
     * 获取所有文档api
     *
     * @return
     */
    public Object apis(HttpServletRequest request, HttpServletResponse response) {
        String path = xDocProperties.getSourcePath();
        if (StringUtils.isBlank(path)) {
            path = ".";//默认为当前目录
        }
        List<String> paths = Arrays.asList(path.split(","));
        log.info("开始解析 XDoc, 路径 path:{}", paths);
        try {
            XDoc xDoc = new XDoc(paths, new SpringWebFramework());
            apiDoc = xDoc.resolve();
            List<ApiModule> apiModules = apiDoc.getApiModules();//所有API模块   相当一个controller类
            Integer s_number = 1;//可选，页面序号。默认是99。数字越小，该页面越靠前
            for(ApiModule apiModule:apiModules){
                String comment =apiModule.getComment();//业务模块的描述
                List<ApiAction> apiActions = apiModule.getApiActions();//此业务模块下有哪些接口  相当一个controller类 下一个方法
                SpringApiModule springApiModule = (SpringApiModule)apiModule;
                for(ApiAction apiAction:apiActions){
                    try{
                        ShowdocModel showdocModel = new ShowdocModel();
                        List<ParamTagImpl> paramTags = new ArrayList<ParamTagImpl>();
                        showdocModel.setParamTag(paramTags);
                        List<FieldInfo> fieldInfos = new ArrayList<FieldInfo>();
                        showdocModel.setFieldInfos(fieldInfos);
                        List<FieldInfo> paramFieldInfos = new ArrayList<FieldInfo>();
                        showdocModel.setParamFieldInfos(paramFieldInfos);
                        List<RespTagImpl> respParam = new ArrayList<RespTagImpl>();
                        showdocModel.setRespParam(respParam);
                        for(DocTag docTag: apiAction.getDocTags()){
                            switch(docTag.getTagName()){
                                case "@author":
                                    showdocModel.setAuthor(String.valueOf(docTag.getValues()));
                                    break;
                                case "@description":
                                    showdocModel.setDescription(String.valueOf(docTag.getValues()));
                                    break;
                                case "@param":
                                    ParamTagImpl paramTag = (ParamTagImpl)docTag;
                                    paramTags.add(paramTag);
                                    break;
                                case "@see":
                                    SeeTagImpl seeTag = (SeeTagImpl)docTag;
                                    fieldInfos.addAll(seeTag.getValues().getFieldInfos());
                                    break;
                                case "@params":
                                    SeeTagImpl seeTag1 = (SeeTagImpl)docTag;
                                    paramFieldInfos.addAll(seeTag1.getValues().getFieldInfos());
                                    break;
                                case "@resp":
                                    respParam.add((RespTagImpl)docTag);
                                    break;
                                case "@date":
                                    showdocModel.setDate(String.valueOf(docTag.getValues()));
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
                        for(String parentUrl:springApiModule.getUris()){
                            for(String url:springApiAction.getUris()){
                                urls.add((String.valueOf(parentUrl.charAt(0)).equals("/") ?"":"/" )+parentUrl + (String.valueOf(url.charAt(0)).equals("/") ?"":"/" ) + url);
                            }
                        }
                        showdocModel.setUrl(urls);
                        String cat_name = xDocProperties.getTitle()+"/"+comment.trim();//可选参数。当页面文档处于目录下时，请传递目录名。当目录名不存在时，showdoc会自动创建此目录。需要创建多层目录的时候请用斜杆隔开，例如 “一层/二层/三层”
                        String page_title = !"".equals(apiAction.getComment())?apiAction.getComment():(apiModule.getType().getCanonicalName()+"."+apiAction.getName());//页面标题。请保证其唯一。（或者，当页面处于目录下时，请保证页面标题在该目录下唯一）。当页面标题不存在时，showdoc将会创建此页面。当页面标题存在时，将用page_content更新其内容
                        String page_content = GroupTemplateHelper.getInstance().showdocTpl(showdocModel);//页面内容，可传递markdown格式的文本或者html源码
                       // HttpHelper.post(xDocProperties.getUrl(),"api_key="+xDocProperties.getApiKey()+"&api_token="+xDocProperties.getApiToken()+"&page_title="+page_title+"&s_number="+(s_number++)+"&page_content="+page_content+"&cat_name="+cat_name,"POST");
                    } catch (Exception e) {
                        log.error(apiAction.getName()+"接口生成文档失败", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("start up XDoc error", e);
            return "执行失败"+e;
        }
        return "执行完毕";
    }

}
