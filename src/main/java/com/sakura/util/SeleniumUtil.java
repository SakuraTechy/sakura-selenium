package com.sakura.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import com.sakura.service.WebXmlParseService;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * <br>
 * 公用函数功能</br>
 *
 * @author 刘智
 * @version 1.0
 * @date 2017年7月27日 下午4:07:18
 * @since 1.0
 */
public class SeleniumUtil {
    static Logger log = Logger.getLogger(SeleniumUtil.class);
    /**
     * 用于保存不同步骤之间进行交互的值
     */
    public static Map<String, Object> localmap = new LinkedHashMap<>();

    private static final String WebDriver_Wait = ConfigUtil.getProperty("WebDriver_Wait", ConstantsUtil.CONFIG_COMMON);

    /**
     * <br>
     * 根据配置取界面上的元素，约定获取界面元素的描述如下：<br/>
     * <h1>属性名=属性值[索引]</h1><br/>
     * 如果属性可以唯一确定要获取的元素，则可以省略[索引]，例子如下：<br/>
     * class=android.widget.TextView[1]
     *
     * @param step
     * @return
     * @throws Exception
     * @author 102051
     * @date 2017年7月26日 下午3:55:57
     */
    public static WebElement getElement1(TestStep step) throws Exception {
        String loc = step.getLocator();
        if (StringUtil.isBlank(loc))
            throw new Exception("当前步骤未定位到任何控件元素！");

        int idx1 = loc.indexOf("=");
        String locatename = loc.substring(0, idx1);
        String locatevalue = loc.substring(idx1 + 1);
        // log.info(locatename+" "+locatevalue);

        WebElement webElement = null;
        switch (locatename) {
            case "id":
                webElement = step.getWebDriver().findElement(By.id(locatevalue));
                break;
            case "name":
                webElement = step.getWebDriver().findElement(By.name(locatevalue));
                break;
            case "xpath":
                webElement = step.getWebDriver().findElement(By.xpath(locatevalue));
                break;
            case "tagName":
                webElement = step.getWebDriver().findElement(By.tagName(locatevalue));
                break;
            case "className":
                webElement = step.getWebDriver().findElement(By.className(locatevalue));
                break;
            case "linkText":
                webElement = step.getWebDriver().findElement(By.linkText(locatevalue));
                break;
            case "partialLinkText":
                webElement = step.getWebDriver().findElement(By.partialLinkText(locatevalue));
                break;
            case "cssSelector":
                webElement = step.getWebDriver().findElement(By.cssSelector(locatevalue));
                break;
            default:
                throw new Exception("step元素locator属性配置有误，'='之前必须为id、name和xpath之一！");
        }
        return webElement;
    }

