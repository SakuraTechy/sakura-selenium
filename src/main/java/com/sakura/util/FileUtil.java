package com.sakura.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.List.*;

/**
 * 文件操作类
 * 
 * @author Administrator
 *
 */
public class FileUtil {
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 单例模式
	 *//*
		 * private static FileUtil instance = null; private FileUtil() { } public static
		 * FileUtil getInstance() { synchronized (FileUtil.class) { if(null == instance)
		 * { instance = new FileUtil(); } } return instance; }
		 */
	/**
	 * 创建文件/文件夹
	 * 
	 * @param path 路径
	 * @return 是否创建成功
	 * @throws Exception
	 */
	public static boolean creatFile(String path) throws Exception {
		if (null == path || path.length() == 0) {
			throw new Exception("路径不正确！");
		}
		File file = new File(path);
		// 如果路径存在，则不创建
		if (!file.exists()) {
			if (file.isDirectory()) {
				// 文件夹
				file.mkdirs();
				// file.mkdir(); 创建单层路径 file.mkdirs() 可以创建多层路径
				return true;
			} else {
				// 文件 先创建父路径，然后再创建文件
				String dirPath = path.substring(0, path.lastIndexOf(File.separator));
				File dirFile = new File(dirPath);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				File fileFile = new File(path);
				try {
					fileFile.createNewFile();
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			throw new Exception("文件已存在！");
		}
		return false;
	}

	/**
	 * 如果目录不存在就创建目录
	 */
	public static void mkdirs(String path) {
		File directories = new File(path);
		if (!directories.exists()) {
			if (directories.mkdirs()) {
				log.info("目录不存在，创建多级目录成功:"+path);
			} else {
				log.info("目录不存在，创建多级目录失败:"+path);
			}
		}
	}

	/**
	 * 删除文件/文件夹及其中的所有子文件夹和文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void deleteFile(String filePath) throws Exception {
		if (null == filePath || filePath.length() == 0) {
			throw new Exception("filePath:" + filePath + "路径不正确！");
		}
		File file = new File(filePath);
		if (!file.exists()) {
			throw new Exception("filePath:" + filePath + "文件不存在！");
		}
		if (file.isFile()) {
			file.delete();
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (null != childFiles && childFiles.length != 0) {
				// 循环递归删除
				for (File childFile : childFiles) {
					deleteFile(childFile.getAbsolutePath());
				}
			}
			file.delete();
		}
		log.info("删除：【"+filePath+"】成功");
	}

	/**
	 * 删除path下所有文件，包括文件夹
	 * @param path
	 * @param isIncludeRoot 是否要删除path(如果是文件夹)
	 * @return true删除成功
	 */
	public static boolean deleteAllFile(String path, String... isIncludeRoot) {
	    File file = new File(path);
	    if (!file.exists()) {
	        return false;
	    }
	    if(file.isFile()) {
	        return file.delete();
	    }
	    File[] fileList = file.listFiles();
	    boolean res = true;
	    for (File f : fileList) {
	        if(f.isFile()) {
	            res = res && f.delete();
	        } else if(f.isDirectory()) {
	            res = res && deleteAllFile(f.getAbsolutePath(), "true");
	        }
	    }
	    if(isIncludeRoot.equals("true")) {
	        res = res && file.delete();
	    }
	    return res;
	}

	/**
	 * 查看文件夹及其中的所有子文件夹和文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getFileList(String filePath) throws FileNotFoundException {
		if (filePath == null || filePath.isEmpty()) {
			throw new IllegalArgumentException("filePath: " + filePath + " 路径不正确！");
		}

		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("filePath: " + filePath + " 文件不存在！");
		}

		// 检查路径类型
		if (file.isDirectory()) {
			List<Map<String, Object>> fileList = new LinkedList<>();
			File[] childFiles = file.listFiles();

			if (childFiles != null) {
				for (File childFile : childFiles) {
					Map<String, Object> fileInfo = new LinkedHashMap<>();
					fileInfo.put("文件路径", childFile.getAbsolutePath());
					fileInfo.put("文件名称", childFile.getName());
					fileInfo.put("文件大小", childFile.length());
					fileList.add(fileInfo);
				}
			} else {
				log.warn("文件夹 {} 为空", filePath);
			}
			log.info("文件列表: {}", fileList);
			return fileList;
		} else {
			throw new IllegalArgumentException("filePath: " + filePath + " 必须是一个目录！");
		}
	}
	
	/**
	 * 获取文件基本信息
	 * 
	 * @param file
	 */
	public static void getBaseInfo(File file) {
		// 文件绝对路径 文件大小 文件是否是文件夹 文件是否是文件 文件是否可读 文件是否可写 文件是否可执行 文件修改时间 文件父目录名
		// 文件所在分区总大小 未使用大小 可用大小
		System.out.println("文件基本信息如下：");
		System.out.println("文件绝对路径：" + file.getAbsolutePath());
		System.out.println("文件名称：" + file.getName());
		System.out.println("文件大小:" + file.length());
		System.out.println("文件是否是文件夹：" + file.isDirectory());
		System.out.println("文件是否是文件：" + file.isFile());
		System.out.println("文件是否可读：" + file.canExecute());
		System.out.println("文件是否可读：" + file.canRead());
		System.out.println("文件是否可写：" + file.canWrite());
		System.out.println("文件修改时间：" + file.lastModified());
		System.out.println("文件父目录名称：" + file.getParent());
		System.out.println("文件所在分区大小：" + file.getTotalSpace() / 1024 / 1024 + "Mb");
		System.out.println("文件所在分区未使用大小：" + file.getFreeSpace() / 1024 / 1024 + "Mb");
		System.out.println("文件所在分区可用大小：" + file.getUsableSpace() / 1024 / 1024 + "Mb");
		System.out.println("文件夹结构如下图：");
		try {
			printFileStructure(file, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 打印文件路径
	 * 
	 * @param file
	 * @param deepth
	 * @throws Exception
	 */
	public static void printFileStructure(File file, int deepth) throws Exception {
		if (!file.exists()) {
			throw new Exception("文件路径不存在！");
		}
		if (!file.isHidden()) {
			if (file.isFile()) {
				// 直接打印
				printFile(file, deepth);
				return;
			}
			if (file.isDirectory()) {
				// 先打印自身，然后递归打印子文件夹和子文件
				printFile(file, deepth);
				File[] childFiles = file.listFiles();
				if (null != childFiles && childFiles.length > 0) {
					deepth++;
					for (File childFile : childFiles) {
						printFileStructure(childFile, deepth);
					}
				}
			}
		}

	}

	/**
	 * 打印文件夹树形结构
	 * 
	 * @param file
	 * @param deepth
	 */
	public static void printFile(File file, int deepth) {
		String name = file.getName();
		StringBuffer sb = new StringBuffer();
		StringBuffer tempSb = new StringBuffer();
		for (int i = 0; i < deepth; i++) {
			tempSb.append("   ");
		}
		sb.append(tempSb);
		sb.append("|" + "\n");
		sb.append(tempSb);
		sb.append("------" + name);
		System.out.println(sb.toString());
	}

	/**
	 * 删除特定的文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public static void deleteNamedFile(String filePath, String regex) throws Exception {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new Exception("文件不存在！");
		}
		// 匿名内部类实现 FilenameFilter 接口种的accept()方法，使用在正则表达式进行匹配
		// accept(File dir,String name) 使用当前文件对象 和 当前文件的名称 进行文件是否符合要求的标准判断；
		/*
		 * =============================================================================
		 * ========== File file = new File("."); String [] nameList =
		 * file.list((dir,name) -> name.endsWith(".java") || new
		 * File(name).isDirectory()); for(String name : nameList) {
		 * System.out.println(name); }
		 * =============================================================================
		 * =========== 这里使用Lamda表达式实现FilenameFilter 接口种的accept()方法
		 */
		File[] fileList = file.listFiles(new FilenameFilter() {

			/**
			 * 使用正则表达式进行匹配
			 * 
			 * @param regexStr
			 * @return
			 */
			private boolean regexMatch(String name, String regexStr) {
				Pattern pattern = Pattern.compile(regexStr);
				Matcher matcher = pattern.matcher(name);
				return matcher.find();
			}

			@Override
			public boolean accept(File dir, String name) {
				return regexMatch(name, regex);
			}
		});
		if (null != fileList && fileList.length > 0) {
			for (File filteredFile : fileList) {
				filteredFile.delete();

			}
		}
	}

	/**
	 * 复制文件/文件夹及其中的所有子文件夹和文件
	 * 
	 * @return
	 */
	public static void copyFile(String srcFilePath, String destFilePath) {
		InputStream is = null;
		OutputStream os = null;
		try {
			if (creatFile(destFilePath)) {
				File srcFile = new File(srcFilePath);
				File destFile = new File(destFilePath);
				is = new FileInputStream(srcFile);
				os = new FileOutputStream(destFile);
				byte[] buffer = new byte[2048];
				int temp = 0;
				while ((temp = is.read(buffer)) != -1) {
					os.write(buffer, 0, temp);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// java 7 以后可以不关闭，可以自动关闭
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 复制指定地文件
	 * 
	 * @return
	 */
	/*
	 * public static boolean copyNamedFile() {
	 * 
	 * }
	 *//**
		 * 剪切文件/文件夹及其中的所有子文件夹和文件
		 * 
		 * @return
		 */
	public static void cutFile(String srcFilePath, String destFilePath) {
		// 先复制，再删除
		try {
			copyFile(srcFilePath, destFilePath);
			deleteFile(srcFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 剪切文件/文件夹及其中的所有子文件夹和文件
	 * 
	 * @return
	 */
	/*
	 * public static boolean cutNamedFile() {
	 * 
	 * }
	 *//**
		 * 文件压缩
		 * 
		 * @param destPath
		 * @param fileFormats
		 * @param srcFile
		 * @return
		 */
	/*
	 * public static boolean fileCompress(String destPath,String fileFormats,String
	 * srcFile,String regex) {
	 * 
	 * }
	 *//**
		 * 文件解压缩
		 * 
		 * @param destPath
		 * @param srcFile
		 * @return
		 */
	/*
	 * public static boolean fileDecompress(String destPath,String srcPath) {
	 * 
	 * }
	 *//**
		 * 文件加密
		 * 
		 * @param srcPath
		 * @param destPath
		 * @param encryptKey
		 * @param encryptAlgorithm
		 * @return
		 */

	/*
	 * public static boolean fileEncrypt(String srcPath,String destPath,String
	 * encryptKey,String encryptAlgorithm) {
	 * 
	 * }
	 *//**
		 * 文件解密
		 * 
		 * @param srcPath
		 * @param destPath
		 * @param encryptKey
		 * @param encryptAlgorithm
		 * @return
	 * @throws Exception 
		 *//*
			 * public static boolean fileDecrypt(String srcPath,String destPath,String
			 * encryptKey,String encryptAlgorithm) {
			 * 
			 * }
			 */

	/**
	 * 检查指定路径下的文件是否符合指定的模式。
	 *
	 * @param directoryPath 目录路径
	 * @param params      文件名模式数组（例如 "*.xlsx", "clientInfoFile?.info"）
	 * @return 如果目录下文件匹配任一模式成功，则返回 true；否则返回 false
	 */
	public static boolean checkFilesExistByPatterns(String directoryPath, String params) {
		File directory = new File(directoryPath);

		// 确保目录存在
		if (!directory.exists() || !directory.isDirectory()) {
			return false;
		}

		// 将 params 转换成 String[]
		String[] patterns = params.substring(1, params.length() - 1)
				.replace("\"", "")
				.split(",\\s*");

		// 定义 FilenameFilter，
		FilenameFilter filter = (dir, name) -> {
			for (String pattern : patterns) {
				Pattern p = Pattern.compile("^" + pattern.replace("*", ".*") + "$");
				Matcher m = p.matcher(name);
				if (m.find()) {
					return true;
				}
			}
			return false;
		};

		// 获取目录下所有文件
		String[] files = directory.list(filter);

		// 判断是否有符合条件的文件
		return files != null && files.length > 0;
	}

	/**
	 * 检查指定路径下的文件是否符合指定的模式。
	 *
	 * @param directoryPath 目录路径
	 * @param params      文件名模式（例如 "*.xlsx", "clientInfoFile*.info"）
	 * @return 如果目录下文件匹配所有模式成功，则返回 true；否则返回 false
	 */
	public static boolean checkFilesExistByAllPatterns(String directoryPath, String params) {
		File directory = new File(directoryPath);

		// 确保目录存在
		if (!directory.exists() || !directory.isDirectory()) {
			return false;
		}

		// 获取目录下所有文件
		File[] files = directory.listFiles();

		if (files == null) {
			return false;
		}

		// 将 params 转换成 String[]
		String[] patterns = params.substring(1, params.length() - 1)
				.replace("\"", "")
				.split(",\\s*");

		// 初始化模式匹配结果数组
		boolean[] matches = new boolean[patterns.length];

		// 遍历所有文件
		for (File file : files) {
			String fileName = file.getName();
			for (int i = 0; i < patterns.length; i++) {
				String pattern = patterns[i];
				Pattern p = Pattern.compile("^" + pattern.replace("*", ".*") + "$");
				Matcher m = p.matcher(fileName);
				if (m.find()) {
					matches[i] = true;
				}
			}
		}

		// 检查所有模式是否都至少有一个文件匹配
		for (boolean match : matches) {
			if (!match) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查指定路径的文件名是否符合指定的模式。
	 *
	 * @param filePath 文件路径
	 * @param params 文件名模式数组（例如 "*.info", "clientInfoFile*.info"）
	 * @return 如果文件符合任一模式，则返回 true；否则返回 false
	 */
	public static boolean checkFileNameByPatterns(String filePath, String params) {
		File file = new File(filePath);
		String fileName = file.getName();

		// 将 params 转换成 String[]
		String[] patterns = params.substring(1, params.length() - 1)
				.replace("\"", "")
				.split(",\\s*");

		for (String pattern : patterns) {
			Pattern p = Pattern.compile("^" + pattern.replace("*", ".*") + "$");
			Matcher m = p.matcher(fileName);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查指定路径下的文件是否符合指定的模式。
	 *
	 * @param filePath 目录路径
	 * @param params      文件名模式（例如 "*.info", "clientInfoFile*.info"）
	 * @return 如果文件符合所有模式都匹配成功，则返回 true；否则返回 false
	 */
	public static boolean checkFileNameByAllPatterns(String filePath, String params) {
		File file = new File(filePath);
		String fileName = file.getName();

		// 将 params 转换成 String[]
		String[] patterns = params.substring(1, params.length() - 1)
				.replace("\"", "")
				.split(",\\s*");

		// 初始化模式匹配结果数组
		boolean[] matches = new boolean[patterns.length];

		for (int i = 0; i < patterns.length; i++) {
			String pattern = patterns[i];
			Pattern p = Pattern.compile("^" + pattern.replace("*", ".*") + "$");
			Matcher m = p.matcher(fileName);
			if (m.find()) {
				matches[i] = true;
			}
		}

		// 检查文件是否匹配所有模式
		for (boolean match : matches) {
			if (!match) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查指定路径下的文件是否匹配指定的模式。
	 *
	 * @param path 目录路径或文件路径
	 * @param params 文件名模式（例如 "*.xlsx", "clientInfoFile*.info"）
	 * @return 如果路径下的文件匹配指定模式成功，则返回 true；否则返回 false
	 */
	public static boolean checkPathFileByPatterns1(String path, String params) {
		// 参数验证
		if (path == null || params == null || path.isEmpty() || params.isEmpty()) {
			return false;
		}

		// 将 params 转换成 String[]
		String[] patterns = params.substring(1, params.length() - 1)
				.replace("\"", "")
				.split(",\\s*");

		// 确保目录存在
		File directory = new File(path);
		if (directory.exists() || directory.isDirectory()) {
			if(patterns.length<2){
				// 定义 FilenameFilter，
				FilenameFilter filter = (dir, name) -> {
					for (String pattern : patterns) {
						Pattern p = Pattern.compile("^" + pattern.replace("*", ".*") + "$");
						Matcher m = p.matcher(name);
						if (m.find()) {
							return true;
						}
					}
					return false;
				};
				// 获取目录下所有文件
				String[] files = directory.list(filter);
				// 判断是否有符合条件的文件
				return files != null && files.length > 0;
			}else{
				// 获取目录下所有文件
				File[] files = directory.listFiles();
				if (files == null) {
					return false;
				}
				// 初始化模式匹配结果数组
				boolean[] matches = new boolean[patterns.length];
				// 遍历所有文件
				for (File file : files) {
					String fileName = file.getName();
					for (int i = 0; i < patterns.length; i++) {
						String pattern = patterns[i];
						Pattern p = Pattern.compile("^" + pattern.replace("*", ".*") + "$");
						Matcher m = p.matcher(fileName);
						if (m.find()) {
							matches[i] = true;
						}
					}
				}
				// 检查所有模式是否都至少有一个文件匹配
				for (boolean match : matches) {
					if (!match) {
						return false;
					}
				}
			}
		} else {
			File file = new File(path);
			String fileName = file.getName();
			if(patterns.length<2){
				for (String pattern : patterns) {
					Pattern p = Pattern.compile("^" + pattern.replace("*", ".*") + "$");
					Matcher m = p.matcher(fileName);
					if (m.find()) {
						return true;
					}
				}
				return false;
			} else {
				// 初始化模式匹配结果数组
				boolean[] matches = new boolean[patterns.length];

				for (int i = 0; i < patterns.length; i++) {
					String pattern = patterns[i];
					Pattern p = Pattern.compile("^" + pattern.replace("*", ".*") + "$");
					Matcher m = p.matcher(fileName);
					if (m.find()) {
						matches[i] = true;
					}
				}
				// 检查文件是否匹配所有模式
				for (boolean match : matches) {
					if (!match) {
						return false;
					}
				}
				return true;
			}
		}
		return true;
	}

	/**
	 * 检查指定路径下的文件是否匹配指定的模式。
	 *
	 * @param path 目录路径或文件路径
	 * @param params 文件名模式（例如 "*.xlsx", "clientInfoFile*.info"）
	 * @return 如果路径下的文件匹配指定模式成功，则返回 true；否则返回 false
	 */
	public static boolean checkPathFileByPatterns(String path, String params) {
		log.info("路径: {}", path);
		log.info("参数: {}", params);

		// 参数验证
		if (path == null || params == null || path.isEmpty() || params.isEmpty()) {
			return false;
		}

		// 将 params 转换成 String[]
		String[] patterns = params.substring(1, params.length() - 1)
				.replace("\"", "")
				.split(",\\s*");

		// 提前编译正则表达式
		Pattern[] compiledPatterns = new Pattern[patterns.length];
		for (int i = 0; i < patterns.length; i++) {
			compiledPatterns[i] = Pattern.compile("^" + patterns[i].replace("*", ".*") + "$");
		}

		// 确保目录存在
		File directory = new File(path);
		if (!directory.exists()) {
			return false;
		}

		// 处理文件路径
		if (directory.isFile()) {
			return checkFileMatch(directory.getName(), compiledPatterns);
		}

		// 处理目录路径
		return checkDirectoryMatch(directory, compiledPatterns);
	}

	private static boolean checkFileMatch(String fileName, Pattern[] compiledPatterns) {
		if (compiledPatterns.length < 2) {
			for (Pattern pattern : compiledPatterns) {
				Matcher matcher = pattern.matcher(fileName);
				if (matcher.find()) {
					return true;
				}
			}
			return false;
		} else {
			boolean[] matches = new boolean[compiledPatterns.length];
			for (int i = 0; i < compiledPatterns.length; i++) {
				Matcher matcher = compiledPatterns[i].matcher(fileName);
				matches[i] = matcher.find();
			}
			for (boolean match : matches) {
				if (!match) {
					return false;
				}
			}
			return true;
		}
	}

	private static boolean checkDirectoryMatch(File directory, Pattern[] compiledPatterns) {
		if (compiledPatterns.length < 2) {
			FilenameFilter filter = (dir, name) -> {
				for (Pattern pattern : compiledPatterns) {
					Matcher matcher = pattern.matcher(name);
					if (matcher.find()) {
						return true;
					}
				}
				return false;
			};
			String[] files = directory.list(filter);
			return files != null && files.length > 0;
		} else {
			boolean[] matches = new boolean[compiledPatterns.length];
			String[] files = directory.list();
			if (files == null) {
				return false;
			}
			for (String fileName : files) {
				for (int i = 0; i < compiledPatterns.length; i++) {
					Matcher matcher = compiledPatterns[i].matcher(fileName);
					if (matcher.find()) {
						matches[i] = true;
					}
				}
			}
			for (boolean match : matches) {
				if (!match) {
					return false;
				}
			}
			return true;
		}
	}

	public static void main(String[] args) throws Exception {
//		deleteFile("C:\\Users\\user06\\Downloads\\");
//		deleteFile("C:\\Users\\user06\\Downloads\\配置信息.xlsx");
//		deleteAllFile("C:/Users/user06/Downloads/",false);
//		String fileName1 = getFileList("D:/1").get(0).get("文件名称").toString();
//		log.info(fileName1);
//		File file = new File("D:\\北邮人下载\\书籍\\编译原理");
//		getBaseInfo(file);
//		try {
//			/* deleteNamedFile("D:\\北邮人下载\\书籍",".pdf"); */
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		/*
//		 * cutFile("F:\\抢票软件\\12306Bypass.exe","F:\\抢票软件\\12306Bypass\\12306Bypass.exe")
//		 * ;
//		 */

//		String directoryPath = "D:/1"; // 替换为实际目录路径
//		String[] patterns = {"*.xlsx","clientInfoFile*.info"}; // 文件名模式数组
//		String params = "[\"*.xlsx\", \"clientInfoFile*.info\"]"; // 假设这是传入的参数
//		params = "[*.xlsx, clientInfoFile*.info]"; // 假设这是传入的参数
//
//		boolean exists = checkFilesExistByPatterns(directoryPath, params);
//		log.info("目录下文件是否匹配任一模式: " + exists);
//
//		exists = checkFilesExistByAllPatterns(directoryPath, params);
//		log.info("目录下文件是否匹配所有模式: " + exists);
//
//		String filePath = "D:/1/clientInfoFile47628A57FE84D04A.info"; // 替换为实际文件路径
//		String[] patterns1 = {"*.info", "clientInfoFile*.info"}; // 文件名模式数组
//		String params1 = "[\"*.info\", \"clientInfoFile*.info\"]"; // 假设这是传入的参数
//		params1 = "[*.info, clientInfoFile*.info]"; // 假设这是传入的参数
//
//		exists = checkFileNameByPatterns(filePath, params1);
//		log.info("文件是否匹配任一模式: " + exists);
//
//		exists = checkFileNameByAllPatterns(filePath, params1);
//		log.info("文件是否匹配所有模式: " + exists);

//		String path = "C:/Users/Ankki/Downloads/"; // 替换为实际目录路径
//		String params = "[检索结果*.zip]"; // 假设这是传入的参数
//
//		String path1 = "C:\\Users\\Ankki/Downloads/脱敏任务报告.zip"; // 替换为实际文件路径
//		String params1 = "[*.zip]"; // 假设这是传入的参数
//
//		boolean exists = checkPathFileByPatterns(path, params);
//		log.info("目录路径是否匹配模式: {}", exists);
//
//		exists = checkPathFileByPatterns(path1, params1);
//		log.info("文件路径是否匹配模式: {}", exists);

		String zipFilePath = "D:/脱敏记录1.zip";
		String destDir = "D:/";
		List<String> fileNames1 = ZipUtil.unzip(zipFilePath, destDir);

//		try {
//			// 使用示例：将文件夹压缩成ZIP
//			String sourceFolder = "D:/敏感发现结果.xlsx";
//			String zipFile = "D:/敏感发现结果.zip";
//			ZipUtil.toZip(sourceFolder, zipFile);
//
//			// 使用示例：解压ZIP文件
//			String zipFilePath = "D:/敏感发现结果.zip";
//			String destDir = "D:/";
//			List<String> fileNames1 = ZipUtil.unzip(zipFilePath, destDir);
//
//			// 打印所有文件名
//			for (String fileName : fileNames1) {
//				log.info(fileName);
//			}
//
//			List<String> fileNames = Arrays.asList(
//					"敏感发现结果1.xlsx",
//					"敏感发现结果2.xlsx",
//					"敏感发现结果3.xlsx",
//					"其他结果.xlsx",
//					"敏感发现报告.xlsx"
//			);
//			// 正则表达式模式
//			Pattern pattern = Pattern.compile("^(.*敏感发现.*)$");
//			// 用于存储匹配结果的变量
//			String fileName = null;
//			// 遍历文件名列表，查找指定序号的文件名
//			for (String name : fileNames) {
//				Matcher matcher = pattern.matcher(name);
//				if (matcher.find()) {
//					fileName = name;
//				}
//			}
//			log.info(fileName);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
