;ControlFocus("title","text",controlID) Edit1=Edit instance 1
;control ID由class和instance组成
;识别windows窗口
ControlFocus("另存为", "","Edit1")

; Wait 10 seconds for the Upload window to appear
;窗口等待十秒
WinWait("[CLASS:#32770]","",10)


; Set the File name text on the Edit field
;想输入框中输入需要上传的地址
ControlSetText("另存为", "", "Edit1", "D:\Jenkins\workspace\Sakura.Web.UI.Automation.Test\Plug-in\AutoIT\下载设备机器码\172.19.3.15.info")

Sleep(1000)

; Click on the Open button
;点击[另存为】按钮
ControlClick("另存为", "","Button2");