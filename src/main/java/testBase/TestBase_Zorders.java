package testBase;
import java.lang.String;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
//import java.text.SimpleDateFormat;
//import java.util.Base64;
//import java.util.Calendar;
//import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.remote.CapabilityType;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;
//import login_page.DatabaseConnectionPool;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URL;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import org.json.JSONArray;
//import org.json.JSONObject;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import testBase.DeleteOldLogs;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;


public class TestBase_Zorders { 
	public static Properties OR;
	public File f1; 
	public FileInputStream file; 
	public static WebDriver driver;
	public static WebDriver swiggy_driver;

	public static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();
	public static ThreadLocal<WebDriver> tdriver2 = new ThreadLocal<>();
	//private static final Logger logger = (org.testng.log4testng.Logger) LogManager.getLogger(TestBase.class);
	
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 6553;
	public static int counter = 0;
	public static SoftAssert sa;

	public static Connection connection; // = DriverManager.getConnection(url, username, password);
	

	public static String url = "jdbc:mysql://localhost:3306/lexerpos_db";
	public static String username = "root";
	public static String password = "";
	
    Fillo fillo = new Fillo();
    com.codoid.products.fillo.Connection connection1 = null;
    Recordset recordset = null;
    
    String Order_Time="";
    String order_view="";
    
    public static final Logger logger = LogManager.getLogger(testBase.TestBase_Zorders.class);
    
