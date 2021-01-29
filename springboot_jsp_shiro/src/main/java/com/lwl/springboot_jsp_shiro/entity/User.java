package com.lwl.springboot_jsp_shiro.entity;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)//使setter方法返回当前对象
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private String id;

    private String username;

    private String password;

    private String salt;

    /**
     * 定义角色集合
     */
    private List<Role> roles;
}
