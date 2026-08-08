package com.sakura.handler;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.alibaba.fastjson.JSONObject;
import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/** 使用真实 Chrome 验证统一元素断言 handler；仅在显式提供匹配驱动时运行。 */
public class CheckActionHandlerBrowserTest {

    @Test
    public void executesRecordedElementAssertionsInRealChrome() throws Exception {
        String driverPath = System.getProperty("sakura.selenium.integration.chromedriver", "").trim();
        if (driverPath.isEmpty()) throw new SkipException("未提供匹配的 ChromeDriver");
        System.setProperty("webdriver.chrome.driver", driverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox");
        WebDriver driver = new ChromeDriver(options);
        try {
            String html = "<!doctype html><html><body>"
                + "<div id='title'>系统管理平台</div>"
                + "<input id='account' value='admin-001'>"
                + "<div id='hidden' style='display:none'>不可见</div>"
                + "</body></html>";
            String encoded = Base64.getEncoder().encodeToString(html.getBytes(StandardCharsets.UTF_8));
            driver.get("data:text/html;charset=utf-8;base64," + encoded);

            RunUnitService.Step = new JSONObject(true);
            RunUnitService.softAssert = new SoftAssert();
            RunUnitService.stepFail = 0;
            runAssertion(driver, "cssSelector=#title", "text", "contains", "管理");
            runAssertion(driver, "cssSelector=#title", "text", "equals", "系统管理平台");
            runAssertion(driver, "cssSelector=#title", "text", "not_contains", "登录失败");
            runAssertion(driver, "cssSelector=#title", "text", "regex", "^系统.*平台$");
            runAssertion(driver, "cssSelector=#title", "auto", "visible", "");
            runAssertion(driver, "cssSelector=#account", "value", "equals", "admin-001");
            Assert.assertEquals(RunUnitService.stepFail, 0);

            try {
                new WebDriverWait(driver, 1).until(ExpectedConditions.visibilityOfElementLocated(By.id("hidden")));
                Assert.fail("display:none 元素不应通过 Selenium 可见性等待");
            } catch (TimeoutException expected) {
                Assert.assertFalse(driver.findElement(By.id("hidden")).isDisplayed());
            }
        } finally {
            driver.quit();
        }
    }

    private void runAssertion(WebDriver driver,
                              String locator,
                              String readMode,
                              String matchMode,
                              String expect) throws Exception {
        TestStep step = new TestStep();
        step.setId("1");
        step.setName("统一元素断言");
        step.setMessage("统一元素断言失败");
        step.setLocator(locator);
        step.setRead_mode(readMode);
        step.setMatch_mode(matchMode);
        step.setExpect(expect);
        step.setParseEls("false");
        step.setTimeout("3");
        step.setWebDriver(driver);
        step.setWebDriverWait(new WebDriverWait(driver, 3));
        new CheckActionHandler().webAssertElementMatch(step);
    }
}
