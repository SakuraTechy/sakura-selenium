package com.sakura.base;

import java.util.Map;

import lombok.Data;

/**
 * <br>对应于XML文件中的step元素</br>
 *
 * @version 1.0
 * @since   1.0
 */
@Data
public class TestStep extends TestBase{
	
	private String id;
    private String name;
    
	/**
	 * 当前步骤的行为，调用StepAction中的方法
	 * <h1>action="keybg"</h1>
	 **/
	private StepAction action;

    /**
     * Action行为对应的元素定位，包含索引，属性名=属性值[索引]
     * 如果可以确定唯一要获取的元素，则可以省略[索引]
     * <h1>locator="resource-id=com.giveu.corder:id/tv_login"</h1>
     * <h1>locator="id=su[0]"</h1>
     **/
    private String locator;

	/**
	 * 是否可见
	 * <h1>invisible="true"</h1>
	 **/
	private String invisible;

	/**
	 * 超时时间，单位为秒
	 * <h1>timeout="20"</h1>
	 **/
	private String timeout;

	/**
	 * 等待时间，单位为毫秒
	 * <h1>waitTime="2000"</h1>
	 **/
	private String waitTime;


	/**
	 * Action行为对应所需要传递的参数值 
	 * <h1>value="5000"</h1>
	 **/
    private String value;
    
    private String element;

	/**
	 * 行为的细节描述，其格式与css中的样式格式一致，比如<br/>
	 * 在要做滑动操作时，细节描述如下，duration代表滑动时间，num代表滑动次数，amp代表滑动幅度
	 * <h1>details="duration:500;num:2;amp:1"</h1>
	 * 具体不同的操作的文档会在项目稳定后整理出来
	 **/
	private Map<String,String> details;
	
	 /**
     * 标记用例的特殊状态
     * <h1>state="true"</h1>
     **/
    private String state;
    
    /**
     * 标记用例是否跳过
     * <h1>skip="true"</h1>
     **/
    private String skip;

	/**
	 * 模拟Windows键盘按键操作的键值，单个按键
	 * <h1>key="F5"</h1>
	 **/
	private String key;
	
	/**
	 * 模拟Windows键盘按键操作的键值，组合按键
	 *<h1>key="CONTROL" keys="TAB"</h1>
	 **/
	private String keys;
	
	/**
	 * 检测结果的预期值
	 *<h1>expect="百度"</h1>
	 **/
    private String expect;
    
	/**
	 * 检测结果不匹配时的提示信息，用于记录到测试报告中
	 */
	private String message;
    
	/**
	 * 失败用例的截图名称
	 *<h1>casename="case1"</h1>
	 **/
    private String caseid;
    
	/**
	 * 调用后台接口的URL地址
	 *<h1>url="https://travel.api.szjqb.net/api/Web/Order/completes"</h1>
	 **/
    private String url;

    /**
     * 调用后台接口所需传递的参数类型名称
     *<h1>headers="tokenId=oaGjrJZrY7lJaXgkOQJ7ai8L5Sra+oOA4Y/XNiryOoQ="</h1>
     **/
    private String headers;
    
	/**
	 * 调用后台接口所需传递的参数类型名称
	 *<h1>body="order_sn"</h1>
	 **/
    private String body;
    
    /**
     * 调用数据库所需的信息
     *<h1>datatype="MySql"</h1>
     *<h1>dataenviron="测试环境"</h1>
     *<h1>database="dbtest01"</h1>
     *<h1>sql="SELECT STR1 from m_getmsg_water where phonno ='17378403911' ORDER BY create_date DESC LIMIT 1"</h1>
     **/
    private String datatype;
    private String dataenviron;
    private String port;
	private String server = "";
    private String database;
    private String sql;

    private String device;
    private String shell;
    
    private String delete;

	private String className;
	private String params;

	private String operationType;
	private String callName;
	private String callParams;

    /**
     * 文件目录
     * catalogue="user.dir丨user.home"
     **/
    private String catalogue;
    
    /**
     * 本地文件路径
     * localpath="\TestData\License\"
     **/
    private String localpath;
    
    /**
     * 远程文件路径
     * remotepath="\TestData\License\"
     **/
    private String remotepath;
    
