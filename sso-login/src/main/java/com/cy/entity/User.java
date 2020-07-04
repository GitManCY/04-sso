package com.cy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @description:
 * @projectName:04-sso
 * @see:com.cy.entity
 * @author:chengyang
 * @createTime:2020/7/4 10:43 下午
 * @version:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    private Integer id;
    private String telephone;
    private String code;
}
