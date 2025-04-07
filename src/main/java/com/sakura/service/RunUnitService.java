package com.sakura.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sakura.util.*;
import org.apache.log4j.Logger;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.sakura.base.StepAction;
import com.sakura.base.TestCase;
import com.sakura.base.TestStep;
import com.sakura.base.TestUnit;

import static com.sakura.util.ScreenRecorderUtil.stopRecording;

/**
 * <br>TODO(描述该类的作用)</br>
 *
 * @date    2017年7月27日 下午5:50:09
 * @version 1.0
 * @since   1.0
 */
public class RunUnitService {

    static Logger log = Logger.getLogger(RunUnitService.class);
    
    public static TestUnit testUnit;
    public static TestCase testCase; 
    public static SoftAssert softAssert = new SoftAssert();

    public static JSONObject Json = new JSONObject(true);

    public static JSONArray Units = new JSONArray();
    public static JSONObject Unit = new JSONObject(true);

    public static JSONArray Cases = new JSONArray();
    public static JSONObject Case = new JSONObject(true);

    public static JSONArray Steps = new JSONArray();
    public static JSONObject Step = new JSONObject(true);

    static int UnitsIndex = 0;
    static int CaseIndex = 0;

    int sceneTotal = 0;
    int scenePass = 0;
    int sceneFail = 0;
    int sceneSkip = 0;
    String scenePassRate="";
    
    int caseTotal = 0;
    int casePass = 0;
    int caseFail = 0;
    int caseSkip = 0;
    String casePassRate="";
    
    int stepTotal = 0;
    int stepPass = 0;
    public static int stepFail = 0;
    public static int stepSkip = 0;
    String stepPassRate="";
    
    static int stepTotal1 = 0;
    int stepPass1 = 0;
    public static int stepFail1 = 0;
    public static int stepSkip1 = 0;
    String stepPassRate1="";
    
    static int stepTotal2 = 0;
    static String durationStartTime = "";
    static String durationEndTime = "";
    
    static String testReportdurationStartTime = "";

    static FrameGrabber grabber;
    static FFmpegFrameRecorder recorder;

	public RunUnitService(TestUnit testUnit) throws FrameGrabber.Exception, FFmpegFrameRecorder.Exception {
        Unit = new JSONObject(true);
        Unit.put("durationStartTime", durationStartTime);
//        Unit.put("durationStartTime", DateUtil.getDate());
        Cases = new JSONArray();
        CaseIndex = 0;
        
        caseTotal = 0;
        casePass = 0;
        caseFail = 0;
        caseSkip = 0;
        
        stepTotal1= 0;
        stepPass1= 0;
        stepFail1= 0;
        stepSkip1= 0;
        RunUnitService.testUnit = testUnit;
    }

    /**
     * <br>
     * 根据索引从TestUnit中获取测试用例</br>
     *
     * @param index
     * @return
     */
    public TestCase getCase(int index) {
        int n = 0;
        if (testUnit.getCaseMap() == null)
            return null;

        int size = testUnit.getCaseMap().size();
        if (index < 0 || (index > 0 && index >= size))
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);

        // 遍历map内部Entry集合
        for (Map.Entry<String, TestCase> e : testUnit.getCaseMap().entrySet()) {
            if (n++ == index)
                return e.getValue();
        }

