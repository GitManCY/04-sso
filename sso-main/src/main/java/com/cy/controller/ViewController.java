package com.cy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.Map;

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

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/index")
    public String toIndex(@CookieValue(required = false, value = "TOKEN") Cookie cookie, HttpSession session) {

        if (cookie != null) {
            String value = cookie.getValue();
            System.out.println(value);
            //TODO  cookie已经写出来了 但是拿到的USER值为空
            if (!StringUtils.isEmpty(value)) {
                session.setAttribute("result","已经登陆");
            }
        }
        return "index";
    }
}
