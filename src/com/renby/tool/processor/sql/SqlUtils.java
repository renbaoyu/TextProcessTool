package com.renby.tool.processor.sql;

import java.util.ArrayList;
import java.util.List;

public class SqlUtils {
	/**
	 * ��sql��ֵ���ֽ���Ϊ����<br>
	 * ����'val1','val2','val3'
	 * @param valueString
	 * @return
	 */
	public static String[] SqlValueSeperate(String valueString){
		List<String> valsList = new ArrayList<String>();
		boolean isContent = false;
		char c;
		StringBuilder value = new StringBuilder();
		for (int i = 0, startIndex = -1; i < valueString.length(); i++) {
			c = valueString.charAt(i);
			// ֵ��ʼ�������ı߽���
			if (isSign(valueString, i, startIndex)) {
				isContent = !isContent;
				if (isContent) {
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
			if (isContent) {
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
