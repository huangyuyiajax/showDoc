package com.api.showDoc.controller;

import com.api.javaParser.xdoc.resolver.IgnoreApi;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 黄育益
 * @version 1.0
 * @date 2025/6/10 13:55
 * @description TODO
 */
@Controller
@IgnoreApi// 加上次注解 ，不生产文档
@RequestMapping("/showdoc")
public class ShowdocController {

    /**
     * @author 黄育益
     * @date 2025/6/10 13:54
     * @description TODO
     */
    @GetMapping
//    @IgnoreApi // 加上次注解 ，不生产文档
//    @RequiresPermissions("base:sysregion:index")
    public String showdoc() {
        return "redirect:showdoc.html";
    }


}
