<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
//请求地址的访问根路径：http://localhost:8080/20200227_Servlet过滤器
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath", basePath);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="zh">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
<title>登录页面 - 学生管理系统</title>
<link rel="icon" href="favicon.ico" type="image/ico">
<link href="${basePath }css/bootstrap.min.css" rel="stylesheet">
<link href="${basePath }css/materialdesignicons.min.css" rel="stylesheet">
<link href="${basePath }css/style.min.css" rel="stylesheet">
<style>
.lyear-wrapper {
    position: relative;
}
.lyear-login {
    display: flex !important;
    min-height: 100vh;
    align-items: center !important;
    justify-content: center !important;
}
.login-center {
    background: #fff;
    min-width: 38.25rem;
    padding: 2.14286em 3.57143em;
    border-radius: 5px;
    margin: 2.85714em 0;
}
.login-header {
    margin-bottom: 1.5rem !important;
}
.login-center .has-feedback.feedback-left .form-control {
    padding-left: 38px;
    padding-right: 12px;
}
.login-center .has-feedback.feedback-left .form-control-feedback {
    left: 0;
    right: auto;
    width: 38px;
    height: 38px;
    line-height: 38px;
    z-index: 4;
    color: #dcdcdc;
}
.login-center .has-feedback.feedback-left.row .form-control-feedback {
    left: 15px;
}
</style>
</head>
  
<body>
<div class="row lyear-wrapper">
  <div class="lyear-login">
    <div class="login-center">
      <div class="login-header text-center">
        <a href="javascript:;"> <img alt="light year admin" src="${basePath }images/logo-sidebar.png"> </a>
      </div>
      <form action="user?method=login" method="post">
      	<c:if test="${not empty param.msg }">
      		<div class="alert alert-danger alert-dismissible" role="alert">
		          <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		          <strong>${param.msg }</strong>
	        </div>
      	</c:if>
      
        <div class="form-group has-feedback feedback-left">
          <input type="text" placeholder="请输入您的用户名" class="form-control" name="username" id="username" />
          <span class="mdi mdi-account form-control-feedback" aria-hidden="true"></span>
        </div>
        <div class="form-group has-feedback feedback-left">
          <input type="password" placeholder="请输入密码" class="form-control" id="password" name="password" />
          <span class="mdi mdi-lock form-control-feedback" aria-hidden="true"></span>
        </div>
        <div class="form-group has-feedback feedback-left row">
          <div class="col-xs-7">
            <input type="text" name="captcha" class="form-control" placeholder="验证码">
            <span class="mdi mdi-check-all form-control-feedback" aria-hidden="true"></span>
          </div>
          <div class="col-xs-5">
            <img src="${basePath }images/captcha.png" class="pull-right" id="captcha" style="cursor: pointer;" onclick="this.src=this.src+'?d='+Math.random();" title="点击刷新" alt="captcha">
          </div>
        </div>
        <div class="form-group">
          <button class="btn btn-block btn-primary" type="submit" >立即登录</button>
          <button class="btn btn-block btn-info" type="button" onclick="location.href='register.jsp'">注册</button>
        </div>
      </form>
      <hr>
      <footer class="col-sm-12 text-center">
        <p class="m-b-0">Copyright © 2020 All right reserved</p>
      </footer>
    </div>
  </div>
</div>
<script type="text/javascript" src="${basePath }js/jquery.min.js"></script>
<script type="text/javascript" src="${basePath }js/bootstrap.min.js"></script>
<script type="text/javascript">
	
</script>
</body>
</html>