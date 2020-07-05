package com.cy.controller;

import com.cy.entity.User;
import com.cy.utils.LoginCache;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * @description:
 * @projectName:04-sso
 * @see:com.cy.controller
 * @author:chengyang
 * @createTime:2020/7/4 10:44 下午
 * @version:1.0
 */
@Controller
@RequestMapping("/view")
public class ViewController {

    @GetMapping("/login")
    public String toLogin(@RequestParam(required = false, defaultValue = "") String target, HttpSession session
            , @CookieValue(value = "TOKEN", required = false) Cookie cookie) {
        if (StringUtils.isEmpty(target)) {
            target = "http://login.cy.com:9090/view/main";
        }
        if (cookie != null) {
            String value = cookie.getValue();
            System.out.println(value);
            //TODO  cookie已经写出来了 但是拿到的USER值为空
//            User user = LoginCache.login.get(value);
//            System.out.println(user);
            if (!StringUtils.isEmpty(value)) {
                session.setAttribute("result", "已经登陆");
                return "redirect:" + target;
            }
        }
        //TODO 要做target重定向校验
        session.setAttribute("target", target);
        System.out.println(target);
        return "login";
    }

    @GetMapping("/main")
    public String toMain(@CookieValue(value = "TOKEN", required = false) Cookie cookie, HttpSession session) {
        if (cookie != null) {
            String value = cookie.getValue();
            System.out.println(value);
            //TODO  cookie已经写出来了 但是拿到的USER值为空
//            User user = LoginCache.login.get(value);
//            System.out.println(user);
            if (!StringUtils.isEmpty(value)) {
                session.setAttribute("result", "已经登陆");
            }
        }
        return "login-main";
    }
}
