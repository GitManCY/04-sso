package com.cy.controller;

import com.cy.entity.User;
import com.cy.service.SendSms;
import com.cy.utils.LoginCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.aliyuncs.utils.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @projectName:04-sso
 * @see:com.cy.controller
 * @author:chengyang
 * @createTime:2020/7/4 10:44 下午
 * @version:1.0
 */
@Controller
public class IndexController {

    private static Set<User> dbusers;

    @Autowired
    private SendSms sendSms;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    static {
        dbusers = new HashSet<>();
        dbusers.add(new User(0, "15583082980", "123456"));
    }

    @PostMapping("/login")
    public String doLogin(User user, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        String code = redisTemplate.opsForValue().get(user.getTelephone());
        String requestCode = request.getParameter("code");
        Optional<User> first = null;
        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(user.getTelephone())) {
            first = dbusers.stream().filter(dbUser -> dbUser.getTelephone().equals(user.getTelephone())
                    && code.equals(requestCode)
            ).findFirst();
        }

        String target = (String) session.getAttribute("target");

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
    public String doLoginOut(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
        session.invalidate();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
//        redisTemplate.delete("15583082980");
        return "login-main";
    }


    @GetMapping("/retrieve/{phone}")
    @ResponseBody
    public String doRetrieve(@PathVariable("phone") String phone) {
        String bufferKey = String.valueOf(new StringBuffer().append("retrieve:").append(phone));
        System.out.println(bufferKey);
        String retrieveCode = redisTemplate.opsForValue().get(bufferKey);
        if (!StringUtils.isEmpty(retrieveCode)) {
            return phone + ":" + retrieveCode + "找回密码的验证码已存在未过期";
        }
        retrieveCode = UUID.randomUUID().toString().substring(0, 4);
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", retrieveCode);

        boolean isSend = sendSms.send(phone, "SMS_173246892", map);

        if (isSend) {
            redisTemplate.opsForValue().set(bufferKey, retrieveCode, 10, TimeUnit.MINUTES);
            return phone + ":" + retrieveCode + "发送成功";
        } else {
            return "发送失败";
        }
    }


    @PostMapping("/retrieve")
    public String doRetrieve(User user, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        String bufferKey = String.valueOf(new StringBuffer().append("retrieve:").append(user.getTelephone()));
        String code = redisTemplate.opsForValue().get(bufferKey);
        System.out.println(code);
        String requestCode = request.getParameter("code");
        //TODO 不当操作会出现空指针
        if (code.equals(requestCode)) {
            for (User dbUser : dbusers) {
                if (dbUser.getTelephone().equals(user.getTelephone())) {
                    dbUser.setPassword(user.getPassword());
                }
                System.out.println(dbUser);
                session.setAttribute("dbUser", dbUser);
            }
        } else {
            session.setAttribute("msg", "手机号验证码错误");
        }
        return "login";
    }


//    private Optional<User> validate(User user, String code, String requestCode) {
//        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(user.getTelephone()) && !StringUtils.isEmpty(user.getPassword())) {
//
//            return dbusers.stream().filter(dbUser -> dbUser.getTelephone().equals(user.getTelephone())
//                    && code.equals(requestCode)
//            ).findFirst();
//        }
//        return null;
//    }
}
