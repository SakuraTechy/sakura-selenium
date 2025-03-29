package com.sakura.handler;

import java.io.*;
import java.net.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import com.sakura.util.AppiumUtil;
import com.sakura.util.ConfigUtil;
import com.sakura.util.Constants;
import com.sakura.util.DateUtil;
import com.sakura.util.PingUtil;
import com.sakura.util.SeleniumUtil;
import com.sakura.util.StringUtil;
import com.sakura.util.SystemUtil;

public class SetActionHandler {
	static Logger log = Logger.getLogger(SetActionHandler.class);

	/**
	 * <br>
	 * Web端设置值到全局</br>
	 * 
	 * @param step
	 * @throws Exception
	 */
	public void webSet(TestStep step) throws Exception {
		if (StringUtils.isBlank(step.getDetails().get("key")))
			throw new Exception("set操作必须设置保存结果的键值，供后续操作使用，例子为details='key:credit'");
//		String value = SeleniumUtil.getElement(step).getText().replace("初始密码：", "");
		String value = "";
        do {
            if (step.getLocator() != null) {
                step.setType("visibilityOfElementLocated");
                value = SeleniumUtil.getElement(step).getText();
            } else if (step.getScript() != null) {
                value = (String) ((JavascriptExecutor) step.getWebDriver()).executeScript(step.getScript());
            } else if (step.getValue() != null) {
				value = step.getValue();
			}
            if (step.getRegex() != null) {
                if (step.getState() != null && StringUtil.isEqual(step.getState(), "today")) {
//				value = StringUtil.getRegex(value, step.getRegex()).replace("-0", "-");
                    value = DateUtil.getDateFormat("yyyy-M-dd");
                } else {
                    value = StringUtil.getRegex(value, step.getRegex());
                }
            }
            if (step.getKey() != null && step.getKeys() != null) {
                value = value.replace(step.getKey(), step.getKeys());
            }
        } while (!StringUtil.isNoEmpty(value));
		SeleniumUtil.localmap.put(step.getDetails().get("key"), value);
		log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">["+ value +"]");
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ value +"]");
	}

	/**
	 * <br>
	 * Android端设置值到全局</br>
	 * 
	 * @param step
	 * @throws Exception
	 */
	public void androidSet(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" + step.getName() + ">" + AppiumUtil.localmap.toString());
		if (StringUtils.isBlank(step.getDetails().get("key")))
			throw new Exception("set操作必须设置保存结果的键值，供后续操作使用，例子为details='key:credit'！");
		String value = AppiumUtil.getElement(step).getText();
		if (step.getRegex() != null) {
			AppiumUtil.localmap.put(step.getDetails().get("key"), value);
		} else {
			AppiumUtil.localmap.put(step.getDetails().get("key"), value);
		}
//		log.info("『正常测试』开始执行: <成功记录到本地List列表，" +SeleniumUtil.localmap.toString() + ">");
	}

	/**
	 * <br>
	 * Web端获取日期设置值到全局</br>
	 * 
	 * @param step
	 * @throws Exception
	 */
	public void webSetdate(TestStep step) throws Exception {
		String date = "";
		if(step.getKey()!=null) {
			switch(step.getKey()) {
			case "获取当前时间戳":
				date = DateUtil.getTime(step.getValue());
				break;
			case "获取当天日期时间":
				date = DateUtil.getDateFormat(step.getValue());
				break;
			case "获取自定义时间":
				date = DateUtil.getDateFormat(step.getValue(),step.getScript());
				break;
			case "获取当月首天":
				date = DateUtil.getDateCalendar(step.getValue()).get("firstday");
				break;
			case "获取当月末天":
				date = DateUtil.getDateCalendar(step.getValue()).get("lastday");
				break;
			case "获取当年的第一天":
				date = DateUtil.formatDateTime(DateUtil.getCurrentFirstOfYear(), step.getValue());
				break;
			case "获取当年的最后一天":
				date = DateUtil.formatDateTime(DateUtil.getCurrentLastOfYear(), step.getValue());
				break;
			case "获取指定间隔开始时间":
				date = DateUtil.parseDateTime(step.getNeed_time(),Integer.parseInt(step.getInterval())).get("start_time");
				break;
			case "获取指定间隔结束时间":
				date = DateUtil.parseDateTime(step.getNeed_time(),Integer.parseInt(step.getInterval())).get("end_time");
				break;
			}
		}else {
			date = DateUtil.getDateFormat(step.getValue());
		}
		log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">["+ date +"]");
		SeleniumUtil.localmap.put(step.getDetails().get("key"), date);
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ date +"]");
	}

	/**
	 * <br>
	 * Web端获取系统信息设置值到全局</br>
	 * 
	 * @param step
	 * @throws Exception
	 */
	public void webSetsysinfo(TestStep step) throws Exception {
		String str = SystemUtil.getLocalHost(step.getKey(), step.getValue());
		log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">["+ str +"]");
		SeleniumUtil.localmap.put(step.getDetails().get("key"), str);
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ str +"]");
	}

	/**
	 * <br>
	 * Web端获取计算公式设置值到全局</br>
	 * 
	 * @param step
	 * @throws Exception
	 */
	public void webSetcalculationformula(TestStep step) throws Exception {
		String str = "";
		if(step.getKeys()!=null) {
			str = StringUtil.formatDouble(Double.parseDouble(StringUtil.ScriptEngine(step.getKey())), step.getKeys());
		}else {
			if(step.getValue().equals("CustomRules")) {
				str = StringUtil.CustomRules(StringUtil.calculateAndFormat(step.getKey()), Integer.parseInt(step.getDetails().get("scale")),Boolean.parseBoolean(step.getDetails().get("keepTrailingZeros")),Boolean.parseBoolean(step.getDetails().get("useGrouping")),Boolean.parseBoolean(step.getDetails().get("isUnit")));
			} else if(step.getValue().equals("formatSize")) {
				str = StringUtil.formatSize(StringUtil.calculateAndFormat(step.getKey()));
			}else {
				str = StringUtil.calculateAndFormat(step.getKey());
			}
		}
		log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + "["+ step.getKey() +"]>["+ str +"]");
		SeleniumUtil.localmap.put(step.getDetails().get("key"), str);
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "["+ step.getKey() +"]>["+ str +"]");
	}
	
	/**
	 * <br>
	 * Web端获取可用IP设置值到全局</br>
	 * 
	 * @param step
	 * @throws Exception
	 */
	public void webSetusableip(TestStep step) throws Exception {
//		String ip = step.getValue();
		String ip = SeleniumUtil.parseStringHasEls(step.getValue());
		int start = Integer.parseInt(step.getDetails().get("start"));
		int end = Integer.parseInt(step.getDetails().get("end"));

		for (int i = start; i <= end; i++) {
			if (PingUtil.ping(ip + i, 2, 1)) {
				log.info(ip + i + "：该地址已被占用，进入下次循环");
			} else {
				log.info(ip + i + "：该地址未被占用，可使用");
				ip = ip + i;
				break;
			}
		}
		log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">["+ ip +"]");
		SeleniumUtil.localmap.put(step.getDetails().get("key"), ip);
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ ip +"]");
	}
	
	public void webSetusableip1(TestStep step) throws UnknownHostException, IOException {
		log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">");
		String ip = step.getValue();
		int start = Integer.parseInt(step.getDetails().get("start"));
		int end = Integer.parseInt(step.getDetails().get("end"));

		for (int i = start; i <= end; i++) {
			InetAddress geek = InetAddress.getByName(ip + i);
			if (geek.isReachable(5000)) {
				log.info(ip + i + "：该地址已被占用，进入下次循环");
			} else {
				log.info(ip + i + "：该地址未被占用，可使用");
				ip = ip + i;
				break;
			}
		}
		SeleniumUtil.localmap.put(step.getDetails().get("key"), ip);
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ ip +"]");
	}

	/**
     * <br>Web端获取配置文件信息</br>
     *
     * @param step
     * @throws Exception
     */
    public void webSetproperties(TestStep step) throws Exception {
		String value = "";
		if(StringUtil.isEmpty(step.getKey())){
			value = ConfigUtil.getProperty(step.getValue(), Constants.CONFIG_APP);
		}else{
			value = ConfigUtil.getProperty(step.getValue(), Constants.CONFIG_JDBC);
		}
		log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">["+ value +"]");
		SeleniumUtil.localmap.put(step.getDetails().get("key"), value);
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ value +"]");
    }
    
	public static void main(String[] args) throws IOException {
		String ip = "172.19.5.";
		int start = 11;
		int end = 40;

//		for (int i = start; i <= end; i++) {
//			InetAddress geek = InetAddress.getByName(ip + i);
//			if (geek.isReachable(5000)) {
//				log.info(ip + i + "：该地址已被占用，进入下次循环");
//			} else {
//				log.info(ip + i + "：该地址未被占用，可使用");
//				ip = ip + i;
//				break;
//			}
//		}
		
		for (int i = start; i <= end; i++) {
			if (PingUtil.ping(ip + i, 2, 1)) {
				log.info(ip + i + "：该地址已被占用，进入下次循环");
			} else {
				log.info(ip + i + "：该地址未被占用，可使用");
				ip = ip + i;
				break;
			}
		}
		
		SeleniumUtil.localmap.put("dd", ip);
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
	}
}
