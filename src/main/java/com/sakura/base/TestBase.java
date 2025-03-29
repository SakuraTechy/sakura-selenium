package com.sakura.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import io.appium.java_client.android.AndroidDriver;
import lombok.Data;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * <br>对应于XML文件中的Case元素</br>
 *
 * @version 1.0
 * @since   1.0
 */
@Data
public class TestBase {
	
	private String id;

    private String name;

    private boolean cancel;

    private WebDriver webDriver;

    private WebDriverWait webDriverWait;

    private AndroidDriver<WebElement> AndroidDriver;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//    
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public boolean getCancel() {
//        return cancel;
//    }
//
//    public void setCancel(boolean cancel) {
//        this.cancel = cancel;
//    }
//    
//    public WebDriver getWebDriver() {
//        return Wdriver;
//    }
//
//    public void setWebDriver(WebDriver driver) {
//        this.Wdriver = driver;
//    }
//    
//    public AndroidDriver<WebElement> getAndroidDriver() {
//        return Adriver;
//    }
//
//    public void setAndroidDriver(AndroidDriver<WebElement> driver) {
//        this.Adriver = driver;
//    }
}