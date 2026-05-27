package com.sakura.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

/**
 * ssh连接 - 使用SSHJ实现
 */
public class SSHUtil {
	private static final Logger log = LoggerFactory.getLogger(SSHUtil.class);
	private List<String> stdout;
	// SSH客户端
	private SSHClient sshClient;

	// 输入IP、端口、用户名和密码，连接远程服务器
	public SSHUtil(final String host, int port, final String username, final String password) {
		try {
			sshClient = new SSHClient();
			sshClient.addHostKeyVerifier(new PromiscuousVerifier());
			sshClient.connect(host, port);
			sshClient.authPassword(username, password);
		} catch (Exception e) {
			log.error("SSH连接失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 测试服务器连接是否成功
	 *
	 * @param host     服务器主机地址
	 * @param port     服务器端口号
	 * @param username 用户名
	 * @param password 密码
	 * @return 连接成功返回 true，否则返回 false
	 */
	public static Boolean testConnection(final String host, int port, final String username, final String password) {
		SSHClient sshClient = new SSHClient();

		try {
			// 设置连接配置
			sshClient.addHostKeyVerifier(new PromiscuousVerifier()); // 禁用主机密钥验证
			sshClient.connect(host, port);

			// 认证
			sshClient.authPassword(username, password);

			log.info("Connected to server at " + host + ":" + port + " using SSHJ");
			return true;
		} catch (Exception e) {
			log.error("Failed to connect to server using SSHJ: " + e.getMessage());
			return false;
		} finally {
			try {
				if (sshClient.isConnected()) {
					sshClient.disconnect();
					log.info("Disconnected from server using SSHJ.");
				}
			} catch (Exception e) {
				log.warn("Error disconnecting SSHJ client: " + e.getMessage());
			}
		}
	}

	/**
	 * 使用SSHJ执行远程命令
	 *
	 * @param host     服务器主机地址
	 * @param port     服务器端口号
	 * @param username 用户名
	 * @param password 密码
	 * @param command  要执行的命令
	 * @return 命令执行结果列表
	 */
	public static List<String> executeCommandWithSSHJ(final String host, int port, final String username,
													  final String password, final String command) {
		List<String> result = new ArrayList<>();
		SSHClient sshClient = new SSHClient();

		try {
			// 设置连接配置
			sshClient.addHostKeyVerifier(new PromiscuousVerifier());
			sshClient.connect(host, port);
			sshClient.authPassword(username, password);

			// 执行命令
			try (net.schmizz.sshj.connection.channel.direct.Session session = sshClient.startSession()) {
				Command cmd = session.exec(command);

				// 读取输出
				BufferedReader reader = new BufferedReader(new InputStreamReader(cmd.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					result.add(line);
					log.info(line);
				}

				cmd.join(10, java.util.concurrent.TimeUnit.SECONDS);
			}

			log.info("Command executed successfully using SSHJ: " + command);
		} catch (Exception e) {
			log.error("Failed to execute command using SSHJ: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (sshClient.isConnected()) {
					sshClient.disconnect();
				}
			} catch (Exception e) {
				log.warn("Error disconnecting SSHJ client: " + e.getMessage());
			}
		}

		return result;
	}

	public int execute(final String command) {
		int returnCode = 0;
		stdout = new ArrayList<>();
		try (net.schmizz.sshj.connection.channel.direct.Session session = sshClient.startSession()) {
			Command cmd = session.exec(command);

			// 读取输出
			BufferedReader reader = new BufferedReader(new InputStreamReader(cmd.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				stdout.add(line);
				log.info(line);
			}

			cmd.join(10, java.util.concurrent.TimeUnit.SECONDS);
			returnCode = cmd.getExitStatus();
		} catch (Exception e) {
			log.error("执行命令失败: " + e.getMessage(), e);
			return -1;
		}
		return returnCode;
	}

	// 断开连接
	public void close() {
		if (sshClient != null && sshClient.isConnected()) {
			try {
				sshClient.disconnect();
			} catch (Exception e) {
				log.warn("断开SSH连接时出错: " + e.getMessage());
			}
		}
	}

	// 执行命令获取执行结果
	public String executeForResult(String command) {
		execute(command);
		StringBuilder sb = new StringBuilder();
		for (String str : stdout) {
			sb.append(str).append("\n");
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
			log.warn("关闭资源时出错: " + e.getMessage());
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
//		SSHUtil sshUtil = new SSHUtil("172.19.5.60", 2233, "root", "@nKk1^2Oe38&8!~!");
////		// 执行 ls /opt/命令
//		String result = sshUtil.executeForResult("df -h");
//		log.info(result);
//		sshUtil.close();
//		runShell("");

		// 测试SSHJ连接
		Boolean sshjResult = testConnection("172.19.5.47", 22, "root", "3edc$RFVAki##888");
		log.info("SSHJ connection result: " + sshjResult);

		// 测试SSHJ连接
		sshjResult = testConnection("172.19.3.63", 22, "root", "3edc$RFV5tgbAki##888");
		log.info("SSHJ connection result: " + sshjResult);

		// 测试SSHJ执行命令
//		List<String> commandResult = executeCommandWithSSHJ("172.19.3.63", 22, "root", "3edc$RFV5tgbAki##888", "df -h");
//		log.info("SSHJ command result: " + commandResult);
	}
}