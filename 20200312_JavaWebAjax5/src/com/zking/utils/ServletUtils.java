package com.zking.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONObject;
import com.zking.entity.User;
import com.zking.service.UserService;


public class ServletUtils {
	
	/**
	 * 	将参数自动解析到实体类中
	 */
	public static <T> T parseParameter(Class<T> clas, HttpServletRequest request) {
		//1.拿到所有的参数列表
		Map<String, String[]> params = request.getParameterMap();
		//2.通过反射获取所有的字段
		Field[] fields = clas.getDeclaredFields();
		T t = null;
		try {
			t = clas.newInstance();
			//4.拼接每个name对应的所有的值。name值是有多选项，checkbox多选
			//如果是多选，直接用,拼接起来
			StringBuilder sbdValue = new StringBuilder();
			for (int i = 0; i < fields.length; i++) {
				//清空StringBuilder类的sbdValue对象里面的值
				sbdValue.setLength(0);
				
				//5.通过字段名称获取对应的值
				String fieldName = fields[i].getName();//每个字段的名字
				String[] value = params.get(fieldName);
				if (value != null && value.length > 0) {
					for (int j = 0; j < value.length; j++) {
						sbdValue.append(value[j]);
						sbdValue.append(",");
					}
					//最后把最后的逗号给去掉
					sbdValue.setLength(sbdValue.length()-1);
				}
				//6.使用set方法设置对应的值
				String methodName = "set" + fieldName.substring(0, 1).toUpperCase() 
						+ fieldName.substring(1);
				//methodName:set的方法名
				//fields[i].getType()：每个字段的类型
				Method method = clas.getDeclaredMethod(methodName, fields[i].getType());
				//调用set方法设置值
				//sbdValue.toString():参数的值固定是字符串
				//讲字符串转化为对应的类型
				//null-->"null"(字符串)
				method.invoke(t, parseType(fields[i].getType().getSimpleName(), 
						sbdValue.length()>0?sbdValue.toString():null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//设置了所有值的对象返回出去
		return t;
	}
	
	 public static <T> void exportObject(List<T> objs, Class<T> clas, String path, String fileName) 
				throws Exception {
			//表头
	    	Field[] fields = clas.getDeclaredFields();
	    	String[] head = new String[fields.length];
	    	for (int i = 0; i < fields.length; i++) {
	    		head[i] = fields[i].getName();
	    	}
			
			//创建excel文件
			Workbook workbook = new XSSFWorkbook();
			
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			
			Sheet sheet = workbook.createSheet("商品数据");
			//创建第一行
			Row row1 = sheet.createRow(0);
			for (int i = 0; i < head.length; i++) {
				row1.createCell(i).setCellValue(head[i]);
				row1.getCell(i).setCellStyle(cellStyle);
			}
			//创建后面的行
			for (int i = 0; i < objs.size(); i++) {
				Row row = sheet.createRow(i+1);
				T t = objs.get(i);
				for (int j = 0; j < fields.length; j++) {
					//使用get方法去取
					String fieldName = fields[j].getName();
					String methodName = "get" + fieldName.substring(0, 1).toUpperCase() 
							+ fieldName.substring(1);
					Method method = clas.getDeclaredMethod(methodName);
					//执行method的get方法
					Object obj = method.invoke(t);
//					System.out.println(obj==null?null:obj.getClass().getSimpleName());
					//设置值的时候，判断
					Cell cell = row.createCell(j);
					setCell(cell, obj);
				}
				//设置样式
				for (int j = 0; j < head.length; j++) {
					row.getCell(j).setCellStyle(cellStyle);
				}
			}
			//设置所有的列都自动适应宽高
			for (int i = 0; i < head.length; i++) {
				sheet.autoSizeColumn(i);
			}
			
			setSizeColumn(sheet, head.length);
			
			//写入文件
			File file = new File(path);
			if (file == null || !file.exists())
				file.mkdirs();
			OutputStream out = new FileOutputStream(path + "\\" + fileName);
			workbook.write(out);
		}
	 public static void setSizeColumn(Sheet sheet, int size) {
	        for (int columnNum = 0; columnNum < size; columnNum++) {
	            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
	            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
	                Row currentRow;
	                //当前行未被使用过
	                if (sheet.getRow(rowNum) == null) {
	                    currentRow = sheet.createRow(rowNum);
	                } else {
	                    currentRow = sheet.getRow(rowNum);
	                }
	 
	                if (currentRow.getCell(columnNum) != null) {
	                    Cell currentCell = currentRow.getCell(columnNum);
	                    if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
	                        int length = currentCell.getStringCellValue().getBytes().length;
	                        if (columnWidth < length) {
	                            columnWidth = length;
	                        }
	                    }
	                }
	            }
	            sheet.setColumnWidth(columnNum, columnWidth * 256);
	        }
	    }
		
	    
	    public static void setCell(Cell cell, Object obj) {
	    	if (obj == null) {
	    		cell.setCellValue("");
	    		return;
			}
	    	switch (obj.getClass().getSimpleName()) {
	    	//八种数据写全
			case "Integer":
				cell.setCellValue(Integer.parseInt(obj.toString()));
				break;
			case "String":
				cell.setCellValue(obj.toString());
				break;
			case "Double":
				cell.setCellValue(Double.parseDouble(obj.toString()));
				break;
			default:
				//其他数据类型
				cell.setCellValue("");
				break;
			}
	    }
	
	/**
	 * 得到实体类中每个参数的类型和对应的字符串的值，然后将字符串转化为对应的值
	 * @param clasName 实体类对应字段的类型
	 * @param value 字段对应的字符串值
	 * @return 对应类型的Object对象
	 */
	public static Object parseType(String clasName, String value) {
		Object obj = null;
		if (value == null) 
			return null;
		switch (clasName) {
		case "int":
			//"" 或者 null
			obj = Integer.parseInt(value);//讲字符串的数字转化为int类型
			break;
		case "Integer":
			obj = Integer.valueOf(value);
			break;
		case "Byte":
			obj = Byte.valueOf(value);
			break;
		case "byte":
			obj = Byte.parseByte(value);
			break;
		case "short":
			obj = Short.parseShort(value);
			break;
		case "Short":
			obj = Short.valueOf(value);
			break;
		case "long":
			obj = Long.parseLong(value);
			break;
		case "Long":
			obj = Long.valueOf(value);
			break;
		case "float":
			obj = Float.parseFloat(value);
			break;
		case "Float":
			obj = Float.valueOf(value);
			break;
		case "double":
			obj = Double.parseDouble(value);
			break;
		case "Double":
			obj = Double.valueOf(value);
			break;
		case "boolean":
			obj = Boolean.parseBoolean(value);
			break;
		case "Boolean":
			obj = Boolean.valueOf(value);
			break;
		default : 
			//以上都不是，就是字符串
			obj = value;
			break;
		}
		return obj;
	}
	public static <T> List<T> importObject(Class<T> clas,InputStream ins) throws Exception{
		DataFormatter formatter = new DataFormatter();
		List<T> list=new ArrayList<T>();
		
				
				Field []  field=clas.getDeclaredFields();
				
				Workbook workbook =new XSSFWorkbook(ins);
				Sheet sheet =workbook.getSheetAt(0);
				for (int i = 1; i <sheet.getPhysicalNumberOfRows(); i++) {
					Row row =sheet.getRow(i);
					T t=clas.newInstance();
					for (int j = 0; j < field.length; j++) {
						String fieldName=field[j].getName();
						String methodName="set"+fieldName.substring(0, 1).toUpperCase() 
								+ fieldName.substring(1);
						Method method = clas.getDeclaredMethod(methodName, field[j].getType());
						method.invoke(t, parseType(field[j].getType().getSimpleName(), formatter.formatCellValue(row.getCell(j)).length()>0?formatter.formatCellValue(row.getCell(j)).toString():null));
					}
					list.add(t);
					
				}
				return list;

		
		
		
	
	}
	public static Object parseType2(String clasName, String value) {
		Object obj = null;
		if (value == null) 
			return null;
		switch (clasName) {
		case "int":
			//"" 或者 null
			obj = Integer.parseInt(value);//讲字符串的数字转化为int类型
			break;
		case "Integer":
			obj = Integer.valueOf(value);
			break;
		case "Byte":
			obj = Byte.valueOf(value);
			break;
		case "byte":
			obj = Byte.parseByte(value);
			break;
		case "short":
			obj = Short.parseShort(value);
			break;
		case "Short":
			obj = Short.valueOf(value);
			break;
		case "long":
			obj = Long.parseLong(value);
			break;
		case "Long":
			obj = Long.valueOf(value);
			break;
		case "float":
			obj = Float.parseFloat(value);
			break;
		case "Float":
			obj = Float.valueOf(value);
			break;
		case "double":
			obj = Double.parseDouble(value);
			break;
		case "Double":
			obj = Double.valueOf(value);
			break;
		case "boolean":
			obj = Boolean.parseBoolean(value);
			break;
		case "Boolean":
			obj = Boolean.valueOf(value);
			break;
		default : 
			//以上都不是，就是字符串
			obj = value;
			break;
		}
		return obj;
	}
	
	/**
	 * 	向前端发送消息
	 */
	public static void send(HttpServletResponse response, JSONObject jsonObject) throws IOException {
		PrintWriter out = response.getWriter();
    	out.print(jsonObject.toString());
		out.flush();
		out.close();
	}
	public static void main(String[] args) {
		UserService ser=new UserService();
		try {
			List<User> users=ser.allUsers();
			try {
				exportObject(users, User.class, "D:\\Java课程代码\\test", "User.xlsx");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
