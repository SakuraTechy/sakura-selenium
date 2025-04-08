package com.sakura.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import org.apache.log4j.Logger;

@SuppressWarnings({ "unused","unchecked", "rawtypes" })
public class StringUtil {

	static Logger log = Logger.getLogger(StringUtil.class);

	public static boolean isEmpty(String str) {
        return null == str || str.isEmpty();
    }

	public static boolean isNoEmpty(String str) {
        return null != str && !str.isEmpty();
    }

	public static boolean isNoEmpty1(String str) {
		if ("".equals(str)) {
			return false;
		} else return null != str;
    }

	public static boolean isEqual(String str1, String str2) {
        return Objects.equals(str1, str2) || str1.equals(str2);
    }

	public static boolean isNotEqual(String str1, String str2) {
        return !Objects.equals(str1, str2) && !str1.equals(str2);
    }

	public static boolean isBlank(String string) {
		if (string == null || string.isEmpty())
			return true;

		int l = string.length();
		for (int i = 0; i < l; i++) {
			if (!StringUtil.isWhitespace(string.codePointAt(i)))
				return false;
		}
		return true;
	}

	public static boolean isWhitespace(int c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
	}

	/**
	 * String结果对比，得到OK，NG
	 * 
	 * @param expectedResult
	 * @param actualResult
	 * @return
	 */
	public static String assertResult(String expectedResult, String actualResult) {
		String result;
		if (expectedResult.equals(actualResult))
			result = "OK";
		else
			result = "NG";
		return result;
	}

	/**
	 * 获取.后的内容
	 * 
	 * @param str
	 * @return
	 */
	public static String getSubString(String str) {
		int one = str.lastIndexOf(".");
		String Suffix = str.substring((one + 1), str.length());
//        log.info(Suffix);
		return Suffix;
	}

	/**
	 * 使用正则表达式去掉多余的.与0 100.00 => 100
	 * 
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			// 去掉多余的 0
			s = s.replaceAll("0+?$", "");
			// 如果最后一位是.则去掉
			s = s.replaceAll("[.]$", "");
		}
		return s;
	}

	/**
	 * 格式化字符串，去除小数部分为 .0 的情况
	 *
	 * @param str 输入字符串，例如 "11.0G" 或 "11.3G"
	 * @return 格式化后的字符串，例如 "11G" 或 "11.3G"
	 */
	public static String formatString(String str) {
		// 正则表达式匹配数值和单位
		Pattern pattern = Pattern.compile("(\\d+)\\.0([a-zA-Z]+)");
		Matcher matcher = pattern.matcher(str);

		// 如果匹配成功，则替换 .0 为 ""
		if (matcher.find()) {
			return matcher.replaceFirst("$1$2");
		}

		// 如果没有匹配成功，则返回原字符串
		return str;
	}

	/**
	 * 获取str最后一位 case1 => 1
	 * 
	 * @param str
	 * @return
	 */
	public static String subLastOne(String str) {
//        String str = "case1";
//        int key = Integer.parseInt(str.substring(str.length()-1,str.length()));
//        log.info(key);
		return str.substring(str.length() - 1, str.length());
	}

	public static String getRegex(String smsBody,String reg) {
		StringBuffer code = new StringBuffer();
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(smsBody);
		while(matcher.find()){
            String group = matcher.group();
            code.append(group);
        }
//		if (matcher.find()) {
//			return matcher.group();
////			return matcher.replaceAll("").trim();
//		}
		return code.toString();
	}
	
	public static List getRegexs(String smsBody,String reg) {
		List list = new ArrayList();
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(smsBody);
		while (matcher.find()) {
			list.add(matcher.group(1));
        }
		return list;
	}
	
	//字符串截取
    public static String regCompFront(String str,String value) {
    	// 替换中文
        String re = "[\\u4e00-\\u9fa5]+";
        //截取波|第前面数字
        String reg = "(.*?["+value+"|测试])";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        if (m.find()) {
        	return m.group().replaceAll(value, "");
        }
        return null;
    }
    
    //字符串截取
    public static String regCompBack(String str,String value) {
    	// 替换中文
        String re = "[\\u4e00-\\u9fa5]+";
        //截取波|第后面内容
        String reg = ".*?(["+value+"|测试]).*?";
        return  str.replaceFirst(reg, "$1").replaceAll(value, "");
    }
    
