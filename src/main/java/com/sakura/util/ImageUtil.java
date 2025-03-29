package com.sakura.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ImageUtil {
	static Logger log = Logger.getLogger(ImageUtil.class);

	public static void main(String[] args) throws IOException {
		String base64Image = "data:image/jpg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\r\n"
				+ "HBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\r\n"
				+ "MjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAZAF8DASIA\r\n"
				+ "AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\r\n"
				+ "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\r\n"
				+ "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\r\n"
				+ "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\r\n"
				+ "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\r\n"
				+ "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\r\n"
				+ "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\r\n"
				+ "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3u4gi\r\n"
				+ "uraW3mXdFKhR1yRlSMEcVlW+r/ZtGs7vUXRUVRFeXDMFEUwIRsjpt3hgSOBwfu5I1Zp4oPL81tok\r\n"
				+ "cIpIONx6AntnoM9yB1IrlfGSX+k+DtZlsPKmicSM9u8DOQkhAkwQw6FpHJIPXHAFJuyuBJYfEbwz\r\n"
				+ "qepQ2Fpflp5pfKj3RMisduQcsB1PygdSe3etnU9R0y301Z7zVYLK2mKiK5a4WMFvvLtYnB6ZxyCA\r\n"
				+ "cgjNfPviPVLrVdNtZpddudU2xQLKDaeWkL4kOx3/AImHzYODuyxyMc9L4ojt28c+FLXxO8J8LxLc\r\n"
				+ "WsPmfIA8bNH87A56rASTheegAanhl7WTix2PRrXxNFZ6mumXl7HfGeJbmzuIGRpJoW3Efu15fG0/\r\n"
				+ "NGpGME7ea2ba00qdZLyyitSbkMrXNtgM+T83zrznI656ivELabTLb40aNJ4ejs5TBbP9qFkoMczr\r\n"
				+ "HKGaNU+Xc0QUhV4DNg87q7D4hC103xB4c8RaWBJqk1wFEVvnN7H8v8YyOhCdCSHHULita1NU7W6q\r\n"
				+ "5FSXIrne777T+ZTJf2/99Y1E0fclgMBx1+6ARgDDE5EEnivw9DHC8+uadAJoxJGJrlIyy5IzhiD1\r\n"
				+ "BH1BHavPvCVzaWXgDX/FTpHfaxcfaTOz2rMoYKX8tgvGw8MSMD5gCflFUfhp4W8Hah4StrvVTaXO\r\n"
				+ "qag0qzJczKzkFmQKoPKngMGXD5P3scVimVTalDnZ6nqEiPc6VMjq0SXrK7g5Cny5UwT2O8hfqQOt\r\n"
				+ "Zl34clXVkvU8y7gjQiOJr6dJoT3KOXIJYZBB2jheRyTxHw6a4gXxN4YiaWey0fWoBbFyWdU+0kNn\r\n"
				+ "sABFu4A5LHvx6jfW/wDaVkyW17JBIr5jngfO11JGCOjDIIKnjr36A5Rs7GXp2q38igi2urqIgsBN\r\n"
				+ "GkcwAODllPlFt2QVJRlCnIJroa5iDTGudTU3F7cJdR5d45FKlzt8tmVk2I42NgMF3qSuSPuVtQTy\r\n"
				+ "wTLZ3jbnbPkz4AEwAzg44DgdR0IGR3CsQ6/mSGKJZoVkt55BBLu5Ch8qMj+IFiq4/wBrPQGsyCZt\r\n"
				+ "AvF06QZ0wQmSCVmJeFQ2GDEk5RdyDPGAR1AZhd8Qf8i3qn/XnL/6AaNS/wCP/R/+vxv/AERLQBly\r\n"
				+ "uE+1RRBgsOp28kTbSPL8yVRIuTzuJMhP+xKuOGArQfQrO8F3DqVlaXlrJdefDDNEJFQ7FBO1hgEt\r\n"
				+ "vPH94nuao33/ACCvFv8A20/9JY66OhO2wHE3ngu6f4n6H4itDZw6Xp1ibU24yrj5ZQAqhdu0b17j\r\n"
				+ "oa2ZdD0W21+z1Z7N1u0XyIJ/Mfy4ht2qgXdtUEMQOMZ/2iM7tZ3iD/kW9U/685f/AEA1U5uVr9BN\r\n"
				+ "J7mfbeGP7M1i+1HSpLa2kvWLTB45pN5JySR5oXOScYUYyR655H/hWmqaFMLfwv4pvtNsr8slzAsH\r\n"
				+ "mpExRiXQlsoPlRQc7hx85r0+ipsOL5dEcLpXhW0+HjxT6V9qfTfLf+0WlfzGPzLtfaoHIyckDART\r\n"
				+ "3PPRatolreM1w1lDcsQRLC6j94CACVY/dkwBhsjOACeFZZ/EH/It6p/15y/+gGjw/wD8i3pf/XnF\r\n"
				+ "/wCgCgG7vUx4tO014oopHuoVYkWtzbSSQorHK8ouEilBJUgqAzHpklR0tvG0NtFE772RApbnkgde\r\n"
				+ "ST+ZJ9zWBff8grxb/wBtP/SWOujoA//Z";

//		GenerateImage(base64, "/TestData/Png/11.png");
		
		String imageUrl = "https://172.19.5.60:8443/getCheckCode?codeId=c112667c-18c3-4a0e-aa3b-e2e88d8f8734";
		base64Image = imageUrlToBase64(imageUrl);
        System.out.println("Base64 Encoded Image: " + base64Image);
        
        GenerateImage(base64Image, "TestData/Code/11.png");
        
//		String imageUrl = "https://172.19.5.60:8443/getCheckCode?codeId=c112667c-18c3-4a0e-aa3b-e2e88d8f8734";
//        String targetPath = "D:\\Jenkins\\workspace\\Ankki.Web.UI.Automation.Test\\TestData\\Code"; // 替换为实际的保存路径及文件名
//        downloadImage(imageUrl, targetPath);
	}

	// 图片转化成base64字符串
	public static String GetImageStr(String imgFile) {
//        String imgFile = "D:\\tanbing.jpg";// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(System.getProperty("user.dir") + imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		Encoder encoder = Base64.getEncoder();
//		BASE64Encoder encoder = new BASE64Encoder();
		System.out.println(encoder.encode(data));
		// 返回Base64编码过的字节数组字符串
		return encoder.encode(data).toString();
	}

	// 对字节数组字符串进行Base64解码并生成图片
	public static String GenerateImage(String imgStr, String imgFilePath) {
		Decoder decoder = Base64.getDecoder();
//		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// 去掉base64前缀 data:image/jpeg;base64,
			imgStr = imgStr.substring(imgStr.indexOf(",", 1) + 1, imgStr.length());
			// Base64解码
			byte[] b = decoder.decode(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error(e);
		}
		return imgFilePath;
	}

	/**
	 * 图片转base64字符串
	 * 
	 * @param imgFile 图片路径
	 * @return
	 */
	public static String imageToBase64Str(String imgFile) {
		InputStream inputStream = null;
		byte[] data = null;
		try {
			inputStream = new FileInputStream(imgFile);
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 加密
		Encoder encoder = Base64.getEncoder();
//		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data).toString();
	}

	/**
	 * base64编码字符串转换为图片
	 * 
	 * @param imgStr base64编码字符串
	 * @param path   图片路径
	 * @return
	 */
	public static boolean base64StrToImage(String imgStr, String path) {
		if (imgStr == null)
			return false;
		Decoder decoder = Base64.getDecoder();
//		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// 去掉base64前缀 data:image/jpeg;base64,
			imgStr = imgStr.substring(imgStr.indexOf(",", 1) + 1, imgStr.length());
			// 解密
			byte[] b = decoder.decode(imgStr);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			// 文件夹不存在则自动创建
			File tempFile = new File(System.getProperty("user.dir") + path);
			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdirs();
			}
			OutputStream out = new FileOutputStream(tempFile);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	// 忽略SSL证书验证并设置自定义主机名验证逻辑的临时解决方案（仅限测试环境）
    private static void setupInsecureSSL(String host) {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};

        HostnameVerifier hv = (hostname, session) -> hostname.equals(host); // 替换为实际的主机名

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
	  public static String imageUrlToBase64(String imageUrl) throws IOException {
	        URL url = new URL(imageUrl);
	        setupInsecureSSL(url.getHost()); // 只有在你需要临时解决这个问题时调用这个方法
	        
	        try (InputStream in = url.openStream();
	             ReadableByteChannel rbc = Channels.newChannel(in)) {

	            ByteBuffer bb = ByteBuffer.allocate(1024); // 创建一个缓冲区
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();

	            while (rbc.read(bb) != -1) { // 读取数据到缓冲区
	                bb.flip(); // 切换到读模式
	                byte[] buffer = new byte[bb.remaining()];
	                bb.get(buffer); // 将缓冲区中的数据复制到临时数组中
	                baos.write(buffer, 0, buffer.length); // 将临时数组写入ByteArrayOutputStream

	                bb.clear(); // 清空缓冲区为下一次读取做准备
	            }

	            byte[] imageBytes = baos.toByteArray();
	            
	            return Base64.getEncoder().encodeToString(imageBytes);
	        }
	    }
}
