package com.api.showDoc.controller;

import com.api.showDoc.model.Model;
import com.api.showDoc.model.ResultModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/** 测试菜单
 * @author huangyuyi
 * @date 2025/6/11 20:31
 * @description TODO
 * @version 1.0
 */
@Controller
@RequestMapping("/test")
public class TestController {

    /** 功能
     * @param request 参数1
     * @param model 参数2
     * @param param 参数3|Y
     * @param param2 参数4
     * @params [param3 参数5|Integer|y]
     * @params [param4 参数6|Integer|必填,param5 参数7|Boolean|必填,param6 参数8|必填]
     * @params ShowdocModel
     * @params [ApiAction,ApiModule]
     * @resp EntyModel
     * @see Model
     * @author huangyuyi
     * @date 2025/6/11 20:31
     * @respbody {"code":0,date:[|ResultModel|],"msg":"成功"}
     * @return 返回结果描述
     * @description TODO 功能描述
     */
    @PostMapping("index")
    @RequiresPermissions("base:sysregion:index3")
    @ResponseBody
    public ResultModel index(HttpServletRequest request, Model model, String param, @NotEmpty String param2) {
        return null;
    }

    /**功能1
     * @return 返回结果描述
     * @author huangyuyi
     * @date 2025/7/12 12:08
     * @description TODO 功能详细描述
     */
    @PostMapping("index1")
    @ResponseBody
    public Integer index1(HttpServletRequest request, String param, @NotEmpty String param2) {
        return null;
    }

    /**功能2
     * @param param 参数1|y
     * @param param2 参数1
     * @resp 返回数量
     * @return 返回结果描述
     * @author huangyuyi
     * @date 2025/7/12 12:42
     * @description TODO 功能详细描述
     */
    @PostMapping("index2")
    @ResponseBody
    public Integer index2(HttpServletRequest request, String param, String param2) {
        return null;
    }

    /**功能3
     * @param param 参数1|y
     * @param param2 参数1
     * @resp 返回数量
     * @respbody {"code":0,data:123456,"msg":"成功"}
     * @return 返回结果描述
     * @author huangyuyi
     * @date 2025/7/12 12:42
     * @description TODO 功能详细描述
     */
    @PostMapping("index3")
    @ResponseBody
    public Integer index3(HttpServletRequest request, String param, String param2) {
        return null;
    }

    /**功能4
     * @param param 参数1|y
     * @param param2 参数1
     * @resp ApiAction
     * @respbody {"code":0,data:|ApiAction|,"msg":"成功"}
     * @return 返回结果描述
     * @author huangyuyi
     * @date 2025/7/12 12:42
     * @description TODO 功能详细描述
     */
    @PostMapping("index4")
    @ResponseBody
    public HashMap index4(HttpServletRequest request, String param, String param2) {
        return null;
    }

    /**功能5
     * @params Model
     * @resp ApiAction
     * @respbody {"code":0,data:|ApiAction|,"msg":"成功"}
     * @return 返回结果描述
     * @author huangyuyi
     * @date 2025/7/12 12:42
     * @description TODO 功能详细描述
     */
    @PostMapping("index5")
    @ResponseBody
    public HashMap index5(HttpServletRequest request, HashMap param) {
        return null;
    }

    /**功能6
     * @params [Model,ResultModel]
     * @resp ApiAction
     * @respbody {"code":0,data:|ApiAction|,"msg":"成功"}
     * @return 返回结果描述
     * @author huangyuyi
     * @date 2025/7/12 12:42
     * @description TODO 功能详细描述
     */
    @PostMapping("index6")
    @ResponseBody
    public HashMap index6(HttpServletRequest request, HashMap param) {
        return null;
    }

    /**功能7
     * @params [param 参数1|Integer|必填,param2 参数2|Boolean|必填,param3 参数3|必填,param4 参数4]
     * @resp ApiAction
     * @respbody ApiAction
     * @return 返回结果描述
     * @author huangyuyi
     * @date 2025/7/12 12:42
     * @description TODO 功能详细描述
     */
    @PostMapping("index7")
    @ResponseBody
    public HashMap index7(HttpServletRequest request, HashMap param) {
        return null;
    }

    /**功能8
     * @return 返回结果描述
     * @author huangyuyi
     * @date 2025/7/12 12:42
     * @description TODO 功能详细描述
     */
    @PostMapping("index8")
    @ResponseBody
    public ResultModel index8(HttpServletRequest request, Model model) {
        return null;
    }

}
