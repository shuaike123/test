<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
<title>显示所有数据</title>
<link rel="icon" href="favicon.ico" type="image/ico">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/materialdesignicons.min.css" rel="stylesheet">
<link href="css/style.min.css" rel="stylesheet">
<link rel="stylesheet" href="js/jconfirm/jquery-confirm.min.css">
<link href="css/animate.css" rel="stylesheet">

</head>
  
<body>
<%-- 使用静态包含：<jsp:include page=""></jsp:include> --%>
<div class="lyear-layout-web">
  <div class="lyear-layout-container">
    <!--左侧导航-->
    <aside class="lyear-layout-sidebar">
       
      <!-- logo -->
      <div id="logo" class="sidebar-header">
        <a href="index.html"><img src="images/logo-sidebar.png" title="LightYear" alt="LightYear" /></a>
      </div>
      <div class="lyear-layout-sidebar-scroll">
        
        <nav class="sidebar-main">
          <ul class="nav nav-drawer">
            <li class="nav-item"> <a href="index.html"><i class="mdi mdi-home"></i> 后台首页</a> </li>
            <li class="nav-item nav-item-has-subnav active open">
              <a href="javascript:void(0)"><i class="mdi mdi-account-multiple"></i> 用户</a>
              <ul class="nav nav-subnav">
                <li> <a href="add.jsp">增加</a> </li>
                <li class="active"> <a href="user?method=showUsers">显示</a> </li>
                <li> <a href="lyear_ui_modals.html">查询</a> </li>
              </ul>
            </li>
            <li class="nav-item nav-item-has-subnav">
              <a href="javascript:void(0)"><i class="mdi mdi-settings"></i>系统设置</a>
              <ul class="nav nav-subnav">
                <li> <a href="lyear_forms_elements.html">修改密码</a> </li>
                <li> <a href="lyear_forms_radio.html">个人信息</a> </li>
                <li> <a href="lyear_forms_checkbox.html">退出登陆</a> </li>
              </ul>
            </li>
          </ul>
        </nav>
        
        <div class="sidebar-footer">
        	
        </div>
      </div>
      
    </aside>
    <!--End 左侧导航-->
    
    <!--头部信息-->
    <header class="lyear-layout-header">
      
      <nav class="navbar navbar-default">
        <div class="topbar">
          
          <div class="topbar-left">
            <div class="lyear-aside-toggler">
              <span class="lyear-toggler-bar"></span>
              <span class="lyear-toggler-bar"></span>
              <span class="lyear-toggler-bar"></span>
            </div>
            <span class="navbar-page-title"> 用户 - 显示 </span>
          </div>
          
          <ul class="topbar-right">
            <li class="dropdown dropdown-profile">
              <a href="javascript:void(0)" data-toggle="dropdown">
                <img class="img-avatar img-avatar-48 m-r-10" src="images/users/avatar.jpg" alt="笔下光年" />
                <span>${sessionScope.user.username } <span class="caret"></span></span>
              </a>
              <ul class="dropdown-menu dropdown-menu-right">
                <li> <a href="lyear_pages_profile.html"><i class="mdi mdi-account"></i> 个人信息</a> </li>
                <li> <a href="lyear_pages_edit_pwd.html"><i class="mdi mdi-lock-outline"></i> 修改密码</a> </li>
                <li> <a href="javascript:void(0)"><i class="mdi mdi-delete"></i> 清空缓存</a></li>
                <li class="divider"></li>
                <li> <a href="lyear_pages_login.html"><i class="mdi mdi-logout-variant"></i> 退出登录</a> </li>
              </ul>
            </li>
            <!--切换主题配色-->
		    <li class="dropdown dropdown-skin">
			  <span data-toggle="dropdown" class="icon-palette"><i class="mdi mdi-palette"></i></span>
			  <ul class="dropdown-menu dropdown-menu-right" data-stopPropagation="true">
                <li class="drop-title"><p>主题</p></li>
                <li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="site_theme" value="default" id="site_theme_1" checked>
                    <label for="site_theme_1"></label>
                  </span>
                  <span>
                    <input type="radio" name="site_theme" value="dark" id="site_theme_2">
                    <label for="site_theme_2"></label>
                  </span>
                  <span>
                    <input type="radio" name="site_theme" value="translucent" id="site_theme_3">
                    <label for="site_theme_3"></label>
                  </span>
                </li>
			    <li class="drop-title"><p>LOGO</p></li>
				<li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="logo_bg" value="default" id="logo_bg_1" checked>
                    <label for="logo_bg_1"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_2" id="logo_bg_2">
                    <label for="logo_bg_2"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_3" id="logo_bg_3">
                    <label for="logo_bg_3"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_4" id="logo_bg_4">
                    <label for="logo_bg_4"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_5" id="logo_bg_5">
                    <label for="logo_bg_5"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_6" id="logo_bg_6">
                    <label for="logo_bg_6"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_7" id="logo_bg_7">
                    <label for="logo_bg_7"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_8" id="logo_bg_8">
                    <label for="logo_bg_8"></label>
                  </span>
				</li>
				<li class="drop-title"><p>头部</p></li>
				<li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="header_bg" value="default" id="header_bg_1" checked>
                    <label for="header_bg_1"></label>                      
                  </span>                                                    
                  <span>                                                     
                    <input type="radio" name="header_bg" value="color_2" id="header_bg_2">
                    <label for="header_bg_2"></label>                      
                  </span>                                                    
                  <span>                                                     
                    <input type="radio" name="header_bg" value="color_3" id="header_bg_3">
                    <label for="header_bg_3"></label>
                  </span>
                  <span>
                    <input type="radio" name="header_bg" value="color_4" id="header_bg_4">
                    <label for="header_bg_4"></label>                      
                  </span>                                                    
                  <span>                                                     
                    <input type="radio" name="header_bg" value="color_5" id="header_bg_5">
                    <label for="header_bg_5"></label>                      
                  </span>                                                    
                  <span>                                                     
                    <input type="radio" name="header_bg" value="color_6" id="header_bg_6">
                    <label for="header_bg_6"></label>                      
                  </span>                                                    
                  <span>                                                     
                    <input type="radio" name="header_bg" value="color_7" id="header_bg_7">
                    <label for="header_bg_7"></label>
                  </span>
                  <span>
                    <input type="radio" name="header_bg" value="color_8" id="header_bg_8">
                    <label for="header_bg_8"></label>
                  </span>
				</li>
				<li class="drop-title"><p>侧边栏</p></li>
				<li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="sidebar_bg" value="default" id="sidebar_bg_1" checked>
                    <label for="sidebar_bg_1"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_2" id="sidebar_bg_2">
                    <label for="sidebar_bg_2"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_3" id="sidebar_bg_3">
                    <label for="sidebar_bg_3"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_4" id="sidebar_bg_4">
                    <label for="sidebar_bg_4"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_5" id="sidebar_bg_5">
                    <label for="sidebar_bg_5"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_6" id="sidebar_bg_6">
                    <label for="sidebar_bg_6"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_7" id="sidebar_bg_7">
                    <label for="sidebar_bg_7"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_8" id="sidebar_bg_8">
                    <label for="sidebar_bg_8"></label>
                  </span>
				</li>
			  </ul>
			</li>
            <!--切换主题配色-->
          </ul>
          
        </div>
      </nav>
      
    </header>
    <!--End 头部信息-->
    
    <!--页面主要内容-->
    <main class="lyear-layout-content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-header"><h4>所有用户信息</h4></div>
              <div class="card-body">
                <table class="table table-bordered" style="text-align: center;">
                	<caption>
                		<c:if test="${not empty msg }">
				      		<div class="alert alert-danger alert-dismissible" role="alert">
						          <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						          <strong>${msg }</strong>
					        </div>
				      	</c:if>
                	</caption>
                  <thead>
                    <tr>
                      <th style="text-align: center;">编号</th>
                      <th style="text-align: center;">用户名</th>
                      <th style="text-align: center;">密码</th>
                      <th style="text-align: center;width: 150px;">操作</th>
                    </tr>
                  </thead>
                  <tbody>
                  	<c:forEach items="${users }" var="user">
	                  	<tr>
	                      <td>${user.id }</td>
	                      <td>${user.username }</td>
	                      <td>${user.password }</td>
	                      <td>
	                      	<div class="btn-group btn-group-sm">
		                      <button class="btn btn-default" onclick="window.location.href='user?method=queryUser&id=${user.id }'">修改</button>
		                      <button class="btn btn-default" onclick="deleteUser(${user.id})">删除</button>
		                    </div>
	                      </td>
	                    </tr>
                  	</c:forEach>
                  </tbody>
                </table>
                <nav>
                  <ul class="pagination pagination-circle">
                  	<c:choose>
                  		<c:when test="${pm.pn <= 1 }">
                  			<li class="disabled">
		                        <span><i class="mdi mdi-chevron-left"></i></span>
		                    </li>
                  		</c:when>
                  		<c:otherwise>
                  			<li>
		                      <a href="user?method=showUsers&pn=${pn - 1 }">
		                        <span><i class="mdi mdi-chevron-left"></i></span>
		                      </a>
		                    </li>
                  		</c:otherwise>
                  	</c:choose>
                    
                    <c:forEach begin="1" end="${pm.pages }" step="1" var="i">
                    	<c:if var="is" test="${pm.pn == i }">
                    		<li class="active"><a href="javascript:;">${i }</a></li>
                    	</c:if>
                    	<c:if test="${!is }">
                    		<li><a href="user?method=showUsers&pn=${i }">${i }</a></li>
                    	</c:if>
                    </c:forEach>
                    
                    <c:if var="lastPage" test="${pm.pn >= pm.pages }">
                    	<li class="disabled">
	                        <span><i class="mdi mdi-chevron-right"></i></span>
	                    </li>
                    </c:if>
                    <c:if test="${!lastPage }">
                    	<li>
	                      <a href="user?method=showUsers&pn=${pm.pn + 1 }">
	                        <span><i class="mdi mdi-chevron-right"></i></span>
	                      </a>
	                    </li>
                    </c:if>
                  </ul>
                </nav>
                </div>
              </div>
            </div>
          </div>
        </div>
        
      </div>
      
    </main>
    <!--End 页面主要内容-->
  </div>
</div>

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/perfect-scrollbar.min.js"></script>
<script type="text/javascript" src="js/main.min.js"></script>
<script src="js/jconfirm/jquery-confirm.min.js"></script>
<!--消息提示-->
<script src="js/bootstrap-notify.min.js"></script>
<script type="text/javascript" src="js/lightyear.js"></script>

<script type="text/javascript">
	function deleteUser(id){
		 $.confirm({
	       title: '警告',
	       content: '确认删除吗',
	       type: 'orange',
	       typeAnimated: false,
	       buttons: {
	           omg: {
	               text: '确定',
	               btnClass: 'btn-orange',
	               action: function(){
	               	   //显示转圈圈的样式
	               	   lightyear.loading('show');
	               	   //延迟2秒钟调用隐藏这个样式
	               	   setTimeout(function() {
					        lightyear.loading('hide');
					        lightyear.notify('删除成功', 'success', 3000);
					   }, 2000);
					   //延迟5秒钟调用方法
					   setTimeout(function() {
					   	window.location.href="user?method=deleteUser&id="+id;
					   }, 5000);
                   },
	           },
	           close: {
	               text: '关闭',
	           }
	       }
	   });
	}
</script>
</body>
</html>