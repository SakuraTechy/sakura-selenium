package TestCases;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.jmoney.luckeylink.base.TestUnit;
import com.jmoney.luckeylink.service.RunUnitService;
import com.jmoney.luckeylink.service.WebXmlParseService;

public class SearchProcess {
	
	private static RunUnitService runService;

	@Parameters({"browser","profile"})
	@BeforeTest
	private void stup(String BrowserName,Boolean profile) throws Exception{
		TestUnit testunit = WebXmlParseService.parse(BrowserName,profile,"SearchProcess.xml");
		runService = new RunUnitService(testunit);
		System.out.println("----------------------------------------【百度-搜索自动化测试系统流程】---------------------------------------");
	}
	
//	@Test
//	public void Precondition() throws Exception{
//		runService.runCase("前置条件");
//	}
	
	@Test
	public void case1() throws Exception{
		runService.runCase("case1");
	}
	
	@Test
	public void case2() throws Exception{
		runService.runCase("case2");
	}
	
	@AfterTest
	public void TearDown(){
		runService.closeBrowser();
	}
}
