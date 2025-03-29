package com.sakura.util;

import java.awt.Robot;

import org.apache.log4j.Logger;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;

public class MouseUtil {
	Logger log = Logger.getLogger(MouseUtil.class);

	private Robot robot;

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
}
