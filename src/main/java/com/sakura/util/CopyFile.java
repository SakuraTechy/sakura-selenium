package com.sakura.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;

/**
 * @author 刘智King
 * @date 2020年10月19日 下午3:19:58
 */
@SuppressWarnings("resource")
public class CopyFile {

    static Logger log = Logger.getLogger(CopyFile.class);
    
    public static void copy(String srcPathStr, String desPathStr) {
        // 获取源文件的名称
        String newFileName = srcPathStr.substring(srcPathStr.lastIndexOf("/") + 1); // 目标文件地址
//        log.info("源文件:" + newFileName);
        desPathStr = desPathStr +  newFileName; // 源文件地址
//        log.info("目标文件地址:" + desPathStr);
        JSchUtil.mkdirs(desPathStr);
        try {
            FileInputStream fis = new FileInputStream(srcPathStr);// 创建输入流对象
            FileOutputStream fos = new FileOutputStream(desPathStr); // 创建输出流对象
            byte datas[] = new byte[1024 * 8];// 创建搬运工具
            int len = 0;// 创建长度
            while ((len = fis.read(datas)) != -1)// 循环读取数据
            {
                fos.write(datas, 0, len);
            }
            fis.close();// 释放资源
            fis.close();// 释放资源
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("",e);
        }
    }

    public static void main(String[] args) {
        String srcPathStr = ""+System.getProperty("user.dir") + "/Logs/log.log"; // 源文件地址
        String data = DateUtil.getDateFormat("yyyy-MM-dd");
        String desPathStr = ""+System.getProperty("user.dir") + "/TestOutput/ExtentReport/"+data+"/"; 
        copy(srcPathStr, desPathStr);// 将E:\\java task\\zhl.txt文件拷贝到E:\\java task\\zhlll
    }
}
