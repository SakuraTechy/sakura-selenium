package com.sakura.handler;

import org.apache.log4j.Logger;

import com.sakura.base.TestStep;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDeviceActionShortcuts;

public class AndroidSystemHandler {
    
    Logger log = Logger.getLogger(AndroidSystemHandler.class);
    
	/**
	 * <br>模拟点击Android系统键盘按键操作,例如：KEYCODE_HOME 3</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void androidKeycode(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
	       ((AndroidDeviceActionShortcuts) step.getAndroidDriver()).
	        	pressKeyCode(Integer.valueOf(step.getDetails().get("key")));
	            Thread.sleep(600);
	}
	
	/**
	 * <br>模拟点击Android系统返回按键操作,可返回多次</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void androidReturn(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		int Num = Integer.valueOf(step.getDetails().get("num"));
		for (int i = 0; i < Num; i++) {  
			step.getAndroidDriver().navigate().back(); 
        }
	}
	
	/**
	 * <br>模拟打开Androud系统通知栏操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void androidOpennb(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		step.getAndroidDriver().openNotifications();
	}
	
	/**
	 * <br>模拟执行Androud系统页面滚动操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void androidScroll(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		step.getAndroidDriver().scrollToExact(step.getValue()).click();
	}
	
	/**
	 * <br>模拟执行Androud系统页面滑动操作</br>
	 * 
	 * @param step
	 * @throws Exception 
	 */
	public void androidSwipe(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		TouchAction tAction = new TouchAction(step.getAndroidDriver());
		tAction.press(400,500).waitAction(800).moveTo(50,500).release().perform();
	}
	
	/**
	 * <br>Android-ADB指令模拟键盘操作</br>
	 * <step action="adb-keycode" locator="1ed814f6" value="3" name="模拟点击安卓键盘HOME键"/>
	 * keycode通过keycode表获取百度可查，home键的keycode=3，back键的keycode=4
	 * @param step
	 * @throws Exception
	 */
	public void adbKeycode(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		Runtime.getRuntime().exec("adb -s "+step.getLocator()+" shell input keyevent  "+ step.getValue());
	}
	
	/**
	 * <br>Android-ADB指令页面输入操作，先点击输入框</br>
	 * <step action="adb-click" locator="1ed814f6" value="艾丝凡" name="输入文本艾丝凡"/>
	 * @param step
	 * @throws Exception
	 */
	public void adbInput(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		Runtime.getRuntime().exec("adb -s "+step.getLocator()+" shell input text  "+ step.getValue());
	}
	
	/**
	 * <br>Android-ADB指令页面坐标点击操作</br>
	 * <step action="adb-click" locator="1ed814f6" details="x:500;y:500" name="点击页面500，500坐标位置"/>
	 * @param step
	 * @throws Exception
	 */
	public void adbClick(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		Runtime.getRuntime().exec("adb -s "+step.getLocator()+" shell input tap  "+ step.getDetails().get("x") +" "+ step.getDetails().get("y"));
	}
	
	/**
	 * <br>Android-ADB指令页面滑动操作</br>
	 * <step action="adb-swipe" locator="1ed814f6" details="x1:500;y1:500;x2:100;y2:500" name="页面向左滑动200"/>
	 * @param step
	 * @throws Exception
	 */
	public void adbSwipe(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		Runtime.getRuntime().exec("adb -s "+step.getLocator()+" shell input swipe "+ step.getDetails().get("x1") +" "+ step.getDetails().get("y1")+ step.getDetails().get("x2") +" "+ step.getDetails().get("y2"));
	}
	
	/**
	 * <br>Android-ADB切换输入法操作</br>
	 * <step action="adb-ime" value="io.appium.android.ime/.UnicodeIME" name="切换Appium输入法"/>
	 * 通过adb shell ime list -s 命令，获取value值
	 * @param step
	 * @throws Exception
	 */
	public void adbIme(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		Runtime.getRuntime().exec("adb -s "+step.getLocator()+" shell ime set "+ step.getValue());
	}
}
