import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

@javax.ws.rs.Path("/files")
public class Files {
    private static final Pattern IMG_PATTERN = Pattern
            .compile("[<]\\s*[iI][mM][gG]\\s*[sS][rR][cC]\\s*[=]\\s*['\"]([^'\"]*)['\"][^>]*[>]");

    @Context ServletContext context;
    @Context private HttpServletRequest request;
    @Context private HttpServletResponse response;

    private static String start = "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /></head><body style=\"font-family: sans;\">";
    private static String before = "<form method=\"post\" action=\"{0}\" {1}>";

    private static String fileAreaBefore = "<p>{0}</p><textarea name=\"{0}\" rows=\"{1}\" cols=\"66\">";
    private static String fileAreaAfter = "</textarea>";
    private static String fileUpload = "<p>{0}: <input type=\"file\" name=\"{0}\"/></p>";
    private static String after = "<p><input type=\"submit\"/><input type=\"hidden\" name=\"repo\" value=\"{0}\"><input type=\"hidden\" name=\"problem\" value=\"{1}\"><input type=\"hidden\" name=\"level\" value=\"{2}\"></p></form>";
    private static String end = "</body></html>";

    // TODO: Singular/plural

    private static String useStart = "<p>Use the following {0,choice,1#file|2#files}:</p>";
    private static String provideStart = "<p>Complete the following {0,choice,1#file|2#files}:</p>";
    // TODO: Separate out empty and nonempty files, with "provide" and "complete"

    private String repo = "ext";
    
    @GET
    @javax.ws.rs.Path("/{problem}") 
    @Produces("text/html")
    public String files(@PathParam("problem") String problem, @CookieParam("ckid") String ckid)
    		throws IOException {
    	return files("ext", problem, "check", false, ckid);
    }

    @GET
    @javax.ws.rs.Path("/{problem}/{level}") 
    @Produces("text/html")
    public String files(@PathParam("problem") String problem, @PathParam("level") String level, @CookieParam("ckid") String ckid)
    		throws IOException {
    	return files("ext", problem, level, false, ckid);
    }
    
