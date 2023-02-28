package com.example.demo.controller;

import com.example.demo.common.AjaxResult;
import com.example.demo.common.Constant;
import com.example.demo.common.SecurityUtil;
import com.example.demo.model.UserInfo;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/reg")
    public Object reg(String username,String password){


        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password)){
            return AjaxResult.fail(-1,"非法请求!");
        }

        int result = userService.add(username, SecurityUtil.encrypt(password));
        if(result == 1){
            return AjaxResult.success(1);
        }
        return AjaxResult.fail(-1,"注册失败!");
    }

    @RequestMapping("/login")
    public Object login(HttpServletRequest request, String username, String password){
        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password)){
            return AjaxResult.fail(-1,"非法请求!");
        }

        UserInfo userInfo = userService.getUserByName(username);
        if(userInfo == null || userInfo.getId()<=0){
            return AjaxResult.fail(-1,"登录失败!");
        }else{

            boolean result = SecurityUtil.decrypt(password,userInfo.getPassword());
            if(result){
                HttpSession session = request.getSession();
                session.setAttribute(Constant.SESSION_USERINFO_KEY,userInfo);
                return AjaxResult.success(1);
            }
        }
        return AjaxResult.fail(-1,"登陆失败!");
    }

    @RequestMapping("/logout")
    public Boolean logout(HttpServletRequest request){

        HttpSession session = request.getSession(false);
        if(session!=null && session.getAttribute(Constant.SESSION_USERINFO_KEY)!=null){
            session.removeAttribute(Constant.SESSION_USERINFO_KEY);
        }
        return true;
    }

    @RequestMapping("/myinfo")
    public UserInfo myinfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null && session.getAttribute(Constant.SESSION_USERINFO_KEY)!=null){
            return (UserInfo) session.getAttribute(Constant.SESSION_USERINFO_KEY);
        }
        return null;
    }

    @RequestMapping("/myinfobyuid")
    public UserInfo myinfobyuid(Integer uid){

        if(uid!=null && uid>0){
            return userService.searchByUid(uid);
        }

        return null;
    }
}
