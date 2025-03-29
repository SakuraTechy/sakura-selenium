package com.sakura.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import com.sakura.service.WebXmlParseService;
//import com.ankki.util.ConfigUtil;
//import com.ankki.util.Constants;
import com.sakura.util.SeleniumUtil;

public class GetUrlActionHandler {
	Logger log = Logger.getLogger(GetUrlActionHandler.class);
	
	/**
	 * <br>Web端获取配置文件，打开网站链接操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
//	public void webGeturl(TestStep step) throws Exception {
//		String URL = "";
//		String port = ConfigUtil.getProperty(""+ step.getDevice()+"_Login_Port", Constants.CONFIG_APP);
//		if(port.equals("")) {
//			URL = 
//					ConfigUtil.getProperty(""+ step.getDevice() +"_Login_HTTP", Constants.CONFIG_APP) +"://"
//				+	ConfigUtil.getProperty(""+ step.getDevice() +"_LinuxShell_IP", Constants.CONFIG_APP) 
//				+	ConfigUtil.getProperty(""+ step.getDevice() +"_Login_Path", Constants.CONFIG_APP);
//		}
//		URL = 
//				ConfigUtil.getProperty(""+ step.getDevice() +"_Login_HTTP", Constants.CONFIG_APP) +"://"
//			+	ConfigUtil.getProperty(""+ step.getDevice() +"_LinuxShell_IP", Constants.CONFIG_APP) +":"
//			+	ConfigUtil.getProperty(""+ step.getDevice() +"_Login_Port", Constants.CONFIG_APP)
//			+	ConfigUtil.getProperty(""+ step.getDevice() +"_Login_Path", Constants.CONFIG_APP);
//		step.getWebDriver().get(URL);	
//		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ URL +"]");
//		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ URL +"]");
//	}
	
	public void webGeturl(TestStep step) throws Exception {
		String URL = SeleniumUtil.parseStringHasEls(step.getValue());
		step.getWebDriver().get(URL);
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ URL +"]");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ URL +"]");
	}
	
	/**
	 * <br>Web端获取值，打开网站链接操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void webGeturls(TestStep step) throws Exception {
		String URL = SeleniumUtil.parseStringHasEls(step.getValue());
		step.getWebDriver().get(URL);
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ URL +"]");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ URL +"]");
	}
	
	/**
	 * <br>Android端打开网站链接操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void androidGeturl(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		step.getAndroidDriver().get(step.getValue());	
	}
	
	/**
	 * <br>WEB端页面刷新操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void webRefresh(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		Thread.sleep(1000);
		step.getWebDriver().navigate().refresh();
		Thread.sleep(1000);
	}
	
	/**
	 * <br>WEB端关闭当前标签页操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void webClose(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		step.getWebDriver().close();
	}
	
	/**
	 * <br>WEB端关闭全部标签页操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void webQuit(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		WebXmlParseService.QuitBrowser();
	}
	
	/**
	 * <br>Web端切换iframe控件操作</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void switchIframe(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		WebDriver driver = step.getWebDriver();
		if(step.getValue()!=null) {
			//通过index获取，，第一位0
			driver.switchTo().frame(Integer.parseInt(step.getValue())-1);
		}else {
			//通过id，name，xpath获取
			driver.switchTo().frame(SeleniumUtil.getElement(step));
		}
    }
	
	/**
	 * <br>Web端返回上一级iframe控件操作</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void returnIframe(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		WebDriver driver = step.getWebDriver();
		driver.switchTo().parentFrame();
    }
	
	/**
	 * <br>Web端返回最上级iframe控件操作</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void quitIframe(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		WebDriver driver = step.getWebDriver();
		driver.switchTo().defaultContent();
    }
	
	/**
	 * <br>模拟操作切换浏览器到当前最新窗口，默认最后一个窗口</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void switchWindow(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		//得到当前所有窗口的Handles  
		Set<String> WindowHandle  = step.getWebDriver().getWindowHandles();
		//将Handles存入list中  
		List<String> WindowHandleList = new ArrayList<String>(WindowHandle);
//		log.info(WindowHandleList);
		//切换浏览器到当前最新窗口
		step.getWebDriver().switchTo().window(WindowHandleList.get(WindowHandleList.size()-1));
	}
	
	/**
	 * <br>模拟操作切换浏览器到当前指定窗口，适合多窗口互相切换</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void switchWindows(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		//得到当前所有窗口的Handles  
		Set<String> WindowHandle  = step.getWebDriver().getWindowHandles();
		//将Handles存入list中  
		List<String> WindowHandleList = new ArrayList<String>(WindowHandle);
//		log.info(WindowHandleList);
		//切换到浏览器指定窗口中，0表示第一个窗口，1表示第二窗口
		step.getWebDriver().switchTo().window(WindowHandleList.get(Integer.parseInt(step.getValue())));
	}

	/**
	 * <br>Web端执行js脚本操作</br>
	 *
	 * @param step
	 * @throws Exception
	 */
	public void javascriptExecutor(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		((JavascriptExecutor)step.getWebDriver()).executeScript(step.getScript());
	}
}
