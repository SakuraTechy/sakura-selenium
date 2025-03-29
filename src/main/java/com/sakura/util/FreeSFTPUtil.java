package com.sakura.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

@SuppressWarnings({"static-access", "rawtypes", "unchecked"})
public class FreeSFTPUtil {
    
    Logger log = Logger.getLogger(FreeSFTPUtil.class);
    
    private String host;
    private String username;
    private String password;
    private int port;
    private static ChannelSftp sftp = null;
    private Session sshSession = null;

    public FreeSFTPUtil() {
        this.host = ConfigUtil.getProperty("FreeSFTP_IP", Constants.CONFIG_APP);
        this.port = Integer.parseInt(ConfigUtil.getProperty("FreeSFTP_Port", Constants.CONFIG_APP));
        this.username = ConfigUtil.getProperty("FreeSFTP_UserName", Constants.CONFIG_APP);
        this.password = ConfigUtil.getProperty("FreeSFTP_PassWord", Constants.CONFIG_APP);
    }
    
    public FreeSFTPUtil(String host,int port,String username,String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 连接Sftp服务
     */
    public void connect() {
        try {
            log.info("开始连接sftp！");
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp)channel;
            log.info("sftp连接成功！");
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("sftp连接失败！",e);
        }
    }

    /**
     * 关闭资源
     */
    public void disconnect() {
        if (this.sftp != null) {
            if (this.sftp.isConnected()) {
                this.sftp.disconnect();
                log.info("sftp is closed already");
            }
        }
        if (this.sshSession != null) {
            if (this.sshSession.isConnected()) {
                this.sshSession.disconnect();
                log.info("sshSession is closed already");
            }
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
    public boolean downloadFile(String remotePath, String remoteFileName, String localPath, String localFileName) {
        try {
            sftp.cd(remotePath);
            File file = new File(localPath + localFileName);
            mkdirs(localPath + localFileName);
            sftp.get(remoteFileName, new FileOutputStream(file));
            return true;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } catch (SftpException e) {
            //e.printStackTrace();
            log.error("文件下载失败！",e);
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
    public boolean batchDownLoadFile(String remotPath, String localPath, String fileFormat, boolean del) {
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
                                if (this.downloadFile(remotPath, filename, localPath, filename) && del) {
                                    deleteSFTP(remotPath, filename);
                                }
                            }
                        } else {
                            if (this.downloadFile(remotPath, filename, localPath, filename) && del) {
                                deleteSFTP(remotPath, filename);
                            }
                        }
                    }
                }
            }
        } catch (SftpException e) {
            //e.printStackTrace();
            log.error("文件下载失败！",e);
        }
        return false;
    }

    /**
     * 上传单个文件
     * 
     * @param localPath 本地上传目录(以路径符号结束)
     * @param localFileName 上传的文件名
     * @param remotePath 远程保存目录
     * @param remoteFileName 保存文件名
     */
    public boolean uploadFile(String localPath, String localFileName, String remotePath, String remoteFileName, boolean del) {
        FileInputStream in = null;
        try {
            createDir(remotePath);
            File file = new File(localPath + localFileName);
            in = new FileInputStream(file);
            sftp.put(in, remoteFileName);
            if(del) {
            	deleteFile(localPath + localFileName);
            }
            log.info("开始上传文件，本地路径为：" + "[" + file.getPath()+ "]");
            log.info("文件上传成功，远程路径为：" + "[" + remotePath+remoteFileName+ "]");
            return true;
        } catch (FileNotFoundException | SftpException e) {
            //e.printStackTrace();
            log.error("文件上传失败！",e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //e.printStackTrace();
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
    public boolean bacthUploadFile(String localPath, String remotePath, boolean del) {
        try {
            File file = new File(localPath);
            File[] files = file.listFiles();
            createDir(remotePath);
            assert files != null;
            for (File value : files) {
                if (value.isFile() && !value.getName().contains("bak")) {
                    if (this.uploadFile(localPath, value.getName(), remotePath, value.getName(), del) && del) {
                        deleteFile(localPath + value.getName());
                    }
                }
            }
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("文件上传失败！",e);
        }
        return false;
    }

    /**
     * 创建目录
     * 
     * @param createpath
     * @return
     */
    public boolean createDir(String createpath) {
        try {
            if (isDirExist(createpath)) {
                this.sftp.cd(createpath);
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
            this.sftp.cd(createpath);
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
    public boolean isDirExist(String directory) {
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
     * 删除本地文件
     * 
     * @param filePath
     * @return
     */
    public boolean deleteFile(String filePath) {
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
    public void deleteSFTP(String directory, String deleteFile) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("删除stfp文件失败！",e);
        }
    }

    /**
     * 如果目录不存在就创建目录
     * 
     * @param path
     */
    public void mkdirs(String path) {
        File f = new File(path);
        String fs = f.getParent();
        f = new File(fs);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public Vector listFile(String directory) throws SftpException {
        return sftp.ls(directory);
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 目录
     * @return ret
     * @throws SftpException
     */
    public List<String> listFiles(String directory) throws SftpException {
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
        log.info("远程目录文件："+ret);
        return ret;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChannelSftp getSftp() {
        return sftp;
    }

    public void setSftp(ChannelSftp sftp) {
        this.sftp = sftp;
    }

    public static void main(String[] args) throws SftpException {
        FreeSFTPUtil ftp = new FreeSFTPUtil("172.19.5.45", 22, "root", "@nKk1^2Oe38&8!~!");
        String localPath = System.getProperty("user.dir") + "/TestData/AAS/V6.5B05/Pcap/MySQL/";
        String remotePath = "/data/pcap/MySQL/";
        
        ftp.connect();
//        ftp.deleteSFTP(remotePath, "log.txt");
//        ftp.deleteSFTP(remotePath, "error.txt");
//        ftp.deleteSFTP(remotePath, "appium.txt");
//         ftp.uploadFile(localPath, "MySQL@@MySQL 5.6@@172.19.3.52@@3306@@UTF-8@@视图.pcap",remotePath, "MySQL@@MySQL 5.6@@172.19.3.52@@3306@@UTF-8@@视图.pcap",false);
         ftp.bacthUploadFile(localPath,remotePath,false);
        // ftp.downloadFile(remotePath, "appium.txt",localPath, "appium.txt");
        // ftp.batchDownLoadFile(remotePath, localPath, null, false);
        ftp.listFiles(remotePath);
        ftp.disconnect();
        System.exit(0);
    }
}