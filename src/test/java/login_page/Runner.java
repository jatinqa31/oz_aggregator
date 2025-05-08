package login_page;

import java.util.ArrayList; 
import java.util.List;

import org.testng.TestNG;
 
public class Runner {   
	public static TestNG testNg;
	public static void main(String[] args) {
		//code to call logindemo test class from runner file.
		testNg = new TestNG();
		//testNg.setTestClasses(new Class[] {LoginTest.class});	
		
		
		// Create a list to store the paths of your TestNG XML files
        List<String> suiteFiles = new ArrayList<>();
        System.out.println("userdir "+System.getProperty("user.dir"));
        
        // Get the operating system name
        String osName = System.getProperty("os.name");
        
        // Print the operating system name
        System.out.println("Operating System: " + osName);
        if(osName.contains("Linux")) {
        	//System.out.println("jj "+System.getProperty("user.dir"));
            suiteFiles.add(System.getProperty("user.dir") + "//testng.xml");// in Ubuntu forward slashes are used while backawrd in windows        	
        }else {
        	suiteFiles.add(System.getProperty("user.dir") + "\\testng.xml");// in Ubuntu forward slashes are used while backawrd in windows
        } 
        
        // Set the list of XML files to the TestNG object
        testNg.setTestSuites(suiteFiles);
        
		testNg.run();
		System.out.println("RUNNING");;
	} 
} 
 