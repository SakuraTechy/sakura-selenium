package BD.V1_0_0.TestCases;

import com.sakura.base.TestUnit;
import com.sakura.service.RunUnitService;
import com.sakura.service.WebXmlParseService;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class BD_SMOKE_001 {
  private static TestUnit testUnit;

  private static WebXmlParseService webXmlParseService;

  private static RunUnitService runService;

  @Parameters({"browser","profile"})
  @BeforeTest
  private void setup(String browserName, Boolean profile) throws Exception {
    TestUnit testunit = WebXmlParseService.parse(browserName,profile,this.getClass().getPackage().getName(),this.getClass().getSimpleName());
    runService = new RunUnitService(testunit);
  }

  @Test(
      groups = {"BD_001"}
  )
  public void BD_001() throws Exception {
    runService.runCase(Thread.currentThread() .getStackTrace()[1].getMethodName());
  }

  @Test(
      groups = {"BD_002"}
  )
  public void BD_002() throws Exception {
    runService.runCase(Thread.currentThread() .getStackTrace()[1].getMethodName());
  }

  @AfterTest
  public void TearDown() {
    runService.setUnit(true);
  }
}
