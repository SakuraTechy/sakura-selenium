package com.sakura.util;

import java.io.*;

import org.apache.log4j.Logger;
public class CommandUtil {
	
	static Logger log = Logger.getLogger(CommandUtil.class);

    /**
     * 执行命令行操作，并返回执行结果。
     *
     * @param command       命令行参数数组。
     * @param workingDir    工作目录路径（可选）。
     * @param resultHandler 处理命令执行结果的回调接口。
     */
    public static void executeCommand(String command, String workingDir, ResultHandler resultHandler) {
        try {
            // 创建Process实例
            log.info("Start Executed Command："+command);
            Process process = Runtime.getRuntime().exec(command, null, workingDir == null ? null : new File(workingDir));

            // 读取输出流，使用cp936编码，解决中文乱码问题
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "cp936"));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));

            // 使用异步线程读取输出流
            Thread outputReaderThread = new Thread(() -> {
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        // 调用回调接口处理输出
                        resultHandler.onOutput(line);
                    }
                } catch (Exception e) {
                    resultHandler.onError("Error reading output: " + e.getMessage());
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        resultHandler.onError("Error closing reader: " + e.getMessage());
                    }
                }
            });
            // 启动线程
            outputReaderThread.start();
        } catch (Exception e) {
            resultHandler.onError("Error executing command: " + command + "\n" + e.getMessage());
        }
    }

    /**
     * 处理命令执行结果的回调接口。
     */
    public interface ResultHandler {
        void onOutput(String output);
        void onSuccess(String command);
        void onError(String errorMessage);
    }

    /**
     * 简化的ResultHandler实现。
     */
    public static class SimpleResultHandler implements ResultHandler {
        @Override
        public void onOutput(String output) {
            System.out.println("Output: " + output);
        }

        @Override
        public void onSuccess(String command) {
            log.info("Command executed successfully");
        }

        @Override
        public void onError(String errorMessage) {
            log.error("Error: " + errorMessage);
        }
    }

    public static void main(String[] args) {
		String command1 = "powershell -Command (Get-Content 'C:\\Users\\ankki\\AppData\\Roaming\\Windows agent\\agent.cfg') | ForEach-Object { $_ -replace 'dst_ip=.*', 'dst_ip=172.19.5.45' } | Set-Content 'C:\\Users\\ankki\\AppData\\Roaming\\Windows agent\\agent.cfg'";
        String command2 = "cmd /c cd D:\\Program\\Agent\\5.1.2 && \"Windows Agent.exe\"";
        String command3 = "wmic process where name='Windows Agent.exe' delete";
		String command = "ipconfig";

        // 调用executeCommand方法
        executeCommand(command1, null, new SimpleResultHandler());
    }
}
