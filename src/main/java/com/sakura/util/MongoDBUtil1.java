package com.sakura.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCommandException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;

public class MongoDBUtil1 {
    // 连接信息
    private static final String URI = "mongodb://localhost:27017";
    private static final String DATABASE = "testdb";
    private static final String COLLECTION = "users";

    private static MongoClient client;
    private static MongoCollection<Document> collection;

    public MongoDBUtil1(String host, int port, String username, String password, String databaseName) {
        String connectionString = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + databaseName + "?authSource=admin";
        ConnectionString connString = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .build();

        client = MongoClients.create(settings);
        // 创建数据库，如果数据库不存在则会创建，如果存在，则会切换到指定数据库
        MongoDatabase database = client.getDatabase(databaseName);

        // 判断集合是否存在
        if (collectionExists(database)) {
            try {
                database.createCollection(COLLECTION);
                System.out.println("集合创建成功: " + COLLECTION);
            } catch (MongoCommandException e) {
                if (e.getErrorCode() == 48) { // 错误码48表示集合已存在
                    System.out.println("集合已存在，直接使用: " + COLLECTION);
                } else {
                    throw e;
                }
            }
        }
        collection = database.getCollection(COLLECTION);
    }

    public static void main(String[] args) {
        try {
            // 初始化连接并验证/创建集合
            initialize();

            // 插入文档
            insertUser("Alice", "alice@example.com");
            insertUser("Bob", "bob@example.com");

            // 查询所有文档
            findAllUsers();

            // 更新文档
            updateUserEmail("Alice", "new.alice@example.com");

            // 删除文档
            deleteUser("Bob");

            // 再次查询验证结果
            findAllUsers();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    // 初始化数据库连接并确保集合存在
    private static void initialize() {
        client = MongoClients.create(URI);
        MongoDatabase database = client.getDatabase(DATABASE);

        // 判断集合是否存在
        if (collectionExists(database)) {
            try {
                database.createCollection(COLLECTION);
                System.out.println("集合创建成功: " + COLLECTION);
            } catch (MongoCommandException e) {
                if (e.getErrorCode() == 48) { // 错误码48表示集合已存在
                    System.out.println("集合已存在，直接使用: " + COLLECTION);
                } else {
                    throw e;
                }
            }
        }
        collection = database.getCollection(COLLECTION);
    }

    // 检查集合是否存在
    private static boolean collectionExists(MongoDatabase database) {
        return !database.listCollectionNames()
                .into(new ArrayList<>())
                .contains(COLLECTION);
    }

    // 插入文档
    public static void insertUser(String name, String email) {
        Document doc = new Document()
                .append("name", name)
                .append("email", email);
        collection.insertOne(doc);
        System.out.println("插入成功: " + name);
    }

    // 查询所有文档
    public static void findAllUsers() {
        FindIterable<Document> result = collection.find();
        System.out.println("\n当前用户列表:");
        for (Document doc : result) {
            System.out.printf(
                    "ID: %s | 姓名: %-10s | 邮箱: %s%n",
                    doc.getObjectId("_id"),
                    doc.getString("name"),
                    doc.getString("email")
            );
        }
    }

    // 更新文档
    public static void updateUserEmail(String name, String newEmail) {
        Bson filter = Filters.eq("name", name);
        Bson update = Updates.set("email", newEmail);
        UpdateResult result = collection.updateOne(filter, update);

        if (result.getModifiedCount() > 0) {
            System.out.println("更新成功: " + name);
        } else {
            System.out.println("未找到用户: " + name);
        }
    }

    // 删除文档
    public static void deleteUser(String name) {
        DeleteResult result = collection.deleteOne(Filters.eq("name", name));
        if (result.getDeletedCount() > 0) {
            System.out.println("删除成功: " + name);
        } else {
            System.out.println("未找到用户: " + name);
        }
    }
}