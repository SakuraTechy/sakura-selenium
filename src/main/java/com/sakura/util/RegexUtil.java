package com.sakura.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private static final Logger log = LoggerFactory.getLogger(RegexUtil.class);

    /**
     * 检查字符串是否完全匹配正则表达式。
     *
     * @param regex 正则表达式
     * @param input 要检查的字符串
     * @return 如果字符串匹配正则表达式，则返回true，否则返回false
     */
    public static boolean matches(String regex, String input) {
        log.info("要检查的字符串：{}", input);
        log.info("正则表达式：{}", regex);
        if (input == null || regex == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL); // 设置DOTALL标志以匹配换行符
        return pattern.matcher(input).matches();
    }

    /**
     * 查找字符串中是否包含符合指定正则表达式的子串。
     *
     * @param regex 正则表达式字符串
     * @param input 要匹配的字符串
     * @return 如果找到匹配的子串，则返回true；否则返回false。
     */
    public static boolean contains(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    /**
     * 替换字符串中符合指定正则表达式的子串。
     *
     * @param regex 正则表达式字符串
     * @param input 要匹配和替换的字符串
     * @param replacement 替换后的字符串
     * @return 替换后的字符串
     */
    public static String replaceMatches(String regex, String input, String replacement) {
        if (input == null || regex == null || replacement == null) {
            return input;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll(replacement);
    }

    /**
     * 将所有匹配正则表达式的子串替换为null。
     *
     * @param regex 正则表达式
     * @param input 要替换的字符串
     * @return 替换后的字符串，其中匹配项被替换为null
     */
    public static String removeMatches(String regex, String input) {
        return replaceMatches(regex, input, null);
    }

    /**
     * 获取字符串中第一个符合指定正则表达式的子串。
     *
     * @param regex 正则表达式字符串
     * @param input 要匹配的字符串
     * @return 如果找到匹配的子串，则返回该子串；否则返回null。
     */
    public static String findFirst(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 查找字符串中所有匹配正则表达式的子串。
     *
     * @param regex 正则表达式
     * @param input 要搜索的字符串
     * @return 包含所有匹配项的列表
     */
    public static List<String> findAllMatches(String regex, String input) {
        List<String> matches = new ArrayList<>();
        if (input == null || regex == null) {
            return matches;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * 提取第一个匹配组的内容。
     *
     * @param regex 正则表达式字符串，应该包含至少一个括号以定义匹配组
     * @param input 要匹配的字符串
     * @return 如果找到匹配的子串，则返回该组的内容；否则返回null。
     */
    public static String extractGroup(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find() && matcher.groupCount() > 0) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 验证输入字符串是否符合预期的格式。
     *
     * @param regex 正则表达式字符串
     * @param input 要验证的字符串
     * @return 如果字符串符合正则表达式，则返回true；否则抛出IllegalArgumentException。
     */
    public static boolean validate(String regex, String input) {
        if (!matches(regex, input)) {
            throw new IllegalArgumentException("Input does not match the expected format.");
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
//        String regex1 = "(?:call|exec|execute).*\\bJDBC_TEST\\b.*";
//        String regex2 = "\\b(call|exec|execute|begin)\\b\\s*['\"]JDBC_TEST['\"]";
//        String regex3 = "^(\\b(call|exec)\\b\\s*[\"']\\bJDBC_TEST\\b[\"']\\s*;\\s*){2}$";
//        String regex = "^(\\b(call|exec)\\b\\s*[\"']\\bJDBC_TEST\\b[\"']\\s*;\\s*){2}\\b(execute)\\b\\s*JDBC_TEST\\s*;\\s*$";
//
//        String input1 = "call JDBC_TEST; exec JDBC_TEST; execute JDBC_TEST;";
//        String input2 = "call 'JDBC_TEST'; exec \"JDBC_TEST\";";
//        String input3 = "call \"JDBC_TEST\"; exec 'JDBC_TEST';";
//        String input = "call \"JDBC_TEST\"; exec 'JDBC_TEST'; execute JDBC_TEST;";
//
//        System.out.println("Input: " + input);
//
//        // 检查整个字符串是否符合正则表达式
//        System.out.println("Matches1: " + matches(regex1, input1));
//        System.out.println("Matches2: " + matches(regex3, input3));
//        System.out.println("Matches: " + matches(regex, input));
//
//        // 检查字符串中是否包含符合正则表达式的子串
//        System.out.println("Contains: " + contains(regex, input));
//
//        // 替换字符串中符合正则表达式的子串
//        String replacement = "call JDBC_TEST1";
//        String replaced = replaceMatches(regex, input, replacement);
//        System.out.println("Replaced: " + replaced);
//
//        // 获取字符串中第一个符合正则表达式的子串
//        String firstMatch = findFirst(regex, input);
//        System.out.println("First Match: " + firstMatch);
//
//        // 获取所有符合正则表达式的子串
//        List<String> allMatches = findAllMatches(regex, input);
//        System.out.println("All Matches: " + allMatches);
//
//        // 提取第一个匹配组的内容
//        String extractedGroup = extractGroup("(call|exec|execute|begin)\\s+JDBC_TEST", input);
//        String extractedGroup1 = extractGroup("(call|exec|execute|begin)\\s*['\"]JDBC_TEST['\"]", input);
//        System.out.println("Extracted Group: " + extractedGroup);
//        System.out.println("Extracted Group: " + extractedGroup1);
//
//        // 验证输入是否符合预期格式
//        try {
//            validate(regex, input);
//            System.out.println("Validation passed.");
//        } catch (IllegalArgumentException e) {
//            System.err.println(e.getMessage());
//        }
//
//        // 将转义的换行符替换为实际的换行符
//        input = "[call\n" +
//                "exec\n" +
//                "execute\n" +
//                "begin]";
//        regex = "\\b(call|exec|execute|begin)\\b";
//        System.out.println("Matches: " + matches(regex, input));
//        regex = "^\\[call\\s+exec\\s+execute\\s+begin\\]$";
//        System.out.println("Matches: " + matches(regex, input));
//        System.out.println("Contains: " + contains(regex, input));
//
//        input = input.replace("\\n", "\n"); // 这一步是关键
//
//        // 正则表达式匹配每一行的开始和结束
//        regex = "^(call|exec|execute|begin)$";
//
//        boolean allLinesMatch = true;
//        // 由于我们已经替换了换行符，现在可以正确地分割字符串
//        String[] lines = input.split("\\n");
//
//        for (String line : lines) {
//            // 去除行首尾的空白字符和方括号
//            line = line.trim().replaceAll("[\\[\\]]", "");
//            // 检查每一行是否与正则表达式完全匹配
//            if (!Pattern.matches(regex, line)) {
//                allLinesMatch = false;
//                break;
//            }
//        }
//        System.out.println("All lines match: " + allLinesMatch);
//
//        String regex_containStr1 = "^(.*SaveDefinition.*)$";
//        String input_containStr1 = "%Studio.ClassMgr SaveDefinition 翭26:RegInterface.WebRegService1:K1:126:RegInterface.WebRegService2:230:2:461:12:6017:%RegisteredObject2:6318:64764,38805.16";
//        System.out.println("Matches: " + contains(regex_containStr1, input_containStr1));
//
//        String regex_containStr2 = "^(.*<b><font color='red'>11111111111</font></b>).*$";
//        String input_containStr2 = "<b><font color='red'>select</font></b> <b><font color='red'>11111111111</font></b>";
//        System.out.println("Matches: " + contains(regex_containStr2, input_containStr2));
//
//
//        String regex_containStr3 = "^(.*最后一次登录成功时间.*)$";
//        String input_containStr3 = "最后一次登录成功时间=2024-09-24 11:05:02;用户是否可登陆=是;用户名=monitoradmin;租户id=0;";
//        System.out.println("Matches: " + contains(regex_containStr3, input_containStr3));

        String regex0 = "^(?=.*3133332E3233362E3231382E3937)(?!^3133332E3233362E3231382E3937$).+$";
        String str0 = "3133332E3233362E3231382E39371";
        Pattern pattern0 = Pattern.compile(regex0);
        Matcher matcher0 = pattern0.matcher(str0);
        System.out.println("Does str1 match? " + matcher0.matches()); // Should be false

        String regex = "^\\[\\{Str=(?=.*3133332E3233362E3231382E3937)(?!.*3133332E3233362E3231382E3937\\}\\]$).+\\}\\]$";
        String str1 = "[{Str=3133332E3233362E3231382E3937}]";
        String str2 = "[{Str=3133332E3233362E3231382E3937E2808FE280AAE280AAE280AFE2808FE280AA}]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher1 = pattern.matcher(str1);
        Matcher matcher2 = pattern.matcher(str2);
        System.out.println("Does str1 match? " + matcher1.matches()); // Should be false
        System.out.println("Does str2 match? " + matcher2.matches()); // Should be true

        String regex2 = "^\\[\\{\"IP\":\"(?=.*3133332E3233362E3231382E3937)(?!.*3133332E3233362E3231382E3937\"}]$).+}]$";
        String str3 = "[{\"IP\":\"3133332E3233362E3231382E3937\"}]";
        String str4 = "[{\"IP\":\"3133332E3233362E3231382E3937E2808FE280AAE280AAE280AFE2808FE280AA\"}]";
        pattern = Pattern.compile(regex2);
        matcher1 = pattern.matcher(str3);
        matcher2 = pattern.matcher(str4);
        System.out.println("Does str1 match? " + matcher1.matches()); // Should be false
        System.out.println("Does str2 match? " + matcher2.matches()); // Should be true

        String regex3 = "^\\[\\{\"IP\":\"(?=.*aa)(?!.*aa\",.*$).+}]$";
        String regex4 = "^\\[\\{\"IP\":\"(?=.*aa)(?!.*aa\",.*$).+,\"地址\":\"(?=.*bb)(?!.*bb\"}]$).+}]$";
        String regex5 = "^\\[\\{\"IP\":\"(?=.*aa)(?!.*aa\",.*$).+,\"地址\":\"(?=.*bb)(?!.*bb\",.*$).+,\"姓名\":\"(?=.*cc)(?!.*cc\"}]$).+}]$";
        String str5 = "[{\"IP\":\"aa\",\"地址\":\"bb\"}]";
        String str6 = "[{\"IP\":\"aa1\",\"地址\":\"bb1\"}]";
        String str7 = "[{\"IP\":\"aa\",\"地址\":\"bb\",\"姓名\":\"cc\"}]";
        String str8 = "[{\"IP\":\"aa1\",\"地址\":\"bb1\",\"姓名\":\"cc1\"}]";
        pattern = Pattern.compile(regex3);
        pattern = Pattern.compile(regex4);
        pattern = Pattern.compile(regex5);
        matcher1 = pattern.matcher(str5);
        matcher2 = pattern.matcher(str6);
        matcher1 = pattern.matcher(str7);
        matcher2 = pattern.matcher(str8);

        System.out.println("Does str1 match? " + matcher1.matches()); // Should be false
        System.out.println("Does str2 match? " + matcher2.matches()); // Should be true
    }
}
