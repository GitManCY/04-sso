package com.cy.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.cy.service.SendSms;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description:
 * @projectName:ali-sms
 * @see:com.cy.service.impl
 * @author:chengyang
 * @createTime:2020/5/8 9:24 上午
 * @version:1.0
 */
@Service
public class SendSmsImpl implements SendSms {
    @Override
    public boolean send(String phoneNum, String templateCode, Map<String, Object> code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4G7sBvJQ5fVKn3f9gUiS", "C2uXEbREAn7FxSJnECbvTDJ0N8DLNS");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("SignName", "找房");
        request.putQueryParameter("TemplateCode", templateCode);

        request.putQueryParameter("TemplateParam", JSON.toJSONString(code));

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
