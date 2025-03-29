package com.sakura.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sakura.base.TestStep;
import com.sakura.service.AndroidXmlParseService;
import com.sakura.service.RunUnitService;

/**
 * <br>
 * 公用函数功能</br>
 *
 * @author 刘智
 * @date 2017年7月26日 上午10:27:26
 * @version 1.0
 * @since 1.0
 */
public class AppiumUtil {
    static Logger log = Logger.getLogger(AppiumUtil.class);
    
    /**
     * 用于保存不同步骤之间进行交互的值
     */
    public static Map<String, Object> localmap = new HashMap<>();
    static String state = "true";
    
    /**
     * <br>
     * 根据配置取界面上的元素，约定获取界面元素的描述如下：<br/>
     * <h1>属性名=属性值[索引]</h1><br/>
     * 如果属性可以唯一确定要获取的元素，则可以省略[索引]，例子如下：<br/>
     * class=android.widget.TextView[1]
     *
     * @author 102051
     * @date 2017年7月26日 下午3:55:57
     * @param step
     * @return
     * @throws Exception
     */
    public static WebElement getElement(TestStep step) throws Exception {
        WebElement a = null;
        String loc = step.getLocator();
        if (StringUtil.isBlank(loc))
            throw new Exception("当前步骤未定位到任何控件元素！");
        // 如果没有索引，则加上索引0
        if (loc.lastIndexOf("[") < 0)
            loc = loc + "[0]";

        int idx1 = loc.indexOf("="), idx2 = loc.lastIndexOf("[");
        if (idx1 == -1 || idx2 == -1 || idx2 < idx1)
            throw new Exception("step元素locator属性格式有误！");

        String locatename = loc.substring(0, idx1);
        String locatevalue = loc.substring(idx1 + 1, idx2);
        int locindex = Integer.valueOf(loc.substring(idx2 + 1, loc.length() - 1));

        // log.error(locatename+" "+locatevalue+" "+locindex);
        List<WebElement> e = null;
        try {
            switch (locatename) {
                case "resource-id":
                    e = step.getAndroidDriver().findElements(By.id(locatevalue));
                    break;
                case "text":
                    e = step.getAndroidDriver().findElementsByAndroidUIAutomator("new UiSelector().text(\"" + locatevalue + "\")");
                    break;
                case "class":
                    e = step.getAndroidDriver().findElements(By.className(locatevalue));
                    break;
                case "content-desc":
                    e = step.getAndroidDriver().findElementsByAccessibilityId(locatevalue);
                    break;
                case "xpath":
                    e = step.getAndroidDriver().findElements(By.xpath(locatevalue));
                    break;
                default:
                    throw new Exception("step元素locator属性配置有误，'='之前必须为resource-id、text、calss、content-desc和xpath之一！");
            }
            a = e.get(locindex);
            RunUnitService.Step.put("name","" + step.getId() + "." + step.getName()+ "");
        } catch (Exception b) {
            if(step.getSkip()==null){
                log.error("『发现问题』执行异常: " +"<" +step.getId() + "." +step.getName() + "> ==> 【未找到相关元素信息】");
                RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
                AndroidXmlParseService.screenShot(RunUnitService.testUnit.getName(), RunUnitService.testCase.getId(),
						"" + step.getId() + "." + step.getName() + "");
                RunUnitService.softAssert.fail("『发现问题』执行异常: " +"<" +step.getId() + "." +step.getName() + "> ==> 【未找到相关元素信息】");
            }else if(StringUtil.isEqual("true",step.getSkip())){
                log.error("『发现问题』执行异常: " +"<" +step.getId() + "." +step.getName() + "> ==> 【未找到相关元素信息】");
                RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
                RunUnitService.Step.put("picture","pass");
            }
//            try {
//                if(StringUtil.isEqual("false",step.getState())){
//                    log.info("『发现问题』执行异常: " +"<" +step.getId() + "." +step.getName() + "> ==> 【未找到相关元素信息】");
//                    RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
//                    RunUnitService.Step.put("picture","pass");
//                }
//            }catch (Exception c) {
//                log.info("『发现问题』执行异常: " +"<" +step.getId() + "." +step.getName() + "> ==> 【未找到相关元素信息】");
//                RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
//                AndroidXmlParseService.screenShot(TestUnit.getname(), TestCase.getid(), "" + step.getId() + "." + step.getName() + "");
//                RunUnitService.softAssert.fail("『发现问题』执行异常: " +"<" +step.getId() + "." +step.getName() + "> ==> 【未找到相关元素信息】");
//                c.printStackTrace();
//            }
//            b.printStackTrace();
            log.error("",b);
            Thread.sleep(500);
        }
        // return e==null?null:e.get(locindex);
        return a;
    }

