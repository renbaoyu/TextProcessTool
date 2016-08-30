package com.renby.tool.processor.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtils {
	public static final String SEPERATE = ",";
	/** NULLƥ��������ʽ */
	public static final Pattern REGEX_NULL = Pattern.compile("^ *(null) *", Pattern.CASE_INSENSITIVE);
	/** ��ֵƥ��������ʽ */
	public static final Pattern REGEX_NUMBER = Pattern.compile("^ *([+-]{0,1}[0-9]+\\.{0,1}[0-9]+) *");
	/**
	 * ��sql��ֵ���ֽ���Ϊ����<br>
	 * ����'val1','val2','val3'
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
				//���NULL
				Matcher matcher = REGEX_NULL.matcher(valueString.substring(i));
				if(matcher.find()){
					valsList.add(matcher.group(1).trim());
					i += matcher.group(0).length();
					continue;
				}
				//�����ֵ
				matcher = REGEX_NUMBER.matcher(valueString.substring(i));
				if(matcher.find()){
					valsList.add(matcher.group(1).trim());
					i += matcher.group(0).length();
					continue;
				}
			}
			c = valueString.charAt(i);
			// �ַ���ֵ��ʼ�������ı߽���
			if (isStringBoundary(valueString, i, startIndex)) {
				isStringContent = !isStringContent;
				if (isStringContent) {
					startIndex = i;
					// ������һ��ֵ����ʼ��¼��ֵ
					if (value.length() > 0) {
						valsList.add(value.toString());
					}
					value.setLength(0);
				} else {
					startIndex = -1;
					// ֵ����ʱ��������ĵ�����
					value.append(c);
				}
			}
			if (isStringContent) {
				value.append(c);
			}
		}
		// ���һ���ַ�Ϊ������־ʱ��value������ӵ�valsList����Ҫ�������
		if (value.length() > 0) {
			valsList.add(value.toString());
		}
		return valsList.toArray(new String[0]);
	}

	/**
	 * �ж��ַ���ֵ����ʼ�����ű��
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
			// ����������Ϊ��ʼ���������
			if (quotesStartIndex == quotesEndIndex) {// '
				return true;
			} else if (startIndex == -1 && (quotesEndIndex - quotesStartIndex) % 2 == 0) {// '*
				return true;
			} else if (startIndex != -1) {
				if ((quotesEndIndex - quotesStartIndex) % 2 == 0 && startIndex < quotesStartIndex
						&& index == quotesEndIndex) {// ������'����ʱ�ж�
					return true;
				} else if ((quotesEndIndex - quotesStartIndex) % 2 != 0 && startIndex == quotesStartIndex
						&& index == quotesEndIndex) {// ������'��ʼʱ�ж�
					return true;
				}
			}
		}
		return false;
	}
}
