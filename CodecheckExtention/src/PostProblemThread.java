import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import bluej.extensions.BlueJ;


public class PostProblemThread implements Runnable {

	private File projectDir;
    private BlueJ bluej;
    private ConfigInfo extCfg;
    
    public PostProblemThread(File projectDir) {
        this.projectDir = projectDir;
        
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
		String urlString = "http://cs12.cs.sjsu.edu:8080/codecheck/check";
		// TODO Auto-generated method stub
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(new FileInputStream(projectDir.getPath() + "/codecheck.dat"), "UTF-8"));
			
			String repoValue = in.readLine();
			String problemValue = in.readLine();
			String levelValue = in.readLine();
			String submitFileName = in.readLine();
			
			in.close();
			in = new BufferedReader(
					new InputStreamReader(new FileInputStream(projectDir.getPath() + "/" + submitFileName), "UTF-8"));
			
			String problemContent = "";
			String line = "";
			while ((line = in.readLine()) != null) {
				problemContent += line + "\n";
			}
			in.close();
			
			
			Map<Object, Object> params = new HashMap<Object, Object>();
			params.put("repo", repoValue);
			params.put("problem", problemValue);
			params.put("level", levelValue);
			params.put(submitFileName, problemContent);
			
			String result = doPost(urlString, params);
			
			PrintWriter out = new PrintWriter(
					new FileWriter(projectDir.getPath() + "/result.html"));
			out.print(result);
			out.close();
			
			//open result file in browser
			Desktop.getDesktop().open(new File(projectDir.getPath() + "/result.html"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String doPost(String urlString, Map<Object, Object> nameValuePairs) throws IOException {
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		try (PrintWriter out = new PrintWriter(connection.getOutputStream())) {
			boolean first = true;
			for (Map.Entry<Object, Object> pair : nameValuePairs.entrySet()) {
				if (first) first = false;
				else out.print('&');
		
				String name = pair.getKey().toString();
				String value = pair.getValue().toString();
				out.print(name);
				out.print('=');
				out.print(URLEncoder.encode(value, "UTF-8"));
			}
		}
		
		StringBuilder response = new StringBuilder();
		try (Scanner in = new Scanner(connection.getInputStream())) {
			while (in.hasNextLine()) {
				response.append(in.nextLine());
				response.append("\n");
			}
		}
		catch (IOException e) {
			if (!(connection instanceof HttpURLConnection)) throw e;
			InputStream err = ((HttpURLConnection) connection).getErrorStream();
			if (err == null) throw e;
			Scanner in = new Scanner(err);
			response.append(in.nextLine());
			response.append("\n");
		}
		
		return response.toString();
	}

}
