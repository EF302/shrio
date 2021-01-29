package com.lwl.springboot_jsp_shiro.service;

import java.util.List;

import com.lwl.springboot_jsp_shiro.entity.Perms;

public interface RoleService {

    /**
     * 根据角色id查询权限集合
     *
     * @param id
     * @return
     */
    List<Perms> findPermsByRoleId(String id);
}