    public static String regComp(String str) {
        String num = "";
        // 替换中文
        String reg = "[\\u4e00-\\u9fa5]+";
        //截取λ|入后面数字
        String comp1 = "[\\s\\S]*([λ|入]\\d*)[\\s\\S]*";
        //截取波|第前面数字
        String comp2 = "(\\d+[波|第])";
        if (str.matches(comp1)) {
            num = str.replaceFirst(comp1, "$1").replaceAll(reg, "").replace("λ", "");
        } else {
            Pattern p = Pattern.compile(comp2);
            Matcher m = p.matcher(str);
            if (m.find()) {
                num = m.group(1).replaceAll(reg, "");
            }
        }
        String str1 = "石家庄(至郑州)架1-2-23-OTU3S-1(OTU3S 1波).OCH)";
        String str3 = " 北京东四1-1-4D-OTU3S-1(OTU3S 100第三个).OCH";
        String str2 = " 北京东四1-1-4D-OTU3S-1(OTU3S 入12).OCH";
        String str4 = " 北京东四1-1-4D-OTU3S-1(OTU3S λ12334).OCH";
        return num;
    }

	private void getPhoneNum(String smsBody) {
		Pattern pattern = Pattern.compile("(13|14|15|18)\\d{9}");
		Matcher matcher = pattern.matcher(smsBody);
		while (matcher.find()) {
			log.info(matcher.group());
		}
	}

	private static String getNumber(String smsBody,String reg) {
		Pattern pattern = Pattern.compile("\\d{1}");
		Matcher matcher = pattern.matcher(smsBody);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}
	
