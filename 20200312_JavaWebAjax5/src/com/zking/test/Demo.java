package com.zking.test;

import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

public class Demo {
	
	/**
	 * 	获取上一个key
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	public static JSONObject getLast(JSONObject jsonObject, String key) {
		if (jsonObject.get(key) instanceof JSONObject 
				&& jsonObject.containsKey(key)) {
			return jsonObject.getJSONObject(key);
		}
		Set<Entry<String, Object>> entries = jsonObject.entrySet();
		for (Entry<String, Object> entry : entries) {
			System.out.println(entry.getValue().getClass());
			if (entry.getValue() instanceof JSONObject) {
				JSONObject obj = (JSONObject) entry.getValue();
				return getLast(obj, key);
			} 
		}
		return null;
	}
	
	public static void main(String[] args) {
		//{"orgId":"1","groupId":"2","devId":"3","business.acctNo":"4","business.paymentNo":"5","business.paymentAmount":"6"}
		String str = "{\"orgId\":\"1\",\"groupId\":\"2\",\"devId\":\"3\","
				+ "\"business.acctNo\":\"4\",\"business.paymentNo.three\":\"5\","
				+ "\"business.paymentAmount\":\"6\",\"business.acctNo.one\":\"4\",\"business.paymentNo.two\":\"5\"}";
//		{"business.acctNo":"4","business.paymentNo":"5"}转换成,{"business":{"accNo":"4","paymentNo":"5"}}
//		如果后面有很多级别的话也相应转换就{"business.acctNo.one":"4","business.paymentNo.two":"5"}
//		 变为{"business":{"accNo":{"one":"4"},"paymentNo":{"two":"5"}}} 
		
		//String str2 = "{\"orgId\":\"1\",\"groupId\":\"2\",\"devId\":\"3\",\"business.acctNo\":\"4\",\"business.acctNo.one\":\"1\",\"business.acctNo.two\":\"2\",\"business.paymentNo\":\"9\",\"business.paymentNo.one\":\"7\",\"business.paymentNo.two\":\"4\",\"business.paymentNo.two.one\":\"1\",\"business.paymentNo.two.two\":\"2\",\"business.paymentAmount\":\"6\"}";
		JSONObject jsonObject = JSONObject.parseObject(str);
		JSONObject newJsonObject = new JSONObject();
		Set<Entry<String, Object>> entries = jsonObject.entrySet();
		
		List<String> keyList = new ArrayList<String>();
		
		for (Entry<String, Object> entry : entries) {
			String key = entry.getKey();
			String value = (String) entry.getValue();
			
			if (key.contains(".")) {
				System.out.println("key...="+key+",value...="+value);
				String[] keyArr = key.split("\\.");
				//从第二个开始
				for (int i = 0; i < keyArr.length; i++) {
					//keyList集合中有这个key
					if (keyList.contains(keyArr[i])) {
						//证明组装的json中已经添加了这个key，获取这个key的值
						//JSONObject obj = newJsonObject.getJSONObject(keyArr[i]);
						//System.out.println(obj);
						if (i == keyArr.length - 1) {
							JSONObject obj = Demo.getLast(newJsonObject, keyArr[i - 1]);
							obj.fluentPut(keyArr[i], value);
						}
					} else {
						//没有这个key，获取上一个key
						JSONObject obj = null;
						if (i > 0) {
							System.out.println("newJsonObject---"+newJsonObject);
							System.out.println("-------------"+Demo.getLast(newJsonObject, keyArr[i - 1]));
							obj = Demo.getLast(newJsonObject, keyArr[i - 1]);
						} else {
							obj = Demo.getLast(newJsonObject, keyArr[i]);
						}
						if (obj == null) {
							//如果上一个key没有，那就是一级key
							newJsonObject.fluentPut(keyArr[i], new JSONObject());
						} else if (i == keyArr.length - 1) {
							//已经到了最后一个值的位置
							obj.fluentPut(keyArr[i], value);
						} else {
							//还没有到最后一个位置
							obj.fluentPut(keyArr[i], new JSONObject());
						}
						//将这个key记录在集合中
						keyList.add(keyArr[i]);
					}
				}
			} else {
				System.out.println("key="+key+",value="+value);
				newJsonObject.fluentPut(key, value);
			}
		}
		System.out.println(newJsonObject);
	}
}
