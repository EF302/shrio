package com.lwl.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

//自定义realm 加入MD5 + salt + hash
public class CustomerMd5Realm extends AuthorizingRealm {
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //得到主身份信息，即当前用户名
        String primaryPrincipal = (String)principals.getPrimaryPrincipal();
        System.out.println("认证——身份信息："+primaryPrincipal);
        //根据身份信息（用户名）获取当前用户的角色信息，以及权限信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //模拟：将数据库查询的角色信息赋值给权限对象
        simpleAuthorizationInfo.addRole("admin");
        simpleAuthorizationInfo.addRole("user");
        //模拟：将数据库查询权限字符串信息赋值给权限对象
        simpleAuthorizationInfo.addStringPermission("user:*:01");
        simpleAuthorizationInfo.addStringPermission("product:create");
        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户名
        String principal = (String) token.getPrincipal();
        //根据用户名查询数据库
        if ("zs".equals(principal)){
            //数据库用户名，数据库MD5+salt密码，注册时随机盐，当前realm的名字，
            //参数1：数据库用户名；参数2:密码+随机盐的MD5后，再散列1024次后的值；参数3：注册时的盐值；参数4：当前realm名字
            return new SimpleAuthenticationInfo(
                    principal,
                    "e4f9bf3e0c58f045e62c23c533fcf633",
                    ByteSource.Util.bytes("X0*7ps"),
                    this.getName());
        }
        return null;
    }
}
