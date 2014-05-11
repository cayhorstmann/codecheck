package com.horstmann.codecheck;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.horstmann.codecheck.rule.Point;

public class JUnitListener extends RunListener {
	List<String> methods;
	List<String> outcomes;
	List<String> reasons;
	List<String> points;
	int totalScore, maxScore;

	public JUnitListener(List<String> methods, List<String> outcomes, List<String> reasons, List<String> points) {
		this.methods = methods;
		this.outcomes = outcomes;
		this.reasons = reasons;
		this.points = points;
		totalScore = 0;
		maxScore = 0;
	}
	
	public void testStarted(Description description)
	{
		methods.add(description.getMethodName());
		outcomes.add("Pass");
		reasons.add("");
		
		int value = 1;
		Point point = description.getAnnotation(Point.class);
		
		if (point != null) {
			value = point.value(); 
		}
		points.add(value + "");
		maxScore += value;
		totalScore += value;
	}
	
	public void testFailure(Failure failure) 
	{
		int index = methods.size() - 1;
		outcomes.remove(index);
		outcomes.add("Fail");
		reasons.remove(index);
		reasons.add(failure.getMessage());
		totalScore -= Integer.parseInt(points.remove(index));
		points.add("0");
	}
	
	public int getMaxScore() {
		return maxScore;
	}
	
	public int getTotalScore() {
		return totalScore;
	}
}