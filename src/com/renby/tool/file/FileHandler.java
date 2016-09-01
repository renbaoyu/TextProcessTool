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

import com.renby.tool.ToolUtils;
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
		printInputInformation(operSet);
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
		processFolder(input, processor, operSet);
		logger.info("处理文件结束.");
	}

	/**
	 * 处理文件夹及子文件夹下的所有指定类型的文件
	 * 
	 * @param file
	 *            文件夹或文件
	 * @param processor
	 *            处理器
	 * @param operSet
	 *            文件设置
	 */
	private static void processFolder(File input, ILineProcessor processor, FileOperationSet operSet) {
		if(input.isFile()){
			if(operSet.isFilterPassed(input)){
				processFile(input, processor, operSet);
			}
		}else{
			File[] files = input.listFiles();
			for (File file : files) {
				processFolder(file, processor, operSet);
			}
		}
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
		File processedParent = new File(file.getParent().replace(operSet.getInputPath(), operSet.getOutputPath()));
		if(!processedParent.exists()){
			processedParent.mkdirs();
		}
		File processedFile = new File(processedParent, file.getName());
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
	
	private static void printInputInformation(FileOperationSet operSet){
		logger.info("******************************************************************");
		logger.info("*待处理文件或目录:{}", operSet.getInputPath());
		logger.info("*输出目录　　　　:{}", operSet.getOutputPath());
		logger.info("*待处理文件字符集:{}", operSet.getCharset());
		logger.info("*处理的文件类型　:{}", ToolUtils.getNewValue(operSet.getFilter(), "All"));
		logger.info("******************************************************************");
	}
}
