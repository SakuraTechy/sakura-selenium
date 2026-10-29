package com.sakura.handler;

import com.sakura.util.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Objects;
import java.util.Locale;

/**
 * <br>
 * 处理界面上的点击操作，即如果当前操作为点击，<br/>
 * 则选中脚本中配置的节点，并执行点击操作</br>
 *
 * @author 刘智
 * @date 2017年7月26日 上午10:27:26
 * @version 1.0
 * @since 1.0
 */
public class ClickActionHandler {
    static Logger log = Logger.getLogger(ClickActionHandler.class);
    static String Click_Fail = ConfigUtil.getProperty("Click_Fail", ConstantsUtil.CONFIG_COMMON);
    /**
     * <br>Web端点击操作</br>
     *
     * @param step
     * @throws Exception
     */
    public static WebElement webClick(TestStep step) throws Exception {
        WebElement webElement = null;
        String test = "";
        int maxRetries = Integer.parseInt(Click_Fail); // 最大重试次数
        int retryCount = 1;
        String loc = step.getLocator();
        int idx1 = loc.indexOf("=");
        String locatename = loc.substring(0, idx1);
        String locatevalue = loc.substring(idx1 + 1);
        locatevalue = SeleniumUtil.parseStringHasEls(locatevalue);
        while (true) {
            try {
                step.setType("elementToBeClickable");
                webElement = SeleniumUtil.getElement(step);
                test = webElement.getText();
                // 配置了状态条件时只允许匹配状态的点击；未配置条件的旧步骤保持原有行为。
                if (!shouldClick(step, webElement)) {
                    RunUnitService.Step.put("skip", "true");
                    RunUnitService.stepSkip++;
                    log.info("『正常测试』元素状态不满足点击条件，跳过点击: <" + step.getId() + "." + step.getName() + ">[" + test + "]");
                    return webElement;
                }
                webElement.click();
                break;
            } catch (Exception e) {
                if(StringUtil.isEqual(step.getSkip(),"locator")){
                    break;
                }else{
                    log.error("点击失败，重试次数: " + retryCount, e);
                    log.info("『发现问题』执行异常: "+ "<" +step.getId() + "." + step.getName()+"_元素[" + test + "]定位异常导致失败，重新点击>[重试次数:" + retryCount + "]");
//                RunUnitService.Step.put("name", step.getId() + ".元素[" + test + "]定位异常导致失败，重新点击>[重试次数:" + (retryCount + 1) + "]");
                    if (retryCount >= maxRetries) {
//                    log.error("超出最大重试次数，放弃点击");
                        log.info("『发现问题』执行异常: "+ "<" +step.getId() +"." + step.getName()+ "_元素[" + test + "]定位异常导致失败，重新点击>[超出最大重试次数:"+maxRetries+"，放弃点击]");
                        RunUnitService.Step.put("name", step.getId() +"." + step.getName()+ "_元素[" + test + "]定位异常导致失败，重新点击>[超出最大重试次数:"+maxRetries+"，放弃点击]");
                        RunUnitService.softAssert.fail("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 元素定位信息异常，并超出最大重试次数：【" + locatevalue + "】");
//                    throw e; // 超出最大重试次数后抛出异常
                        break;
                    }
                    retryCount++;
                }
            }
        }
        retryCount = 1;
        while (true) {
            if (StringUtil.isNoEmpty(step.getInvisible())&&step.getInvisible().equals("true")){
                step.setName("检查元素[" + test + "]是否消失，否则重新执行点击操作");
                step.setMessage("检查元素[" + test + "]是否消失，否则重新执行点击操作");
                if(CheckActionHandler.webCheckInvisible(step)) {
                    break;
                }else {
                    log.info("『发现问题』执行异常: "+ "<" +step.getId() +"." + step.getName()+ "_元素[" + test + "]未消失导致失败，重新点击>[重试次数:" + retryCount + "]");
                    if (retryCount >= maxRetries) {
                        log.info("『发现问题』执行异常: "+ "<" +step.getId() +"." + step.getName()+ "_元素[" + test + "]未消失导致失败，重新点击>[超出最大重试次数:"+retryCount+"，放弃点击]");
                        RunUnitService.Step.put("name", step.getId() +"." + step.getName()+ "_元素[" + test + "]未消失导致失败，重新点击>[超出最大重试次数:"+retryCount+"，放弃点击]");
                        break;
                    }
                    retryCount++;
                    webElement.click();
                }
            }else {
                break;
            }
        }
        log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">[" + test + "]");
        RunUnitService.Step.put("name", step.getId() + "." + step.getName() + ">[" + test + "]");
        return webElement;
    }

    private static boolean shouldClick(TestStep step, WebElement element) {
        String condition = step.getDetails() == null ? "" : step.getDetails().get("click_when");
        if (condition == null || condition.trim().isEmpty() || "always".equalsIgnoreCase(condition)
            || "any".equalsIgnoreCase(condition) || "none".equalsIgnoreCase(condition)) {
            return true;
        }
        String expected = condition.trim().toLowerCase(Locale.ROOT);
        if ("closed".equals(expected) || "unchecked".equals(expected) || "inactive".equals(expected)) expected = "off";
        if ("opened".equals(expected) || "checked".equals(expected) || "active".equals(expected)) expected = "on";
        if ("exists".equals(expected) || "present".equals(expected) || "element_present".equals(expected)
                || "if_exists".equals(expected) || "element_exists".equals(expected)) {
            String conditionLocator = step.getDetails() == null ? "" : step.getDetails().get("click_condition_locator");
            if (conditionLocator == null || conditionLocator.trim().isEmpty()) return false;
            try {
                // 存在性条件只检查 DOM 是否有元素，不要求条件元素可见。
                return !step.getWebDriver().findElements(SeleniumUtil.resolveLocator(conditionLocator)).isEmpty();
            } catch (RuntimeException e) {
                return false;
            }
        }
        if (!"on".equals(expected) && !"off".equals(expected)) return false;
        return expected.equals(readElementState(element));
    }

    private static String readElementState(WebElement element) {
        for (String attribute : new String[] {"checked", "aria-checked", "aria-pressed", "data-state", "data-checked", "data-on", "data-status"}) {
            String value = element.getAttribute(attribute);
            if ("checked".equals(attribute) && value != null) return "on";
            String state = stateFromValue(value);
            if (!state.isEmpty()) return state;
        }
        String text = element.getText();
        String state = stateFromValue(text);
        if (!state.isEmpty()) return state;
        return stateFromValue(element.getAttribute("class"));
    }

    private static String stateFromValue(String rawValue) {
        String value = rawValue == null ? "" : rawValue.trim().toLowerCase(Locale.ROOT);
        if (value.matches(".*\\b(on|open|opened|active|checked|selected|enabled)\\b.*") || value.matches(".*(开启|打开|已开启|选中|启用).*")) return "on";
        if (value.matches(".*\\b(off|close|closed|inactive|unchecked|unselected|disabled)\\b.*") || value.matches(".*(关闭|未开启|未选中|禁用).*")) return "off";
        return "";
    }

    public static WebElement webClicks(TestStep step) throws Exception {
//        step.setType("");
        WebElement webElement = SeleniumUtil.getElement(step);
        String name = step.getName();
        String test = webElement.getText();
        log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + name + ">[" + test + "]");
        RunUnitService.Step.put("name", step.getId() + "." + name + ">[" + test + "]");
        int maxRetries = Integer.parseInt(Click_Fail); // 最大重试次数
        int retryCount = 1;
        while (true) {
            try {
                webElement.click();
                break;
            } catch (Exception e) {
                if(StringUtil.isEqual(step.getSkip(),"locator")){
                    break;
                }else{
                    log.error("点击失败，重试次数: " + retryCount, e);
                    log.info("『发现问题』执行异常: "+ "<" +step.getId() + "." + step.getName()+"_元素[" + test + "]定位异常导致失败，重新点击>[重试次数:" + retryCount + "]");
//                RunUnitService.Step.put("name", step.getId() + ".元素[" + test + "]定位异常导致失败，重新点击>[重试次数:" + (retryCount + 1) + "]");
                    if (retryCount >= maxRetries) {
//                    log.error("超出最大重试次数，放弃点击");
                        log.info("『发现问题』执行异常: "+ "<" +step.getId() +"." + step.getName()+ "_元素[" + test + "]定位异常导致失败，重新点击>[超出最大重试次数:"+maxRetries+"，放弃点击]");
                        RunUnitService.Step.put("name", step.getId() +"." + step.getName()+ "_元素[" + test + "]定位异常导致失败，重新点击>[超出最大重试次数:"+maxRetries+"，放弃点击]");
//                    throw e; // 超出最大重试次数后抛出异常
                        break;
                    }
                    retryCount++;
                }
            }
        }
        retryCount = 1;
        while (true) {
            if (StringUtil.isNoEmpty(step.getInvisible())&&step.getInvisible().equals("true")){
                step.setName("检查元素[" + test + "]是否消失，否则重新执行点击操作");
                step.setMessage("检查元素[" + test + "]是否消失，否则重新执行点击操作");
                if(CheckActionHandler.webCheckInvisible(step)) {
                    break;
                }else {
                    log.info("『发现问题』执行异常: "+ "<" +step.getId() +"." + step.getName()+ "_元素[" + test + "]未消失导致失败，重新点击>[重试次数:" + retryCount + "]");
                    if (retryCount >= maxRetries) {
                        log.info("『发现问题』执行异常: "+ "<" +step.getId() +"." + step.getName()+ "_元素[" + test + "]未消失导致失败，重新点击>[超出最大重试次数:"+retryCount+"，放弃点击]");
                        RunUnitService.Step.put("name", step.getId() +"." + step.getName()+ "_元素[" + test + "]未消失导致失败，重新点击>[超出最大重试次数:"+retryCount+"，放弃点击]");
                        break;
                    }
                    retryCount++;
                    webElement.click();
                }
            }else {
                break;
            }
        }
        return webElement;
    }

    /**
	 * <br>Web端选项框点击操作</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void selectClick(TestStep step) throws Exception{ 
//		WebDriver driver = step.getWebDriver();
//        step.setType("elementToBeClickable");
//		WebElement selectElem = SeleniumUtil.getElement(step);
////        Actions actions = new Actions(driver);
////        actions.moveToElement(selectElem).click().perform();
//        selectElem.click();
////        Thread.sleep(Long.valueOf("3000"));
////        Thread.sleep(1000);
////        WebElement e = step.getWebDriver().findElement(By.xpath(step.getValue()));
//        step.setLocator(SeleniumUtil.parseStringHasEls(step.getValue()));
//        selectElem = SeleniumUtil.getElement(step);
//        log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ selectElem.getText() +"]");
//        RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ selectElem.getText() +"]");
//        selectElem.click();

        webClick(step);
        step.setLocator(SeleniumUtil.parseStringHasEls(step.getValue()));
        Thread.sleep(500);
        webClick(step);
    }

	/**
	 * <br>Web端选项框输入点击操作</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void inputClick(TestStep step) throws Exception{
//        step.setType("elementToBeClickable");
//        WebElement selectElem = SeleniumUtil.getElement(step);
//        selectElem.sendKeys(SeleniumUtil.parseStringHasEls(step.getValue()));
//		Thread.sleep(Long.valueOf("1000"));
//		Thread.sleep(1000);
//        WebElement e = step.getWebDriver().findElement(By.xpath(step.getElement()));
//        e.click();

//        step.setType("elementToBeClickable");
//        WebElement selectElem = SeleniumUtil.getElement(step);
//        selectElem.sendKeys(SeleniumUtil.parseStringHasEls(step.getValue()));
//        step.setLocator(SeleniumUtil.parseStringHasEls(step.getElement()));
//        selectElem = SeleniumUtil.getElement(step);
//        log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ selectElem.getText() +"]");
//        RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ selectElem.getText() +"]");
//        selectElem.click();

        InputActionHandler.webInput(step);
        step.setLocator(SeleniumUtil.parseStringHasEls(step.getElement()));
        webClick(step);
    }

    /**
     * <br>Web端检查某个元素存在则点击操作</br>
     *
     * @param step
     * @throws Exception
     */
    public void checkExistClick(TestStep step) throws Exception {
        step.setType("visibilityOfElementLocated");
        WebElement webElement = SeleniumUtil.getElement(step);
        if(Objects.nonNull(webElement)){
            step.setLocator(SeleniumUtil.parseStringHasEls(step.getElement()));
            webClick(step);
        };
    }

    /**
     * <br>Web端检查某个元素不存在则点击操作</br>
     *
     * @param step
     * @throws Exception
     */
    public void checkNotExistClick(TestStep step) throws Exception {
        step.setType("invisibilityOfElementLocated");
        WebElement webElement = SeleniumUtil.getElement(step);
        if(Objects.nonNull(webElement)){
            step.setLocator(SeleniumUtil.parseStringHasEls(step.getElement()));
            webClick(step);
        };
    }

    /**
     * <br>Web端检查某个元素相等则点击操作</br>
     *
     * @param step
     * @throws Exception
     */
    public void checkEqualsClick(TestStep step) throws Exception {
        if(CheckActionHandler.webCheck(step)){
            step.setLocator(SeleniumUtil.parseStringHasEls(step.getElement()));
            webClick(step);
        };
    }

    /**
     * <br>Web端检查某个元素不相等则点击操作</br>
     *
     * @param step
     * @throws Exception
     */
    public void checkNotEqualsClick(TestStep step) throws Exception {
        if(!CheckActionHandler.webCheck(step)){
            step.setLocator(SeleniumUtil.parseStringHasEls(step.getElement()));
            webClick(step);
        };
    }

	/**
	 * <br>Web端滚动到元素操作</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void scrollElement(TestStep step) throws Exception {
		WebDriver driver = step.getWebDriver();
        step.setType("elementToBeClickable");
		WebElement selectElem = SeleniumUtil.getElement(step);
		selectElem.click();
//        WebElement e = driver.findElement(By.xpath(step.getElement()));
        step.setLocator(SeleniumUtil.parseStringHasEls(step.getElement()));
        step.setType("presenceOfElementLocated");
        selectElem = SeleniumUtil.getElement(step);
        JavascriptExecutor js= (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)",selectElem);
        log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ selectElem.getText() +"]");
        RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ selectElem.getText() +"]");
        selectElem.click();
    }

    /**
     * <br>Web端点击浏览器弹出框的确定键</br>
     *
     * @param step
     * @throws Exception
     */
    public void clickOk(TestStep step) throws Exception {
        log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
        step.getWebDriver().switchTo().alert().accept();
    }

    /**
     * <br>Web端点击浏览器弹出框的取消键</br>
     *
     * @param step
     * @throws Exception
     */
    public void clickCancel(TestStep step) throws Exception {
        log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
        step.getWebDriver().switchTo().alert().dismiss();
    }

    /**
     * <br>Web端执行浏览器文本弹出框的文本输入</br>
     *
     * @param step
     * @throws Exception
     */
    public void clickText(TestStep step) throws Exception {
        log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
        step.getWebDriver().switchTo().alert().sendKeys(step.getValue());
    }

    /**
     * <br>
     * Android点击操作</br>
     *
     * @param step
     * @throws Exception
     */
    public void androidClick(TestStep step) throws Exception {
        // log.info(TestUnit.getid());
        // log.info(TestUnit.getname());
        // log.info(TestCase.getid());
        // log.info(TestCase.getname());
        try {
            log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">");
            AppiumUtil.getElement(step).click();
        } catch (Exception e) {
            // log.info("『发现问题』执行异常: " +"<" +step.getId() + "." +step.getName() + "> ==> 【未找到相关元素信息】");
            // RunUnitService.softAssert.fail("『发现问题』执行异常: " +"<" +step.getId() + "." +step.getName() + "> ==>
            // 【未找到相关元素信息】");
            // AndroidXmlParseService.screenShot(TestUnit.getname(),TestCase.getid(),"" +step.getId() + "."
            // +step.getName() + "");
             //e.printStackTrace();
             log.error("",e);
             Thread.sleep(500);
        }
    }
}
