package com.sakura.handler;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import com.sakura.service.WebXmlParseService;
import com.sakura.util.AppiumUtil;
import com.sakura.util.DateUtil;
import com.sakura.util.FileUtil;
import com.sakura.util.SeleniumUtil;
import com.sakura.util.StringUtil;
import com.sakura.util.ConfigUtil;
import com.sakura.util.Constants;

import static com.sakura.handler.ClickActionHandler.Click_Fail;

public class InputActionHandler {
    static Logger log = Logger.getLogger(InputActionHandler.class);
    
    /**
     * <br>
     * Web端输入操作</br>
     *
     * @param step
     * @throws Exception
     */
    public static void webInput(TestStep step) throws Exception {
		if (StringUtil.isNoEmpty(step.getWaitTime())){
			Thread.sleep(Long.parseLong(step.getWaitTime()));
		}
    	String value = SeleniumUtil.parseStringHasEls(step.getValue());
    	log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ value +"]");
        RunUnitService.Step.put("name", step.getId() + "." + step.getName() + ">["+ value +"]");

//		WebElement webElement = null;
//		step.setType("elementToBeClickable");
//		webElement = SeleniumUtil.getElement(step);
//        if (webElement != null) {
//			webElement.click();
////        	Thread.sleep(500);
//			webElement.sendKeys(Keys.CONTROL,"a");
//			webElement.sendKeys(Keys.DELETE);
//			webElement.sendKeys(value);
//        }

//		WebElement webElement = null;
//		int maxRetries = 3; // 最大重试次数
//		int retryCount = 0;
//		try {
//			step.setType("elementToBeClickable");
//			webElement = SeleniumUtil.getElement(step);
//			if (webElement != null) {
//				while (retryCount < maxRetries) {
//					try {
//						webElement.click();
//						webElement.sendKeys(Keys.CONTROL,"a");
//						webElement.sendKeys(Keys.DELETE);
//						webElement.sendKeys(value);
//						break; // 成功点击后退出循环
//					} catch (Exception e) {
//						log.error("点击失败，重试次数: " + (retryCount + 1), e);
//						retryCount++;
//						if (retryCount >= maxRetries) {
//							log.error("超出最大重试次数，放弃点击");
//							throw e; // 超出最大重试次数后抛出异常
//						}
////                        Thread.sleep(500); // 等待一段时间后重试
//					}
//				}
//			}
//		} catch (Exception e) {
////			log.error("", e);
//		}

