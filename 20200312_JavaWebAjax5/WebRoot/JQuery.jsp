<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'JQuery.jsp' starting page</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    	<input type="text" name="username" id="username"/>
    	<input type="button" onclick="getInput()" value="获取Input的值"/>
  </body>
  <!-- 导入jQuery包 -->
  <script type="text/javascript" src="js/jquery.min.js"></script>
  <script type="text/javascript">
  	function getInput(){
  		var username = document.getElementById("username");
  		console.log(username.value);
  		//$---JQuery
  	}
  	
  </script>
</html>
