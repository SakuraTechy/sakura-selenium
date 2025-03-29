package com.sakura.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.sakura.util.*;
import org.apache.bcel.generic.SWITCH;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;

public class DBActionHandler {
    static Logger log = Logger.getLogger(DBActionHandler.class);
    
	public void dbInserta(TestStep step) throws Exception{
		String sql = AppiumUtil.parseStringHasEls(step.getSql());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">【"+ sql +"】");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "【"+ sql + "】");
		int n = DBHelper.insert(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(), step.getServer(), step.getDatabase(),sql);
		if(n > 0){
		    log.info("『执行成功』已插入"+n+"条数据");
		}
	}
	
	public void dbInsertw(TestStep step) throws Exception{
		String sql = SeleniumUtil.parseStringHasEls(step.getSql());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">【"+ sql +"】");
	    RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "【"+ sql + "】");
        int n = DBHelper.insert(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(),step.getServer(),step.getDatabase(),sql);
        if(n > 0){
            log.info("『执行成功』已插入"+n+"条数据");
        }
	}

	public void dbDeletea(TestStep step) throws Exception{
		String sql = AppiumUtil.parseStringHasEls(step.getSql());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">【"+ sql +"】");
	    RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "【"+ sql + "】");
        int n = DBHelper.delete(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(),step.getServer(), step.getDatabase(),sql);
        if(n > 0){
            log.info("『执行成功』已删除"+n+"条数据");
        }
	}
	
	public void dbDeletew(TestStep step) throws Exception{
		String sql = SeleniumUtil.parseStringHasEls(step.getSql());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">【"+ sql +"】");
	    RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "【"+ sql + "】");
        int n = DBHelper.delete(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(),step.getServer(), step.getDatabase(),sql);
        if(n > 0){
            log.info("『执行成功』已删除"+n+"条数据");
        }
	}

	public void dbUpdatea(TestStep step) throws Exception{
		String sql = AppiumUtil.parseStringHasEls(step.getSql());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">【"+ sql +"】");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "【"+ sql + "】");
		int n = DBHelper.update(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(),step.getServer(), step.getDatabase(),sql);
		if(n > 0){
		    log.info("『执行成功』已修改"+n+"条数据");
//			Reporter.log(base.getDesc());
		}
	}
	
	public void dbUpdatew(TestStep step) throws Exception{
		String sql = SeleniumUtil.parseStringHasEls(step.getSql());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">【"+ sql +"】");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "【"+ sql + "】");
		int n = DBHelper.update(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(),step.getServer(), step.getDatabase(),sql);
		if(n > 0){
		    log.info("『执行成功』已修改"+n+"条数据");
//			Reporter.log(base.getDesc());
		}	
	}
	
	public void dbQuerya(TestStep step) throws Exception{
		if(StringUtils.isBlank(step.getDetails().get("key")))
			throw new Exception("数据库查询务必设置保存结果的键值，供后续操作使用，例子为details='key:credit'！");
		String sql = AppiumUtil.parseStringHasEls(step.getSql());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">【"+ sql +"】");
		RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "【"+ sql + "】");
