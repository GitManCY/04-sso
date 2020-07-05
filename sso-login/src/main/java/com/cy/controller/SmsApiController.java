package com.cy.controller;

import com.aliyuncs.utils.StringUtils;
import com.cy.service.SendSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @projectName:ali-sms
 * @see:com.cy.controller
 * @author:chengyang
 * @createTime:2020/7/3 11:40 上午
 * @version:1.0
 */
@RestController
@CrossOrigin
public class SmsApiController {

    @Autowired
    private SendSms sendSms;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/send/{phone}")
    public String code(@PathVariable("phone") String phone) {
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)){
            return phone + ":" + "已存在 未过期";
        }
        code = UUID.randomUUID().toString().substring(0, 4);
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);

        boolean isSend = sendSms.send(phone, "SMS_173246892", map);

        if (isSend) {
            redisTemplate.opsForValue().set(phone, code, 10, TimeUnit.MINUTES);
            return phone + ":" + code + "发送成功";
        } else {
            return "发送失败";
        }
    }
}
