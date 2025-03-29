package com.sakura.handler;

import java.util.Map;

import org.apache.log4j.Logger;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import com.sakura.util.HttpRequestUtil;
import com.sakura.util.SeleniumUtil;

/**
 * http请求动作处理类
 */
public class HttpRequestHandler {
	Logger log = Logger.getLogger(HttpRequestHandler.class);
	
	/**
	 *指定API接口URL,获取Cookie
	 * @param map
	 * @param step
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public void getCookie(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
//		String ApiUrl = ConfigUtil.getProperty("test.api."+step.getUrl()+"", Constants.CONFIG_COMMON);
		String ApiUrl = step.getUrl();
		String Param = "{\"userId\": \"666666\",\"password\": \"612426\",\"type\": \"string\",\"version\": \"string\",\"identification\": \"string\"}";
        Map<String, String> CookieVal =HttpRequestUtil.GetPostCookie(ApiUrl,Param);
	}
	
	/**
	 * 指定API接口URL,发送POST请求
	 * @param map
	 * @param step
	 * @throws Exception 
	 */
	public void sendPost(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
//		String ApiUrl = ConfigUtil.getProperty("test.api."+step.getUrl()+"", Constants.CONFIG_COMMON);
		String ApiUrl = step.getUrl();
		String Param = "{\""+step.getBody()+"\": \""+SeleniumUtil.parseStringHasEls(step.getValue())+"\"}";
         HttpRequestUtil.SendPost(ApiUrl,Param);
//         log.info("接口详情:" + ApiUrl+"  "+Param);
	}

}