    @GET
    @javax.ws.rs.Path("/")    
    @Produces("text/html")
    public String files(@QueryParam("repo") @DefaultValue("ext") String repo,
                        @QueryParam("problem") String problemName,
                        @DefaultValue("check") @QueryParam("level") String level,
                        @DefaultValue("false") @QueryParam("upload") boolean upload,
                        @CookieParam("ckid") String ckid)
    throws IOException {
    	//get cookie from browser
    	System.out.println("In /FILES");
    	
    	/*
        Cookie[] cookies = request.getCookies();
        String cookieValue = "";
        if (cookies != null) {
	        for (int i = 0; i < cookies.length; i++) {
	        	String name = cookies[i].getName();
	        	if (name.equals("ckid")) {
	        		cookieValue = cookies[i].getValue();
	        	}
	        }
        }
        */
    	
        //set cookie to browser
        if (ckid == null) {
        	System.out.println("Cookie is NULL");
        	Random rd = new Random();
        	ckid = rd.nextLong() + "";
            Cookie cookie = new Cookie("ckid", ckid);
            cookie.setMaxAge(180 * 24 * 60 * 60);
            cookie.setSecure(false);
            cookie.setPath("/codecheck");
            response.addCookie(cookie);
        }
        System.out.println("ckid = " + ckid);
        
    	StringBuilder result = new StringBuilder();
        result.append(start);

        Path repoPath = Paths.get(context
                                  .getInitParameter("com.horstmann.codecheck.repo." + repo));
        // TODO: That comes from Problems.java--fix it there
        System.out.println("repoPath = " + repoPath.toString());
        
        if (problemName.startsWith("/")) problemName = problemName.substring(1);
        Path problemPath = repoPath.resolve(problemName);

        if (isParametricProblem(problemPath)) {
        	System.out.println("Parametric Problem");
        	
        	long cookie = Long.parseLong(ckid);
    		ParametricProblem paraProb = new ParametricProblem(cookie);
    		
    		Path submissionDir = Util.getDir(context, "submissions");
    		Path workDir = Util.createTempDirectory(submissionDir);
    		
    		System.out.println("workDir = " + workDir.toString());
    		try {
				paraProb.run(workDir, problemPath);
				
			} catch (ScriptException e) {
				e.printStackTrace();
			}
    		problemPath = Paths.get(workDir.toString() + "/temp");
    		
    		System.out.println("submissionDir = " + submissionDir.toString());
    		
    		List<File> testers = findTester(problemPath);
    		if (testers.size() > 0) {
    			//check(submissionDir);
    			/*
    			for (File f : testers) {
    				System.out.println("Compiling: " + f.getName());
    				if (compile(f.getName(), problemPath)) {
    					System.out.println("Compiled: " + f.getName());
    				}
    			}
    			*/
    		}
    		
    		System.out.println("Done Parametric");
        } else {
        	System.out.println("Normal problem");
        }
        System.out.println("problem path = " + problemPath.toString());
        
        Problem problem = new Problem(problemPath, level);
        boolean includeCode = true;
        String description = getDescription(problemPath, "statement.html"); // TODO: Legacy
        if (description == null) {
            description = getDescription(problemPath, "problem.html");
            if (description == null)
                description = getDescription(problemPath, "original-statement.html"); // TODO: legacy
        } else
            includeCode = false; // code already shown in statement.html

        // TODO: Indicate whether it is ok to add more classes
        // TODO: Should this be a part of the script?

        Set<Path> requiredFiles = problem.getRequiredFiles();
        Set<Path> useFiles = problem.getUseFiles();
        Map<Path, StringBuilder> contents = new HashMap<>();
        Iterator<Path> iter = useFiles.iterator();
        while (iter.hasNext()) {
        	Path p = iter.next();
        	StringBuilder cont = Util.htmlEscape(Util.read(problemPath, p));
        	if (cont.substring(0, 7).matches("//HIDE\\s"))
        		iter.remove();
        	else
        		contents.put(p, cont);
        }
        if (description != null)
            result.append(description);
        if (includeCode && useFiles.size() > 0) {
            result.append(MessageFormat.format(useStart, useFiles.size()));
            for (Path p : useFiles) {
            	StringBuilder cont = contents.get(p); 
                result.append("<p>");
                result.append(Util.tail(p).toString());
                result.append("</p>\n");
                result.append("<pre>");
                result.append(cont);
                result.append("</pre\n>");
            }
        }
        
        // TODO: In file upload, must still SHOW the non-empty required files
        
        String requestURL = request.getRequestURL().toString();
        String url = requestURL.substring(0, requestURL.indexOf("files")) + (upload ? "checkUpload" : "check");
        
        result.append(MessageFormat.format(before, url,
                                           upload ? "encoding=\"multipart/form-data\"" : ""));
        result.append(MessageFormat.format(provideStart, requiredFiles.size()));

        
        // TODO: Remove heuristic for codecomp
        if (!upload && useFiles.size() == 0 && requiredFiles.size() == 1) includeCode = true;

        for (Path p : requiredFiles) {
            String file = Util.tail(p).toString();
            if (upload) {
                if (includeCode) {
                    result.append("<p>");
                    result.append(file);
                    result.append("</p>\n");
                    result.append("<pre>");
                    result.append(Util.htmlEscape(Util.read(problemPath, p)));
                    result.append("</pre\n>");
                }
                result.append(MessageFormat.format(fileUpload, file));
            } else {
                int lines = 0;
                String cont = "";
                if (includeCode) {
                    cont = Util.read(problemPath, p);
                    lines = Util.countLines(cont);
                    if (cont == null) cont = "";
                }
                if (lines == 0) lines = 20;

                result.append(MessageFormat.format(fileAreaBefore, file, lines));
                result.append(Util.htmlEscape(cont));
                result.append(fileAreaAfter);
            }
        }

        result.append(MessageFormat.format(after, repo, problemName, level));
        result.append(end);
        return result.toString();
    }

