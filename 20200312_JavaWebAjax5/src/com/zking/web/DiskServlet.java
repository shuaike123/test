package com.zking.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zking.utils.DiskUtils;
import com.zking.utils.ServletUtils;

@WebServlet("/searchDisk")
public class DiskServlet extends HttpServlet{
	private static final long serialVersionUID = -3621773085665401977L;
	
	private JSONObject jsonObject = new JSONObject();
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if ("queryFiles".equals(method)) {
			String path = request.getParameter("disk");
			File file = new File(path);
			jsonObject.clear();
			if (file != null && file.exists()) {
				jsonObject.fluentPut("code", 1);
				Map<String, Long> map = new HashMap<String, Long>();
				//将搜索到的所有数据存储到map集合中
				DiskUtils.getDocSize(file, map);
				//对这个map集合进行排序
				List<Entry<String, Long>> list = DiskUtils.mapSort(map);
				//返回到前端
				jsonObject.fluentPut("list", list);
			} else {
				jsonObject.fluentPut("code", -1);
			}
			//System.out.println(jsonObject);
			//将数据发送给前端
			ServletUtils.send(response, jsonObject);
		}
		
	}
	
}
