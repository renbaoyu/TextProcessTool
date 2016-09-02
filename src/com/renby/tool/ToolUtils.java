package com.renby.tool;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 工具类
 * @author renbaoyu
 *
 */
public class ToolUtils {
	/**
	 * 判断值是否为空值
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(String val){
		return val == null || val.isEmpty();
	}
	
	/**
	 * 从Map中获取值
	 * @param map
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public static String getString(Map<String, String> map, String key, String defaultVal){
		if(map == null || map.isEmpty() || isEmpty(key)){
			return defaultVal;
		}
		String value = map.get(key);
		return isEmpty(value) ? defaultVal : value;
	}
	
	/**
	 * 返回非空的值，值为空则返回默认值
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static String getNonEmptyValue(String value, String defaultValue){
		return isEmpty(value) ? defaultValue : value;
	}

	/**
	 * 返回非空的值，值为空则返回默认值
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static boolean getNonEmptyValue(String value, boolean defaultValue){
		return isEmpty(value) ? defaultValue : Boolean.valueOf(value);
	}
	
	/**
	 * 获取用户执行目录下文件的绝对路径
	 * @param filename
	 * @return
	 */
	public static String getAbstractUserFilePath(String filename){
		return System.getProperty("user.dir") + File.separator + filename;
	}
	
	/**
	 * 获取classpath下文件的绝对路径
	 * @param filename
	 * @return
	 */
	public static String getAbstractClassPathFilePath(String filename){
		try {
			String parent = Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
			return URLDecoder.decode(parent,"UTF-8") + filename;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("系统不支持UTF-8字符集-_-");
		}
	}
}
