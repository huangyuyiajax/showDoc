package com.api.showDoc.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试标题
 * @author 黄育益
 * @description
 * @date 2025-06-09 19:52:01
*/
@Controller
@RequestMapping("/text")
public class TextController {

    /**
     * 跳转到接口文档页面
     */
    @GetMapping
//    @IgnoreApi // 加上次注解 ，不生产文档
//    @RequiresPermissions("base:sysregion:index")
    public String index() {
        return "redirect:showdoc.html";
    }


}
