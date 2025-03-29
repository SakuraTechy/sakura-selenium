package com.sakura.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelShell;

/**
 * JSch连接服务器操作
 * 
 * @author 刘智King
 * @date 2020年10月21日 上午11:35:43
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class JSchUtil {

    static Logger log = Logger.getLogger(JSchUtil.class);
    
    private static String host;
    private static int port;
    private static String username;
    private static String password;

    private static JSch jsch;
    private static Session session;
    private static ChannelSftp sftp;

    public JSchUtil() {
        host = ConfigUtil.getProperty("FreeSFTP_IP", Constants.CONFIG_APP);
        port = Integer.parseInt(ConfigUtil.getProperty("FreeSFTP_Port", Constants.CONFIG_APP));
        username = ConfigUtil.getProperty("FreeSFTP_UserName", Constants.CONFIG_APP);
        password = ConfigUtil.getProperty("FreeSFTP_PassWord", Constants.CONFIG_APP);
    }

    /**
     * jsch连接到指定的IP
     *
     * @throws JSchException
     * @throws InterruptedException 
     */
    public static void connect() throws JSchException, InterruptedException {
        try {
            jsch = new JSch();// 创建JSch对象
            session = jsch.getSession(username, host, port);// 根据用户名、主机ip、端口号获取一个Session对象
            session.setPassword(password);// 设置密码

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);// 为Session对象设置properties
            session.setTimeout(1500);// 设置超时
            session.connect();// 通过Session建立连接

            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp)channel;
            log.info(sftp.lpwd());
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("jsch连接失败！",e);
            Thread.sleep(500);
        }
    }

    public static void connect(String host, int port, String user, String passwd) throws JSchException, InterruptedException {
        try {
            jsch = new JSch();// 创建JSch对象
            session = jsch.getSession(user, host, port);// 根据用户名、主机ip、端口号获取一个Session对象
            session.setPassword(passwd);// 设置密码

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);// 为Session对象设置properties
            session.setTimeout(1500);// 设置超时
            session.connect();// 通过Session建立连接

            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp)channel;
            // log.info(sftp.pwd());
