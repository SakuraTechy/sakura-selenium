package com.sakura.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 执行shell命令
 */
public class ExecuteShellUtil {

	static Logger log = Logger.getLogger(ExecuteShellUtil.class);
	private Vector<String> stdout;
	// 会话session
	Session session;

	// 输入IP、端口、用户名和密码，连接远程服务器
	public ExecuteShellUtil(final String ipAddress, int port, final String username, final String password) {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, ipAddress, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(100000);
			if (session.isConnected()) {
				log.info("SSH服务器连接成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int execute(final String command) {
		int returnCode = 0;
		ChannelShell channel = null;
		PrintWriter printWriter = null;
		BufferedReader input = null;
		stdout = new Vector<String>();
		try {
			channel = (ChannelShell) session.openChannel("shell");
			channel.connect();
			input = new BufferedReader(new InputStreamReader(channel.getInputStream()));
			printWriter = new PrintWriter(channel.getOutputStream());
			printWriter.println(command);
			printWriter.println("exit");
			printWriter.flush();
//			log.info("The remote command is: ");
			String line;
			while ((line = input.readLine()) != null) {
				stdout.add(line);
				log.info(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			close(printWriter);
			close(input);
			if (channel != null) {
				channel.disconnect();
			}
		}
		return returnCode;
	}

	// 断开连接
	public void close() {
		if (session != null) {
			session.disconnect();
		}
	}

	// 执行命令获取执行结果
	public String executeForResult(String command) {
		execute(command);
		StringBuilder sb = new StringBuilder();
		for (String str : stdout) {
			sb.append(str);
		}
		return sb.toString();
	}

	public static void close(Closeable closeable) {
		if (closeable == null) {
			return;
		}
		try {
			closeable.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * 运行shell脚本
     * @param shell 需要运行的shell脚本
     */
    public static void execShell(String shell){
        try {
            Runtime.getRuntime().exec(shell);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 运行shell脚本 new String[]方式
     * @param shell 需要运行的shell脚本
     */
    public static void execShellBin(String shell){
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",shell},null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 

    /**
     * 运行shell并获得结果，注意：如果sh中含有awk,一定要按new String[]{"/bin/sh","-c",shStr}写,才可以获得流
     * 
     * @param shStr
     *            需要执行的shell
     * @return
     */
    public static List<String> runShell(String shStr) {
        List<String> strList = new ArrayList<String>();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",shStr},null,null);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            process.waitFor();
            while ((line = input.readLine()) != null){
                strList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strList;
    }
    
	public static void main(String[] args) {
		ExecuteShellUtil executeShellUtil = new ExecuteShellUtil("172.19.5.60", 2233, "root", "@nKk1^2Oe38&8!~!");
//		// 执行 ls /opt/命令
		String result = executeShellUtil.executeForResult("df -h");
		log.info(result);
		executeShellUtil.close();
//		runShell("");
	}
}
