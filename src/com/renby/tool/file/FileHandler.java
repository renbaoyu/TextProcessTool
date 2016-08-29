package com.renby.tool.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.renby.tool.processor.ILineProcessor;

/**
 * 文件处理工具，输入的文件处理后输出到指定的另一文件中
 * 
 * @author Administrator
 *
 */
public class FileHandler {
	/** 文件默认编码格式 */
	public static final String FILE_ENCORDING = "UTF-8";
	/** 行结束符号 */
	public static String LINE_END = System.getProperty("line.separator");
	private static Logger logger = LogManager.getLogger();

	/**
	 * 执行文件处理
	 * 
	 * @param processor
	 *            处理器
	 * @param operSet
	 *            文件设置
	 */
	public static void excute(ILineProcessor processor, FileOperationSet operSet) {

		File input = new File(operSet.getInputPath());
		if (!input.exists()) {
			logger.error("输入文件/文件夹[{}]不存在.", operSet.getInputPath());
			return;
		}
		File output = new File(operSet.getOutputPath());
		if (!output.exists()) {
			output.mkdirs();
		} else {
			for (File file : output.listFiles()) {
				if (!file.delete()) {
					logger.error("删除文件失败：" + file.getName());
				}
			}
		}
		LogManager.getLogger().info("处理文件开始.");
		if(input.isFile()){
			if(operSet.isFilterPassed(input)){
				processFile(input, processor, operSet);
			}
		}else{
			File[] files = input.listFiles();
			for (File file : files) {
				if(operSet.isFilterPassed(input)){
					processFile(file, processor, operSet);
				}
			}
		}
		logger.info("处理文件结束.");
	}

	/**
	 * 执行文件处理
	 * 
	 * @param file
	 *            文件
	 * @param processor
	 *            处理器
	 * @param operSet
	 *            文件设置
	 */
	private static void processFile(File file, ILineProcessor processor, FileOperationSet operSet) {

		File processedFile = new File(operSet.getOutputPath(), file.getName());
		try {
			BufferedReader reader = null;
			OutputStreamWriter writer = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), operSet.getCharset()));
				writer = new OutputStreamWriter(new FileOutputStream(processedFile), operSet.getCharset());
			} catch (UnsupportedEncodingException e1) {
				logger.error("指定的编码[{}]错误，请指定正确的字符集", operSet.getCharset());
				closeFile(reader);
				closeFile(writer);
				return;
			}
			try {
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					try{
						writer.write(processor.process(line));
					}catch(Exception e){
						logger.error("处理文件异常", e);
					}
				}
			} catch (IOException e) {
				logger.error("读取文件错误");
			} finally {
				closeFile(reader);
				closeFile(writer);
			}
			logger.info("已处理文件：" + file.getName());
		} catch (FileNotFoundException e) {
			logger.error("配置文件不存在.");
		}

	}

	/**
	 * 关闭文件流
	 * 
	 * @param file
	 */
	private static <T> void closeFile(T file) {
		if (file == null) {
			return;
		}
		try {
			if (file instanceof Reader) {
				((Reader) file).close();
			} else if (file instanceof Writer) {
				((Writer) file).close();
			}
		} catch (IOException e) {
			logger.error("关闭文件失败");
		}
	}
}
