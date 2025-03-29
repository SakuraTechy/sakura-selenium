package com.sakura.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.*;

public class ZipUtil {
    static Logger log = Logger.getLogger(ZipUtil.class);
    /**
     * 将文件或文件夹压缩成ZIP格式
     *
     * @param sourceFile 要压缩的文件或文件夹路径
     * @param zipFilePath 压缩后的ZIP文件路径
     * @throws IOException 如果发生I/O错误
     */
    public static void toZip(String sourceFile, String zipFilePath) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)));
        File source = new File(sourceFile);
        zipFile(zos, source, "");
        zos.close();
    }

    /**
     * 递归压缩文件和文件夹
     *
     * @param zos        ZIP输出流
     * @param source     要压缩的文件或文件夹
     * @param baseFolder 基础文件夹路径
     * @throws IOException 如果发生I/O错误
     */
    private static void zipFile(ZipOutputStream zos, File source, String baseFolder) throws IOException {
        if (source.isDirectory()) {
            File[] sourceFiles = source.listFiles();
            if (sourceFiles != null) {
                for (File file : sourceFiles) {
                    zipFile(zos, file, source.getName() + "/");
                }
            }
        } else {
            String path = (baseFolder + source.getName()).replace("\\", "/");
            ZipEntry zipEntry = new ZipEntry(path);
            zos.putNextEntry(zipEntry);
            FileInputStream fis = new FileInputStream(source);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            fis.close();
        }
    }

    /**
     * 解压ZIP文件
     *
     * @param zipFilePath  ZIP文件路径
     * @param destDir     解压后的目录路径
     * @return 返回所有文件名的列表
     * @throws IOException 如果发生I/O错误
     */
    public static List<String> unzip(String zipFilePath, String destDir) throws IOException {
        List<String> fileNames = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(zipFilePath)))) {
            ZipEntry zipEntry = zis.getNextEntry();
            int count;
            byte[] buffer = new byte[8192]; // 调整缓冲区大小

            File dir = new File(destDir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Could not create directory: " + dir);
            }

            while (zipEntry != null) {
                String zipEntryName = zipEntry.getName();
                // 防止路径遍历攻击
                Path entryPath = Paths.get(destDir, zipEntryName);
                if (!entryPath.normalize().startsWith(Paths.get(destDir).normalize())) {
                    throw new IOException("Invalid zip entry path: " + zipEntryName);
                }

                String filePath = entryPath.toString();
                if (zipEntry.isDirectory()) {
                    File dirPath = new File(filePath);
                    if (!dirPath.exists() && !dirPath.mkdirs()) {
                        throw new IOException("Could not create directory: " + dirPath);
                    }
                } else {
                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        while ((count = zis.read(buffer)) != -1) {
                            fos.write(buffer, 0, count);
                        }
                    }
                    fileNames.add(zipEntryName); // 收集文件名
                }
                zipEntry = zis.getNextEntry();
            }
        } catch (IOException e) {
            log.error("Error reading zip file: " + zipFilePath);
            throw new IOException("Error reading zip file: " + zipFilePath, e);
        }
        return fileNames;
    }

    public static void main(String[] args) {
        try {
            // 使用示例：将文件夹压缩成ZIP
            String sourceFolder = "D:/敏感发现结果.xlsx";
            String zipFile = "D:/敏感发现结果.zip";
            ZipUtil.toZip(sourceFolder, zipFile);

            // 使用示例：解压ZIP文件
            String zipFilePath = "D:/敏感发现结果.zip";
            String destDir = "D:/";
            List<String> fileNames1 = ZipUtil.unzip(zipFilePath, destDir);
            log.info(fileNames1);
            // 打印所有文件名
            for (String fileName : fileNames1) {
                log.info(fileName);
            }
            List<String> fileNames = Arrays.asList(
                    "敏感发现结果1.xlsx",
                    "敏感发现结果2.xlsx",
                    "敏感发现结果3.xlsx",
                    "其他结果.xlsx",
                    "敏感发现报告.xlsx"
            );
            // 正则表达式模式，用于匹配文件名中的序号
            Pattern pattern = Pattern.compile("^(.*敏感发现.*)$");
            // 用于存储匹配结果的变量
            String fileName = null;
            // 遍历文件名列表，查找指定序号的文件名
            for (String name : fileNames) {
                Matcher matcher = pattern.matcher(name);
                if (matcher.find()) {
                    fileName = name;
                }
            }
            log.info(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
