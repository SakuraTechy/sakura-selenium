package com.sakura.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
public class GetInfo {
  /**

  * windows和Linux，mac下结果不一样

  */
  public static void getIpconfig() {
      Map < String, String > map = System.getenv();
      System.out.println(map);
      /*windows*/
      System.out.println(map.get("USERNAME")); //获取用户名
      System.out.println(map.get("COMPUTERNAME")); //获取计算机名
      System.out.println(map.get("USERDOMAIN")); //获取计算机域名
      /*mac*/
      System.out.println(map.get("USER"));
    }
    /**

    * Java的运行环境版本：1.6.0_33

    Java的运行环境供应商：Apple Inc.

    Java供应商的URL：http://www.apple.com/

    Java的安装路径：/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home

    Java的虚拟机规范版本：1.0

    Java的虚拟机规范供应商：Sun Microsystems Inc.

    Java的虚拟机规范名称：Java Virtual Machine Specification

    Java的虚拟机实现版本：20.8-b03-424

    Java的虚拟机实现供应商：Apple Inc.

    Java的虚拟机实现名称：Java HotSpot(TM) 64-Bit Server VM

    Java运行时环境规范版本：1.6

    Java运行时环境规范供应商：null

    Java运行时环境规范名称：Java Platform API Specification

    Java的类格式版本号：50.0

    Java的类路径：/Users/mc2/WEB/JSP/Javaer/bin:/Users/mc2/WEB/JSP/Javaer/lib/mongo-2.9.0-RC2.jar:/Users/mc2/WEB/JSP/Javaer/lib/bbCommon1.2.jar:/Users/mc2/WEB/JSP/Javaer/lib/jedis-2.1.0.jar

    加载库时搜索的路径列表：.:/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java

    默认的临时文件路径：/var/folders/-E/-EZ40U6QGn06ccbzahOZME+++TI/-Tmp-/

    一个或多个扩展目录的路径：/Library/Java/Extensions:/System/Library/Java/Extensions:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/ext

    操作系统的名称：Mac OS X

    操作系统的构架：x86_64

    操作系统的版本：10.6.8

    文件分隔符：/

    路径分隔符：:

    行分隔符：

    用户的账户名称：mc2

    用户的主目录：/Users/mc2

    用户的当前工作目录：/Users/mc2/WEB/JSP/Javaer

    */
    //其它的一些东西,会有用到的时候的
  public static void all() {
      Properties props = System.getProperties();
      System.out.println("Java的运行环境版本：" + props.getProperty("java.version"));
      System.out.println("Java的运行环境供应商：" + props.getProperty("java.vendor"));
      System.out.println("Java供应商的URL：" + props.getProperty("java.vendor.url"));
      System.out.println("Java的安装路径：" + props.getProperty("java.home"));
      System.out.println("Java的虚拟机规范版本：" + props.getProperty("java.vm.specification.version"));
      System.out.println("Java的虚拟机规范供应商：" + props.getProperty("java.vm.specification.vendor"));
      System.out.println("Java的虚拟机规范名称：" + props.getProperty("java.vm.specification.name"));
      System.out.println("Java的虚拟机实现版本：" + props.getProperty("java.vm.version"));
      System.out.println("Java的虚拟机实现供应商：" + props.getProperty("java.vm.vendor"));
      System.out.println("Java的虚拟机实现名称：" + props.getProperty("java.vm.name"));
      System.out.println("Java运行时环境规范版本：" + props.getProperty("java.specification.version"));
      System.out.println("Java运行时环境规范供应商：" + props.getProperty("java.specification.vender"));
      System.out.println("Java运行时环境规范名称：" + props.getProperty("java.specification.name"));
      System.out.println("Java的类格式版本号：" + props.getProperty("java.class.version"));
      System.out.println("Java的类路径：" + props.getProperty("java.class.path"));
      System.out.println("加载库时搜索的路径列表：" + props.getProperty("java.library.path"));
      System.out.println("默认的临时文件路径：" + props.getProperty("java.io.tmpdir"));
      System.out.println("一个或多个扩展目录的路径：" + props.getProperty("java.ext.dirs"));
      System.out.println("操作系统的名称：" + props.getProperty("os.name"));
      System.out.println("操作系统的构架：" + props.getProperty("os.arch"));
      System.out.println("操作系统的版本：" + props.getProperty("os.version"));
      System.out.println("文件分隔符：" + props.getProperty("file.separator"));
      //在 unix 系统中是＂／＂
      System.out.println("路径分隔符：" + props.getProperty("path.separator"));
      //在 unix 系统中是＂:＂
      System.out.println("行分隔符：" + props.getProperty("line.separator"));
      //在 unix 系统中是＂/n＂
      System.out.println("用户的账户名称：" + props.getProperty("user.name"));
      System.out.println("用户的主目录：" + props.getProperty("user.home"));
      System.out.println("用户的当前工作目录：" + props.getProperty("user.dir"));
    }
    /**

    * 得到计算机的ip,名称,操作系统名称,操作系统版本

    * 本机IP：127.0.0.1

    本机名称:localhost

    操作系统的名称：Mac OS X

    操作系统的版本：10.6.8

    */
  public static void Config() {
      try {
        InetAddress addr = InetAddress.getLocalHost();
        String ip = addr.getHostAddress().toString(); //获取本机ip
        String hostName = addr.getHostName().toString(); //获取本机计算机名称
        System.out.println("本机IP：" + ip + "\n本机名称:" + hostName);
        Properties props = System.getProperties();
        System.out.println("操作系统的名称：" + props.getProperty("os.name"));
        System.out.println("操作系统的版本：" + props.getProperty("os.version"));
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    /**

    * 得到计算机的ip地址和mac地址

    * IP：127.0.0.1

    * MAC：FE-80-00-00-00-00-00-00-00-00-00-00-00-00-00-01

    */
  @SuppressWarnings("resource")
public static void getConfig() {
      try {
        InetAddress address = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(address);
        //ni.getInetAddresses().nextElement().getAddress();
        byte[] mac = ni.getHardwareAddress();
        if(mac == null) {
          mac = ni.getInetAddresses().nextElement().getAddress();
        }
        String sIP = address.getHostAddress();
        String sMAC = "";
        Formatter formatter = new Formatter();
        for(int i = 0; i < mac.length; i++) {
          sMAC = formatter.format(Locale.getDefault(), "%02X%s", mac[i], (i < mac.length - 1) ? "-" : "").toString();
        }
        System.out.println("IP：" + sIP);
        System.out.println("MAC：" + sMAC);
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    /**

    * @param args

    */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    GetInfo.all();
  }
}
