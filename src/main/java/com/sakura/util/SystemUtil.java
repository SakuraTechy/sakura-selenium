package com.sakura.util;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import cn.hutool.core.net.NetUtil;

/**
 * @author 刘智King
 * @date 2021年1月13日 上午10:07:17
 */
public class SystemUtil {
	public static void main(String[] args) throws UnknownHostException, SocketException {
        String str = getLocalIp4Address().get().getHostAddress();
		String str1 = getLocalHost("主机IP","");
		System.out.println(str);
		System.out.println(str1);
		System.out.println(getCurrentIpLocalMac());
        System.out.println(getAllLocalMac());
        
        InetAddress inetAddress = InetAddress.getLocalHost();
        String localMacAddress2 = NetUtil.getMacAddress(inetAddress);
        System.out.println("localMacAddress2 = " + localMacAddress2);
	}

	/**
	 * 获取系统分辨率
	 * 
	 * @date 2021年1月13日 上午10:07:17
	 */
	public static void getScreen() {
		int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		System.out.println(screenWidth);
		System.out.println(screenHeight);
	}

	/**
	 * 获取主机名称和系统
	 * 
	 * @date 2021年1月13日 上午10:07:17
	 */
	public static String getLocalHost(String option,String value) {
		String str = "";
		try {
			switch (option) {
			case "主机IP":
				if(StringUtil.isEqual(ConfigUtil.getProperty("Environment_Type", Constants.CONFIG_APP), "Local")) {
					str="172.19.5.232";
				}else {
//					str = InetAddress.getLocalHost().getHostAddress();
					str = getLocalIp4Address().get().getHostAddress();
				}
				break;
			case "主机名称":
				InetAddress ip = InetAddress.getLocalHost();
				str = ip.getHostName();
				break;
			case "主机系统":
				str = System.getProperty("os.name");
				break;
			case "系统版本":
				str = System.getProperty("os.version");
				break;
			case "系统架构":
				str = System.getProperty("os.arch");
				break;
			case "系统日期":
				str = DateUtil.getDateFormat(value);
				break;
			case "当前用户":
				str = System.getProperty("user.name");
				break;
			case "用户的主目录":
				str = System.getProperty("user.home");
				break;
			case "用户的当前工作目录":
				str = System.getProperty("user.dir");
				break;
			}
//			System.out.println(option+"："+str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 获取局域网IP地址
	 * 
	 * @throws UnknownHostException
	 * @date 2021年1月13日 上午10:07:17
	 */
	public static String getIpAddress() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}

	/**
	 * 获取本机所有网卡信息 得到所有IP信息
	 * 
	 * @return Inet4Address>
	 */
	public static List<Inet4Address> getLocalIp4AddressFromNetworkInterface() throws SocketException {
		List<Inet4Address> addresses = new ArrayList<>(1);
		// 所有网络接口信息
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		if (Objects.isNull(networkInterfaces)) {
			return addresses;
		}
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();
			// 滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
			if (!isValidInterface(networkInterface)) {
				continue;
			}
			// 所有网络接口的IP地址信息
			Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				InetAddress inetAddress = inetAddresses.nextElement();
				// 判断是否是IPv4，并且内网地址并过滤回环地址.
				if (isValidAddress(inetAddress)) {
					addresses.add((Inet4Address) inetAddress);
				}
			}
		}
		return addresses;
	}

	/**
	 * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
	 *
	 * @param ni 网卡
	 * @return 如果满足要求则true，否则false
	 */
	private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
		return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
				&& (ni.getName().startsWith("eth") || ni.getName().startsWith("ens"));
	}

	/**
	 * 判断是否是IPv4，并且内网地址并过滤回环地址.
	 */
	private static boolean isValidAddress(InetAddress address) {
		return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
	}

	/**
	 * 通过Socket 唯一确定一个IP 当有多个网卡的时候，使用这种方式一般都可以得到想要的IP。甚至不要求外网地址8.8.8.8是可连通的
	 * 
	 * @return Inet4Address>
	 */
	private static Optional<Inet4Address> getIpBySocket() throws SocketException {
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			if (socket.getLocalAddress() instanceof Inet4Address) {
				return Optional.of((Inet4Address) socket.getLocalAddress());
			}
		} catch (UnknownHostException networkInterfaces) {
			throw new RuntimeException(networkInterfaces);
		}
		return Optional.empty();
	}

	/**
	 * 获取本地IPv4地址
	 * 
	 * @return Inet4Address>
	 */
	public static Optional<Inet4Address> getLocalIp4Address() throws SocketException {
		final List<Inet4Address> inet4Addresses = getLocalIp4AddressFromNetworkInterface();
		if (inet4Addresses.size() != 1) {
			final Optional<Inet4Address> ipBySocketOpt = getIpBySocket();
			if (ipBySocketOpt.isPresent()) {
				return ipBySocketOpt;
			} else {
				return inet4Addresses.isEmpty() ? Optional.empty() : Optional.of(inet4Addresses.get(0));
			}
		}
		return Optional.of(inet4Addresses.get(0));
	}
	
	/**
     * 获取当前所用ip的mac地址
     * @return
     */
    public static String getCurrentIpLocalMac(){
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            System.out.println(ia);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
 
        byte[] mac = new byte[0];
        try {
            // NetworkInterface.getByInetAddress(ia) 根据ip信息获取网卡信息
            mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }
 
        StringBuffer sb = new StringBuffer("");
        for(int i=0; i<mac.length; i++) {
            if(i!=0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i]&0xff;
            // 把无符号整数参数所表示的值转换成以十六进制表示的字符串
            String str = Integer.toHexString(temp);
            if(str.length()==1) {
                sb.append("0"+str);
            }else {
                sb.append(str);
            }
        }
 
        return sb.toString();
    }
 
    /**
     * 获取本地所有mac文件
     * @return
     */
    public static List<String> getAllLocalMac(){
        // 使用set集合，避免重复
        Set<String> macs = new HashSet<>();
 
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                StringBuffer stringBuffer = new StringBuffer();
                NetworkInterface networkInterface = enumeration.nextElement();
                if (networkInterface != null) {
                    byte[] bytes = networkInterface.getHardwareAddress();
                    if (bytes != null) {
                        for (int i = 0; i < bytes.length; i++) {
                            if (i != 0) {
                                stringBuffer.append("-");
                            }
                            // 字节转换为整数
                            int tmp = bytes[i] & 0xff;
                            // 把无符号整数参数所表示的值转换成以十六进制表示的字符串
                            String str = Integer.toHexString(tmp);
                            if (str.length() == 1) {
                                stringBuffer.append("0" + str);
                            } else {
                                stringBuffer.append(str);
                            }
                        }
                        String mac = stringBuffer.toString();
                        macs.add(mac);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Set 转 List
        List<String> macList = new ArrayList<>(macs);
        return macList;
    }
}