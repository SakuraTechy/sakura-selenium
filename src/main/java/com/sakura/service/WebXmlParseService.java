package com.sakura.service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.sakura.util.*;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sakura.base.StepAction;
import com.sakura.base.TestCase;
import com.sakura.base.TestStep;
import com.sakura.base.TestUnit;

import static com.sakura.util.ScreenRecorderUtil.startRecording;

/**
 * <br>配置Selenium</br>
 * <br>解析xml到TestUnit</br>
 *
 * @version 1.0
 * @since   1.0
 */
 @SuppressWarnings("unused")
public class WebXmlParseService {
	private static final Logger log = LoggerFactory.getLogger(WebXmlParseService.class);

	public static WebDriver driver;
	private static final String WebDriver_Wait = ConfigUtil.getProperty("WebDriver_Wait", ConstantsUtil.CONFIG_COMMON);
	public static ThreadLocal<WebDriver> ThreadDriver = new ThreadLocal<WebDriver>() ;

	static String Product_Name = ConfigUtil.getProperty("Product_Name", Constants.CONFIG_APP);
	static String Product_Version =  ConfigUtil.getProperty("Product_Version", Constants.CONFIG_APP);

	/**
	 * <br>火狐浏览器配置</br>
	 *
	 * @throws Exception
	 */
	//【Selenium2.53.0 --> Firefox47.0.1】版本
	public static void FirefoxDriver(){
		//通过指定浏览器路径启动，默认不加载用户个人配置
		System.setProperty("webdriver.firefox.bin", ConfigUtil.getProperty("webdriver.firefox.bin", Constants.CONFIG_COMMON)); 
		driver = new FirefoxDriver();
//		driver .manage().window().maximize();//全屏
	}
	
	//【Selenium2.53.0 --> Firefox47.0.1】版本
	public static void AppointFirefoxDriver(){
		//通过指定浏览器路径启动，加载用户个人配置
		System.setProperty("webdriver.firefox.bin", ConfigUtil.getProperty("webdriver.firefox.bin", Constants.CONFIG_COMMON)); 
		File file = new File(ConfigUtil.getProperty("webdriver.firefox.profile", Constants.CONFIG_COMMON));
		FirefoxProfile profile = new FirefoxProfile(file);   
// 	    driver = new FirefoxDriver(profile); //使用旧版Selenium2.53.0，去掉注释，不需要设置FirefoxOptions，新版Selenium3.11.0^需要设置FirefoxOptions
// 	    driver .manage().window().maximize();//全屏
	}
	
	//【Selenium3.11.0^ --> Firefox47.0.1】版本
	public static void FirefoxMarionette(){
		//通过marionette获取geckodriver路径启动旧版火狐浏览器，默认不加载用户个人配置
		System.setProperty("webdriver.firefox.bin", ConfigUtil.getProperty("webdriver.firefox.bin", Constants.CONFIG_COMMON)); 
		System.setProperty("webdriver.firefox.marionette", ConfigUtil.getProperty("webdriver.gecko.driver", Constants.CONFIG_COMMON));
        driver = new FirefoxDriver();
//		driver .manage().window().maximize();//全屏
	}	
	
	//【Selenium3.11.0^ --> Firefox47.0.1】版本
	public static void AppointFirefoxMarionette(){
		//通过marionette获取geckodriver路径启动旧版火狐浏览器，加载用户个人配置
		System.setProperty("webdriver.firefox.bin", ConfigUtil.getProperty("webdriver.firefox.bin", Constants.CONFIG_COMMON)); 
		System.setProperty("webdriver.firefox.marionette", ConfigUtil.getProperty("webdriver.gecko.driver", Constants.CONFIG_COMMON));
		//FirefoxOptions设置Webdriver启动firefox为默认用户的配置信息（包括书签、扩展程序等）
 	    FirefoxOptions options = new FirefoxOptions();
		File file = new File(ConfigUtil.getProperty("webdriver.firefox.profile", Constants.CONFIG_COMMON));
	    FirefoxProfile profile = new FirefoxProfile(file);   
 	    options.setProfile(profile);
        driver = new FirefoxDriver(options);
//		driver .manage().window().maximize();//全屏
	}		
		
