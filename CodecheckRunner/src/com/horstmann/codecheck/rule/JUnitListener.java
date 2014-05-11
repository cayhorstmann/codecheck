package com.horstmann.codecheck.rule;


import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class JUnitListener extends RunListener {
	List<String> methods;
	List<String> outcomes;
	List<String> reasons;
	List<String> points;
	PointWatcher pointWatcher;

	public JUnitListener(List<String> methods, List<String> outcomes, List<String> reasons, List<String> points) {
		this.methods = methods;
		this.outcomes = outcomes;
		this.reasons = reasons;
		this.points = points;
		pointWatcher = new PointWatcher();
	}
	
	public void testStarted(Description description)
	{
		methods.add(description.getMethodName());
		outcomes.add("Pass");
		reasons.add("");
		
		int value = pointWatcher.getPointValue();
		points.add(value + "");
		System.out.println(value);
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