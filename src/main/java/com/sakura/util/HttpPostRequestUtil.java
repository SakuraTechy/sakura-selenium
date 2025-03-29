package com.sakura.util;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import static io.restassured.RestAssured.given;

public class HttpPostRequestUtil {
    static Logger log = Logger.getLogger(HttpPostRequestUtil.class);
    
	/**
     * 指定API接口URL,POST请求参数,获取Cookie
     * @param ApiUrl
     * @param Param
     * @return Cookie
     */
    public static Map<String, String> GetCookie(String ApiUrl, String Param) throws Exception{

        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .request()
                .body(Param)
                .when()
                .post(ApiUrl);

        response.print();
        Map<String, String> Cookie=response.getCookies();
        log.info("Cookie"+Cookie);

        return Cookie;
    }
    
	   /**
	   * 指定API接口URL,POST请求参数,获取ToKen
	   * @param ApiUrl
	   * @param Param
	   * @return ToKen
	   */
	  public static String GetToKenId(String ApiUrl, String Body){
	      
	      Response response = given()
	              .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
	              .contentType("application/json")
	              .log().all()
	              .request()
	              .body(Body)
	              .when()
	              .post(ApiUrl);

	      log.info("Response:");
	      response.print();
	      
	      String tokenId = response.jsonPath().get("robj.tokenId");
//	      log.info("tokenId： " +tokenId);
	      log.info("<--------------------------------------------------------------------- 分    割    线 --------------------------------------------------------------------->");
	      
	      return tokenId;
	  }

	   /**
	   * 指定API接口URL,POST请求参数,获取Message
	   * @param ApiUrl
	   * @param Param
	   * @return Message
	   */
	  public static String GetMessag(String ApiUrl, String Param){

	      Response response = given()
	              .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
	              .contentType("application/json")
	              .log().all()
	              .request()
	              .body(Param)
	              .when()
	              .post(ApiUrl);

	       String Message = response.jsonPath().get("message");
	       log.info(Message);

	      return Message;
	  }

	  /**
	   * 指定API接口URL,POST请求参数,获取StatusCode
	   * @param ApiUrl
	   * @param Param
	   * @return StatusCode
	   */
	  public static int GetStatusCode(String ApiUrl, String Param){

	      Response response = given()
	              .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
	              .contentType("application/json")
	              .log().all()
	              .request()
	              .body(Param)
	              .when()
	              .post(ApiUrl);

	      int StatusCode = response.jsonPath().get("status_code");
	      log.info(StatusCode);

	      return StatusCode;
	  }
	  
   /**
    * 指定API接口URL,POST请求参数,获取JsonResult
    * @param ApiUrl
    * @param Param
    * @return JsonResult
    */
   public static String GetJsonResult(String ApiUrl, String Body){

       Response response = given()
               .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
               .contentType("application/json")
               .log().all()
               .request()
               .body(Body)
               .when()
               .post(ApiUrl);

       String JsonResult = response.asString();
       log.info("返回的值JsonResult:"+JsonResult);

       return JsonResult;
   }
   
   /**
    * 指定API接口URL,POST请求参数,获取JsonResult
    * @param ApiUrl
    * @param Param
    * @return JsonResult
    */
   public static void GetJsonResult(String ApiUrl, Map<String, Object> Headers, String Body){
       
       Response response = given()
               .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
               .contentType("application/json; charset=UTF-8")
               .headers(Headers)
               .log().all()
               .request()
               .body(Body)
               .when()
               .post(ApiUrl);

       log.info("Response:");
       response.print();
       log.info("<--------------------------------------------------------------------- 分    割    线 --------------------------------------------------------------------->");
   }
   
   /**
    * 指定API接口URL,POST请求参数,获取JsonResult
    * @param ApiUrl
    * @param Param
    * @return JsonResult
    */
   public static String GetJsonResultX(String ApiUrl, String Param){

	   Response response = given()
               .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
               .contentType("application/x-www-form-urlencoded; charset=UTF-8")
               .log().all()
               .request()
               .body(Param)
               .when()
               .post(ApiUrl);

       String JsonResult = response.asString();
       log.info("返回的值JsonResult:"+JsonResult);

       return JsonResult;
   }
   
