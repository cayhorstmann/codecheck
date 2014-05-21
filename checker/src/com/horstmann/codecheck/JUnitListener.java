package com.horstmann.codecheck;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import com.horstmann.codecheck.rule.Score;

public class JUnitListener extends RunListener {
	List<String> methods;
	List<String> outcomes;
	List<String> reasons;
	List<String> points;
	int totalScore, maxScore;
	int maxPoint;

	public JUnitListener(List<String> methods, List<String> outcomes, List<String> reasons, List<String> points) {
		this.methods = methods;
		this.outcomes = outcomes;
		this.reasons = reasons;
		this.points = points;
		totalScore = 0;
		maxScore = 0;
	}
	
	public void testStarted(Description description) {
		methods.add(description.getMethodName());
		outcomes.add("Pass");
		reasons.add("");
		
		maxPoint = 1;
		Score score = description.getAnnotation(Score.class);
		
		if (score != null) {
			maxPoint = score.value(); 
		}
		points.add(maxPoint + " / " + maxPoint);
		maxScore += maxPoint;
		totalScore += maxPoint;
	}
	
	public void testFailure(Failure failure) {
		int index = methods.size() - 1;
		outcomes.remove(index);
		outcomes.add("Fail");
		reasons.remove(index);
		reasons.add(failure.getMessage());
		points.remove(index);
		points.add("0 / " + maxPoint);
		
		totalScore -= maxPoint;
	}
	
	public int getMaxScore() {
		return maxScore;
	}
	
	public int getTotalScore() {
		return totalScore;
	}
}