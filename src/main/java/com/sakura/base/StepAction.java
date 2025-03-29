package com.sakura.base;

import java.util.HashMap;
import java.util.Map;

import com.sakura.handler.AndroidSystemHandler;
import com.sakura.handler.CheckActionHandler;
import com.sakura.handler.ClearActionHandler;
import com.sakura.handler.ClickActionHandler;
import com.sakura.handler.DBActionHandler;
import com.sakura.handler.ExecuteShellHandler;
import com.sakura.handler.FileAction_Handler;
import com.sakura.handler.FreeSSH_FTP_Handler;
import com.sakura.handler.GetCodeActionHandler;
import com.sakura.handler.GetUrlActionHandler;
import com.sakura.handler.HttpRequestHandler;
import com.sakura.handler.InputActionHandler;
import com.sakura.handler.RecordActionHandler;
import com.sakura.handler.SetActionHandler;
import com.sakura.handler.SlideActionHandler;
import com.sakura.handler.WaitActionHandler;
import com.sakura.handler.WindowsSystemHandler;

public enum StepAction {
    WEB_GETURL("web-geturl", "Web端打开默认网站操作", GetUrlActionHandler.class),
    WEB_GETURLS("web-geturls", "Web端打开自定义网站操作", GetUrlActionHandler.class),
    WEB_REFRESH("web-refresh", "WEB端页面刷新操作", GetUrlActionHandler.class),
    WEB_CLOSE("web-close", "WEB端关闭当前标签页操作", GetUrlActionHandler.class),
    WEB_QUIT("web-quit", "WEB端关闭全部标签页操作", GetUrlActionHandler.class),
    
    WEB_GETCODE("web-getcode", "Web端获取图片验证码", GetCodeActionHandler.class),
    
	SWITCH_IFRAME("switch-Iframe", "Web端切换Iframe控件操作", GetUrlActionHandler.class),
    RETURN_IFRAME("return-Iframe", "Web端返回上一级Iframe控件操作", GetUrlActionHandler.class),
    QUIT_IFRAME("quit-Iframe", "Web端返回最上级Iframe控件操作", GetUrlActionHandler.class),
	
    SWITCH_WINDOW("switch-window", "切换浏览器到当前最新窗口", GetUrlActionHandler.class),
    SWITCH_WINDOWS("switch-windows", "切换浏览器到指定窗口", GetUrlActionHandler.class),
    JAVASCRIPT_EXECUTOR("javascript-executor", "Web端执行js脚本操作", GetUrlActionHandler.class),

    ANDROID_GETURL("android-geturl", "Android端打开网站链接操作", GetUrlActionHandler.class),
    
    WEB_SET("web-set", "Web端设置值到全局", SetActionHandler.class),
    WEB_SETDATE("web-setdate", "Web端获取日期设置值到全局", SetActionHandler.class),
    WEB_SETSYSINFO("web-setsysinfo", "Web端获取系统信息设置值到全局", SetActionHandler.class),
    WEB_SETUSABLEIP("web-setusableip", "Web端获取可用IP设置值到全局", SetActionHandler.class),
    WEB_SETPROPERTIES("web-setproperties", "Web端获取配置文件信息", SetActionHandler.class),
    WEB_SETCALCULATIONFORMULA("web-setcalculationformula", "Web端获取计算公式设置值到全局", SetActionHandler.class),
    ANDROID_SET("android-set", "Android端设置值到全局", SetActionHandler.class),
    
    WINDOWS_KEYBG("windows-keybg", "模拟点击Windows系统键盘普通按键,例如：Home键", WindowsSystemHandler.class),
    WINDOWS_KEYBC("windows-keybc", "模拟点击Windows系统键盘组合按键,例如：Ctrl+W", WindowsSystemHandler.class),
    WINDOWS_SKEYBC("windows-skeybc", "模拟点击Windows系统键盘特殊组合按键,例如：Ctrl+Tab", WindowsSystemHandler.class),
    WINDOWS_SKEYBCM("windows-skeybcm", "模拟点击Windows系统键盘多重特殊组合按键,例如：Ctrl+Shift+K", WindowsSystemHandler.class),
    WINDOWS_CMD("windows-cmd", "模拟执行Windows系统的cmd命令", WindowsSystemHandler.class),
    
