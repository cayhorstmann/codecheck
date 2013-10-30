package com.horstmann.codecheck;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class CLanguage implements Language {

	@Override
	public boolean isSource(Path p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTester(String modulename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMain(Path dir, Path p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String moduleOf(Path path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path pathOf(String moduleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean compile(String modulename, Path dir, Report report) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String run(String mainclass, Path classpathDir, String args,
			String input, int timeoutMillis) throws IOException,
			ReflectiveOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeTester(Path sourceDir, Path targetDir, Path file,
			List<String> modifiers, String name, List<String> argsList)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] pseudoCommentDelimiters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pattern variablePattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String substitutionSeparator() {
		// TODO Auto-generated method stub
		return null;
	}

}