	//【Selenium3.11.0^ --> Firefox72.0.1^】版本
	public static void FirefoxGeckoDriver(){
		//通过获取geckodriver路径启动火狐浏览器，默认不加载用户个人配置
		System.setProperty("webdriver.firefox.bin", ConfigUtil.getProperty("webdriver.firefox.bin1", Constants.CONFIG_COMMON)); 
		System.setProperty("webdriver.gecko.driver", ConfigUtil.getProperty("webdriver.gecko.driver", Constants.CONFIG_COMMON));
		driver = new FirefoxDriver();
//		driver .manage().window().maximize();//全屏
	}
	
	//【Selenium3.11.0^ --> Firefox72.0.1^】版本
	public static void AppointFirefoxGeckoDriver(){
		//通过获取浏览器和geckodriver的路径启动火狐浏览器，加载用户个人配置
		System.setProperty("webdriver.firefox.bin", ConfigUtil.getProperty("webdriver.firefox.bin1", Constants.CONFIG_COMMON)); 
		System.setProperty("webdriver.gecko.driver", ConfigUtil.getProperty("webdriver.gecko.driver", Constants.CONFIG_COMMON));
		//FirefoxOptions设置Webdriver启动firefox为默认用户的配置信息（包括书签、扩展程序等）
 	    FirefoxOptions options = new FirefoxOptions();
		// 	    ProfilesIni pi = new ProfilesIni();
// 	    FirefoxProfile profile = pi.getProfile("default-release"); //个人配置文件名称
 	    File file = new File(ConfigUtil.getProperty("webdriver.firefox.profile1", Constants.CONFIG_COMMON));
	    FirefoxProfile profile = new FirefoxProfile(file);   
 	    options.setProfile(profile);
        driver = new FirefoxDriver(options);
// 	    driver.manage().window().maximize();//全屏
	}
	
	/**
	 * <br>谷歌浏览器配置</br>
	 *
	 * @throws Exception
	 */
	//【Chromedriver v2.41 --> Chrome v64-66^】版本
	public static void ChromeDriver(){
		//通过获取chromedriver路径启动谷歌浏览器，默认不加载用户个人配置
		System.setProperty("webdriver.chrome.bin", ConfigUtil.getProperty("webdriver.chrome.bin", Constants.CONFIG_COMMON)); 
		System.setProperty("webdriver.chrome.driver", ConfigUtil.getProperty("webdriver.chrome.driver", Constants.CONFIG_COMMON)); 
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--ignore-certificate-errors");
        option.setExperimentalOption("useAutomationExtension", false);
        option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        
        driver = new ChromeDriver(option);
        // 页面加载超时时间设置为20s
//        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
//        // 定位对象时给20s 的时间, 如果20s 内还定位不到则抛出异常
//        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//        // 异步脚本的超时时间设置成20s
//        driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
        // 页面全屏
		driver .manage().window().maximize();
	}
	
	//【Chromedriver v2.41 --> Chrome v64-66^】版本
	public static void AppointChromeDriver(){
		//通过获取chromedriver路径启动谷歌浏览器，加载用户个人配置
		System.setProperty("webdriver.chrome.bin", ConfigUtil.getProperty("webdriver.chrome.bin", Constants.CONFIG_COMMON)); 
		System.setProperty("webdriver.chrome.driver", ConfigUtil.getProperty("webdriver.chrome.driver", Constants.CONFIG_COMMON)); 

        //ChromeOptions设置Webdriver启动chrome为默认用户的配置信息（包括书签、扩展程序等）
        ChromeOptions option = new ChromeOptions();
        //通过ChromeOptions的setExperimentalOption方法，传下面两个参数来禁止掉谷歌受自动化控制的信息栏
        option.setExperimentalOption("useAutomationExtension", false);
        option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        //配置忽略HTTPS安全证书提示
        option.addArguments("--ignore-certificate-errors");
        //chrome://version/中的对应个人资料路径
        option.addArguments("--user-data-dir="+ConfigUtil.getProperty("webdriver.chrome.profile", Constants.CONFIG_COMMON)+""); 
        driver = new ChromeDriver(option);
		driver .manage().window().maximize();//全屏
	}

