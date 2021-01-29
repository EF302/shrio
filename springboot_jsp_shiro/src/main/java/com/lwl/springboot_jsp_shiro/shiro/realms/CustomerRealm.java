package com.lwl.springboot_jsp_shiro.shiro.realms;

import java.util.List;

import com.lwl.springboot_jsp_shiro.entity.Perms;
import com.lwl.springboot_jsp_shiro.entity.User;
import com.lwl.springboot_jsp_shiro.service.RoleService;
import com.lwl.springboot_jsp_shiro.service.UserService;
import com.lwl.springboot_jsp_shiro.shiro.salt.MyByteSource;
import com.lwl.springboot_jsp_shiro.utils.ApplicationContextUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * 自定义realm
 */
public class CustomerRealm extends AuthorizingRealm {

    /**
     * 认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证！");
        //获取当前登录用户名
        String principal = (String) token.getPrincipal();
        //在工厂中获取service对象
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        //根据用户名去数据库查询，看是否存在对应用户
        User user = userService.findByUserName(principal);
        if (!ObjectUtils.isEmpty(user)) {
            return new SimpleAuthenticationInfo(
//                    user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), this.getName());
                    //加入redis缓存后，涉及序列化
                    user.getUsername(), user.getPassword(), new MyByteSource(user.getSalt()), this.getName());
        }
        return null;
    }

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权！");
        //获取当前登录用户名
        String primaryPrincipal = (String) principals.getPrimaryPrincipal();
        System.out.println("授权验证——用户名：" + primaryPrincipal);
        //根据主身份信息获取角色和权限信息
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        RoleService roleService = (RoleService) ApplicationContextUtils.getBean("RoleService");
        //根据用户名去数据库查询，看是否存在对应用户
        User user = userService.findRolesByUserName(primaryPrincipal);
        //提取查询到的用户数据中的角色信息，进行相关授权操作
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            user.getRoles().forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getName());
                //根据角色id去获取对应的权限信息
                List<Perms> perms = roleService.findPermsByRoleId(role.getId());
                if (!CollectionUtils.isEmpty(perms)) {
                    perms.forEach(perms1 -> {
                        simpleAuthorizationInfo.addStringPermission(perms1.getName());
                    });
                }
            });
            return simpleAuthorizationInfo;
        }
        return null;
    }

}
