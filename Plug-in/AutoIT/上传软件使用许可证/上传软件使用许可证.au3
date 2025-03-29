;ControlFocus("title","text",controlID) Edit1=Edit instance 1
;control ID由class和instance组成
;识别windows窗口
ControlFocus("打开", "","Edit1")

; Wait 10 seconds for the Upload window to appear
;窗口等待十秒
WinWait("[CLASS:#32770]","",10)


; Set the File name text on the Edit field
;想输入框中输入需要上传的地址
ControlSetText("打开", "", "Edit1", "D:\King\Eclipse\Ankki.Dmp.Web.Test\Plug-in\AutoIT\上传软件使用许可证\B27CA2A58CC55512_audit.lic")

Sleep(1000)

; Click on the Open button
;点击[打开】按钮
ControlClick("打开", "","Button1");