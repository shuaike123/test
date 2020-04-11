package com.zking.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.zking.entity.PageManager;
import com.zking.entity.User;
import com.zking.service.UserService;
import com.zking.utils.NumberUtils;
import com.zking.utils.ServletUtils;

@WebServlet("/user")
public class UserServlet extends HttpServlet{
	//子类是父类的扩展
	
	private static final long serialVersionUID = 1L;
	
	//创建一个UserService对象
	private UserService userService = new UserService();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//重启项目（Debug启动，不要用start）：
		//1.只是修改方法的内容是不需要重启的。
		//2.如果增加了属性，或者增加了方法，或者增加了注解，都是需要重启的。
		//3.修改前端jsp页面是不需要重启的。
		//但是要注意：
		//a.修改完jsp记得保存，并且等待几秒钟再去刷新页面。因为修改完之后会进行编译，可能会刷到之前的页面。
		//b.如果发现修改的内容没有效果，记得清空浏览器缓存。
		//清空缓存快捷键：ctrl+shift+del键（delete键）
		//禁用缓存的设置：在刷新页面的时候禁用缓存。打开f12-->NetWork-->Disable cache打勾
		
		String methodName = request.getMethod();
		System.out.println("doGet---->"+methodName);
		//1.获取前端请求的内容是什么
		String method = request.getParameter("method");
		if ("showUsers".equals(method)) {
			//查询所有用户数据
			queryUsers(request, response);
		} else if ("deleteUser".equals(method)) {
			deleteUser(request, response);//删除用户
		} else if ("queryUser".equals(method)) {
			//修改之前先查询那一条要修改的数据
			queryUser(request, response);
		} else if ("logOut".equals(method)) {
			//销毁整个session
			request.getSession().invalidate();
			response.sendRedirect("login.jsp?msg="+URLEncoder.encode("注销成功！请重新登陆！", "UTF-8"));
		} else if ("ajaxShowUsers".equals(method)) {
			ajaxShowUsers(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String methodName = request.getMethod();
		//一般登陆和注册都是post请求，密码的东西不能够直接显示在地址栏
		//增加数据也是post请求
		System.out.println("doPost---->"+methodName);
		String method = request.getParameter("method");
		if ("addUser".equals(method)) {	//注册
			addUser(request, response);
		} else if ("login".equals(method)) {
			login(request, response);
		} else if ("updateUser".equals(method)) {
			updateUser(request, response);
		} else if ("addUser2".equals(method)) {
			addUser2(request, response);
		}
	}
	
	
	/**
	 * 	修改用户数据
	 */
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println(ServletUtils.parseParameter(User.class, request));
		//1.获取三个参数
		User user = ServletUtils.parseParameter(User.class, request);
	   	//2.将这个数据增加到数据库
    	int i = userService.updateUser(user);
		//3.将页面转发到或者重定向到首页
		//将增加结果存储到request域中，一次请求，多次转发页面有效
		//4.修改成功之后，重新显示首页数据
		response.sendRedirect("user?method=showUsers&msg="+URLEncoder.encode(i > 0?"修改成功！":"修改失败！", "UTF-8"));
	}

