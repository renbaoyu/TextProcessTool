package com.renby.tool.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.renby.tool.ToolUtils;

/**
 * �ṩ����
 * 
 * @author Administrator
 *
 */
public class ConfigProvider {
	/** �����ļ��ı����ʽ����KEY */
	public static final String KEY_CONFIG_ENCORDING = "config.encording";
	/** �����ļ������ʽ */
	private static String CONFIG_ENCORDING = "UTF-8";
	/** �����ļ�ע��ǰ׺ */
	public static final String NOTE_RPEFIX = "#";
	/** ϵͳ�����ļ� */
	public static final String SYSTEM_CONFIG = ToolUtils.getAbstractClassPathFilePath("SysConfig.cfg");
	private static final Logger logger = LogManager.getLogger();

	/**
	 * ��ȡָ������
	 * 
	 * @return
	 */
	public static Map<String, String> getConfigMap(String configFile) {
		List<String> lines;
		Map<String, String> configMap = new HashMap<String, String>();
		try {
			lines = Files.readAllLines(Paths.get(configFile), Charset.forName(CONFIG_ENCORDING));
		} catch (IOException e) {
			logger.error("��ȡ�����ļ�����.");
			return configMap;
		}
		int seperateIndex;
		String key;
		String value;
		logger.debug("��ʼ��[{}]�ж�ȡ����.", configFile);
		for (String line : lines) {
			if (line.startsWith(NOTE_RPEFIX)) {
				continue;
			}
			seperateIndex = line.indexOf("=");
			if (seperateIndex > 0 && seperateIndex < line.length() - 1) {
				key = line.substring(0, seperateIndex).trim();
				value = line.substring(seperateIndex + 1, line.length()).trim();
				configMap.put(key, value);
				logger.debug("�Ѷ�ȡ����{}	:{}", key, value);
			}
		}
		logger.debug("��ȡ���ý���.");
		return configMap;
	}

	/**
	 * ��ȡϵͳ���õ�ϵͳ������
	 */
	public static void initSystemProperty() {
		Map<String, String> config = getConfigMap(SYSTEM_CONFIG);
		for (Entry<String, String> entry : config.entrySet()) {
			System.setProperty(entry.getKey(), entry.getValue());
		}
		CONFIG_ENCORDING = ToolUtils.getNewValue(System.getProperty(KEY_CONFIG_ENCORDING),
				CONFIG_ENCORDING);
	}
}
