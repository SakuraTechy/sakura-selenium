package com.sakura.base;

import java.util.List;

import lombok.Data;

@Data
public class TestCase extends TestBase{
	
	private String id;

    private String name;

    private boolean cancel;

    private List<TestStep> steps;

//    public static String getid() {
//        return id;
//    }
//
//    public void setid(String id) {
//        TestCase.id = id;
//    }
//    
//    public static String getname() {
//        return name;
//    }
//
//    public void setname(String name) {
//        TestCase.name = name;
//    }
//
//    public boolean isCancel() {
//        return cancel;
//    }
//
//    public void setCancel(boolean cancel) {
//        this.cancel = cancel;
//    }
//    
//	public List<TestStep> getSteps() {
//		return steps;
//	}
//
//	public void setSteps(List<TestStep> steps) {
//		this.steps = steps;
//	}
//
//	public String getName() {
//		return super.getName();
//	}
	
	@Override
	public String toString() {
	    System.out.println("TestCase "+super.getName()+" [steps=" + steps + "]");
		return "TestCase "+super.getName()+" [steps=" + steps + "]";
	}
}