	/**
	 * <br>解析xml测试场景配置文件之前，对浏览器进行配置</br>
	 *
	 * @return
	 */
	public static TestUnit parse(String BrowserName,Boolean profile,String packagePath,String className) throws Exception {
//		startRecording();
		Rectangle rectangle1 = new Rectangle(0, 0, 1920, 1080);
		String filePath = System.getProperty("user.dir") +"/TestOutput/ExtentReport/"+DateUtil.getDateFormat("yyyy-MM-dd")+"/"+Product_Name+"/"+Product_Version+"/video/";
		VideoRecorder videoRecorder = new VideoRecorder(filePath,className,false,rectangle1);
		VideoRecorder.start();
		RunUnitService.durationStartTime = DateUtil.getDate();
		if("firefox".equals(BrowserName)){
			if(!profile){
				log.info("初始化火狐浏览器，不加载个人配置信息，耐心等待启动ing..."); 
//				FirefoxMarionette();//【Selenium3.11.0^ --> Firefox47.0.1】，不加载个人配置，启动最快...
				FirefoxGeckoDriver(); //【Selenium3.11.0^ --> Firefox72.0.1】，不加载个人配置，启动最快...
				ThreadDriver.set(driver); // 多线程并发启动浏览器
			}else{
				log.info("初始化火狐浏览器，加载个人配置信息，耐心等待启动ing..."); 
//				AppointFirefoxMarionette();//【Selenium3.11.0^ --> Firefox47.0.1】，加载个人配置
				AppointFirefoxGeckoDriver();// 【Selenium3.11.0^ --> Firefox72.0.1^】，加载个人配置
				ThreadDriver.set(driver); // 多线程并发启动浏览器
			}
		}else if("chrome".equals(BrowserName)){
			if(!profile){
				log.info("初始化谷歌浏览器，不加载个人配置信息，耐心等待启动ing..."); 
				ChromeDriver();//【Chromedriver v2.41 --> Chrome v64-66^】，不加载个人配置，启动最快...
				ThreadDriver.set(driver); // 多线程并发启动浏览器
			}else{
				log.info("初始化谷歌浏览器，加载个人配置信息，耐心等待启动ing..."); 
				AppointChromeDriver();//【Chromedriver v2.41 --> Chrome v64-66^】，加载个人配置，启动比较慢...
				ThreadDriver.set(driver); // 多线程并发启动浏览器
			}
		}
//		return parse(new File("src/test/java/TestCaseXml/"+XmlPath));
		String XmlPath = packagePath + className;
		return parse(new File("src/test/java/"+XmlPath.replace(".", "/").replace("TestCases", "TestCaseXml/")+".xml"));
	}
	
	/**
	 * <br>解析xml测试场景配置文件，转换为一个TestUnit实例，也就是一个TestCase的集合</br>
	 *
	 * @param xmlFile
	 * @return
	 */
	public static TestUnit parse(File xmlFile) {

		TestUnit testUnit = null;

		if ( xmlFile == null || !xmlFile.exists()  )
			return testUnit;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		
		try {
			builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(xmlFile);
			Element root = doc.getDocumentElement();
			NodeList cases = root.getElementsByTagName("case");
			
			//存放case的map
			LinkedHashMap<String, TestCase> caseMap = new LinkedHashMap<String, TestCase>();
			Element child;
			TestCase testCase;
			//逐个解析xml中的case元素
			for (int i = 0; i < cases.getLength(); i++) {
				child = (Element) cases.item(i);
				testCase = parseTestCase(child);
				if (testCase == null)
					continue;
				if (caseMap.containsKey(testCase.getId()))
					throw new RuntimeException("存在多个" + testCase.getId() + "，请检查id配置");
				else
					caseMap.put(testCase.getId(), testCase);
			}
			testUnit = new TestUnit();
			testUnit.setId(doc.getDocumentElement().getAttribute("id"));
			testUnit.setName(doc.getDocumentElement().getAttribute("name"));
//			log.info("<-----------------------------------------------------------【"+doc.getDocumentElement().getAttribute("name")+"】----------------------------------------------------------->");
			testUnit.setCaseMap(caseMap);
			deleteDirectory(testUnit.getName());
		} catch (Exception e) {
//			e.printStackTrace();
			log.error("",e);
		}
		return testUnit;
	}

