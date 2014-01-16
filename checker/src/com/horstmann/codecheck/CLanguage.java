package com.horstmann.codecheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
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
        System.out.println("CLanguage isMain(): " + contents != null && mainPattern.matcher(contents).find());
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
		

		try
        {
            Runtime r = Runtime.getRuntime();
			String compileCommand = "gcc -o " + dir.toString() + "/"  + modulename + " " + dir.toString() + "/" + modulename + ".c";
            Process p = r.exec(compileCommand);
            p.waitFor();
            
            System.out.println("CLanguage compile() $compileCommand = " + compileCommand);
        }
		
        catch (Exception e)
        {
			report.error("Cannot compile", e.getMessage());
			return false;
        }

		System.out.println("CLanguage compile() $return = true");
		return true;
	}

	@Override
	public String run(String mainclass, Path classpathDir, String args,
			String input, int timeoutMillis) throws IOException,
			ReflectiveOperationException {
		
		String result = "";
		System.out.println("CLanguage run() --- call made --- " + classpathDir + "/" + mainclass + (args == null || args.trim().equals("") ? "" : " " + args));

		try
        {
            Runtime r = Runtime.getRuntime();
            
            String runCProgram = "#!/bin/bash";
            runCProgram += String.format("\n" + classpathDir + "/" + mainclass + (args == null || args.trim().equals("") ? "" : " " + args));
            runCProgram += String.format("\necho $?");
            
            try {
    			FileWriter fw = new FileWriter(String.format("%s/runCProgram", classpathDir),true);
    			Process p0 = r.exec("chmod 777 " + String.format("%s/runCProgram", classpathDir));
    			p0.waitFor();
    			fw.write(runCProgram);
    			fw.close();
    				
    			// run the CProgram
    			String execCommand = classpathDir + File.separator + "runCProgram";
    			System.out.println("CLanguage run() $execCommand = " + execCommand);
    			Process p = r.exec(execCommand);
    			p.waitFor();
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
		

		System.out.println("CLanguage writeTester()");

	}

	@Override
	public String[] pseudoCommentDelimiters() { return new String[] { "/*/*", "" }; }
	
	private static String patternString = "\\s*([A-Za-z][A-Za-z0-9]*)\\s*=\\s*(.+)";
	private static Pattern pattern = Pattern.compile(patternString);

	@Override
	public Pattern variablePattern() { return pattern; }

	@Override
	public String substitutionSeparator() { return ";"; } 

}
