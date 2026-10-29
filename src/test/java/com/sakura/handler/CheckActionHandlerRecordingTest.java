package com.sakura.handler;

import java.lang.reflect.Proxy;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

/** 录制生成的统一元素断言在 Selenium 中的语义契约。 */
public class CheckActionHandlerRecordingTest {

    @Test
    public void supportsFourValueMatchModes() {
        Assert.assertTrue(CheckActionHandler.matchesElementAssertion("系统管理平台", "管理", "contains"));
        Assert.assertTrue(CheckActionHandler.matchesElementAssertion("系统管理平台", "系统管理平台", "equals"));
        Assert.assertTrue(CheckActionHandler.matchesElementAssertion("系统管理平台", "登录失败", "not_contains"));
        Assert.assertTrue(CheckActionHandler.matchesElementAssertion("系统管理平台", "^系统.*平台$", "regex"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*正则表达式不合法.*")
    public void rejectsInvalidRegex() {
        CheckActionHandler.matchesElementAssertion("系统管理平台", "[invalid", "regex");
    }

    @Test
    public void autoModeReadsFormValueAndTextModeReadsElementText() {
        WebElement input = element("input", "不可见文本", "account");
        WebElement div = element("div", "系统管理平台", null);

        Assert.assertEquals(CheckActionHandler.readElementAssertionValue(input, "auto"), "account");
        Assert.assertEquals(CheckActionHandler.readElementAssertionValue(input, "value"), "account");
        Assert.assertEquals(CheckActionHandler.readElementAssertionValue(div, "text"), "系统管理平台");
    }

    @Test
    public void attributeModeReadsHtmlAttributeWithoutFallingBackToText() {
        WebElement div = element("div", "错误文本", "icon-inner  running");

        Assert.assertEquals(CheckActionHandler.readElementAttributeValue(div, "class", attributeExecutor("icon-inner  running")), "icon-inner  running");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*缺少属性名.*")
    public void attributeModeRequiresAttributeName() {
        CheckActionHandler.readElementAttributeValue(element("div", "文本", null), " ", attributeExecutor(null));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*不存在属性.*")
    public void attributeModeRejectsMissingAttribute() {
        CheckActionHandler.readElementAttributeValue(element("div", "文本", null), "data-status", attributeExecutor(null));
    }

    @Test
    public void attributeModeKeepsEmptyAttributeDistinctFromMissing() {
        Assert.assertEquals(CheckActionHandler.readElementAttributeValue(element("div", "文本", null), "title", attributeExecutor("")), "");
    }

    private JavascriptExecutor attributeExecutor(String value) {
        return (JavascriptExecutor)Proxy.newProxyInstance(JavascriptExecutor.class.getClassLoader(),
            new Class<?>[] {JavascriptExecutor.class}, (proxy, method, args) -> {
                Assert.assertEquals(args[0], "return arguments[0].getAttribute(arguments[1]);");
                return value;
            });
    }

    private WebElement element(String tagName, String text, String value) {
        return (WebElement)Proxy.newProxyInstance(
            WebElement.class.getClassLoader(),
            new Class<?>[] {WebElement.class},
            (proxy, method, args) -> {
                if ("getTagName".equals(method.getName())) return tagName;
                if ("getText".equals(method.getName())) return text;
                if ("getAttribute".equals(method.getName())) return value;
                if ("isDisplayed".equals(method.getName())) return true;
                Class<?> returnType = method.getReturnType();
                if (returnType.equals(boolean.class)) return false;
                if (returnType.equals(int.class)) return 0;
                return null;
            }
        );
    }
}