	/**
	 * <br>将case元素解析为TestCase实例，也就是一个TestStep的集合</br>
	 *
	 * @param element 一个case元素
	 * @return
	 */
	private static TestCase parseTestCase(Element element) {
		if (element == null)
			return null;
		
		NamedNodeMap attrs = element.getAttributes();
		//根据case的属性实例化TestCase，并注入相应字段值
		TestCase TestCase = initByAttributes(attrs, new TestCase());
		
		NodeList stepNodes = element.getElementsByTagName("step");
		int len = stepNodes.getLength();
		List<TestStep> stepList = new ArrayList<TestStep>(len);
		
		Element stepNode;
		//逐个解析case元素的step子元素
		for (int i = 0; i < len; i++) {
			stepNode = (Element) stepNodes.item(i);
			stepList.add(parseTestStep(stepNode));
		}
		TestCase.setSteps(stepList);
		
		return TestCase;
	}

	/**
	 * <br>解析step元素为TestStep实例</br>
	 *
	 * @param element
	 * @return
	 */
	private static TestStep parseTestStep(Element element) {
		if (element == null)
			return null;
		TestStep TestStep = initByAttributes(element.getAttributes(), new TestStep());
		TestStep.setWebDriver(driver);
		TestStep.setWebDriverWait(new WebDriverWait(driver, Long.parseLong(WebDriver_Wait)));
		return TestStep;
	}

	/**
	 * <br>根据xml文件中的元素属性为对象的对应字段注入值</br>
	 *
	 * @param attrs
	 * @param t 需要实例化并注入字段值的对象
	 * @return
	 */
	private static <T> T initByAttributes(NamedNodeMap attrs, T t) {
		if (attrs == null || attrs.getLength() == 0)
			return t;
		
		int len = attrs.getLength();
		Node attr;
		String name, value;
		
		//通过反射逐个注入字段值
		for (int i = 0; i < len; i++) {
			attr = attrs.item(i);
			if (attr == null) continue;
			
			name = attr.getNodeName();
			value = attr.getNodeValue();
			//通过反射为对象的对应字段注入值
			initByReflect(name, value, t);
		}
		return t;
	}

