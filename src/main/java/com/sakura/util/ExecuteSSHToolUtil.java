package com.sakura.util;
 
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
 
/**
 * 执行Shell工具类
 *
 */
public class ExecuteSSHToolUtil {
 
    private static final Logger log = LoggerFactory.getLogger(ExecuteSSHToolUtil.class);
 
    /** 未调用初始化方法 错误提示信息 */
    private static final String DONOT_INIT_ERROR_MSG = "please invoke init(...) first!";
 
    private Session session;
 
    private Channel channel;
 
    private ChannelExec channelExec;
 
    public ExecuteSSHToolUtil() {
    }
 
    /**
     * 获取ExecuteShellUtil类实例对象
     *
     */
    public static ExecuteSSHToolUtil getInstance() {
        return new ExecuteSSHToolUtil();
    }
 
    /**
     * 初始化
     *
     * @param ip
     *         远程Linux地址
     * @param port
     *         端口
     * @param username
     *         用户名
     * @param password
     *         密码
     * @throws JSchException
     *         JSch异常
     */
    public void init(String ip, Integer port, String username, String password) throws Exception {
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(username, ip, port);
            session.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect(60 * 1000);
            log.info("Session connected!");
            // 打开执行shell指令的通道
            channel = session.openChannel("exec");
            channelExec = (ChannelExec) channel;
        } catch (JSchException e) {
            log.error("Failed to connect to SSH session: " + e.getMessage(), e);
            throw new Exception("Failed to connect to SSH session: " + e.getMessage(), e);
        }
    }
 
    /**
     * 执行一条命令
     */
    public String execCmd(String command) throws Exception {
        if (session == null || channel == null || channelExec == null) {
            throw new Exception(DONOT_INIT_ERROR_MSG);
        }
        log.info("execCmd command - > {}", command);
        channelExec.setCommand(command);
        channel.setInputStream(null);
        channelExec.setErrStream(System.err);
        channel.connect();
        StringBuilder sb = new StringBuilder(16);
        try (InputStream in = channelExec.getInputStream();
             InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                sb.append("\n").append(buffer);
            }
            log.info("execCmd result succeed- > {}", sb.toString());
        }catch (Exception  e){
            log.info("execCmd result failed- > {}",e.toString());
        }finally {
            // 释放资源
            close();
        }
        return sb.toString();
    }
 
    /**
     * 释放资源
     *
     * @date 2019/3/15 12:47
     */
    private void close() {
        if (channelExec != null && channelExec.isConnected()) {
            channelExec.disconnect();
        }
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        log.info("Successfully released resources");
    }
    
    public static void main(String[] args) throws Exception {
    	ExecuteSSHToolUtil executeShellUtil = new ExecuteSSHToolUtil();
    	executeShellUtil.init("172.19.5.33", 22, "root", "@1fw#2soc$3vpn");
		String result = executeShellUtil.execCmd("df -lh | grep /dev/mapper/sysvg-root | awk '{print $2}'");
		log.info(result);
	}
}