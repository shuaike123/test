package com.zking.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jsonServlet")
public class JSONServlet extends HttpServlet{
	//序列化
	private static final long serialVersionUID = -3705311860886138726L;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//允许跨域请求
		response.setHeader("Access-Control-Allow-Origin","*");
		
		
		
		//怎么从后台传递数据到前端
		//1.获取PrintWriter对象
		PrintWriter out = response.getWriter();
		//2.将内容直接写到前端
		out.print("{\"password\":\"ewwww\",\"id\":2,\"username\":\"qqqqq\"}");
		out.flush();//刷新
		out.close();//关闭
	}
	

}
