package com.lwl.springboot_jsp_shiro.dao;

import java.util.List;

import com.lwl.springboot_jsp_shiro.entity.Perms;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDao {

    /**
     * 根据角色id查询权限集合
     *
     * @param id
     * @return
     */
    List<Perms> findPermsByRoleId(String id);
}