   /**
    * 指定API接口URL,POST请求参数,获取JsonResult
    * @param ApiUrl
    * @param Param
    * @return JsonResult
    */
   public static String GetJsonResultX(String ApiUrl, String Param,String Value){

       Response response = given()
               .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
               .contentType("application/x-www-form-urlencoded; charset=UTF-8")
               .log().all()
               .request()
               .body(Param)
               .when()
               .post(ApiUrl);

       String result = response.asString();
       log.info("返回的值Json:"+result);

       String JsonData = response.jsonPath().get(Value);//获取单个值
       log.info(JsonData);

       return JsonData;
   }
   /**
    * 指定API接口URL,POST请求参数,获取JsonData
    * @param ApiUrl
    * @param Value
    * @return JsonData
    */
   public static String GetJsonData(String ApiUrl, String Value) throws Exception{

       Response response = given()
               .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
               .contentType("application/json")
               .log().all()
               .request()
               .when()
               .post(ApiUrl);

       String JsonData = response.jsonPath().get(Value);
       log.info(JsonData);

       return JsonData;
   }

   /**
    * 指定API接口URL,POST请求参数,获取JsonDataParamStringValue
    * @param ApiUrl
    * @param Param
    * @param Value
    * @return JsonDataParamStringValue
    */
   public static String GetJsonDataParamStringValue(String ApiUrl, String Param,  String Value){

       Response response = given()
               .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
               .contentType("application/json")
               .log().all()
               .body(Param)
               .request()
               .when()
               .post(ApiUrl);

       String JsonDataParamStringValue = response.jsonPath().get(Value);
       log.info(JsonDataParamStringValue);

       return JsonDataParamStringValue;
   }

   /**
    * 指定API接口URL,POST请求参数,获取JsonDataParamIntValue
    * @param ApiUrl
    * @param Param
    * @param Value
    * @return JsonDataParamIntValue
    */
   public static int GetJsonDataParamIntValue(String ApiUrl, String Param,  String Value){

       Response response = given()
               .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
               .contentType("application/json")
               .log().all()
               .body(Param)
               .request()
               .when()
               .post(ApiUrl);

       int JsonDataParamIntValue = response.jsonPath().getInt(Value);
       log.info(JsonDataParamIntValue);

       return JsonDataParamIntValue;
   }

   /**
    * 指定API接口URL,POST请求参数,获取JsonDataParamValueValue
    * @param ApiUrl
    * @param Param
    * @param Value
    * @param Value1
    * @return JsonDataParamValueValue
    */
   public static String GetJsonDataParamValueValue(String ApiUrl, String Param,  String Value,  String Value1) throws Exception{

       Response response = given()
               .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
               .contentType("application/json")
               .log().all()
               .request()
               .body(Param)
               .when()
               .post(ApiUrl);

       String result = response.asString();
       JsonPath jsonPath = new JsonPath(result).setRoot(Value);
       String JsonDataParamValueValue = jsonPath.getString(Value1);

       return JsonDataParamValueValue;
   }

   /**
    * 指定API接口URL,POST请求参数,获取Cookie
    * @param ApiUrl
    * @param Param
    * @return Cookie
    */
   public static Map<String, String> GetPostCookies(String ApiUrl, String Param){

       Response response = given()
               .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
               .contentType("application/json")
               .request()
               .body(Param)
               .when()
               .post(ApiUrl);

       response.print();
       Map<String, String> Cookie=response.getCookies();
       log.info("allCookies"+Cookie);

       return Cookie;
   }

    /**
     * 指定API接口URL,POST请求参数,获取JsonResult
     * @param ApiUrl
     * @param Param
     * @param Cookie
     * @return JsonResult
     */
    public static String GetJsonResult(String ApiUrl, String Param, Map<String, String> Cookie){

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
        String JsonResult = response.asString();
        log.info("返回的值Json:"+JsonResult);

        return JsonResult;
    }

