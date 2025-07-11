package com.api.showDoc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** 测试菜单
 * @author 黄育益
 * @date 2025/6/11 20:31
 * @description TODO1111111111111111111111111
 * @version 1.0
 */
@Controller
@RequestMapping("/test")
public class TestController {

//    /** 测试接口
//     * @author 黄育益
//     * @date 2025/6/10 17:56
//     * @description TODO
//     */
//    @GetMapping("index")
//    @RequiresPermissions("base:sysregion:index")
//    public ResEnty index(EntyModel entyModel) {
//        return null;
//    }
//
//   /**
//    * @param null
//    * @rep
//    * @author 黄育益
//    * @date 2025/6/11 19:39
//    * @description TODO
//    */
//    @PostMapping("index2")
//    public List<ResEnty> index2(List<EntyModel> entyModel) {
//        return null;
//    }

    
//    /** 测试标题
//     * @param request 参数1
//     * @param entyModel 参数2
//     * @param param 参数3
//     * @param param2 参数4|Y
//     * @params [username 用户名|Integer|y]
//     * @params [username 用户名|Integer|必填,username2 用户名|Boolean|必填,username 用户名|必填]
//     * @params EntyModel
//     * @params [EntyModel,PramModel]
//     * @resp EntyModel
//     * @see EntyModel
//     * @author 黄育益
//     * @date 2025/6/11 20:31
//     * @respbody |EntyModel|
//     * @return 签到信息
//     * @description TODO
//     */
//    @PostMapping("index3")
//    @ResponseBody
//    public boolean index3(HttpServletRequest request, EntyModel entyModel, String param,@NotEmpty String param2) {
//        System.out.println(param);
//        System.out.println(entyModel.toString());
//        return true;
//    }

    /**
     * @return 3
     * @author 黄育益
     * @date 2025/7/11 17:59
     * @description TODO
     */
    @PostMapping("index1")
    public boolean index1() {
        return true;
    }

    /**
     * @param param c
     * @params username 用户名|Integer|必填
     * @params [EntyModel,PramModel]
     * @resp EntyModel
     * @respbody {code:0,data:[|EntyModel|],msg:成功}
     * @return 返回结果描述
     * @author 黄育益
     * @date 2025/7/11 18:50
     * @description TODO
     */
    @PostMapping("index2")
    public String index2(String param) {
        return null;
    }


}