    /**
     * 文件路径
     * filetype="单个"
     **/
    private String filetype;
    
    /**
	 * 使用正则表达式
	 * 例如："共 3 条" => "3"，regex="\\d{1}"
	 * 例如：共 402706 条"=> "402706",regex="(?<=共\\s)(.*?)(?=\\s条"
	 * 
	 **/
    private String regex;
    
    private Map<String,String> replace;
    
    /**
     * Step操作步骤序号
     *<h1>index="5"</h1>
     **/
    private int index;
    
    /**
     * 计算公式脚本
     * script="30*2"
     **/
    private String script;
    
    /**
     * 指定时间间隔参数
     * script="30*2"
     **/
    private String need_time;
    private String interval;
    
    /**
	 * 使用类型
	 **/
    private String type;

	/**
	 * 用户名
	 **/
	private String userName;

	/**
	 * 密码
	 **/
	private String password;

//    public StepAction getAction() {
//        return action;
//    }
//
//    public void setAction(StepAction action) {
//        this.action = action;
//    }
// 
//    public String getLocator() {
//        return locator;
//    }
//
//    public void setLocator(String locator) {
//        this.locator = locator;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//    
//    public String getElement() {
//        return element;
//    }
//
//    public void setElement(String element) {
//        this.element = element;
//    }
//    
//    public String getDesc() {
//        return name;
//    }
//
//    public void setDesc(String name) {
//        this.name = name;
//    }
//    
//	public Map<String,String> getDetails() {
//		return details;
//	}
//
//	public void setDetails(Map<String,String> details) {
//		this.details = details;
//	}
//	
//	public String getState() {
//        return state;
//    }
//
//    public void setSkip(String skip) {
//        this.skip = skip;
//    }
//    
//    public String getSkip() {
//        return skip;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//    
//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }
//    
//    public String getKeys() {
//        return keys;
//    }
//
//    public void setKeys(String keys) {
//        this.keys = keys;
//    }
//    
//    public String getExpect() {
//        return expect;
//    }
//
//    public void setExpect(String expect) {
//        this.expect = expect;
//    }
//    
//    public String getCaseid() {
//        return caseid;
//    }
//
//    public void setCaseid(String caseid) {
//        this.caseid = caseid;
//    }
//    
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//    
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//    
//    public String getHeaders() {
//        return headers;
//    }
//
//    public void setHeaders(String headers) {
//        this.headers = headers;
//    }
//    
//    public String getBody() {
//        return body;
//    }
//
//    public void setBody(String body) {
//        this.body = body;
//    }
//    
//    public String getDatatype() {
//        return datatype;
//    }
//
//    public void setDatatype(String datatype) {
//        this.datatype = datatype;
//    }
//    
//    public String getDataenviron() {
//        return dataenviron;
//    }
//
//    public void setDataenviron(String dataenviron) {
//        this.dataenviron = dataenviron;
//    }
//    
//    public String getPort() {
//        return port;
//    }
//
//    public void setPort(String port) {
//        this.port = port;
//    }
//    
//    public String getDatabase() {
//        return database;
//    }
//
//    public void setDatabase(String database) {
//        this.database = database;
//    }
//    
//    public String getSql() {
//        return sql;
//    }
//
//    public void setSql(String sql) {
//        this.sql = sql;
//    }
//    
//    public int getIndex() {
//        return index;
//    }
//
//    public void setIndex(int index) {
//        this.index = index;
//    }
//    
//    public String getDevice() {
//        return device;
//    }
//
//    public void setDevice(String device) {
//        this.device = device;
//    }
//    
//    public String getShell() {
//        return shell;
//    }
//
//    public void setShell(String shell) {
//        this.shell = shell;
//    }
    
	@Override
	public String toString() {
		return "TestStep ["
		    + "action=" + action + ", locator=" + locator + ", value=" + value + ", name=" + name + ", details=" + details + ", key=" + key + ", keys=" + keys + ", expect=" + expect + ", message=" + message + ", "
		    + "url=" + url +", body=" + body + ", "
		    + "datatype=" + datatype + ", dataenviron=" + dataenviron + ", database=" + database + ", sql=" + sql + ""
		    + "]";
	}
}