	/**
	 * <br>通过反射为对象的对应字段注入值</br>
	 *
	 * @param name
	 * @param value
	 * @param t
	 * @return
	 */
	private static <T> T initByReflect(String name, String value, T t) {
		if (t == null)
			throw new RuntimeException("null object");
		if (name == null || "".equals(name))
			throw new RuntimeException("empty name");
		
		Class<?> clazz = t.getClass();
		Method setter, getter;

		try {
			String methodStr = name.substring(0, 1).toUpperCase() + name.substring(1);
			
			//如果名称是cancel，则调用isCancel()方法，主要是为了语义上的直观
			getter = clazz.getMethod(("cancel".equals(name) ? "is" : "get") + methodStr, new Class<?>[] {});
			setter = clazz.getMethod("set" + methodStr, getter.getReturnType());
			
			if ("action".equals(name))
				//根据StepAction类中的map来获取名称对应的StepAction（枚举）实例
				setter.invoke(t, StepAction.action(value));
			else if ("cancel".equals(name))
				setter.invoke(t, "true".equals(value) ? true : false);
			else if("details".equals(name))
				setter.invoke(t,parseDetail(value));
			else
				setter.invoke(t, value);
		} catch (Exception e) {
//			log.info("对象反射初始化失败");
//			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * <br>解析行为的细节描述为map</br>
	 *
	 * @author    102051
	 * @date      2017年7月28日 上午11:01:14
	 * @param detail
	 * @return
	 */
	public static Map<String,String> parseDetail(String detail){
		HashMap<String,String> map = new HashMap<>();
		String[] strarr = detail.split(";");
		
		for(String str : strarr){
			map.put(str.split(":")[0], str.split(":")[1]);
		}
		return map;
	}
	
	/**
	 * <br>根据用例的名称，截取图片，进行保存</br>
	 *
	 * @param ScreenshotName
	 */
	public static void screenShot(String CaseID) {

		int t = 1;
		String AppointDir = System.getProperty("user.dir")+"\\TestOutput\\ExtentReport\\screenshot\\";
		String picture = AppointDir + CaseID + ".png";

		File file = new File(AppointDir);
		File[] fs = file.listFiles();
		File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		// 指定图片数量，过多删除
		try {
			if (fs.length >= 300) {
				for (File f : fs) {
					if (f.getName().contains("png"))
						f.delete();
				}
			}

			FileUtils.copyFile(screenShot, new File(picture));
			++t;
		} catch (IOException e) {
			log.info("截图失败:" + CaseID);
			e.printStackTrace();
		} finally {
			log.info("『发现问题』开始执行: " + "<截图操作,保存目录为[" + picture + "]>");
		}
	}
	
	/**
	 * <br>根据用例的名称，截取图片，进行保存</br>
	 *
	 * @param UnitName,CaseName,StepID
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static void screenShot(String UnitName,String CaseName,String StepID) throws Exception{

	    String data = DateUtil.getDateFormat("yyyy-MM-dd");
	    String Product_Name = ConfigUtil.getProperty("Product_Name", Constants.CONFIG_APP);
	    String Product_Version =  ConfigUtil.getProperty("Product_Version", Constants.CONFIG_APP);
	    String AppointDir = System.getProperty("user.dir")+"/TestOutput/ExtentReport/"+data+"/"+Product_Name+"/"+Product_Version+"/screenshot/"+UnitName+"/" + CaseName+"/";
		String picture = AppointDir + StepID + "异常.png";
		try {
		    File file = new File(AppointDir);
		    File[] fs = file.listFiles();
		    //图片大于300时，删除清空
	        if (fs!=null&&fs.length >= 300) { 
	            for (File f : fs) {
	                if (f.getName().contains("png"))
	                    f.delete();
	            }
	        }
			File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        FileUtils.copyFile(screenShot, new File(picture));
//			FileUtils.copyFile(new File("D:\\King\\Eclipse\\Sunline.finline.Android.Test\\TestOutput\\ExtentReport\\456.jpg"), new File(picture));
	        log.info("『发现问题』开始执行: " + "<截图操作,保存目录为[" + picture + "]>");
		} catch (Exception e) {
//			e.printStackTrace();
			log.error("",e);
			Thread.sleep(500);
		} finally {
		    if(StringUtil.isEqual(ConfigUtil.getProperty("Environment_Type", Constants.CONFIG_APP), "Windows")&&StringUtil.isNoEmpty(ConfigUtil.getProperty("ExtentReport_Type", Constants.CONFIG_APP))){
	            AppointDir = ConfigUtil.getProperty("ExtentReport_URL", Constants.CONFIG_APP)+"/"+ConfigUtil.getProperty("ExtentReport_BUILD_NUMBER", Constants.CONFIG_APP)+"/"+ConfigUtil.getProperty("ExtentReport_Type", Constants.CONFIG_APP)+"/TestOutput/ExtentReport/"+data+"/"+Product_Name+"/"+Product_Version+"/screenshot/"+UnitName+"/" + CaseName+"/";
	            picture = AppointDir + StepID + "异常.png";
	            RunUnitService.Step.put("picture",picture);
		    }else if(StringUtil.isEqual(ConfigUtil.getProperty("Environment_Type", Constants.CONFIG_APP), "Local")){
	            RunUnitService.Step.put("picture",picture);
	        }
		}
	}
	
	/**
     * <br>删除当天的BUG截图目录</br>
     *
     * @param command
     */
    public static void deleteDirectory(String UnitName) throws IOException{
        String Data = DateUtil.getDateFormat("yyyy年MM月dd日");
        String AppointDir = System.getProperty("user.dir")+"/TestOutput/ExtentReport/screenshot/"+Data+"/"+UnitName+"/";
        FileUtils.deleteDirectory(new File(AppointDir));
    }
    
	/**
	 * <br>执行完毕，关闭浏览器</br>
	 */
	public static void QuitBrowser() {
		try {
//			driver.quit();
			ThreadDriver.get().quit();
			ThreadDriver.remove();
//			RunUnitService.Unit.put("durationEndTime", DateUtil.getDate());
			log.info("『测试结束』开始执行: <结束进程，关闭浏览器>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
