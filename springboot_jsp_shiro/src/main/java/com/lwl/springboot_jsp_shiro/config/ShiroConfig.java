package com.lwl.springboot_jsp_shiro.config;

import java.util.HashMap;
import java.util.Map;

import com.lwl.springboot_jsp_shiro.shiro.cache.RedisCacheManager;
import com.lwl.springboot_jsp_shiro.shiro.realms.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用来整合shiro框架相关的配置类
 */
@Configuration
public class ShiroConfig {
    /**
     * 1.创建shiroFilter——负责拦截所有请求，依赖SecurityManager
     *
     * @param defaultWebSecurityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        System.out.println("1.创建shiroFilter——负责拦截所有请求，依赖SecurityManager");

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //给filter设置安全管理器SecurityManager
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //配置拦截
        Map<String, String> map = new HashMap<String, String>();
        //authc 请求这个资源需要认证和授权
        map.put("/**", "authc");
        //anon 设置为公共资源
        //注 公共资源一般放在受限资源之后
        map.put("/user/login", "anon");
        map.put("/user/register", "anon");
//        map.put("/register.jsp","anon");
        map.put("/user/getImage", "anon");
        //默认认证界面路径,默认就是login.jsp
        shiroFilterFactoryBean.setLoginUrl("/login.jsp");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }

    /**
     * 2.创建安全管理器——依赖Reaml
     *
     * @param realm
     * @return
     */
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("customerRealm") Realm realm) {
        System.out.println("2.创建安全管理器——依赖Reaml");
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置Realm
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }

    /**
     * 3.创建自定义realm
     *
     * @return
     */
    @Bean("customerRealm")
    public Realm getRealm() {
        System.out.println("3.创建自定义realm");
        CustomerRealm customerRealm = new CustomerRealm();

        //===============修改凭证校验匹配器为hash凭证匹配器==============
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1024);
        customerRealm.setCredentialsMatcher(credentialsMatcher);

        //==================开启缓存管理===========================
        //默认本地内存缓存，缺点：应用宕机、停止后，缓存数据消失
//        customerRealm.setCacheManager(new EhCacheManager());
        //redis缓存，适合分布式应用。注 涉及序列化问题，相关数据需要能序列化
        customerRealm.setCacheManager(new RedisCacheManager());
        //全局缓存
        customerRealm.setCachingEnabled(true);
        //认证缓存
        customerRealm.setAuthenticationCachingEnabled(true);
        //设置认证缓存名字，默认名为：this.getClass().getName() + ".authenticationCache"
        customerRealm.setAuthenticationCacheName("authenticationCache");
        //授权缓存
        customerRealm.setAuthorizationCachingEnabled(true);
        customerRealm.setAuthorizationCacheName("authorizationCache");

        return customerRealm;
    }
}