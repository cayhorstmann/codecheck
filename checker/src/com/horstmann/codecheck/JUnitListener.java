package com.horstmann.codecheck;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class JUnitListener extends RunListener {
	List<String> methods;
	List<String> outcomes;
	List<String> reasons;

	public JUnitListener(List<String> methods, List<String> outcomes, List<String> reasons) {
		this.methods = methods;
		this.outcomes = outcomes;
		this.reasons = reasons;
	}
	
	public void testStarted(Description description)
	{
		methods.add(description.getMethodName());
		outcomes.add("Pass");
		reasons.add("");
	}
	
	public void testFailure(Failure failure) 
	{
		int index = methods.size() - 1;
		outcomes.remove(index);
		outcomes.add("Fail");
		reasons.remove(index);
		reasons.add(failure.getMessage());
	}
}
