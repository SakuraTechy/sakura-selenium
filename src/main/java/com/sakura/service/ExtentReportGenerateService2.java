//package com.sakura.service;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.sakura.util.ConfigUtil;
//import com.sakura.util.Constants;
//import com.sakura.util.CopyFile;
//import com.sakura.util.DateUtil;
//import com.sakura.util.JSchUtil;
//import com.sakura.util.JsonUtil;
//import com.sakura.util.StringUtil;
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.ResourceCDN;
//import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.model.TestAttribute;
//import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
//import com.aventstack.extentreports.reporter.configuration.ChartLocation;
//import com.aventstack.extentreports.reporter.configuration.Theme;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.SftpException;
//
//import org.apache.log4j.Logger;
//import org.testng.*;
//import org.testng.xml.XmlSuite;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//
///**
// * <br>
// * 用于在测试结束后生成测试报告</br>
// *
// * @author chenwx
// * @date 2017年3月24日
// * @version 1.0
// * @since 1.0
// */
//public class ExtentReportGenerateService2 implements IReporter {
//    static Logger log = Logger.getLogger(ExtentReportGenerateService2.class);
//
//    /**
//     * 生成的路径以及文件名
//     */
//    private static final String OUTPUT_FOLDER = System.getProperty("user.dir") + "/TestOutput/ExtentReport/";
//    /**
//     * 报告的名称
//     */
//    private static final String FILE_NAME = "index.html";
//
//    private ExtentReports extent;
//
//    String Product_Name = ConfigUtil.getProperty("Product_Name", Constants.CONFIG_APP);
//    String Product_Version =  ConfigUtil.getProperty("Product_Version", Constants.CONFIG_APP);
//    String date;
//
//    String jsonContent;
//    int UnitsIndex = 0;
//    int CasesIndex = 0;
//    List<Throwable> tw = new ArrayList<Throwable>();
//    int ThrowableIndex = 0;
//
//    @SuppressWarnings("unused")
//    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory){
//        RunUnitService.Json.put("name", "name");
//        RunUnitService.Json.put("units", RunUnitService.Units);
//        jsonContent = JSON.toJSONString(RunUnitService.Json, SerializerFeature.DisableCircularReferenceDetect);;
//        log.info(jsonContent);
//        init(suites);
//        for (ISuite suite : suites) {
//            ExtentTest suiteTest = null;
//            // 存在多个suite的情况下，在报告中将同一个一个suite的测试结果归为一类，创建一级节点。
//            if (suites.size() > 1) {
////              suiteTest = extent.createTest(suite.getName()).assignCategory();
//            }
//
//            Map<String, ISuiteResult> result = suite.getResults();
//            // 如果suite里面没有任何用例，直接跳过，不在报告里生成
//            if (result.size() == 0) {
//                continue;
//            }
//
//            // 统计suite下的成功、失败、跳过的总用例数
//            int suiteFailSize = 0;
//            int suitePassSize = 0;
//            int suiteSkipSize = 0;
//
//            boolean createSuiteResultNode = false;
//            if (result.size() > 1) {
//                createSuiteResultNode = true;
//            }
//
//            for (ISuiteResult r : result.values()) {
//                CasesIndex = 0;
//                tw = new ArrayList<Throwable>();
//                ThrowableIndex = 0;
//                ExtentTest resultNode;
//                ITestContext context = r.getTestContext();
//                if (createSuiteResultNode) {
//                    // 没有创建suite的情况下，将在SuiteResult的创建为一级节点，否则创建为suite的一个子节点。
//                    if (null == suiteTest) {
//                        resultNode = extent.createTest(r.getTestContext().getName());
//                    } else {
//                        resultNode = suiteTest.createNode(r.getTestContext().getName());
//                    }
//                } else {
//                    resultNode = suiteTest;
//                }
//                log.info("UnitName：" + r.getTestContext().getName());
//                if (resultNode != null) {
//                    try {// TestsName模块名称
////						resultNode.getModel().setName(suite.getName());
////						resultNode.getModel().setName(r.getTestContext().getName());
////						resultNode.getModel().setName(suite.getName()+" : "+r.getTestContext().getName());
//                    	resultNode.getModel().setName(JsonUtil.getNodeValue(jsonContent, "units["+UnitsIndex+"].id"));
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//                    if (resultNode.getModel().hasCategory()) {
//                        resultNode.assignCategory(r.getTestContext().getName());
//                    } else {
////                        resultNode.assignCategory(suite.getName(), r.getTestContext().getName());
//                        resultNode.assignCategory(r.getTestContext().getName());
//                    }
//                    resultNode.getModel().setStartTime(r.getTestContext().getStartDate());
//                    resultNode.getModel().setEndTime(r.getTestContext().getEndDate());
//                    // 统计SuiteResult下的数据
//                    int passSize = r.getTestContext().getPassedTests().size();
//                    int failSize = r.getTestContext().getFailedTests().size();
//                    int skipSize = r.getTestContext().getSkippedTests().size();
//                    suitePassSize += passSize;
//                    suiteFailSize += failSize;
//                    suiteSkipSize += skipSize;
//                    if (failSize > 0) {
//                        resultNode.getModel().setStatus(Status.FAIL);
//                    }
//                    resultNode.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;", passSize, failSize, skipSize));
//                }
//                try {
//                    buildTestNodes(resultNode, context.getFailedTests(), Status.FAIL);
//                    buildTestNodes(resultNode, context.getPassedTests(), Status.PASS);
//                    buildTestNodes(resultNode, context.getSkippedTests(), Status.SKIP);
//                } catch (Exception e) {
//                    //e.printStackTrace();
//                    log.error("",e);
//                }
//                UnitsIndex++;
//            }
//            if (suiteTest != null) {
//                suiteTest.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;", suitePassSize, suiteFailSize, suiteSkipSize));
//                if (suiteFailSize > 0) {
//                    suiteTest.getModel().setStatus(Status.FAIL);
//                }
//            }
//        }
//
//        String appium = ""+System.getProperty("user.dir") + "/Logs/appium.txt";
//        String logs = ""+System.getProperty("user.dir") + "/Logs/log.txt";
//        String error = ""+System.getProperty("user.dir") + "/Logs/error.txt";
//        String path = ""+System.getProperty("user.dir") + "/TestOutput/ExtentReport/"+date+"/"+Product_Name+"/"+Product_Version+"/logs/";
//        if (StringUtil.isEqual(ConfigUtil.getProperty("Environment_Type", Constants.CONFIG_APP), "Linux")) {
//            String localPath = System.getProperty("user.dir") + "/Logs/";
//            String remotePath = "/";
//            try {
//                JSchUtil.connect(ConfigUtil.getProperty(""+AppiumService.DeviceID+"_FreeSSHd_IP", Constants.CONFIG_APP),22,ConfigUtil.getProperty(""+AppiumService.DeviceID+"_FreeSSHd_UserName", Constants.CONFIG_APP),ConfigUtil.getProperty(""+AppiumService.DeviceID+"_FreeSSHd_PassWord", Constants.CONFIG_APP));
//                JSchUtil.download(remotePath , "appium.txt", localPath);
//                JSchUtil.execCmd("adb -s "+ConfigUtil.getProperty(""+AppiumService.DeviceID+"_DeviceID", Constants.CONFIG_APP)+" shell ime list -s");
//                JSchUtil.execCmd("adb -s "+ConfigUtil.getProperty(""+AppiumService.DeviceID+"_DeviceID", Constants.CONFIG_APP)+" shell ime set com.sohu.inputmethod.sogou.xiaomi/.SogouIME");
//                JSchUtil.close();
//                CopyFile.copy(appium, path);
//                CopyFile.copy(logs, path);
//                CopyFile.copy(error, path);
//            } catch (JSchException | SftpException | IOException | InterruptedException e) {
//                //e.printStackTrace();
//                log.error("",e);
//            }
//        }else{
//            CopyFile.copy(appium, path);
//            CopyFile.copy(logs, path);
//            CopyFile.copy(error, path);
//        }
//
//        // TestRunner Logs备注信息输出
//        for (String s : Reporter.getOutput()) {
//            extent.setSystemInfo("123", "456");
//            extent.setTestRunnerOutput(s);
//        }
//        extent.flush();
//    }
//
//    private void init(List<ISuite> suites) {
//        date = DateUtil.getDateFormat("yyyy-MM-dd");
//        // 文件夹不存在的话进行创建
//        File reportDir = new File(OUTPUT_FOLDER +date+"/"+ Product_Name+"/"+Product_Version+"/logs/");
//        if (!reportDir.exists() && !reportDir.isDirectory()) {
//            reportDir.mkdir();
//        }
//        for (ISuite suite : suites) {
//            // String a =suite.getName();
//            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER +date+"/"+ Product_Name+"/"+Product_Version+"/"+FILE_NAME);
//            htmlReporter.config().setDocumentTitle("Selenium 自动化测试报告");
//            htmlReporter.config().setReportName(suite.getName()+"【"+Product_Name+"_"+Product_Version+"】WEB UI自动化测试报告");
//            htmlReporter.config().setChartVisibilityOnOpen(true);
//            htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
//            htmlReporter.config().setTheme(Theme.STANDARD);
//            htmlReporter.config().setEncoding("utf-8");
//            htmlReporter.config().setCSS(".node.level-1  ul{ display:none;} .node.level-1.active ul{display:block;}");
//            // 如果cdn.rawgit.com访问不了，可以设置为：ResourceCDN.EXTENTREPORTS或者ResourceCDN.GITHUB
//            htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
//
//            extent = new ExtentReports();
//            extent.attachReporter(htmlReporter);
//            extent.setReportUsesManualConfiguration(true);
//        }
//    }
//
//    private void buildTestNodes(ExtentTest extenttest, IResultMap tests, Status status) throws Exception {
//        ExtentTest test;
//        // 存在父节点时，获取父节点的标签
//        String[] categories = new String[0];
//        if (extenttest != null) {
//            List<TestAttribute> categoryList = extenttest.getModel().getCategoryContext().getAll();
//            categories = new String[categoryList.size()];
//            for (int index = 0; index < categoryList.size(); index++) {
//                categories[index] = categoryList.get(index).getName();
////                log.info("TestName：" + categories[index]);
//            }
//
//        }
//        if (tests.size() > 0) {
//            // 调整用例排序，按时间排序
//            Set<ITestResult> treeSet = new TreeSet<ITestResult>(new Comparator<ITestResult>() {
//                public int compare(ITestResult o1, ITestResult o2) {
//                    return o1.getStartMillis() < o2.getStartMillis() ? -1 : 1;
//                }
//            });
//            treeSet.addAll(tests.getAllResults());
//            for (ITestResult result : treeSet) {
//                result.getParameters();
//                String name = "";
//                // 如果有参数，则使用参数的toString组合代替报告中的Casename
//                // for(Object param:parameters){
//                // name+=param.toString();
//                // }
//                // 当Case的名称大于50位，则用...忽略
//                if (name.length() > 0) {
//                    if (name.length() > 50) {
//                        name = name.substring(0, 49) + "...";
//                    }
//                } else {
//                    name = result.getMethod().getMethodName();
//                }
//                if (result.getThrowable() != null) {
//                    tw.add(result.getThrowable());
//                }
//                log.info("UnitsIndex：" + UnitsIndex);
//                log.info("CasesIndex：" + CasesIndex);
//                try {
//                    String CaseID = JsonUtil.getNodeValue(jsonContent, "units["+UnitsIndex+"].cases["+CasesIndex+"].id");
//                    String CaseName = JsonUtil.getNodeValue(jsonContent, "units["+UnitsIndex+"].cases["+CasesIndex+"].name");
//
//                    if (extenttest == null) {
//                        test = extent.createTest(name);
//                    } else {
////                      test = extent.createTest(name);//外层Tests子节点
//                        // 作为子节点进行创建时，设置同父节点的标签一致，便于报告检索。
////                        test = extenttest.createNode(name).assignCategory(categories);
//                        test = extenttest.createNode(CaseID+"  "+ CaseName).assignCategory(categories);
//                    }
//                    log.info(CaseID + " "+CaseName);
//                    test.debug(CaseName);
//
//                    for (String group : result.getMethod().getGroups()) {
//                        test.assignCategory(group);
//                        String getMethod = group;
//                        log.info("内容：" + getMethod);
//                    }
//
////                    List<String> outputList = Reporter.getOutput(result);
////                    for (String output : outputList) {
////                        test.debug(output);
////                        log.info("Name：" + output);
////                    }
//
//                    JSONArray stepsArray = JsonUtil.getNode(jsonContent, "units["+UnitsIndex+"].cases["+CasesIndex+"]").getJSONArray("steps");
//                    log.info("stepsArray：" + stepsArray);
//                    for(int i=0; i<stepsArray.size(); i++){
//                        if(StringUtil.isNoEmpty((String)stepsArray.getJSONObject(i).get("name"))&&StringUtil.isEmpty((String)stepsArray.getJSONObject(i).get("picture"))){
//                            log.info("Step：" + stepsArray.getJSONObject(i).get("name"));
//                            test.pass("Step: " + stepsArray.getJSONObject(i).get("name"));
//                        }else if(StringUtil.isNoEmpty((String)stepsArray.getJSONObject(i).get("picture"))&&StringUtil.isNotEqual(stepsArray.getJSONObject(i).get("picture").toString(), "pass")){
//                            String step_name = (String)stepsArray.getJSONObject(i).get("name");
//                            String picture = (String)stepsArray.getJSONObject(i).get("picture");
//                            log.info("Step：" + step_name);
//                            log.info("Picture：" + picture);
//                            test.fail("Step: " + step_name);
//                            test.addScreenCaptureFromPath(picture);
//                        }else{
//                            log.info("Step：" + stepsArray.getJSONObject(i).get("name"));
//                            test.pass("Step: " + stepsArray.getJSONObject(i).get("name"));
//                        }
//                    }
//                    if(StringUtil.isEqual("FAIL", test.getStatus().name())){
//                        log.info("Throwable: "+tw);
//                        log.info("ThrowableIndex: "+ThrowableIndex);
//                        test.log(Status.DEBUG, tw.get(ThrowableIndex));
//                        ThrowableIndex++;
//                    }else{
//                        test.log(Status.DEBUG, "Test Pass");
//                    }
//                    log.info("status：" + test.getStatus());
//                    test.getModel().setStartTime(getTime(result.getStartMillis()));
//                    test.getModel().setEndTime(getTime(result.getEndMillis()));
//                    log.info("===================================================================================================================");
//                    CasesIndex++;
//                }catch (Exception e) {
//                    //e.printStackTrace();
//                    log.error("",e);
//                    Thread.sleep(500);
//                }
//            }
//        }
//    }
//
//    private Date getTime(long millis) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(millis);
//        return calendar.getTime();
//    }
//
//    public static void main(String[] args) throws Exception {
//        int a = 0;
//        a = a+1;
//        log.info(a);
//    }
//}