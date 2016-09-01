package com.renby.tool;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class ToolUtils {
	public static boolean isEmpty(String val){
		return val == null || val.isEmpty();
	}
	
	public static String getString(Map<String, String> map, String key, String defaultVal){
		if(map == null || map.isEmpty() || isEmpty(key)){
			return defaultVal;
		}
		String value = map.get(key);
		return isEmpty(value) ? defaultVal : value;
	}
	
	public static String getNewValue(String newVal, String oldVal){
		return isEmpty(newVal) ? oldVal : newVal;
	}

	public static boolean getNewValue(String newVal, boolean oldVal){
		return isEmpty(newVal) ? oldVal : Boolean.valueOf(newVal);
	}
	
	public static String getAbstractUserFilePath(String filename){
		return System.getProperty("user.dir") + File.separator + filename;
	}
	
	public static String getAbstractClassPathFilePath(String filename){
		try {
			String parent = Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
			return URLDecoder.decode(parent,"UTF-8") + filename;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("系统不支持UTF-8字符集-_-");
		}
	}
}
