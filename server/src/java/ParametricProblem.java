import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.script.ScriptException;


public class ParametricProblem {
	private Path workDir;
    private Path problemDir;
    private Path tempDir;
    private ScriptRunner sRunner; 
    private long cookie;
    
    public ParametricProblem(long cookie) {
    	this.cookie = cookie;
    	this.sRunner = new ScriptRunner();
    }
    
	public void run(Path workDir, Path problemDir) throws IOException, ScriptException {
		this.workDir = workDir;
		this.problemDir = problemDir;
		
		File javascriptFile = new File(problemDir.toString() + "/" + "params.js");
		if (javascriptFile.exists()) {
			tempDir = Paths.get(workDir.toString() + "/temp");
	        if (Files.exists(tempDir))
	        	deleteFolder(new File(tempDir.toString()));
	        
	        //adding cookie
	        sRunner.putValue("codecheck", new Functions(cookie));
	        
	        Files.createDirectory(tempDir);	        
	        //Run Params.js on at the beginning
			sRunner.executeScriptFromFile(javascriptFile.getAbsolutePath());
					
			//Traverse all the files in sub-folders and replace
			traverseFolder(problemDir);
			
		} 
	}
	
	private void deleteFolder(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				deleteFolder(f);
			}
			if (file.listFiles().length == 0) { //empty folder
				file.delete();
			}
		} else {
			file.delete();
		}
	}
	
	private void traverseFolder(Path dir) throws IOException, ScriptException {
		//System.out.println("Folder: " + dir.toString());
		File folder = new File(dir.toString());
		File[] allFiles = folder.listFiles();
		for (File f : allFiles) {
			if (f.getName().contains("~")) continue;
			if (f.isDirectory()) {
				Path tFolder = Paths.get(tempDir.toString() + "/" + f.getName());
		        if (! Files.exists(tFolder)) Files.createDirectory(tFolder);
				traverseFolder(Paths.get(f.getPath()));
			} else {
				replaceParameterInFile(f);
			}
		}
	}
	
	private void replaceParameterInFile(File f) throws ScriptException {
		int index = f.getAbsolutePath().indexOf(problemDir.toString()) + problemDir.toString().length();
		String t = f.getAbsolutePath().substring(index);
		String newFile = tempDir.toAbsolutePath() + t;
		System.out.println("Processing: " + t);
		
		BufferedReader br = null;
		 
		try {
			
			File file = new File(newFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			String sCurrentLine;
			br = new BufferedReader(new FileReader(f));
 
			while ((sCurrentLine = br.readLine()) != null) {
				String content = "";
				sCurrentLine += " ";
				for (int i = 0; i < sCurrentLine.length() - 1; )
					if (sCurrentLine.charAt(i) == '{' && sCurrentLine.charAt(i + 1) == '=') {
						int j = i + 2;
						int count = 1;
						while (count != 0) {
							if (sCurrentLine.charAt(j) == '}')
								count--;
							if (sCurrentLine.charAt(j) == '{')
								count++;
							j++;							
						}
						
						String key = sCurrentLine.substring(i + 2, j - 1);
						/*
						if (key.contains("{=")) 
							key = substituion(key);
						*/
						//System.out.println("key = " + key);
						
						String value = sRunner.getValue(key.trim());
						//System.out.println("value = " + value);
						
						
						
						if (value == "") {
							value = sRunner.executeScript(key);
						}
						content += value;
						i = j;
					} else {
						content += sCurrentLine.charAt(i);
						i++;
					}
				bw.write(content + "\n");
			}
			
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private String substitution(String key) {
		
		return "";
	}
}