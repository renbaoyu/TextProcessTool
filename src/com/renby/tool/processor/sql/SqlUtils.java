package com.renby.tool.processor.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtils {
	/** NULL匹配器 */
	public static Pattern REGEX_NULL = Pattern.compile("^ *(null) *", Pattern.CASE_INSENSITIVE);
	/** 数值匹配器 */
	public static Pattern REGEX_NUMBER = Pattern.compile("^ *([+-]{0,1}[0-9]+\\.{0,1}[0-9]*) *", Pattern.CASE_INSENSITIVE);

	/**
	 * 将sql的值部分解析为数组<br>
	 * 例：'val1','val2','val3'
	 * 
	 * @param valueString
	 * @return
	 */
	public static String[] SqlValueSeperate(String valueString) {
		List<String> valsList = new ArrayList<String>();
		boolean isContent = false;
		char c;
		StringBuilder value = new StringBuilder();
		for (int i = 0, startIndex = -1; i < valueString.length(); i++) {
			c = valueString.charAt(i);
			if (!isContent) {
				Matcher matcher = REGEX_NULL.matcher(valueString.substring(i));
				if (matcher.find()) {
					valsList.add(matcher.group(1).trim());
					i += matcher.group(0).length();
					continue;
				}
				matcher = REGEX_NUMBER.matcher(valueString.substring(i));
				if (matcher.find()) {
					valsList.add(matcher.group(1).trim());
					i += matcher.group(0).length();
					continue;
				}
			}
			// 值开始、结束的边界检测
			if (isSign(valueString, i, startIndex)) {
				isContent = !isContent;
				if (isContent) {
					startIndex = i;
				} else {
					startIndex = -1;
					value.append(c);
					// 保存上一个值并开始记录新值
					valsList.add(value.toString());
					value.setLength(0);
				}
			}
			if (isContent) {
				value.append(c);
			}
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
	private static boolean isSign(String s, int index, int startIndex) {
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
