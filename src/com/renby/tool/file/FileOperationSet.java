package com.renby.tool.file;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.renby.tool.ToolUtils;

/**
 * 文件操作信息
 * @author renbaoyu
 *
 */
public class FileOperationSet {
	private String inputPath;
	private String outputPath;
	private String charset;
	private String filter;
	private Set<String> filterSet = new HashSet<String>();

	public FileOperationSet() {
	}

	public FileOperationSet(String inputPath, String outputPath, String charset, String filter) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.charset = charset;
		this.filter = filter;
		if (!ToolUtils.isEmpty(filter)) {
			for (String extension : filter.split(",")) {
				filterSet.add(extension.toLowerCase());
			}
		}
	}

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
		if (!ToolUtils.isEmpty(filter)) {
			for (String extension : filter.split(",")) {
				filterSet.add(extension.toLowerCase());
			}
		}
	}

	/**
	 * 判断文件是否为指定的格式
	 * 
	 * @param file
	 * @return
	 */
	public boolean isFilterPassed(File file) {
		if (filterSet.isEmpty()) {
			return true;
		}
		String[] name = file.getName().split("\\.");
		if (filterSet.contains(name[name.length - 1].toLowerCase())) {
			return true;
		}
		return false;
	}
}
