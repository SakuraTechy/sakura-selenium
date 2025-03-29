package com.sakura.handler;

import org.apache.log4j.Logger;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import com.sakura.util.ConfigUtil;
import com.sakura.util.Constants;
import com.sakura.util.ExecuteShellUtil;
import com.sakura.util.SeleniumUtil;
import com.sakura.util.StringUtil;
import com.sakura.util.ExecuteSSHToolUtil;

/**
 * 连接Linux执行shell命令
 */
public class ExecuteShellHandler {
	Logger log = Logger.getLogger(getClass());
	
	public void exeShell(TestStep step) throws Exception{
//		String Device = step.getDevice();
		String Shell = SeleniumUtil.parseStringHasEls(step.getShell());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ Shell +"]");
		
		String host = ConfigUtil.getProperty(""+step.getDevice()+"_LinuxShell_IP", Constants.CONFIG_APP);
		int port = Integer.parseInt(ConfigUtil.getProperty(""+step.getDevice()+"_LinuxShell_Port", Constants.CONFIG_APP));
		String username = ConfigUtil.getProperty(""+step.getDevice()+"_LinuxShell_UserName", Constants.CONFIG_APP);
		String password = ConfigUtil.getProperty(""+step.getDevice()+"_LinuxShell_PassWord", Constants.CONFIG_APP);
		
		ExecuteSSHToolUtil executeShellUtil = new ExecuteSSHToolUtil();
    	executeShellUtil.init(host,port,username,password);
		String result = executeShellUtil.execCmd(Shell);
		if(step.getRegex()!=null) {
			result = StringUtil.getRegex(result,step.getRegex());
		}
		if(step.getKey()!=null) {
			result = StringUtil.replaceAllBlank(result, step.getKey(), step.getValue());
		}
		if(step.getDetails()!=null) {
    		SeleniumUtil.localmap.put(step.getDetails().get("key"), result);
    		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
    	}
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ Shell +"]");
	}
	
	public void exeShell1(TestStep step) throws Exception{
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "");
//		String Device = step.getDevice();
		String Shell = step.getShell();
		
		String host = ConfigUtil.getProperty(""+step.getDevice()+"_LinuxShell_IP", Constants.CONFIG_APP);
		int port = Integer.parseInt(ConfigUtil.getProperty(""+step.getDevice()+"_LinuxShell_Port", Constants.CONFIG_APP));
		String username = ConfigUtil.getProperty(""+step.getDevice()+"_LinuxShell_UserName", Constants.CONFIG_APP);
		String password = ConfigUtil.getProperty(""+step.getDevice()+"_LinuxShell_PassWord", Constants.CONFIG_APP);
		
		ExecuteShellUtil executeShellUtil = new ExecuteShellUtil(host,port,username,password);
		String result = executeShellUtil.executeForResult(Shell);
//		log.info(result);
		log.info("『正常测试』执行成功: " + "<" + result + ">");
		executeShellUtil.close();
	}
}
