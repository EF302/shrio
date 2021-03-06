<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Title</title>
</head>
<body>
    <h1>系统主页V1.0</h1>
    <h1>你好，<shiro:principal/>!</h1>
    <shiro:authenticated>
        认证通过后展示<br/>
    </shiro:authenticated>
    <shiro:notAuthenticated>
        没有认证之后展示<br/>
    </shiro:notAuthenticated>

    <ul>
        <%--<shiro:hasRole name="user">--%>
        <shiro:hasAnyRoles name="user,admin">
            <li>
                <a href="">用户管理</a>
                <ul>
                    <shiro:hasPermission name="user:add:*">
                        <li><a href="">添加</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="user:delete:*">
                        <li><a href="">删除</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="user:update:*">
                        <li><a href="">修改</a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="order:find:*">
                        <li><a href="">查询</a></li>
                    </shiro:hasPermission>
                </ul>
            </li>
        </shiro:hasAnyRoles>
        <shiro:hasRole name="admin">
            <li><a href="">商品管理</a></li>
            <li><a href="">订单管理</a></li>
            <li><a href="">物流管理</a></li>
        </shiro:hasRole>
    </ul>
    <a href="${pageContext.request.contextPath}/user/logout">退出登录</a>
</body>
</html>