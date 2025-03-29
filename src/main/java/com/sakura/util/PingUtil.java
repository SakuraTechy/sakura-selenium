package com.sakura.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

/**
 * @Author： 刘智
 * @Date：2022年9月29日 18:29:34
 */
public class PingUtil {
	static Logger log = Logger.getLogger(PingUtil.class);

    /**ping  ipaddress 完整返回信息*/
    public static String executeLinuxCmd(String ipAddress, int pingTimes, int timeOut) {
        Runtime run = Runtime.getRuntime();
        String pingCommand;
        try {
            String osName = System.getProperty("os.name");
            if(osName.contains("Windows")){
                pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;
            }else{
                pingCommand = "ping " + " -c " + "4" + " -w " + "2 " + ipAddress;
            }
            Process process = run.exec(pingCommand);
            InputStream in = process.getInputStream();
            BufferedReader bs = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
            StringBuffer out = new StringBuffer();
            String content = null;
            while ((content = bs.readLine()) != null) {
                out.append(content + "\n");
            }
            in.close();
            process.destroy();
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**ping  ipaddress 完整返回true在线 false离线*/
    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        BufferedReader in = null;
        String pingCommand;
        Runtime r = Runtime.getRuntime();
        String osName = System.getProperty("os.name");
        if(osName.contains("Windows")){
            pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;
        }else{
            pingCommand = "ping " + " -c " + "4" + " -w " + "2 " + ipAddress;
        }
        try {
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int connectedCount = 0;
            String line;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line,osName);
            }
            return connectedCount >= 2 ? true : false;
        } catch (Exception ex) {
            ex.printStackTrace(); //出现异常则返回假
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static int getCheckResult(String line,String osName) {
        if(osName.contains("Windows")){
            if(line.contains("TTL=")){
                return 1;
            }
        }else{
            if(line.contains("ttl=")){
                return 1;
            }
        }
        return 0;
    }

    /**
	 * Ping IP
	 * 
	 * @return Inet4Address>
	 */
	public static boolean ping(String ip){
        Runtime runtime = Runtime.getRuntime(); // 获取当前程序的运行进对象
        Process process = null; // 声明处理类对象
        String line = null; // 返回行信息
        InputStream is = null; // 输入流
        InputStreamReader isr = null; // 字节流
        BufferedReader br = null;
        boolean res = false;// 结果
        try {
            process = runtime.exec("ping " + ip); // PING

            is = process.getInputStream(); // 实例化输入流
            isr = new InputStreamReader(is);// 把输入流转换成字节流
            br = new BufferedReader(isr);// 从字节中读取文本
            while ((line = br.readLine()) != null) {
                if (line.contains("TTL") || line.contains("ttl")) {
                    res = true;
                    break;
                }
            }
            is.close();
            isr.close();
            br.close();
            if (res) {
//                System.out.println("ping 通  ...");
                return true;

            } else {
//                System.out.println("ping 不通...");
                return false;
            }
        } catch (IOException e) {
            System.out.println(e);
            runtime.exit(1);
        }
        return false;
    }
	
    public static void main(String args[]) {
//    	ping("172.19.5.10");
    	
        String ping = executeLinuxCmd("172.19.5.10", 2, 1);
        log.info(ping);

        boolean ping1 = ping("172.19.5.10", 2, 1);
        log.info("IP地址：" + "172.19.5.10" + (ping1 ? "在线" : "下线"));
    }
}