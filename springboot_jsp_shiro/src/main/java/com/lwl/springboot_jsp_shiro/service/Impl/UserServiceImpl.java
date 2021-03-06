package com.lwl.springboot_jsp_shiro.service.Impl;

import com.lwl.springboot_jsp_shiro.dao.UserDao;
import com.lwl.springboot_jsp_shiro.entity.User;
import com.lwl.springboot_jsp_shiro.service.UserService;
import com.lwl.springboot_jsp_shiro.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserDao userDao;

    /**
     * 根据用户名查询角色
     *
     * @param username
     * @return
     */
    @Override
    public User findRolesByUserName(String username) {
        return userDao.findRolesByUserName(username);
    }

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    /**
     * 注册用户
     *
     * @param user
     */
    @Override
    public void register(User user) {
        //1.生成随机盐
        String salt = SaltUtils.getSalt(8);
        //2.将随机盐保存到数据
        user.setSalt(salt);
        //3.明文密码进行md5+salt+hash散列
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
        user.setPassword(md5Hash.toHex());
        //4.保存注册数据
        userDao.save(user);
    }
}
