package com.lwl;

import com.lwl.realm.CustomerMd5Realm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

import java.util.Arrays;

//使用自定义realm
public class TestCustomerMd5RealmAuthenticator {
    public static void main(String[] args) {
        //创建安全管理器
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //注入realm
        CustomerMd5Realm realm = new CustomerMd5Realm();

        //===todo 修改shiro密码校验时的默认的凭证匹配器SimpleCredentialMatcher
        //设置realm使用hash凭证匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //使用算法
        credentialsMatcher.setHashAlgorithmName("md5");
        //散列次数
        credentialsMatcher.setHashIterations(1024);
        //设置密码比较器为哈希凭证匹配器
        realm.setCredentialsMatcher(credentialsMatcher);

        defaultSecurityManager.setRealm(realm);
        //将安全管理器注入安全工具类
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //通过安全工具类获取subject
        Subject subject = SecurityUtils.getSubject();
        //认证
        UsernamePasswordToken token = new UsernamePasswordToken("zs", "123");
        try {
            subject.login(token);
            System.out.println("登陆成功");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误");
        }

        //======================权限校验==================
        if (subject.isAuthenticated()){
            System.out.println("==============权限===============");
            //基于角色权限控制
            System.out.println(subject.hasRole("super"));//认证一个角色调用一次doGetAuthorizationInfo()
            //基于多角色权限控制
            System.out.println(subject.hasAllRoles(Arrays.asList("admin", "super")));
            //是否具有其中一个角色
            boolean[] booleans = subject.hasRoles(Arrays.asList("admin","super","user"));
            for (boolean aBoolean : booleans) {
                System.out.println(aBoolean);
            }
//            ==============权限===============
//            认证——身份信息：zs
//            false
//            认证——身份信息：zs
//            认证——身份信息：zs
//            false
//            认证——身份信息：zs
//            认证——身份信息：zs
//            认证——身份信息：zs
//            true
//            false
//            true

            System.out.println("===========================");
            //基于权限字符串的访问控制 资源标识符:操作:资源类型
            System.out.println("权限："+subject.isPermitted("user:update:01"));////认证一个权限资源调用一次doGetAuthorizationInfo()
            System.out.println("权限："+subject.isPermitted("product:create:02"));
            //分别具有哪些权限
            boolean[] permitted = subject.isPermitted("user:*:01","order:*:10");
            for (boolean b : permitted) {
                System.out.println(b);
            }
            //同时具有哪些权限
           boolean permittedAll = subject.isPermittedAll("user:*:01","product:create:01");
            System.out.println(permittedAll);

//            认证——身份信息：zs
//            权限：true
//            认证——身份信息：zs
//            权限：true
//            认证——身份信息：zs
//            认证——身份信息：zs
//            true
//            false
//            认证——身份信息：zs
//            认证——身份信息：zs
//            true
        }
    }
}