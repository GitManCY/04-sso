package com.cy.controller;

import com.cy.entity.User;
import com.cy.utils.LoginCache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @description:
 * @projectName:04-sso
 * @see:com.cy.controller
 * @author:chengyang
 * @createTime:2020/7/4 10:44 下午
 * @version:1.0
 */
@Controller
public class LoginController {

    private static Set<User> dbusers;

    static {
        dbusers = new HashSet<>();
        dbusers.add(new User(0, "15583082980", "123456"));
    }

    @PostMapping("/login")
    public String doLogin(User user, HttpSession session, HttpServletResponse response) {
        String target = (String) session.getAttribute("target");
        Optional<User> first = dbusers.stream().filter(dbUser -> dbUser.getTelephone().equals(user.getTelephone())
                && dbUser.getCode().equals(user.getCode())
        ).findFirst();

        if (first.isPresent()) {
            String token = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("TOKEN", token);
            cookie.setDomain("cy.com");
            response.addCookie(cookie);
            LoginCache.login.put("token", first.get());
            System.out.println(LoginCache.login.toString());
        } else {
            session.setAttribute("msg", "手机号验证码错误");
            return "login";
        }
        return "redirect:" + target;
    }

    @GetMapping("/login/info")
    @ResponseBody
    public ResponseEntity<User> getUserInfo(String token) {
        if (!StringUtils.isEmpty(token)) {
            User user = LoginCache.login.get(token);
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/loginout")
    public String doLoginOut(HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "login";
    }
}