//      System.err.println("Query-sql "+sql);
		List<String> st = DBHelper.query(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(), step.getServer(),step.getDatabase(),sql);
		AppiumUtil.localmap.put(step.getDetails().get("key"), st);
		log.info("『正常测试』执行成功: <成功记录到本地缓存List列表，" +AppiumUtil.localmap.toString() + ">");
	}

	public void dbQueryw(TestStep step) throws Exception{
		String sql = SeleniumUtil.parseStringHasEls(step.getSql());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">【"+ sql +"】");
        RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "【"+ sql + "】");
		if (StringUtil.isEqual(step.getDatatype(),"MongoDB")){
			MongoDBUtil mongoDBUtil = DBHelper.Connect_MongoDB(step.getDataenviron(), step.getDevice(),step.getPort(), step.getDatabase());
			mongoDBUtil.executeOperation("SELECT", sql, null, null);
		}else{
			DBHelper.query(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(),step.getServer(),step.getDatabase(),sql);
		}
	}
	
	public void dbQueryws(TestStep step) throws Exception{
		String sql = SeleniumUtil.parseStringHasEls(step.getSql());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">【"+ sql +"】");
        List<String> re;
		if (StringUtil.isEqual(step.getDatatype(),"MongoDB")){
			MongoDBUtil mongoDBUtil = DBHelper.Connect_MongoDB(step.getDataenviron(), step.getDevice(),step.getPort(), step.getDatabase());
			re = mongoDBUtil.executeOperation("SELECT", sql, null, null);
		}else{
			re = DBHelper.query(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(),step.getServer(), step.getDatabase(),sql);
		}
        if(StringUtil.isNoEmpty(step.getDetails().get("key"))) {
        	// 数据库查询务必设置保存结果的键值，供后续操作使用，例子为details='key:credit'！
        	SeleniumUtil.localmap.put(step.getDetails().get("key"), re);
            log.info("『正常测试』执行成功: <成功记录到本地缓存List列表，" +SeleniumUtil.localmap.toString() + ">");
            RunUnitService.Step.put("name","" + step.getId() + "." + step.getName() + "【"+ step.getSql() + "】"+ "获取本地变量：【"+SeleniumUtil.localmap.toString()+"】");
        }
	}
	
	public void dbProcedurea(TestStep step) throws Exception{
	    String params = AppiumUtil.parseStringHasEls(step.getValue());
        log.info("『正常测试』开始执行: "+ step.getDetails().get("prc_name") + params + " <" +step.getId() + "." +step.getName() + ">");	    
        RunUnitService.Step.put("name","" +step.getId() + "." +step.getName() + ""+step.getDetails().get("prc_name")+"");
        int n =DBHelper.procedure(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(),step.getDatabase(),step.getDetails().get("prc_name"),params, (Object) null);
		if(n > 0){
		    log.info("『执行成功』已运行"+n+"条数据");
		}
	}
	
	public void dbProcedurew(TestStep step) throws Exception{
		String params = SeleniumUtil.parseStringHasEls(step.getValue());
		log.info("『正常测试』开始执行: "+ step.getDetails().get("prc_name") + params + " <" +step.getId() + "." +step.getName() + ">");
		RunUnitService.Step.put("name","" +step.getId() + "." +step.getName() + ""+step.getDetails().get("prc_name")+"");
		int n =DBHelper.procedure(step.getDatatype(),step.getDataenviron(), step.getDevice(),step.getPort(), step.getDatabase(),step.getDetails().get("prc_name"),params, (Object) null);
		if(n > 0){
		    log.info("『执行成功』已运行"+n+"条数据");
		}
	}

	public void customDatabaseOperations(TestStep step) throws Exception{
		Object results;
		String sql = SeleniumUtil.parseStringHasEls(step.getSql());
		String url = SeleniumUtil.parseStringHasEls(step.getUrl());
		log.info("『正常测试』开始执行: " + "<" +step.getId() + "." +step.getName() + ">【"+ sql +"】");
		RunUnitService.Step.put("name", step.getId() + "." + step.getName() + "【"+ sql + "】");
		switch (step.getType()){
			case "executeUpdate":
				DBHelper.executeDatabaseOperations(step.getClassName(),url,step.getUserName(), step.getPassword(),"executeUpdate",sql,null);
				break;
			case "executeQuery":
				DBHelper.executeDatabaseOperations(step.getClassName(),url,step.getUserName(), step.getPassword(),"executeQuery",sql, null);
				break;
			case "executeQueryOut":
				results = DBHelper.executeDatabaseOperations(step.getClassName(),url,step.getUserName(), step.getPassword(),"executeQuery",sql, null);
				if(StringUtil.isNoEmpty(step.getDetails().get("key"))) {
					// 数据库查询务必设置保存结果的键值，供后续操作使用，例子为details='key:credit'！
					SeleniumUtil.localmap.put(step.getDetails().get("key"), results);
					log.info("『正常测试』执行成功: <成功记录到本地缓存List列表，" +SeleniumUtil.localmap.toString() + ">");
					RunUnitService.Step.put("name", step.getId() + "." + step.getName() + "【"+ step.getSql() + "】"+ "获取本地变量：【"+SeleniumUtil.localmap.toString()+"】");
				}
				break;
			case "prepareCall":
				String params = SeleniumUtil.parseStringHasEls(step.getParams());
				log.info("『正常测试』执行参数: 【"+ params +"】");
				RunUnitService.Step.put("name", step.getId() + "." + step.getName() + "【"+ sql +"】【"+params+"】");
				DBHelper.executeDatabaseOperations(step.getClassName(),url,step.getUserName(),step.getPassword(),"prepareCall",sql,params);
				break;
		}
	}

	public static void main(String[] args) throws Exception {
		String className = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://172.19.5.229:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT";
		String user = "root";
		String password = "Ankki_mySQL123";
		String sql1 = "INSERT INTO `TEST`.`JDBC` VALUES (1, '小王', 5/3,5/3,SYSDATE());";
		String sql2 = "UPDATE `TEST`.`JDBC` SET name='小王' WHERE id=1;";
		String sql3 = "SELECT * FROM `TEST`.`JDBC`;";
		String sql4 = "DELETE FROM `TEST`.`JDBC` WHERE id=1;";
		String sql5 = "{call `TEST`.`JDBC_TEST`(?,?,?,?)};";
		String params = "[1, '小王', 1, 1]";

//		Object results = DBHelper.executeDatabaseOperations(className,url,user, password,"executeUpdate",sql1,null,(Object) null);
//		results = DBHelper.executeDatabaseOperations(className,url,user, password,"executeUpdate",sql2,null,(Object) null);
//		results = DBHelper.executeDatabaseOperations(className,url,user, password,"executeQuery",sql3,null,(Object) null);
//		results = DBHelper.executeDatabaseOperations(className,url,user, password,"executeUpdate",sql4,null,(Object) null);
//		DBHelper.executeDatabaseOperations(className,url,user, password,"prepareCall",sql5,params);

		MongoDBUtil mongoDBUtil = DBHelper.Connect_MongoDB("测试环境","AAS_DM_M","27017", "test");
		List<String> st = mongoDBUtil.executeOperation("SELECT", "tb4", null, null);
		log.info(st);
	}
}
