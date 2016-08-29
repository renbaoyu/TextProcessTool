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
 * �ļ������ߣ�������ļ�����������ָ������һ�ļ���
 * 
 * @author Administrator
 *
 */
public class FileHandler {
	/** �ļ�Ĭ�ϱ����ʽ */
	public static final String FILE_ENCORDING = "UTF-8";
	/** �н������� */
	public static String LINE_END = System.getProperty("line.separator");
	private static Logger logger = LogManager.getLogger();

	/**
	 * ִ���ļ�����
	 * 
	 * @param processor
	 *            ������
	 * @param operSet
	 *            �ļ�����
	 */
	public static void excute(ILineProcessor processor, FileOperationSet operSet) {

		File input = new File(operSet.getInputPath());
		if (!input.exists()) {
			logger.error("�����ļ�/�ļ���[{}]������.", operSet.getInputPath());
			return;
		}
		File output = new File(operSet.getOutputPath());
		if (!output.exists()) {
			output.mkdirs();
		} else {
			for (File file : output.listFiles()) {
				if (!file.delete()) {
					logger.error("ɾ���ļ�ʧ�ܣ�" + file.getName());
				}
			}
		}
		LogManager.getLogger().info("�����ļ���ʼ.");
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
		logger.info("�����ļ�����.");
	}

	/**
	 * ִ���ļ�����
	 * 
	 * @param file
	 *            �ļ�
	 * @param processor
	 *            ������
	 * @param operSet
	 *            �ļ�����
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
				logger.error("ָ���ı���[{}]������ָ����ȷ���ַ���", operSet.getCharset());
				closeFile(reader);
				closeFile(writer);
				return;
			}
			try {
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					try{
						writer.write(processor.process(line));
					}catch(Exception e){
						logger.error("�����ļ��쳣", e);
					}
				}
			} catch (IOException e) {
				logger.error("��ȡ�ļ�����");
			} finally {
				closeFile(reader);
				closeFile(writer);
			}
			logger.info("�Ѵ����ļ���" + file.getName());
		} catch (FileNotFoundException e) {
			logger.error("�����ļ�������.");
		}

	}

	/**
	 * �ر��ļ���
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
			logger.error("�ر��ļ�ʧ��");
		}
	}
}
