package com.sakura.handler;

import com.sakura.service.RunUnitService;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.sakura.base.TestStep;
import com.sakura.util.AppiumUtil;
import com.sakura.util.SeleniumUtil;

public class ClearActionHandler {
    Logger log = Logger.getLogger(ClearActionHandler.class);
    
	/**
	 * <br>Web端清除操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void webInputclear(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name", step.getId() + "." + step.getName() + ">");
		step.setType("elementToBeClickable");
		WebElement e = SeleniumUtil.getElement(step);
//		e.clear();	
		
//		WebDriver driver = step.getWebDriver();
//		Actions actions=new Actions(driver);
//	    actions.doubleClick(e).perform();
//	    e.sendKeys("");
	    
	    e.sendKeys(Keys.CONTROL,"a");
	    e.sendKeys(Keys.DELETE);
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
