package com.cy.service;

import java.util.Map;

/**
 * @description:
 * @projectName:ali-sms
 * @see:com.cy.service
 * @author:chengyang
 * @createTime:2020/5/8 9:23 上午
 * @version:1.0
 */
public interface SendSms {
     boolean send(String phoneNum, String templateCode, Map<String, Object> map);
}
