package com.zking.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zking.entity.User;

public class MyJson {
	public static void main(String[] args) {
		//1.项目是单独开发的时候，前后端混合在一起，使用jsp。
		//直接使用四大作用域存储我们的数据并且在前端显示。
		//2.如果前后端分离，前端不懂Java的。
		//3.分离之后，给前端一串字符串。
		//id、username、password
		//数据库查询出来的一个对象
//		User user = new User();
//		user.setId(1);
//		user.setUsername("admin");
//		user.setPassword("123456");
//		//map集合也是一个对象
//		String str = "User [id=" + user.getId() 
//		+ ", username=" + user.getUsername() + ", password=" + user.getPassword() + "]";
//		String[] strs = str.split("\\,");
//		for (int i = 0; i < strs.length; i++) {
//			System.out.println(strs[i].trim());
//		}
		
		//基本格式
		//{}-->代表是一个json
		//{{}}-->代表一个json里面有一个json
		//数据存储结构：key-value
		//key:String
		//value:Object
		//1.将一个json的字符串进行解析
		//1.1 手动编写一个最简单的一个json字符串
		String jsonStr = "{\"id\":1,\"username\":\"admin\",\"password\":\"123456\"}";
		//User userObj = JSONObject.parseObject(jsonStr, User.class);
		
		//1.2 使用alibaba的fastjson将json字符串转化成一个json的对象
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		//1.3 从对象中取出对应的值，类似于map集合
		//前端再转化成json对象去取
		Integer id = jsonObject.getInteger("id");
		String username = jsonObject.getString("username");
		String password = jsonObject.getString("password");
		System.out.println("id="+id+",username="+username+",password="+password);
		
		//2.组装一个json数组(集合)
		//2.1 组装多个对象成为一个json的数组
		String jsonStr2 = "[{\"id\":1,\"username\":\"admin\",\"password\":\"123456\"},"
				+ "{\"id\":2,\"username\":\"qqqqq\",\"password\":\"ewwww\"}]";
		//2.2 将json字符串转化成一个json数组的对象
		JSONArray jsonArray2 = JSONObject.parseArray(jsonStr2);
		//2.3 遍历
		for (int i = 0; i < jsonArray2.size(); i++) {
			//(JSONObject)jsonArray2.get(i);
			JSONObject obj2 = jsonArray2.getJSONObject(i);
			//obj2.getInteger(key)
			System.out.println(obj2.toString());
		}
		
		//3.如何直接组装一个JSONObject对象
		//跟map集合差不多
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.fluentPut("id", 1);
		jsonObject3.fluentPut("username", 1123);
		jsonObject3.fluentPut("password", 1123);
		System.out.println(jsonObject3);
				
		//4.直接将对象组装成JSONObject对象
		User user = new User();
		user.setId(1);
		user.setUsername("admin");
		user.setPassword("123456");
		//将user直接转化，带有一个user的key
		JSONObject jsonObject4 = new JSONObject();
		jsonObject4.fluentPut("user", user);
		System.out.println("jsonObject4="+jsonObject4.toString());
		//不带key，直接就是一个json对象
		JSONObject jsonObject42 = (JSONObject) JSONObject.toJSON(user);
		System.out.println("jsonObject42="+jsonObject42.toString());
		//直接转化成
		String userStr = JSONObject.toJSONString(user);
		System.out.println("userStr="+userStr);
		
		//5.怎么将list集合转化成json
		 
		List<User> users = new ArrayList<User>();
		users.add(user);
		users.add(new User(2,"aaaa","aaaa"));
		users.add(new User(3,"bbbb","bbbb"));
		JSONObject jsonObject5 = new JSONObject();
		jsonObject5.fluentPut("users", users);
		System.out.println("jsonObject5="+jsonObject5);
		
		//随便拿一个json进行解析
		String parseStr = "{\"code\":200,\"curTime\":1583822605230,"
				+ "\"data\":[\"少年\",\"我这一生\",\"自己\",\"大碗宽面\",\"微光\","
				+ "\"抖音最火歌曲排行榜\",\"Anthem Lights\",\"莫问归期\",\"催眠音乐 深度睡眠\","
				+ "\"你是我唯一的执着\"],\"msg\":\"success\",\"profileId\":\"site\","
				+ "\"reqId\":\"9ef0cd93204b7dae2e7efbf82e8237e1\"}";
		//1.看最外面的是[]（数组）还是{}（json对象）
		JSONObject json = JSONObject.parseObject(parseStr);
		String code = json.get("code").toString();
		System.out.println("code="+code);
		//Object data = json.get("data");
		//包转化成了json的数组
		//JSONArray-->List<String>
		JSONArray dataStr = (JSONArray) json.get("data");
		System.out.println("data的类型是："+json.get("data").getClass());
		for (int i = 0; i < dataStr.size(); i++) {
			System.out.println(dataStr.get(i)+","+dataStr.get(i).getClass());
		}
		//System.out.println(data);
		
		String jsonStr3 = "{\"code\":200,\"curTime\":1583822605220,\"data\":{\"total\":\"14040\",\"artistList\":[{\"artistFans\":926458,\"albumNum\":34,\"mvNum\":447,\"pic\":\"http://img1.kuwo.cn/star/starheads/300/10/6/294045140.jpg\",\"musicNum\":1037,\"pic120\":\"http://img1.kuwo.cn/star/starheads/120/10/6/294045140.jpg\",\"isStar\":0,\"content_type\":\"0\",\"aartist\":\"Jay Chou\",\"name\":\"周杰伦\",\"pic70\":\"http://img1.kuwo.cn/star/starheads/70/10/6/294045140.jpg\",\"id\":336,\"pic300\":\"http://img1.kuwo.cn/star/starheads/300/10/6/294045140.jpg\"},{\"artistFans\":576711,\"albumNum\":54,\"mvNum\":223,\"pic\":\"http://img4.kuwo.cn/star/starheads/300/1/27/2191880246.jpg\",\"musicNum\":716,\"pic120\":\"http://img4.kuwo.cn/star/starheads/120/1/27/2191880246.jpg\",\"isStar\":0,\"content_type\":\"0\",\"aartist\":\"G.E.M.\",\"name\":\"G.E.M.邓紫棋\",\"pic70\":\"http://img4.kuwo.cn/star/starheads/70/1/27/2191880246.jpg\",\"id\":5371,\"pic300\":\"http://img4.kuwo.cn/star/starheads/300/1/27/2191880246.jpg\"},{\"artistFans\":74499,\"albumNum\":13,\"mvNum\":2,\"pic\":\"http://img1.kuwo.cn/star/starheads/300/48/54/762316714.jpg\",\"musicNum\":27,\"pic120\":\"http://img1.kuwo.cn/star/starheads/120/48/54/762316714.jpg\",\"isStar\":0,\"content_type\":\"0\",\"aartist\":\"\",\"name\":\"要不要买菜\",\"pic70\":\"http://img1.kuwo.cn/star/starheads/70/48/54/762316714.jpg\",\"id\":4182412,\"pic300\":\"http://img1.kuwo.cn/star/starheads/300/48/54/762316714.jpg\"},{\"artistFans\":77281,\"albumNum\":84,\"mvNum\":33,\"pic\":\"http://img1.kuwo.cn/star/starheads/300/13/60/2087586964.jpg\",\"musicNum\":265,\"pic120\":\"http://img1.kuwo.cn/star/starheads/120/13/60/2087586964.jpg\",\"isStar\":0,\"content_type\":\"0\",\"aartist\":\"\",\"name\":\"魏新雨\",\"pic70\":\"http://img1.kuwo.cn/star/starheads/70/13/60/2087586964.jpg\",\"id\":104764,\"pic300\":\"http://img1.kuwo.cn/star/starheads/300/13/60/2087586964.jpg\"},{\"artistFans\":11677,\"albumNum\":91,\"mvNum\":1,\"pic\":\"http://img3.kuwo.cn/star/starheads/300/93/31/1713900214.jpg\",\"musicNum\":171,\"pic120\":\"http://img3.kuwo.cn/star/starheads/120/93/31/1713900214.jpg\",\"isStar\":0,\"content_type\":\"0\",\"aartist\":\"\",\"name\":\"海伦\",\"pic70\":\"http://img3.kuwo.cn/star/starheads/70/93/31/1713900214.jpg\",\"id\":1080030,\"pic300\":\"http://img3.kuwo.cn/star/starheads/300/93/31/1713900214.jpg\"},{\"artistFans\":9731,\"albumNum\":30,\"mvNum\":4,\"pic\":\"http://img4.kuwo.cn/star/starheads/300/39/25/3353884165.jpg\",\"musicNum\":76,\"pic120\":\"http://img4.kuwo.cn/star/starheads/120/39/25/3353884165.jpg\",\"isStar\":0,\"content_type\":\"0\",\"aartist\":\"\",\"name\":\"杨小壮\",\"pic70\":\"http://img4.kuwo.cn/star/starheads/70/39/25/3353884165.jpg\",\"id\":2553219,\"pic300\":\"http://img4.kuwo.cn/star/starheads/300/39/25/3353884165.jpg\"}]},\"msg\":\"success\",\"profileId\":\"site\",\"reqId\":\"9ac96d25fed662145c3e82635d472f0c\"}";
		JSONObject json2 = JSONObject.parseObject(jsonStr3);
		JSONObject data = json2.getJSONObject("data");
		System.out.println("data="+data);
		System.out.println("total="+data.get("total"));
		JSONArray artistList = data.getJSONArray("artistList");
		System.out.println("artistList="+artistList);
		for (int i = 0; i < artistList.size(); i++) {
			JSONObject json3 = artistList.getJSONObject(i);
			System.out.println(json3);
		}
		
		
	}
}