    /**
     * 指定API接口URL,POST请求参数,获取JsonIntValue
     * @param ApiUrl
     * @param Param
     * @param Cookie
     * @return JsonIntValue
     */
    public static int GetJsonIntValue(String ApiUrl, String Param, Map<String, String> Cookie, String Value){

        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .log().all()
                .request()
                .body(Param)
                .cookies(Cookie)
                .when()
                .post(ApiUrl);

        int JsonIntValue = response.jsonPath().getInt(Value);
        log.info("Value:"+JsonIntValue);

        return JsonIntValue;
    }

    /**
     * 指定API接口URL,POST请求参数,获取JsonStringValue
     * @param ApiUrl
     * @param Param
     * @param Cookie
     * @return JsonStringValue
     */
    public static String GetJsonStringValue(String ApiUrl, String Param, Map<String, String> Cookie, String Value){

        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .log().all()
                .request()
                .body(Param)
                .cookies(Cookie)
                .when()
                .post(ApiUrl);

        String JsonStringValue = response.jsonPath().getString(Value);
        log.info("Value:"+JsonStringValue);

        return JsonStringValue;
    }

    /**
     * 指定API接口URL,POST请求参数,获取JsonValueValue
     * @param ApiUrl
     * @param Param
     * @param Cookie
     * @return JsonValueValue
     */
    public static String GetJsonValueValue(String ApiUrl, String Param, Map<String, String> Cookie, String Value,String Value1){

        Response response = given()
                .config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())))
                .contentType("application/json")
                .log().all()
                .request()
                .body(Param)
                .cookies(Cookie)
                .when()
                .post(ApiUrl);

        String result = response.print();
        JsonPath jsonPath = new JsonPath(result).setRoot(Value);
        String JsonValueValue = jsonPath.getString(Value1);
        log.info("Value:" +JsonValueValue);

        return JsonValueValue;
    }

    /**
     * 指定API接口URL,POST请求参数,获取StatusCode
     * @param ApiUrl
     * @param Param
     * @param Cookie
     * @return StatusCode
     */
    public static int GetStatusCode(String ApiUrl, String Param, Map<String, String> Cookie){

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
        int StatusCode = response.getStatusCode();
        log.info("StatusCode:" + StatusCode);

        return StatusCode;
    }

    /**
     * 指定API接口URL,POST请求参数,获取Cookie
     * @param Param
     * @param ApiUrl
     * @return sessionId
     * @throws Exception
     */
    public static String GetPostCookie(String ApiUrl,String Param) throws Exception{

    	 String result = "";
         int responseCode = 0;
         BufferedReader br = null;
         Map<String, String> map = new HashMap<String, String>();
        //登录
        URL url = new URL(ApiUrl);
//        String param = "username="+username+"&password="+password;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        OutputStream out = conn.getOutputStream();
        out.write(Param.getBytes());
        out.flush();
        out.close();
        String cookieVal = "";
        String sessionId = "";
        String key = null;
        //取cookie
        for(int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++){
            if(key.equalsIgnoreCase("set-cookie")){
                cookieVal = conn.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                sessionId = sessionId + cookieVal + ";";
                log.info("cookieVal:"+cookieVal);
            }
        }
        responseCode = conn.getResponseCode();
        map.put("code", String.valueOf(responseCode));
        // 打印 http 状态码
         log.info("responseCode: " + responseCode);

        if (HttpURLConnection.HTTP_OK == responseCode) {
            // 定义BufferedReader输入流来读取URL的响应
            br = new BufferedReader(new InputStreamReader(
            		conn.getInputStream(), "UTF-8"));
            String strLine;
            StringBuffer responseBuf = new StringBuffer();

            while ((strLine = br.readLine()) != null) {
                responseBuf.append(strLine);
            }

            result = responseBuf.toString();
            map.put("result", result);
            log.info(result);
        }
		return sessionId;
    }
}