package com.renby.tool.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * �д���ӿ�
 * @author renbaoyu
 *
 */
public interface ILineProcessor {
	public Logger logger = LogManager.getLogger();

	/**
	 * ���������ַ����е�����
	 * 
	 * @param line
	 *            ���������
	 * @return
	 */
	public String process(String line);
}
