package com.horstmann.codecheck.rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;


public class PointWatcher extends TestWatcher {
	
	private Point point;
	
	@Override
	protected void starting(Description description) {
		point = description.getAnnotation(Point.class);
	}
	
	public Point getPoint() {
		return point;
	}
	
	public int getPointValue() {
		if (point == null) {
			return -1;
		}
		return point.value();
	}
}