	//去除所有空格
    public static String replaceAllBlank(String str) {
        String s = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            /*\n 回车(\u000a)
            \t 水平制表符(\u0009)
            \s 空格(\u0008)
            \r 换行(\u000d)*/
            Matcher m = p.matcher(str);
            s = m.replaceAll("");
        }
        return s;
    }
    
    public static String replaceAllBlank(String str,String reg,String rep) {
        String s = "";
        if (str!=null) {
            Pattern p = Pattern.compile(reg);
            /*\n 回车(\u000a)
            \t 水平制表符(\u0009)
            \s 空格(\u0008)
            \r 换行(\u000d)*/
            Matcher m = p.matcher(str);
            s = m.replaceAll(rep);
        }
        return s;
    }
    
    //去除所有空格，留下一个
    public static String replaceBlankLeaveOne(String str) {
        String s = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s{2,}|\t|\r|\n");
            Matcher m = p.matcher(str);
            s = m.replaceAll(" ");
        }
        return s;
    }

    // 根据计算公式计算，例如1+1+1
    public static String ScriptEngine(String script) {
    	String str = "";
    	try {
//          StringBuilder str = new StringBuilder();
//          str.append("(");
//          str.append("1.53");
//          str.append("+2.53");
//          str.append(")");
//          str.append("*3.3");
//          log.info(str.toString());
//          ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
//          log.info(jse.eval(str.toString()));
			
          ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
          str = jse.eval(SeleniumUtil.parseStringHasEls(script)).toString();
      }
      catch (Exception e){
          e.printStackTrace();
      }
    	return str;
    }



	/**
	 * 计算并格式化输入字符串中的数值
	 *
	 * @param str 输入字符串，可以是带单位的（如 "3KB+4KB"）或不带单位的（如 "3+4"）
	 * @return 计算结果，带单位或不带单位的字符串
	 */
	public static String calculateAndFormat(String str) throws Exception {
		str = SeleniumUtil.parseStringHasEls(str);
		// 创建 ScriptEngineManager 和 ScriptEngine 对象
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		// 正则表达式匹配数值和单位
		Pattern pattern = Pattern.compile("(\\d+)([a-zA-Z]+)?");
		Matcher matcher = pattern.matcher(str);

		StringBuilder expressionBuilder = new StringBuilder();
		String lastUnit = null;
		int lastMatchEnd = 0;

		while (matcher.find()) {
			String value = matcher.group(1);
			String unit = matcher.group(2);

			// 添加操作符和前一部分
			if (lastMatchEnd < matcher.start()) {
				expressionBuilder.append(str, lastMatchEnd, matcher.start());
			}

			if (unit != null) {
				if (lastUnit == null) {
					lastUnit = unit;
				} else if (!lastUnit.equals(unit)) {
					throw new IllegalArgumentException("表达式中的所有单位必须相同。");
				}
			}

			expressionBuilder.append(value);
			lastMatchEnd = matcher.end();
		}

		// 添加剩余部分
		if (lastMatchEnd < str.length()) {
			expressionBuilder.append(str.substring(lastMatchEnd));
		}

		String expression = expressionBuilder.toString();

		// 使用 ScriptEngine 计算表达式
//			Object result = engine.eval(expression);

		System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
		try (Context context = Context.create()) {
			Value result = context.eval("js", expression);
			return lastUnit != null ? result.toString() + lastUnit : result.toString();
		}catch (Exception e) {
			throw new Exception("表达式计算失败：" + e.getMessage());
		}
	}

	/**
	 * 格式化文件大小，将给定的大小以易读的格式返回
	 *
	 * @param kb 文件大小的字符串表示，假设为千字节（KB）
	 * @return 格式化后的大小字符串，例如 "1.5 MB"
	 */
	public static String CustomRules(String kb, int scale, boolean keepTrailingZeros, boolean useGrouping, boolean isUnit) {
		// 将输入的字符串转换为double类型，以便进行数学计算
		double sizeInKb = Double.parseDouble(kb);
		// 定义存储单位数组，从KB开始到TB
		String[] units = { "K", "M", "G", "T" };
		// 初始化单位索引为0，即默认单位为KB
		int unitIndex = 0;

		// 当大小大于等于1024且单位索引未达到数组的最后一个元素时，执行循环
		while (sizeInKb >= 1024 && unitIndex < units.length - 1) {
			// 将大小除以1024，转换到下一个更大的单位
			sizeInKb /= 1024;
			// 更新单位索引，移动到下一个单位
			unitIndex++;
		}

		// 使用自定义的roundNumber方法四舍五入大小数值，并附加相应的单位，返回最终的格式化大小字符串
		String unit = units[unitIndex];
//		sizeInKb = Objects.equals(unit, "G") ? sizeInKb+0.1: sizeInKb;
//		sizeInKb = sizeInKb+0.1;
//		return isUnit ? formatSize(String.valueOf(sizeInKb)) + unit : formatSize(String.valueOf(sizeInKb));
		return isUnit ? formatNumber(sizeInKb, scale, keepTrailingZeros, useGrouping) + unit : formatNumber(sizeInKb, scale, keepTrailingZeros, useGrouping);
	}

	/**
	 * 格式化字符串，根据小数点后第一位的值决定是否取整或保留一位小数
	 *
	 * @param str 输入字符串，例如 "11.0333G" 或 "11.2333G"
	 * @return 格式化后的字符串，例如 "11G" 或 "11.2G"
	 */
	public static String formatSize(String str) {
		// 正则表达式匹配数值和单位
		Pattern pattern = Pattern.compile("(\\d+)\\.(\\d)(\\d+)?([a-zA-Z]+)?");
		Matcher matcher = pattern.matcher(str);

		if (matcher.find()) {
			String integerPart = matcher.group(1);
			String firstDecimal = matcher.group(2);
			String remainingDecimals = matcher.group(3);
			String unit = matcher.group(4);

			if (firstDecimal.equals("0")) {
				// 小数点后第一位是 0，取整
				return integerPart + (unit != null ? unit : "");
			} else {
				// 小数点后第一位大于 0，保留一位小数
				return integerPart + "." + firstDecimal + (unit != null ? unit : "");
			}
		}

		// 如果没有匹配成功，则返回原字符串
		return str;
	}

	//根据小数点后第一位=0，则取整，大于0，则保留一位小数，例如11.0333=11，11.2333=11.2
	public static String formatSize1(String str) {
//        String str = "11.0333";
		String result = "";

		// 创建正则表达式
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
		// 使用正则表达式匹配字符串
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			// 匹配成功，获取小数点后的位数
			int decimals = 0;
			if (str.contains(".")) {
				decimals = str.length() - str.indexOf(".") - 1;
			}
			// 判断小数位的第一位
			if (decimals > 0) {
				String firstDecimal = str.substring(str.indexOf(".") + 1, str.indexOf(".") + 2);
				if (Integer.parseInt(firstDecimal) > 0) {
					// 保留一位小数
					str = str.substring(0, str.indexOf(".") + 2);
//                    log.info(str);
				} else {
					// 取整
					str = str.substring(0, str.indexOf("."));
//                    log.info(str);
				}
			} else {
				// 字符串没有小数部分，直接取整
				log.info(str);
			}
		} else {
			// 字符串不是合法的数字格式
			log.info("不是合法的数字格式");
		}
		return str;
	}

	// 检查数字是否为整数，如果是则返回整数格式。否则，使用 Math.round 方法进行四舍五入，并保留一位小数。
	// 例如：123.4567 -> 123.5
	public static String roundNumber(double number) {
//		if (number == Math.floor(number)) {
//			return String.format("%.0f", number);
//		} else {
//			double rounded = Math.round(number * 100) / 100.0;
//			return String.format("%.1f", rounded);
//		}
		// 四舍五入到一位小数
		double rounded = Math.round(number * 10) / 10.0;
		// 如果是整数值，返回整数格式
		if (rounded == Math.floor(rounded)) {
			return String.format("%.0f", rounded);
		}
		return String.format("%.1f", rounded);
	}

	/**
	 * 格式化数字的增强版本
	 * @param number 需要格式化的数字
	 * @param scale 保留的小数位数
	 * @param keepTrailingZeros 是否保留末尾的0
	 * @param useGrouping 是否使用千分位分隔符
	 * @return 格式化后的字符串
	 */
	public static String formatNumber(double number, int scale, boolean keepTrailingZeros, boolean useGrouping) {
		// 处理特殊值
		if (Double.isNaN(number)) return "NaN";
		if (Double.isInfinite(number)) return number > 0 ? "∞" : "-∞";

		// 创建数字格式化器
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(scale);
		formatter.setMinimumFractionDigits(keepTrailingZeros ? scale : 0);
		formatter.setGroupingUsed(useGrouping);

		String result = formatter.format(number);
		// 如果不需要保留末尾的0，使用BigDecimal处理
		if (!keepTrailingZeros && result.contains(".")) {
			return BigDecimal.valueOf(number)
					.setScale(scale, RoundingMode.HALF_UP)
					.stripTrailingZeros()
					.toPlainString();
		}
		return result;
	}

	public static String formatNumber1(double number, int scale, boolean keepTrailingZeros, boolean useGrouping) {
		// 处理特殊值
		if (Double.isNaN(number)) return "NaN";
		if (Double.isInfinite(number)) return number > 0 ? "∞" : "-∞";

		// 使用 BigDecimal 处理精度问题，使用 String 构造器避免双精度浮点数的精度损失
		BigDecimal bd = new BigDecimal(String.valueOf(number));

		// 使用 HALF_UP 模式进行四舍五入
		bd = bd.setScale(scale, RoundingMode.HALF_UP);

		// 如果不需要保留末尾的0，且结果是整数
		if (!keepTrailingZeros && bd.stripTrailingZeros().scale() <= 0) {
			return bd.toBigInteger().toString();
		}

		// 如果需要使用千分位分隔符
		if (useGrouping) {
			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMaximumFractionDigits(scale);
			formatter.setMinimumFractionDigits(keepTrailingZeros ? scale : 0);
			formatter.setGroupingUsed(true);
			return formatter.format(bd.doubleValue());
		}

		// 不需要保留末尾的0时，去除尾部零
		if (!keepTrailingZeros) {
			return bd.stripTrailingZeros().toPlainString();
		}

		return bd.toPlainString();
	}

    /**String 四舍五入 */
    public static String formatDouble(double d,String format) {
//        DecimalFormat df = new DecimalFormat("#.00");
    	DecimalFormat df = new DecimalFormat(format);
        return df.format(d);
    }
    
    public static String formatDouble1(double d,String format) {
    	double pi=3.1415927;//圆周率

    	//取一位整数

    	log.info(new DecimalFormat("0").format(pi));//3

    	//取一位整数和两位小数  

    	log.info(new DecimalFormat("0.00").format(pi));//3.14

    	//取两位整数和三位小数，整数不足部分以0填补。  

    	log.info(new DecimalFormat("00.000").format(pi));//03.142  

    	//取所有整数部分  

    	log.info(new DecimalFormat("#").format(pi));//3  

    	//以百分比方式计数，并取两位小数  

    	log.info(new DecimalFormat("#.##%").format(pi));//314.16%  

    	long c=299792458;//光速  

    	//显示为科学计数法，并取五位小数  

    	log.info(new DecimalFormat("#.#####E0").format(c));//2.99792E8  

    	//显示为两位整数的科学计数法，并取四位小数  

    	log.info(new DecimalFormat("00.####E0").format(c));//29.9792E7  

    	//每三位以逗号进行分隔。  

    	log.info(new DecimalFormat(",###").format(c));//299,792,458  

    	//将格式嵌入文本  

    	log.info(new DecimalFormat("光速大小为每秒,###米").format(c)); //光速大小为每秒299,792,458米
      return new DecimalFormat(format).format(d);
  }
    
    /*

    * 字符不包含特定字符串的表达式

    */

    public static boolean StringMatchRule(String souce, String regex) {

    boolean result = false;

    if (regex != null && souce != null) {

    result = Pattern.matches(regex, souce);

    }

    return result;

    }
    
	public static void main1(String[] arg) throws Exception {
//        log.info(isEqual("f",""));
		log.info(getRegex("共 402706 条","(?<=共)(.*?)(?=条)"));
		log.info(getRegex("用户名：test；初始密码：An3{wurz","(?<=初始密码：)[^；]*"));
		log.info(getRegex("[402706]","(?<=\\[)(.*?)(?=])"));
		log.info(getRegex("[402706]","\\[(.*?)]"));
		if(getRegex("共 402706 条","(?<=共\\s)(.*?)(?=\\s条)").equals("402706")) {
			log.info("11");
		}
		log.info(getRegex("报告编号： 172725668504078146","报告编号：\\s*(\\d+)"));
		log.info(getRegex("2022-08-24 18:40:49","(\\d{4})-(\\d{1,2})-(\\d{1,2})").replace("-0", "-"));
		log.info(getRegex("2022-08-24 18:40:49","([0-9]{4})-(\\d{1,2})-(\\d{1,2})").replace("-0", "-"));
		log.info(getRegex("测试用户对试用户对试用户","(^.*?[用])").replace("用", ""));
		log.info(getRegex("系统正在升级,请勿断电或强制退出，300s后重新登录","(^.*?[，])").replace("，", ""));
		log.info(getRegex("产测试用户对试用户对试用户","([试].*?)$"));
		log.info(getRegex("截至今日: 2022-09-13 16:12:59。产生风险:高风险0个 中风险0个 低风险0个","([。].*?)$").replace("。", ""));
		
		log.info(regCompFront("石家庄(至郑州)架1-2-23-OTU3S-1(OTU3S 1波2测试).OCH)","波"));
		log.info(regCompFront("北京东四1-1-4D-OTU3S-1(OTU3S λ12334).OC","λ"));
		log.info(regCompFront("保护对象名称${Local_IP}s","1"));
		log.info(regCompBack("保护对象名称${Local_IP}s","保护对象名称"));
		log.info(replaceAllBlank("保护对象名称${Local_IP}s","(.*?[称])",""));
		
		log.info(getRegex("用户对orcl数据库[测试]表所有字段进行了查询操作；操作发生在：2022-08-31 17:15:55，使用的电脑IP为：172.18.1.118，电脑名称为：ANKKI014","\\[(.*?)]"));
		log.info(getRegex("本地IP用户对orcl数据库[测试]表所有字段进行了查询操作；操作发生在：2022-08-31 17:15:55，使用的电脑IP为：172.18.1.118，电脑名称为：ANKKI014","(^.*?[用])").replace("用", ""));
		log.info(regCompFront("本地IP用户对orcl数据库[测试]表所有字段进行了查询操作；操作发生在：2022-09-01 09:41:37，使用的电脑IP为：172.18.1.118，电脑名称为：ANKKI014","用"));
		
		log.info(getRegex("172.18.1.118 : 26 (>99%)","(?<= : )(.*?)(?= \\(>)"));
		log.info(getRegex("系统平台监控墙432","[\\d]"));
		log.info(getRegex("系统平台监控墙432","[0-9]"));
		log.info(getRegex("系统平台监控墙432","[\u4e00-\u9fa5]"));
		log.info(formatDouble(31/2,"0"));
		
		log.info(getRegex("用户对orcl数据库[ALL_OBJECTS]表所有字段进行了查询操作；操作的条件为：[OBJECT_TYPE]等于[PACKAGE]；且[所有者字段翻译]等于[var_1]；操作发生在：2022-11-10 09:34:51，使用的电脑IP为：172.18.1.26，电脑物理地址（MAC地址）为：C4:FF:1F:F3:9D:D0，电脑名称为：DESKTOP-3TR91IC","(?<=且\\[)(.*?)(?=\\]等)"));
//		String a = "402,706";
//		Map<String,String> replace = new LinkedHashMap<String,String>();
//		replace.put("key", ",");
//		replace.put("value", "");
//		log.info(a.replace(replace.get("key"), replace.get("value")));
		
		String regex_containStr = "^(.*(SaveDefinition)).*$";
		String regex_containStr1 = "^(.*(20117-04-17 00:00:00|20117-04-18 23:59:59)).*$";
		String regex_containStr2 = "^(.*(1</font></b>)).*$";
		String regex_notcontainStr = "^(?!.*(转发)).*$";// 不包含特定字符串的表达式
	    log.info(StringMatchRule("%Studio.ClassMgr SaveDefinition 翭26:RegInterface.WebRegService1:K1:126:RegInterface.WebRegService2:230:2:461:12:6017:%RegisteredObject2:6318:64764,38805.16", regex_containStr));
	    log.info(StringMatchRule("发药时间 Order By 发药时间,<b><font color='red'>科室</font></b>;(2017-04-17 00:00:00|2017-04-18 23:59:59)", regex_containStr1));
	    log.info(StringMatchRule("<b><font color='red'>select</font></b> <b><font color='red'>11111111111</font></b>", regex_containStr2));
	    log.info(StringMatchRule("这个邮件 是转发的！", regex_notcontainStr));
	    
	    log.info(replaceAllBlank("大小1：64              块：8          IO 块：4096","(?i)(?<=大小：)[^块]*",""));
	    log.info(getRegex("大小：64              块：8          IO 块：4096","(\\s)|(?i)(?<=大小：)[^块]*"));
	    log.info("["+replaceAllBlank(getRegex("大小：64              块：8          IO 块：4096","(?i)(?<=大小：)[^块]*"),"\\s|\n","")+"]");
//	    final String regex = "(?i)(?<=isemployee:')[^']*";
//        final String string = "id:'1234',Salary:'200000',Year:'2018',IsEmployee:'Yes'";
        final String regex = "(?i)(?<=大小：)[^']*";
        final String string = "大小：64              块：8          IO 块：4096";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            log.info("Full match: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                log.info("Group " + i + ": " + matcher.group(i));
            }
        }
	}
	
	public static void main(String[] arg) throws Exception {
//		log.info(getRegex("172.19.4.56","\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\."));
//		log.info(getRegexs("172.19.4.123","(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\.\\d{1,3}"));
//		log.info(replaceAllBlank("\n52399108+104806400+","(\n|.$)",""));
//		log.info(CustomRules("11.0333"));
//		String regex_containStr2 = "^(.*(1</font></b>)).*$";
//		log.info(StringMatchRule("<b><font color='red'>select</font></b> <b><font color='red'>11111111111</font></b>", regex_containStr2));
//
//		log.info(getRegex("报告编号： 172725668504078146","[^：]*$"));

//		log.info(CustomRules("10969684","false"));
//		log.info(formatSize("11.011111"));
//		log.info(formatSize("11.311111"));
//		log.info(calculateAndFormat("3+4"));
//		log.info(calculateAndFormat("3T+4T"));
//		log.info(formatDouble(15+1,"#"));
//		log.info(calculateAndFormat("59+(36-31)*1.6+(40+(36-31))*1"));
//		log.info(formatSize(calculateAndFormat("59+(36-31)*1.6+(40+(36-31))*1")));
//		log.info(formatNumber(Double.parseDouble("3285"),0,false,true));
//		log.info(formatNumber(Double.parseDouble(calculateAndFormat("59+(36-31)*1.6+(40+(36-31))*1")),0,true,true));
//		log.info(CustomRules(calculateAndFormat("74944764+508580"),1,false,false, true));
		log.info(replaceAllBlank("大小1：64              块：8          IO 块：4096","(?i)(?<=大小：)[^块]*",""));
		log.info(replaceAllBlank("保护对象名称${Local_IP}s","(.*?[称])",""));
		log.info(replaceAllBlank("3,223","[,]",""));
	}
}
