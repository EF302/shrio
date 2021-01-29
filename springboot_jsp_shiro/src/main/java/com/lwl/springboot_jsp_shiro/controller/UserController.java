package com.lwl.springboot_jsp_shiro.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lwl.springboot_jsp_shiro.entity.User;
import com.lwl.springboot_jsp_shiro.service.UserService;
import com.lwl.springboot_jsp_shiro.utils.VerifyCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 验证码方法
     *
     * @param session
     * @param response
     * @throws IOException
     */
    @RequestMapping("/getImage")
    public void getImage(HttpSession session, HttpServletResponse response) throws IOException {
        //生成验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //验证码放入session
        session.setAttribute("code", code);
        //验证码存入图片
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("image/png");
        VerifyCodeUtils.outputImage(220, 60, outputStream, code);
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @RequestMapping("/register")
    public String register(User user) {
        try {
            userService.register(user);
            return "redirect:login.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:register.jsp";
        }
    }

    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping("/logout")
    public String logout() {
        //SecurityUtils.getSubject()之所以能获取到用户对象，是因为经过shiroFilter拦截处理，实现了对象注入
        Subject subject = SecurityUtils.getSubject();
        //退出
        subject.logout();//退出用户
        return "redirect:login.jsp";
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param code
     * @param session
     * @return
     */
    @RequestMapping("/login")
    public String login(String username, String password, String code, HttpSession session) {
        //获取比较验证码
        String codes = (String) session.getAttribute("code");
        try {
            if (codes != null && codes.equalsIgnoreCase(code)) {
                //SecurityUtils.getSubject()之所以能获取到用户对象，是因为经过shiroFilter拦截处理，实现了对象注入
                Subject subject = SecurityUtils.getSubject();
                //登录认证
                subject.login(new UsernamePasswordToken(username, password));
                return "redirect:/index.jsp";
            } else {
                throw new RuntimeException("验证码错误！");
            }
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误！");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("异常信息：" + e.getMessage());
        }
        return "redirect:login.jsp";
    }
}
