package com.zking.filters;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zking.entity.User;
//urlPatterns：想过滤哪一些页面
// /*:拦截所有的请求
//@WebFilter(urlPatterns = {"/*"})
public class LoginFilter implements Filter{

	Properties p = null;
	
	String[] URIFilters = null;//需要放行页面或者请求
	String[] StyleFilters = null;//样式
	String[] DirectoryFilters = null;//文件夹
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//初始化
		System.out.println("init().....");
		//1.创建Properties对象
		p = new Properties();
		//2.读取配置文件
		InputStream inputStream = LoginFilter.class.getResourceAsStream("/URLFilters.properties");
		try {
			//3.加载配置文件
			p.load(inputStream);
			URIFilters = p.getProperty("URIFilters").split("\\,");
			StyleFilters = p.getProperty("StyleFilters").split("\\,");
			DirectoryFilters = p.getProperty("DirectoryFilters").split("\\,");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain chain)
			throws IOException, ServletException {
		//1.先将ServletRequest转化成HttpServletRequest
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpReponse = (HttpServletResponse) response;
		//获取访问请求地址
		String uri = httpRequest.getRequestURI();
		//过程
		//System.out.println("doFilter()....."+uri);
		//只有登陆的用户才可以访问首页和增加、修改页面
		//否则就只能登陆和注册
		//2.再判断用户是否登陆
		//2.1 先得到session里面的user
		User user = (User) httpRequest.getSession().getAttribute("user");
		String method = request.getParameter("method");
		if (user == null) {
			//没有登陆，我就是来登陆的
			if (!checkFilter(uri, method)) {
				//你啥也不是，只能去登陆
				httpReponse.sendRedirect("login.jsp?msg="
				+URLEncoder.encode("不要搞事，请登录！！！", "UTF-8"));
				return;
			}
		}
		
		//已登录，放行
		//拦截页面之后，要按情况放行
		chain.doFilter(request, response);
	}

	/**
	 * 	验证该请求是否被放行
	 * @param uri 请求的地址，除参数和http://localhost:8080部分
	 * @param method 请求的servlet的方法。
	 * @return 是否放行， true 是 false 否
	 */
	private boolean checkFilter(String uri, String method) {
		//拿到uri的后缀
		///20200227_Servlet过滤器/images/captcha.png
		String URIName = uri.substring(uri.lastIndexOf("/")+1);
		//System.out.println("uri="+uri);
		//System.out.println("URIName="+URIName);
		//1.先比较URIFilters
		for (int i = 0; i < URIFilters.length; i++) {
			if (URIFilters[i].equals(URIName) || URIFilters[i].equals(method)) {
				return true;
			}
		}
		//2.再比较StyleFilters
		for (int i = 0; i < StyleFilters.length; i++) {
			if (URIName.endsWith(StyleFilters[i])) {
				return true;
			}
		}
		//3.最后比较文件夹
		for (int i = 0; i < DirectoryFilters.length; i++) {
			if (uri.contains(DirectoryFilters[i])) {
				return true;
			}
		}
		//如果上面一个if都没进去，就是不符合要求的
		return false;
	}
	
	@Override
	public void destroy() {
		//消亡
		System.out.println("destroy()......");
	}
	
	
	
}