    /**
     * <br>
     * 根据配置取界面上的元素，约定获取界面元素的描述如下：<br/>
     * <h1>属性名=属性值[索引]</h1><br/>
     * 如果属性可以唯一确定要获取的元素，则可以省略[索引]，例子如下：<br/>
     * class=android.widget.TextView[1]
     *
     * @param step
     * @return
     * @throws Exception
     * @author 102051
     * @date 2017年7月26日 下午3:55:57
     */
    public static WebElement getElement(TestStep step) throws Exception {
//		String loc = StringUtil.isNoEmpty(step.getLocator()) ? step.getLocator() : step.getValue();
        String loc = step.getLocator();
        if (StringUtil.isBlank(loc))
            throw new Exception("当前步骤未定位到任何控件元素！");

        int idx1 = loc.indexOf("=");
        String locatename = loc.substring(0, idx1);
        String locatevalue = loc.substring(idx1 + 1);
        if (StringUtil.isNoEmpty(step.getScript())){
            String script = StringUtil.formatNumber(Double.parseDouble(StringUtil.calculateAndFormat(step.getScript())), Integer.parseInt(step.getDetails().get("scale")),Boolean.parseBoolean(step.getDetails().get("keepTrailingZeros")),Boolean.parseBoolean(step.getDetails().get("useGrouping")));
            SeleniumUtil.localmap.put(step.getDetails().get("key"), script);
            log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
        }
        locatevalue = parseStringHasEls(locatevalue);
        // log.error(locatename+" "+locatevalue+" "+locindex);
        WebElement webElement = null;
        // 创建 WebDriverWait 对象，设置最大等待时间为 60s
//		WebDriverWait wait = new WebDriverWait(step.getWebDriver(), Long.parseLong(WebDriver_Wait));

        step.setWebDriverWait(new WebDriverWait(step.getWebDriver(), Long.parseLong(WebDriver_Wait)));
        if (StringUtil.isNoEmpty(step.getTimeout())) {
            step.setWebDriverWait(new WebDriverWait(step.getWebDriver(), Long.parseLong(step.getTimeout())));
        }
        if (StringUtil.isNoEmpty(step.getSkip()) && StringUtil.isEqual("locator", step.getSkip())||StringUtil.isEqual("expect", step.getSkip())) {
            step.setWebDriverWait(new WebDriverWait(step.getWebDriver(), 1));
        }
        WebDriverWait wait = step.getWebDriverWait();

        // 判断定义元素是否加载到 DOM
//		if(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.modal-content"))) != null){
//			// 等待指定的遮挡元素消失
//			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.modal-content")));
//			// 等待所有指定的遮挡元素消失
////			wait.until(ExpectedConditions.invisibilityOfAllElements(step.getWebDriver().findElements(By.cssSelector("div.el-dialog__wrapper, div.loading-overlay, div.modal-content"))));
//		}
        /* 以下是 ExpectedConditions 类中一些常用方法的注释说明
         * https://blog.51cto.com/u_16099166/11054569
         * presenceOfElementLocated(By)：判断该元素是否被加载在DOM中，并不代表该元素一定可见。
         * visibilityOfElementLocated(By)：判断该元素是否可见（即元素存在且可见）。
         * invisibilityOfElementLocated(By)：判断该元素是否不可见或不存在。
         * elementToBeClickable(By)：判断该元素是否可点击（即元素存在且可见且可点击）。
         * textToBePresentInElementLocated(By, String)：判断指定文本是否出现在元素中。
         * textToBePresentInElementValue(By, String)：判断指定文本是否出现在元素的值属性中。
         * alertIsPresent()：判断是否存在alert弹窗。
         * frameToBeAvailableAndSwitchToIt(By)：判断frame是否可用，并切换到该frame。
         * stalenessOf(WebElement)：判断元素是否已经从DOM中移除。
         * numberOfWindowsToBe(int)：判断窗口数量是否为指定值。
         * titleIs(String)：判断页面标题是否为指定值。
         * titleContains(String)：判断页面标题是否包含指定文本。
         * urlContains(String)：判断当前URL是否包含指定文本。
         * urlToBe(String)：判断当前URL是否为指定值。
         * urlMatches(String)：判断当前URL是否匹配正则表达式。
         */
        try {
            switch (locatename) {
                case "id":
//				webElement = step.getWebDriver().findElement(By.id(locatevalue));
                    webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatevalue)));
                    break;
                case "name":
//				webElement = step.getWebDriver().findElement(By.name(locatevalue));
                    webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatevalue)));
                    break;
                case "xpath":
//				webElement = step.getWebDriver().findElement(By.xpath(locatevalue));
                    webElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatevalue)));
                    if (StringUtil.isNoEmpty(step.getType())) {
                        String attribute = "";
                        String expect = "";
                        switch (step.getType()) {
                            case "presenceOfElementLocated":
                                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatevalue)));
                                break;
                            case "visibilityOfElementLocated":
                                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatevalue)));
                                break;
                            case "invisibilityOfElementLocated":
                                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locatevalue)));
                                break;
                            case "elementToBeClickable":
                                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locatevalue)));
                                break;
                            case "textToBe":
                                expect = SeleniumUtil.parseStringHasEls(step.getExpect());
                                wait.until(ExpectedConditions.textToBe(By.xpath(locatevalue), expect));
                                break;
                            case "textToBePresentInElementLocated":
                                expect = SeleniumUtil.parseStringHasEls(step.getExpect());
                                wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(locatevalue), expect));
                                break;
                            case "textToBePresentInElementValue":
                                expect = SeleniumUtil.parseStringHasEls(step.getExpect());
                                wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath(locatevalue), expect));
                                break;
                            case "attributeToBe":
                                attribute = SeleniumUtil.parseStringHasEls(step.getValue());
                                expect = SeleniumUtil.parseStringHasEls(step.getExpect());
                                wait.until(ExpectedConditions.attributeToBe(By.xpath(locatevalue), attribute, expect));
                                break;
                            default:
                                webElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatevalue)));
                        }
                    }
//				if(StringUtil.isNoEmpty(step.getExpect())){
//					String expect = SeleniumUtil.parseStringHasEls(step.getExpect());
//					wait.until(ExpectedConditions.textToBe(By.xpath(locatevalue), expect));
//				}
                    break;
                case "tagName":
