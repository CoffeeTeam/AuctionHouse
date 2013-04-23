package util;

import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;

public class CustomFileAppender {

	private static CustomFileAppender customAppender = null;
	private static FileAppender fileAppender;
	
	public FileAppender getFileAppender() {
		return fileAppender;
	}

	public void setFileAppender(FileAppender fileAppender) {
		CustomFileAppender.fileAppender = fileAppender;
	}

	private CustomFileAppender(String filename) {
		fileAppender = new FileAppender();
		fileAppender.setLayout(new PatternLayout("%-5p [%t]: %m%n"));
		fileAppender.setFile(filename);
		fileAppender.activateOptions();
		fileAppender.setAppend(true);
	}
	
	public static CustomFileAppender getCustomFileAppender(String fileName) {
		if (null == customAppender) {
			customAppender = new CustomFileAppender(fileName);
		}
		
		return customAppender;
	}
	
	public static CustomFileAppender getCustomFileAppender() {
		return customAppender;
	}
}