    /**
     * <h1>解析包含EL表达式的字符串</h1></br>
     * 当前假设包含多个EL表达式的字符串中，</br>
     * 每一个EL表达式对应的是localmap中的值为String类型的键值对
     *
     * @author 102051
     * @date 2017年8月2日 上午11:43:43
     * @param str
     * @return
     * @throws Exception
     */
    public static String parseStringHasEls(String str) throws Exception {
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

            if (val instanceof List)
                throw new Exception("类型错误，字符串中的取值表达式的获取结果是一个 List 类型！");

            res = res.replace(substr, val.toString());
        } while (end < str.length());
        return res;
    }

    /**
     * <br>
     * 解析EL表达式，从Appium.localmap中取出对应的值</br>
     * 自定义的EL表达式的数据获取逻辑为：
     * <h1>key1[idx1].key2[idx2]</h1> 其中key1表示localmap中的键，如果对应于key1的值是List，则需要配置[idx1]，其他情况可不用配置</br>
     * key2[idx2]对应于上一步中取出的值，如果该值不是一个Map，则key2[idx2]无效，且报错</br>
     * 
     * 当前只localmap中只考虑三种类型的值：
     * <h1>String</h1>
     * <h1>List&lt;String></h1>
     * <h1>List&lt;Map&lt;String,Object>></h1></br>
     *
     * @author 102051
     * @date 2017年8月2日 下午2:43:54
     * @param str
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object parseEL(String str) throws Exception {
        // log.info("EL表达式： "+str);
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
            Object o = map.get(key.toUpperCase());

            // 处理索引
            if (index > -1) {
                if (o instanceof List) {
                    o = ((List)o).get(index);
                } else {
                    throw new Exception(st + "对应的值不是列表，索引无效！");
                }
            }

            // 判断最后一个元素
            if (i == s.length - 1) {
                return o;
            } else {
                try {
                    map = (Map)o;
                } catch (ClassCastException e) {
                    //e.printStackTrace();
                    log.error("",e);
                    throw new Exception(st + "对应的值不是键值对集合，无法继续获取值！");
                }
            }
        }
        return null;
    }
}

/*if(o == null){
	 throw new Exception("键"+key+"对应的值不存在！");
}else if(o instanceof Map){
	 
}else if(o instanceof List){//如果获取的值为List类型
	 if(index == -1){
		 if(i == s.length-1) return o;
		 else throw new Exception("当前数据是一个列表，请提供索引");
	 }
	Object m = ((List) o).get(index);
	if(m instanceof Map){
		 map = (Map)m;
		 if(i == s.length-1) return m;
	 }else{
		 if(i != s.length-1) throw new Exception("键及索引"+st+"对应的值类型不是Map,"+s[i=1]+"无效！");
		 return m;
	 }
}else{//如果获取的值为其他类型
	 if(index > 0) throw new Exception("键"+key+"对应的值类型不是List，索引值无效！");
	 if(i != s.length-1) throw new Exception("键"+key+"对应的值类型不是Map,"+s[i=1]+"无效！");
	 return o;
}*/