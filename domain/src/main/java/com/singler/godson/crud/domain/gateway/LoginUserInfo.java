package com.singler.godson.crud.domain.gateway;

import lombok.Data;

import java.io.Serializable;

/**
 * 当前登录人信息
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/12/4 16:25
 */
@Data
public class LoginUserInfo implements Serializable {
    private Long shopId;
    private Long platformId;
    private Long accountId;
    private String accountName;
    private Integer accountType;
    private Long organizeId;
    private String organizeName;
    private Long enterpriseId;
    private Integer isPersonal;
}