    MOUSEMOVE("mouse-move", "模拟移动鼠标到指定位置x，y", WindowsSystemHandler.class),
    MOVEBYOFFSET("move-byoffset", "模拟移动鼠标到指定位置x，y", WindowsSystemHandler.class),
    MOVETOELEMENT("move-toelement", "模拟移动鼠标到指定元素中间", WindowsSystemHandler.class),

    EXE_SHELL("exe-shell", "连接Linux执行shell命令", ExecuteShellHandler.class),
    FREE_SFTPS("free-sftp", "上传本地文件到服务器，指定文件目录", FreeSSH_FTP_Handler.class),
    FREE_SFTP("free-sftps", "上传本地文件到服务器，默认当前工作目录", FreeSSH_FTP_Handler.class),

    GET_FILE("get-file", "获取本地目录文件，指定文件目录", FileAction_Handler.class),
    GET_FILES("get-files", "获取本地目录文件，默认当前工作目录", FileAction_Handler.class),
    DELETE_FILES("delete-file", "删除本地文件或目录，指定文件目录", FileAction_Handler.class),
    DELETE_FILE("delete-files", "删除本地文件或目录，默认当前工作目录", FileAction_Handler.class),
    ZIP_FILE("zip-file", "获取本地zip文件，压缩或解压到指定目录", FileAction_Handler.class),
    WEB_INPUT("web-input", "Web端输入操作", InputActionHandler.class),
    WEB_INPUTZS("web-inputzs", "Web端上传证书操作", InputActionHandler.class),
    WEB_INPUTDATE("web-inputdate", "Web端输入时间操作", InputActionHandler.class),
    
    WEB_INPUTFILE("web-inputfile", "Web端上传文件操作，指定文件目录", InputActionHandler.class),
    WEB_INPUTFILES("web-inputfiles", "Web端上传文件操作，默认当前工作目录", InputActionHandler.class),

    ANDROID_INPUT("android-input", "Android端Appium输入操作", InputActionHandler.class),
    
    WEB_INPUTCLEAR("web-inputclear", "Web端输入框清除操作", ClearActionHandler.class),
    ANDROID_CLEAR("android-clear", "Android端清除操作", ClearActionHandler.class),
    
    ANDROID_KEYCODE("android-keycode", "模拟Android系统键盘按键操作,例如：KEYCODE_HOME 3", AndroidSystemHandler.class),
    ANDROID_RETURN("android-return", "模拟Android系统返回按键操作,可返回多次", AndroidSystemHandler.class),
    ANDROID_OPENNB("android-opennb", "模拟打开Androud系统通知栏操作", AndroidSystemHandler.class),
    ANDROID_SCROLL("android-scroll", "模拟执行Androud系统页面滚动操作", AndroidSystemHandler.class),
    ANDROID_SWIPE("android-swipe", "模拟执行Androud系统页面滑动操作", AndroidSystemHandler.class),
    
    ADB_KEYCODE("adb-keycode", "Android端ADB系统键盘按键操作", AndroidSystemHandler.class),
    ADB_INPUT("adb-input", "Android端ADB输入操作", AndroidSystemHandler.class),
    ADB_CLICK("adb-click", "Android端ADB点击操作", AndroidSystemHandler.class),
    ADB_SWIPE("adb-swipe", "模拟执行ADB指令页面滑动操作", AndroidSystemHandler.class),
    ADB_IME("adb-ime", "模拟执行ADB切换输入法操作", AndroidSystemHandler.class),
    
