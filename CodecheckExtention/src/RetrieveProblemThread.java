import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import bluej.extensions.BlueJ;
import bluej.extensions.PackageNotFoundException;
import bluej.extensions.ProjectNotOpenException;


/** 
 * Thread to retrieve problem from server,
 * and put it in the current BlueJ project directory
 * 
 * */
public class RetrieveProblemThread implements Runnable {

	private File projectDir;
	private String urlString;
    private BlueJ bluej;
    private ConfigInfo extCfg;
    
    public RetrieveProblemThread(File projectDir, String urlString) {
        this.projectDir = projectDir;
        this.urlString = urlString;
        
        ExtensionInformation extNfo = ExtensionInformation.getInstance();
       
        try { 
            bluej = extNfo.getBlueJ();
            extCfg = extNfo.getConfig();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    
	@Override
	public void run() {
		try {
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			connection.connect();

			PrintWriter out = new PrintWriter(
					new FileWriter(projectDir.getPath() + "/codecheck.dat"));
			String t = "";
			Scanner in = new Scanner(connection.getInputStream());
			while (in.hasNextLine()) {
				t += in.nextLine() + "\n";
			}
			
			int startIndex, endIndex;
			String s;
			//find support files;
			startIndex = t.indexOf("Use the following files:");
			endIndex = t.indexOf("Complete the following file:");
			if (startIndex > -1) {
				s = t.substring(startIndex, endIndex - 1);
				int endJava = s.indexOf(".java", 0) - 1;
				while (endJava > -1) {
					String supportFileName = ".java";
					while (s.charAt(endJava) != '>') {
						supportFileName = s.charAt(endJava--) + supportFileName;
					}
					
					startIndex = s.indexOf("<pre>", endJava) + "<pre>".length() + 1;
					endIndex = s.indexOf("</pre", endJava) - 1;
					
					String supportFileContent = s.substring(startIndex, endIndex);
					createFile(supportFileName, supportFileContent);
					
					endJava = s.indexOf(".java", endIndex) - 1;
				}
			}
			
			//find the content of submit file
			startIndex = t.indexOf("<textarea name=");
			s = t.substring(startIndex);
			
			endIndex = s.indexOf(" rows=");
			String submitFileName = s.substring("<textarea name=".length() + 1, endIndex - 1);
			startIndex = s.indexOf("public class ");
			endIndex = s.indexOf("</textarea>");
			String submitFileContent = s.substring(startIndex, endIndex - 1);
			createFile(submitFileName, submitFileContent);
			
			//find repo value
			startIndex = s.indexOf("name=\"repo\"") + "name=\"repo\" value=\"".length();
			String repoValue = "";
			while (s.charAt(startIndex) != '\"') {
				repoValue += s.charAt(startIndex++);
			}
			
			//find problem value
			startIndex = s.indexOf("name=\"problem\"") + "name=\"problem\" value=\"".length();
			String problemValue = "";
			while (s.charAt(startIndex) != '\"') {
				problemValue += s.charAt(startIndex++);
			}
			
			//find problem value
			startIndex = s.indexOf("name=\"level\"") + "name=\"level\" value=\"".length();
			String levelValue = "";
			while (s.charAt(startIndex) != '\"') {
				levelValue += s.charAt(startIndex++);
			}
			
			out.println(repoValue);
			out.println(problemValue);
			out.println(levelValue);
			out.println(submitFileName);
			out.println(urlString);
			out.close();

			try {
				bluej.getCurrentPackage().reload();
			} catch (ProjectNotOpenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PackageNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createFile(String fileName, String fileContent) {
		try {
			PrintWriter out = new PrintWriter(
					new FileWriter(projectDir.getPath() + "/" + fileName));
			out.print(fileContent);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
