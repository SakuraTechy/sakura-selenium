package com.sakura.util;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/** 
* @author 刘智King
* @date 2020年10月22日 下午2:32:46
*/
public class SystemOutUtil {

    public static void main(String[] args) throws FileNotFoundException {
        PrintStream print = new PrintStream("Logs/log.txt"); 
        System.setOut(print);
        System.out.print("Reallly?");  
        System.out.println("Yes");  
        System.out.println("So easy");  
    }
}