    WEB_CHECK("web-check", "检查Web界面元素的文本值", CheckActionHandler.class),
    WEB_NOTCHECK("web-notcheck", "不检查Web界面元素的文本值", CheckActionHandler.class),
    WEB_CHECKINVISIBLE("web-check-invisible", "检查Web界面元素消失不可见", CheckActionHandler.class),
    WEB_CHECKVALUE("web-checkvalue", "检查Web界面元素的属性值", CheckActionHandler.class),
    WEB_CHECKJS("web-checkjs", "检查Web界面调用JS返回的值", CheckActionHandler.class),
    WEB_CHECKLIST("web-checklist", "检查从数据库中查询出的结果中的值是否符合预期", CheckActionHandler.class),
    WEB_NOTCHECKLIST("web-notchecklist", "不检查从数据库中查询出的结果中的值是否符合预期", CheckActionHandler.class),
    WEB_REGEXCHECKLIST("web-regexchecklist", "正则检查从数据库中查询出的结果中的值是否符合预期", CheckActionHandler.class),
    WEB_CHECKSETLIST("web-checksetlist", "检查Web本地缓存的list中的元素", CheckActionHandler.class),
    WEB_NOTCHECKSETLIST("web-notchecksetlist", "不检查Web本地缓存的list中的元素", CheckActionHandler.class),
    WEB_REGEXCHECKSETLIST("web-regexchecksetlist", "正则检查Web本地缓存的list中的元素", CheckActionHandler.class),
    WEB_FUZZYCHECK("web-fuzzycheck", "模糊检查Web界面元素的内容", CheckActionHandler.class),
    WEB_REGEXCHECK("web-regex-check", "正则检查Web界面元素的内容", CheckActionHandler.class),
    WEB_CHECKPATHFILEBYPATTERNS("web-check-path-file-by-patterns", "检查指定路径下的文件是否匹配模式", CheckActionHandler.class),
    WEB_CHECK_FILE_CONTENT("web-check-file-content", "检查文件内容是否与预期值相等", CheckActionHandler.class),
    ANDROID_CHECK("android-check", "检查Android界面元素值", CheckActionHandler.class),
    ANDROID_CHECKLIST("android-checklist", "检查Android本地缓存的list中的元素", CheckActionHandler.class),
    
    WAIT_FORCED("wait-forced", "强制等待", WaitActionHandler.class),
    WEBWAIT_IMPLICIT("web-implicit", "Web端隐式等待", WaitActionHandler.class),
    WEBWAIT_DISPLAY("web-display", "Web端显示等待", WaitActionHandler.class),
    ANDROIDWAIT_IMPLICIT("android-implicit", "Android端隐式等待", WaitActionHandler.class),
    
    WEB_CLICK("web-click", "Web端点击可见元素操作", ClickActionHandler.class),
    WEB_CLICKS("web-clicks", "Web端元素指定方式操作", ClickActionHandler.class),
    ANDROID_CLICK("android-click", "Android端点击操作", ClickActionHandler.class),
    SELECT_CLICK("select-click", "Web端选项框点击操作", ClickActionHandler.class),
    INPUT_CLICK("input-click", "Web端选项框搜索点击操作", ClickActionHandler.class),
    CHECK_EXIST_CLICK("check-exist-click", "Web端检查某个元素存在则点击操作", ClickActionHandler.class),
    CHECK_NOT_EXIST_CLICK("check-not-exist-click", "Web端检查某个元素不存在则点击操作", ClickActionHandler.class),
    CHECK_EQUALS_CLICK("check-equals-click", "Web端检查某个元素相等则点击操作", ClickActionHandler.class),
    CHECK_NOT_EQUALS_CLICK("check-not-equals-click", "Web端检查某个元素不相等则点击操作", ClickActionHandler.class),

    SCROLL_ElEMENT("scroll-element", "Web端滚动到元素操作", ClickActionHandler.class),
    
    CLICK_OK("click-ok", "点击浏览器弹出框的确定键", ClickActionHandler.class),
    CLICK_CANCEL("click-cancel", "点击浏览器弹出框的取消键", ClickActionHandler.class),
    CLICK_TEXT("click-text", "执行浏览器文本弹出框的文本输入", ClickActionHandler.class),

    START_RECORD("start-record", "开始录制视频",RecordActionHandler.class),
    END_RECORD("end-record", "结束录制视频",RecordActionHandler.class),
    PNLL_RECORD("pull-record", "上传录制的视频",RecordActionHandler.class),
    MOVE_RECORD("move-record", "移动录制的视频",RecordActionHandler.class),
    
    SLIDE("slide", "Android自定义滑动", SlideActionHandler.class),
    SLIDE_UP("slide-up", "Android向上滑动", SlideActionHandler.class),
    SLIDE_DOWN("slide-down", "Android向下滑动", SlideActionHandler.class),
    SLIDE_LEFT("slide-left", "Android向左滑动", SlideActionHandler.class),
    SLIDE_RIGHT("slide-right", "Android向右滑动", SlideActionHandler.class),
    
