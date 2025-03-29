package com.sakura.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtil {

    static Logger log = Logger.getLogger(ConfigUtil.class);
    
    public static String getProperty(String key, String config) {
        String value = null;
        try {
            if (key == null || "".equals(key)) {
                log.info("请传入正确的key信息");
                return null;
            }

            if (config == null || "".equals(config)) {
                log.info("请传人正确的配置文件信息");
                return null;
            }
            value = new String(loadConfig(config).getProperty(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
            log.error("",e);
        }
//        log.info("读取" + key + "=> " + value);
        return value;
    }

    public static String readPropertie(String key, String config) {
        Properties props = new OrderedProperties();
        String value = null;
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(config));
            props.load(in);
            value = new String(props.getProperty(key).getBytes("ISO-8859-1"), "UTF-8");
//            log.info("读取" + key + "=> " + value);
            in.close();
        } catch (Exception e) {
            log.error("",e);
        }
        return value;
    }

    public static void readProperties(String config) {
        Properties props = new OrderedProperties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(config));
            props.load(in);
            Iterator<String> it = props.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                log.info("读取" + key + "=> " + props.getProperty(key));
            }
            in.close();
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * 更新properties文件的键值对 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插件一对键值。
     *
     * @param key 键名
     * @param value 键值
     */
    public static void updateProperties(String key, String value, String config) {
        Properties props = new OrderedProperties();
        try {
            props.load(new FileInputStream(ConstantsUtil.CONFIG_COMMON));
            OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(config, false), "UTF-8");
            props.setProperty(key, value);
//            log.info("写入" + key + "=> " + value);
            props.store(fos, "Update '" + key + "'");
            fos.flush();
//            updateProperties(profilepath);
        } catch (IOException e) {
            log.error("属性文件更新错误", e.fillInStackTrace());
        }
    }

    @SuppressWarnings("resource")
    public static void updateProperties(String config) {
        Properties props = new OrderedProperties();
        try {
            OutputStream fos = new FileOutputStream(config);
            // 这里不使用Properties的store()方法，因为冒号会被转义
            Enumeration<?> e = props.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String)e.nextElement();
                String value = props.getProperty(key);
                String s = key + "=" + value + "\n";
                fos.write(s.getBytes());
            }
            fos.flush();
        } catch (IOException e) {
            log.error("属性文件更新错误", e.fillInStackTrace());
        }
    }

    private static Properties loadConfig(String config) {
        Properties props = new OrderedProperties();
        // InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream(config);
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(config));
            props.load(in);
            in.close();
        } catch (Exception e) {
            log.error("配置文件加载失败", e.fillInStackTrace());
        }
        return props;
    }

    // 读取资源文件,并处理中文乱码
    public static Properties readPropertiesFileObj(String filename) {
        Properties properties = new OrderedProperties();
        try {
            InputStream inputStream = new FileInputStream(filename);
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            properties.load(bf);
            inputStream.close(); // 关闭流
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("",e);
        }
        return properties;
    }
    
    // 写资源文件，含中文
    public static void writePropertiesFileObj(String filename, Properties properties) {
        if (properties == null) {
            properties = new OrderedProperties();
        }
        try {
            OutputStream outputStream = new FileOutputStream(filename);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            properties.setProperty("username1" + Math.random(), "myname");
            properties.setProperty("password2" + Math.random(), "mypassword");
            properties.setProperty("chinese3" + Math.random(), "中文");
            properties.store(bw, null);
            outputStream.close();
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("",e);
        }
    }
    
    public static void main(String[] args){
//        String readfile = "src/main/java/common.properties";
//        Properties pro = readPropertiesFileObj(readfile); // 读取properties文件
//        log.info(pro.getProperty("loginDecrypt"));
//        pro.remove("loginDecrypt");
//        writePropertiesFileObj(readfile,pro); // 写properties文件
        
        getProperty("privateKey", ConstantsUtil.CONFIG_COMMON);
        readPropertie("loginDecrypt", ConstantsUtil.CONFIG_COMMON);
        updateProperties("SERVER_PUBLIC_KEY", "22", ConstantsUtil.CONFIG_COMMON);
//        updateProperties("publicKey", "22", ConstantsUtil.CONFIG_COMMON);
//        updateProperties("xencryptdata", "33", ConstantsUtil.CONFIG_COMMON);
//        updateProperties("privateKey", "44", ConstantsUtil.CONFIG_COMMON);
//        updateProperties("ref_tokenId", "55", ConstantsUtil.CONFIG_COMMON);
//        updateProperties("loginDecrypt", "66", ConstantsUtil.CONFIG_COMMON);
    }
}