//            listFiles("/");
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("jsch连接失败！",e);
            Thread.sleep(500);
        }
    }

    /**
     * 关闭连接
     */
    public static void close() {
        session.disconnect();
    }

    /**
     * 关闭资源
     */
    public static void disconnect() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                log.info("sftp is closed already");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                log.info("sshSession is closed already");
            }
        }
    }

    /**
     * 执行cmd的命令
     *
     * @throws JSchException
     * @throws IOException
     */
    public static void execCmd(String cmd) throws JSchException, IOException {
        Channel channel = null;
        BufferedReader reader = null;
        try {
            if (cmd != null) {
                // 执行命令
                log.info(cmd);
                channel = session.openChannel("exec");
                ((ChannelExec)channel).setCommand("cmd /c " + cmd);
                // ((ChannelExec) channel).setErrStream(System.err);
                channel.connect();
                InputStream in = channel.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                String buf = null;
                while ((buf = reader.readLine()) != null) {
                    log.info(buf);
                }
            }
        } catch (JSchException e) {
            //e.printStackTrace();
            log.error("执行cmd命令失败！",e);
        } finally {
            reader.close();
            channel.disconnect();
        }
    }

    /**
     * 执行shell的命令
     *
     * @throws JSchException
     * @throws IOException
     */
    public static void execShell(String shell) throws JSchException, IOException {
        ChannelShell channel = null;
        BufferedReader reader = null;
        try {
            if (shell != null) {
                channel = (ChannelShell)session.openChannel("shell");
                channel.connect();
                InputStream inputStream = channel.getInputStream();// 从远程端到达的所有数据都能从这个流中读取到
                OutputStream outputStream = channel.getOutputStream();// 写入该流的所有数据都将发送到远程端。
                // 使用PrintWriter流的目的就是为了使用println这个方法
                // 好处就是不需要每次手动给字符串加\n
                PrintWriter printWriter = new PrintWriter(outputStream);
                printWriter.println(shell);
                printWriter.println("exit");
                printWriter.flush();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String msg = null;
                while ((msg = reader.readLine()) != null) {
                    log.info(msg);
                }
            }
        } catch (JSchException e) {
            //e.printStackTrace();
            log.error("执行shell命令失败！",e);
        } finally {
            reader.close();
            channel.disconnect();
        }
    }

    /**
     * 上传文件
     *
     * @param uploadFile 上传文件的路径
     * @param directory 上传文件的保存目录
     * @param sftp
     * @throws JSchException
     * @throws SftpException
     * @throws FileNotFoundException
     */
    public static void upload(String uploadFile, String directory) throws JSchException, FileNotFoundException, SftpException {
        sftp.cd(directory);
        File file = new File(uploadFile);
        sftp.put(new FileInputStream(file), file.getName());
    }

    /**
     * 上传单个文件
     * 
     * @param remotePath 远程保存目录
     * @param remoteFileName 保存文件名
     * @param localPath 本地上传目录(以路径符号结束)
     * @param localFileName 上传的文件名
     */
    public static boolean uploadFile(String localPath, String localFileName, String remotePath, String remoteFileName) {
        FileInputStream in = null;
        try {
            createDir(remotePath);
            File file = new File(localPath + localFileName);
            in = new FileInputStream(file);
            sftp.put(in, remoteFileName);
            return true;
        } catch (FileNotFoundException | SftpException e) {
            //e.printStackTrace();
            log.error("上传单个文件失败！",e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                    log.error("",e);
                }
            }
        }
        return false;
    }

    /**
     * 批量上传文件
     * 
     * @param remotePath 远程保存目录
     * @param localPath 本地上传目录(以路径符号结束)
     * @param del 上传后是否删除本地文件
     */
    public static boolean bacthUploadFile(String localPath, String remotePath, boolean del) {
        try {
            File file = new File(localPath);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() && files[i].getName().indexOf("bak") == -1) {
                    if (uploadFile(localPath, files[i].getName(), remotePath, files[i].getName()) && del) {
                        deleteFile(localPath + files[i].getName());
                    }
                }
            }
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("批量上传文件失败！",e);
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param remotePath 远程下载目录
     * @param localPath 本地存放目录
     * @throws JSchException
     * @throws SftpException
     * @throws InterruptedException 
     */
    public static void download(String remotePath, String remoteFileName, String localPath) throws JSchException, SftpException, InterruptedException {
        try {
            sftp.cd(remotePath);
            File file = new File(localPath + remoteFileName);
            mkdirs(localPath + remoteFileName);
            sftp.get(remoteFileName, new FileOutputStream(file));
        } catch (FileNotFoundException | SftpException e) {
            //e.printStackTrace();
            log.error("下载文件失败！",e);
            Thread.sleep(500);
        }
    }

    /**
     * 下载单个文件
     * 
     * @param remotPath 远程下载目录(以路径符号结束)
     * @param remoteFileName 下载文件名
     * @param localPath 本地保存目录(以路径符号结束)
     * @param localFileName 保存文件名
     */
    public static boolean downloadFile(String remotePath, String remoteFileName, String localPath, String localFileName) {
        try {
            sftp.cd(remotePath);
            File file = new File(localPath + localFileName);
            mkdirs(localPath + localFileName);
            sftp.get(remoteFileName, new FileOutputStream(file));
            return true;
        } catch (FileNotFoundException | SftpException e) {
            //e.printStackTrace();
            log.error("下载文件失败！",e);
        }
        return false;
    }

    /**
     * 批量下载文件
     * 
     * @param remotPath 远程下载目录(以路径符号结束)
     * @param localPath 本地保存目录(以路径符号结束)
     * @param fileFormat 下载文件格式(以特定字符开头,为空不做检验)
     * @param del 下载后是否删除sftp文件
     * @return
     */
    public static boolean batchDownLoadFile(String remotPath, String localPath, String fileFormat, boolean del) {
        try {
            Vector v = listFile(remotPath);
            if (v.size() > 0) {
                Iterator it = v.iterator();
                while (it.hasNext()) {
                    LsEntry entry = (LsEntry)it.next();
                    String filename = entry.getFilename();
                    SftpATTRS attrs = entry.getAttrs();
                    if (!attrs.isDir()) {
                        if (fileFormat != null && !"".equals(fileFormat.trim())) {
                            if (filename.startsWith(fileFormat)) {
                                if (downloadFile(remotPath, filename, localPath, filename) && del) {
                                    deleteSFTP(remotPath, filename);
                                }
                            }
                        } else {
                            if (downloadFile(remotPath, filename, localPath, filename) && del) {
                                deleteSFTP(remotPath, filename);
                            }
                        }
                    }
                }
            }
        } catch (SftpException e) {
            //e.printStackTrace();
            log.error("批量下载文件失败！",e);
        }
        return false;
    }

    /**
     * 删除本地文件
     * 
     * @param filePath 文件的路径
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        if (!file.isFile()) {
            return false;
        }
        return file.delete();
    }

    /**
     * 删除stfp文件
     * 
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public static void deleteSFTP(String directory, String deleteFile) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("文件删除失败！",e);
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    public static Vector listFile(String directory) throws SftpException {
        return sftp.ls(directory);
    }

    /**
     * 列出目录下的文件
     * 
     * @param directory 目录
     * @return ret
     * @throws SftpException
     */
    public static List<String> listFiles(String directory) throws SftpException {
        sftp.cd(directory);
        Vector<String> files = sftp.ls("*");
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            Object obj = files.elementAt(i);
            if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                LsEntry entry = (LsEntry)obj;
                if (true && !entry.getAttrs().isDir()) {
                    ret.add(entry.getFilename());
                }
                if (true && entry.getAttrs().isDir()) {
                    if (!entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                        ret.add(entry.getFilename());
                    }
                }
            }
        }
        log.info(ret);
        return ret;
    }

    /**
     * 创建目录
     * 
     * @param createpath
     * @return
     */
    public static boolean createDir(String createpath) {
        try {
            if (isDirExist(createpath)) {
                sftp.cd(createpath);
                return true;
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString())) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }
            }
            sftp.cd(createpath);
            return true;
        } catch (SftpException e) {
            //e.printStackTrace();
            log.error("创建目录失败！",e);
        }
        return false;
    }

    /**
     * 判断目录是否存在
     * 
     * @param directory
     * @return
     */
    public static boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 如果目录不存在就创建目录
     * 
     * @param path
     */
    public static void mkdirs(String path) {
        File f = new File(path);
        String fs = f.getParent();
        f = new File(fs);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public static void main(String[] args) throws IOException, SftpException, InterruptedException {
        String localPath = System.getProperty("user.dir") + "/Logs/";
        String localPath1 = System.getProperty("user.dir") + "/Logs/";
        String remotePath = "/";
        String remotePath1 = "/data/H5/sit/yhttest/TestOutput/APP/ExtentReport/";
        try {
            // connect();
//            connect("10.18.22.32", 22, "King-liu", "111111");
            connect("10.18.22.65", 22, "Administrator", "111111");
            // execCmd("node -v>" + localPath + "1.txt");
            // execCmd("node -v>"+localPath+"2.txt");
            // execCmd("node -v>"+localPath+"3.txt");
            // execCmd(" node -v>"+localPath+"4.txt");

            // upload(localPath + "appium.txt", remotePath);
            // uploadFile(localPath, "log.txt", remotePath, "log.txt");
            // bacthUploadFile(localPath, remotePath, false);

             download(remotePath , "appium.txt", localPath);
            // downloadFile(remotePath, "appium.txt", localPath, "appium.txt");
            // batchDownLoadFile(remotePath, localPath, null, false);

            // deleteFile("/appium.txt");
            // deleteSFTP("/", "log.txt");

            // connect("10.22.83.50", 38822, "yhttest", "yhttest_300348");
            // execShell("pwd >" + remotePath1 + "1.txt");
            // execShell("cat " + remotePath1 + "1.txt");

            // upload(localPath1 + "log.txt", remotePath1);
            // uploadFile(localPath1, "error.txt", remotePath1, "error.txt");
            // bacthUploadFile(localPath1, remotePath1, false);

            // download(remotePath1 , "1.txt", localPath1);
            // downloadFile(remotePath1, "1.txt", localPath1, "1.txt");
            // batchDownLoadFile(remotePath1, localPath1, null, false);

            // deleteFile(localPath1+"1.txt");
            // deleteSFTP(remotePath1, "1.txt");
             close();
        } catch (JSchException e) {
            //e.printStackTrace();
            log.error("",e);
        }
    }
}
