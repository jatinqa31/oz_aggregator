package testBase;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class history_operations extends TestBase_Zorders {
    Fillo fillo = new Fillo();
    com.codoid.products.fillo.Connection connection1 = null;
    Recordset recordset = null;
    
    String Order_Time="";
    String order_view="";
	String query="";
	
	public static String url = "jdbc:mysql://localhost:3306/lexerpos_db";
	public static String username = "root";
	public static String password = "";
	
	String cal_Day_From="01";
	String cal_Day_To="10";//upto 10 days record can be viewd in History page
	String file_Day_From="";
	String file_Day_To="";
	String Month_From="";
	String Month_To="";
	
	public void fetch_records_from_calander(WebDriver driver,Connection connection) throws InterruptedException {
        
		System.out.println("Fetch_records_from_calander ");
	try{
}catch(Exception e)
	{	
		String msg=e.getLocalizedMessage();
		System.out.println("ERROR MESSAGE " + msg);
		if(msg.contains("It may have died") || msg.contains("element click intercepted") || msg.contains("element not interactable"))
		{
	    	try {
	    		
				close_browser();
		    	getBrowser("chrome");
		    	//logger.info("error on new_order_arrived 1 browser CLOSED initiated" + msg);
		    	System.out.println("POP1 " + e.getMessage());
		        //((JavascriptExecutor) driver).executeScript("location.reload(true);");
	       }catch (Exception e1) { 
				e1.printStackTrace();
				msg = e1.getLocalizedMessage().split("Build info:")[0];
				System.out.println("pop2 " + e1.getMessage());
			}

		}
	}
}
	
	public void close_browser() throws IOException {	
		try {
			
	        // Create a ProcessBuilder instance with the taskkill command
	        ProcessBuilder processBuilder = new ProcessBuilder("taskkill", "/F", "/IM", "chromedriver.exe", "/T");
	        
	        // Start the process
	        Process process = processBuilder.start();
	        
	        // Wait for the process to finish executing
	        int exitCode = process.waitFor();
	        
	        if (exitCode == 0) {
	            System.out.println("All chromedriver processes have been successfully terminated.");
	        } else {
	            System.out.println("There was an issue terminating chromedriver processes.");
	        }
	    }	catch (IOException | InterruptedException e) 
		{
	        e.printStackTrace();
	    }
		
	  int targetPort = portNo;  // Specify the debugging port you want to target
	  
	  try {
	      // Get the list of network connections and processes
	      ProcessBuilder processBuilder = new ProcessBuilder("netstat", "-ano");
	      Process process = processBuilder.start();
	      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	      String line;

	      boolean isChromeOnPort = false;
	      String chromePID = "";

	      // Loop through the network connections and look for the port
	      while ((line = reader.readLine()) != null) {
	          if (line.contains(":" + targetPort) || line.contains("chrome.exe") || line.contains("java.exe")) {
	              // Check if the line contains the target port
	              String[] tokens = line.trim().split("\\s+");
	              chromePID = tokens[tokens.length - 1];  // Last token is the PID
	              isChromeOnPort = true;
	              break;
	          }
	      }
	   
//	      try 
//	      {
	    	  
	          // Run the tasklist command to list all processes with memory usage
//	          processBuilder = new ProcessBuilder("tasklist", "/FI", "IMAGENAME eq chrome.exe", "/FO", "CSV", "/NH");
//	          process = processBuilder.start();
//	          reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
////	          String line;
//
//	          // Read the output of the tasklist command
//	          while ((line = reader.readLine()) != null) {
//	              // Output in CSV format: "chrome.exe", "1234", "100,000 K", etc.
//	              String[] processInfo = line.split("\",\"");
//	              if (processInfo.length > 3) {
//	                  String processName = processInfo[0].replace("\"", ""); 
//	                  String pid = processInfo[1].replace("\"", "");
//	                  String memoryUsage = processInfo[4].replace("\"", "");
//
//	                  System.out.println("Process: " + processName);
//	                  System.out.println("PID: " + pid);
//	                  System.out.println("Memory Usage: " + memoryUsage);
//	              }
//	          }
//	      } catch (IOException e) {
//	          System.out.println("E1 "+e.getLocalizedMessage());
//	      }

	      try {    
	      if (isChromeOnPort) {
	          // Now kill the Chrome process associated with the PID
	          System.out.println("Chrome is using port " + targetPort + ". Closing it...");

	          // Use the PID to terminate the Chrome process
	          Process killProcess = Runtime.getRuntime().exec("taskkill /F /PID " + chromePID);
	          killProcess.waitFor();

	          System.out.println("Chrome process with PID " + chromePID + " has been closed.");
	      } else {
	          System.out.println("No Chrome process is using port " + targetPort);
	      }
	  } catch (Exception e) {
	      System.out.println("E2 "+e.getLocalizedMessage());
	  }

	}catch (Exception e) 
	  {
	    System.out.println("E3 "+e.getLocalizedMessage());
	  }
	}
	
	
	
//	*#07# - 2 check the radiation in mobile phone
//	if greater the >1.2 then it is not good
	
	// Method to extract the ZIP file
    public void extractZipFile(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
//                File newFile = new File(destDir, zipEntry.getName());
            	File newFile = new File(destDir, "output.csv");
            	 
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
            System.out.println("Extraction completed!");
        } catch (IOException e) 
        {
        	String msg=e.getLocalizedMessage();
    		if(msg.contains("It may have died."))
    		{
    	    	try {
    	    		
    				close_browser();
    		    	getBrowser("chrome");
    		    	//logger.info("error on new_order_arrived 1 browser CLOSED initiated" + msg);
    		    	System.out.println("POP1 " + e.getMessage());
    		        //((JavascriptExecutor) driver).executeScript("location.reload(true);");
    	       }catch (Exception e1) { 
    				e1.printStackTrace();
    				msg = e1.getLocalizedMessage().split("Build info:")[0];
    				System.out.println("pop2 " + e1.getMessage());
    			}
    		}
        }
    }

 
 
   // Method to get the latest ZIP file with "order_history" in its name
    public static File getLatestZipFile(String downloadsPath) {
        File downloadsDir = new File(downloadsPath);
        File[] zipFiles = downloadsDir.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".zip") && name.toLowerCase().contains("order_history")
        );

        if (zipFiles == null || zipFiles.length == 0) {
            System.out.println("No ZIP files found with 'order_history' in the Downloads folder.");
            return null;
        }

        // Find the latest ZIP file based on the last modified time
        File latestZip = zipFiles[0];
        for (File file : zipFiles) {
            if (file.lastModified() > latestZip.lastModified()) {
                latestZip = file;
            }
        }
        return latestZip;
    }

    
    

    //****************Method  TO CONVERT CSV TO XLSX***************************************************************
    public static void convertCsvToXlsx(String csvFilePath, String xlsxFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             XSSFWorkbook workbook = new XSSFWorkbook()) {
             
            XSSFSheet sheet = workbook.createSheet("Sheet1");
            String line;
            int rowNum = 0;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assuming comma as separator
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < values.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(values[i]);
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(xlsxFilePath)) {
                workbook.write(fileOut);
            }
            System.out.println("Conversion completed successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Deletezip_files() {
   	 // Get the user's home directory
       String homeDir = System.getProperty("user.home");
       // Construct the path to the Downloads folder
       String downloadsDirPath = homeDir + File.separator + "Downloads";
       
       File downloadsDir = new File(downloadsDirPath);
       
       // Check if the Downloads directory exists and is a directory
       if (downloadsDir.exists() && downloadsDir.isDirectory()) {
           // List all files in the Downloads directory
           File[] files = downloadsDir.listFiles();
           Date date = new Date();
           Calendar cal = Calendar.getInstance();

           // Set the Calendar time to the Date object
           cal.setTime(date);
           
           int year= cal.get(Calendar.YEAR);
           String str_year = String.valueOf(year);
           
           if (files != null) {
               for (File file : files) {
                   // Check if the file has a .zip extension
                   if (file.isFile() && file.getName().endsWith(".zip") && file.getName().contains(str_year)) {// it will del files having year in name
                       // Attempt to delete the file
                       if (file.delete()) {
                           System.out.println("Deleted: " + file.getName());
                       } else {
                           System.out.println("Failed to delete: " + file.getName());
                       }
                   }
               }
           } else {
               System.out.println("No files found in the Downloads directory.");
           }
       } else {
           System.out.println("Downloads directory does not exist.");
       }
   }

   public void Right_side_execution(Connection connection,String order_number_small, String order_no_big, int index,WebDriver driver) throws SQLException, InterruptedException, AWTException 
    {

    		WebDriverWait wait1 = new WebDriverWait(driver,Duration.ofSeconds(7));  
        	Thread.sleep(2000);  
        	//Date time is picked from Left side date mention on order history page.
        	
	   		WebElement datetime = driver.findElement(By.xpath("//div[@data-index='"+index+"']//span[contains(text(),'|')]"));
	   		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", datetime);
	   		Thread.sleep(2000);
	   		
	       	Order_Time=datetime.getText(); 
	       	
	        String[] parts = Order_Time.split(" \\| ");
	        
	        // The second part contains the date
	        String datePart = parts[1]; // "18 October"
	        
	        System.out.println(datePart); // Output: 18 October
	        
	        // Optionally, to format it as "October 18"
	        String[] dateComponents = datePart.split(" ");
	        String formattedDate = dateComponents[1] + " " + dateComponents[0]; // "October 18"
	        
	       	Order_Time =formattedDate;
        	System.out.println("Order_Time "+Order_Time);
        	Thread.sleep(4000);
	        	
//    			Hided by me on 26-september
	        	JavascriptExecutor js2 = (JavascriptExecutor) driver;
	        	js2.executeScript("window.scrollBy(0,350)", ""); 


	        	
	          System.out.println("Order No "+order_number_small);
	          PreparedStatement statement2 = connection.prepareStatement("Select order_no from pos_integration where order_no ='" + order_number_small +"' and Order_Time='"+Order_Time+"'");
	          ResultSet resultSet1 = statement2.executeQuery(); 
	          
	          System.out.println("SQlll  >>>"+"Select order_no from pos_integration where order_no ='" + order_number_small +"' and Order_Time='"+Order_Time+"'");
	         
	          System.out.println("SMall order "+ order_number_small);
	          if (order_number_small !="" && resultSet1.next()==false) // && !resultSet1.getString("order_no").equals(order_no)) 
	          {
 
//----------------- Code from above[630 - 656] Pasted here to click on Order BUtton after order no is not there in DATABAse
	        	  
		    		driver.findElements(By.xpath("//div[@class='css-1ken19u']")).get(0).click();//****this is order button on Right side of order details
		    		Thread.sleep(6000);
				    Robot robot = new Robot();

			        // Simulate pressing the Escape key
			        robot.keyPress(KeyEvent.VK_ESCAPE);  // Press Escape
			        robot.keyRelease(KeyEvent.VK_ESCAPE);
//			        
			        
		    		System.out.println("Order IIIDD "+order_number_small);
		    		//-------This IFRAME is generated after clicking on Order Button------------------------
//		        	WebElement Iframe=driver.findElement(By.xpath("//iframe[contains(@id, 'BILLorder-history-card_"+order_no_big+"')]"));
		    		WebElement Iframe=driver.findElement(By.xpath("//iframe[@id='BILLorder-history-card_"+order_no_big+"']"));
		        	System.out.println("iFRAME "+ "//iframe[@id= 'BILLorder-history-card_"+order_no_big+"']");
		        	
		        	//--------------------------- 
		        	Thread.sleep(3000); 
		        	driver.switchTo().frame(Iframe); 
		        	Thread.sleep(2000);
		        	 
		        	//below is to get the html of IFrame
		        	order_view=driver.getPageSource();//.findElement(By.xpath("//div[text()='ID: ']//child::span//span//ancestor::div[@class='css-12f0rae']//following-sibling::div//div[text()='KOT']//preceding-sibling::iframe")).getAttribute("innerHTML");
		//        	order_view = order_view.replace("\"", "#").replace("'", "@").replaceAll("\\s+", " ");
		        	order_view = order_view.replace("'", "@").replaceAll("\\s+", " ");
		        	
		        	
		        	System.out.println("Order_View ======"+order_view);	            	
		        	
		        	driver.switchTo().defaultContent();
	 	        	 
	        	    System.out.println("Insert query above-->>>"+"INSERT INTO pos_integration (order_no,order_view,Order_Time,entered) VALUES ('" + order_number_small + "','" + order_view +"','"+Order_Time+"',"+1+")");
  
	        	   //-----------------------------------------------------------------------------------------------------------------------------------------------------	        	    
	        	  // ---------------------------This is when html is not created properly

	        	  
	        	  if(order_view.equals("<html><head></head><body></body></html>")==false) {
	            	String insertQuery ="INSERT INTO pos_integration (order_no,order_view,Order_Time,entered) VALUES ('" + order_number_small + "','" + order_view +"','"+Order_Time+"',"+1+")";                    
	                System.out.println("ISQL ===="+insertQuery);
	            	executeQuery2(insertQuery,connection);
	                 
	                resultSet1.close();
	                statement2.close();
	    		}
    		} 
    }

   
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
	
}
