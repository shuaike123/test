package com.zking.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSONObject;
import com.zking.entity.PageManager;
import com.zking.entity.Product;
import com.zking.entity.ProductPics;
import com.zking.service.ProductService;
import com.zking.utils.NumberUtils;
import com.zking.utils.ServletUtils;

@WebServlet("/product")
@MultipartConfig
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 6915020476106513112L;
	
	//后端传递数据给前端
	private JSONObject jsonObject = new JSONObject();
	
	//商品的service
	private ProductService proService = new ProductService();
	
	//图片的存储路径
	private static String basePath = "Product\\Images\\";
	
	//如果路径不存在
	static {
		//找到电脑上所有的盘符
		//默认存储到最后一个盘
		File[] roots = File.listRoots();
		//拼接最后一个盘符
		basePath = roots[roots.length - 1] + basePath;
		basePath = basePath.replaceAll("\\\\", "/");
		File file = new File(basePath);
		if (file == null || !file.exists()) {
			//创建文件夹
			file.mkdirs();
		}
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//史诗之上的装备---神话---紫金色。满满的手游气息。
		String method = request.getParameter("method");
		if ("addProduct".equals(method)) {
			//增加商品信息
			addProduct(request, response);
		} else if ("showProducts".equals(method)) {
			showProducts(request, response);
		} else if ("showImage".equals(method)) {
			showImage(request, response);
		}else if("deleteProduct".equals(method)) {
			deleteProduct(request,response);
		}
		else if("updateProduct".equals(method)) {
			updateProduct(request,response);
		}
		else if("queryProduct".equals(method)) {
			try {
				queryProduct(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	private void queryProduct(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		Product product=ServletUtils.parseParameter(Product.class, request);
		proService.queryProduct(product);
		jsonObject.clear();
		
		jsonObject.fluentPut("product", product);
		ServletUtils.send(response, jsonObject);
		
		
		
	}


	private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Product product=ServletUtils.parseParameter(Product.class, request);
		Collection<Part> parts=request.getParts();
		jsonObject.clear();
		List<ProductPics> pics =new ArrayList<ProductPics>();
		for (Part part : parts) {
			String filename=part.getSubmittedFileName();
			if(part.getContentType()!=null&&filename!=null&&!"".equals(filename)) {
				filename=Long.toString(System.currentTimeMillis())+filename.substring(filename.lastIndexOf("."));
				
				ProductPics pic =new ProductPics();
				pic.setPic_name(filename);
				pic.setPic_address(basePath+filename);
				pics.add(pic);
				part.write(basePath+filename);
				
			}
			
		}
		if(pics.size()>0) {
			product.setPro_coverPic(pics.get(0).getPic_address());
		}
		try {
			int i[]=proService.updatePics(product);
			jsonObject.fluentPut("addCount", i.length);
			ServletUtils.send(response, jsonObject);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Product pro=ServletUtils.parseParameter(Product.class, request);
		int i=proService.deleteProduct(pro);
		jsonObject.clear();
		jsonObject.fluentPut("code", i>0?1:-1);
    	ServletUtils.send(response, jsonObject);
		
		
	}


	//直接读取本地文件的流，显示图片
	private void showImage(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		//上传下载excel解析
		//传递一个图片真实的地址
		String path = request.getParameter("path");
		//转化为字节输入流
		File file = new File(path);
		if (file != null && file.exists()) {
			InputStream in = new FileInputStream(file);
			//获取response相应的输出流
			//一个请求request--->相应response
			OutputStream out = response.getOutputStream();
			//将这个图片的字节直接写到前端
			int len = 0;
			byte[] b = new byte[1024];
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
			//关闭资源
			out.flush();
			out.close();
			in.close();
			//刷新一下响应的缓存
			response.flushBuffer();
		}
		
	}


	//显示所有商品信息
	private void showProducts(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		int pn = NumberUtils.parseStr(request.getParameter("pn"));
		int limit = NumberUtils.parseStr(request.getParameter("limit"));
		//2.统计当前用户表的所有数据条数
		int totalCount = proService.getTotalCount();
		//3.将所有的分页参数进行初始化
		PageManager pm = new PageManager(pn, totalCount, limit);
		//4. 查询这一页的数据。只要跟商品相关都写在一起
		List<Product> products = proService.queryProducts(pm);
		jsonObject.clear();
		jsonObject.fluentPut("products", products);//商品信息
		jsonObject.fluentPut("pm", pm);//分页信息
		
		ServletUtils.send(response, jsonObject);
	}


	/**
	 * 	增加商品信息
	 */
	private void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//其他参数直接转成对象
		Product product = ServletUtils.parseParameter(Product.class, request);
		
		jsonObject.clear();
		//先存储商品，再查询商品
		//获取所有的文件
		Collection<Part> parts = request.getParts();
		//创建一个存储对象的list集合
		List<ProductPics> pics = new ArrayList<ProductPics>();
		for (Part part : parts) {
			//提交的文件名称
			String filename = part.getSubmittedFileName();
			if (part.getContentType() != null 
					&& filename != null
					&& !"".equals(filename)) {
				
				//自己重新取个名字。客户上传的图片名称很可能会有重复的。
				filename = Long.toString(System.currentTimeMillis()) 
						+ filename.substring(filename.lastIndexOf("."));//以当前时间的毫秒数当作文件名
				//存储文件名
				ProductPics pic = new ProductPics();
				//获取图片的名称
				pic.setPic_name(filename);
				//直接将图片存储到服务器本地最后一个磁盘
				pic.setPic_address(basePath+filename);
				//将图片存储到服务器的项目里面
				
				//String path = ProductServlet.class.getResource("/").getPath();
				//最前面还有个/
				//pic.setPic_address("upload/"+filename);
				
				//当项目部署到服务器上之后，本地的图片路径是不能显示的，只能显示项目中的相对路径
				
				//将这个文件对象加入到集合中
				pics.add(pic);
				//将这个文件存储到对应的路径下
				part.write(basePath+filename);
				
				
			}
		}
		if (pics.size() > 0) {
			//取封面
			product.setPro_coverPic(pics.get(0).getPic_address());
		}
		
		//将商品数据保存到数据库中
		//无法解决超高并发性问题，除非超高并发，才会有问题。
		//方式一：使用last_insert_id()函数来获取最后一个自增主键
//		int i = proService.addProduct(product);
//		//增加完之后，才知道了商品的id。如何取到刚刚存储的商品id是多少。
//		Long id = proService.getProductId();
//		System.out.println("i="+i+",id="+id);
		
		//方式二：通过jdk提供的底层代码来返回自增的主键值
		Long id = proService.getProductIdJDBC(product);
		
		//如果返回了主键，证明增加成功，否则增加失败，就不增加图片了
		if (id > 0) {
			//循环将图片存储第二个表
			int[] is = proService.addPics(id, pics);
			jsonObject.fluentPut("addCount", is.length);
			jsonObject.fluentPut("code", 1);
		} else {
			jsonObject.fluentPut("code", -1);
		}
		
		//返回结果
		ServletUtils.send(response, jsonObject);
		
		//绝对错误的
		//这个是源代码的位置，我们放到服务器上的是编译之后的代码
		//E:\Workspaces\MyEclipse20190407\20200312_JavaWebAjax\WebRoot\\upload
		//动态的获取WebRoot的位置
		//E:\Workspaces\MyEclipse20190407\.metadata\.me_tcat85\webapps\20200312_JavaWebAjax\\upload
		//使用类的反射动态获取项目前面的路径
		//String path = ProductServlet.class.getResource("/").getPath();
		//E:\Workspaces\MyEclipse20190407\.metadata\.me_tcat85\webapps\20200312_JavaWebAjax
		//path = path.substring(0, path.lastIndexOf("WEB-INF/")) + "/upload";
		//如果存在服务器上面就不用存地址，直接使用相对路径就能访问
		//本地必须存地址
		//System.out.println("path="+path);
		//这样存储之后，就可以直接在页面显示
		//直接使用
		//直接写
		
		//拿到本地源文件的流，然后直接response.getOutputStream()写
		//OutputStream out = response.getOutputStream();
		
		//作业：接着写完增删改查
		
	}
	

}
