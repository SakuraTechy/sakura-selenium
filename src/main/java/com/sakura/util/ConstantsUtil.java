package com.sakura.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * Description: 工具类 <br/>
 * Date: 2014年11月20日 下午2:36:04 <br/>
 * 
 * @author 刘智
 * @version
 * @since JDK 1.7
 * @see
 */
public class ConstantsUtil {

    public static final String CONFIG_JDBC = "src/main/java/jdbc.properties";
    
    public static final String CONFIG_COMMON = "src/main/java/common.properties";
    
    /**
     * 检查条件：列表大小不为空.
     */
    public static final String SIZE_NOT_ZERO = "size_not_null";
    
    /**
     * 检查条件：字段值是否等于预期.
     */
    public static final String FIELD = "field";  
	
	// 微软雅黑
	public static Font BASIC_FONT = new Font("微软雅黑", Font.PLAIN, 12);
	public static Font BASIC_FONT2 = new Font("微软雅黑", Font.TYPE1_FONT, 12);
	public static Font BASIC_FONT3 = new Font("微软雅黑", Font.PLAIN, 15);
	public static Font BASIC_FONT4 = new Font("宋体",Font.BOLD,12);
	// 楷体
	public static Font DIALOG_FONT = new Font("楷体", Font.PLAIN, 16);
	
	public static Border GRAY_BORDER = BorderFactory.createLineBorder(Color.GRAY);
	public static Border DARKGRAY_BORDER = BorderFactory.createLineBorder(Color.darkGray);
	public static Border LIGHT_GRAY_BORDER = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
	public static Border BLUE_BORDER = BorderFactory.createLineBorder(Color.BLUE);

}
