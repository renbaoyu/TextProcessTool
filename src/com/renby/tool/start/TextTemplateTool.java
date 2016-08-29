package com.renby.tool.start;

import com.renby.tool.ToolUtils;
import com.renby.tool.config.ConfigProvider;
import com.renby.tool.file.FileHandler;
import com.renby.tool.file.FileOperationSet;
import com.renby.tool.processor.AttributeReplaceProcessor;
import com.renby.tool.processor.ILineProcessor;

public class TextTemplateTool {
	/** 配置文件名 */
	public static final String CONFIG_FILE = "TemplateConfig.cfg";
	/** 模版文件的编码格式 */
	public static final String TEMPLATE_ENCORDING_KEY = "template.encording";
	/** 模版文件扩展名 */
	public static final String KEY_TEMPLATE_FILTER = "template.filter";

	public static void main(String[] args) {
		ConfigProvider.initSystemProperty();
		String inputPath = ToolUtils.getAbstractUserFilePath("template");
		String outputPath = ToolUtils.getAbstractUserFilePath("output");
		String encording = ToolUtils.getNewValue(System.getProperty(TEMPLATE_ENCORDING_KEY),
				FileHandler.FILE_ENCORDING);
		String filter = System.getProperty(KEY_TEMPLATE_FILTER);

		inputPath = args.length > 0 ? args[0] : inputPath;
		ILineProcessor processor = new AttributeReplaceProcessor(ConfigProvider.getConfigMap(CONFIG_FILE));
		FileHandler.excute(processor, new FileOperationSet(inputPath, outputPath, encording, filter));
		System.exit(0);
	}
}
