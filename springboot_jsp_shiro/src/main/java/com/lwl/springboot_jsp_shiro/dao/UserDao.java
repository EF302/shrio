package com.lwl.springboot_jsp_shiro.dao;

import com.lwl.springboot_jsp_shiro.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    /**
     * 根据用户名查询角色
     *
     * @param username
     * @return
     */
    User findRolesByUserName(String username);

    /**
     * 添加用户
     *
     * @param user
     */
    void save(User user);

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    User findByUserName(String username);
}
