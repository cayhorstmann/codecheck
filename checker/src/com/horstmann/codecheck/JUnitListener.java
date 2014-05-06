package com.horstmann.codecheck;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class JUnitListener extends RunListener {
	
	public void testRunStarted(Description description) {
		System.out.println("Test run started: ");
		System.out.println(description.testCount());
	}
	
	public void testStarted(Description description)
	{
		System.out.println("Starting test: " + description.getMethodName());
		//System.out.println(description.());
	}
	
	public void testFailure(Failure failure) 
	{
		System.out.println("Execution of test case failed : "+ failure.getMessage());
	}
	
	public void testRunFinished(Result result) throws java.lang.Exception
	{
		System.out.println(result.getFailureCount());
		System.out.println("Number of testcases executed : " + result.getRunCount());
	}
}