    public static String getDescription(Path problemDir, String problemFile)
    throws IOException {
        String description = Util.read(problemDir, problemFile);
        if (description == null)
            return null;
        // Strip off HTML header. If the file contains "<body>" or "<BODY>",
        String lcdescr = description.toLowerCase();
        int start = lcdescr.indexOf("<body>");
        int end = -1;
        if (start != -1) {
            start += "<body>".length();
            end = lcdescr.lastIndexOf("</body>");
        } else {
            start = lcdescr.indexOf("<html>");
            if (start != -1) {
                start += "<html>".length();
                end = lcdescr.lastIndexOf("</html>");
            }
        }

        StringBuilder result = new StringBuilder(description);
        if (end != -1) result.replace(end, result.length(), "");
        if (start != -1) result.replace(0, start, "");

        Matcher matcher = IMG_PATTERN.matcher(result);
        start = 0;
        while (matcher.find(start)) {
            start = matcher.start(1);
            end = matcher.end(1);
            String replacement = "data:image/png;base64,"
                                 + Util.base64(problemDir, result.substring(start, end));
            result.replace(start, end, replacement);
            start += replacement.length();
        }

        // Old-style Wiley Plus crud
        Path ipf = problemDir.resolve("image.properties");
        if (java.nio.file.Files.exists(ipf)) {
            Properties ip = new Properties();
            ip.load(new FileInputStream(ipf.toFile()));
            String imageToken = "@" + ip.getProperty("image.token") + "@";
            start = result.indexOf(imageToken);
            if (start >= 0) {
                String replacement = "<img src=\"data:image/png;base64,"
                                     + Util.base64(problemDir, ip.getProperty("image.file"))
                                     + "\"/>";
                result.replace(start, start + imageToken.length(), replacement);
            }
        }
        return result.toString();
    }
    
    private boolean isParametricProblem(Path problemDir) {
    	File f = new File(problemDir.toString() + "/params.js");
    	return f.exists();
    }
    
    private List<File> findTester(Path problemDir) {
    	System.out.println("findTester");
    	List<File> r = new ArrayList<File>();
    	File folder = new File(problemDir.toString() + "/student");
    	File[] listOfFiles = folder.listFiles();
    	for (File f : listOfFiles) {
    		if (f.getName().matches(".*Tester[0-9]*.java")) {
    			r.add(f);
    		}
    	    
    	}
    	return r;
    }
    
    private boolean compile(String classname, Path dir) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        OutputStream outStream = null; //new ByteArrayOutputStream();
        OutputStream errStream = null; //new ByteArrayOutputStream();
        int result = compiler.run(null, outStream, errStream, "-sourcepath", dir.toString(),
                                  "-d", dir.toString(), dir.resolve(Util.javaPath(classname)).toString());
        return result == 0;
    }
    
    private void check(Path dir, Path submissionDir) throws IOException {
        // TODO: Only good for old-style
    	System.out.println("check - dir = " + dir.toString());
        int maxLevel = 1;
        for (int i = 9; i >= 2 && maxLevel == 1; i--) // Find highest level
            if (java.nio.file.Files.exists(dir.resolve("student" + i)) || java.nio.file.Files.exists(dir.resolve("solution" + i))) maxLevel = i;

        boolean grade = java.nio.file.Files.exists(dir.resolve("grader"));        
        List<String> subdirs = new ArrayList<>();
        for (int i = 1; i <= (grade ? maxLevel + 1 : maxLevel); i++) {
            Path tempDir = Util.createTempDirectory(submissionDir);
            // Copy solution files up to the current level
            if (i <= maxLevel) subdirs.add(i == 1 ? "solution" : "solution" + i);
            for (Path p : Util.getDescendantFiles(dir, subdirs))
            	java.nio.file.Files.copy(dir.resolve(p), tempDir.resolve(Util.tail(p)));

            String problem = dir.getFileName().toString();
            String levelString = grade && i == maxLevel + 1 ? "grade" : "" + i;
            Util.runLabrat(context, repo, problem, levelString, tempDir.toAbsolutePath().toString());
            System.out.println("Files - check");
            System.out.println("repo = " + repo);
            System.out.println("problem = " + problem);
            System.out.println("level = " + levelString);
            System.out.println("tempDir = " + tempDir.toAbsolutePath().toString());
            Path tempDirName = tempDir.getFileName();

        }
    }
}
