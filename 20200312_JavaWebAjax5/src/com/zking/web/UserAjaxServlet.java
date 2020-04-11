package com.zking.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONObject;
import com.zking.dao.DBPool;
import com.zking.entity.PageManager;
import com.zking.entity.ProductPics;
import com.zking.entity.User;
import com.zking.service.UserService;
import com.zking.utils.NumberUtils;
import com.zking.utils.ServletUtils;

@WebServlet("/userAjax")
@MultipartConfig
public class UserAjaxServlet extends HttpServlet{
	//子类是父类的扩展
	
		private static final long serialVersionUID = 1L;
		
		//创建一个UserService对象
		private UserService userService = new UserService();
		
		//后端返回给前端的json对象
		//servlet是一个单例模式，写在最上面就只会创建一个对象。
		private JSONObject jsonObject = new JSONObject();;
		
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			} else if ("getIds".equals(method)) {
				List<Object[]> ids = userService.getIds();
				jsonObject.clear();
				jsonObject.fluentPut("ids", ids);
				ServletUtils.send(response, jsonObject);
			}else if("exportExcel".equals(method)) {
				try {
					exportExcel(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			} else if ("deleteAll".equals(method)) {
				deleteAll(request, response);
			}
			else if("importExcel".equals(method)) {
				importExcel(request, response);
			}
		}
		private void importExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
			Collection<Part> parts = request.getParts();
			System.out.println("nihoa");
			for (Part part : parts) {
				//提交的文件名称
				String filename = part.getSubmittedFileName();
				if (part.getContentType() != null 
						&& filename != null
						&& !"".equals(filename)) {
					
					
					String sql="insert into lpm_user values (NULL,?,?)";
					String sql2="select count(*) from information_schema. COLUMNS where table_name= 'lpm_user' ";
					QueryRunner runner =new QueryRunner(DBPool.getInstance().getDataSource());
					try {
						
						List <User> list=ServletUtils.importObject(User.class,part.getInputStream());
						Object params[] [] = new Object [list.size()][Integer.parseInt(runner.query(sql2, new ScalarHandler<Object>()).toString())-1];
						System.out.println(list.size());
						System.out.println(runner.query(sql2, new ScalarHandler<Object>()));
						for (int i = 0; i < params.length; i++) {
							params[i][0]=list.get(i).getUsername();
							params[i][1]=list.get(i).getPassword();
							
						}
						
							int i[]=runner.batch(sql, params);
							if(i!=null) {
								
								jsonObject.clear();
								jsonObject.fluentPut("code", 1);
								ServletUtils.send(response, jsonObject);
							}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
					
			
			}
			
		}
		
		/**
		 * 批量删除
		 */
		private void deleteAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
			String[] ids = request.getParameterValues("ids[]");
			int i = userService.deleteAll(ids);
	    	jsonObject.clear();
	    	jsonObject.fluentPut("code", i > 0?1:-1);
	    	ServletUtils.send(response, jsonObject);
		}
		/**
		 * 导出数据
		 * @param request
		 * @param response
		 * @throws Exception 
		 */
		private void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
			// TODO Auto-generated method stub
			
			List<User> users=userService.allUsers();
			String path = "D:\\Java课程代码\\test";
			String filename="User.xlsx";
			ServletUtils.exportObject(users, User.class, path,filename);
			File file=new File(path+"\\"+filename);
			if(file!=null&&file.exists()) {
				response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("ISO-8859-1")));
				InputStream in =new BufferedInputStream(new FileInputStream(file));
				OutputStream out=new BufferedOutputStream(response.getOutputStream());
				int len=0;
				byte[] b=new byte[1024];
				
				while((len=in.read(b))!=-1) {
					out.write(b,0,len);
				}
				out.flush();
				out.close();
				in.close();
				response.flushBuffer();
			}
			
		
		}
		/**
		 * 	修改用户数据
		 */
		private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//			System.out.println(ServletUtils.parseParameter(User.class, request));
			//1.获取三个参数
			User user = ServletUtils.parseParameter(User.class, request);
		   	//2.将这个数据增加到数据库
	    	int i = userService.updateUser(user);
	    	jsonObject.clear();
	    	jsonObject.fluentPut("code", i > 0?1:-1);
	    	ServletUtils.send(response, jsonObject);
		}

		/**
		 * 	查询单个用户信息
		 */
		private void queryUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			User user = ServletUtils.parseParameter(User.class, request);
			System.out.println(user);
			User queryUser = userService.queryUser(user);
			jsonObject.clear();
			jsonObject.fluentPut("user", queryUser);
			ServletUtils.send(response, jsonObject);
		}

		/**
		 * 	删除
		 */
		private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			//1.获取参数
			User user = ServletUtils.parseParameter(User.class, request);
			//2.在service中调用数据库
	    	int i = userService.deleteUser(user);
	    	jsonObject.clear();
			jsonObject.fluentPut("code", i>0?1:-1);
	    	ServletUtils.send(response, jsonObject);
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
	    	System.out.println("queryUser="+queryUser);
	    	
	    	//返回给前端一个登陆的状态。（成功，失败[用户名或者密码错误]）
	    	jsonObject.clear();//清空整个jsonObject对象的内容
			if(queryUser == null){
				//用户名或者密码错误
				jsonObject.fluentPut("code", "-1");//-1代表用户名或者密码错误
			} else {
				//将该登陆的用户存储到session中
				session.setAttribute("user", user);
				//设置用户的活动时长为30秒
				//session.setMaxInactiveInterval(30);
				jsonObject.fluentPut("code", "1");//成功
			}
			//通过out对象将数据返回给Ajax
			ServletUtils.send(response, jsonObject);
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
	    	//清空json
	    	jsonObject.clear();
	    	if(checkUser == null){
	    		//3.再service增加数据
	    		//证明该用户名未被注册，直接注册
	        	int i = userService.addUser(user);
	        	jsonObject.fluentPut("code", i>0?1:-2);//数据增加错误
	    	} else {
	    		jsonObject.fluentPut("code", -1);//用户名已存在
	    	}
	    	//往前端传递json数据
	    	ServletUtils.send(response, jsonObject);
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
			System.out.println(request.getParameter("method"));
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
			
			PrintWriter out = response.getWriter();
			jsonObject.clear();
			//加载users
			jsonObject.fluentPut("users", users);
			jsonObject.fluentPut("pm", pm);
			out.print(jsonObject.toString());
			out.flush();
			out.close();
		}
		
}
