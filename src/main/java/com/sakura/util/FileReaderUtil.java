package com.sakura.util;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import com.google.gson.Gson;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileReaderUtil {
    static Logger log = Logger.getLogger(FileReaderUtil.class);

    public static String readFileContent(String filePath, String isTitleHeader) throws IOException {
        String fileExtension = getFileExtension(filePath);
        Gson gson = new Gson();
        List<Object> resultList = new ArrayList<>();
//        switch (fileType.toLowerCase()) {
        switch (fileExtension.toLowerCase()) {
            case "csv":
            case "txt":
                // 1=222,2=333
//                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        Map<String, String> lineMap = new HashMap<>();
//                        String[] parts = line.split(",");
//                        for (String part : parts) {
//                            String[] keyValue = part.split("=");
//                            if (keyValue.length == 2) {
//                                lineMap.put(keyValue[0].trim(), keyValue[1].trim());
//                            }
//                        }
//                        resultList.add(lineMap);
//                    }
//                }
                // "ID","IP","
                // "1","133.236.218.97",
                if (isTitleHeader.equals("true")) {
                    try (FileReader reader = new FileReader(filePath)) {
                        // 明确设置CSV格式包含表头信息
                        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
                        Map<String, Integer> headerMap = csvParser.getHeaderMap();
                        if (headerMap != null && !headerMap.isEmpty()) {
                            for (CSVRecord record : csvParser) {
                                Map<String, String> recordMap = new HashMap<>();
                                for (String header : headerMap.keySet()) {
                                    recordMap.put(header, record.get(header));
                                }
                                resultList.add(recordMap);
                            }
                        }
                    }catch (IOException e){
                        log.error("文件读取失败",e);
                    }
                } else {
                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(",");
                            for (int i = 0; i < parts.length; i++) {
                                // Remove quotes if present
                                parts[i] = parts[i].replaceAll("^\"|\"$", "");
                            }
                            resultList.add(parts);
                        }
                    }catch (IOException e){
                        log.error("文件读取失败",e);
                    }
                }
                break;
            case "xls":
            case "xlsx":
                try (FileInputStream fis = new FileInputStream(filePath);
                     Workbook workbook = new XSSFWorkbook(fis)) {
                    Sheet sheet = workbook.getSheetAt(0);
                    if (isTitleHeader.equals("true")) {
                        List<String> headers = new ArrayList<>();
                        Row headerRow = sheet.getRow(0);
                        if (headerRow != null) {
                            for (Cell cell : headerRow) {
                                headers.add(cell.getStringCellValue());
                            }
                        }
                        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                            Row row = sheet.getRow(i);
                            if (row != null) {
                                Map<String, String> rowMap = new HashMap<>();
                                for (int j = 0; j < headers.size(); j++) {
                                    Cell cell = row.getCell(j);
                                    if (cell != null) {
                                        try {
                                            rowMap.put(headers.get(j), getCellValueAsString(cell));
                                        } catch (Exception e) {
                                            // 处理异常，例如记录日志或设置默认值
                                            rowMap.put(headers.get(j), "");
                                        }
                                    }
                                }
                                resultList.add(rowMap);
                            }
                        }
                    } else {
                        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                            Row row = sheet.getRow(i);
                            if (row != null) {
                                List<String> rowData = new ArrayList<>();
                                for (int j = 0; j < row.getLastCellNum(); j++) {
                                    Cell cell = row.getCell(j);
                                    if (cell != null) {
                                        try {
                                            rowData.add(getCellValueAsString(cell));
                                        } catch (Exception e) {
                                            // 处理异常，例如记录日志或设置默认值
                                            rowData.add("");
                                        }
                                    } else {
                                        rowData.add("");
                                    }
                                }
                                resultList.add(rowData);
                            }
                        }
                    }
                }catch (Exception e){
                    log.error("文件读取异常",e);
                }
                break;
            case "del":
                // 假设DEL文件格式类似CSV或者文本简单格式，这里简单按行处理分割（实际可能不同）
                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        Map<String, String> lineMap = new HashMap<>();
                        String[] parts = line.split("\t");  // 假设是制表符分隔，可调整
                        for (String part : parts) {
                            String[] keyValue = part.split("=");
                            if (keyValue.length == 2) {
                                lineMap.put(keyValue[0].trim(), keyValue[1].trim());
                            }
                        }
                        resultList.add(lineMap);
                    }
                }
                break;
            case "dump":
                // 非常简单的示例，实际需要根据Oracle Dump具体格式和恢复导入逻辑深入处理
                // 这里假设通过JDBC连接到数据库并查询数据，需要配置好数据库连接等信息
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@your_oracle_host:port:your_service_name", "username", "password");
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM your_table_name");  // 替换为实际表名和查询语句
                    List<String> columnNames = getColumnNames(resultSet);
                    while (resultSet.next()) {
                        Map<String, String> rowMap = new HashMap<>();
                        for (String columnName : columnNames) {
                            rowMap.put(columnName, resultSet.getString(columnName));
                        }
                        resultList.add(rowMap);
                    }
                    resultSet.close();
                    statement.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "dbf":
                // 使用第三方库示例来读取DBF文件，这里只是简单示意，可能需更多调整
                try {
                    // 这里的具体读取逻辑依据jdbf库的使用方式，可能需要进一步完善
                    // net.sf.jdbf.engine.Table table = new net.sf.jdbf.engine.Table(new File(filePath));
                    // 以下省略完整解析循环添加到resultList的逻辑，需要深入实现
                    log.info("DBF文件读取逻辑待进一步完善");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "dat":
                // DAT文件格式多样，不确定具体结构，这里简单按二进制读取示例（可能不符合实际需求）
                try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
                    byte[] buffer = new byte[1024];
                    while (dis.read(buffer) > 0) {
                        // 这里需要根据DAT文件实际存储的数据结构解析，简单打印字节数组示例
                        log.info(new String(buffer));
                    }
                }
                break;
            case "dicom":
                // Dicom是医学影像格式，非常复杂，需要专业库如DCM4CHE等深入解析，这里简单提示
                log.info("Dicom文件需使用专业医学影像库深入解析，暂未完整实现");
                break;
            case "sql":
                // 类似Oracle Dump，简单示例通过JDBC查询恢复数据（实际需根据Dump格式调整）
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://your_mysql_host:port/your_database_name", "username", "password");
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM your_table_name");  // 替换为实际表名和查询语句
                    List<String> columnNames = getColumnNames(resultSet);
                    while (resultSet.next()) {
                        Map<String, String> rowMap = new HashMap<>();
                        for (String columnName : columnNames) {
                            rowMap.put(columnName, resultSet.getString(columnName));
                        }
                        resultList.add(rowMap);
                    }
                    resultSet.close();
                    statement.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                log.info("不支持的文件类型: " + fileExtension.toLowerCase());
        }
        return gson.toJson(resultList);
    }

    private static String getFileExtension(String filePath) {
        int lastIndex = filePath.lastIndexOf('.');
        if (lastIndex > 0) {
            return filePath.substring(lastIndex + 1);
        }
        return "";
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    // Check if the numeric value is actually an integer
                    if (numericValue == Math.floor(numericValue) && !Double.isInfinite(numericValue)) {
                        return String.valueOf((int) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private static List<String> getColumnNames(ResultSet resultSet) throws Exception {
        List<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            columnNames.add(resultSet.getMetaData().getColumnName(i));
        }
        return columnNames;
    }

    public static void main(String[] args) {
        try {
            // CSV文件读取示例
            String FilePath = "D:/1/SENSITIVE_TYPES_ALL_CSV_带标题.csv";
            String Content = readFileContent(FilePath, "true");
            log.info("CSV文件内容: " + Content);

            FilePath = "D:/1/SENSITIVE_TYPES_ALL_CSV_不带标题.csv";
            Content = readFileContent(FilePath, "false");
            log.info("CSV文件内容: " + Content);

            // TXT文件读取示例
            FilePath = "D:/1/SENSITIVE_TYPES_ALL_TXT_带标题.txt";
            Content = readFileContent(FilePath, "true");
            log.info("TXT文件内容: " + Content);

            FilePath = "D:/1/SENSITIVE_TYPES_ALL_TXT_不带标题.txt";
            Content = readFileContent(FilePath, "false");
            log.info("TXT文件内容: " + Content);

            // Excel文件读取示例
            FilePath = "D:/1/SENSITIVE_TYPES_ALL_EXCEL_带标题_0.xlsx";
            Content = readFileContent(FilePath, "true");
            log.info("Excel文件内容: " + Content);

            FilePath = "D:/1/SENSITIVE_TYPES_ALL_EXCEL_不带标题.xlsx";
            Content = readFileContent(FilePath, "false");
            log.info("Excel文件内容: " + Content);
//
//            // DEL文件读取示例
//            String delFilePath = "path/to/your/del/file.del";
//            List<Map<String, String>> delContent = readFileContent(delFilePath, "del");
//            log.info("DEL文件内容: " + delContent);
//
//            // Oracle Dump文件读取示例（需配置好正确数据库连接等信息）
//            String oracleDumpFilePath = "path/to/your/oracle/dump/file.dmp";
//            List<Map<String, String>> oracleDumpContent = readFileContent(oracleDumpFilePath, "oracle dump");
//            log.info("Oracle Dump文件内容: " + oracleDumpContent);
//
//            // DBF文件读取示例（逻辑待完善）
//            String dbfFilePath = "path/to/your/dbf/file.dbf";
//            List<Map<String, String>> dbfContent = readFileContent(dbfFilePath, "dbf");
//            log.info("DBF文件内容: " + dbfContent);
//
//            // DAT文件读取示例（按简单二进制读取，需按实际调整）
//            String datFilePath = "path/to/your/dat/file.dat";
//            List<Map<String, String>> datContent = readFileContent(datFilePath, "dat");
//            log.info("DAT文件内容: " + datContent);
//
//            // Dicom文件读取示例（需专业库深入解析）
//            String dicomFilePath = "path/to/your/dicom/file.dicom";
//            List<Map<String, String>> dicomContent = readFileContent(dicomFilePath, "dicom");
//            log.info("Dicom文件内容: " + dicomContent);
//
//            // MySQL Dump文件读取示例（需配置好正确数据库连接等信息）
//            String mysqlDumpFilePath = "path/to/your/mysql/dump/file.sql";
//            List<Map<String, String>> mysqlDumpContent = readFileContent(mysqlDumpFilePath, "mysql dump");
//            log.info("MySQL Dump文件内容: " + mysqlDumpContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}