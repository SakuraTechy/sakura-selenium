package com.sakura.util;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

/**
 * 数据库操作类
 */
@SuppressWarnings("unused")
public class DBHelper {

    static Logger log = Logger.getLogger(DBHelper.class);

    private static final String Oracle_DRIVER = ConfigUtil.getProperty("Oracle.jdbc.driver", ConstantsUtil.CONFIG_JDBC);
    private static final String Gbase8s_DRIVER = ConfigUtil.getProperty("Gbase8s.jdbc.Fdriver", ConstantsUtil.CONFIG_JDBC);

//    private static final String Oracle_FURL = ConfigUtil.getProperty("Oracle.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String Oracle_FUSERNAME = ConfigUtil.getProperty("Oracle.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String Oracle_FPASSWORD = ConfigUtil.getProperty("Oracle.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String Oracle_DDRIVER = ConfigUtil.getProperty("Oracle.jdbc.Ddriver", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String Oracle_DURL = ConfigUtil.getProperty("Oracle.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String Oracle_DUSERNAME = ConfigUtil.getProperty("Oracle.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String Oracle_DPASSWORD = ConfigUtil.getProperty("Oracle.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String Oracle_TDRIVER = ConfigUtil.getProperty("Oracle.jdbc.Tdriver", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String Oracle_TURL = ConfigUtil.getProperty("Oracle.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
//
//    private static final int Oracle_TPort = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("Oracle.jdbc.Tport", ConstantsUtil.CONFIG_JDBC)));
//
//    private static final String Oracle_TUSERNAME = ConfigUtil.getProperty("Oracle.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String Oracle_TPASSWORD = ConfigUtil.getProperty("Oracle.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_FDRIVER = ConfigUtil.getProperty("MySql.jdbc.Fdriver", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_FURL = ConfigUtil.getProperty("MySql.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_FUSERNAME = ConfigUtil.getProperty("MySql.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_FPASSWORD = ConfigUtil.getProperty("MySql.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_DDRIVER = ConfigUtil.getProperty("MySql.jdbc.Ddriver", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_DURL = ConfigUtil.getProperty("MySql.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_DUSERNAME = ConfigUtil.getProperty("MySql.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_DPASSWORD = ConfigUtil.getProperty("MySql.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_TDRIVER = ConfigUtil.getProperty("MySql.jdbc.Tdriver", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_TURL = ConfigUtil.getProperty("MySql.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
//
//    private static final int MySql_TPort = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("MySql.jdbc.Tport", ConstantsUtil.CONFIG_JDBC)));
//
//    private static final String MySql_TUSERNAME = ConfigUtil.getProperty("MySql.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String MySql_TPASSWORD = ConfigUtil.getProperty("MySql.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
//
    private static final String MySqlSSH_FHost = ConfigUtil.getProperty("MySql.ssh.Fhost", ConstantsUtil.CONFIG_JDBC);