//				webElement = step.getWebDriver().findElement(By.tagName(locatevalue));
                    webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(locatevalue)));
                    break;
                case "className":
//				webElement = step.getWebDriver().findElement(By.className(locatevalue));
                    webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(locatevalue)));
                    break;
                case "linkText":
//				webElement = step.getWebDriver().findElement(By.linkText(locatevalue));
                    webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(locatevalue)));
                    break;
                case "partialLinkText":
//				webElement = step.getWebDriver().findElement(By.partialLinkText(locatevalue));
                    webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(locatevalue)));
                    break;
                case "cssSelector":
//				webElement = step.getWebDriver().findElement(By.cssSelector(locatevalue));
                    webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locatevalue)));
                    break;
                default:
                    throw new Exception("step元素locator属性配置有误，'='之前必须为id、name和xpath之一！");
            }
        } catch (Exception e) {
//            log.error(e.getMessage());
            if (StringUtil.isEqual("locator", step.getSkip())) {
                log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 【元素定位信息异常，目前已忽略跳过，请仔细检查测试脚本..】");
                RunUnitService.Step.put("name", step.getId() + "." + step.getName() + "==> 【元素定位信息异常，目前已忽略跳过，请仔细检查测试脚本..】");
                RunUnitService.Step.put("skip", "true");
                RunUnitService.stepSkip++;
            }else if (StringUtil.isNotEqual("expect", step.getSkip())) {
                log.error("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 元素定位信息异常：【" + locatevalue + "】");
                RunUnitService.Step.put("name", step.getId() + "." + step.getName() + ">【" + locatevalue + "】");
                RunUnitService.Step.put("skip", "false");
                RunUnitService.softAssert.fail("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + "==> 元素定位信息异常：【" + locatevalue + "】");
                RunUnitService.stepFail++;
            }
            WebXmlParseService.screenShot(RunUnitService.testUnit.getId(), RunUnitService.testCase.getId(), "Step" + step.getId() + "." + step.getName()+"_"+step.getMessage());
            log.error("异常日志：",e);
        }
        return webElement;
    }

    /**
     * <h1>解析包含EL表达式的字符串</h1></br>
     * 当前假设包含多个EL表达式的字符串中，</br>
     * 每一个EL表达式对应的是localmap中的值为String类型的键值对
     *
     * @param str
     * @return
     * @throws Exception
     * @author 102051
     * @date 2017年8月2日 上午11:43:43
     */
    public static String parseStringHasEls1(String str) throws Exception {
        int start = -1, end = -1;
        String res = str;
        do {
            start = str.indexOf("${", end);
            end = str.indexOf("}", start);
            if (end == -1 || start == -1)
                break;

            String substr = str.substring(start, end + 1);
            // 解析EL表达式
            Object val = parseEL(substr);

            if (val instanceof List) {
//				throw new Exception("类型错误，字符串中的取值表达式的获取结果是一个 List 类型！");
            }
            res = res.replace(substr, val.toString());
        } while (end < str.length());
        return res;
    }

    public static String parseStringHasEls(String str) throws Exception {
        if (str == null || !str.contains("${") || !str.contains("}")) {
            return str;
        }

        int start = -1, end = -1;
        StringBuilder resBuilder = new StringBuilder(str);
        do {
            start = resBuilder.indexOf("${", end);
            if (start == -1) {
                break;
            }

            end = findMatchingEnd(resBuilder, start);
            if (end == -1) {
                throw new IllegalArgumentException("未找到匹配的结束符 '}'");
            }

            String substr = resBuilder.substring(start, end + 1);
            // 解析EL表达式
            Object val = parseEL(substr);

            if (val instanceof List) {
                // 将 List 转换为字符串，包括方括号
                List<?> listVal = (List<?>) val;
                String listStr = listVal.toString();
                resBuilder.replace(start, end + 1, listStr);
            } else {
                resBuilder.replace(start, end + 1, val.toString());
            }

            // 更新 end 位置，以便继续解析后续的表达式
            end = start + val.toString().length() - 1;
        } while (end < resBuilder.length());

        return resBuilder.toString();
    }

    private static int findMatchingEnd(StringBuilder str, int start) {
        int depth = 0;
        for (int i = start; i < str.length(); i++) {
            if (str.charAt(i) == '{') {
                depth++;
            } else if (str.charAt(i) == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1; // 没有找到匹配的结束符
    }

    /**
     * <br>
     * 解析EL表达式，从Appium.localmap中取出对应的值</br>
     * 自定义的EL表达式的数据获取逻辑为：
     * <h1>key1[idx1].key2[idx2]</h1>
     * 其中key1表示localmap中的键，如果对应于key1的值是List，则需要配置[idx1]，其他情况可不用配置</br>
     * key2[idx2]对应于上一步中取出的值，如果该值不是一个Map，则key2[idx2]无效，且报错</br>
     * <p>
     * 当前只localmap中只考虑三种类型的值：
     * <h1>String</h1>
     * <h1>List&lt;String></h1>
     * <h1>List&lt;Map&lt;String,Object>></h1></br>
     *
     * @param str
     * @return
     * @throws Exception
     * @author 102051
     * @date 2017年8月2日 下午2:43:54
     */
    public static Object parseEL(String str) throws Exception {
    // log.info("EL表达式： " + str);
    // 去除 ${ 和 }
    String temp = str.trim().substring(2, str.length() - 1);
    String[] s = temp.split("\\.");

    Map<String, Object> map = localmap;
    String key;
    int index = -1, idp = -1;

    for (int i = 0; i < s.length; i++) {
        String st = s[i];
        index = -1;
        idp = st.lastIndexOf("[");
        key = st;
        if (idp > 0) {
            index = Integer.valueOf(st.substring(idp + 1, st.length() - 1));
            key = st.substring(0, idp);
        }
        Object o = map.get(key);

        // 处理索引
        if (index > -1) {
            if (o instanceof List<?>) {
                try {
                    o = ((List<?>) o).get(index);
                } catch (IndexOutOfBoundsException e) {
                    log.error("索引 " + index + " 超出列表范围", e);
                    throw new IndexOutOfBoundsException();
                }
            } else {
                log.error(st + "对应的值不是列表，索引无效！");
                throw new IllegalArgumentException(st + "对应的值不是列表，索引无效！");
            }
        }

        // 判断最后一个元素
        if (i == s.length - 1) {
            if (o == null) {
                log.error(st + "：未能找到有效的返回值");
                throw new IllegalStateException(st + "：未能找到有效的返回值");
            }
            return o;
        } else {
            if (o instanceof Map) {
                map = (Map<String, Object>) o;
            } else if (o instanceof String) {
                try {
                    String jsonString = (String) o;
                    Gson gson = new Gson();
                    Type type = new TypeToken<Map<String, Object>>() {}.getType();
                    map = gson.fromJson(jsonString, type);
                    log.info("解析 JSON 后的 Map: " + map);
                } catch (Exception jsonException) {
                    log.error("对应的值不是Json，无法继续获取值！", jsonException);
                    throw new Exception(st + "对应的值不是Json，无法继续获取值！", jsonException);
                }
            } else {
                log.error("对应的值不是键值对Map集合，无法继续获取值！");
                throw new Exception(st + "对应的值不是键值对Map集合，无法继续获取值！");
            }
        }
    }
    throw new IllegalStateException("未能找到有效的返回值");
}


    public static Object parseEL1(String el) {
        // 模拟解析EL表达式
        if (el.equals("${AAS_DM_Y_Oracle_IP}")) {
            return "192.168.1.1";
        } else if (el.equals("${AAS_DM_Y_Oracle_Port}")) {
            return "1521";
        }
        return null;
    }

    public static void main(String[] args) {
        String str = "${AAS_DM_Y_Oracle_IP}_${AAS_DM_Y_Oracle_Port}_Oracle_数据源";
        try {
            String result = parseStringHasEls(str);
            System.out.println(result); // 输出: 192.168.1.1_1521_Oracle_数据源
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
 * if(o == null){ throw new Exception("键"+key+"对应的值不存在！"); }else if(o instanceof
 * Map){
 *
 * }else if(o instanceof List){//如果获取的值为List类型 if(index == -1){ if(i ==
 * s.length-1) return o; else throw new Exception("当前数据是一个列表，请提供索引"); } Object m
 * = ((List) o).get(index); if(m instanceof Map){ map = (Map)m; if(i ==
 * s.length-1) return m; }else{ if(i != s.length-1) throw new
 * Exception("键及索引"+st+"对应的值类型不是Map,"+s[i=1]+"无效！"); return m; }
 * }else{//如果获取的值为其他类型 if(index > 0) throw new
 * Exception("键"+key+"对应的值类型不是List，索引值无效！"); if(i != s.length-1) throw new
 * Exception("键"+key+"对应的值类型不是Map,"+s[i=1]+"无效！"); return o; }
 */
