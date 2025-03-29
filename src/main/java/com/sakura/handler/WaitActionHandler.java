package com.sakura.handler;

import java.util.concurrent.TimeUnit;

import com.sakura.util.SeleniumUtil;
import com.sakura.util.StringUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;

/**
 * 等待动作处理类
 */
public class WaitActionHandler {
	static Logger log = Logger.getLogger(WaitActionHandler.class);
	
	/**
	 * 强制等待
	 * @param map
	 * @param step
	 */
	public static void waitForced(TestStep step){
		try {
			log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
			RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
			int waitTime = Integer.parseInt(step.getValue())/1000;
			if(waitTime>=1) {
				for(int i=waitTime;i>0;i--) {
					log.info("『正常测试』开始执行: " + "倒计时<"+i+"s>");
					Thread.sleep(1000);
				}
			}else {
				Thread.sleep(waitTime*1000);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * Web端隐式等待
	 * @param map
	 * @param step
	 */
	public void webImplicit(TestStep step){
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		long waitTime = Long.valueOf(step.getValue());
		step.getWebDriver().manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}
	
	/**
	 * Web端显示等待
	 * @param map
	 * @param step
	 */
	public void webDisplay(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");

		String loc = step.getLocator();
		if (StringUtil.isBlank(loc)) throw new Exception("当前步骤未定位到任何控件元素！");
		int idx1 = loc.indexOf("=");
		String locatename = loc.substring(0, idx1);
		String locatevalue = loc.substring(idx1 + 1);
		locatevalue = SeleniumUtil.parseStringHasEls(locatevalue);

		WebDriverWait wait = new WebDriverWait(step.getWebDriver(), 10,500);
	    //通过wait.until里面的方法找到元素
		if(locatename.equals("xpath")){
			// 等待元素是否被加载在DOM中，并不代表该元素一定可见
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatevalue)));
			// 等待元素是否可见（非隐藏，并且元素的宽和高都不等以0）
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatevalue)));
		}
//	    WebElement weekelement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatevalue)));
//	    //执行点击或者输入值操作
//	    weekelement.click();
	}

	/**
	 * Android端隐式等待
	 * @param map
	 * @param step
	 */
	public void androidImplicit(TestStep step){
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		long waitTime = Long.valueOf(step.getValue());
		step.getAndroidDriver().manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);	
	}
	
	/**
     * 在给定的时间内去查找元素，如果没找到则超时，抛出异常
     * */
    public static void waitForElementToLoad(WebDriver driver, int timeOut, final By By) {
        try {
            (new WebDriverWait(driver, timeOut)).until(new ExpectedCondition<Boolean>() {

                public Boolean apply(WebDriver driver) {
                    WebElement element = driver.findElement(By);
                    return element.isDisplayed();
                }
            });
        } catch (TimeoutException e) {
            Assert.fail("超时!! " + timeOut + " 秒之后还没找到元素 [" + By + "]");
        }
    }
    
  //定义一个显式等待元素定位的方法,presenceOfElementLocated方法能保证元素出现在dom树上，但不能保证可见
    public WebElement getElement(TestStep step,By by,long outtime){
        //显式等待,new WebDriverWait 里面要传一个驱动对象，然后是超时时间，以秒为单位，后面是轮询时间（默认是500毫秒），以毫秒为单位
        WebDriverWait wait = new WebDriverWait(step.getWebDriver(), outtime);
        try {
            WebElement weekelement = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return weekelement;
        } catch (Exception e) {
            System.out.println("定位元素超时");
            return null;
        }
    }
    //所以需要再加一个获取一个可见元素方法
    public WebElement getVibileElement(TestStep step,By by){
        //显式等待,new WebDriverWait 里面要传一个驱动对象，然后是超时时间，以秒为单位，后面是轮询时间（默认是500毫秒），以毫秒为单位
        WebDriverWait wait = new WebDriverWait(step.getWebDriver(), 30);
        try {
            WebElement weekelement = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return weekelement;
        } catch (Exception e) {
            System.out.println("定位元素超时");
            return null;
        }
    }

    //定义一个显式等待页面加载完成的方法
    public WebElement getpagereadyElement(TestStep step,By by){
        //显式等待,new WebDriverWait 里面要传一个驱动对象，然后是超时时间，以秒为单位，后面是轮询时间（默认是500毫秒），以毫秒为单位
        WebDriverWait wait = new WebDriverWait(step.getWebDriver(), 30);
        try {
            String jsToBeExecute = "return document.readyState == 'complete'";
            Boolean isready = (Boolean) wait.until(ExpectedConditions.jsReturnsValue(jsToBeExecute));
            if (isready){
                return getVibileElement(step,by);
            }
        } catch (Exception e) {
            System.out.println("页面加载超时");
        }
        return null;
    }
}