    private static final int MySqlSSH_FPort = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("MySql.ssh.Fport", ConstantsUtil.CONFIG_JDBC)));

    private static final String MySqlSSH_FUser = ConfigUtil.getProperty("MySql.ssh.Fuser", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_FPassword = ConfigUtil.getProperty("MySql.ssh.Fpassword", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_FKeyFile = ConfigUtil.getProperty("MySql.ssh.FkeyFile", ConstantsUtil.CONFIG_JDBC);

    private static final int MySqlSSH_FLport = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("MySql.ssh.Flport", ConstantsUtil.CONFIG_JDBC)));

    private static final String MySqlSSH_FRhost = ConfigUtil.getProperty("MySql.ssh.Frhost", ConstantsUtil.CONFIG_JDBC);

    private static final int MySqlSSH_FRport = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("MySql.ssh.Frport", ConstantsUtil.CONFIG_JDBC)));

    private static final String MySqlSSH_FDRIVER = ConfigUtil.getProperty("MySql.jdbc.Fdriver", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_FURL = ConfigUtil.getProperty("MySql.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_FUSERNAME = ConfigUtil.getProperty("MySql.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_FPASSWORD = ConfigUtil.getProperty("MySql.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_DHost = ConfigUtil.getProperty("MySql.ssh.Dhost", ConstantsUtil.CONFIG_JDBC);

    private static final int MySqlSSH_DPort = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("MySql.ssh.Dport", ConstantsUtil.CONFIG_JDBC)));

    private static final String MySqlSSH_DUser = ConfigUtil.getProperty("MySql.ssh.Duser", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_DPassword = ConfigUtil.getProperty("MySql.ssh.Dpassword", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_DKeyFile = ConfigUtil.getProperty("MySql.ssh.DkeyFile", ConstantsUtil.CONFIG_JDBC);

    private static final int MySqlSSH_DLport = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("MySql.ssh.Dlport", ConstantsUtil.CONFIG_JDBC)));

    private static final String MySqlSSH_DRhost = ConfigUtil.getProperty("MySql.ssh.Drhost", ConstantsUtil.CONFIG_JDBC);

    private static final int MySqlSSH_DRport = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("MySql.ssh.Drport", ConstantsUtil.CONFIG_JDBC)));

    private static final String MySqlSSH_DURL = ConfigUtil.getProperty("MySql.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_DUSERNAME = ConfigUtil.getProperty("MySql.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_DPASSWORD = ConfigUtil.getProperty("MySql.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_THost = ConfigUtil.getProperty("MySql.ssh.Thost", ConstantsUtil.CONFIG_JDBC);

    private static final int MySqlSSH_TPort = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("MySql.ssh.Tport", ConstantsUtil.CONFIG_JDBC)));

    private static final String MySqlSSH_TUser = ConfigUtil.getProperty("MySql.ssh.Tuser", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_TPassword = ConfigUtil.getProperty("MySql.ssh.Tpassword", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_TKeyFile = ConfigUtil.getProperty("MySql.ssh.TkeyFile", ConstantsUtil.CONFIG_JDBC);

    private static final int MySqlSSH_TLport = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("MySql.ssh.Tlport", ConstantsUtil.CONFIG_JDBC)));

    private static final String MySqlSSH_TRhost = ConfigUtil.getProperty("MySql.ssh.Trhost", ConstantsUtil.CONFIG_JDBC);

    private static final int MySqlSSH_TRport = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("MySql.ssh.Trport", ConstantsUtil.CONFIG_JDBC)));

    private static final String MySqlSSH_TDRIVER = ConfigUtil.getProperty("MySql.jdbc.ssh.Tdriver", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_TURL = ConfigUtil.getProperty("MySql.jdbc.ssh.Turl", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_TUSERNAME = ConfigUtil.getProperty("MySql.jdbc.ssh.Tusername", ConstantsUtil.CONFIG_JDBC);

    private static final String MySqlSSH_TPASSWORD = ConfigUtil.getProperty("MySql.jdbc.ssh.Tpassword", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String DM_FDRIVER = ConfigUtil.getProperty("DM.jdbc.Fdriver", ConstantsUtil.CONFIG_JDBC);
//    private static final String DM_FURL = ConfigUtil.getProperty("DM.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
//    private static final int DM_FPort = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("DM.jdbc.Fport", ConstantsUtil.CONFIG_JDBC)));
//    private static final String DM_FUSERNAME = ConfigUtil.getProperty("DM.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
//    private static final String DM_FPASSWORD = ConfigUtil.getProperty("DM.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String DM_DDRIVER = ConfigUtil.getProperty("DM.jdbc.Ddriver", ConstantsUtil.CONFIG_JDBC);
//    private static final String DM_DURL = ConfigUtil.getProperty("DM.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
//    private static final int DM_DPort = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("DM.jdbc.Dport", ConstantsUtil.CONFIG_JDBC)));
//    private static final String DM_DUSERNAME = ConfigUtil.getProperty("DM.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
//    private static final String DM_DPASSWORD = ConfigUtil.getProperty("DM.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String DM_TDRIVER = ConfigUtil.getProperty("DM.jdbc.Tdriver", ConstantsUtil.CONFIG_JDBC);
//    private static final String DM_TURL = ConfigUtil.getProperty("DM.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
//    private static final int DM_TPort = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("DM.jdbc.Tport", ConstantsUtil.CONFIG_JDBC)));
//    private static final String DM_TUSERNAME = ConfigUtil.getProperty("DM.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
//    private static final String DM_TPASSWORD = ConfigUtil.getProperty("DM.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
//
//    private static final String SqlServer_FDRIVER = ConfigUtil.getProperty("SqlServer.jdbc.Fdriver", ConstantsUtil.CONFIG_JDBC);
//    private static final String SqlServer_FURL = ConfigUtil.getProperty("SqlServer.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
//    private static final String SqlServer_FUSERNAME = ConfigUtil.getProperty("SqlServer.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
//    private static final String SqlServer_FPASSWORD = ConfigUtil.getProperty("SqlServer.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
//    private static final String SqlServer_DDRIVER = ConfigUtil.getProperty("SqlServer.jdbc.Ddriver", ConstantsUtil.CONFIG_JDBC);
//    private static final String SqlServer_DURL = ConfigUtil.getProperty("SqlServer.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
//    private static final String SqlServer_DUSERNAME = ConfigUtil.getProperty("SqlServer.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
//    private static final String SqlServer_DPASSWORD = ConfigUtil.getProperty("SqlServer.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
//    private static final String SqlServer_TDRIVER = ConfigUtil.getProperty("SqlServer.jdbc.Tdriver", ConstantsUtil.CONFIG_JDBC);
//    private static final String SqlServer_TURL = ConfigUtil.getProperty("SqlServer.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
//    private static final int SqlServer_TPort = Integer.parseInt(Objects.requireNonNull(ConfigUtil.getProperty("SqlServer.jdbc.Tport", ConstantsUtil.CONFIG_JDBC)));
//    private static final String SqlServer_TUSERNAME = ConfigUtil.getProperty("SqlServer.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
//    private static final String SqlServer_TPASSWORD = ConfigUtil.getProperty("SqlServer.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);

    private static String URL;
    private static String NewPort;
    private static String NewServer;
    private static String NewDataBase;
    private static String USER;
    private static String PASSWORD;

    private static ResultSet rs;
    public static Statement sm;
    private static Connection con;
    private static CallableStatement cs;
    private static MongoClient mc;

    /**
     * 数据库插入
     *
     */
    public static int insert(String DataType, String DataEnviron,String DataName,String Port, String Server,String DataBase, String Sql) {
        return executeUpdate(DataType, DataEnviron, DataName,Port, Server,DataBase, Sql, OpType.INSERT);
    }

    /**
     * 数据库删除
     *
     */
    public static int delete(String DataType, String DataEnviron, String DataName,String Port,String Server,String DataBase, String Sql) {
        return executeUpdate(DataType, DataEnviron, DataName,Port, Server,DataBase, Sql, OpType.DELETE);
    }

    /**
     * 数据库修改
     *
     */
    public static int update(String DataType, String DataEnviron, String DataName,String Port,String Server,String DataBase, String Sql) {
        return executeUpdate(DataType, DataEnviron, DataName,Port,Server, DataBase, Sql, OpType.UPDATE);
    }

    /**
     * 执行数据库操作
     *
     */
    private static int executeUpdate(String DataType, String DataEnviron, String DataName,String Port, String Server,String DataBase, String Sql, OpType Type) {
        checkConnection(DataType, DataEnviron, DataName,Port, Server,DataBase);
        PreparedStatement ps = null;
        int rs;
        log.info("Sql: " + Sql);
        try {
            // ps = con.prepareStatement(Sql);
            // int result = ps.executeUpdate();
            rs = sm.executeUpdate(Sql);
            log.info("Result: " + rs);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(Type.desc() + "失败", e.fillInStackTrace());
//            RunUnitService.Step.put("picture", "数据库查询失败");
        } finally {
            close();
        }
        return -1;
    }

    /**
     * 执行数据库操作
     *
     */
    public static Object executeDatabaseOperations(String className, String url, String user, String password, String type, String sql, String params) {
        checkConnection(className, url, user, password);
        Object rs = null;
        try {
            log.info("开始执行: " + sql);
            long startTime = System.currentTimeMillis();
            switch (type) {
                case "executeUpdate":
                    rs = sm.executeUpdate(sql);
                    break;
                case "executeQuery":
                    rs = query(className, url, user, password, sql);
                    break;
                case "prepareCall":
                    log.info("执行参数: " + params);
                    rs = prepareCall(className, url, user, password, sql, params);
                    break;
            }
            log.info("影响数据："+ rs);
            long endTime = System.currentTimeMillis();
            log.info("执行耗时：" + (endTime - startTime) + "ms");
        } catch (SQLException e) {
            log.error("执行数据库操作失败", e.fillInStackTrace());
        } finally {
            close();
        }
        return rs;
    }

    /**
     * 数据库查询
     *
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> query(String className, String url, String user, String password, String sql) {
        ResultSet rs = null;
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        try {
            rs = sm.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                int count = metaData.getColumnCount();
                Map<String, Object> rowMap = new HashMap<String, Object>();
                for (int i = 1; i <= count; i++) {
                    rowMap.put(metaData.getColumnLabel(i), rs.getObject(i));
                    // log.info(rs.getString(i) + "\t");
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        // log.info("\t");
                    }
                }
                // log.info("");
                results.add(rowMap);
            }
            log.info("Count: " + results.size());
        } catch (SQLException e) {
            log.error("查询失败", e.fillInStackTrace());
//            RunUnitService.Step.put("picture", "数据库查询失败");
        }
        return results;
    }

    public static List<LinkedHashMap<String, Object>> query1(String DataType, String DataEnviron, String DataName,String Port,String Server,String DataBase, String Sql) {
        checkConnection(DataType,DataEnviron, DataName,Port, Server,DataBase);
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<LinkedHashMap<String, Object>> results = new ArrayList<LinkedHashMap<String, Object>>();
        log.info("Sql: " + Sql);
        try {
            // ps = connection.prepareStatement(sql);
            // rs = ps.executeQuery();
            rs = sm.executeQuery(Sql);
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                int count = metaData.getColumnCount();
                LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= count; i++) {
                    rowMap.put(metaData.getColumnLabel(i), rs.getObject(i));
                    // log.info(rs.getString(i) + "\t");
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        // log.info("\t");
                    }
                }
                // log.info("");
                results.add(rowMap);
            }
            log.info("Count: " + results.size());
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("查询失败", e.fillInStackTrace());
//            RunUnitService.Step.put("picture", "数据库查询失败");
        } finally {
            close(rs);
        }
        return results;
    }

    public static List<String> query(String DataType, String DataEnviron, String DataName,String Port,String Server,String DataBase, String Sql) {
        checkConnection(DataType,DataEnviron, DataName,Port,Server, DataBase);
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> results = new ArrayList<>();
        List<LinkedHashMap<String, Object>> results1 = new ArrayList<LinkedHashMap<String, Object>>();
        Gson gson = new Gson(); // 使用默认的 Gson 实例，不进行 pretty printing，输出非格式化的json
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();// 输出格式化的json

        log.info("Sql: " + Sql);
        try {
            // ps = connection.prepareStatement(sql);
            // rs = ps.executeQuery();
            rs = sm.executeQuery(Sql);
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                int count = metaData.getColumnCount();
                LinkedHashMap<String, Object> rowMap = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= count; i++) {
                    rowMap.put(metaData.getColumnLabel(i), rs.getObject(i));
                    // log.info(rs.getString(i) + "\t");
//                    if ((i == 2) && (rs.getString(i).length() < 8)) {
//                        // log.info("\t");
//                    }
                }
                // log.info("");
//                results.add(rowMap);
                String json = gson.toJson(rowMap);
                results.add(json);
            }
            log.info("Count: " + results.size());
            log.info("Results: " + results);
//            for (String json : results) {
//                Type type = new TypeToken<LinkedHashMap<String, Object>>(){}.getType();
//                LinkedHashMap<String, Object> map = gson.fromJson(json, type);
//                results1.add(map);
//            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("查询失败", e.fillInStackTrace());
//            RunUnitService.Step.put("picture", "数据库查询失败");
        } finally {
            close(rs);
        }
        return results;
    }

    /**
     * 指定SQL语句,执行查询操作,并打印结果
     *
     */
    public static void Query(String DataType, String DataEnviron,String DataName,String Port,String Server,String DataBase, String Sql) {
        checkConnection(DataType,DataEnviron, DataName,Port,Server, DataBase);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // ps = con.prepareStatement(Sql);
            // rs = ps.executeQuery();
            rs = sm.executeQuery(Sql);
            int col = rs.getMetaData().getColumnCount();
            log.info("============================");
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    log.info(rs.getString(i) + "\t");
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        log.info("\t");
                    }
                }
                log.info("");
            }
            log.info("============================");
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("", e);
        } finally {
            close(rs);
        }
    }

    /** 执行一批SQL查询语句 */
    public static void getBatchQuery(Connection con, String[] sqls) throws Exception {
        boolean supportBatch = supportBatch(con); // 判断是否支持批处理
        if (supportBatch && sqls != null) {
            Statement sm = null;
            try {
                sm = con.createStatement();
                for (int a = 0; a < sqls.length; a++) {
                    ResultSet rs = sm.executeQuery(sqls[a]);
                    int col = rs.getMetaData().getColumnCount();
                    log.info("============================");
                    while (rs.next()) {
                        for (int i = 1; i <= col; i++) {
                            log.info(rs.getString(i) + "\t");
                            if ((i == 2) && (rs.getString(i).length() < 8)) {
                                log.info("\t");
                            }
                        }
                        log.info("");
                    }
                    log.info("============================");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                sm.close();
            }
        }
    }

    /** 执行一批SQL更新语句 */
    public static void goBatchUpdate(Connection con, String[] sqls) throws Exception {
        boolean supportBatch = supportBatch(con); // 判断是否支持批处理
        if (supportBatch && sqls != null) {
            Statement sm = null;
            try {
                sm = con.createStatement();
                for (int i = 0; i < sqls.length; i++) {
                    sm.addBatch(sqls[i]);// 将所有的SQL语句添加到Statement中
                }
                // 一次执行多条SQL语句
                int[] results = sm.executeBatch();// 执行一批SQL语句
                // 分析执行的结果
                for (int i = 0; i < sqls.length; i++) {
                    if (results[i] >= 0) {
                        log.info("语句: " + sqls[i] + " 执行成功，影响了" + results[i] + "行数据");
                    } else if (results[i] == Statement.SUCCESS_NO_INFO) {
                        log.info("语句: " + sqls[i] + " 执行成功，影响的行数未知");
                    } else if (results[i] == Statement.EXECUTE_FAILED) {
                        log.info("语句: " + sqls[i] + " 执行失败");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                sm.close();
            }
        }
    }

    /**
     * 执行存储过程,带参数
     *
     * @return
     */
    public static int procedure(String DataType, String DataEnviron, String DataName,String Port,String Server,String DataBase, String prc_name, Object... params) {
        checkConnection(DataType,DataEnviron, DataName,Port, Server,DataBase);
        CallableStatement cs = null;
        try {
            cs = con.prepareCall(prc_name);
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    // TODO set类型对应数据库,包含输入和输出
                    cs.setString(i + 1, String.valueOf(params[i]));
                }
            }
            log.info("开始执行存储过程: " + prc_name);
            cs.execute();
            log.info("存储过程执行成功 ");
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("存储过程[" + prc_name + "]执行失败", e.fillInStackTrace());
        } finally {
            close(cs);
        }
        return 0;
    }

    public static Object prepareCall(String className, String url, String user, String password, String sql, String params) {
        Object results = null;
        try {
            checkConnection(className, url, user, password);
            CallableStatement cs = con.prepareCall(sql);
            int index = 1;
            // 使用fastjson解析JSON字符串
            JSONArray jsonArray = JSONArray.parseArray(params);
            // 将JSONArray转换为Object数组
            Object[] params1 = jsonArray.toArray(new Object[0]);
            for (Object param : params1) {
                // 根据参数的实际类型设置不同的数据类型
                if (param instanceof String) {
                    cs.setString(index++, (String) param);
                } else if (param instanceof Integer) {
                    cs.setInt(index++, (Integer) param);
                } // 可以继续添加其他类型的参数处理
            }
            boolean hasResultSet = cs.execute();
            if (hasResultSet) {
                // 处理返回的结果集
                while (cs.getMoreResults()) {
                    ResultSet rs = cs.getResultSet();
                    if (rs != null) {
                        // 这里可以根据需要处理结果集，并将结果作为返回值
                        while (rs.next()) {
                            // 处理结果集数据打印第一列
                            log.info(rs.getString(1));
                        }
                        // 例如，你可以将查询结果转换为一个List<Map<String, Object>>或其他数据结构
                        results = convertResultSetToList(rs);
                    }
                }
            } else {
                int updateCount = cs.getUpdateCount();
//                if (updateCount == -1) {
//                    log.info("存储过程执行成功，但没有返回更新计数。");
//                    return "No update count"; // 返回一个状态消息
//                } else {
//                    log.info("存储过程执行成功，更新了 " + updateCount + " 行。");
//                    return updateCount; // 返回更新的行数
//                }
                results = "存储过程执行成功，更新了 " + updateCount + " 行。";
            }
        } catch (SQLException e) {
            log.error("存储过程执行失败", e.fillInStackTrace());
            return e; // 返回异常对象，调用者可以根据需要处理
        }
        return results;
    }

    // 将ResultSet转换为List<Map<String, Object>>
    private static List<Map<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                Object value = getColumnValue(rs, i, metaData.getColumnType(i));
                row.put(metaData.getColumnName(i), value);
            }
            resultList.add(row);
        }
        return resultList;
    }

    // 根据列的数据类型获取值
    private static Object getColumnValue(ResultSet rs, int columnIndex, int sqlType) throws SQLException {
        switch (sqlType) {
            case Types.BOOLEAN:
            case Types.BIT:
                return rs.getBoolean(columnIndex);
            case Types.TINYINT:
                return rs.getByte(columnIndex);
            case Types.SMALLINT:
                return rs.getShort(columnIndex);
            case Types.INTEGER:
                return rs.getInt(columnIndex);
            case Types.BIGINT:
                return rs.getLong(columnIndex);
            case Types.FLOAT:
                return rs.getFloat(columnIndex);
            case Types.DOUBLE:
                return rs.getDouble(columnIndex);
            case Types.DECIMAL:
            case Types.NUMERIC:
                return rs.getBigDecimal(columnIndex);
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return rs.getString(columnIndex);
            case Types.DATE:
                return rs.getDate(columnIndex).toLocalDate();
            case Types.TIME:
                return rs.getTime(columnIndex).toLocalTime();
            case Types.TIMESTAMP:
                return rs.getTimestamp(columnIndex).toLocalDateTime();
            default:
                return rs.getObject(columnIndex);
        }
    }

    // public static int procedure2(String prc_name, String params) {
    // checkConnection();
    // CallableStatement cs = null;
    // try {
    // cs = connection.prepareCall(prc_name);
    // //给存储过程的第一个参数设置值
    // cs.setBigDecimal(1, new BigDecimal(params));
    // //注册存储过程的第二个参数
    // cs.registerOutParameter(2,java.sql.Types.VARCHAR);
    // cs.execute();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return 0;
    // }
    //
    // public static void procedure2(String Process,String Value,String Result) {
    // checkConnection();
    // CallableStatement cs;
    // PreparedStatement ps;
    // try {
    // cs = conn.prepareCall(Process);
    // cs.setString(1, Value);
    // log.info("开始执行存储过程: "+ Process);
    // cs.execute();
    // log.info("存储过程执行成功, 生成数据如下: ");
    // ps = conn.prepareStatement(Result);
    // rs = ps.executeQuery();
    // int col = rs.getMetaData().getColumnCount();
    // log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    // while (rs.next()) {
    // for (int i = 1; i <= col; i++) {
    // log.info(rs.getString(i) + "\t");
    // if ((i == 2) && (rs.getString(i).length() < 8)) {
    // log.info("\t");
    // }
    // }
    // log.info("");
    // }
    // log.info("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    // CloseDatabase();
    // log.info("数据库连接已关闭！");
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // log.info("存储过程执行失败！");
    // }
    // }
