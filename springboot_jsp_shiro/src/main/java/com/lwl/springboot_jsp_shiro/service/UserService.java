package com.lwl.springboot_jsp_shiro.service;

import com.lwl.springboot_jsp_shiro.entity.User;

public interface UserService {

    /**
     * 根据用户名查询角色
     *
     * @param username
     * @return
     */
    User findRolesByUserName(String username);

    /**
     * 注册用户
     *
     * @param user
     */
    void register(User user);

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    User findByUserName(String username);
}