    GET_COOKIE("get-cookie", "获取cookie值", HttpRequestHandler.class),
    SEND_POST("send-post", "发送Post接口请求", HttpRequestHandler.class),

    CUSTOM_DATABASE_OPERATIONS("custom-database-operations", "自定义数据库操作", DBActionHandler.class),

    DB_INSERTA("db-inserta", "数据库插入", DBActionHandler.class),
    DB_INSERTW("db-insertw", "数据库插入", DBActionHandler.class),
    DB_DELETEA("db-deletea", "数据库删除", DBActionHandler.class),
    DB_DELETEAW("db-deletew", "数据库删除", DBActionHandler.class),
    DB_UPDATEA("db-updatea", "数据库更新", DBActionHandler.class),
    DB_UPDATEW("db-updatew", "数据库更新", DBActionHandler.class),
	DB_QUERYA("db-querya", "数据库查询", DBActionHandler.class),
	DB_QUERYW("db-queryw", "数据库查询", DBActionHandler.class),
	DB_QUERYWS("db-queryws", "数据库查询，获取结果设置变量", DBActionHandler.class),
	DB_PROCEDUREA("db-procedurea", "数据库存储过程", DBActionHandler.class),
	DB_PROCEDUREW("db-procedurew", "数据库存储过程", DBActionHandler.class),
	
    DB_INSERTMA("db-insertma", "MySql数据库插入", DBActionHandler.class),
    DB_INSERTMW("db-insertmw", "MySql数据库插入", DBActionHandler.class),
    DB_DELETEMA("db-deletema", "MySql数据库删除", DBActionHandler.class),
    DB_DELETEMW("db-deletemw", "MySql数据库删除", DBActionHandler.class),
    DB_UPDATEMA("db-updatema", "MySql数据库更新", DBActionHandler.class),
    DB_UPDATEMW("db-updatemw", "MySql数据库更新", DBActionHandler.class),
	DB_QUERYMA("db-querya", "MySql数据库查询", DBActionHandler.class),
	DB_QUERYMW("db-queryw", "MySql数据库查询", DBActionHandler.class),
	DB_PROCEDUREMA("db-procedurema", "MySql数据库存储过程", DBActionHandler.class),
	DB_PROCEDUREMW("db-proceduremw", "MySql数据库存储过程", DBActionHandler.class),
	
    DB_INSERTMSSHA("db-insertmssha", "MySqlSSH数据库插入", DBActionHandler.class),
    DB_INSERTMSSHW("db-insertmsshw", "MySqlSSH数据库插入", DBActionHandler.class),
    DB_DELETEMSSHA("db-deletemssha", "MySqlSSH数据库删除", DBActionHandler.class),
    DB_DELETEMSSHW("db-deletemsshw", "MySqlSSH数据库删除", DBActionHandler.class),
    DB_UPDATEMSSHA("db-updatemssha", "MySqlSSH数据库更新", DBActionHandler.class),
    DB_UPDATEMSSHW("db-updatemsshw", "MySqlSSH数据库更新", DBActionHandler.class),
	DB_QUERYMSSHA("db-querymssha", "MySqlSSH数据库查询", DBActionHandler.class),
	DB_QUERYMSSHW("db-querymsshw", "MySqlSSH数据库查询", DBActionHandler.class),
	DB_PROCEDUREMSSHA("db-proceduremssha", "MySqlSSH数据库存储过程", DBActionHandler.class),
	DB_PROCEDUREMSSHW("db-proceduremsshwW", "MySqlSSH数据库存储过程", DBActionHandler.class);
	
    private String key;

    private String desc;

    private Class<?> handler;

    private static Map<String,StepAction> map;

    static{
        map = new HashMap<String,StepAction>();
        for(StepAction action : StepAction.values()){
            map.put(action.key(), action);
        }
    }

    private StepAction(String key, String desc, Class<?> handler) {
        this(key, desc);
        this.handler = handler;
    }

    private StepAction(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static StepAction action(String name){
        return map.get(name);
    }

    public String key(){
        return this.key;
    }

    public String desc(){
        return this.desc;
    }

    public Class<?> handler(){
        return this.handler;
    }
    
    public void run(TestStep step) throws Exception{
    	
    }
}
