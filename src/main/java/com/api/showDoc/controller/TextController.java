package com.api.showDoc.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/text")
public class TextController {

    /**
     * @author 黄育益
     * @date 2025/6/10 13:54
     * @description TODO
     */
    @GetMapping
//    @IgnoreApi // 加上次注解 ，不生产文档
//    @RequiresPermissions("base:sysregion:index")
    public String index() {
        return "redirect:showdoc.html";
    }

    /**
     * @author 黄育益
     * @date 2025/6/10 14:09
     * @description TODO
     */
    @GetMapping("index")
    @RequiresPermissions("base:sysregion:index")
    public String index2(String param,String param2) {
        return "redirect:showdoc.html";
    }

}
