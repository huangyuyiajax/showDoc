package com.api.showDoc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.javaParser.xdoc.utils.JsonFormatUtils;
import com.api.javaParser.xdoc.utils.JsonUtils;
import com.api.showDoc.model.ShowdocModel;
import com.api.util.HttpHelper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author 黄育益
 * @description
 * @date
*/
@Controller
@RequestMapping("/text")
public class TextController {

    /**
     *
     * @author 黄育益
     * @description
     * @date
    */
    @ResponseBody
    @RequestMapping(value = "start")
    public String login(HttpServletRequest request, String params) {
        Cookie[] cookies = request.getCookies();
        if(cookies.length<=0){
            return "请登录";
        }
        JSONObject json = JSON.parseObject(params);
        String url = json.getString("url");
        String method = json.getString("method");
        String requestType = json.getString("requestType");
        StringBuilder param = new StringBuilder();
        String cookie="JSESSIONID="+cookies[0].getValue();
        Set<String> it = json.keySet();
        for(String key:it){
            if(key.equals("url")||key.equals("method")||key.equals("requestType")){
                continue;
            }
            String value = json.getString(key);
            param.append("&").append(key).append("=").append(value);
            if("body".equals(requestType)){
                params = value;
                break;
            }
        }
        String result = "";
        if(method.equals("GET")){
            result= HttpHelper.sendGet(url+"?"+(param.length()>0?(param.toString().substring(1, param.toString().length())):""),cookie);
        }else {
            if(!"body".equals(requestType)&&param.length()>0){
                params = param.toString().substring(1, param.toString().length());
            }
            result = HttpHelper.post(url,params,requestType,method,cookie);
        }
        System.out.println(result);
        return JsonFormatUtils.formatJson(result);
    }

    /**
     * 跳转到xdoc接口文档首页
     */
    @GetMapping
    public String index(String url, String params, String method) {
        return "redirect:index.html?url="+url+"&params="+params+"&method="+method;
    }

}
