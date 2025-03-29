package com.sakura.handler;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import com.sakura.util.DateUtil;
import com.sakura.util.SeleniumUtil;
import com.sakura.util.StringUtil;
import com.sakura.util.CommandUtil;

public class WindowsSystemHandler {
	Logger log = Logger.getLogger(WindowsSystemHandler.class);
	
	private Robot robot;
	
	/**
	 * <br>模拟点击Windows系统键盘普通按键,例如：Home键</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void windowsKeybg(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		Actions action = new Actions(step.getWebDriver()); 
	    action.sendKeys(Keys.valueOf(step.getKey())).perform();
	}
	
	/**
	 * <br>模拟点击Windows系统键盘组合按键,例如：Ctrl+W</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void windowsKeybc(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		Actions action = new Actions(step.getWebDriver()); 
		action.keyDown(Keys.valueOf(step.getKey())).sendKeys(step.getValue()).perform();
	}
	
	/**
	 * <br>模拟点击Windows系统键盘特殊组合按键,例如：Ctrl+Tab</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void windowsSkeybc(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		Actions action = new Actions(step.getWebDriver()); 
		action.keyDown(Keys.valueOf(step.getKey())).sendKeys(Keys.valueOf(step.getKeys())).keyUp(Keys.valueOf(step.getKey())).sendKeys(Keys.valueOf(step.getKeys())).perform();
	}
	
	/**
	 * <br>模拟点击Windows系统键盘多重特殊组合按键,例如：Ctrl+Shift+K</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void windowsSkeybcm(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		Actions action = new Actions(step.getWebDriver()); 
		action.keyDown(Keys.valueOf(step.getKey())).sendKeys(Keys.valueOf(step.getKeys())).sendKeys(step.getValue()).keyUp(Keys.valueOf(step.getKey())).sendKeys(Keys.valueOf(step.getKeys())).sendKeys(step.getValue()).perform();
	}
	
	/**
	 * <br>模拟浏览器页面刷新</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void refresh(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");

		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_F5);
	}
	
	/**
	 * <br>模拟执行Windows系统的cmd命令</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void windowsCmd(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
		CommandUtil.executeCommand(SeleniumUtil.parseStringHasEls(step.getValue()),null, new CommandUtil.SimpleResultHandler());
        Thread.sleep(500);
	}

	/**
	 * <br>模拟移动鼠标到指定位置x，y</br>
	 * https://blog.csdn.net/qq_46029070/article/details/126014028
	 * @param step
	 * @throws Exception
	 */
	public void mouseMove(TestStep step) throws Exception {
		log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">");
		RunUnitService.Step.put("name", "" + step.getId() + "." + step.getName() + "");
		
		int x = Integer.valueOf(step.getDetails().get("x")); 
	    int y = Integer.valueOf(step.getDetails().get("y"));
	    robot = new Robot();
		robot.mouseMove(x, y);
	}
	
	/**
	 * <br>模拟移动鼠标到指定位置x，y</br>
	 * https://blog.csdn.net/scut_yfli/article/details/107226221
	 * @param step
	 * @throws Exception 
	 */
	public void moveByoffset(TestStep step) throws Exception{ 
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");

		int x = Integer.valueOf(step.getDetails().get("x")); 
	    int y = Integer.valueOf(step.getDetails().get("y"));
	    
		Actions action = new Actions(step.getWebDriver());
	    action.moveByOffset(x,y);
	    action.perform();
	}
	
	/**
	 * <br>模拟移动鼠标到指定元素中间</br>
	 *
	 * @param step
	 * @throws Exception 
	 */
	public void moveToelement(TestStep step) throws Exception{
		//x=定位元素最右边到整体中心位置的距离
		int x = Integer.valueOf(step.getDetails().get("x")); 
	    int y = Integer.valueOf(step.getDetails().get("y"));
	    
	    Actions action = new Actions(step.getWebDriver());
	    WebElement drop = SeleniumUtil.getElement(step);
	    if(step.getState()!=null&&step.getValue()!=null&&StringUtil.isEqual(step.getState(), "today")) {
	    	int date = Integer.valueOf(DateUtil.getDateFormat("dd"));
	    	int spac = Integer.valueOf(step.getValue());
	    	int lastday =Integer.valueOf(DateUtil.getDateCalendar("dd").get("lastday"));
	    	int day = Integer.valueOf(StringUtil.formatDouble(lastday/2,"0"));
//	    	x=(x+date-15)*spac;
//	    	x=(date-31/2)*55-(55/2-12);
	    	log.info("『正常测试』开始执行: " + "计算公式<("+date+"-"+day+")*"+spac+"-("+spac+"/2+("+x+"))>");
	    	x=(date-day)*spac-(spac/2+x);
	    	action.moveToElement(drop, x, y);
		    action.perform();
	    }else {
	    	action.moveToElement(drop, x, y);
		    action.perform();
	    }
	    log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">[x:"+ x +";y:"+ y +"]");
        RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">[x:"+ x +";y:"+ y +"]");
	}

	public static void main(String[] args) {
		String command1 = "powershell -Command (Get-Content 'D:/Program/Agent/5.1.2/agent.cfg') | ForEach-Object { $_ -replace 'dst_ip=.*', 'dst_ip=172.19.5.45' } | Set-Content 'D:/Program/Agent/5.1.2/agent.cfg'";
		String command2 = "cmd /c cd D:\\Program\\Agent\\5.1.2 && \"Windows Agent.exe\"";
		String command3 = "wmic process where name='Windows Agent.exe' delete";
		String command = "ipconfig";

		// 调用executeCommand方法
		CommandUtil.executeCommand(command1, null, new CommandUtil.SimpleResultHandler());
	}
}
