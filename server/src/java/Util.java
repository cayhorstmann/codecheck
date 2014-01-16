import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletContext;

public class Util {
    public static Path getDir(ServletContext context, String key) throws IOException {
        String dirName = context.getInitParameter("com.horstmann.codecheck." + key);
        Path dir = Paths.get(dirName);
        if (!Files.exists(dir)) Files.createDirectory(dir);
        return dir;
    }


    public static Path tail(Path p) {
        return p.subpath(1, p.getNameCount());
    }

    public static String read(Path path) {
        try {
            return new String(java.nio.file.Files.readAllBytes(path), "UTF-8");
        } catch (IOException ex) {
            return null;
        }
    }
    public static String read(Path dir, String file) {
        return read(dir.resolve(file));
    }
    public static String read(Path dir, Path file) {
        return read(dir.resolve(file));
    }

    public static void write(Path parent, String name, String contents) {
        try {
            java.nio.file.Files.write(parent.resolve(name), contents.getBytes("UTF-8"));
        } catch (IOException ex) {
            ex.printStackTrace();
            // TODO: log
            throw new RuntimeException(ex);
        }
    }

    public static String base64(Path dir, String fileName) throws IOException {
        return new sun.misc.BASE64Encoder().encode(java.nio.file.Files.readAllBytes(dir.resolve(fileName)));
    }

    public static String getProperty(String dir, String file, String property) {
        File pf = new File(dir, file);
        if (pf.exists()) {
            Properties p = new Properties();
            try {
                p.load(new FileInputStream(pf));
                return p.getProperty(property);
            } catch (IOException ex) {
            }
        }
        return null;
    }