//    try {
//		long start = System.currentTimeMillis();
//		// 设置不允许自动提交数据
//		con.setAutoCommit(false);
//		String sql = "INSERT INTO \"TEST\".\"JDBC\" VALUES (?,?)";
//		ps = con.prepareStatement(sql);
//		for (int i = 0; i < 200; i++) {
//			ps.setInt(1,  i);
//			ps.setObject(2, "name" + i);
//			// 1、暂时缓存sql，缓存一定数量之后再与数据库交互，进行插入
//			ps.addBatch();
//			if (i % 50 == 0) { // 缓存50个sql,执行一次数据库插入的交互
//				// 2、执行batch
//				ps.executeBatch();
//				// 3、清空batch
//				ps.clearBatch();
//			}
//		}
//		// 统一提交数据
//		con.commit();
//		long end = System.currentTimeMillis();
//		log.info("花费的时间为：" + (end - start));
//	} catch (Exception e) {
//		e.printStackTrace();
//	} finally {
//		con.close();
//		ps.close();
//	}

//	String sql = "INSERT INTO \"TEST\".\"JDBC\" VALUES (?,?)";
//	ps = con.prepareStatement(sql);
//	ps.setInt(1, 3);
//	ps.setString(2, "小赵");
//	ps.execute();

    public static void checkConnection1(String DataType, String DataEnviron, String DataName,String Port, String DataBase) {
        switch (DataType) {
            case "Oracle":
                checkConnection_Oracle(DataEnviron, DataName,Port, DataBase);
                break;
            case "MySql":
                checkConnection_MySql(DataEnviron, DataName,Port, DataBase);
                break;
            case "SSHKeyMySql":
                checkConnect_SSHKeyMySql(DataEnviron, DataBase);
                break;
            case "SSHPassWordMySql":
                checkConnect_SSHPassWordMySql(DataEnviron, DataBase);
                break;
        }
    }

    public static void checkConnection(String DataType, String DataEnviron, String DataName,String Port, String Server, String DataBase) {
        try {
            if (con == null || con.isClosed()) {
                switch (DataType) {
                    case "Oracle":
                        Connect_Oracle(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "MySql":
                        Connect_MySql(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "SSHKeyMySql":
                        Connect_SSHKeyMySql(DataEnviron, DataBase);
                        break;
                    case "SSHPassWordMySql":
                        Connect_SSHPassWordMySql(DataEnviron, DataBase);
                        break;
                    case "SqlServer":
                        Connect_SqlServer(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "PostgreSQL":
                        Connect_PostgreSQL(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "Greenplum":
                        Connect_Greenplum(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "Sybase":
                        Connect_Sybase(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "Hive":
                        Connect_Hive(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "TiDB":
                        Connect_TiDB(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "OceanBase":
                        Connect_OceanBase(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "MariaDB":
                        Connect_MariaDB(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "Teradata":
                        Connect_Teradata(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "KingBase":
                        Connect_KingBase(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "IRIS":
                        Connect_IRIS(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "Informix":
                        Connect_Informix(DataEnviron, DataName, Port, Server, DataBase);
                        break;
                    case "DB2":
                        Connect_DB2(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "Cache":
                        Connect_Cache(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "GaussDB":
                        Connect_GaussDB(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "Gbase8a":
                        Connect_Gbase8a(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "Gbase8s":
                        Connect_Gbase8s(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "TDengine":
                        Connect_TDengine(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "Hbase":
                        Connect_Hbase(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "DM":
                        Connect_DM(DataEnviron, DataName, Port, DataBase);
                        break;
                    case "MongoDB":
//                        Connect_MongoDB(IP, Port, DataBase);
                        break;
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void checkConnection(String className, String url,String user, String password) {
        try {
            if (con == null || con.isClosed()) {
                connect_Custom_DataBase(className, url, user, password);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void checkConnection_Oracle(String DataEnviron, String DataName,String Port,String DataBase) {
        try {
            if (con == null || con.isClosed())
                Connect_Oracle(DataEnviron, DataName,Port,DataBase);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("", e);
        }
    }

    public static void checkConnection_MySql(String DataEnviron,String DataName,String Port, String DataBase) {
        try {
            if (con == null || con.isClosed())
                Connect_MySql(DataEnviron, DataName,Port, DataBase);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e.fillInStackTrace());
        }
    }

    private static void checkConnect_SSHKeyMySql(String DataEnviron, String DataBase) {
        try {
            if (con == null || con.isClosed())
                Connect_SSHKeyMySql(DataEnviron, DataBase);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e.fillInStackTrace());
        }
    }

    private static void checkConnect_SSHPassWordMySql(String DataEnviron, String DataBase) {
        try {
            if (con == null || con.isClosed())
                Connect_SSHPassWordMySql(DataEnviron, DataBase);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e.fillInStackTrace());
        }
    }

    /**
     * 连接自定义数据库
     *
     */
    public static void connect_Custom_DataBase(String className, String url,String user, String password) throws Exception {
        try {
            Class.forName(className);
            con = DriverManager.getConnection(url, user, password);
            sm = con.createStatement();
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接Oracle数据库
     *
     * @throws Exception
     */
    public static void Connect_Oracle(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            Class.forName(Oracle_DRIVER);
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Oracle.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Oracle.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Oracle.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Oracle.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                USER = StringUtil.isEqual("sys", USER) ? "sys as sysdba" : USER;
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Oracle.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:oracle:thin:@"+URL+":"+NewPort+":"+NewDataBase+"" , USER, PASSWORD);
                sm = con.createStatement();
            }else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Oracle.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Oracle.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Oracle.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Oracle.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                USER = StringUtil.isEqual("sys", USER) ? "sys as sysdba" : USER;
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Oracle.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:oracle:thin:@"+URL+":"+NewPort+":"+NewDataBase+"" , USER, PASSWORD);
                sm = con.createStatement();
            }else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Oracle.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Oracle.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Oracle.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Oracle.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                USER = StringUtil.isEqual("sys", USER) ? "sys as sysdba" : USER;
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Oracle.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:oracle:thin:@"+URL+":"+NewPort+":"+NewDataBase+"" , USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接默认MySql数据库
     *
     * @throws Exception
     */
    public static void Connect_MySql(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_MySql.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_MySql.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_MySql.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_MySql.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_MySql.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mysql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_MySql.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_MySql.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_MySql.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_MySql.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_MySql.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mysql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_MySql.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_MySql.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_MySql.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_MySql.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_MySql.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mysql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接SHHMySql数据库
     *
     * @throws Exception
     */
    public static void Connect_SSHKeyMySql(String DataEnviron, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                SSHKey(DataEnviron);
                Class.forName(MySqlSSH_TDRIVER);
                con = DriverManager.getConnection(MySqlSSH_TURL + "" + DataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", MySqlSSH_TUSERNAME, MySqlSSH_TPASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                SSHKey(DataEnviron);
                Class.forName(MySqlSSH_TDRIVER);
                con = DriverManager.getConnection(MySqlSSH_TURL + "" + DataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", MySqlSSH_TUSERNAME, MySqlSSH_TPASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                SSHKey(DataEnviron);
                Class.forName(MySqlSSH_TDRIVER);
                con = DriverManager.getConnection(MySqlSSH_TURL + "" + DataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", MySqlSSH_TUSERNAME, MySqlSSH_TPASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接SSHPassWordMySql数据库
     *
     * @throws Exception
     */
    public static void Connect_SSHPassWordMySql(String DataEnviron, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                SSHPassWord(DataEnviron);
                Class.forName(MySqlSSH_TDRIVER);
                con = DriverManager.getConnection(MySqlSSH_TURL + "" + DataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", MySqlSSH_TUSERNAME, MySqlSSH_TPASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                SSHPassWord(DataEnviron);
                Class.forName(MySqlSSH_TDRIVER);
                con = DriverManager.getConnection(MySqlSSH_TURL + "" + DataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", MySqlSSH_TUSERNAME, MySqlSSH_TPASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                SSHPassWord(DataEnviron);
                Class.forName(MySqlSSH_TDRIVER);
                con = DriverManager.getConnection(MySqlSSH_TURL + "" + DataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", MySqlSSH_TUSERNAME, MySqlSSH_TPASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    public static void SSHKey(String DataEnviron) throws Exception {
        String passphrase = "11";
        try {
            if ("正式环境".equals(DataEnviron)) {
                JSch jsch = new JSch();
                jsch.addIdentity(MySqlSSH_FKeyFile);
                Session session = jsch.getSession(MySqlSSH_FUser, MySqlSSH_FHost, MySqlSSH_FPort);
                UserInfo ui = new MyUserInfo(passphrase);
                session.setUserInfo(ui);
                session.connect();
                // log.info("SSH服务器连接成功，版本信息为："+session.getServerVersion());//这里打印SSH服务器版本信息
                int assinged_port = session.setPortForwardingL(MySqlSSH_FLport, MySqlSSH_FRhost, MySqlSSH_FRport);
                // log.info("端口映射成功：localhost:" + assinged_port + " -> " + MySqlSSH_FRhost + ":" + MySqlSSH_FRport);
            } else if ("开发环境".equals(DataEnviron)) {
                JSch jsch = new JSch();
                jsch.addIdentity(MySqlSSH_DKeyFile);
                Session session = jsch.getSession(MySqlSSH_DUser, MySqlSSH_DHost, MySqlSSH_DPort);
                UserInfo ui = new MyUserInfo(passphrase);
                session.setUserInfo(ui);
                session.connect();
                // log.info("SSH服务器连接成功，版本信息为："+session.getServerVersion());//这里打印SSH服务器版本信息
                int assinged_port = session.setPortForwardingL(MySqlSSH_DLport, MySqlSSH_DRhost, MySqlSSH_DRport);
                // log.info("端口映射成功：localhost:" + assinged_port + " -> " + MySqlSSH_DRhost + ":" + MySqlSSH_DRport);
            } else if ("测试环境".equals(DataEnviron)) {
                JSch jsch = new JSch();
                jsch.addIdentity(MySqlSSH_TKeyFile);
                Session session = jsch.getSession(MySqlSSH_TUser, MySqlSSH_THost, MySqlSSH_TPort);
                UserInfo ui = new MyUserInfo(passphrase);
                session.setUserInfo(ui);
                session.connect();
                // log.info("SSH服务器连接成功，版本信息为："+session.getServerVersion());//这里打印SSH服务器版本信息
                int assinged_port = session.setPortForwardingL(MySqlSSH_TLport, MySqlSSH_TRhost, MySqlSSH_TRport);
                // log.info("端口映射成功：localhost:" + assinged_port + " -> " + MySqlSSH_TRhost + ":" + MySqlSSH_TRport);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("", e);
            Thread.sleep(500);
        }
    }

    public static void SSHPassWord(String DataEnviron) throws Exception {
        String passphrase = "11";
        try {
            if ("正式环境".equals(DataEnviron)) {
                JSch jsch = new JSch();
                Session session = jsch.getSession(MySqlSSH_FUser, MySqlSSH_FHost, MySqlSSH_FPort);
                session.setPassword(MySqlSSH_FPassword);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                // log.info("SSH服务器连接成功，版本信息为："+session.getServerVersion());//这里打印SSH服务器版本信息
                int assinged_port = session.setPortForwardingL(MySqlSSH_FLport, MySqlSSH_FRhost, MySqlSSH_FRport);
                // log.info("端口映射成功：localhost:" + assinged_port + " -> " + MySqlSSH_FRhost + ":" + MySqlSSH_FRport);
            } else if ("开发环境".equals(DataEnviron)) {
                JSch jsch = new JSch();
                Session session = jsch.getSession(MySqlSSH_DUser, MySqlSSH_DHost, MySqlSSH_DPort);
                session.setPassword(MySqlSSH_DPassword);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                // log.info("SSH服务器连接成功，版本信息为："+session.getServerVersion());//这里打印SSH服务器版本信息
                int assinged_port = session.setPortForwardingL(MySqlSSH_DLport, MySqlSSH_DRhost, MySqlSSH_DRport);
                // log.info("端口映射成功：localhost:" + assinged_port + " -> " + MySqlSSH_DRhost + ":" + MySqlSSH_DRport);
            } else if ("测试环境".equals(DataEnviron)) {
                JSch jsch = new JSch();
                Session session = jsch.getSession(MySqlSSH_TUser, MySqlSSH_THost, MySqlSSH_TPort);
                session.setPassword(MySqlSSH_TPassword);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                // log.info("SSH服务器连接成功，版本信息为："+session.getServerVersion());//这里打印SSH服务器版本信息
                int assinged_port = session.setPortForwardingL(MySqlSSH_TLport, MySqlSSH_TRhost, MySqlSSH_TRport);
                // log.info("端口映射成功：localhost:" + assinged_port + " -> " + MySqlSSH_TRhost + ":" + MySqlSSH_TRport);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("", e);
        }
    }

    /**
     * 连接默认 SqlServer 数据库
     *
     * @throws Exception
     */
    public static void Connect_SqlServer(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_SqlServer.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_SqlServer.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_SqlServer.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_SqlServer.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_SqlServer.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:sqlserver://"+ URL + ":"+NewPort+";DatabaseName=" + NewDataBase + ";encrypt=false;trustServerCertificate=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_SqlServer.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_SqlServer.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_SqlServer.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_SqlServer.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_SqlServer.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:sqlserver://"+ URL + ":"+NewPort+";DatabaseName=" + NewDataBase + ";encrypt=false;trustServerCertificate=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_SqlServer.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_SqlServer.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_SqlServer.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_SqlServer.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_SqlServer.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:sqlserver://"+ URL + ":"+NewPort+";DatabaseName=" + NewDataBase + ";encrypt=false;trustServerCertificate=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 PostgreSQL 数据库
     *
     * @throws Exception
     */
    public static void Connect_PostgreSQL(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_PostgreSQL.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_PostgreSQL.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_PostgreSQL.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_PostgreSQL.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_PostgreSQL.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:postgresql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_PostgreSQL.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_PostgreSQL.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_PostgreSQL.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_PostgreSQL.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_PostgreSQL.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:postgresql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_PostgreSQL.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_PostgreSQL.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_PostgreSQL.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_PostgreSQL.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_PostgreSQL.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:postgresql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 Greenplum 数据库
     *
     * @throws Exception
     */
    public static void Connect_Greenplum(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Greenplum.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Greenplum.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Greenplum.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Greenplum.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Greenplum.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:postgresql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Greenplum.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Greenplum.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Greenplum.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Greenplum.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Greenplum.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:postgresql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Greenplum.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Greenplum.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Greenplum.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Greenplum.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Greenplum.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:postgresql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 KingBase 数据库
     *
     * @throws Exception
     */
    public static void Connect_KingBase(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_KingBase.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_KingBase.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_KingBase.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_KingBase.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_KingBase.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:kingbase8://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_KingBase.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_KingBase.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_KingBase.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_KingBase.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_KingBase.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:kingbase8://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_KingBase.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_KingBase.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_KingBase.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_KingBase.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_KingBase.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:kingbase8://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 IRIS 数据库
     *
     * @throws Exception
     */
    public static void Connect_IRIS(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_IRIS.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_IRIS.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_IRIS.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_IRIS.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_IRIS.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:IRIS://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_IRIS.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_IRIS.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_IRIS.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_IRIS.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_IRIS.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:IRIS://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_IRIS.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_IRIS.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_IRIS.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_IRIS.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_IRIS.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:IRIS://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 Informix 数据库
     *
     * @throws Exception
     */
    public static void Connect_Informix(String DataEnviron, String DataName,String Port, String Server, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Informix.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Informix.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewServer = StringUtil.isNoEmpty(Server) ? Server : ConfigUtil.getProperty("" + DataName + "_Informix.jdbc.Tserver", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Informix.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Informix.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Informix.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:informix-sqli://"+ URL + ":"+NewPort+"/" + NewDataBase + ":informixserver="+NewServer, USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Informix.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Informix.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewServer = StringUtil.isNoEmpty(Server) ? Server : ConfigUtil.getProperty("" + DataName + "_Informix.jdbc.Tserver", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Informix.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Informix.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Informix.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:informix-sqli://"+ URL + ":"+NewPort+"/" + NewDataBase + ":informixserver="+NewServer, USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Informix.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Informix.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewServer = StringUtil.isNoEmpty(Server) ? Server : ConfigUtil.getProperty("" + DataName + "_Informix.jdbc.Tserver", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Informix.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Informix.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Informix.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:informix-sqli://"+ URL + ":"+NewPort+"/" + NewDataBase + ":informixserver="+NewServer, USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 DM 数据库
     *
     * @throws Exception
     */
    public static void Connect_DM(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_DM.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_DM.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_DM.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_DM.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_DM.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:dm://"+ URL + ":"+NewPort+"/schema=" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_DM.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_DM.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_DM.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_DM.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_DM.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:dm://"+ URL + ":"+NewPort+"/schema=" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_DM.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_DM.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_DM.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_DM.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_DM.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:dm://"+ URL + ":"+NewPort+"/schema=" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 Sybase 数据库
     *
     * @throws Exception
     */
    public static void Connect_Sybase(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Sybase.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Sybase.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Sybase.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Sybase.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Sybase.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:sybase:Tds:"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Sybase.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Sybase.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Sybase.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Sybase.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Sybase.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:sybase:Tds:"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Sybase.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Sybase.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Sybase.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Sybase.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Sybase.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:sybase:Tds:"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 Hive 数据库
     *
     * @throws Exception
     */
    public static void Connect_Hive(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Hive.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Hive.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Hive.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Hive.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Hive.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:hive2://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Hive.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Hive.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Hive.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Hive.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Hive.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:hive2://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
//                Class.forName("org.apache.hive.jdbc.HiveDriver");
                URL = ConfigUtil.getProperty(""+DataName+"_Hive.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Hive.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Hive.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Hive.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Hive.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:hive2://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 TiDB 数据库
     *
     * @throws Exception
     */
    public static void Connect_TiDB(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_TiDB.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_TiDB.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_TiDB.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_TiDB.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_TiDB.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mysql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_TiDB.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_TiDB.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_TiDB.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_TiDB.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_TiDB.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mysql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_TiDB.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_TiDB.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_TiDB.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_TiDB.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_TiDB.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mysql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 OceanBase 数据库
     *
     * @throws Exception
     */
    public static void Connect_OceanBase(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_OceanBase.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_OceanBase.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_OceanBase.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_OceanBase.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_OceanBase.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mysql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_OceanBase.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_OceanBase.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_OceanBase.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_OceanBase.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_OceanBase.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mysql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_OceanBase.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_OceanBase.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_OceanBase.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_OceanBase.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_OceanBase.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mysql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 Teradata 数据库
     *
     * @throws Exception
     */
    public static void Connect_Teradata(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Teradata.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Teradata.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Teradata.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Teradata.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Teradata.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:teradata://"+URL+"/DATABASE="+NewDataBase+",DBS_PORT="+NewPort, USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Teradata.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Teradata.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Teradata.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Teradata.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Teradata.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:teradata://"+URL+"/DATABASE="+NewDataBase+",DBS_PORT="+NewPort, USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Teradata.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Teradata.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Teradata.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Teradata.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Teradata.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:teradata://"+URL+"/DATABASE="+NewDataBase+",DBS_PORT="+NewPort, USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 MariaDB 数据库
     *
     * @throws Exception
     */
    public static void Connect_MariaDB(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_MariaDB.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_MariaDB.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_MariaDB.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_MariaDB.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_MariaDB.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mariadb://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_MariaDB.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_MariaDB.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_MariaDB.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_MariaDB.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_MariaDB.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mariadb://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_MariaDB.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_MariaDB.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_MariaDB.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_MariaDB.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_MariaDB.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:mariadb://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 DB2 数据库
     *
     * @throws Exception
     */
    public static void Connect_DB2(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_DB2.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_DB2.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_DB2.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_DB2.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_DB2.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:db2://"+ URL + ":"+NewPort+"/" + NewDataBase, USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_DB2.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_DB2.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_DB2.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_DB2.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_DB2.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:db2://"+ URL + ":"+NewPort+"/" + NewDataBase, USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_DB2.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_DB2.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_DB2.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_DB2.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_DB2.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:db2://"+ URL + ":"+NewPort+"/" + NewDataBase, USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 Cache 数据库
     *
     * @throws Exception
     */
    public static void Connect_Cache(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Cache.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Cache.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Cache.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Cache.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Cache.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:Cache://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Cache.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Cache.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Cache.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Cache.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Cache.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:Cache://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Cache.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Cache.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Cache.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Cache.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Cache.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:Cache://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 GaussDB 数据库
     *
     * @throws Exception
     */
    public static void Connect_GaussDB(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_GaussDB.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_GaussDB.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_GaussDB.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_GaussDB.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_GaussDB.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:postgresql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_GaussDB.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_GaussDB.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_GaussDB.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_GaussDB.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_GaussDB.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:postgresql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_GaussDB.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_GaussDB.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_GaussDB.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_GaussDB.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_GaussDB.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:postgresql://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 Gbase8a 数据库
     *
     * @throws Exception
     */
    public static void Connect_Gbase8a(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Gbase8a.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Gbase8a.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Gbase8a.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Gbase8a.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Gbase8a.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:gbase://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Gbase8a.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Gbase8a.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Gbase8a.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Gbase8a.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Gbase8a.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:gbase://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Gbase8a.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Gbase8a.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Gbase8a.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Gbase8a.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Gbase8a.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:gbase://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 Gbase8s 数据库
     *
     * @throws Exception
     */
    public static void Connect_Gbase8s(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            Class.forName(Gbase8s_DRIVER);
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Gbase8s.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Gbase8s.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Gbase8s.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Gbase8s.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Gbase8s.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:gbasedbt-sqli://"+ URL + ":"+NewPort+"/" + NewDataBase + ":GBASEDBTSERVER=gbaseserver;CLIENT_LOCALE=zh_cn.utf8;SQLMODE=GBase;NEWCODESET=UTF8,zh_cn.UTF8,57372;DB_LOCALE=zh_CN.57372;", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Gbase8s.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Gbase8s.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Gbase8s.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Gbase8s.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Gbase8s.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:gbasedbt-sqli://"+ URL + ":"+NewPort+"/" + NewDataBase + ":GBASEDBTSERVER=gbaseserver;CLIENT_LOCALE=zh_cn.utf8;SQLMODE=GBase;NEWCODESET=UTF8,zh_cn.UTF8,57372;DB_LOCALE=zh_CN.57372;", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Gbase8s.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Gbase8s.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Gbase8s.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Gbase8s.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Gbase8s.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:gbasedbt-sqli://"+ URL + ":"+NewPort+"/" + NewDataBase + ":GBASEDBTSERVER=gbaseserver;CLIENT_LOCALE=zh_cn.utf8;SQLMODE=GBase;NEWCODESET=UTF8,zh_cn.UTF8,57372;DB_LOCALE=zh_CN.57372;", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            if (e instanceof ClassNotFoundException)
                message = "数据库驱动类未找到";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 TDengine 数据库
     *
     * @throws Exception
     */
    public static void Connect_TDengine(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_TDengine.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_TDengine.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_TDengine.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_TDengine.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_TDengine.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:TAOS-RS://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_TDengine.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_TDengine.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_TDengine.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_TDengine.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_TDengine.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:TAOS-RS://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_TDengine.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_TDengine.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_TDengine.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_TDengine.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_TDengine.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:TAOS-RS://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 Hbase 数据库
     *
     * @throws Exception
     */
    public static void Connect_Hbase(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Hbase.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Hbase.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Hbase.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Hbase.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Hbase.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:phoenix://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Hbase.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Hbase.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Hbase.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Hbase.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Hbase.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:phoenix://"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_Hbase.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_Hbase.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_Hbase.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_Hbase.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_Hbase.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                con = DriverManager.getConnection("jdbc:phoenix:"+ URL + ":"+NewPort+"/" + NewDataBase + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", USER, PASSWORD);
                sm = con.createStatement();
            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            throw new Exception(message, e.fillInStackTrace());
        }
    }

    /**
     * 连接 MongoDB 数据库
     *
     * @throws Exception
     */
    public static MongoDBUtil Connect_MongoDB(String DataEnviron, String DataName,String Port, String DataBase) throws Exception {
        MongoDBUtil mongoDBUtil = null;
        try {
            if ("正式环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_MongoDB.jdbc.Furl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_MongoDB.jdbc.Fport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_MongoDB.jdbc.FdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_MongoDB.jdbc.Fusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_MongoDB.jdbc.Fpassword", ConstantsUtil.CONFIG_JDBC);
                mongoDBUtil = new MongoDBUtil(URL, NewPort, USER, PASSWORD, NewDataBase);
            } else if ("开发环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_MongoDB.jdbc.Durl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_MongoDB.jdbc.Dport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_MongoDB.jdbc.DdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_MongoDB.jdbc.Dusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_MongoDB.jdbc.Dpassword", ConstantsUtil.CONFIG_JDBC);
                mongoDBUtil = new MongoDBUtil(URL, NewPort, USER, PASSWORD, NewDataBase);

            } else if ("测试环境".equals(DataEnviron)) {
                URL = ConfigUtil.getProperty(""+DataName+"_MongoDB.jdbc.Turl", ConstantsUtil.CONFIG_JDBC);
                NewPort = StringUtil.isNoEmpty(Port) ? Port : ConfigUtil.getProperty("" + DataName + "_MongoDB.jdbc.Tport", ConstantsUtil.CONFIG_JDBC);
                NewDataBase = StringUtil.isNoEmpty(DataBase) ? DataBase : ConfigUtil.getProperty("" + DataName + "_MongoDB.jdbc.TdataBase", ConstantsUtil.CONFIG_JDBC);
                USER = ConfigUtil.getProperty(""+DataName+"_MongoDB.jdbc.Tusername", ConstantsUtil.CONFIG_JDBC);
                PASSWORD = ConfigUtil.getProperty(""+DataName+"_MongoDB.jdbc.Tpassword", ConstantsUtil.CONFIG_JDBC);
                mongoDBUtil = new MongoDBUtil(URL, NewPort, USER, PASSWORD, NewDataBase);

            }
            log.info("数据库连接成功");
        } catch (Exception e) {
            String message = "数据库连接失败";
            throw new Exception(message, e.fillInStackTrace());
        }
        return mongoDBUtil;
    }

    public static Boolean Connect_MongoDB1(String IP, int Port, String DataBase) {
        try {
            mc = new MongoClient(IP, Port);

            // 创建数据库，如果数据库不存在则会创建，如果存在，则会切换到指定数据库
            MongoDatabase db = mc.getDatabase(DataBase);

            // 删除集合
            db.getCollection("test").drop();

            // 创建集合
            db.createCollection("test");

            // 获取集合
            MongoCollection<Document> table = db.getCollection("test");

            // 插入单个文档
            Document doc = new Document("title", "MongoDB").append("description", "Musql is a RDBMS")
                    .append("by", "sql练习").append("url", "http://www.runoob.com")
                    .append("tages", Arrays.asList("mongodb", "database", "NoSQL")).append("likes", 100);

            table.insertOne(doc);

            // 插入多个文档
            /**
             * 1. 创建文档 org.bson.Document 参数为key-value的格式 2. 创建文档集合List<Document> 3.
             * 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用
             * mongoCollection.insertOne(Document)
             */
            Document doc1 = new Document("title", "MongoDB1").append("description", "database").append("likes", 200)
                    .append("by", "Fly");

            Document doc2 = new Document("title", "MongoDB2").append("description", "Musql is a RDBMS")
                    .append("by", "sql练习").append("url", "http://www.runoob.com")
                    .append("tages", Arrays.asList("mongodb", "database", "NoSQL")).append("likes", 300);

            List<Document> documents = new ArrayList<Document>();
            documents.add(doc1);
            documents.add(doc2);
            table.insertMany(documents);
            log.info("文档插入成功");

            // 查询所有
            FindIterable<Document> data = table.find();
            for (Document record : data) {
//				log.info(record.getString("title")+" : "+record.getString("description"));
                log.info(record.toJson());
            }

            // 条件查询: db.blog.find({"author":"assad"})
            table.find(new Document("title", "MongoDB")).forEach((Block<? super Document>) document -> {
                log.info(document.toJson());
            });

            // 更新文档 将文档中likes=100的文档修改为likes=200
            table.updateMany(Filters.eq("likes", 200), new Document("$set", new Document("likes", 201)));
            // 检索查看结果
            FindIterable<Document> findIterable = table.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while (mongoCursor.hasNext()) {
                log.info(mongoCursor.next().toString());
            }

            // 删除符合条件的第一个文档
            table.deleteOne(Filters.eq("likes", 201));
            // 删除所有符合条件的文档
            table.deleteMany(Filters.eq("likes", 300));
            // 检索查看结果
            FindIterable<Document> findIterable1 = table.find();
            MongoCursor<Document> mongoCursor1 = findIterable1.iterator();
            while (mongoCursor1.hasNext()) {
                log.info(mongoCursor1.next().toString());
            }

//			//连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
//            //ServerAddress()两个参数分别为 服务器地址 和 端口
//            ServerAddress serverAddress = new ServerAddress(IP,Port);
//            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
//            addrs.add(serverAddress);
//            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
//            MongoCredential credential = MongoCredential.createScramSha1Credential("用户名", "数据库名", "密码".toCharArray());
//            List<MongoCredential> credentials = new ArrayList<MongoCredential>();
//            credentials.add(credential);
//            //通过连接认证获取MongoDB连接
//            mc = new MongoClient(addrs,credentials);
//            //连接到数据库
//            MongoDatabase mongoDatabase = mc.getDatabase("yapi");
//            log.info("Connect to database successfully");
//            //选择集合
//            MongoCollection<Document> collection = mongoDatabase.getCollection("user");
//            log.info("集合 test 选择成功");
//			//模糊查询
//            BasicDBObject searchDoc = new BasicDBObject().append("username",new BasicDBObject("$regex","liuzhi"));
//            FindIterable<Document> findIterable=collection.find(searchDoc);
//            MongoCursor<Document> mongoCursor = findIterable.iterator();
//            Document demo=new Document();
//            if(mongoCursor.hasNext()){
//                demo = mongoCursor.next();
//                JSONObject jsonObject=JSONObject.parseObject(demo.toJson());
//                //获取集合中value为Object对象"test"下的com的值
//                log.info(jsonObject.getJSONObject("test").getString("com"));
//                //获取集合中value为Array列表“testArray”下，索引为1的值
//                log.info(jsonObject.getJSONArray("testArray").getString(1).getClass());
//                //循环遍历获取集合中value为Array列表“testArray”下的值
//                for (Object json:jsonObject.getJSONArray("testArray")){
//                    log.info(json);
//                }
//                //获取集合中value为String类型的值
//                log.info(jsonObject.getString("pharmacological_toxicology"));
//            }
            log.info("MongoDB数据库连接成功");
            return true;
        } catch (Exception e) {
            log.info("MongoDB数据库连接失败");
            e.printStackTrace();
        } finally {
            mc.close();
        }
        return false;
    }

    /** 判断数据库是否支持批处理 */
    public static boolean supportBatch(Connection con) {
        try {
            // 得到数据库的元数据
            DatabaseMetaData md = con.getMetaData();
            return md.supportsBatchUpdates();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 释放资源并关闭数据库
     */
    public static void close(ResultSet rs) {
        try {
            if (sm != null) {
                sm.close();
                sm = null;
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            log.info("数据库资源释放成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("数据库资源释放失败！", e);
        }
        close();
    }

    /**
     * 释放资源并关闭数据库
     */
    public static void close(CallableStatement cs) {
        try {
            if (cs != null) {
                cs.close();
                cs = null;
            }
            log.info("数据库资源释放成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("数据库资源释放失败！", e);
        }
        close();
    }

    /**
     * 关闭数据库
     */
    public static void close() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                con = null;
            }
            if (sm != null) {
                sm.close();
                sm = null;
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (cs != null) {
                cs.close();
                cs = null;
            }
            log.info("数据库关闭成功");
        } catch (SQLException e) {
            log.error("数据库关闭失败", e);
        }
    }

    public static class MyUserInfo implements UserInfo {
        private String passphrase = null;

        public MyUserInfo(String passphrase) {
            this.passphrase = passphrase;
        }

        public String getPassphrase() {
            return passphrase;
        }

        public String getPassword() {
            return null;
        }

        public boolean promptPassphrase(String s) {
            return true;
        }

        public boolean promptPassword(String s) {
            return true;
        }

        public boolean promptYesNo(String s) {
            return true;
        }

        public void showMessage(String s) {
            log.info(s);
        }
    }

    /**
     * 数据库操作枚举
     */
    public enum OpType {

        INSERT("插入"), UPDATE("更新"), DELETE("删除"), QUERY("查询"), PROCEDURE("执行存储过程");

        final String desc;

        OpType(String desc) {
            this.desc = desc;
        }

        String desc() {
            return desc;
        }
    }

    public static void main(String[] args) throws Exception {
        String Oracle_sql = "SELECT RAWTOHEX(IP) AS IP, RAWTOHEX(地址) AS 地址 FROM \"ROOT\".\"SENSITIVE_TYPES_ALL\"";
        String MySql_sql = "SELECT * FROM `test`.`jdbc` LIMIT 0,1000";
        String SqlServer_sql = "SELECT * FROM testdb.dbo.sensitive_types_all";
        String PostgreSQL_sql = "SELECT * FROM \"public\".\"users\" LIMIT 1000 OFFSET 0";
        String Greenplum_sql = "SELECT * FROM \"public\".\"users\" LIMIT 1000 OFFSET 0";
        String Sybase_sql = "SELECT id, a_text FROM test.dbo.a_text WHERE id='1'";
        String Hive_sql = "SELECT id, name FROM default.test WHERE id = '1'";
        String TiDB_sql = "SELECT * FROM test.TEST WHERE CVV ='420'";
        String MariaDB_sql = "SELECT * FROM test.student";
        String OceanBase_sql = "SELECT * FROM oceanbase.`__all_user` WHERE tenant_id=0 AND user_id=200001";
        String Teradata_sql = "SELECT a_id, a_timestamp, a_date, a_time FROM test.all_date WHERE a_id ='3'";
        String KingBase_sql ="SELECT dummy FROM sys.dual";
        String IRIS_sql ="SELECT * FROM SYS.Process";
        String Informix_sql ="SELECT * FROM sysadmin:informix.ph_version";
        String DB2_sql ="SELECT * FROM TEST.\"zmy_A_DATE_TYPE\"";
        String Cache_sql ="SELECT * FROM \"%SYS\".Task WHERE ID=1";
        String GaussDB_sql ="SELECT * FROM public.\"name\"";
        String Gbase8s_sql ="SELECT * FROM test:gbasedbt.test1 WHERE id=1";
        String Gbase8a_sql ="SELECT * FROM test.student";
        String TDengine_sql ="SELECT * FROM information_schema.ins_dnodes WHERE id='1'";
        String Hbase_sql ="SELECT NAME, AGE FROM T_USER WHERE AGE=1";

        String DM7_sql = "SELECT \"numid\", \"name\", \"school\", \"addrid\" FROM SYSDBA.\"test3\" WHERE \"numid\"=1;";
        String DM8_sql = "SELECT POPEDOM_ID, IS_ALLOW_BROWSE, IS_ALLOW_DELETE, IS_ALLOW_EDIT, IS_ALLOW_INSERT, MODULE_ID, USER_ID FROM SYSDBA.USER_POPEDOM WHERE \"POPEDOM_ID\"=1;";

//        String[] sqls = new String[1];
//        sqls[0] = "SELECT * FROM `555` ORDER BY df DESC;";
//        sqls[0] = "SELECT * FROM \"TEST\".\"JDBC\"";
//        sqls[0] = "SELECT \"numid\", \"name\", \"school\", \"addrid\" FROM SYSDBA.\"test3\" WHERE \"numid\"=1;";

//        String[] sqls1 = new String[7];
//        sqls1[0] = "DROP TABLE \"TEST\".\"JDBC\"";
//        sqls1[1] = "CREATE TABLE \"TEST\".\"JDBC\" (\r\n"
//        		+ "  \"id\" NUMBER ,\r\n"
//        		+ "  \"name\" VARCHAR2(255 BYTE) \r\n"
//        		+ ")";
//        sqls1[2] = "ALTER TABLE \"TEST\".\"JDBC\" ADD CONSTRAINT \"tableName_PK\" PRIMARY KEY (\"id\")";
//        sqls1[3] = "INSERT INTO \"TEST\".\"JDBC\" VALUES (1, '小王')";
//        sqls1[4] = "INSERT INTO \"TEST\".\"JDBC\" VALUES (2, '小李')";
//        sqls1[5] = "DELETE FROM \"TEST\".\"JDBC\" WHERE \"id\"=2";
//        sqls1[6] = "UPDATE \"TEST\".\"JDBC\" SET \"name\"='小李' WHERE \"id\"=1";
        try {
//            Connect_Oracle("测试环境","Oracle","1521", "","bs_audit");
//        	Connect_MySql("测试环境","MySql","3306", "","bs_audit");
//            Connect_SqlServer("测试环境", "SqlServer", "1433","","master");
//            Connect_DM("测试环境","DM","12345","", "TEST");
//            Connect_Sybase("测试环境","AAS_DM_Y","5000", "","test");
//            Connect_Hive("测试环境","AAS_DM_Y","10000","", "default");
//            query("Oracle","测试环境","AAS_DM_M","1521","sys", "ORCL", Oracle_sql);
//            List<String> st = query("MySql","测试环境","AAS_DM_Y","3306", "","test", MySql_sql);
//            List<String> st = query("SqlServer","测试环境","AAS_DM_Y","1433","", "testdb", SqlServer_sql);
//            List<String> st = query("PostgreSQL","测试环境","AAS_DM_Y","5432","", "postgres", PostgreSQL_sql);
//            query("Greenplum","测试环境","AAS_DM_Y","5432","", "postgres", Greenplum_sql);
//            query("Sybase","测试环境","AAS_DM_Y","5000", "","test", Sybase_sql);
//            query("Hive","测试环境","AAS_DM_Y","10000","", "default", Hive_sql);
//            query("TiDB","测试环境","AAS_DM_Y","4000","", "test", TiDB_sql);
//            query("MariaDB","测试环境","AAS_DM_Y","3306", "test", MariaDB_sql);
//            query("OceanBase","测试环境","AAS_DM_Y","2881","","", "oceanbase", OceanBase_sql);
//            query("Teradata","测试环境","AAS_DM_Y","1025", "","test", Teradata_sql);

//            query("KingBase","测试环境","AAS_DM_Y","54321", "test", KingBase_sql);
//            query("IRIS","测试环境","AAS_DM_Y","1972", "","%sys",  IRIS_sql);
//            query("DB2","测试环境","AAS_DM_M","50000","", "test", DB2_sql);
//            query("Informix","测试环境","AAS_DM_Y","9088", "informix","sysadmin",  Informix_sql);
//            query("Cache","测试环境","AAS_DM_M","1972","", "%sys", Cache_sql);
//            query("GaussDB","测试环境","AAS_DM_Y","25308","", "test", GaussDB_sql);
//            query("Gbase8a","测试环境","AAS_DM_Y","5258","", "test", Gbase8a_sql);
//            query("Gbase8s","测试环境","AAS_DM_Y","9088","", "test", Gbase8s_sql);
//            query("TDengine","测试环境","AAS_DM_Y","6041","", "information_schema", TDengine_sql);
            query("Hbase","测试环境","AAS_DM_Y","2181","", "hbase", Hbase_sql);

//            List<String> st = query("DM","测试环境","AAS_DM_Y","12345", "TEST", DM7_sql);
//            List<String> st = query("DM","测试环境","AAS_DM_M","5236", "TEST", DM8_sql);

//            getBatchQuery(con, sqls);
//            goBatchUpdate(con, sqls1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            close();
        }

//        String className = "com.mysql.jdbc.Driver";
//        String url = "jdbc:mysql://172.19.5.229:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT";
//        String user = "root";
//        String password = "Ankki_mySQL123";
//        String sql1 = "INSERT INTO `TEST`.`JDBC` VALUES (1, '小王', 5/3,5/3,SYSDATE());";
//        String sql2 = "UPDATE `TEST`.`JDBC` SET name='小王' WHERE id=1;";
//        String sql3 = "SELECT * FROM `TEST`.`JDBC`;";
//        String sql4 = "DELETE FROM `TEST`.`JDBC` WHERE id=1;";
//        String sql5 = "{call `TEST`.`JDBC_TEST`(?,?,?,?)};";
//        String callParams = "[1, '小王', 1, 1]";
//        prepareCall(className, url, user, password, sql5, callParams);
    }
}