		int maxRetries = Integer.parseInt(Click_Fail); // 最大重试次数
		int retryCount = 1;
		WebElement webElement = null;
		while (true) {
			try {
				step.setType("elementToBeClickable");
				webElement = SeleniumUtil.getElement(step);
				webElement.click();
				webElement.sendKeys(Keys.CONTROL,"a");
				webElement.sendKeys(Keys.DELETE);
				webElement.sendKeys(value);
				break;
			} catch (Exception e) {
				log.info("『发现问题』执行异常: "+ "<" +step.getId() +"." + step.getName() + "_元素定位异常导致失败，重新点击>[重试次数:" + retryCount + "]");
				if (retryCount > maxRetries) {
					log.info("『发现问题』执行异常: "+ "<" +step.getId() + "." + step.getName() +"_元素定位异常导致失败，重新点击>[超出最大重试次数:"+maxRetries+"，放弃点击]");
					RunUnitService.Step.put("name", step.getId() +"." + step.getName() + "_元素定位异常导致失败，重新点击>[超出最大重试次数:"+maxRetries+"，放弃点击]");
					break;
				}
				retryCount++;
			}
		}
    }
    
    public void webInputdate(TestStep step) throws Exception {
    	String value = "";
    	if(step.getKey()!=null) {
    		String key = SeleniumUtil.parseStringHasEls(step.getKey());
    		value = DateUtil.getDateFormat(key);
    		if(step.getKeys()!=null) {
    			value = DateUtil.getDateFormat(key,step.getKeys());
    		}
    	}else {
    		value = DateUtil.getDate();
    	}
		step.setType("elementToBeClickable");
        WebElement e = SeleniumUtil.getElement(step);
        if (e != null) {
    	    e.sendKeys(Keys.CONTROL,"a");
    	    e.sendKeys(Keys.DELETE);
            e.sendKeys(value);
        }
        log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ value +"]");
        RunUnitService.Step.put("name", step.getId() + "." + step.getName() + ">["+ value +"]");
    }
    
	public void webInputfile(TestStep step) throws Exception {
		String value = "";
		try {
			step.setType("presenceOfElementLocated");
			WebElement e = SeleniumUtil.getElement(step);
			if (e != null) {
				value = SeleniumUtil.parseStringHasEls(step.getLocalpath());
				e.sendKeys(value);
				if (step.getDelete() != null && StringUtil.isEqual(step.getDelete(), "true")) {
					FileUtil.deleteFile(value);
				}
			}
			log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">[" + value + "]");
			RunUnitService.Step.put("name", "" + step.getId() + "." + step.getName() + ">[" + value + "]");
		} catch (Exception b) {
			if (step.getSkip() == null || StringUtil.isNotEqual("localpath", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 未找到文件：【" + value + "】");
				RunUnitService.Step.put("name", step.getId() + "." + step.getName() + ">[" + value + "]");
				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(),"Step" + step.getId() + "." + step.getName() + "");
				RunUnitService.softAssert.fail("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 未找到文件：【" + value + "】");
				RunUnitService.Step.put("skip", "false");
				log.error("", b);
			} else if (StringUtil.isEqual("localpath", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【未找到文件：[ "+ value + "]，目前已忽略跳过，请仔细检查测试脚本..】");
				RunUnitService.Step.put("name", step.getId() + "." + step.getName() + "==> 【未找到文件：[ " + value + "]，目前已忽略跳过，请仔细检查测试脚本..】");
				RunUnitService.Step.put("skip", "true");
			}
		}
	}
    
	public void webInputfiles(TestStep step) throws Exception {
		String value = "";
		try {
			step.setType("presenceOfElementLocated");
			WebElement e = SeleniumUtil.getElement(step);
			if (e != null) {
				if (step.getCatalogue() != null) {
					value = SeleniumUtil.parseStringHasEls(System.getProperty(step.getCatalogue()) + step.getLocalpath());
					e.sendKeys(value);
				} else {
					value = SeleniumUtil.parseStringHasEls(System.getProperty("user.dir") + step.getLocalpath());
					e.sendKeys(value);
				}
				if (step.getDelete() != null && StringUtil.isEqual(step.getDelete(), "true")) {
					FileUtil.deleteFile(value);
				}
			}
			log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">[" + value + "]");
			RunUnitService.Step.put("name", "" + step.getId() + "." + step.getName() + ">[" + value + "]");
		} catch (Exception b) {
			if (step.getSkip() == null || StringUtil.isNotEqual("localpath", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 未找到文件：【"+value+"】");
				RunUnitService.Step.put("name", "" + step.getId() + "." + step.getName() + ">[" + value + "]");
				RunUnitService.Step.put("skip", "false");
				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(),"Step" + step.getId() + "." + step.getName() + "");
				RunUnitService.softAssert.fail("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==>未找到文件： 【"+value+"】");
				log.error("", b);
			} else if (StringUtil.isEqual("localpath", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【未找到文件：[ "+ value + "]，目前已忽略跳过，请仔细检查测试脚本..】");
				RunUnitService.Step.put("name", "" + step.getId() + "." + step.getName() + "==> 【未找到文件：[ " + value + "]，目前已忽略跳过，请仔细检查测试脚本..】");
				RunUnitService.Step.put("skip", "true");
			}
		}
	}
    
	public void webInputzs(TestStep step) throws Exception {
		String value = "";
		try {
			step.setType("presenceOfElementLocated");
			WebElement e = SeleniumUtil.getElement(step);
			String ip = ConfigUtil.getProperty("" + step.getDevice() + "_LinuxShell_IP", Constants.CONFIG_APP)
					.replace(".", "_");
			if (e != null) {
				value = SeleniumUtil.parseStringHasEls(System.getProperty(step.getCatalogue()) + step.getLocalpath() + ip + "_audit.lic");
				e.sendKeys(value);
			}
			log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">[" + value + "]");
			RunUnitService.Step.put("name", "" + step.getId() + "." + step.getName() + ">[" + value + "]");
		} catch (Exception b) {
			if (step.getSkip() == null || StringUtil.isNotEqual("localpath", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 未找到文件：【" + value + "】");
				RunUnitService.Step.put("name", "" + step.getId() + "." + step.getName() + ">[" + value + "]");
				RunUnitService.Step.put("skip", "false");
				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(),"Step" + step.getId() + "." + step.getName() + "");
				RunUnitService.softAssert.fail("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 未找到文件：【" + value + "】");
				log.error("", b);
			} else if (StringUtil.isEqual("localpath", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【未找到文件：[ "+ value + "]，目前已忽略跳过，请仔细检查测试脚本..】");
				RunUnitService.Step.put("name", "" + step.getId() + "." + step.getName() + "==> 【未找到文件：[ " + value + "]，目前已忽略跳过，请仔细检查测试脚本..】");
				RunUnitService.Step.put("skip", "true");
			}
		}
	}
    
    /**
     * <br>
     * Android端输入操作</br>
     *
     * @param step
     * @throws Exception
     */
    public void androidInput(TestStep step) throws Exception {
        try {
            log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">");
            if(step.getState()==null){
                WebElement e = AppiumUtil.getElement(step);
                e.clear();
                e.sendKeys(AppiumUtil.parseStringHasEls(step.getValue()));
            }else {
                char[] values = AppiumUtil.parseStringHasEls(step.getValue()).toCharArray();
                WebElement e = AppiumUtil.getElement(step);
                e.sendKeys(AppiumUtil.parseStringHasEls(String.valueOf(values[Integer.parseInt(step.getState())])));
            }
        } catch (Exception e) {
//            log.info("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "> ==> 【未找到相关元素信息】");
//            RunUnitService.softAssert.fail("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "> ==> 【未找到相关元素信息】");
//            AndroidXmlParseService.screenShot(TestUnit.getname(), TestCase.getid(), "" + step.getId() + "." + step.getName() + "");
             //e.printStackTrace();
             log.error("",e);
             Thread.sleep(500);
        }
    }
}