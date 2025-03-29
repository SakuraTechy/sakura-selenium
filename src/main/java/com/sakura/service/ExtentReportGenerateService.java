package com.sakura.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sakura.util.ConfigUtil;
import com.sakura.util.Constants;
import com.sakura.util.CopyFile;
import com.sakura.util.DateUtil;
import com.sakura.util.JSchUtil;
import com.sakura.util.JsonUtil;
import com.sakura.util.StringUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.TestAttribute;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
//import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import org.apache.log4j.Logger;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <br>
 * 用于在测试结束后生成测试报告</br>
 *
 * @author chenwx
 * @date 2017年3月24日
 * @version 1.0
 * @since 1.0
 */
public class ExtentReportGenerateService implements IReporter {
    static Logger log = Logger.getLogger(ExtentReportGenerateService.class);
    
    /**
     * 生成的路径以及文件名
     */
    private static final String OUTPUT_FOLDER = System.getProperty("user.dir") + "/TestOutput/ExtentReport/";
    /**
     * 报告的名称
     */
    private static final String FILE_NAME = "index.html";

    private ExtentReports extent;
    
    String Product_Name = ConfigUtil.getProperty("Product_Name", Constants.CONFIG_APP);
    String Product_Version =  ConfigUtil.getProperty("Product_Version", Constants.CONFIG_APP);
    String SysScene_UploadResult_API =  ConfigUtil.getProperty("SysScene_UploadResult_API", Constants.CONFIG_APP);
    String buildNumber =  ConfigUtil.getProperty("buildNumber", Constants.CONFIG_APP);
    String TestReport_UploadResult_API =  ConfigUtil.getProperty("TestReport_UploadResult_API", Constants.CONFIG_APP);
    String testReportId =  ConfigUtil.getProperty("testReportId", Constants.CONFIG_APP);
    String testPlanId =  ConfigUtil.getProperty("testPlanId", Constants.CONFIG_APP);
    String date;
    
    String jsonContent;
    int UnitsIndex = 0;
    int CasesIndex = 0;
    List<Throwable> tw = new ArrayList<Throwable>();
    int ThrowableIndex = 0;
    
    String sceneId = "";
    int sceneTotal = 0;
    int scenePass = 0;
    int sceneFail = 0;
    int sceneSkip = 0;
    
    int case_total = 0;
    int case_pass = 0;
    int case_fail = 0;
    int case_skip = 0;
    
    int step_total = 0;
    int step_pass = 0;
    int step_fail = 0;
    int step_skip = 0;
    
    int caseTotal = 0;
    int casePass = 0;
    int caseFail = 0;
    int caseSkip = 0;
    
    int stepTotal = 0;
    int stepPass = 0;
    int stepFail = 0;
    int stepSkip = 0;
    
