package com.renby.tool;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * ������
 * @author renbaoyu
 *
 */
public class ToolUtils {
	/**
	 * �ж�ֵ�Ƿ�Ϊ��ֵ
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(String val){
		return val == null || val.isEmpty();
	}
	
	/**
	 * ��Map�л�ȡֵ
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
	 * ���طǿյ�ֵ��ֵΪ���򷵻�Ĭ��ֵ
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static String getNonEmptyValue(String value, String defaultValue){
		return isEmpty(value) ? defaultValue : value;
	}

	/**
	 * ���طǿյ�ֵ��ֵΪ���򷵻�Ĭ��ֵ
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static boolean getNonEmptyValue(String value, boolean defaultValue){
		return isEmpty(value) ? defaultValue : Boolean.valueOf(value);
	}
	
	/**
	 * ��ȡ�û�ִ��Ŀ¼���ļ��ľ���·��
	 * @param filename
	 * @return
	 */
	public static String getAbstractUserFilePath(String filename){
		return System.getProperty("user.dir") + File.separator + filename;
	}
	
	/**
	 * ��ȡclasspath���ļ��ľ���·��
	 * @param filename
	 * @return
	 */
	public static String getAbstractClassPathFilePath(String filename){
		try {
			String parent = Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
			return URLDecoder.decode(parent,"UTF-8") + filename;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("ϵͳ��֧��UTF-8�ַ���-_-");
		}
	}
}