        return null;
    }

    /**
     * <br>
     * 根据用例名，即case元素中的id元素值，从TestUnit中获取测试用例</br>
     *
     * @param id
     * @return
     */
    public TestCase getCase(String id) {
        return testUnit.getCaseMap() == null ? null : testUnit.getCaseMap().get(id);
    }

    /**
     * <br>
     * 执行测试用例</br>
     *
     * @param id
     * @throws Exception
     */
    public void runCase(String id) throws Exception {
    	log.info("<----------------------------------------【"+ testUnit.getId()+"，"+testUnit.getName() +"】--------------------------------------->");
        softAssert = new SoftAssert();
        Case = new JSONObject(true);
        Steps = new JSONArray();
        testCase = getCase(id);
        testCase.setId(testCase.getId());
        testCase.setName(testCase.getName());
        log.info("<---"+testCase.getId()+"，"+testCase.getName()+"--->");
        List<TestStep> steps = testCase.getSteps();
        int StepIndex = 1;
        stepFail = 0;
        stepSkip = 0;
        for (TestStep step : steps) {
            Step = new JSONObject(true);
            step.setId(String.valueOf(StepIndex));
            if (step.isCancel())
                continue;
            try {
                // log.info("开始执行: "+step.toString());
                StepAction action = step.getAction();
                // 如果StepAction实例有handler字段，则调用handler中的方法，否则直接调用run方法
                Class<?> clazz = action.handler();
                if (clazz != null) {
                    String key = step.getAction().key();
                    Method m = clazz.getDeclaredMethod(getMethodName(key), new Class<?>[] {TestStep.class});
                    m.invoke(clazz.newInstance(), step);
                } else {
                    action.run(step);
                }
            }  catch (Exception e) {
//                log.info("『发现问题』执行异常: " + "<" + step.getId() + "." + step.getName() + ">");
                //e.printStackTrace();
//                log.error("",e);
//                Steps.add(StepIndex - 1, Step);
//                Thread.sleep(500);
                continue;
            } finally {
            	Steps.add(StepIndex - 1, Step);
                StepIndex++;
            }
        }
        stepTotal = steps.size();
        stepPass = stepTotal-stepFail-stepSkip;
        stepFail = stepTotal-stepPass-stepSkip;
        stepSkip = stepTotal-stepPass-stepFail;
        stepPassRate = String.format("%.2f%%", (double)(stepTotal-stepFail-stepSkip)/stepTotal*100);
//        stepTotal1 += stepTotal;
        stepPass1 += stepPass;
        stepFail1 += stepFail;
        stepSkip1 += stepSkip;
        
        Case.put("id", id);
        Case.put("name", testCase.getName());
        Case.put("stepTotal", stepTotal);
        Case.put("stepPass", stepPass);
        Case.put("stepFail", stepFail);
        Case.put("stepSkip", stepSkip);
        Case.put("stepPassRate", stepPassRate);
        Case.put("steps", Steps);
        Cases.add(CaseIndex, Case);
        CaseIndex = CaseIndex + 1;
        caseFail = stepFail>0?caseFail+1:caseFail;
        casePass = stepSkip>0?casePass+1:casePass;
        TestReportRemarks(testCase.getName());
        softAssert.assertAll();
    }

    /**
     * <br>
     * 根据step元素的值解析出对应的方法名</br>
     * 作用是将"-"后面的第一个字母转为大写，并且去掉“-”
     *
     * @param actionKey
     * @return
     */
    private String getMethodName(String actionKey) {
        if (actionKey == null || "".equals(actionKey))
            throw new RuntimeException("empty action key");

        char[] arr = actionKey.toCharArray();
        char prevChar = '\0';
        StringBuilder sb = new StringBuilder();
        char splitChar = '-';

        for (char c : arr) {
            if (c == splitChar) {
                prevChar = c;
                continue;
            }
            if (prevChar == splitChar) {
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(c);
            }
            prevChar = c;
        }

        return sb.toString();
    }

    /**
     * <br>
     * 执行完毕，退出App程序</br>
     */
    public void closeApp() {
        AndroidXmlParseService.QuitApp();
    }

    /**
     * 
     * 执行完毕，添加Unit
     * 
     * @throws JSONException
     */
    public void setUnit(Boolean ...State) {
    	closeBrowser();
        if(State.length>0){
            UnitsIndex = 0;
            testReportdurationStartTime = durationStartTime;
        }
        try {
        	caseTotal = CaseIndex;
        	casePass = caseTotal-caseFail-caseSkip;
        	caseFail = caseTotal-casePass-caseSkip;
        	caseSkip = caseTotal-casePass-caseFail;
//            casePassRate = String.format("%.2f%%", (double)(caseTotal-caseFail-caseSkip)/caseTotal*100);
          
        	LinkedHashMap<String, TestCase> caseMap = testUnit.getCaseMap();
            caseTotal = caseMap.size();
            caseSkip = caseTotal-casePass-caseFail;
            casePassRate = String.format("%.2f%%", (double)(caseTotal-caseFail-caseSkip)/caseTotal*100);
                    	
            int num=0;
            for (String caseKey : caseMap.keySet()) {
            	num += caseMap.get(caseKey).getSteps().size();
            }
            stepTotal1 = num;
            stepSkip1 = stepTotal1-stepPass1-stepFail1;
        	stepPassRate1 = String.format("%.2f%%", (double)(stepTotal1-stepFail1-stepSkip1)/stepTotal1*100);
        	stepTotal2 += stepTotal1;
        	
            Unit.put("id", testUnit.getId());
            Unit.put("name", testCase.getName());
            Unit.put("caseTotal", caseTotal);
            Unit.put("casePass", casePass);
            Unit.put("caseFail", caseFail);
            Unit.put("caseSkip", caseSkip);
            Unit.put("casePassRate", casePassRate);
            Unit.put("cases", Cases);
            durationEndTime = DateUtil.getDate();
            Unit.put("durationEndTime", durationEndTime);
            Units.add(UnitsIndex, Unit);
//            log.info(Units);
            
            sceneTotal = 1;
            scenePass = caseFail>0 ? 0 : 1;
            sceneFail = sceneTotal-scenePass-sceneSkip;
            sceneSkip = sceneTotal-scenePass-sceneFail;
            scenePassRate = String.format("%.2f%%", (double)(sceneTotal-sceneFail-sceneSkip)/sceneTotal*100);
            
            Json.put("sceneTotal", sceneTotal);
            Json.put("scenePass", scenePass);
            Json.put("sceneFail", sceneFail);
            Json.put("sceneSkip", sceneSkip);
            Json.put("scenePassRate", scenePassRate);
            Json.put("units", Units);
//            log.info(Json);
            
            try {
            	String Product_Name = ConfigUtil.getProperty("Product_Name", Constants.CONFIG_APP);
                String Product_Version =  ConfigUtil.getProperty("Product_Version", Constants.CONFIG_APP);
                String SysScene_UploadResult_API =  ConfigUtil.getProperty("SysScene_UploadResult_API", Constants.CONFIG_APP);
                String buildNumber =  ConfigUtil.getProperty("buildNumber", Constants.CONFIG_APP);
                String testPlanId =  ConfigUtil.getProperty("testPlanId", Constants.CONFIG_APP);
                 
                String ApiUrl = SysScene_UploadResult_API;
        		String Param = "{\n"
        				+ "  \"projectName\": \""+Product_Name+"\",\n"
        				+ "  \"versionName\": \""+Product_Version+"\",\n"
        				+ "  \"sceneId\": \""+testUnit.getId()+"\",\n"
        				+ "  \"testPlanId\": \""+testPlanId+"\",\n"
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
        				+ "      \"stepTotal\": "+stepTotal1+",\n"
        				+ "      \"stepPass\": "+stepPass1+",\n"
        				+ "      \"stepFail\": "+stepFail1+",\n"
        				+ "      \"stepSkip\": "+stepSkip1+",\n"
        				+ "      \"stepPassRate\": \""+stepPassRate1+"\",\n"
        				+ "      \"durationStartTime\": \""+durationStartTime+"\",\n"
        				+ "      \"durationEndTime\": \""+durationEndTime+"\"\n"
        				+ "    }\n"
        				+ "  }\n"
        				+ "}";
					HttpRequestUtil.SendPut(ApiUrl,Param);
					
					String TestReport_UploadResult_API =  ConfigUtil.getProperty("TestReport_UploadResult_API", Constants.CONFIG_APP);
				    String testReportId =  ConfigUtil.getProperty("testReportId", Constants.CONFIG_APP);
                    if(StringUtil.isNoEmpty(testReportId)){
                        ApiUrl = TestReport_UploadResult_API;
                        Param = "{\n"
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
                                + "      \"stepTotal\": "+stepTotal1+",\n"
                                + "      \"stepPass\": "+stepPass1+",\n"
                                + "      \"stepFail\": "+stepFail1+",\n"
                                + "      \"stepSkip\": "+stepSkip1+",\n"
                                + "      \"stepPassRate\": \""+stepPassRate1+"\",\n"
                                + "      \"durationStartTime\": \""+testReportdurationStartTime+"\",\n"
                                + "      \"durationEndTime\": \""+durationEndTime+"\"\n"
                                + "    }\n"
                                + "  }\n"
                                + "}";
                        HttpRequestUtil.SendPut(ApiUrl,Param);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
            UnitsIndex = UnitsIndex+1;
        } catch (JSONException e) {
            //e.printStackTrace();
            log.error("",e);
        }
    }

    /**
     * <br>
     * 执行完毕，关闭浏览器</br>
     */
    public void closeBrowser(){
        WebXmlParseService.QuitBrowser();
//        stopRecording();
        VideoRecorder.stop();
    }

    /**
     * <br>
     * 标记备注，一般展示在测试报告中</br>
     *
     * @param RemarksName
     */
    public void TestReportRemarks(String RemarksName) {
        Reporter.log(RemarksName);
    }

    /**
     * <br>
     * 标记备注，一般展示在测试报告中</br>
     *
     * @param command
     */
    public void AndroidAdb(String command) throws IOException {
        Runtime.getRuntime().exec(command);
    }

    public static void main(String[] args) throws Exception {

    }
}