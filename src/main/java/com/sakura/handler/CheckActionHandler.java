package com.sakura.handler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sakura.util.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.sakura.base.TestStep;
import com.sakura.service.AndroidXmlParseService;
import com.sakura.service.RunUnitService;
import com.sakura.service.WebXmlParseService;

/**
 * <br>检查操作<br/>
 *
 * @author  刘智
 * @date    2017年7月26日 上午10:27:26
 * @version 1.0
 * @since   1.0
 */
/**
 * <br>检查操作<br/>
 *
 * @author  刘智
 * @date    2017年7月26日 上午10:27:26
 * @version 1.0
 * @since   1.0
 */
@SuppressWarnings("unchecked")
public class CheckActionHandler {
    static Logger log = Logger.getLogger(CheckActionHandler.class);
	static String Check_Fail = ConfigUtil.getProperty("Check_Fail", ConstantsUtil.CONFIG_COMMON);

	/**
	 * <br>检查界面上的元素是否与预期值相等</br>
	 *
	 * @param step
	 * @return
	 * @throws Exception
	 * @author 刘智
	 * @date 2017年8月2日 下午6:03:33
	 */
	//获取元素的文本值
	public static boolean webCheck(TestStep step) throws Exception{
	    log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		step.setType("textToBe");
//		String Actual = SeleniumUtil.getElement(step).getText();
		String Actual = "";
		int maxRetries = Integer.parseInt(Check_Fail); // 最大重试次数
		int retryCount = 1;
		while (true) {
//			log.info(step.getWebDriver().getPageSource());
			Actual = SeleniumUtil.getElement(step).getText();
			if(StringUtil.isEmpty(Actual)&&StringUtil.isNoEmpty(step.getExpect())&&StringUtil.isNotEqual(step.getSkip(),"expect")){
				log.info("『发现问题』执行异常: "+ "<" +step.getId() + "." + step.getName()+ ".获取元素文本值为空导致失败，重新获取>[重试次数:" + retryCount + "]");
//				RunUnitService.Step.put("name", step.getId() + ".获取元素文本值为空导致失败，重新获取>[重试次数:" + (retryCount + 1) + "]");
				if (retryCount > maxRetries) {
                    log.info("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName()+ ".获取元素文本值为空导致失败，重新获取>[超出最大重试次数:" + maxRetries + "，放弃获取]");
                    RunUnitService.Step.put("name", step.getId() +"." + step.getName()+ ".获取元素文本值为空导致失败，重新获取>[超出最大重试次数:"+maxRetries+"，放弃获取]");
                    break;
                }
				retryCount++;
			}else {
				break;
			}
		}
		if(step.getRegex()!=null) {
			if(step.getValue()!=null) {
				Actual = StringUtil.replaceAllBlank(Actual, step.getRegex(), step.getValue());
			} else {
				Actual = StringUtil.getRegex(Actual,step.getRegex());
			}
		}
		if(step.getKey()!=null&&step.getKeys()!=null) {
			Actual = Actual.replace(step.getKey(), step.getKeys());
		}
		String Expected = SeleniumUtil.parseStringHasEls(step.getExpect());
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
		checkEqualsWeb(step,Actual,Expected,FailHint,name);

		return Actual.equals(Expected);
	}

	/**
	 * <br>检查界面上的元素是否与预期值不相等</br>
	 *
	 * @param step
	 * @return
	 * @throws Exception
	 * @author 刘智
	 * @date 2017年8月2日 下午6:03:33
	 */
	public void webNotcheck(TestStep step) throws Exception{
	    log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		step.setType("textToBe");
//		String Actual = SeleniumUtil.getElement(step).getText();
		String Actual;
		int maxRetries = Integer.parseInt(Check_Fail); // 最大重试次数
		int retryCount = 1;
		while (true) {
//			log.info(step.getWebDriver().getPageSource());
			Actual = SeleniumUtil.getElement(step).getText();
			if(StringUtil.isEmpty(Actual)&&StringUtil.isNoEmpty(step.getExpect())){
				log.info("『发现问题』执行异常: "+ "<" +step.getId() + "." + step.getName()+ ".获取元素文本值为空导致失败，重新获取>[重试次数:" + retryCount + "]");
//				RunUnitService.Step.put("name", step.getId() + ".获取元素文本值为空导致失败，重新获取>[重试次数:" + (retryCount + 1) + "]");
				if (retryCount > maxRetries) {
					log.info("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName()+ ".获取元素文本值为空导致失败，重新获取>[超出最大重试次数:" + maxRetries + "，放弃获取]");
					RunUnitService.Step.put("name", step.getId() + "." + step.getName()+ ".获取元素文本值为空导致失败，重新获取>[超出最大重试次数:"+maxRetries+"，放弃获取]");
					break;
				}
				retryCount++;
			}else {
				break;
			}
		}
		String Expected = SeleniumUtil.parseStringHasEls(step.getExpect());	
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
		checkNotEqualsWeb(step,Actual,Expected,FailHint,name);
	}
	
	//获取元素的属性值
	public void webCheckvalue(TestStep step) throws Exception{
	    log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		step.setType("attributeToBe");
//		String Actual = SeleniumUtil.getElement(step).getAttribute(step.getValue());
		String Actual;
		int maxRetries = Integer.parseInt(Check_Fail); // 最大重试次数
		int retryCount = 1;
		while (true) {
//			log.info(step.getWebDriver().getPageSource());
			Actual = SeleniumUtil.getElement(step).getAttribute(step.getValue());
			if(StringUtil.isEmpty(Actual)&&StringUtil.isNoEmpty(step.getExpect())){
				log.info("『发现问题』执行异常: "+ "<" +step.getId() + "." + step.getName()+ ".获取元素文本值为空导致失败，重新获取>[重试次数:" + retryCount + "]");
//				RunUnitService.Step.put("name", step.getId() + ".获取元素文本值为空导致失败，重新获取>[重试次数:" + (retryCount + 1) + "]");
				if (retryCount > maxRetries) {
					log.info("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName()+ ".获取元素文本值为空导致失败，重新获取>[超出最大重试次数:" + maxRetries + "，放弃获取]");
					RunUnitService.Step.put("name", step.getId() + "." + step.getName()+ ".获取元素文本值为空导致失败，重新获取>[超出最大重试次数:"+maxRetries+"，放弃获取]");
					break;
				}
				retryCount++;
			}else {
				break;
			}
		}
		String Expected = SeleniumUtil.parseStringHasEls(step.getExpect());	
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
		checkEqualsWeb(step,Actual,Expected,FailHint,name);
	}
	
	//获取调用JS脚本返回的值
	public void webCheckjs(TestStep step) throws Exception{
	    log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
//		String js = "var user_input = document.getElementById(\"passport_51_user\").title;return user_input;";
//		String js1 = "var specVol =$(\"#specVol\").val();return specVol;";
//		String js2 = "return computeVol();";
//	    String js3 = "document.querySelector(\"div[class='modal-body scroll-wrap'] div:nth-child(2) div:nth-child(1) button:nth-child(1)\").className";
		String Actual = (String) ((JavascriptExecutor)step.getWebDriver()).executeScript(step.getValue());  
		String Expected = SeleniumUtil.parseStringHasEls(step.getExpect());	
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
		checkEqualsWeb(step,Actual,Expected,FailHint,name);
	}

	// 检查从数据库中查询出的结果中的值是否符合预期
	public void webChecklist(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		String condition = step.getDetails().get("condition");
		String name = step.getId() + "." +step.getName();
		if(Constants.SIZE_NOT_ZERO.equals(condition))
			weblistSizeNotZero(step,name);
		else if(Constants.FIELD.equals(condition))
			webFieldEquals(step,name);
	}

	// 不检查从数据库中查询出的结果中的值是否符合预期
	public void webNotchecklist(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		String condition = step.getDetails().get("condition");
		String name = step.getId() + "." +step.getName();
		if(Constants.SIZE_NOT_ZERO.equals(condition))
			weblistSizeNotZero(step,name);
		else if(Constants.FIELD.equals(condition))
			webNotFieldEquals(step,name);
	}

	// 正则检查从数据库中查询出的结果中的值是否符合预期
	public void webRegexchecklist(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		String name = step.getId() + "." +step.getName();
		if(StringUtil.isNoEmpty(step.getValue())){
			webRegexFieldEqualsAll(step,name);
		}else{
			String condition = step.getDetails().get("condition");
			if(Constants.SIZE_NOT_ZERO.equals(condition))
				weblistSizeNotZero(step,name);
			else if(Constants.FIELD.equals(condition))
				webRegexFieldEquals(step,name);
		}
	}

	// 检查本地list变量
	public void webCheckset(TestStep step) throws Exception{
	    log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		String Actual = SeleniumUtil.parseStringHasEls(step.getValue());	
		String Expected = SeleniumUtil.parseStringHasEls(step.getExpect());	
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
		checkEqualsWeb(step,Actual,Expected,FailHint,name);
	}
	
	// 不检查本地list变量
	public void webNotchecksetlist(TestStep step) throws Exception{
	    log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		String Actual = SeleniumUtil.parseStringHasEls(step.getValue());
		String Expected = SeleniumUtil.parseStringHasEls(step.getExpect());	
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
		checkNotEqualsWeb(step,Actual,Expected,FailHint,name);
	}

	// 正则检查本地list变量
	public void webRegexchecksetlist(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		String Actual = SeleniumUtil.parseStringHasEls(step.getValue());
		String Regex = SeleniumUtil.parseStringHasEls(step.getRegex());
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
//		checkRegexEqualsWeb(step,Actual,Regex,FailHint,name);
		webRegexFieldEqualsAll(step,name);
	}

	/**
	 * <br>检查指定路径下的文件是否匹配模式</br>
	 *
	 * @author    刘智
	 * @date      2017年8月2日 下午6:03:33
	 * @param step
	 * @throws Exception
	 */
	public void webCheckPathFileByPatterns(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		String value;
		if (step.getCatalogue() != null) {
			value = SeleniumUtil.parseStringHasEls(System.getProperty(step.getCatalogue()) + step.getLocalpath());
		} else {
			value = SeleniumUtil.parseStringHasEls(System.getProperty("user.dir") + step.getLocalpath());
		}

		File file = new File(value);
		Object Actual;
		if(StringUtil.isEmpty(step.getRegex())) {
			Actual = file.exists() && file.isFile();
		} else {
			Actual = FileUtil.checkPathFileByPatterns(value, SeleniumUtil.parseStringHasEls(step.getRegex()));
		}

		Object Expected = Boolean.parseBoolean(SeleniumUtil.parseStringHasEls(step.getExpect()));
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
		checkEqualsWeb(step,Actual,Expected,FailHint,name);
	}

	/**
	 * <br>检查界面上的元素是否消失不可见</br>
	 *
	 * @author    刘智
	 * @date      2017年8月2日 下午6:03:33
	 * @param step
	 * @throws Exception
	 */
	//获取元素的文本值
	public static Boolean webCheckInvisible(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name",step.getId() + "." +step.getName());
		String loc = step.getLocator();
		if (StringUtil.isBlank(loc)) throw new Exception("当前步骤未定位到任何控件元素！");
		int idx1 = loc.indexOf("=");
		String locatename = loc.substring(0, idx1);
		String locatevalue = loc.substring(idx1 + 1);
		locatevalue = SeleniumUtil.parseStringHasEls(locatevalue);
		boolean result = true;
		try {
			if (StringUtil.isNoEmpty(step.getSkip()) && StringUtil.isEqual("invisible", step.getSkip())) {
				step.setWebDriverWait(new WebDriverWait(step.getWebDriver(), 1));
			}
			if (locatename.equals("xpath")) {
				// 等待遮挡元素消失
				step.getWebDriverWait().until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locatevalue)));
//				throw new Exception();
			}
		} catch (Exception e) {
			if(StringUtil.isEmpty(step.getSkip())&&StringUtil.isNotEqual(step.getSkip(),"invisible")){
				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(), "Step" + step.getId() + "." + step.getName());
				log.error("『发现问题』执行异常: ==> "+step.getMessage()+"：【" + locatevalue + "】");
				RunUnitService.softAssert.fail("『发现问题』执行异常: ==> "+step.getMessage()+"：【" + locatevalue + "】");
				RunUnitService.Step.put("name", step.getId() + "." +step.getName());
				RunUnitService.Step.put("skip", "false");
				RunUnitService.stepFail++;
			}else if(StringUtil.isNoEmpty(step.getSkip())&&StringUtil.isEqual(step.getSkip(),"invisible")){
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【目前已忽略跳过，不会截图也不会标记失败..】");
				RunUnitService.Step.put("name", step.getId() + "." + step.getName() + "==> 【目前已忽略跳过，不会截图也不会标记失败..】");
				RunUnitService.Step.put("skip", "true");
				RunUnitService.stepSkip++;
			}
			result = false;
			log.error("异常日志：",e);
		}
		Thread.sleep(1000);
		return result;
	}

	/**
	 * <br>模糊检查Web界面元素的内容</br>
	 *
	 * @author    刘智
	 * @date      2017年8月2日 下午6:03:33
	 * @param step
	 * @throws Exception
	 */
	public void webFuzzycheck(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		step.setType("visibilityOfElementLocated");
		String Actual = SeleniumUtil.getElement(step).getText();
		String Regex = step.getRegex();
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
		if(!StringUtil.StringMatchRule(Actual, Regex)) {
			if (StringUtil.isEqual("regex", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【实际结果【"+Actual+"】和正则匹配结果【"+Regex+"】不一致，已忽略跳过，请仔细检查测试脚本..】");
				name = step.getId() + "." + step.getName() + "(实际结果和预期结果不一致) 【实际结果【"+Actual+"】和正则匹配结果【"+Regex+"】不一致，已忽略跳过，请仔细检查测试脚本..】";
				RunUnitService.Step.put("skip", "true");
				RunUnitService.stepSkip++;
			} else {
				log.info("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Regex +"】");
				RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Regex +"】");
				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(),FailHint);
				RunUnitService.Step.put("skip", "false");
				RunUnitService.stepFail++;
			}
		}
		RunUnitService.Step.put("name",name);
	}

	/**
	 * <br>正则检查Web界面元素的内容</br>
	 *
	 * @author    刘智
	 * @date      2017年8月2日 下午6:03:33
	 * @param step
	 * @throws Exception
	 */
	public void webRegexCheck(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		step.setType("visibilityOfElementLocated");
		String Actual = SeleniumUtil.getElement(step).getText();
		String Regex = SeleniumUtil.parseStringHasEls(step.getRegex());
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
		if(!RegexUtil.matches(Regex, Actual)) {
			if (StringUtil.isEqual("regex", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【实际结果【"+Actual+"】和正则匹配结果【"+Regex+"】不一致，已忽略跳过，请仔细检查测试脚本..】");
				name = step.getId() + "." + step.getName() + " ==> 【实际结果【"+Actual+"】和正则匹配结果【"+Regex+"】不一致，已忽略跳过，请仔细检查测试脚本..】";
				RunUnitService.Step.put("skip", "true");
				RunUnitService.stepSkip++;
			} else {
				log.info("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Regex +"】");
				RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Regex +"】");
				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(),FailHint);
				RunUnitService.Step.put("skip", "false");
				RunUnitService.stepFail++;
			}
		}
		RunUnitService.Step.put("name",name);
	}

	/**
	 * <br>检查文件内容是否与预期值相等</br>
	 *
	 * @author    刘智
	 * @date      2017年8月2日 下午6:03:33
	 * @param step
	 * @throws Exception
	 */
	public void webCheckFileContent(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		String value = "";
		if (step.getCatalogue() != null) {
			value = SeleniumUtil.parseStringHasEls(System.getProperty(step.getCatalogue()) + step.getLocalpath());
		} else{
			value = SeleniumUtil.parseStringHasEls(step.getLocalpath());
		}
		String Actual;
		try {
			Actual = FileReaderUtil.readFileContent(value, step.getState());
		}catch (Exception e){
			Actual = "";
		}
		String Expected = SeleniumUtil.parseStringHasEls(step.getExpect());
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		String name = step.getId() + "." +step.getName();
		checkEqualsWeb(step,Actual,Expected,FailHint,name);
	}

	public void androidCheck(TestStep step) throws Exception{
	    try{
	        log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
//	        String Actual = AppiumUtil.getElement(step).getText();
	        String Actual = AppiumUtil.getElement(step).getAttribute("name");
	        String Expected = AppiumUtil.parseStringHasEls(step.getExpect());
	        String FailHint = "Step"+step.getId()+"."+step.getMessage();
//	        String CaseID = step.getCaseid();
	        String name = step.getId() + "." +step.getName();
	        checkEqualsAndroid(Actual,Expected,FailHint,name);
	    }catch(Exception e){
	        //e.printStackTrace();
			log.error("异常日志：",e);
	        Thread.sleep(500);
	    }
	}
	
	public void androidChecklist(TestStep step) throws NumberFormatException, Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		String conditon = step.getDetails().get("condition");
		if(Constants.SIZE_NOT_ZERO.equals(conditon))
			androidlistSizeNotZero(step);
		else if(Constants.FIELD.equals(conditon)) 
			androidfieldEquals(step);
	}
	
	public void weblistSizeNotZero(TestStep step,String name) throws Exception{
		List<Map<String,Object>>  list = (List<Map<String,Object>>)
		SeleniumUtil.parseEL(step.getDetails().get("subject"));

		if (list != null && list.isEmpty()) throw new Exception(step.getMessage());
		RunUnitService.Step.put("name", name);
	}
	
	public void androidlistSizeNotZero(TestStep step) throws Exception{
		List<Map<String,Object>>  list = (List<Map<String,Object>>)
		AppiumUtil.parseEL(step.getDetails().get("subject"));
		
		if(list.size() == 0)
			throw new Exception(step.getMessage());
	}
	/**
	 * <br>检查列表中的字段值</br>
	 * 此时localmap中的值是一个List<Map<>>，所以需要提供检查的索引
	 *
	 * @author    刘智
	 * @date      2017年8月3日 上午9:35:19
	 * @param step
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void webFieldEquals(TestStep step,String name) throws NumberFormatException, Exception{
//		log.info("CHECK_FILED---"+step.getDetails().get("subject"));
		String Actual = (String)SeleniumUtil.parseStringHasEls(step.getDetails().get("subject"));
		String Expected = SeleniumUtil.parseStringHasEls(step.getExpect());
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		try {
			Assert.assertEquals(Actual,Expected,FailHint);
        }catch (Error e)  {
        	if (StringUtil.isEqual("expect", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【实际结果【"+Actual+"】和预期结果【"+Expected+"】不一致，已忽略跳过，请仔细检查测试脚本..】");
				name = step.getId() + "." + step.getName() + " ==> 【实际结果【"+Actual+"】和预期结果【"+Expected+"】不一致，已忽略跳过，请仔细检查测试脚本..】";
				RunUnitService.Step.put("skip", "true");
				RunUnitService.stepSkip++;
			}else {
				RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
				RunUnitService.Step.put("skip", "false");
				RunUnitService.stepFail++;
			}
			log.error("异常日志：",e);
 	    }
		RunUnitService.Step.put("name", name);
	}

	/**
	 * <br>不检查列表中的字段值</br>
	 * 此时localmap中的值是一个List<Map<>>，所以需要提供检查的索引
	 *
	 * @author    刘智
	 * @date      2017年8月3日 上午9:35:19
	 * @param step
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void webNotFieldEquals(TestStep step,String name) throws NumberFormatException, Exception{
//		log.info("CHECK_FILED---"+step.getDetails().get("subject"));
		String Actual = (String)SeleniumUtil.parseStringHasEls(step.getDetails().get("subject"));
		String Expected = SeleniumUtil.parseStringHasEls(step.getExpect());
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		try {
			Assert.assertNotEquals(Actual,Expected,FailHint);
		}catch (Error e)  {
			if (StringUtil.isEqual("expect", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【实际结果【"+Actual+"】和预期结果【"+Expected+"】不一致，已忽略跳过，请仔细检查测试脚本..】");
				name = step.getId() + "." + step.getName() + " ==> 【实际结果【"+Actual+"】和预期结果【"+Expected+"】不一致，已忽略跳过，请仔细检查测试脚本..】";
				RunUnitService.Step.put("skip", "true");
				RunUnitService.stepSkip++;
			}else {
				RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
				RunUnitService.Step.put("skip", "false");
				RunUnitService.stepFail++;
			}
			log.error("异常日志：",e);
		}
		RunUnitService.Step.put("name", name);
	}

	/**
	 * <br>正则检查列表中的字段值</br>
	 * 此时localmap中的值是一个List<Map<>>，所以需要提供检查的索引
	 *
	 * @author    刘智
	 * @date      2017年8月3日 上午9:35:19
	 * @param step
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void webRegexFieldEquals(TestStep step,String name) throws NumberFormatException, Exception{
		log.info("数据表达式为："+step.getDetails().get("subject"));
		String Actual = SeleniumUtil.parseStringHasEls(step.getDetails().get("subject"));
		String Regex = SeleniumUtil.parseStringHasEls(step.getRegex());
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		log.info("正则表达式为："+Regex);
		log.info("开始检查第 1 条数据: "+Actual);
		if(!RegexUtil.matches(Regex, Actual)) {
			if (StringUtil.isEqual("regex", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【实际结果【"+Actual+"】和正则匹配结果【"+Regex+"】不一致，已忽略跳过，请仔细检查测试脚本..】");
				name = step.getId() + "." + step.getName() + " ==> 【实际结果【"+Actual+"】和正则匹配结果【"+Regex+"】不一致，已忽略跳过，请仔细检查测试脚本..】";
				RunUnitService.Step.put("skip", "true");
				RunUnitService.stepSkip++;
			} else {
				log.info("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Regex +"】");
				RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Regex +"】");
				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(),FailHint);
				RunUnitService.Step.put("skip", "false");
				RunUnitService.stepFail++;
			}
		}
		RunUnitService.Step.put("name",name);
	}

	public void webRegexFieldEqualsAll(TestStep step,String name) throws Exception{
		String Actual = SeleniumUtil.parseStringHasEls(step.getValue());
		String Regex = SeleniumUtil.parseStringHasEls(step.getRegex());
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		Object jsonObjectOrArray = JsonUtil.parseJsonString(Actual);
		JSONArray jsonArray = (JSONArray) jsonObjectOrArray;
		log.info("数据表达式为："+Actual);
		log.info("正则表达式为："+Regex);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			Actual = obj.toJSONString();
			if(StringUtil.isNoEmpty(step.getKey())){
				Actual = (String) obj.get(step.getKey());
//				log.info(step.getKey() + ": " + Actual);
			}
			log.info("开始检查第 "+(i+1)+" 条数据: "+Actual);
			if(!RegexUtil.matches(Regex, Actual)) {
				if (StringUtil.isEqual("regex", step.getSkip())) {
					log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【实际结果【"+Actual+"】和正则匹配结果【"+Regex+"】不一致，已忽略跳过，请仔细检查测试脚本..】");
					name = step.getId() + "." + step.getName() + " ==> 【实际结果【"+Actual+"】和正则匹配结果【"+Regex+"】不一致，已忽略跳过，请仔细检查测试脚本..】";
					RunUnitService.Step.put("skip", "true");
					RunUnitService.stepSkip++;
				} else {
					log.error("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Regex +"】");
					RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Regex +"】");
					WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(),FailHint);
					RunUnitService.Step.put("skip", "false");
					RunUnitService.stepFail++;
				}
			}
		}
		RunUnitService.Step.put("name",name);
	}

	public void androidfieldEquals(TestStep step) throws Exception{
//		log.info("CHECK_FILED---"+step.getDetails().get("subject"));
		String Actual = (String)AppiumUtil.parseStringHasEls(step.getDetails().get("subject"));
		String Expected = AppiumUtil.parseStringHasEls(step.getExpect());
		String FailHint = "Step"+step.getId()+"."+step.getMessage();
		try {
			Assert.assertEquals(Actual,Expected,FailHint);
        }catch (Error e)  {
 	    	Assert.fail(FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
 	    }
	}
	
	/**
	 * <br>检查预期与实际是否相等，不等则提示错误信息，并截图</br>
	 *
	 * @author    刘智
	 * @date      2017年8月2日 下午6:01:04
	 * @param Actual 
	 * @param Expected
	 * @param FailHint
     */
	public void checkEqualsWeb1(String Actual,String Expected,String FailHint,String CaseID){
		try {
			Assert.assertEquals(Actual,Expected,FailHint);
			Thread.sleep(500);
        }
 	    catch (Error | InterruptedException e)  {
 	    	WebXmlParseService.screenShot(CaseID);
 	    	Assert.fail(FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
 	    }
	}
	
	public static void checkEqualsWeb(TestStep step, Object Actual, Object Expected, String FailHint, String name) throws Exception {
		try {
			Assert.assertEquals(Expected,Actual,FailHint);
        } catch (Error e)  {
        	if (StringUtil.isEqual("expect", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【实际结果【"+Actual+"】和预期结果【"+Expected+"】不一致，已忽略跳过，请仔细检查测试脚本..】");
				name = step.getId() + "." + step.getName() + " ==> 【实际结果【"+Actual+"】和预期结果【"+Expected+"】不一致，已忽略跳过，请仔细检查测试脚本..】";
				RunUnitService.Step.put("skip", "true");
				RunUnitService.stepSkip++;
			} else {
				log.info("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
//	 	        Assert.fail(FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
//				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(),FailHint);
//				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(), "Step" + step.getId() + "." + step.getName()+"_"+FailHint);
				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(), "Step" + step.getId() + "." + step.getName()+"_"+step.getMessage());
	 	        RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
				RunUnitService.Step.put("skip", "false");
				RunUnitService.stepFail++;
			}
        	//e.printStackTrace();
			log.error("异常日志：",e);
 	    }
		RunUnitService.Step.put("name",name);
	}

	public void checkNotEqualsWeb(TestStep step, String Actual,String Expected,String FailHint,String name) {
		try {
			Assert.assertNotEquals(Actual,Expected,FailHint);
        } catch (Error e)  {
        	if (StringUtil.isEqual("expect", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【实际结果【"+Actual+"】和预期结果【"+Expected+"】不一致，已忽略跳过，请仔细检查测试脚本..】");
				name = step.getId() + "." + step.getName() + " ==> 【实际结果【"+Actual+"】和预期结果【"+Expected+"】不一致，已忽略跳过，请仔细检查测试脚本..】";
				RunUnitService.Step.put("skip", "true");
				RunUnitService.stepSkip++;
			} else {
				log.info("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
				RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
				RunUnitService.Step.put("skip", "false");
				RunUnitService.stepFail++;
			}
        	//e.printStackTrace();
			log.error("异常日志：",e);
 	    }
		RunUnitService.Step.put("name",name);
	}

	public void checkRegexEqualsWeb(TestStep step, String Actual,String Regex,String FailHint,String name) throws Exception {
		if(!RegexUtil.matches(Regex, Actual)) {
			if (StringUtil.isEqual("regex", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【实际结果【"+Actual+"】和正则匹配结果【"+Regex+"】不一致，已忽略跳过，请仔细检查测试脚本..】");
				name = step.getId() + "." + step.getName() + " ==> 【实际结果【"+Actual+"】和正则匹配结果【"+Regex+"】不一致，已忽略跳过，请仔细检查测试脚本..】";
				RunUnitService.Step.put("skip", "true");
				RunUnitService.stepSkip++;
			} else {
				log.info("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Regex +"】");
				RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Regex +"】");
				WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(),FailHint);
				RunUnitService.Step.put("skip", "false");
				RunUnitService.stepFail++;
			}
		}
		RunUnitService.Step.put("name",name);
	}

	public void checkBooleanWeb(TestStep step, String Actual,String Expected,String FailHint,String name) {
		try {
			Assert.assertEquals(Expected,Actual,FailHint);
        } catch (Error e)  {
        	if (StringUtil.isEqual("expect", step.getSkip())) {
				log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【实际结果【"+Actual+"】和预期结果【"+Expected+"】不一致，已忽略跳过，请仔细检查测试脚本..】");
				name = step.getId() + "." + step.getName() + " ==> 【实际结果【"+Actual+"】和预期结果【"+Expected+"】不一致，已忽略跳过，请仔细检查测试脚本..】";
				RunUnitService.Step.put("skip", "true");
				RunUnitService.stepSkip++;
			} else {
				log.info("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
	 	        RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
				RunUnitService.Step.put("skip", "false");
				RunUnitService.stepFail++;
			}
        	//e.printStackTrace();
			log.error("异常日志：",e);
 	    }
		RunUnitService.Step.put("name",name);
		if(StringUtil.StringMatchRule(FailHint, name)) {
			
		}
	}

	public void checkEqualsAndroid(String Actual,String Expected,String FailHint,String name) throws Exception{
		try { 
			Assert.assertEquals(Actual,Expected,FailHint);
        } catch (Error e)  {
 	        log.info("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
			AndroidXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(),FailHint);
 	        RunUnitService.softAssert.fail("『发现问题』执行异常: "+FailHint +"  "+"Actual 【"+ Actual +"】"+"  "+"but found 【"+ Expected +"】");
 	        RunUnitService.Step.put("name",name);
			RunUnitService.Step.put("skip", "false");
 	        //e.printStackTrace();
			log.error("异常日志：",e);
 	    }
		RunUnitService.Step.put("name",name);
	}

	public static void main(String[] args) {

	}
}