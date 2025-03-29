package com.sakura.util;

import org.apache.log4j.Logger;
import org.bson.Document;
import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MongoDBUtil {
    static Logger log = Logger.getLogger(MongoDBUtil.class);
    
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MongoDBUtil(String host, String port, String username, String password, String databaseName) {
        String connectionString = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + databaseName + "?authSource=admin";
        ConnectionString connString = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .build();

        this.mongoClient = MongoClients.create(settings);
        // 创建数据库，如果数据库不存在则会创建，如果存在，则会切换到指定数据库
        database = mongoClient.getDatabase(databaseName);
    }

    // 创建集合，如果集合不存在则创建
    public void createCollection(String collectionName) {
        List<String> collectionNames = new ArrayList<>();
        database.listCollectionNames().into(collectionNames);
        if (!collectionNames.contains(collectionName)) {
            database.createCollection(collectionName);
            log.info("Collection created: " + collectionName);
        } else {
            log.info("Collection already exists: " + collectionName);
        }
    }

    // 执行增删改查操作的方法
    public List<String> executeOperation(String operationType, String collectionName, Document filterDoc, Document updateDoc) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        switch (operationType) {
            case "CREATE":
                collection.insertOne(filterDoc); // 这里filterDoc作为要插入的文档
                return null;
            case "DELETE":
                collection.deleteOne(filterDoc).getDeletedCount();
                return null;
            case "UPDATE":
                collection.updateOne(filterDoc, new Document("$set", updateDoc));
                return null;
            case "SELECT":
                // 查询所有
                FindIterable<Document> foundDocuments;
                List<String> results = new ArrayList<>();
                if (filterDoc == null || filterDoc.isEmpty()) {
                    foundDocuments = collection.find();
                } else {
                    foundDocuments = collection.find(filterDoc);
                }
                for (Document doc : foundDocuments) {
//                    log.info(doc.toJson());
                    results.add(doc.toJson());
                }
                return results; // 返回查询结果
            default:
                throw new IllegalArgumentException("Unsupported operation type.");
        }
    }

    // 关闭连接
    public void close() {
        mongoClient.close();
    }

    public enum OperationType {
        CREATE, DELETE, UPDATE, SELECT
    }

    public static void main(String[] args) {
        MongoDBUtil mongoDBUtil = new MongoDBUtil("172.19.1.233", "27017", "root", "Ceshi123", "test");

//        // 创建集合
//        mongoDBUtil.createCollection("test");
//
//        // 插入数据
//        Document toInsert = new Document("name", "MongoDB")
//                .append("type", "database")
//                .append("count", 1)
//                .append("info", new Document("x", 203).append("y", 102));
//        Document doc = new Document("title", "MongoDB")
//                .append("description", "Musql is a RDBMS")
//                .append("by", "sql练习")
//                .append("url", "http://www.runoob.com")
//                .append("tages", Arrays.asList("mongodb", "database", "NoSQL"))
//                .append("likes", 100);
//        mongoDBUtil.executeOperation(OperationType.CREATE, "test", toInsert, null);
//
//        // 删除数据
//        Document deleteFilter = new Document("name", "MongoDB");
//        mongoDBUtil.executeOperation(OperationType.DELETE, "test", deleteFilter, null);
//
//        // 更新数据
//        Document updateFilter = new Document("name", "MongoDB");
//        Document updateDoc = new Document("count", 2);
//        mongoDBUtil.executeOperation(OperationType.UPDATE, "yourCollectionName", updateFilter, updateDoc);

        // 查询数据
        Document findFilter = new Document("name", "张三");
        List<String> st = mongoDBUtil.executeOperation("SELECT", "tb4", null, null);
        log.info(st);
        // 关闭连接
        mongoDBUtil.close();
    }
}