    @SuppressWarnings("unused")
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory){
    	JSONObject Json = new JSONObject(true);
    	Json.put("name", "name");
    	Json.put("units", RunUnitService.Units);
        jsonContent = JSON.toJSONString(Json, SerializerFeature.DisableCircularReferenceDetect);;
//        log.info(jsonContent);
        init(suites);

        for (ISuite suite : suites) {
        	sceneFail=0;
            ExtentTest suiteTest = null;
            // 存在多个suite的情况下，在报告中将同一个一个suite的测试结果归为一类，创建一级节点。
            if (suites.size() > 1) {
//              suiteTest = extent.createTest(suite.getName()).assignCategory();
            }
            
            Map<String, ISuiteResult> result = suite.getResults();
            // 如果suite里面没有任何用例，直接跳过，不在报告里生成
            if (result.size() == 0) {
                continue;
            }
            
            
            
            boolean createSuiteResultNode = false;
            if (result.size() > 0) {
                createSuiteResultNode = true;
            }
            
            for (ISuiteResult r : result.values()) {
            	// 统计suite下的成功、失败、跳过的总用例数
                int suiteFailSize = 0;
                int suitePassSize = 0;
                int suiteSkipSize = 0;
                
            	int sceneTotal = 1;
            	int scenePass = 0;
            	int sceneFail1 = 0;
                int sceneSkip = 0;
                
                int case_total = 0;
                int case_pass1 = 0;
                int case_fail1 = 0;
                int case_skip1 = 0;
                
                int step_total = 0;
                int step_pass1 = 0;
                int step_fail1 = 0;
                int step_skip1 = 0;
                
                CasesIndex = 0;
                tw = new ArrayList<Throwable>();
                ThrowableIndex = 0;
                ExtentTest resultNode;
                ITestContext context = r.getTestContext();
                if (createSuiteResultNode) {
                    // 没有创建suite的情况下，将在SuiteResult的创建为一级节点，否则创建为suite的一个子节点。
                    if (null == suiteTest) {
                        resultNode = extent.createTest(r.getTestContext().getName());
                    } else {
                        resultNode = suiteTest.createNode(r.getTestContext().getName());
                    }
                } else {
                    resultNode = suiteTest;
                }
//                log.info("UnitName：" + r.getTestContext().getName());
                try {
        			sceneId = JsonUtil.getNodeValue(jsonContent, "units["+UnitsIndex+"].id");
        		} catch (Exception e1) {
        			e1.printStackTrace();
        		}
                
                if (resultNode != null) {
                    try {// TestsName模块名称
//						resultNode.getModel().setName(suite.getName());
//						resultNode.getModel().setName(r.getTestContext().getName());
//						resultNode.getModel().setName(suite.getName()+" : "+r.getTestContext().getName());
                    	resultNode.getModel().setName(sceneId);
					} catch (Exception e) {
						e.printStackTrace();
					}
                    if (resultNode.getModel().hasCategory()) {
                        resultNode.assignCategory(r.getTestContext().getName());
                    } else {
//                        resultNode.assignCategory(suite.getName(), r.getTestContext().getName());
                        resultNode.assignCategory(r.getTestContext().getName());
                    }
                    resultNode.getModel().setStartTime(r.getTestContext().getStartDate());
                    resultNode.getModel().setEndTime(r.getTestContext().getEndDate());
                    // 统计SuiteResult下的数据
                    int passSize = r.getTestContext().getPassedTests().size();
                    int failSize = r.getTestContext().getFailedTests().size();
                    int skipSize = r.getTestContext().getSkippedTests().size();
                    suitePassSize += passSize;
                    suiteFailSize += failSize;
                    suiteSkipSize += skipSize;
                    if (failSize > 0) {
                    	sceneFail++;
                    	sceneFail1++;
                        resultNode.getModel().setStatus(Status.FAIL);
                    }
//                    resultNode.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;", passSize, failSize, skipSize));
                }
                try {
                	step_pass = 0;
                    step_fail = 0;
                    step_skip = 0;
                    buildTestNodes(resultNode, context.getFailedTests(), Status.FAIL);
                    buildTestNodes(resultNode, context.getPassedTests(), Status.PASS);
                    buildTestNodes(resultNode, context.getSkippedTests(), Status.SKIP);
                    step_pass1 = step_pass;
                    step_fail1 = step_fail;
                    step_skip1 = step_skip;
                } catch (Exception e) {
                    //e.printStackTrace();
                    log.error("",e);
                }
//                resultNode.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;", suitePassSize, suiteFailSize, suiteSkipSize));
                resultNode.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ; Pass(Warning): %s ;", suitePassSize, suiteFailSize, suiteSkipSize, step_skip));
               
                case_pass1 += r.getTestContext().getPassedTests().size();
                case_fail1 += r.getTestContext().getFailedTests().size();
                case_skip1 += r.getTestContext().getSkippedTests().size();
                
                scenePass = sceneTotal-sceneFail1;
                sceneSkip = sceneTotal-sceneFail1-scenePass;
                String scenePassRate = String.format("%.2f%%", (double)scenePass/sceneTotal*100);
                log.info("单个场景总数："+sceneTotal);
                log.info("单个场景成功数："+scenePass);
                log.info("单个场景失败数："+sceneFail1);
                log.info("单个场景跳过数："+sceneSkip);
                log.info("单个场景通过率："+scenePassRate);
                
                case_total = case_pass1+case_fail1+case_skip1;
                String casePassRate = String.format("%.2f%%", (double)case_pass1/case_total*100);
                log.info("单个场景用例总数："+case_total);
                log.info("单个场景用例成功数："+case_pass1);
                log.info("单个场景用例失败数："+case_fail1);
                log.info("单个场景用例跳过数："+case_skip1);
//                log.info("场景用例通过率："+(case_pass/case_total)*100+"%");
                log.info("单个场景用例通过率："+casePassRate);
                
                step_total = step_pass1+step_fail1+step_skip1;
                String stepPassRate = String.format("%.2f%%", (double)step_pass1/step_total*100);
                log.info("单个场景用例步骤总数："+step_total);
                log.info("单个场景用例步骤成功数："+step_pass1);
                log.info("单个场景用例步骤失败数："+step_fail1);
                log.info("单个场景用例步骤跳过数："+step_skip1);
                log.info("单个场景用例步骤通过率："+stepPassRate);
                
                try {
                String ApiUrl = SysScene_UploadResult_API;
                String durationStartTime = JsonUtil.getNodeValue(jsonContent, "units["+UnitsIndex+"].durationStartTime");
                String durationEndTime = JsonUtil.getNodeValue(jsonContent, "units["+UnitsIndex+"].durationEndTime");
        		String Param1 = "{"
        				+ "\"projectName\":\""+Product_Name+"\","
        				+ "\"versionName\":\""+Product_Version+"\","
        				+ "\"sceneId\":\""+sceneId+"\","
        				+ "\"buildNumber\":\""+buildNumber+"\","
        				+ "\"sceneTotal\":"+sceneTotal+","
        				+ "\"scenePass\":"+scenePass+","
        				+ "\"sceneFail\":"+sceneFail1+","
        				+ "\"sceneSkip\":"+sceneSkip+","
        				+ "\"casePass\":"+case_pass1+","
        				+ "\"caseFail\":"+case_fail1+","
        				+ "\"caseSkip\":"+case_skip1+","
        				+ "\"stepPass\":"+step_pass1+","
        				+ "\"stepFail\":"+step_fail1+","
        				+ "\"stepSkip\":"+step_skip1+""
        				+ "}";
        		String Param = "{\n"
        				+ "  \"projectName\": \""+Product_Name+"\",\n"
        				+ "  \"versionName\": \""+Product_Version+"\",\n"
        				+ "  \"sceneId\": \""+sceneId+"\",\n"
        				+ "  \"testPlanId\": \""+testPlanId+"\",\n"
        				+ "  \"statisticAnalysis\": {\n"
        				+ "    \"ui\": {\n"
        				+ "      \"buildNumber\": "+buildNumber+",\n"
        				+ "      \"sceneTotal\": "+sceneTotal+",\n"
        				+ "      \"scenePass\": "+scenePass+",\n"
        				+ "      \"sceneFail\": "+sceneFail1+",\n"
        				+ "      \"sceneSkip\": "+sceneSkip+",\n"
        				+ "      \"scenePassRate\": \""+scenePassRate+"\",\n"
        				+ "      \"caseTotal\": "+case_total+",\n"
        				+ "      \"casePass\": "+case_pass1+",\n"
        				+ "      \"caseFail\": "+case_fail1+",\n"
        				+ "      \"caseSkip\": "+case_skip1+",\n"
        				+ "      \"casePassRate\": \""+casePassRate+"\",\n"
        				+ "      \"stepTotal\": "+step_total+",\n"
        				+ "      \"stepPass\": "+step_pass1+",\n"
        				+ "      \"stepFail\": "+step_fail1+",\n"
        				+ "      \"stepSkip\": "+step_skip1+",\n"
        				+ "      \"stepPassRate\": \""+stepPassRate+"\",\n"
        				+ "      \"durationStartTime\": \""+durationStartTime+"\",\n"
        				+ "      \"durationEndTime\": \""+durationEndTime+"\"\n"
        				+ "    }\n"
        				+ "  }\n"
        				+ "}";
//					HttpRequestUtil.SendPut(ApiUrl,Param);
				} catch (Exception e) {
					e.printStackTrace();
				}
                UnitsIndex++;
                caseTotal = caseTotal +case_total;
                
                casePass = casePass +case_pass1;
                caseFail = caseFail +case_fail1;
                caseSkip = caseSkip +case_skip1;
                
                stepTotal = stepTotal +step_total;
                stepPass = stepPass +step_pass;
                stepFail = stepFail +step_fail;
                stepSkip = stepSkip +step_skip;
            }
            
            sceneTotal = result.size();
            scenePass = sceneTotal-sceneFail;
            sceneSkip = sceneTotal-sceneFail-scenePass;
            
            log.info("所有场景总数："+sceneTotal);
            log.info("所有场景成功数："+scenePass);
            log.info("所有场景失败数："+sceneFail);
            log.info("所有场景跳过数："+sceneSkip);
            String scenePassRate = String.format("%.2f%%", (double)scenePass/sceneTotal*100);
            log.info("所有场景通过率："+scenePassRate);
            
            log.info("所有场景用例总数："+caseTotal);
            log.info("所有场景用例成功数："+casePass);
            log.info("所有场景用例失败数："+caseFail);
            log.info("所有场景用例跳过数："+caseSkip);
            String casePassRate = String.format("%.2f%%", (double)casePass/caseTotal*100);
            log.info("所有场景用例通过率："+casePassRate);
            
            stepTotal = RunUnitService.stepTotal2;
            stepSkip = stepTotal-stepPass-stepFail;
            log.info("所有场景用例步骤总数："+stepTotal);
            log.info("所有场景用例步骤成功数："+stepPass);
            log.info("所有场景用例步骤失败数："+stepFail);
            log.info("所有场景用例步骤跳过数："+stepSkip);
            String stepPassRate = String.format("%.2f%%", (double)stepPass/stepTotal*100);
            log.info("所有场景用例步骤通过率："+stepPassRate);
            
            String ApiUrl = TestReport_UploadResult_API;
    		String Param = "{\n"
    				+ "  \"id\": \""+testReportId+"\",\n"
    				+ "  \"statisticAnalysis\": {\n"
    				+ "    \"ui\": {\n"
    				+ "      \"buildNumber\": "+buildNumber+",\n"
    				+ "      \"sceneTotal\": "+sceneTotal+",\n"
    				+ "      \"scenePass\": "+scenePass+",\n"
    				+ "      \"sceneFail\": "+sceneFail+",\n"
    				+ "      \"sceneSkip\": "+sceneSkip+",\n"
    				+ "      \"scenePassRate\": \""+scenePassRate+"\",\n"
    				+ "      \"caseTotal\": "+caseTotal+",\n"
    				+ "      \"casePass\": "+casePass+",\n"
    				+ "      \"caseFail\": "+caseFail+",\n"
    				+ "      \"caseSkip\": "+caseSkip+",\n"
    				+ "      \"casePassRate\": \""+casePassRate+"\",\n"
    				+ "      \"stepTotal\": "+stepTotal+",\n"
    				+ "      \"stepPass\": "+stepPass+",\n"
    				+ "      \"stepFail\": "+stepFail+",\n"
    				+ "      \"stepSkip\": "+stepSkip+",\n"
    				+ "      \"stepPassRate\": \""+stepPassRate+"\"\n"
    				+ "    }\n"
    				+ "  }\n"
    				+ "}";
            try {
//				HttpRequestUtil.SendPut(ApiUrl,Param);
			} catch (Exception e) {
				e.printStackTrace();
			}
//            if (suiteTest != null) {                
////                suiteTest.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ; Warning: %s ;", suitePassSize, suiteFailSize, suiteSkipSize, step_skip));
//                if (suiteFailSize > 0) {
//                    suiteTest.getModel().setStatus(Status.FAIL);
//                }
//            }
        }
        
        String appium = ""+System.getProperty("user.dir") + "/Logs/appium.txt";
        String logs = ""+System.getProperty("user.dir") + "/Logs/log.txt";
        String error = ""+System.getProperty("user.dir") + "/Logs/error.txt";
        String path = ""+System.getProperty("user.dir") + "/TestOutput/ExtentReport/"+date+"/"+Product_Name+"/"+Product_Version+"/logs/";
        if (StringUtil.isEqual(ConfigUtil.getProperty("Environment_Type", Constants.CONFIG_APP), "Linux")) {
            String localPath = System.getProperty("user.dir") + "/Logs/";
            String remotePath = "/";
            try {
                JSchUtil.connect(ConfigUtil.getProperty(""+AppiumService.DeviceID+"_FreeSSHd_IP", Constants.CONFIG_APP),22,ConfigUtil.getProperty(""+AppiumService.DeviceID+"_FreeSSHd_UserName", Constants.CONFIG_APP),ConfigUtil.getProperty(""+AppiumService.DeviceID+"_FreeSSHd_PassWord", Constants.CONFIG_APP));
                JSchUtil.download(remotePath , "appium.txt", localPath);
                JSchUtil.execCmd("adb -s "+ConfigUtil.getProperty(""+AppiumService.DeviceID+"_DeviceID", Constants.CONFIG_APP)+" shell ime list -s");
                JSchUtil.execCmd("adb -s "+ConfigUtil.getProperty(""+AppiumService.DeviceID+"_DeviceID", Constants.CONFIG_APP)+" shell ime set com.sohu.inputmethod.sogou.xiaomi/.SogouIME");
                JSchUtil.close();
            } catch (JSchException | SftpException | IOException | InterruptedException e) {
                //e.printStackTrace();
                log.error("",e);
            }
        }
        if (StringUtil.isEqual(ConfigUtil.getProperty("Automation_Type", Constants.CONFIG_APP), "APP")) {
            CopyFile.copy(appium, path);
        }
        CopyFile.copy(logs, path);
        CopyFile.copy(error, path);
        // TestRunner Logs备注信息输出
        for (String s : Reporter.getOutput()) {
            extent.setSystemInfo("123", "456");
            extent.setTestRunnerOutput(s);
        }
        extent.flush();
    }

    private void init(List<ISuite> suites) {
        date = DateUtil.getDateFormat("yyyy-MM-dd");
        // 文件夹不存在的话进行创建
        File reportDir = new File(OUTPUT_FOLDER +date+"/"+ Product_Name+"/"+Product_Version+"/logs/");
        if (!reportDir.exists() && !reportDir.isDirectory()) {
            reportDir.mkdir();
        }
        for (ISuite suite : suites) {
            // String a =suite.getName();
            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER +date+"/"+ Product_Name+"/"+Product_Version+"/"+FILE_NAME);
            htmlReporter.config().setDocumentTitle("Selenium 自动化测试报告");
            htmlReporter.config().setReportName(suite.getName()+"【"+Product_Name+"_"+Product_Version+"】WEB UI自动化测试报告");
//            htmlReporter.config().setChartVisibilityOnOpen(true);
//            htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
            htmlReporter.config().setTheme(Theme.STANDARD);
            htmlReporter.config().setEncoding("utf-8");
//            htmlReporter.config().setCSS(".node.level-1  ul{display:none;} .node.level-1.active ul{display:block;} .r-img {display: block !important; width: 20%;}");
            htmlReporter.config().setCSS(".r-img {display: inline !important; width: 10%;}");
//            如果cdn.rawgit.com访问不了，可以设置为：ResourceCDN.EXTENTREPORTS或者ResourceCDN.GITHUB
//            htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            extent.setReportUsesManualConfiguration(true);
        }
    }

    private void buildTestNodes(ExtentTest extenttest, IResultMap tests, Status status) throws Exception {
        ExtentTest test;
        // 存在父节点时，获取父节点的标签
        String[] categories = new String[0];
        if (extenttest != null) {
            List<TestAttribute> categoryList = extenttest.getModel().getCategoryContext().getAll();
            categories = new String[categoryList.size()];
            for (int index = 0; index < categoryList.size(); index++) {
                categories[index] = categoryList.get(index).getName();
//                log.info("TestName：" + categories[index]);
            }
            
        }
        if (tests.size() > 0) {
            // 调整用例排序，按时间排序
            Set<ITestResult> treeSet = new TreeSet<ITestResult>(new Comparator<ITestResult>() {
                public int compare(ITestResult o1, ITestResult o2) {
                    return o1.getStartMillis() < o2.getStartMillis() ? -1 : 1;
                }
            });
            treeSet.addAll(tests.getAllResults());
            for (ITestResult result : treeSet) {
                result.getParameters();
                String name = "";
                // 如果有参数，则使用参数的toString组合代替报告中的Casename
                // for(Object param:parameters){
                // name+=param.toString();
                // }
                // 当Case的名称大于50位，则用...忽略
                if (name.length() > 0) {
                    if (name.length() > 50) {
                        name = name.substring(0, 49) + "...";
                    }
                } else {
                    name = result.getMethod().getMethodName();
                }
                if (result.getThrowable() != null) {
                    tw.add(result.getThrowable());
                }
//                log.info("UnitsIndex：" + UnitsIndex);
//                log.info("CasesIndex：" + CasesIndex);
                try {
                    String CaseID = JsonUtil.getNodeValue(jsonContent, "units["+UnitsIndex+"].cases["+CasesIndex+"].id");
                    String CaseName = JsonUtil.getNodeValue(jsonContent, "units["+UnitsIndex+"].cases["+CasesIndex+"].name");

                    if (extenttest == null) {
                        test = extent.createTest(name);
                    } else {
//                      test = extent.createTest(name);//外层Tests子节点
                        // 作为子节点进行创建时，设置同父节点的标签一致，便于报告检索。
//                        test = extenttest.createNode(name).assignCategory(categories);
                        test = extenttest.createNode(CaseID+"  "+ CaseName).assignCategory(categories);
                    }
                    
//                    log.info(CaseID + " "+CaseName);
                    test.debug(CaseName); 
                    
                    for (String group : result.getMethod().getGroups()) {
                        test.assignCategory(group);
//                        String getMethod = group;
//                        log.info("内容：" + getMethod);
                    }
                    
//                    List<String> outputList = Reporter.getOutput(result);
//                    for (String output : outputList) {
//                        test.debug(output);
//                        log.info("Name：" + output);
//                    }

                    JSONArray stepsArray = JsonUtil.getNode(jsonContent, "units["+UnitsIndex+"].cases["+CasesIndex+"]").getJSONArray("steps");
//                    log.info("stepsArray：" + stepsArray);
                    for(int i=0; i<stepsArray.size(); i++){
                        if(StringUtil.isNoEmpty((String)stepsArray.getJSONObject(i).get("name"))&&StringUtil.isEmpty((String)stepsArray.getJSONObject(i).get("picture"))){
//                            log.info("Step：" + stepsArray.getJSONObject(i).get("name"));
                            test.pass("Step: " + stepsArray.getJSONObject(i).get("name"));
                            step_pass++;
                        }else if(StringUtil.isNoEmpty((String)stepsArray.getJSONObject(i).get("picture"))&&StringUtil.isNoEmpty(stepsArray.getJSONObject(i).get("picture").toString())&&StringUtil.isNotEqual(stepsArray.getJSONObject(i).get("skip").toString(),"true")){
                            String step_name = (String)stepsArray.getJSONObject(i).get("name");
                            String picture = (String)stepsArray.getJSONObject(i).get("picture");
                            test.fail("Step: " + step_name);
                            test.addScreenCaptureFromPath(picture);
                            step_fail++;
                        }else if(StringUtil.isNoEmpty((String)stepsArray.getJSONObject(i).get("picture"))&&StringUtil.isNoEmpty(stepsArray.getJSONObject(i).get("picture").toString())&&StringUtil.isEqual(stepsArray.getJSONObject(i).get("skip").toString(),"true")){
                            String step_name = (String)stepsArray.getJSONObject(i).get("name");
                            String picture = (String)stepsArray.getJSONObject(i).get("picture");
                            test.warning("Step: " + step_name);
                            test.addScreenCaptureFromPath(picture);
                            step_skip++;
                        }
                    }
//                    log.info("status：" + test.getStatus().name());
                    if(StringUtil.isEqual("FAIL", test.getStatus().name())){
//                        log.info("Throwable: "+tw);
//                        log.info("ThrowableIndex: "+ThrowableIndex);
                        test.log(Status.DEBUG, tw.get(ThrowableIndex));
                        ThrowableIndex++;
                    }else{
                        test.log(Status.DEBUG, "Test Pass");
                    }
                    test.getModel().setStartTime(getTime(result.getStartMillis()));
                    test.getModel().setEndTime(getTime(result.getEndMillis()));
//                    log.info("===================================================================================================================");
                    CasesIndex++;
                }catch (Exception e) {
                    //e.printStackTrace();
//                    log.error("",e);
                    Thread.sleep(500);
                }
                if(result.getStatus()==3) {
                	test = extenttest.createNode(result.getName()+"  "+"前置场景用例失败，会导致后续用例跳过未执行").assignCategory(categories);
//                	test.createNode("11111","22222");
//                	test.skip("");
//                	test.assignAuthor("dddd");
                	test.getModel().setStatus(status);
                }
            }
        }
    }

    private static Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
//        calendar.get(Calendar.HOUR_OF_DAY );//得到24小时机制的
//        calendar.get(Calendar.HOUR);// 得到12小时机制的
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
    
    public static void main(String[] args) throws Exception {
//        int a = 0;
//        a = a+1;
//        log.info(a);
//        
//        String ApiUrl = "http://172.19.5.222:8084/system/automation/uploadResults";
//		String Param = "{\"projectName\":\"AAS_TEST\",\"versionName\":\"V6_5_1SP001D003\",\"sceneId\":\"AAS_WS_SMOKE_026\",\"casePass\":4112,\"caseFail\":0,\"caseSkip\":0,\"stepPass\":10,\"stepFail\":0,\"stepSkip\":2}";
//        try {
//			HttpRequestUtil.SendPut(ApiUrl,Param);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    	log.info("场景用例通过率："+(1/3)*100+"%");
    	System.out.println("场景用例通过率："+((double)1/3)*100+"%");
    	System.out.println(String.format("场景用例通过率：%.2f%%", (double)1/3*100));
    }
}