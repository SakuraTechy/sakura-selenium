package com.sakura.util;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

public class SeleniumUtilLocatorTest {

    @Test
    public void shouldKeepBracketRelativeAndAbsoluteXpathUnchanged() {
        assertLocator("xpath=(//span[@class='user-title'])[1]", By.xpath("(//span[@class='user-title'])[1]"));
        assertLocator("//span[@class='user-title']", By.xpath("//span[@class='user-title']"));
        assertLocator("/html/body/main/span[1]", By.xpath("/html/body/main/span[1]"));
        assertLocator(".//span", By.xpath(".//span"));
    }

    @Test
    public void shouldSupportExplicitAndLegacyCss() {
        assertLocator("css=.user-title", By.cssSelector(".user-title"));
        assertLocator("cssSelector=.user-title", By.cssSelector(".user-title"));
        assertLocator("span.user-title", By.cssSelector("span.user-title"));
    }

    @Test
    public void shouldRejectPrivateLocatorStrategies() {
        for (String locator : new String[] {"jquery=$('.user-title')", "js=document.querySelector('.user-title')", "testrigor=系统管理平台"}) {
            try {
                SeleniumUtil.resolveLocator(locator);
                Assert.fail("Expected unsupported locator: " + locator);
            } catch (IllegalArgumentException error) {
                Assert.assertTrue(error.getMessage().contains("LOCATOR_STRATEGY_UNSUPPORTED"));
            }
        }
    }

    private void assertLocator(String raw, By expected) {
        Assert.assertEquals(expected.toString(), SeleniumUtil.resolveLocator(raw).toString());
    }
}
