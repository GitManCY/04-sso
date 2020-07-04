package com.cy.utils;

import com.cy.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @projectName:04-sso
 * @see:com.cy.utils
 * @author:chengyang
 * @createTime:2020/7/4 11:25 下午
 * @version:1.0
 */
public class LoginCache {

    public static Map<String, User> login = new HashMap<>();

}
