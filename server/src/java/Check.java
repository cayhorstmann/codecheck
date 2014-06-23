import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@javax.ws.rs.Path("/check")
public class Check {
    @Context ServletContext context;
    //@Context private HttpServletRequest request;
    //@Context private HttpServletResponse response;
    
    static Random random = new Random();

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public Response check(MultivaluedMap<String, String> formParams, @CookieParam("ckid") String ckid)
    throws IOException {
    	//get cookie from browser
    	System.out.println("In /CHECK");
    	
        if (ckid == null) {
        	System.out.println("Cookie is NULL");
        	ckid = random.nextLong() + "";
        }
        System.out.println("ckid = " + ckid);
        
        Path submissionDir = Util.getDir(context, "submissions");
        Path tempDir = Util.createTempDirectory(submissionDir);
        
        //save cookie to submissionDir
        File file = new File(tempDir.toString() + "/cookie.dat");
        if (!file.exists()) {
			file.createNewFile();
		}
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(ckid);
		bw.close();
        
        String repo = "ext";
        String problem = "";
        String level = "check";
        for (String key : formParams.keySet()) {
            String value = formParams.getFirst(key);
            if (key.equals("repo"))
                repo = value;
            else if (key.equals("problem"))
                problem = value;
            else if (key.equals("level"))
                level = value;
            else
                Util.write(tempDir, key, value);
        }
        Util.runLabrat(context, repo, problem, level, tempDir.toAbsolutePath().toString());
        System.out.println("Check");
        System.out.println("repo = " + repo);
        System.out.println("problem = " + problem);
        System.out.println("level = " + level);
        System.out.println("tempDir = " + tempDir.toAbsolutePath().toString());
        Path tempDirName = tempDir.getFileName();

        int age = 180 * 24 * 60 * 60;
        NewCookie cookie = new NewCookie(new NewCookie("ckid", ckid), "", age, false);
        return Response.seeOther(URI.create("fetch/" + tempDirName + "/report.html")).cookie(cookie).build();
    }
}
