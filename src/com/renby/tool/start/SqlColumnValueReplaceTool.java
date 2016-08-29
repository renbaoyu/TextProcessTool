package com.renby.tool.start;

import com.renby.tool.ToolUtils;
import com.renby.tool.config.ConfigProvider;
import com.renby.tool.file.FileHandler;
import com.renby.tool.file.FileOperationSet;
import com.renby.tool.processor.ILineProcessor;
import com.renby.tool.processor.SqlColumnValueReplaceProcessor;
public class SqlColumnValueReplaceTool {
	/** 配置文件名 */
	public static final String CONFIG_FILE = "SqlConfig.cfg";
	/** SQL文件的编码格式 */
	public static final String KEY_SQL_ENCORDING = "sql.encording";
	/** SQL文件扩展名 */
	public static final String KEY_SQL_FILTER = "sql.filter";

	public static void main(String[] args) {
		ConfigProvider.initSystemProperty();
		String inputPath = ToolUtils.getAbstractUserFilePath("sql");
		String outputPath = ToolUtils.getAbstractUserFilePath("output");
		String encording = ToolUtils.getNewValue(System.getProperty(KEY_SQL_ENCORDING), FileHandler.FILE_ENCORDING);
		String filter = System.getProperty(KEY_SQL_FILTER);
		inputPath = args.length > 0 ? args[0] : inputPath;
		ILineProcessor processor = new SqlColumnValueReplaceProcessor(ConfigProvider.getConfigMap(CONFIG_FILE));
		FileHandler.excute(processor, new FileOperationSet(inputPath, outputPath, encording, filter));
		System.exit(0);
	}
}
