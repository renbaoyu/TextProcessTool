package com.renby.tool.processor;

import java.util.Map;
import java.util.Map.Entry;

import com.renby.tool.ToolUtils;
import com.renby.tool.file.FileHandler;

/**
 * ʵ�ּ򵥵�EL���ʽ���������滻Ϊʵ��ֵ
 * 
 * @author renbaoyu
 *
 */
public class AttributeReplaceProcessor implements ILineProcessor {
	/** EL���ʽǰ׺ */
	public static final String PROPERTY_PREFIX = "\\$\\{";
	/** EL���ʽ��׺ */
	public static final String PROPERTY_SUFFIX = "\\}";
	/** ������ʽ�е������ַ� */
	public static final String REGULAR_EXPRESSION_SPRCIFIC_SIGN = "^$.*+-?=!:|\\()[]{}";
	/** ���Լ��� */
	Map<String, String> configMap = null;

	public AttributeReplaceProcessor(Map<String, String> configMap) {
		this.configMap = configMap;
	}

	@Override
	public String process(String line) {
		if (line == null) {
			return line;
		}
		String newLine = line;
		for (Entry<String, String> entry : configMap.entrySet()) {
			newLine = newLine.replaceAll(PROPERTY_PREFIX + specifiedSignToValue(entry.getKey()) + PROPERTY_SUFFIX,
					entry.getValue());
		}
		logger.debug("�Ѵ����У�����ǰ��{}", line);
		logger.debug("�Ѵ����У������{}", newLine);
		return newLine + FileHandler.LINE_END;
	}

	private String specifiedSignToValue(String str) {
		if (ToolUtils.isEmpty(str)) {
			return str;
		}

		StringBuilder newStr = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (REGULAR_EXPRESSION_SPRCIFIC_SIGN.indexOf(str.charAt(i)) >= 0) {
				if (i == 0 || str.charAt(i - 1) != '\\') {
					newStr.append('\\');
				}
			}
			newStr.append(str.charAt(i));
		}
		return newStr.toString();
	}
}
