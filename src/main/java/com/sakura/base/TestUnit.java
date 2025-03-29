package com.sakura.base;

import java.util.LinkedHashMap;

import lombok.Data;

/**
 * <br>对应于XML文件中的unit元素</br>
 *
 * @version 1.0
 * @since   1.0
 */
@Data
public class TestUnit {
	
    private String id;

    private String name;
    
	private LinkedHashMap<String,TestCase> caseMap;

//	public static String getId() {
//        return id;
//    }
//
//	public void setId(String id) {
//        this.id = id;
//    }
//    
//    public static String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//    
//    public LinkedHashMap<String,TestCase> getCaseMap() {
//        return caseMap;
//    }
//
//    public void setCaseMap(LinkedHashMap<String,TestCase> caseMap) {
//        this.caseMap = caseMap;
//    }
    
	@Override
	public String toString() {
//	    System.out.println("TestUnit [caseMap=" + caseMap + "]");
		return "TestUnit [caseMap=" + caseMap + "]";
	}
}