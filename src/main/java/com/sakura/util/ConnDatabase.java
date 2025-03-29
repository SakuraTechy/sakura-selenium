package com.sakura.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ConnDatabase {
	// 首先定义下连接数据的驱动，URL、用户名、密码
	static String driver = "oracle.jdbc.driver.OracleDriver";
	static String url = "jdbc:oracle:thin:@172.19.2.33:3307:orcl";
	static String user = "system";
	static String password = "3edc$RFV";

	public static void main(String[] args) throws SQLException {
//		statics();
//		dynamic();
//		procedure();
//		MongoDB();
		SQLServer();
	}

	/**
	 * 执行静态SQL案例
	 */
	public static void statics() throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet rs = null;
		try {
			// 1.加载驱动，使用了反射的知识
			Class.forName(driver);
			/*
			 * 使用DriverManager获取数据库连接 其中返回的Connection就代表了JAVA程序和数据库的连接
			 * 不同数据库的URL写法需要查看驱动文档知道，用户名、密码由DBA分配
			 */
			con = DriverManager.getConnection(url, user, password);
			// 使用Connection来创建一个Statement对象
			sm = con.createStatement();
			// 执行静态SQL语句
			String[] sqls = new String[7];
			sqls[0] = "DROP TABLE \"TEST\".\"JDBC\"";
			sqls[1] = "CREATE TABLE \"TEST\".\"JDBC\" (\r\n" + "  \"id\" NUMBER ,\r\n"
					+ "  \"name\" VARCHAR2(255 BYTE) \r\n" + ")";
			sqls[2] = "ALTER TABLE \"TEST\".\"JDBC\" ADD CONSTRAINT \"tableName_PK\" PRIMARY KEY (\"id\")";
			sqls[3] = "INSERT INTO \"TEST\".\"JDBC\" VALUES (1, '小王')";
			sqls[4] = "INSERT INTO \"TEST\".\"JDBC\" VALUES (2, '小李')";
			sqls[5] = "DELETE FROM \"TEST\".\"JDBC\" WHERE \"id\"=2";
			sqls[6] = "UPDATE \"TEST\".\"JDBC\" SET \"name\"='小李' WHERE \"id\"=1";
			
			String sqls1 = "CREATE TABLESPACE D_test DATAFILE 'D_test.dbf' SIZE 40M AUTOEXTEND ON NEXT 5M MAXSIZE 100M ONLINE LOGGING EXTENT MANAGEMENT LOCAL UNIFORM SIZE 2M SEGMENT SPACE MANAGEMENT AUTO";
			sm.executeUpdate(sqls1);

			// 将所有的SQL语句添加到Statement中
			for (int i = 0; i < sqls.length; i++) {
				sm.addBatch(sqls[i]);
			}
			// 执行一批SQL语句
			int[] results = sm.executeBatch();
			for (int i = 0; i < sqls.length; i++) {
				if (results[i] >= 0) {
					System.out.println("语句: " + sqls[i] + " 执行成功，影响了" + results[i] + "行数据");
				} else if (results[i] == Statement.SUCCESS_NO_INFO) {
					System.out.println("语句: " + sqls[i] + " 执行成功，影响的行数未知");
				} else if (results[i] == Statement.EXECUTE_FAILED) {
					System.out.println("语句: " + sqls[i] + " 执行失败");
				}
			}
//			/*
//			 * Statement有三种执行SQL语句的方法 1、execute可执行任何SQL语句 --返回一个boolean值，
//			 * 如果执行后，第一个结果是ResultSet,则返回true，否则返回false 2、executeQuery 执行select 语句
//			 * --返回查询到的结果集 3、executeUpdate用于执行DML语句。---返回一个整数，代表被SQL语句影响的记录数
//			 */
			String sql = "SELECT * FROM \"TEST\".\"JDBC\"";
			rs = sm.executeQuery(sql);
			// ResultSet有系列的GetXXX(索引名||列名),用于获取记录指针指向行、特定列的值
			// 不断的使用next将记录指针下移一行，如果依然指向有效行，则指针指向有效行的记录
			while (rs.next()) {
				// 使用索引
				System.out.println("id：" + rs.getInt(1) + "      name：" + rs.getString(2));
				// 使用列名
				System.out.println("id：" + rs.getInt("id") + "      name：" + rs.getString("name"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			con.close();
			sm.close();
//			rs.close();
		}
	}

	/**
	 * 执行动态SQL案例
	 */
	public static void dynamic() throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);

			String[] sqls = new String[7];
			sqls[0] = "DROP TABLE \"TEST\".\"JDBC\"";
			sqls[1] = "CREATE TABLE \"TEST\".\"JDBC\" (\r\n" + "  \"id\" NUMBER ,\r\n"
					+ "  \"name\" VARCHAR2(255 BYTE) \r\n" + ")";
			sqls[2] = "ALTER TABLE \"TEST\".\"JDBC\" ADD CONSTRAINT \"tableName_PK\" PRIMARY KEY (\"id\")";
			sqls[3] = "INSERT INTO \"TEST\".\"JDBC\" VALUES (?, ?)";
			sqls[4] = "INSERT INTO \"TEST\".\"JDBC\" VALUES (?, ?)";
			sqls[5] = "DELETE FROM \"TEST\".\"JDBC\" WHERE \"id\"=2";
			sqls[6] = "UPDATE \"TEST\".\"JDBC\" SET \"name\"='小李' WHERE \"id\"=1";
			for (int i = 0; i < sqls.length; i++) {
				ps = con.prepareStatement(sqls[i]);
				if(i==3) {
					ps.setInt(1,  1);
					ps.setObject(2, "小王");
				}
				if(i==4) {
					ps.setInt(1,  2);
					ps.setObject(2, "小李");
				}
				ps.execute();
			}
			
			String sql1 = "SELECT * FROM \"TEST\".\"JDBC\"";
			rs = ps.executeQuery(sql1);
			while (rs.next()) {
				System.out.println("id：" + rs.getInt("id") + "      name：" + rs.getString("name"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}  finally {
			con.close();
			ps.close();
			rs.close();
		}
	}
	
	/**
	 * 执行存储过程案例
	 */
	public static void procedure() throws SQLException {
		Connection con = null;
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);

	        String sql = "{call JDBC_TEST(?,?,?,?)}";
			call = con.prepareCall(sql);
			call.setInt(1, 10);
			call.setString(2, "小四");
			call.setFloat(3, 5);
			call.setLong(4, 5);
			
//			call.setDate(5, new java.sql.Date(System.currentTimeMillis()));
//			call.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
			
			call.execute();
			
			String sql1 = "SELECT * FROM \"TEST\".\"JDBC\"";
			rs = call.executeQuery(sql1);
			while (rs.next()) {
				System.out.println("id：" + rs.getInt("id") + "      name：" + rs.getString("name")+ "      float：" + rs.getFloat("float")+ "      long：" + rs.getLong("long")+ "      date：" + rs.getString("date"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			con.close();
			call.close();
			rs.close();
		}
	}
	
	/**
	 * 连接MongoDB
	 */
	public static void MongoDB() throws SQLException {
		MongoClient mongoClient = null;
		try{  
			mongoClient = new MongoClient("172.19.2.250" , 27017);  
			MongoDatabase db = mongoClient.getDatabase("yapi");  
			MongoCollection<Document> table = db.getCollection("test_01");  
			FindIterable<Document> data =  table.find();
			for(Document record : data) {
				System.out.println(record.getInteger("title")+" : "+record.getString("description"));
				System.out.println(record.toJson());
			}
			
			Document doc = new Document("title","MongoDB 学习")
			        .append("description","Musql is a RDBMS")
			        .append("by","sql练习")
			        .append("url","http://www.runoob.com")
			        .append("tages", Arrays.asList("mongodb","database","NoSQL"))
			        .append("likes",100000);
			table.insertOne(doc);
		} catch(Exception e){  
			System.out.println(e);  
		} finally {
			mongoClient.close();
		}
	}
	
	/**
	 * 连接SQLServer
	 */
	public static void SQLServer() throws SQLException {
		Connection con = null;
		
		String url = "jdbc:sqlserver://172.19.2.33:1433;DatabaseName=P_TEST;encrypt=false;trustServerCertificate=false";
        String user = "sa";
        String password = "Ceshi123";
        String select = "SELECT * FROM P_TEST;";
        String update = "update P_TEST set sex='女' where id=1";
        String insert="insert into P_TEST(id,name,sex) values(1,'小王','男')";
        String delete="delete  from P_TEST where id=1";

		try{  
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = null;
			statement = con.prepareStatement(select);
			ResultSet res = null;
            res = statement.executeQuery();
            //当查询下一行有记录时：res.next()返回值为true，反之为false
            while (res.next()) {
                int id = res.getInt("id");
                String name = res.getString("name");
                String sex = res.getString("sex");
                System.out.println("学号：" + id + "姓名：" + name + " 性别：" + sex);
            }
            
            //修改
            statement = con.prepareStatement(update);
            int res1 = statement.executeUpdate();
            System.out.println(res1);

            //添加
            statement = con.prepareStatement(insert);
            int res2=statement.executeUpdate();
            System.out.println(res2);

            //删除
            statement = con.prepareStatement(delete);
            int res3=statement.executeUpdate();
            System.out.println(res3);
		}catch(Exception e){  
			System.out.println(e);  
		}  
	}
}