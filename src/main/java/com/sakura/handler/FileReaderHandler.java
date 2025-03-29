package com.sakura.handler;

import com.sakura.base.TestStep;
import com.sakura.util.AppiumUtil;
import com.sakura.util.FileReaderUtil;
import com.sakura.util.SeleniumUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class FileReaderHandler {
    Logger log = Logger.getLogger(FileReaderHandler.class);
    
	/**
	 * <br>Web端清除操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void webInputclear(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		FileReaderUtil.readFileContent(step.getLocalpath(), step.getFiletype());
	}
	
	/**
	 * <br>Android端清除操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void androidClear(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		WebElement e = AppiumUtil.getElement(step);
		e.clear();	
	}
}
