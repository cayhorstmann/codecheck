import java.io.File;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String urlString = "http://cs12.cs.sjsu.edu:8080/codecheck/files/1405031718ldc30r5fjhiqwpgly9j7y0qw/2";
		String urlString = "http://cs12.cs.sjsu.edu:8080/codecheck/files/13122624052f3hddznnkpzi9yryjg4jtd42/2";
		//String urlString = "http://cs12.cs.sjsu.edu:8080/codecheck/files?repo=bj4cc&problem=ch06/c06_exp_6_105";
		File dir = new File("/home/minhminh/eclipse_plugins/CodecheckExtention");
		
		boolean testGet = false;
		if (testGet) {
			RetrieveProblemThread get = new RetrieveProblemThread(dir, urlString);
			get.run();
		} else {
			PostProblemThread post = new PostProblemThread(dir);
			post.run();
		}
	}

}
