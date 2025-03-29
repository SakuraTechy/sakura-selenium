package com.sakura.util;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;

import java.util.Map;

import org.apache.log4j.Logger;

import static io.restassured.RestAssured.given;

public class HttpRequestUtil {
	static Logger log = Logger.getLogger(HttpRequestUtil.class);
	
    /**
     * 指定API接口URL,POST请求参数,获取Cookie
     * @param ApiUrl
     * @param Param
     * @return
     * @throws Exception
     */
    public static Map<String, String> GetPostCookie(String ApiUrl, String Param) throws Exception{

        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .request()
                .body(Param)
                .when()
                .post(ApiUrl);

        response.print();
        Map<String, String> allCookies=response.getCookies();
        log.info("allCookies"+allCookies);

        return allCookies;
    }

    /**
     * 指定API接口URL,POST请求参数,获取Json结果
     * @param ApiUrl
     * @param Param
     * @param Cookie
     * @return
     * @throws Exception
     */
    public static String GetJsonResult(String ApiUrl, String Param, Map<String, String> Cookie) throws Exception{

        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .log().all()
                .request()
                .body(Param)
                .cookies(Cookie)
                .when()
                .post(ApiUrl);

        // 打印出 response 的body
//        response1.print();
        String result = response.asString();
        log.info("返回的值Json:"+result);

        return result;
    }

    /**
     * 指定API接口URL,POST请求参数,获取int类型值
     * @param ApiUrl
     * @param Param
     * @param Cookie
     * @return
     * @throws Exception
     */
    public static int GetJsonIntValue(String ApiUrl, String Param, Map<String, String> Cookie, String Value) throws Exception{

        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .log().all()
                .request()
                .body(Param)
                .cookies(Cookie)
                .when()
                .post(ApiUrl);


        int Value1 = response.jsonPath().getInt(Value);
        log.info("Value:"+Value1);

        return Value1;
    }

    /**
     * 指定API接口URL,POST请求参数,获取String类型值
     * @param ApiUrl
     * @param Param
     * @param Cookie
     * @return
     * @throws Exception
     */
    public static String GetJsonStringValue(String ApiUrl, String Param, Map<String, String> Cookie, String Value) throws Exception{

        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .log().all()
                .request()
                .body(Param)
                .cookies(Cookie)
                .when()
                .post(ApiUrl);

        String Value1 = response.jsonPath().get(Value);
        log.info("Value:"+Value1);

        return Value1;
    }

    /**
     * 指定API接口URL,POST请求参数,获取状态码
     * @param ApiUrl
     * @param Param
     * @param Cookie
     * @return
     * @throws Exception
     */
    public static int GetStatusCode(String ApiUrl, String Param, Map<String, String> Cookie) throws Exception{

        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .log().all()
                .request()
                .body(Param)
                .cookies(Cookie)
                .when()
                .post(ApiUrl);

        // 打印出 response 的statusCode
        int statusCode = response.getStatusCode();
        log.info("statusCode:" + statusCode);

        return statusCode;
    }
    
    /**
     * 指定API接口URL,发送POST请求
     * @param ApiUrl
     * @param Param
     * @return 
     * @return
     * @throws Exception
     */
    public static void SendPost(String ApiUrl, String Param) throws Exception{

        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .request()
                .body(Param)
                .when()
                .post(ApiUrl);
        
        // 打印出 response 的statusCode
        int statusCode = response.getStatusCode();
		log.info("『正常测试』开始执行: " + "<接口请求成功，状态码为："+statusCode+">");
//        log.info("statusCode:" + statusCode);
    }
    
    /**
     * 指定API接口URL,发送PUT请求
     * @param ApiUrl
     * @param Param
     * @return 
     * @return
     * @throws Exception
     */
    public static void SendPut(String ApiUrl, String Param) throws Exception{
        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .log().all()
                .request()
                .body(Param)
                .when()
                .put(ApiUrl);
        
        log.info("response:" + response.asString());
    }
}