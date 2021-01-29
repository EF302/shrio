package com.lwl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;

public class TestAuthenticator {
    public static void main(String[] args) {
        //1.创建安全管理器对象,是SecurityManager接口的间接实现
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        //2.给安全管理器设置realm，realm对象用于存储认证数据
        securityManager.setRealm(new IniRealm("classpath:shiro.ini"));
        //3.给全局安全工具类SecurityUtils设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        //4.关键对象subject主体，即当前登录的用户对象
        Subject subject = SecurityUtils.getSubject();
        //5.创建令牌，是AuthenticationToken接口的间接实现
        UsernamePasswordToken token = new UsernamePasswordToken("zs", "123");
        try {
            System.out.println("认证状态" + subject.isAuthenticated());
            //=============用户认证==================
            subject.login(token);
            System.out.println("认证状态" + subject.isAuthenticated());
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("认证失败，用户名不存在");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("认证失败，密码错误");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