    public static int countLines(String s) {
        if (s == null)
            return 0;
        int lines = 0;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == '\n')
                lines++;
        return lines;
    }

    public static StringBuilder htmlEscape(CharSequence s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '<')
                b.append("&lt;");
            else if (c == '>')
                b.append("&gt;");
            else if (c == '&')
                b.append("&amp;");
            else
                b.append(c);
        }
        return b;
    }

    public static Path createTempDirectory(Path parent) throws IOException {
        String prefix = new SimpleDateFormat("yyMMddkkmm").format(new Date());
        return java.nio.file.Files.createTempDirectory(parent, prefix);
    }

    public static void unzip(InputStream in, Path dir) throws IOException {
        // TODO: Check for containing dir whose name equals zip file
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry entry;
        while ((entry = zin.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                String name = entry.getName();
                Path outputPath = dir.resolve(name);
                Path parent = outputPath.getParent();
                Stack<Path> parents = new Stack<>();
                while (!parent.equals(dir)) {
                    parents.push(parent);
                    parent = parent.getParent();
                }
                while (!parents.empty()) {
                    Path top = parents.pop();
                    if (!java.nio.file.Files.exists(top))
                        java.nio.file.Files.createDirectory(top);
                }
                OutputStream out = new FileOutputStream(outputPath.toFile());
                byte[] buf = new byte[1024];
                int len;
                while ((len = zin.read(buf)) > 0)
                    out.write(buf, 0, len);
                out.close();
            }
            zin.closeEntry();
        }
        zin.close();
    }

    public static String runScript(String script) {
        try {
            Process process = Runtime.getRuntime().exec(script);
            process.waitFor();

            Scanner in = new Scanner(process.getErrorStream(), "UTF-8");
            StringBuilder result = new StringBuilder();
            while (in.hasNextLine()) {
                result.append(in.nextLine());
                result.append("\n");
            }
            in.close();
            if (result.length() > 0)
                return result.toString();

            in = new Scanner(process.getInputStream(), "UTF-8");
            result = new StringBuilder();
            while (in.hasNextLine()) {
                result.append(in.nextLine());
                result.append("\n");
            }
            in.close();
            // CAUTION: Apparently, one can't just large input from the process stdout
            // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4062587
            return result.toString().trim();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public static String runLabrat(ServletContext context, String repo,
                                 String problem, String level, String tempDir) {
        String repoPath = context
                          .getInitParameter("com.horstmann.codecheck.repo." + repo);
        String command = Util.getProperty(repoPath, "repo.properties",
                                          "repo.command");

        
        // check file extension to invoke proper codecheck type (java, python, or c/c++) 
        File f = new File(tempDir);
        String fileName = f.listFiles()[0].getName();
        if (command == null) {
        	
        	// run Java
        	if (fileName.endsWith(".java")){
        		command = context.getInitParameter("com.horstmann.codecheck.javacommand");
                runScript(MessageFormat.format(command, level, tempDir, repoPath + File.separator + problem, repo + ":" + problem + ":" + level));
                
            } else if (fileName.endsWith(".py")) {
            	
            	// run Python inside docker
                
                /* NOTE !!!!!!!!!!!!!!!!
                 * 
                 * 
                 * 	!!!!!!!!!!!!!!!!!   Must enable swap to prevent docker from not being able to run due to low memory   	!!!!!!!!!!!!!!!!!
                 * 
                 * 
                 * 				To add 1024 MB swap to your ec2 instance you type:
    			 *
    			 *	sudo /bin/dd if=/dev/zero of=/var/swap.1 bs=1M count=1024
    			 *	sudo /sbin/mkswap /var/swap.1
    			 *	sudo /sbin/swapon /var/swap.1
    			 *
    			 *				If you need more than 1024 then change that to something higher.
    			 *	
    			 *				To enable it by default after reboot, add this line to /etc/fstab:
    			 *	
    			 *	/var/swap.1 swap swap defaults 0 0
    			 *
                 *  
                 *  
                 *  
                 *  !!!!!!!!!!!!!!!!!   If using a password for sudo, change variable "systemPassword" accordingly 			!!!!!!!!!!!!!!!!!
                 *  
                 *  If not using a password then no need to do anything
                 *  
                 *  
                 *   */
            	
            	command = context.getInitParameter("com.horstmann.codecheck.pythoncommand");
            	
            	// move the problem file to tempDir so that docker can copy it to container (docker's security reason)
                String codecheckCommitNumber = "latest";
                String systemPassword = "kietkiet";
                String runDockerScript = "#!/bin/bash";
                runDockerScript += String.format("\nmkdir %sproblem", tempDir + File.separator);
                runDockerScript += String.format("\necho %s | sudo -S cp -r %s* %sproblem", systemPassword, repoPath + File.separator + problem + File.separator, tempDir + File.separator);
                
                // create the Dockerfile in tempDir
                String dockercommand = context.getInitParameter("com.horstmann.codecheck.dockerpythoncommand");
                String codecheckScript = MessageFormat.format(dockercommand, level, tempDir, repoPath
                        + File.separator + problem, repo + ":" + problem + ":" + level);
				codecheckScript = codecheckScript.replace("\n", "");
                runDockerScript += String.format("\necho 'FROM codecheck/cc-%s' >> %sDockerfile", codecheckCommitNumber, tempDir + File.separator);
                runDockerScript += String.format("\necho 'ADD ./ %s' >> %sDockerfile", tempDir, tempDir + File.separator);
                runDockerScript += String.format("\necho 'ADD ./problem %s' >> %sDockerfile", repoPath + File.separator + problem, tempDir + File.separator);
                runDockerScript += String.format("\necho 'RUN %s' >> %sDockerfile", codecheckScript, tempDir + File.separator);
                
                // run the Dockerfile
                runDockerScript += String.format("\necho %s | sudo -S docker build -rm %s > %sdockeroutput.txt", systemPassword, tempDir,  tempDir + File.separator);
                
                // remove problem folder inside tempDir
                runDockerScript += String.format("\necho %s | sudo -S rm -r %sproblem",systemPassword, tempDir + File.separator);
                
                try {
        			FileWriter fw = new FileWriter(String.format("%s/runDocker", tempDir),true);
        			Process p0 = Runtime.getRuntime().exec("chmod 777 " + String.format("%s/runDocker", tempDir));
        			p0.waitFor();
        			fw.write(runDockerScript);
        			fw.close();
        				
        			// run the runDockerScript
        			Process p = Runtime.getRuntime().exec(tempDir + File.separator + "runDocker");
        			p.waitFor();
        	        
        		} catch (Exception e1) {
        			e1.printStackTrace();
        		}
                
                // parse the dockeroutput text file for the final docker image ID
                String dockerOutput = read(Paths.get(tempDir + File.separator + "dockeroutput.txt"));
                int startPos = dockerOutput.lastIndexOf("Successfully built ");
                int endPos = dockerOutput.indexOf("\n", startPos);
                String dockerImageID = dockerOutput.substring(endPos - 12, endPos);
                
                // run final image to copy the report.html and singed zip files from the docker container back to host
                String tempDirFolder = tempDir.substring(tempDir.lastIndexOf(File.separator) + 1);
                String copyFileScript = "#!/bin/bash";
                copyFileScript += String.format("\nID=`echo %s | sudo -S docker run -d %s /bin/bash`", systemPassword, dockerImageID);
                copyFileScript += String.format("\necho %s | sudo -S docker cp $ID:%s %s", systemPassword, tempDir, tempDir);
                copyFileScript += String.format("\nmv %s %s", tempDir + File.separator + tempDirFolder + File.separator + "*.html", tempDir);
                copyFileScript += String.format("\nmv %s %s", tempDir + File.separator + tempDirFolder + File.separator + "*.signed.zip", tempDir);
                copyFileScript += String.format("\necho %s | sudo -S rm -r %s", systemPassword, tempDir + File.separator + tempDirFolder);
                
                // remove the container BEFORE image to prevent stale NFS file handle ERROR
                copyFileScript += String.format("\necho %s | sudo -S docker rm $ID", systemPassword);
                copyFileScript += String.format("\necho %s | sudo -S docker rmi %s", systemPassword, dockerImageID);
                
                try {
        			FileWriter fw = new FileWriter(String.format("%s/copyFiles", tempDir),true);
        			Process p01 = Runtime.getRuntime().exec("chmod 777 " + String.format("%s/copyFiles", tempDir));
        			p01.waitFor();
        			fw.write(copyFileScript);
        			fw.close();
        				
        			// run the copyFileScript 
        			Process p = Runtime.getRuntime().exec(tempDir + File.separator + "copyFiles");
        			p.waitFor();
        	        
        		} catch (Exception e1) {
        			e1.printStackTrace();
        		}
            }  else if (fileName.endsWith(".c")) {
            	command = context.getInitParameter("com.horstmann.codecheck.ccommand");
                runScript(MessageFormat.format(command, level, tempDir, repoPath + File.separator + problem, repo + ":" + problem + ":" + level));
            }
        }
        
        return MessageFormat.format(command, level, tempDir, repoPath + File.separator + problem, repo + ":" + problem + ":" + level);
    }

    /**
     * Gets all files contained in a directory and its subdirectories
     *
     * @param dir
     *            a directory
     * @return the list of files, as Path objects that are relativized against
     *         dir
     * @throws IOException
     */
    public static Set<Path> getDescendantFiles(final Path dir) throws IOException {
        final Set<Path> result = new TreeSet<>();
        if (dir == null || !Files.exists(dir)) return result;
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                result.add(dir.relativize(file));
                return FileVisitResult.CONTINUE;
            }
        });
        return result;
    }

    public static Set<Path> getDescendantFiles(Path dir, List<String> subdirs) throws IOException {
        Set<Path> result = new TreeSet<>();
        for (String subdir : subdirs) {
            for (Path p : getDescendantFiles(dir.resolve(subdir))) {
                // Is there a matching one? If so, replace it
                boolean found = false;
                Iterator<Path> iter = result.iterator();
                while (!found && iter.hasNext())
                    if (p.equals(Util.tail(iter.next()))) {
                        iter.remove();
                        found = true;
                    }
                result.add(Paths.get(subdir).resolve(p));
            }
        }
        return result;
    }

    public static String javaClass(Path path) {
        String name = path.toString();
        if (!name.endsWith(".java"))
            return null;
        name = name.substring(0, name.length() - 5); // drop .java
        return name.replace(FileSystems.getDefault().getSeparator(), ".");
    }

    public static Path javaPath(String classname) {
        Path p = FileSystems.getDefault().getPath("", classname.split("[.]"));
        Path parent = p.getParent();
        if (parent == null)
            return FileSystems.getDefault().getPath(classname + ".java");
        else
            return parent.resolve(p.getFileName().toString() + ".java");
    }
}
