package com.renby.tool.processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.renby.tool.ToolUtils;
import com.renby.tool.file.FileHandler;
import com.renby.tool.processor.sql.SqlUtils;

/**
 * ʵ��SQL�������������滻ָ���ֶε�ֵ����֧��ע�ͣ�
 * 
 * @author Administrator
 *
 */
public class SqlColumnValueReplaceProcessor implements ILineProcessor {
	private static Map<String, Map<String, String>> configs = new HashMap<String, Map<String, String>>();
	/** �������ƥ��������ʽ */
	public static String REGEX_INSERT_STR = "^ *(insert) +into +([0-9a-zA-Z_$#]+) *\\((.+)\\) *values *\\((.+)\\) *;";
	/** �������ƥ���� */
	public static Pattern REGEX_INSERT = Pattern.compile(REGEX_INSERT_STR, Pattern.CASE_INSENSITIVE);
	/** �ֶλ���ֵ֮��ļ������ */
	public static String SEPARATOR = ",";
	/** ���SQL�ļ��Ƿ��д */
	public static final String KEY_SQL_ISUPPERCASE = "sql.isUppercase";
	/** �����/СдSQL��־ */
	private boolean isUppercase = false;

	public SqlColumnValueReplaceProcessor(Map<String, String> configMap) {
		initColumnConfig(configMap);
	}

	public SqlColumnValueReplaceProcessor(Map<String, String> configMap, boolean isUppercase) {
		this.isUppercase = isUppercase;
		initColumnConfig(configMap);
	}

	@Override
	public String process(String sql) {
		if (sql == null) {
			return sql + FileHandler.LINE_END;
		}
		SqlInfo info = getSqlInfo(sql);
		if (info == null) {
			logger.debug("ֱ�������{}", sql);
			return sql + FileHandler.LINE_END;
		}
		Map<String, String> config = configs.get(info.talbe);
		if (config == null) {
			return sql + FileHandler.LINE_END;
		}
		String newVal;
		for (int i = 0; i < info.cols.length; i++) {
			newVal = config.get(info.cols[i].trim().toLowerCase());
			if (newVal != null) {
				info.vals[i] = "'" + newVal + "'";
			}
		}

		String newSql = buildInsertSql(info);
		logger.debug("�Ѵ����У�����ǰ��{}", sql);
		logger.debug("�Ѵ����У������{}", newSql);
		return newSql + FileHandler.LINE_END;
	}

	/**
	 * ����SQL��䣬���SQL�ṹ
	 * 
	 * @param sql
	 * @return
	 */
	private SqlInfo getSqlInfo(String sql) {
		Matcher matcher = REGEX_INSERT.matcher(sql);
		if (!matcher.find()) {
			logger.error("����SQL�Ƿ���{}", sql);
			return null;
		} else {
			SqlInfo info = new SqlInfo();
			info.opetation = matcher.group(1);
			info.talbe = matcher.group(2);
			info.cols = matcher.group(3).split(",");
			info.vals = SqlUtils.SqlValueSeperate(matcher.group(4));
			if (info.cols.length != info.vals.length) {
				logger.error("SQL���������ֶ�������ֵ������һ��.");
				logger.error("�쳣SQL:{}", sql);
				logger.error("�쳣�ֶ�:{}", Arrays.toString(info.cols));
				logger.error("�쳣ֵ:{}", Arrays.toString(info.vals));
				info = null;

			}
			return info;
		}
	}

	/**
	 * ����������
	 * 
	 * @param info
	 * @return
	 */
	private String buildInsertSql(SqlInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append(info.opetation).append(" into ");
		sql.append(info.talbe);
		sql.append(" (");
		for (String col : info.cols) {
			sql.append(col.trim()).append(SEPARATOR);
		}
		sql.setLength(sql.length() - SEPARATOR.length());
		sql.append(") values (");
		String changed;
		if (isUppercase) {
			changed = sql.toString().toUpperCase();
		} else {
			changed = sql.toString().toLowerCase();
		}
		sql.setLength(0);
		sql.append(changed);

		for (String val : info.vals) {
			sql.append(val).append(SEPARATOR);
		}
		sql.setLength(sql.length() - SEPARATOR.length());
		sql.append(");");
		return sql.toString();
	}

	/**
	 * �������ã��Ա������飬�����ֶ������ֶ�ֵ����
	 * 
	 * @param configMap
	 */
	private void initColumnConfig(Map<String, String> configMap) {
		String[] keys;
		Map<String, String> columnConfig;
		for (Entry<String, String> entry : configMap.entrySet()) {
			keys = entry.getKey().split("\\.");
			if (keys.length == 2) {
				columnConfig = configs.get(keys[0]);
				if (columnConfig == null) {
					columnConfig = new HashMap<String, String>();
					configs.put(keys[0], columnConfig);
				}
				columnConfig.put(keys[1].toLowerCase(), entry.getValue());
				logger.debug("�Ѷ�ȡ�ֶ����ã�{}.{}:{}", keys[0], keys[1], entry.getValue());
			} else {
				logger.error("�������쳣���ã�{}={}", entry.getKey(), entry.getValue());
			}
		}
		isUppercase = ToolUtils.getNewValue(System.getProperty(KEY_SQL_ISUPPERCASE), isUppercase);
	}

	/**
	 * SQL���ṹ��
	 * 
	 * @author Administrator
	 *
	 */
	private class SqlInfo {
		public String opetation;
		public String talbe;
		public String[] cols;
		public String[] vals;
	}

}
