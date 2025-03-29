package com.sakura.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 刘智King
 * @date 2020年9月17日 下午1:29:30
 */
public class MapUtil {
    static Logger log = Logger.getLogger(MapUtil.class);
    
    public static void main(String[] args) {

        Map<String, Object> map1 = new LinkedHashMap<String, Object>();
        map1.put("", "");
        map1.put("1.强制等待3秒", "1.png");
        map1.put("2.强制等待3秒", "2.png");
        map1.put("3.强制等待3秒", "3.png");
        log.info(map1);
        
        Map<String, Object> map2 = new LinkedHashMap<String, Object>();
        map2.put("", "");
        map2.put("11", "11.png");
        map2.put("22", "22.png");
        map2.put("33", "33.png");
        log.info(map2);
        
        // Map转String
        String str1 = getMapToString(map1);
        String str2 = getMapToString(map2);
        log.info(str1);
        log.info(str2);
        
        Map<Object, Object> map3 = new LinkedHashMap<Object, Object>();
        map3.put("case1", str1);
        map3.put("case2", str2);
        log.info(map3);
        log.info(map3.get("case1"));
        log.info(map3.get("case2"));
        for (Object cased : map3.keySet()) {
            log.info(cased + " " + map3.get(cased));
        }

        log.info(map3.get("case1").toString());
        Map<String, Object> map4 = getStringToMap(map3.get("case1").toString());
        for (Object cased : map4.keySet()) {
            log.info(cased + " " + map4.get(cased));
        }
        // Map转String
        String str3 = getMapToString(map4);
        log.info(str3);
        
        // String转map
        Map<String, Object> map5 = getStringToMap(str3);
        log.info(map5);
        for (Object cased : map5.keySet()) {
            log.info(cased + " " + map5.get(cased));
        }

        // String转map
        Map<String, Object> map6 = getStringToMap("查询数据库获=发送1=1, 查询数据库获=发送2=2, 查询数据库获=发送3=3");
        log.info(map6);
        for (Object cased : map6.keySet()) {
            log.info(cased + " " + map6.get(cased));
        }

        String str = "查询数据库获=发送=发发发发=1";
        String key = str.substring(0, str.lastIndexOf("="));
        String value = str.substring(str.lastIndexOf("=") + 1, str.length());
        log.info(key + " " + value);
        
        Map<String, Object> map7 = getStringToMap("查询数据库获=发送1=, 查询数据库获=发送2=, 查询数据库获=发送3=");
        log.info(getMapToJson(map7));
    }

    /**
     *
     * Map转String
     * 
     * @param map
     * @return
     */
    public static String getMapToString(Map<String, Object> map) {
        Set<String> keySet = map.keySet();
        // 将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        // 给数组排序(升序)
        // Arrays.sort(keyArray);
        // 因为String拼接效率会很低的，所以转用StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if ((String.valueOf(map.get(keyArray[i]))).trim().length() >= 0) {
                sb.append(keyArray[i]).append("=").append(String.valueOf(map.get(keyArray[i])).trim());
            }
            if (i != keyArray.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * 
     * String转map
     * 
     * @param str
     * @return
     */
    public static Map<String, Object> getStringToMap(String str) {
        // 根据逗号截取字符串数组
        String[] str1 = str.split(", ");
        // 创建Map对象
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        // 循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            // 根据":"截取字符串数组
            // String[] str2 = str1[i].split("=");
            // log.info(str2[0]);
            //// log.info(str2[0]);
            //// log.info(str2[1]);
            // //str2[0]为KEY,str2[1]为值
            // if(str2.length==0){
            // map.put("","");
            // }else if(str2.length==1){
            // map.put(str2[0],"");
            // }else{
            // map.put(str2[0],str2[1]);
            // }
            String key = str1[i].substring(0, str1[i].lastIndexOf("="));
            String value = str1[i].substring(str1[i].lastIndexOf("=") + 1, str1[i].length());
            map.put(key, value);
        }
        return map;
    }
    
    public static Map<String, Object> getStringToMap1(String str) {
        // 根据逗号截取字符串数组
        String[] str1 = str.split(", ");
        // 创建Map对象
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        // 循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            // 根据":"截取字符串数组
            // String[] str2 = str1[i].split("=");
            // log.info(str2[0]);
            //// log.info(str2[0]);
            //// log.info(str2[1]);
            // //str2[0]为KEY,str2[1]为值
            // if(str2.length==0){
            // map.put("","");
            // }else if(str2.length==1){
            // map.put(str2[0],"");
            // }else{
            // map.put(str2[0],str2[1]);
            // }
            String key = str1[i].substring(0, str1[i].indexOf("="));
            String value = str1[i].substring(str1[i].indexOf("=") + 1, str1[i].length());
            map.put(key, value);
        }
        return map;
    }
    
    public static JSONObject getMapToJson(Map<String, Object> map) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("a", "a");
//        map.put("b", "123");
//      JSONObject json = new JSONObject(Headers);
//      json.forEach((key,value)->{
//          log.info(key+" : "+value);
//          Headers.put(key, value);
//      });
        JSONObject json = new JSONObject(map);
        return json;
    }
}
