package com.api.showDoc.controller;

import com.api.showDoc.javaParser.xdoc.resolver.IgnoreApi;
import com.api.showDoc.service.XDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * XDoc的Spring Web入口
 *
 * @author huangyuyi
 *
 */
@IgnoreApi// 加上次注解 ，不生产文档
@RequestMapping("xdoc")
@RestController
public class XDocController {

    @Autowired
    private XDocService xDocService;

    /**
     * 生成文档
     * @author huangyuyi
     * @description  获取所有文档a
     * @date 2019/3/26
    */
    @ResponseBody
    @RequestMapping("apis")
    public Object apis() {
        return xDocService.apis();
    }

}
