package com.renby.tool.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 行处理接口
 * @author renbaoyu
 *
 */
public interface ILineProcessor {
	public Logger logger = LogManager.getLogger();

	/**
	 * 处理输入字符串中的数据
	 * 
	 * @param line
	 *            处理的数据
	 * @return
	 */
	public String process(String line);
}
