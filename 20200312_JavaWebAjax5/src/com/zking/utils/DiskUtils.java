package com.zking.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DiskUtils {

	static DecimalFormat df = new DecimalFormat("#.###");
	
	/**
	 * 	获取搜索盘符下文件夹所占大小
	 * @param file 需要搜索的路径file对象
	 */
	public static void getDocSize(File file, Map<String, Long> map) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				//将这个文件夹的路径加入进去，并且把这个文件夹的大小加入进去
				addMap(file.getPath(), file.length(), map);
				//是文件夹
				File[] files = file.listFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						//再去判断这个文件夹或者文件
						getDocSize(files[i], map);
					}
				} 
			} else {
				addMap(file.getParent(), file.length(), map);
			}
		}
	}


	/**
	 * 	将搜索到的数据增加到Map集合中
	 * @param path 文件所在的路径
	 * @param fileSize 文件的大小
	 */
	public static void addMap(String path, long fileSize, Map<String, Long> map) {
		
		//没有这个key，设置一个0
		if (!map.containsKey(path)) 
			map.put(path, new Long(0));
		
		for (Entry<String, Long> entry : map.entrySet()) {
			if (path.startsWith(entry.getKey())) {
				//找到这个文件夹的下一级，将下一级的大小增加到文件夹中
				map.put(entry.getKey(), entry.getValue() + fileSize);
			}
		}
	}
	
	/**
	 * 	对结果进行排序
	 * @param map
	 * @return
	 */
	public static List<Entry<String, Long>> mapSort(Map<String, Long> map) {
		List<Map.Entry<String, Long>> list = new ArrayList<Map.Entry<String,Long>>(map.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<String, Long>>() {
			public int compare(Entry<String, Long> o1,
					Entry<String, Long> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		return list;
	}
	
	/**
	 * 	单位换算
	 * @param size
	 * @return
	 */
	public static String parseUnit(long size) {
		double dSize = size;
		if (dSize < 1024) {
			return dSize + "B";
		} else if ((dSize /= 1024) < 1024) {
			return df.format(dSize) + "KB";
		} else if ((dSize /= 1024) < 1024) {
			return df.format(dSize) + "MB";
		} else if ((dSize /= 1024) < 1024) {
			return df.format(dSize) + "GB";
		}  else if ((dSize /= 1024) < 1024) {
			return df.format(dSize) + "TB";
		} 
		return null;
	}
	
	
	/**
	 * 	统计所有文件的数量
	 * @param file
	 * @param nums
	 */
	public static void docsCount(File file, int[] nums) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						docsCount(files[i], nums);
					}
				}
			} else {
				nums[0]++;
			}
		}
	}
	
	
	public static void main(String[] args) {
		
		Map<String, Long> map = new HashMap<String, Long>();
		
		File file = new File("F:\\");
		int[] sum = new int[1];
		docsCount(file, sum);
		System.out.println(sum[0]);
		DiskUtils.getDocSize(file, map);
		
		//大小排序
		List<Entry<String, Long>> list = mapSort(map);
		
		//打印输出结果
		for(Map.Entry<String,Long> mapping:list){ 
			System.out.println(mapping.getKey() + "----->" + parseUnit(mapping.getValue()));
		}
		
	}
}