	static {
		try 
		{
			connection = DriverManager.getConnection(url, username, password);	
			//System.out.println("Db Connected!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int portNo=1;
	
    // Declare ExtentReports and ExtentTest
    public ExtentReports extent;
    public ExtentSparkReporter sparkReporter;
    public ExtentTest test;
	
    @BeforeTest()
	public void launchBrowser() throws Exception 
    {
		getBrowser("chrome");
//		   	sparkReporter = new ExtentSparkReporter("target/extent-report.html");
//	        extent = new ExtentReports();
//	        extent.attachReporter(sparkReporter);
    } 

    @BeforeTest(enabled =false)
	public void launchBrowser2() throws Exception 
    {
		getBrowser("chrome","Swiggy");
//		   	sparkReporter = new ExtentSparkReporter("target/extent-report.html");
//	        extent = new ExtentReports();
//	        extent.attachReporter(sparkReporter);
    } 
 
	//Very VERy Important this is use  // Remove the WebDriver instance from ThreadLocal after cleanup used after every method so that browser memory is not used
	@AfterMethod()
	public void cleanUp() throws InterruptedException 
	{
		if(tdriver!= null)
		{
			tdriver.remove();
		}
//		driver.manage().deleteAllCookies();
		System.gc(); // Optional, but suggests garbage collection
		Thread.sleep(1000);
		//System.out.println("cleanup");

	}
	

//	public static final Logger logger = LogManager.getLogger(testBase.TestBase_Zorders.class);  
	public void getBrowser(String browser,String aggregator) throws Exception {
		 if (browser.equalsIgnoreCase("chrome")) 
			{	  
			    portNo=1; 
			    int counter = 0;
			    String printer = "";

		        
		        // SQL query to retrieve data 
		        String query1 = "SELECT * FROM aggregator_setting where aggregator="+aggregator;
		        
	            // Create a PreparedStatement to execute the query
	            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

	            // Execute the query and get the ResultSet
	            ResultSet resultSet1 = preparedStatement1.executeQuery();
	            
	            if (resultSet1.next()) {
			    
	            
					//generate random port no until port is available = true
					do { portNo=generateRandomPort(connection); }
					while(isPortAvailable(portNo)==false);
		        	
		            int newPortNumber = portNo; // Change this to the desired port number
		             
		            try {
		            		
		            	DeleteOldLogs dl = new DeleteOldLogs();
		            	dl.DeleteOldLogs();
		            	            	
		
		                // Automatically download and setup the latest ChromeDriver
		                WebDriverManager.chromedriver().setup();
		                
		                // Set up Chrome options
		                ChromeOptions options = new ChromeOptions();
		
		                System.out.println("newPortNumber "+newPortNumber);
//		                
//		//                webSocketDebuggerUrl=webSocketDebuggerUrl.replace("ws://", "").replace("/devtools/browser/", "");
//		//                webSocketDebuggerUrl="http://192.168.1.4:9016/devtools/browser/fc082222-7f0c-4a2a-a18a-bcee70e6a77c";
//		                
//		                // Use WebSocket URL directly to connect with Chrome DevTools
//		                //options.setExperimentalOption("debuggerAddress", "http://localhost:9016");

//				         
//					        //options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
//					        
					        // SQL query to retrieve data 
					        String query = "SELECT * FROM aggregator_setting where aggregator="+aggregator;
					        
				            // Create a PreparedStatement to execute the query
				            PreparedStatement preparedStatement = connection.prepareStatement(query);
		
				            // Execute the query and get the ResultSet
				            ResultSet resultSet = preparedStatement.executeQuery();
				            
				            if (resultSet.next()) {
				            	if(resultSet.getInt("enable_silent_printing")==1) {
					            	options.addArguments("--kiosk-printing");		            		
				            	}
				            	if(resultSet.getInt("is_headless")==1) {
					            	options.addArguments("--headless");  // Example: for headless testing
				  	 		        //important arg to run in HEADLESS without applying this HEADLESS wont work/RUN
				    	 		    options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome Safari/537.36");
				            	}
				            }
		
					         options.addArguments("--hide-crash-restore-bubble");//to avoid chrome restore popup to appear 
		 		             options.addArguments("--max-old-space-size=4096");//increase browser memory size
			 		  	       	
			 		         // Create ChromeOptions and specify any necessary arguments
		
		 		             options.addArguments("--disable-gpu");  // Disable GPU if headless
			 		         options.addArguments("--remote-debugging-port="+newPortNumber);  // If you're using remote debugging
		 		 	       	 String userProfile = System.getenv("USERPROFILE");
//		// 		 	       	 System.out.println("uprofile "+userProfile);
			 		 	     options.addArguments("--no-sandbox"); 
			 		 	       		 		         
			 		         options.addArguments("--disable-software-rasterizer"); // Disable software rasterizer (sometimes helps with fonts)
			 		         //options.addArguments("--window-size=800x400"); // Set a resolution for the headless mode
			 		         options.addArguments("window-size=800,600");
			 		         
			 		         options.addArguments("--disable-dev-shm-usage");
			 		         options.addArguments("--disable-extensions");
			 		         options.addArguments("--disable-cache");
		//	 		       --disable-cache --disable-extensions --disable-dev-shm-usage --no-sandbox --disable-gpu --hide-crash-restore-bubble --max-old-space-size=4096 --kiosk-printing
			 		        
			 		          	 		         
			 		       // Initialize ChromeDriver with the specified options
			 		         	  	       	
			 		    // Configure the default print settings (use a PDF printer or a physical printer)
//			 		        Map<String, Object> prefs = new HashMap<>();
//			 		        
//			 		        // Enable silent printing (using PDF as an example)
//			 		        prefs.put("printing.print_preview_sticky_settings", false);
//			 		        prefs.put("savefile.default_directory", "C:\\Temp");
//			 		        prefs.put("printing.print_header_footer", false);
//			 		        prefs.put("savefile.filename", "p1.pdf"); // You can also customize the filename
//			 		        options.setExperimentalOption("prefs", prefs);
		
			 		        
				            //moveWindowTo(driver, 720, 20); 
			 		        //options.addArguments("start-maximized");
			 		        options.addArguments("user-data-dir="+"C:\\Google\\Chrome\\User Data\\Profile 2");
		//  	 		        options.setExperimentalOption("debuggerAddress",webSocketDebuggerUrl);  // Connect to the Chrome instance
		  	 		        options.addArguments("--remote-allow-origins=*");
		
	// 						 this to run portable chrome exe which do not use profiles o save passwords 
		//	 		         options.setBinary("C:\\Users\\Jatin\\Downloads\\GoogleChromePortable\\GoogleChromePortable.exe");
		//	 		         options.addArguments("user-data-dir=C:\\browser");
		  	 		        
			 		         swiggy_driver = new ChromeDriver(options);	  	
		 		             swiggy_driver.navigate().to("https://partner.swiggy.com/login?next=restaurant-info");
			 		         tdriver2.set(swiggy_driver);
		                 } 
			            catch (Exception e) {
			                System.out.println("Exception: IS " + e.getMessage());
			            }  
 					} 
			}		  
		 }
			//	ZOMATO code runs from here ...    
	            
	public void getBrowser(String browser) throws Exception {
			 if (browser.equalsIgnoreCase("chrome")) 
				{	
				    portNo=1; 
				    int counter = 0;
				    String printer = "";
       
			        // SQL query to retrieve data 
			        String query1 = "SELECT * FROM aggregator_setting where aggregator='Zomato'";
			        
		           // Create a PreparedStatement to execute the query
		           PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

		           // Execute the query and get the ResultSet
		           ResultSet resultSet1 = preparedStatement1.executeQuery();
		           
		           if (resultSet1.next()) {
					//generate random port no until port is available = true
					do { portNo=generateRandomPort(connection); }
					while(isPortAvailable(portNo)==false);	        	
						
	            int newPortNumber = portNo; // Change this to the desired port number
	             
	            try {
	            		
	            	DeleteOldLogs dl = new DeleteOldLogs();
	            	dl.DeleteOldLogs();
	            	            	
	
	                // Automatically download and setup the latest ChromeDriver
	                WebDriverManager.chromedriver().setup(); 
	                	                
                	ChromeOptions options2 = new ChromeOptions();

	                options2.addArguments("--remote-debugging-port="+newPortNumber); 
//	                options.addPreference("devtools.chrome.enabled", true);
	                //options.addPreference("devtools.debugger.remote-enabled", true);
	                //options.addPreference("devtools.debugger.remote-port"+"," +newPortNumber, options); 
	                

	                // Connect to the existing Firefox instance (remote debugger)
//	                WebDriver driver = new RemoteWebDriver(new URL(remoteURL), options);

	                String userProfile2 = System.getenv("USERPROFILE");
	                
	                System.out.println("userProfile2 "+userProfile2);
	                            	    
			            String port = "localhost:" + newPortNumber;  
//		                options2.addArguments("user-data-dir="+"C:\\Google\\Chrome\\User Data\\Profile 1");			         
		                options2.addArguments("user-data-dir="+userProfile2+"\\AppData\\Local\\Google\\Chrome\\User Data\\Profile 1");
		                //options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
				        
				        // SQL query to retrieve data 
				        String query = "SELECT * FROM aggregator_setting where aggregator='Zomato'";
				        
			            // Create a PreparedStatement to execute the query
			            PreparedStatement preparedStatement = connection.prepareStatement(query);
	
			            // Execute the query and get the ResultSet
			            ResultSet resultSet = preparedStatement.executeQuery();
			            
			            if (resultSet.next()) {
			            	if(resultSet.getInt("enable_silent_printing")==1) {
				            	options2.addArguments("--kiosk-printing");		            		
			            	}
			            	if(resultSet.getInt("is_headless")==1) {
				            	options2.addArguments("--headless");  // Example: for headless testing
			  	 		        //important arg to run in HEADLESS without applying this HEADLESS wont work/RUN
			    	 		    options2.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome Safari/537.36");
			            	}
			            }
	
				         options2.addArguments("--hide-crash-restore-bubble");//to avoid chrome restore popup to appear 
	 		             options2.addArguments("--max-old-space-size=5096");//increase browser memory size
		 		  	       	
		 		         // Create ChromeOptions and specify any necessary arguments
	
	 		             options2.addArguments("--disable-gpu");  // Disable GPU if headless
		 		          // If you're using remote debugging
	 		 	       	 
	// 		 	       	 System.out.println("uprofile "+userProfile);
		 		 	     options2.addArguments("--no-sandbox"); 
//---for chrome-----------------------		 		 	       		 		         
		 		         options2.addArguments("--disable-software-rasterizer"); // Disable software rasterizer (sometimes helps with fonts)
		 		         //options2.addArguments("--window-size=800x400"); // Set a resolution for the headless mode
		 		         options2.addArguments("window-size=800,600");
		 		        options2.addArguments("start-maximized");

		 		         options2.addArguments("--disable-dev-shm-usage");
		 		         options2.addArguments("--disable-extensions");
//		 		         
		 		         options2.addArguments("--disable-infobars");
		 		         options2.addArguments("start-maximized");
		 		         options2.addArguments("--disable-extensions");
		 		         options2.addArguments("--disable-popup-blocking");
		 		         options2.addArguments("disable-infobars");
		 		         
		 		         options2.addArguments("--disable-notifications");
		 		         
		 		        options2.addArguments("start-maximized");
		 		        options2.addArguments("--disable-infobars"); // Disables the infobars
		 		        options2.addArguments("--disable-notifications");
		 		         
		 		         
//		 		         //additionl arguments to stop GPU
//
//		 		        options.addArguments("--disable-accelerated-2d-canvas");
//		 		        options.addArguments("--disable-accelerated-compositing");
//		 		        options.addArguments("--disable-gpu-compositing");
//		 		        options.addArguments("--disable-webgl");
//		 		        options.addArguments("--disable-webgl2");
//		 		         
//		 		         
//		 		        options.addArguments("--disable-background-networking");
//		 		        options.addArguments("--disable-background-timer-throttling");
//		 		        options.addArguments("--disable-backgrounding-occluded-windows");
//		 		        options.addArguments("--disable-sync");
		 		         
		 		        options2.addArguments("--disable-application-cache");
		 		        options2.addArguments("--disable-cache");
	//	 		       --disable-cache --disable-extensions --disable-dev-shm-usage --no-sandbox --disable-gpu --hide-crash-restore-bubble --max-old-space-size=4096 --kiosk-printing
//
		 		 	     //--above options are for chrome		 		        
		 		          	 		         
		 		       // Initialize ChromeDriver with the specified options
		 		         	  	       	
		 		        System.out.println(userProfile2);
	//	 		        options.addArguments("user-data-dir="+userProfile+"\\AppData\\Local\\Mozilla\\Firefox\\Profiles\\yicxuyyi.Profile 2");
	//  	 		        options.setExperimentalOption("debuggerAddress",webSocketDebuggerUrl);  // Connect to the Chrome instance
	//  	 		        options.addArguments("--remote-allow-origins=*");
	
		 		         // this to run portable chrome exe which do not use profiles o save passwords 
	//	 		         options.setBinary("C:\\Users\\Jatin\\Downloads\\GoogleChromePortable\\GoogleChromePortable.exe");
	//	 		         options.addArguments("user-data-dir=C:\\browser");
    //		         	 driver = new ChromeDriver(options);	  	
		 		         
	// 		        	 options.addArguments("--remote-debugging-port="+newPortNumber);
		 		        // options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		 		         //options.setCapability("webdriver.chrome.timeout", 30);
	  	 		         driver = new ChromeDriver(options2);
	  	 		         tdriver.set(driver); 
		 		        // driver.navigate().to("https://www.zomato.com/partners/onlineordering/orderHistory/");
		 		         
		 		         // below code is to fetch records from history page based upon yesterday and todays date and save them in db 
//		 		         history_operations hoperations = new history_operations();
//		 		         hoperations.fetch_records_from_calander(driver, connection); 
		 		         
		 		         
		 		         //Thread.sleep(2000);
	  	 		         driver.manage().window().maximize();
	  	 		         driver.navigate().to("https://www.zomato.com/partners/onlineordering/orders/");
	  	 		         logger.info("Browser is open with url");
		 		         //------------------------------------------------------------------------------------------------------------
		 		         //this is done so that if headless is equal to 0-zero then it will make headless = 1 after opening the browser 
		 		        //executeQuery2("update aggregator_setting set is_headless=1 where aggregator='Zomato'",connection);

//				            String destDir = "C:\\xampp\\htdocs\\lexerpos\\hz_aggregator\\history_files"; // Specify the destination directory
//				            Thread.sleep(7000);
//				                
////				            System.out.println("ZIP "+zipFilePath);
//				            String downloadsPath = System.getProperty("user.home") + "/Downloads"; // Adjust for your OS
//		
//				            
//				            System.out.println("downloadsPath " + downloadsPath);
//				            
//				            //below code get name of latest zip file added to download folder having name contains ["order_history"]
//				            File latestZip = getLatestZipFile(downloadsPath);
//				            System.out.println("latest Zip name " + latestZip.getAbsolutePath());
//				            
//				            if (latestZip != null) {
//				                extractZipFile(latestZip.getAbsolutePath(), destDir);
//				            }
//				            
//				            
//				          //---------------FILLO API CODE------------------------------
//  	            		 //CODE TO CONVERT .CSV TO XLSX
//					            String csvFilePath = "C:\\xampp\\htdocs\\lexerpos\\hz_aggregator\\history_files\\output.csv";
//					            String xlsxFilePath = "C:\\xampp\\htdocs\\lexerpos\\hz_aggregator\\history_files\\output.xlsx";
//					            
//					            
////					            System.out.println("str_month_To ==" + str_month_To);
////					            System.out.println("csvFilePath ==" + csvFilePath);
//					            convertCsvToXlsx(csvFilePath, xlsxFilePath);
//					            
//					            //call this method to DELETE all zipp files from downloads
//					            Thread.sleep(7000);
//					            Deletezip_files();
//				            
//						 try {	
//				    			//************CODE TO ITERATE THRU XLSX FILE AND SAVE IT IN DB*******************************************************************
//				    		    connection1 = fillo.getConnection("C:\\xampp\\htdocs\\lexerpos\\hz_aggregator\\history_files\\output.XLSX");
//				    		    query = "SELECT * FROM Sheet1"; // Use "file" as the table name
//				    		    recordset = connection1.executeQuery(query);
//				    		    System.out.println("Recrdset "+recordset.getCount());
//				    		    int k=0;
//				    		    while (recordset.next()) {
//				    		        // Access the data by column name
//				    		        String orderNumber = recordset.getField("Order ID");
//				    		        if(orderNumber.length()>4) {//this code is if order no is in excel file is less then 4 digit number or sum invalid data
//				    		        
//				    		        System.out.println(orderNumber);
//				    		        WebDriverWait wait1 = new WebDriverWait(driver,Duration.ofSeconds(7)); 
//				    		    	WebElement wb1=wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//input")));
//				    		    	if(wb1.isDisplayed()) {
//				    		    		Thread.sleep(2000);
//				    		    		wb1.click();
//				    		    		Actions actions2 = new Actions(driver);
//				    		            actions2.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform(); 
//				    		            actions2.sendKeys(Keys.DELETE).perform();
//				    		            wb1.clear(); 
//				    		    		
//				    		    		Thread.sleep(1000);
//				    		    		wb1.sendKeys(orderNumber);
//				    		    		}
//				    		    	
//				    		        } 
//				    		    	Thread.sleep(2000); 
//				    		    	System.out.println("//span[text()='"+orderNumber+"']");
//				    		    	//Now we get the desired Order ID and put that in search textbox                
//
//				    		    	//to click on Search Button
//				    		    	WebDriverWait wait1 = new WebDriverWait(driver,Duration.ofSeconds(7));
//				    		    	WebElement wb3=wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Search']")));
//				    		     	if(wb3.isDisplayed()) {
//				    		     		wb3.click();
//				    		     		String Today ="";
//				    		     		Thread.sleep(2000);
//				    		     		String Order_no2 = orderNumber.substring(orderNumber.length() - 4);
//				    		     		Right_side_execution(connection,Order_no2,orderNumber,k);
//				    		//     		driver.findElement(By.xpath("//span[@class='css-1e9u2l5']")).click();
//				    		     	}
//				    		    k++;
//				    		    }
//				    		}catch (Exception e) {
////				    			logger.error("Error is "+e.getMessage());
//				    		    e.printStackTrace();
//				    		}
				    	} 


		            catch (Exception e) {
		                System.out.println("Exception: IS " + e.getMessage());
		            }  
	         } 
				}
		}
	
	
//		    // Method to extract the ZIP file
//		    public static void extractZipFile(String zipFilePath, String destDir) {
//		        File dir = new File(destDir);
//		        if (!dir.exists()) {
//		            dir.mkdirs();
//		        }
//
//		        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
//		            ZipEntry zipEntry;
//		            while ((zipEntry = zis.getNextEntry()) != null) {
////		                File newFile = new File(destDir, zipEntry.getName());
//		            	File newFile = new File(destDir, "output.csv");
//		            	 
//		                if (zipEntry.isDirectory()) {
//		                    newFile.mkdirs();
//		                } else {
//		                    new File(newFile.getParent()).mkdirs();
//		                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
//		                        byte[] buffer = new byte[1024];
//		                        int len;
//		                        while ((len = zis.read(buffer)) > 0) {
//		                            fos.write(buffer, 0, len);
//		                        }
//		                    }
//		                }
//		                zis.closeEntry();
//		            }
//		            System.out.println("Extraction completed!");
//		        } catch (IOException e) {
//		            e.printStackTrace();
//		        }
//		    }
//
//		 
//		 
//		   // Method to get the latest ZIP file with "order_history" in its name
//		    public static File getLatestZipFile(String downloadsPath) {
//		        File downloadsDir = new File(downloadsPath);
//		        File[] zipFiles = downloadsDir.listFiles((dir, name) -> 
//		            name.toLowerCase().endsWith(".zip") && name.toLowerCase().contains("order_history")
//		        );
//
//		        if (zipFiles == null || zipFiles.length == 0) {
//		            System.out.println("No ZIP files found with 'order_history' in the Downloads folder.");
//		            return null;
//		        }
//
//		        // Find the latest ZIP file based on the last modified time
//		        File latestZip = zipFiles[0];
//		        for (File file : zipFiles) {
//		            if (file.lastModified() > latestZip.lastModified()) {
//		                latestZip = file;
//		            }
//		        }
//		        return latestZip;
//		    }
//
//		    
//		    
//
//		    //****************Method  TO CONVERT CSV TO XLSX***************************************************************
//		    public static void convertCsvToXlsx(String csvFilePath, String xlsxFilePath) {
//		        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
//		             XSSFWorkbook workbook = new XSSFWorkbook()) {
//		             
//		            XSSFSheet sheet = workbook.createSheet("Sheet1");
//		            String line;
//		            int rowNum = 0;
//
//		            while ((line = br.readLine()) != null) {
//		                String[] values = line.split(","); // Assuming comma as separator
//		                Row row = sheet.createRow(rowNum++);
//		                for (int i = 0; i < values.length; i++) {
//		                    Cell cell = row.createCell(i);
//		                    cell.setCellValue(values[i]);
//		                }
//		            }
//
//		            try (FileOutputStream fileOut = new FileOutputStream(xlsxFilePath)) {
//		                workbook.write(fileOut);
//		            }
//		            System.out.println("Conversion completed successfully!");
//
//		        } catch (IOException e) {
//		            e.printStackTrace();
//		        }
//		    }
//
//		    public void Deletezip_files() {
//		   	 // Get the user's home directory
//		       String homeDir = System.getProperty("user.home");
//		       // Construct the path to the Downloads folder
//		       String downloadsDirPath = homeDir + File.separator + "Downloads";
//		       
//		       File downloadsDir = new File(downloadsDirPath);
//		       
//		       // Check if the Downloads directory exists and is a directory
//		       if (downloadsDir.exists() && downloadsDir.isDirectory()) {
//		           // List all files in the Downloads directory
//		           File[] files = downloadsDir.listFiles();
//		           Date date = new Date();
//		           Calendar cal = Calendar.getInstance();
//
//		           // Set the Calendar time to the Date object
//		           cal.setTime(date);
//		           
//		           int year= cal.get(Calendar.YEAR);
//		           String str_year = String.valueOf(year);
//		           
//		           if (files != null) {
//		               for (File file : files) {
//		                   // Check if the file has a .zip extension
//		                   if (file.isFile() && file.getName().endsWith(".zip") && file.getName().contains(str_year)) {// it will del files having year in name
//		                       // Attempt to delete the file
//		                       if (file.delete()) {
//		                           System.out.println("Deleted: " + file.getName());
//		                       } else {
//		                           System.out.println("Failed to delete: " + file.getName());
//		                       }
//		                   }
//		               }
//		           } else {
//		               System.out.println("No files found in the Downloads directory.");
//		           }
//		       } else {
//		           System.out.println("Downloads directory does not exist.");
//		       }
//		   }
//
//		   public void Right_side_execution(Connection connection,String order_number_small, String order_no_big, int index) throws SQLException, InterruptedException, AWTException 
//		    {
//
//		    		WebDriverWait wait1 = new WebDriverWait(driver,Duration.ofSeconds(7));  
//		        	Thread.sleep(2000);  
//		        	//Date time is picked from Left side date mention on order history page.
//		        	
//			   		WebElement datetime = driver.findElement(By.xpath("//div[@data-index='"+index+"']//span[contains(text(),'|')]"));
//			   		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", datetime);
//			   		Thread.sleep(2000);
//			   		
//			       	Order_Time=datetime.getText(); 
//			       	
//			        String[] parts = Order_Time.split(" \\| ");
//			        
//			        // The second part contains the date
//			        String datePart = parts[1]; // "18 October"
//			        
//			        System.out.println(datePart); // Output: 18 October
//			        
//			        // Optionally, to format it as "October 18"
//			        String[] dateComponents = datePart.split(" ");
//			        String formattedDate = dateComponents[1] + " " + dateComponents[0]; // "October 18"
//			        
//			       	Order_Time =formattedDate;
//		        	System.out.println("Order_Time "+Order_Time);
//		        	Thread.sleep(4000);
//			        	
////		    			Hided by me on 26-september
//			        	JavascriptExecutor js2 = (JavascriptExecutor) driver;
//			        	js2.executeScript("window.scrollBy(0,350)", ""); 
//
//
//			        	
//			          System.out.println("Order No "+order_number_small);
//			          PreparedStatement statement2 = connection.prepareStatement("Select order_no from pos_integration where order_no ='" + order_number_small +"' and Order_Time='"+Order_Time+"'");
//			          ResultSet resultSet1 = statement2.executeQuery(); 
//			          
//			          System.out.println("SQlll  >>>"+"Select order_no from pos_integration where order_no ='" + order_number_small +"' and Order_Time='"+Order_Time+"'");
//			         
//			          System.out.println("SMall order "+ order_number_small);
//			          if (order_number_small !="" && resultSet1.next()==false) // && !resultSet1.getString("order_no").equals(order_no)) 
//			          {
//		 
//		//----------------- Code from above[630 - 656] Pasted here to click on Order BUtton after order no is not there in DATABAse
//			        	  
//				    		driver.findElements(By.xpath("//div[@class='css-1ken19u']")).get(0).click();//****this is order button on Right side of order details
//				    		Thread.sleep(6000);
//						    Robot robot = new Robot();
//
//					        // Simulate pressing the Escape key
//					        robot.keyPress(KeyEvent.VK_ESCAPE);  // Press Escape
//					        robot.keyRelease(KeyEvent.VK_ESCAPE);
////					        
//					        
//				    		System.out.println("Order IIIDD "+order_number_small);
//				    		//-------This IFRAME is generated after clicking on Order Button------------------------
////				        	WebElement Iframe=driver.findElement(By.xpath("//iframe[contains(@id, 'BILLorder-history-card_"+order_no_big+"')]"));
//				    		WebElement Iframe=driver.findElement(By.xpath("//iframe[@id='BILLorder-history-card_"+order_no_big+"']"));
//				        	System.out.println("iFRAME "+ "//iframe[@id= 'BILLorder-history-card_"+order_no_big+"']");
//				        	
//				        	//--------------------------- 
//				        	Thread.sleep(3000); 
//				        	driver.switchTo().frame(Iframe); 
//				        	Thread.sleep(2000);
//				        	 
//				        	//below is to get the html of IFrame
//				        	order_view=driver.getPageSource();//.findElement(By.xpath("//div[text()='ID: ']//child::span//span//ancestor::div[@class='css-12f0rae']//following-sibling::div//div[text()='KOT']//preceding-sibling::iframe")).getAttribute("innerHTML");
//				//        	order_view = order_view.replace("\"", "#").replace("'", "@").replaceAll("\\s+", " ");
//				        	order_view = order_view.replace("'", "@").replaceAll("\\s+", " ");
//				        	
//				        	
//				        	System.out.println("Order_View ======"+order_view);	            	
//				        	
//				        	driver.switchTo().defaultContent();
//			 	        	 
//			        	    System.out.println("Insert query above-->>>"+"INSERT INTO pos_integration (order_no,order_view,Order_Time,entered) VALUES ('" + order_number_small + "','" + order_view +"','"+Order_Time+"',"+1+")");
//		  
//			        	   //-----------------------------------------------------------------------------------------------------------------------------------------------------	        	    
//			        	  // ---------------------------This is when html is not created properly
//
//			        	  
//			        	  if(order_view.equals("<html><head></head><body></body></html>")==false) {
//			            	String insertQuery ="INSERT INTO pos_integration (order_no,order_view,Order_Time,entered) VALUES ('" + order_number_small + "','" + order_view +"','"+Order_Time+"',"+1+")";                    
//			                System.out.println("ISQL ===="+insertQuery);
//			            	executeQuery2(insertQuery,connection);
//			                 
//			                resultSet1.close();
//			                statement2.close();
//			    		}
//		    		} 
//		    }
		    

		 
	 public static void executeQuery2(String query,Connection connection) throws SQLException 
		//EARLIER  --->public static void executeQuery2(String query, Connection connection) throws SQLException 
		{
		 try {
		 	
		     if(connection == null) 
		     {	            	
			        	 connection = DriverManager.getConnection(url, username, password);
			             PreparedStatement preparedStatement = connection.prepareStatement(query);
			             preparedStatement.executeUpdate();
			             //System.out.println("query "+query +" connection counter = "+counter++);		             
		     }
		     else {

		     			PreparedStatement preparedStatement = connection.prepareStatement(query);
			            preparedStatement.executeUpdate();
			            //System.out.println("query "+query +" connection counter = "+counter++);
			            preparedStatement.close();
		     }		
		 }
		 catch(Exception e) {
			   System.out.println("EXCEPTION "+e.getLocalizedMessage());
			   e.printStackTrace();
		 }
		}	

 
    // Method to execute the PowerShell command
    private static void executePowerShellCommand(String command) throws IOException {
        // Execute the PowerShell command using ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true); // Combine standard output and error

        // Start the process and capture the output
        Process process = processBuilder.start();
        printCommandOutput(process);

        // Wait for the process to complete
        try {
            int exitCode = process.waitFor();
            System.out.println("PowerShell command executed with exit code: " + exitCode);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to print the command output
    private static void printCommandOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);  // Print each line of the output
        }
    }
	
	
	 // Method to move the browser window to a specific location
    public static void moveWindowTo(WebDriver driver, int x, int y) {
        // Use Window.setPosition() method to move the window to new (x, y) position
        driver.manage().window().setPosition(new Point(x, y));
    }
	
	

	private static int generateRandomPort(Connection connection) throws SQLException {
        Random random = new Random();
        int port;
        
//        do {
            // Generate a random port number
            port = random.nextInt((MAX_PORT - MIN_PORT) + 1) + MIN_PORT;
//        }
//        while (portExists_in_DB(connection, port));  // Check if the port exists in the database
        
        return port;
    }
	
	public static boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // Port is available, the server socket was successfully created.
            return true;
        } catch (IOException e) {
            // Port is occupied, an exception was thrown when trying to bind to the port.
            return false;
        }
    }
	
	private boolean isHeadless(WebDriver driver2) {
		return false;
	}

//	commented by me on 25 mar
//	public static synchronized WebDriver getDriver() {
//	return tdriver.get();
//		}


	@AfterMethod
    public void cleanup() throws InterruptedException {
        // Nullify the resource to allow GC to reclaim memory
		
	}

}