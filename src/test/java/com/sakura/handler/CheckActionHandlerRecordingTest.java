package com.sakura.handler;

import java.lang.reflect.Proxy;

import org.openqa.selenium.WebElement;
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
