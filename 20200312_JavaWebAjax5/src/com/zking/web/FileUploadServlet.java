package com.zking.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(urlPatterns = {"/upload"})
@MultipartConfig//代表这个servlet可以接收文件流
public class FileUploadServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void service(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
//		String method = request.getParameter("method");
//		String username = request.getParameter("username");
		
		//一、单文件上传
//		//Servlet 3.0有这个方法
//		Part part = request.getPart("file1");
//		//name的名称
//		String name = part.getName();
//		//文件的输入流。自己存储到电脑上的哪个位置都可以。
//		InputStream inputStream = part.getInputStream();
//		//文件的大小
//		long size = part.getSize();
//		//请求头信息
//		Collection<String> headers = part.getHeaderNames();
//		//文件名
//		String filename = part.getSubmittedFileName();
//		//文件类型
//		String contentType = part.getContentType();
//		//获取文件头信息
//		String disp = part.getHeader("content-disposition");
//		System.out.println("name="+name);
//		System.out.println("inputStream="+inputStream);
//		System.out.println("size="+size);
//		System.out.println("headers="+headers);
//		System.out.println("filename="+filename);
//		System.out.println("contentType="+contentType);
//		System.out.println("disp="+disp);
//		System.out.println("method="+method);
//		System.out.println("username="+username);
//		//将文件存储到指定位置
//		part.write("d://自带"+filename);
//		//手动存储
//		int len = 0;//每次读取的真实长度
//		byte[] b = new byte[1024];
//		OutputStream out = new FileOutputStream("d://手动"+filename);
//		while ((len = inputStream.read(b)) != -1) {
//			out.write(b, 0, len);
//		}
//		out.close();
//		inputStream.close();
		
		//二、多文件上传
		//获取了所有的Part
		Collection<Part> parts = request.getParts();
		for (Part part : parts) {
			if (part.getContentType() != null) {
				//文件的input
				printMsg(part);
			} else {
				//普通input
				String value = request.getParameter(part.getName());
				System.out.println("name="+part.getName()+",value="+value);
			}
			System.out.println("-------------------");
		}
	}
	
	
	//打印每一个文件的信息
	public void printMsg(Part part) throws IOException {
		//name的名称
		String name = part.getName();
		//文件的输入流。自己存储到电脑上的哪个位置都可以。
		InputStream inputStream = part.getInputStream();
		//文件的大小
		long size = part.getSize();
		//请求头信息
		Collection<String> headers = part.getHeaderNames();
		//文件名
		String filename = part.getSubmittedFileName();
		//文件类型
		String contentType = part.getContentType();
		//获取文件头信息
		String disp = part.getHeader("content-disposition");
		System.out.println("name="+name);
		System.out.println("inputStream="+inputStream);
		System.out.println("size="+size);
		System.out.println("headers="+headers);
		System.out.println("filename="+filename);
		System.out.println("contentType="+contentType);
		System.out.println("disp="+disp);
		if (size > 9 && filename != null && !"".equals(filename)) {
			part.write("d:\\"+filename);
		}
	}
	
}
