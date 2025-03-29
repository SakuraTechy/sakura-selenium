package com.sakura.handler;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sakura.util.ZipUtil;
import org.apache.log4j.Logger;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import com.sakura.util.FileUtil;
import com.sakura.util.SeleniumUtil;

/**
 * 上传本地文件到服务器
 */
public class FileAction_Handler {
	Logger log = Logger.getLogger(getClass());

	public void getFile(TestStep step) throws Exception{
		String value = SeleniumUtil.parseStringHasEls(step.getLocalpath());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ value +"]");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ value +"]");
		value = FileUtil.getFileList(value).get(0).get(step.getDetails().get("key")).toString();
		SeleniumUtil.localmap.put(step.getDetails().get("keys"), value);
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
	}
	
	public void getFiles(TestStep step) throws Exception{
		String value;
		if(step.getCatalogue()!=null) {
    		value = SeleniumUtil.parseStringHasEls(System.getProperty(step.getCatalogue())+step.getLocalpath());
    	}else {
    		value = SeleniumUtil.parseStringHasEls(System.getProperty("user.dir")+step.getLocalpath());
    	}
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ value +"]");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ value +"]");
		value = FileUtil.getFileList(value).get(0).get(step.getDetails().get("key")).toString();
		SeleniumUtil.localmap.put(step.getDetails().get("keys"), value);
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
	}
	
	public void deleteFile(TestStep step) throws Exception{
		String value = SeleniumUtil.parseStringHasEls(step.getLocalpath());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ value +"]");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ value +"]");
		if(step.getDelete()!=null) {
			FileUtil.deleteAllFile(value,step.getDelete());
    	}else {
    		FileUtil.deleteAllFile(value);
    	}
	}
	
	public void deleteFiles(TestStep step) throws Exception{
		String value = "";
		if(step.getCatalogue()!=null) {
    		value = SeleniumUtil.parseStringHasEls(System.getProperty(step.getCatalogue())+step.getLocalpath());
    	}else {
    		value = SeleniumUtil.parseStringHasEls(System.getProperty("user.dir")+step.getLocalpath());
    	}
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ value +"]");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + ">["+ value +"]");
		if(step.getDelete()!=null) {
			FileUtil.deleteAllFile(value,step.getDelete());
    	}else {
    		FileUtil.deleteAllFile(value);
    	}
	}

	public void zipFile(TestStep step) throws Exception{
		String localpath = "";
		String remotepath = "";
		String result = "";
		if(step.getCatalogue()!=null) {
			localpath = SeleniumUtil.parseStringHasEls(System.getProperty(step.getCatalogue())+step.getLocalpath());
			remotepath  = SeleniumUtil.parseStringHasEls(System.getProperty(step.getCatalogue())+step.getRemotepath());
		}else {
			localpath = SeleniumUtil.parseStringHasEls(step.getLocalpath());
			remotepath  = SeleniumUtil.parseStringHasEls(step.getRemotepath());
		}
		if(step.getType().equals("压缩文件")) {
			ZipUtil.toZip(localpath,remotepath);
			result = step.getRemotepath();
		}else if(step.getType().equals("解压文件")) {
			List<String> fileNames = ZipUtil.unzip(localpath,remotepath);
			if(step.getRegex()!=null) {
				// 正则表达式模式，用于匹配文件名中的序号
				Pattern pattern = Pattern.compile(step.getRegex());
				// 遍历文件名列表，查找指定序号的文件名
				for (String name : fileNames) {
					Matcher matcher = pattern.matcher(name);
					if (matcher.find()) {
						result = name;
					}
				}
			}else{
				result = fileNames.get(Integer.parseInt(step.getValue()));
			}
		}
		if(step.getDelete()!=null) {
			FileUtil.deleteAllFile(localpath,step.getDelete());
		}else {
			FileUtil.deleteAllFile(localpath);
		}
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">["+ result +"]");
		RunUnitService.Step.put("name", step.getId() + "." + step.getName() + ">["+ result +"]");
		SeleniumUtil.localmap.put(step.getDetails().get("key"), result);
		log.info("『正常测试』开始执行: <成功记录到本地List列表，" + SeleniumUtil.localmap.toString() + ">");
	}
}
