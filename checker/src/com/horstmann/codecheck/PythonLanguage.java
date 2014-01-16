package com.horstmann.codecheck;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class PythonLanguage implements Language {

	@Override
	public boolean isSource(Path p) {
		return p.toString().endsWith(".py");
	}

	@Override
	public boolean isTester(String modulename) {
		return modulename != null && modulename.matches(".*Tester[0-9]*");
	}

    private static Pattern mainPattern = Pattern.compile("def\\s+main\\s*:");
    
    /* (non-Javadoc)
	 * @see com.horstmann.codecheck.Language#isMain(java.nio.file.Path, java.nio.file.Path)
	 */
    @Override
	public boolean isMain(Path dir, Path p) {
        if (!isSource(p))
            return false;
        String contents = Util.read(dir, p);
        return contents != null && mainPattern.matcher(contents).find();
    }

	@Override
	public String moduleOf(Path path) {
        String name = path.toString();
        if (!name.endsWith(".py"))
            return null;
        return name.substring(0, name.length() - 3); // drop .py
	}

	@Override
	public Path pathOf(String moduleName) {
		Path p = FileSystems.getDefault().getPath("", moduleName);
        Path parent = p.getParent();
        if (parent == null)
            return FileSystems.getDefault().getPath(moduleName + ".py");
        else
            return parent.resolve(p.getFileName().toString() + ".py");
	}

	@Override
	public boolean compile(String modulename, Path dir, Report report) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String run(String mainclass, Path classpathDir, String args,
			String input, int timeoutMillis) throws IOException,
			ReflectiveOperationException {

		//System.out.println(mainclass + " ---- ");
		//System.out.println(classpathDir + " ---- ");
		//System.out.println(args + " ---- ");
		//System.out.println(input + " ---- ");


		String result = "";
		System.out.println("--- call made --- /usr/bin/python3 " + classpathDir + "/" + mainclass + ".py" + (args == null || args.trim().equals("") ? "" : " " + args));

		try
        {
            Runtime r = Runtime.getRuntime();
			
            Process p = r.exec("/usr/bin/python3 " + classpathDir + "/" + mainclass + ".py" + (args == null || args.trim().equals("") ? "" : " " + args));
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            p.waitFor();
            while (br.ready()) {
                result += br.readLine() + "\n";
            }

        }
        catch (Exception e)
        {
			String cause = e.getMessage();
			if (cause.equals("python3: not found"))
				System.out.println("No python interpreter found.");
        }

		return result;
	}

	@Override
	public void writeTester(Path sourceDir, Path targetDir, Path file,
			List<String> modifiers, String name, List<String> argsList)
			throws IOException {

		String className = moduleOf(Util.tail(file));
		List<String> lines = Util.readLines(sourceDir.resolve(file));
		lines.add(0, "import " + className);
		lines.add(0, "import sys");
		int i = 0;
		// TODO use regular expression to deal with multiple spaces between 'def' and function name
		while (i < lines.size() && !lines.get(i).contains("def " + name)) i++;
		if (i == lines.size())
			throw new RuntimeException("Can't find function " + name + " for inserting CALL in " + file);
		lines.set(i, lines.get(i).replace(name, name + "CodeCheck"));
		i = lines.size() - 1;
		lines.add(++i, "def main(argv=None):");
		lines.add(++i, "	if argv is None:");
		lines.add(++i, "		argv = sys.argv");
		//lines.add(++i, "");
		for (int k = 0; k < argsList.size(); k++) {
			lines.add(++i, "	if argv[1] == \"" + (k + 1) + "\":");
			lines.add(++i, "		expected = " + name + "CodeCheck(" + argsList.get(k) + ")");
			lines.add(++i, "		print(expected)");
			lines.add(++i, "		actual = " + name + "." + name + "(" + argsList.get(k) + ")");
			lines.add(++i, "		print(actual)");
			lines.add(++i, "		print('true' if (actual == expected) else 'false')");
		}
		lines.add(++i, "if __name__ == \"__main__\":");
		lines.add(++i, "	main()");
		//System.out.println(" --- className --- " + className); 
		//System.out.println(" --- sourceDir --- " + sourceDir.toString()); 
		//System.out.println(" --- targetDir --- " + targetDir.toString());
		//System.out.println(" --- file --- " + file.toString());
		//System.out.println(" --- modifiers --- " + modifiers);
		//System.out.println(" --- name --- " + name);
		//System.out.println(" --- argsList.get(0) --- " + argsList.get(0));
		//System.out.println("*************************************************");
		//for (int x = 0; x < lines.size(); x++){
		//	System.out.println(lines.get(x));
		//}
		//System.out.println("*************************************************");
		Files.write(targetDir.resolve(pathOf(className + "CodeCheck")), lines, StandardCharsets.UTF_8);
	}

    @Override
	public String[] pseudoCommentDelimiters() { return new String[] { "##", "" }; }
    
    private static String patternString = "\\s*([A-Za-z][A-Za-z0-9]*)\\s*=\\s*(.+)";
    private static Pattern pattern = Pattern.compile(patternString);
    
    /* (non-Javadoc)
	 * @see com.horstmann.codecheck.Language#variablePattern()
	 */
    @Override
	public Pattern variablePattern() { return pattern; }
    
    /* (non-Javadoc)
	 * @see com.horstmann.codecheck.Language#substitutionSeparator()
	 */
    @Override
	public String substitutionSeparator() { return ";"; } 
}
