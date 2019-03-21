package pers.mrwangx.netdisk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pers.mrwangx.netdisk.service.NetDiskService;
import pers.mrwangx.netdisk.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/3/9 20:14
 *****/
@CrossOrigin
@Controller
@RequestMapping(value = {"/user"})
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private NetDiskService netDiskService;

    @RequestMapping(path = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String login(@RequestBody String reqData, HttpSession session) {
        int status = 200;
        String msg = null;
        try {
            JSONObject reqJson = JSON.parseObject(reqData);
            if (userService.login(reqJson.getString("username"), reqJson.getString("password"))) {
                msg = "success";
                session.setAttribute("islogin", true);
            } else {
                status = 300;
                msg = "wrong username or password";
            }
        } catch (Exception e) {
            status = 400;
            msg = e.getMessage();
            e.printStackTrace();
        }
        return netDiskService.getReturnData(status, msg, null);
    }

    @RequestMapping(path = "/lg")
    public String lg() {
        return "/login.html";
    }


}
