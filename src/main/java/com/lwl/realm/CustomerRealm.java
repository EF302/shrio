package com.lwl.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

//自定义realm实现，目的：将认证/授权数据的来源转为数据库的实现
public class CustomerRealm extends AuthorizingRealm {
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //在token中获取用户名
        String principal = (String) token.getPrincipal();
        //模拟：根据身份信息使用jdbc mybatis查询相关数据库
        if ("zs".equals(principal)){
            //数据库用户名，数据库密码，当前realm的名字
            //参数1：返回数据库中正确的用户名；参数2：密码；参数3：当前realm的名字 this.getName()  ——> com.lwl.realm.CustomerRealm_0
            //完成用户名校验后，返回的simpleAuthenticationInfo对象（包含密码）是用于自动的密码校验
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo("zs","123",this.getName());
            System.out.println(this.getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }
}
