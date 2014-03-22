package com.horstmann.codecheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class CLanguage implements Language {

	@Override
	public boolean isSource(Path p) {
		return p.toString().endsWith(".c");
	}

	@Override
	public boolean isTester(String modulename) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private static Pattern mainPattern = Pattern.compile("int|void\\s*main\\s+");

	@Override
	public boolean isMain(Path dir, Path p) {
		if (!isSource(p))
            return false;
        String contents = Util.read(dir, p);
        System.out.println("CLanguage isMain(): " + (contents != null && mainPattern.matcher(contents).find()));
        return contents != null && mainPattern.matcher(contents).find();
	}

	@Override
	public String moduleOf(Path path) {
		String name = path.toString();
        if (!name.endsWith(".c"))
            return null;
        return name.substring(0, name.length() - 2); // drop .c
	}

	@Override
	public Path pathOf(String moduleName) {
		Path p = FileSystems.getDefault().getPath("", moduleName);
        Path parent = p.getParent();
        if (parent == null)
            return FileSystems.getDefault().getPath(moduleName + ".c");
        else
            return parent.resolve(p.getFileName().toString() + ".c");
	}

	@Override
	public boolean compile(String modulename, Path dir, Report report) {
		

		String compilerMessage = "";
		try
        {
            Runtime r = Runtime.getRuntime();
			String compileCommand = "gcc -w -o " + dir.toString() + "/"  + modulename + " " + dir.toString() + "/" + modulename + ".c -lm ";
			System.out.println("CLanguage compile() $compileCommand = " + compileCommand);
            Process p = r.exec(compileCommand);
            p.waitFor();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while (br.ready()) {
            	compilerMessage += br.readLine() + "\n";
            }
            
            System.out.println("CLanguage compile() $compilerMessage = " + compilerMessage);
            
            if (!(compilerMessage.trim().equals(""))) {
            	report.error("Compiler error", compilerMessage);
            	System.out.println("CLanguage compile() $return = FALSE");
            	return false;
            }
            
        }
		
        catch (Exception e)
        {
			report.error("Cannot compile", e.getMessage());
			return false;
        }

		System.out.println("CLanguage compile() $return = TRUE");
		return true;
	}

	@Override
	public String run(String mainclass, Path classpathDir, String args,
			String input, int timeoutMillis) throws IOException,
			ReflectiveOperationException {
		
		System.out.println("CLanguage run() $mainclass = " + mainclass);
		System.out.println("CLanguage run() $classpathDir = " + classpathDir);
		System.out.println("CLanguage run() $args = " + args);
		System.out.println("CLanguage run() $input = " + input);
		System.out.println("CLanguage run() $timeoutMillis = " + timeoutMillis);
		
		String result = "";
		String execCommand = "";
		
		try
        {
            Runtime r = Runtime.getRuntime();
            
            try {
            	
            	Process p;
            	
            	if (input != null){
            		execCommand = "/home/kn-ub64/codecheck/checker/runC.sh " + classpathDir + "/" + mainclass + (args == null || args.trim().equals("") ? "" : " " + args  + " 2>&1");
                	System.out.println("CLanguage run() $execCommand = " + execCommand);
                	p = r.exec(execCommand);
                	p.getOutputStream();
                	p.getOutputStream().write(input.getBytes(Charset.forName("UTF-8")));
                	p.getOutputStream().flush();
        			p.waitFor();
            	}
            	else {
            		execCommand = "/home/kn-ub64/codecheck/checker/runC.sh " + classpathDir + "/" + mainclass + (args == null || args.trim().equals("") ? "" : " " + args) + " 2>&1";
            		System.out.println("CLanguage run() $execCommand = " + execCommand);
            		p = r.exec(execCommand);
            		p.waitFor();
            	}
            	
    			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while (br.ready()) {
                    result += br.readLine() + "\n";
                }
    	        
    		} catch (Exception e1) {
    			e1.printStackTrace();
    		}
        }
        catch (Exception e)
        {
			System.out.println("!!! Main run() ERROR !!! " + e.getMessage());
        }

		System.out.println("CLanguage run() $result = " + result);
		return result;
	}

	@Override
	public void writeTester(Path sourceDir, Path targetDir, Path file,
			List<String> modifiers, String name, List<String> argsList)
			throws IOException {
		
		String className = moduleOf(Util.tail(file));
		String returnType = "";
		String parameterType = "";
		List<String> lines = Util.readLines(sourceDir.resolve(file));
		List<String> studentLines = Util.readLines(targetDir.resolve(className + ".c"));
		
		// append codecheck to solution function
		int i = 0;
		while (i < lines.size() && !lines.get(i).contains(className)){
			i++;
		}
		if (i == lines.size())
			throw new RuntimeException("Can't find function " + className + " for inserting CALL in " + file);
		lines.set(i, lines.get(i).replace(className, className + "codeCheck"));
		
		// find return type and parameter type
		String trimmedLine = lines.get(i).trim();
		returnType = trimmedLine.substring(0, trimmedLine.indexOf(" "));
		parameterType = trimmedLine.substring(trimmedLine.indexOf("("), trimmedLine.lastIndexOf(")"));
		
		// append student lines
		for (int x = 0; x < studentLines.size(); x++){
			lines.add(studentLines.get(x));
		}
		
		// write main method
		lines.add("int main( int argc, const char* argv[] ){");
		for (int k = 0; k < argsList.size(); k++) {
			lines.add("   if (strcmp(argv[1], \"" + (k + 1) + "\") == 0) {");
			lines.add("      " + returnType + " expected = " + className + "codeCheck(" + argsList.get(k) + ");");
			
			if (returnType.contains("char")) {
				lines.add("      printf(\"%s\\n\", expected);");
			} else if (returnType.contains("float")) {
				lines.add("      printf(\"%f\\n\", expected);");
			} else {
				lines.add("      printf(\"%d\\n\", expected);");	
			}
			
			lines.add("      " + returnType + " actual = " + className + "(" + argsList.get(k) + ");");
			
			if (returnType.contains("char")) {
				lines.add("      printf(\"%s\\n\", actual);");
			} else if (returnType.contains("float")) {
				lines.add("      printf(\"%f\\n\", actual);");
			} else {
				lines.add("      printf(\"%d\\n\", actual);");	
			}
			
			if (returnType.contains("char*")) {
				lines.add("      if (strcmp(actual, expected) == 0) printf(\"%s\", \"true\");");
				lines.add("      else printf(\"%s\\n\", \"false\");");
			} else {
				lines.add("      if (actual == expected) printf(\"%s\", \"true\");");
				lines.add("      else printf(\"%s\\n\", \"false\");");
			} 
			
			lines.add("   }");
		}
		
		lines.add("}");
		
		System.out.println("CLanguage writeTester()");
		System.out.println(" --- className --- " + className); 
		System.out.println(" --- sourceDir --- " + sourceDir.toString()); 
		System.out.println(" --- targetDir --- " + targetDir.toString());
		System.out.println(" --- file --- " + file.toString());
		System.out.println(" --- modifiers --- " + modifiers);
		System.out.println(" --- name --- " + name);
		System.out.println("CLanguage writeTester() $returnType = " + returnType);
		System.out.println("CLanguage writeTester() $parameterType = " + parameterType);
//		for (int x = 0; x < argsList.size(); x++){
//			System.out.println(" --- argsList.get(" + x + ") --- " + argsList.get(x));
//		}
//		System.out.println("********************* lines **********************");
//		for (int x = 0; x < lines.size(); x++){
//			System.out.println(lines.get(x));
//		}
//		System.out.println("*************************************************");
		
		Files.write(targetDir.resolve(pathOf(className + "CodeCheck")), lines, StandardCharsets.UTF_8);

	}

	@Override
	public String[] pseudoCommentDelimiters() { return new String[] { "////", "" }; }
	
	private static String patternString = ".*\\S\\s+(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)\\s*=\\s*([^\\s;]+)\\s*;.*";
	private static Pattern pattern = Pattern.compile(patternString);

	@Override
	public Pattern variablePattern() { return pattern; }

	@Override
	public String substitutionSeparator() { return ";"; } 

}
