package com.renby.tool.processor;

import java.util.Map;
import java.util.Map.Entry;

import com.renby.tool.ToolUtils;
import com.renby.tool.file.FileHandler;

/**
 * 实现简单的EL表达式，将属性替换为实际值
 * 
 * @author renbaoyu
 *
 */
public class AttributeReplaceProcessor implements ILineProcessor {
	/** EL表达式前缀 */
	public static final String PROPERTY_PREFIX = "\\$\\{";
	/** EL表达式后缀 */
	public static final String PROPERTY_SUFFIX = "\\}";
	/** 正则表达式中的特殊字符 */
	public static final String REGULAR_EXPRESSION_SPRCIFIC_SIGN = "^$.*+-?=!:|\\()[]{}";
	/** 属性集合 */
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
		logger.debug("已处理行，处理前：{}", line);
		logger.debug("已处理行，处理后：{}", newLine);
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
