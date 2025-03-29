package com.sakura.util;

import java.sql.*;

import org.apache.log4j.Logger;

import com.jcraft.jsch.JSch;  
import com.jcraft.jsch.Session;  
  
public class DBSSH {  
  
    static Logger log = Logger.getLogger(DBSSH.class);
    
    static int lport = 33101;//本地端口  
    static String rhost = "rm-wz9f2uq58wflnolke.mysql.rds.aliyuncs.com";//远程MySQL服务器  
    static int rport = 3306;//远程MySQL服务端口  
  
    public static void go() {  
        String user = "root";//SSH连接用户名  
        String password = "\\Jg9.>P#fa8w";//SSH连接密码  
        String host = "119.23.226.118";//SSH服务器  
        int port = 22;//SSH访问端口  
        try {  
            JSch jsch = new JSch();  
            Session session = jsch.getSession(user, host, port);  
            session.setPassword(password);  
            session.setConfig("StrictHostKeyChecking", "no");  
            session.connect();  
            log.info("SSH服务器连接成功，版本信息为："+session.getServerVersion());//这里打印SSH服务器版本信息  
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);  
            log.info("端口映射成功：localhost:" + assinged_port + " -> " + rhost + ":" + rport);   
        } catch (Exception e) {  
            //e.printStackTrace();  
            log.error("",e);
        }  
    }  
      
    public static void sql() {  
        Connection conn = null;  
        ResultSet rs = null;  
        Statement st = null;  
        try {  
            Class.forName("com.mysql.jdbc.Driver");  
            conn = DriverManager.getConnection("jdbc:mysql://localhost:33101/travel_test?useUnicode=true&characterEncoding=utf-8&useSSL=false", "user2017", "jUPa5wrlCh");  
            st = conn.createStatement();  
            String sql = "SELECT * FROM account";  
            rs = st.executeQuery(sql);  
            log.info("开始执行SQL：" + sql);  
            
           // 获取列名  
            ResultSetMetaData metaData = rs.getMetaData();  
            for (int i = 0; i < metaData.getColumnCount(); i++) {  
                // resultSet数据下标从1开始  
                String columnName = metaData.getColumnName(i + 1);  
                int type = metaData.getColumnType(i + 1);  
                if (Types.INTEGER == type) {  
                    // int  
                } else if (Types.VARCHAR == type) {  
                    // String  
                }  
                log.info(columnName + "\t");  
            }  
            log.info(""); 
            
            // 获取数据  
            while (rs.next()) {  
                for (int i = 0; i < metaData.getColumnCount(); i++) {  
                    // resultSet数据下标从1开始  
                    log.info(rs.getString(i + 1) + "\t");  
                }  
                log.info("");  
      
            }  
            st.close();  
            conn.close();  
        } catch (Exception e) {  
            //e.printStackTrace();  
            log.error("",e);
        } finally {  
            //rs.close();st.close();conn.close();  
        }  
    }  
  
    @SuppressWarnings("unused")
	private static void getData() throws SQLException, ClassNotFoundException {  
    	  
        Connection conn = null;  
        // 获取所有表名  
        Class.forName("com.mysql.jdbc.Driver");  
        conn = DriverManager.getConnection("jdbc:mysql://localhost:33101/travel_test?useUnicode=true&characterEncoding=utf-8&useSSL=false", "user2017", "jUPa5wrlCh");
		Statement statement = conn.createStatement();  
		ResultSet resultSet = statement  
                .executeQuery("SELECT * FROM account");  
        // 获取列名  
        ResultSetMetaData metaData = resultSet.getMetaData();  
        for (int i = 0; i < metaData.getColumnCount(); i++) {  
            // resultSet数据下标从1开始  
            String columnName = metaData.getColumnName(i + 1);  
            int type = metaData.getColumnType(i + 1);  
            if (Types.INTEGER == type) {  
                // int  
            } else if (Types.VARCHAR == type) {  
                // String  
            }  
            log.info(columnName + "\t");  
        }  
        log.info("");  
        // 获取数据  
        while (resultSet.next()) {  
            for (int i = 0; i < metaData.getColumnCount(); i++) {  
                // resultSet数据下标从1开始  
                log.info(resultSet.getString(i + 1) + "\t");  
            }  
            log.info("");  
  
        }  
        statement.close();  
        conn.close();  
    }
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {  
        go();  
        sql();
    }  
}
