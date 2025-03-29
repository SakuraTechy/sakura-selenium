package com.sakura.service;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.sakura.util.ConfigUtil;
import com.sakura.util.Constants;
import com.sakura.util.FreeSSHUtil;
import com.sakura.util.StringUtil;

import io.appium.java_client.android.AndroidDriver;

public class AppiumService {

    static Logger log = Logger.getLogger(AppiumService.class);

    AndroidDriver<WebElement> driver;
    public static String DeviceID;

    public static void appiumStop(String IP, String UserName, String PassWord) throws IOException, InterruptedException {
        // 创建或删除taskmgr计划任务,获取任务管理器的system权限
        // Runtime.getRuntime().exec("schtasks /create /tn taskmgr /sc daily /st 00:01 /tr taskmgr.exe");
        // Runtime.getRuntime().exec("schtasks /f /delete /tn taskmgr");

        // 强制结束node.exe进程
        try {
            if (StringUtil.isEqual(ConfigUtil.getProperty("Environment_Type", Constants.CONFIG_APP), "Linux")) {
                log.info("Appium服务关闭中,耐心等待ing...");
                FreeSSHUtil.cmd(IP, UserName, PassWord, "taskkill /f /IM node.exe");
            } else {
                log.info("Appium服务关闭中,耐心等待ing...");
                cmd("taskkill /f /IM node.exe");
                log.info("关闭所有cmd窗口...");
                cmd("wmic process where name='cmd.exe' call terminate");
            }
            Thread.sleep(500);
        } catch (Exception e) {
            log.info("Appium服务启动失败，请检查服务配置！");
            //e.printStackTrace();
            log.error("",e);
        }
    }

    public static void killProcess(String IP, String UserName, String PassWord) {
        try {
            if (StringUtil.isEqual(ConfigUtil.getProperty("Environment_Type", Constants.CONFIG_APP), "Linux")) {
                log.info("关闭所有cmd窗口...");
                FreeSSHUtil.cmd(IP, UserName, PassWord, "wmic process where name='cmd.exe' call terminate");
            } else {
                log.info("关闭所有cmd窗口...");
                cmd("wmic process where name='cmd.exe' call terminate");
            }
            Thread.sleep(500);
        } catch (Exception e) {
            log.info("Appium服务启动失败，请检查服务配置！");
            //e.printStackTrace();
            log.error("",e);
        }
    }

    public static void appiumStart(String IP, String Prot, String UserName, String PassWord, String Device) throws InterruptedException {
        try {
            // String AppiumStart = "cmd.exe /c start TestData/Appium/Appium.bat");
            // 多设备server端需要手动指定每台设备的udid,安卓无线连接下就是设备的ip:port..
            // String AppiumStart = "appium.cmd -p 4723 -bp 4724 --session-override --chromedriver-port 9515 -U
            // 10.18.88.33:8888 >C:/Users/King-liu/Desktop/4723.txt";

            if (StringUtil.isEqual(ConfigUtil.getProperty("Environment_Type", Constants.CONFIG_APP), "Linux")) {
                DeviceID = Device;
                String AppiumStart =
                    "node C:/Users/" + UserName + "/AppData/Local/Programs/Appium/resources/app/node_modules/appium/build/lib/main.js --address " + IP + " --port " + Prot + ">D:/appium.txt";
                log.info("Appium服务启动中，耐心等待ing...");
                FreeSSHUtil.cmd(IP, UserName, PassWord, AppiumStart);
            } else {
                String AppointLog = System.getProperty("user.dir") + "/Logs";
                String AppiumStart = "node C:/Users/" + UserName + "/AppData/Local/Programs/Appium/resources/app/node_modules/appium/build/lib/main.js --address " + IP + " --port " + Prot + ">"
                    + AppointLog + "/appium.txt";
                File file = new File(AppointLog);
                if (!file.exists()) {
                    file.mkdir();
                    log.info("创建目录：" + file);
                }
                log.info("Appium服务启动中，耐心等待ing...");
                cmd(AppiumStart);
            }
            Thread.sleep(5000);
            if (StringUtil.isEqual(ConfigUtil.getProperty("" + Device + "_Type", Constants.CONFIG_APP), "WIFI")) {
                log.info("Appium服务启动成功，开始连接手机设备...");
                FreeSSHUtil.cmd(IP, UserName, PassWord, "adb tcpip 8888");
                FreeSSHUtil.cmd(IP, UserName, PassWord, "adb disconnect");
                FreeSSHUtil.cmd(IP, UserName, PassWord, "adb connect " + ConfigUtil.getProperty("" + Device + "_DeviceID", Constants.CONFIG_APP) + "");
            }
            log.info("<--------------------------------------------------------------------------------------------------------------------------------------->");
        } catch (Exception e) {
            log.info("Appium服务启动失败，请检查服务配置！");
            //e.printStackTrace();
            log.error("",e);
        }
    }

    public static void cmd(String cmd) {
        try {
            log.info("cmd /c " + cmd);
            Runtime.getRuntime().exec("cmd /c " + cmd);
            // Process pr = Runtime.getRuntime().exec(cmd);
            // BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "utf-8"));
            // String line = input.readLine();
            // if (line != null) {
            // while (line != null) {
            // line = input.readLine();
            // log.info(line);
            // }
            // }
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("",e);
        }
    }

    public static void StartAppium(String IP, String Prot, String UserName, String PassWord, String Device) {
        try {
            appiumStop(IP, UserName, PassWord);
            killProcess(IP, UserName, PassWord);
            appiumStart(IP, Prot, UserName, PassWord, Device);
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
            log.error("",e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // StartAppium("10.18.22.32", "4723", "King-liu", "111111", "MI_8");
        StartAppium("10.18.22.65", "4723", "Administrator", "111111", "MI_9");

    }
}