	/**
	 * 	查询单个用户信息
	 */
	private void queryUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = ServletUtils.parseParameter(User.class, request);
		System.out.println(user);
		User queryUser = userService.queryUser(user);
		//将用户信息存储到域中
		request.setAttribute("user", queryUser);
		//转发页面
		request.getRequestDispatcher("update.jsp").forward(request, response);
	}

	/**
	 * 	删除
	 */
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.获取参数
		User user = ServletUtils.parseParameter(User.class, request);
		//2.在service中调用数据库
    	int i = userService.deleteUser(user);
    	response.sendRedirect("user?method=showUsers&msg="+URLEncoder.encode(i > 0?"删除成功！":"删除失败！", "UTF-8"));
	}
	
	
	/**
	 * 	登陆
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		//1.获取参数
		//九大内置对象是jsp的不是Java的
		//Java获取session
		HttpSession session = request.getSession();
    	User user = ServletUtils.parseParameter(User.class, request);
    	//2.在service中核对用	户名和密码是否正确
    	User queryUser = userService.login(user);
		if(queryUser == null){
			response.sendRedirect("login.jsp?msg="+URLEncoder.encode("用户名或者密码错误", "UTF-8"));
		} else {
			//将该登陆的用户存储到session中
			session.setAttribute("user", user);
			//设置用户的活动时长为30秒
			session.setMaxInactiveInterval(30);
			//重新查询用户数据
			response.sendRedirect("user?method=showUsers");
		}
	}

	/**
	 * 	增加用户数据的方法
	 */
	private void addUser2(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		//1.获取两个参数
    	User user = ServletUtils.parseParameter(User.class, request);
    	System.out.println("user="+user);
    	//证明该用户名未被注册，直接注册
    	int i = userService.addUser(user);
    	response.sendRedirect("user?method=showUsers&msg="+URLEncoder.encode(i > 0?"增加成功！":"增加失败！", "UTF-8"));
	}
	
	/**
	 * 	增加用户数据的方法
	 */
	private void addUser(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		//1.获取两个参数
    	User user = ServletUtils.parseParameter(User.class, request);
    	//2.核对这个用户名是否存在
    	User checkUser = userService.checkUser(user);
    	if(checkUser == null){
    		//3.再service增加数据
    		//证明该用户名未被注册，直接注册
        	int i = userService.addUser(user);
			//3.将页面转发到或者重定向到首页
    		if (i > 0) 
    			response.sendRedirect("login.jsp?msg="+URLEncoder.encode("注册成功！请登录！", "UTF-8"));
    		else 
    			response.sendRedirect("register.jsp?msg="+URLEncoder.encode("注册失败！请重新注册！", "UTF-8"));
    	} else {
    		response.sendRedirect("register.jsp?msg="+URLEncoder.encode("该用户名已注册，请重新填写！", "UTF-8"));
    	}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//当写了service方法时，get和post方法自动失效
		String methodName = request.getMethod();
		System.out.println("service---->"+methodName);
		if ("GET".equals(methodName)) {
			doGet(request, response);
		} else if ("POST".equals(methodName)) {
			doPost(request, response);
		}
	}
	
	/**
	 * 	查询所有的用户数据--分页
	 */
	private void queryUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//serlvet:接受前端的参数，至于参数做什么用，由service处理
		//显示数据一定要通过这个步骤
		//1.获取所有的参数并处理
		int pn = NumberUtils.parseStr(request.getParameter("pn"));
		int limit = NumberUtils.parseStr(request.getParameter("limit"));
		//2.统计当前用户表的所有数据条数
		int totalCount = userService.getTotalCount();
		//3.将所有的分页参数进行初始化
		PageManager pm = new PageManager(pn, totalCount, limit);
		//4. 查询这一页的数据
		List<User> users = userService.queryUsers(pm);
		
//		PrintWriter out = response.getWriter();
//		out.write(s);
		
		//5.将数据存储到域中
		request.setAttribute("msg", request.getParameter("msg"));
		request.setAttribute("users", users);//查询数据
		request.setAttribute("pm", pm);//所有的分页参数都在PageManager对象中
		//转发到首页
		//直接刷新页面的话，就没有必要用ajax
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	
	
	/**
	 * 	查询所有的用户数据--分页
	 */
	private void ajaxShowUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//serlvet:接受前端的参数，至于参数做什么用，由service处理
		//显示数据一定要通过这个步骤
		//1.获取所有的参数并处理
		int pn = NumberUtils.parseStr(request.getParameter("pn"));
		int limit = NumberUtils.parseStr(request.getParameter("limit"));
		//2.统计当前用户表的所有数据条数
		int totalCount = userService.getTotalCount();
		//3.将所有的分页参数进行初始化
		PageManager pm = new PageManager(pn, totalCount, limit);
		//4. 查询这一页的数据
		List<User> users = userService.queryUsers(pm);
		
//		PrintWriter out = response.getWriter();
//		out.write(s);
		
		//使用ajax传递数据，不适用页面转发
		
		PrintWriter out = response.getWriter();
		//直接将整个用户的数据变为json字符串，传递给前端
		out.print(JSONObject.toJSONString(users));
		
		out.flush();
		out.close();
		
		//5.将数据存储到域中
//		request.setAttribute("msg", request.getParameter("msg"));
//		request.setAttribute("users", users);//查询数据
//		request.setAttribute("pm", pm);//所有的分页参数都在PageManager对象中
//		//转发到首页
//		//直接刷新页面的话，就没有必要用ajax
//		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
