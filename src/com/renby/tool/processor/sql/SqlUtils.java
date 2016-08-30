package com.renby.tool.processor.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtils {
	public static final String SEPERATE = ",";
	/** NULL匹配正则表达式 */
	public static final Pattern REGEX_NULL = Pattern.compile("^ *(null) *", Pattern.CASE_INSENSITIVE);
	/** 数值匹配正则表达式 */
	public static final Pattern REGEX_NUMBER = Pattern.compile("^ *([+-]{0,1}[0-9]+\\.{0,1}[0-9]+) *");
	/**
	 * 将sql的值部分解析为数组<br>
	 * 例：'val1','val2','val3'
	 * @param valueString
	 * @return
	 */
	public static String[] SqlValueSeperate(String valueString){
		List<String> valsList = new ArrayList<String>();
		boolean isStringContent = false;
		char c;
		StringBuilder value = new StringBuilder();
		for (int i = 0, startIndex = -1; i < valueString.length(); i++) {
			if(!isStringContent && i != valueString.length() - 1){
				//检测NULL
				Matcher matcher = REGEX_NULL.matcher(valueString.substring(i));
				if(matcher.find()){
					valsList.add(matcher.group(1).trim());
					i += matcher.group(0).length();
					continue;
				}
				//检测数值
				matcher = REGEX_NUMBER.matcher(valueString.substring(i));
				if(matcher.find()){
					valsList.add(matcher.group(1).trim());
					i += matcher.group(0).length();
					continue;
				}
			}
			c = valueString.charAt(i);
			// 字符串值开始、结束的边界检测
			if (isStringBoundary(valueString, i, startIndex)) {
				isStringContent = !isStringContent;
				if (isStringContent) {
					startIndex = i;
					// 保存上一个值并开始记录新值
					if (value.length() > 0) {
						valsList.add(value.toString());
					}
					value.setLength(0);
				} else {
					startIndex = -1;
					// 值结束时加入结束的单引号
					value.append(c);
				}
			}
			if (isStringContent) {
				value.append(c);
			}
		}
		// 最后一个字符为结束标志时，value不会添加到valsList，需要进行添加
		if (value.length() > 0) {
			valsList.add(value.toString());
		}
		return valsList.toArray(new String[0]);
	}

	/**
	 * 判断字符是值的起始单引号标记
	 * 
	 * @param s
	 * @param index
	 * @param isContent
	 * @return
	 */
	private static boolean isStringBoundary(String s, int index, int startIndex) {
		char c = s.charAt(index);
		if (c == '\'') {
			int quotesStartIndex = 0;
			int quotesEndIndex = s.length() - 1;
			for (int i = index; i > 0; i--) {
				if (s.charAt(i) != '\'') {
					quotesStartIndex = i + 1;
					break;
				}
			}
			for (int i = index; i < s.length(); i++) {
				if (s.charAt(i) != '\'') {
					quotesEndIndex = i - 1;
					break;
				}
			}
			// 单个单引号为起始、结束标记
			if (quotesStartIndex == quotesEndIndex) {// '
				return true;
			} else if (startIndex == -1 && (quotesEndIndex - quotesStartIndex) % 2 == 0) {// '*
				return true;
			} else if (startIndex != -1) {
				if ((quotesEndIndex - quotesStartIndex) % 2 == 0 && startIndex < quotesStartIndex
						&& index == quotesEndIndex) {// 连续个'结束时判断
					return true;
				} else if ((quotesEndIndex - quotesStartIndex) % 2 != 0 && startIndex == quotesStartIndex
						&& index == quotesEndIndex) {// 连续个'开始时判断
					return true;
				}
			}
		}
		return false;
	}
}
