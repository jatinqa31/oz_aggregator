package login_page;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
//import java.io.InputStreamReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
//import java.net.URL;
//import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.HashPrintRequestAttributeSet;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import testBase.TestBase_Zorders;         
import login_page.*;

public class oz_test extends TestBase_Zorders
{	
  String otp="";
  String mobile_no="";
  String Login_btn_label="";
  String send_otp_btn_label="";
  String order_no="";
  String Order_view="";//This is used for view of order, after fetching the Main DIV
  String kot_view="";  
  String order_view="";//this is the xpath
  String invoice_view="";
  String insertQuery="";
  String popup_xpath=""; 
  int k = 0;
  String Order_time="";
  String phoneno;
  String phoneno2;
  
  String okay_btn="";
  String already_added_html="";
  
  boolean allow=true; 
  int count=0;
  public static final Logger logger = LogManager.getLogger(login_page.oz_test.class);    
	
	
	public static boolean is_new_order=false;
	
	public static Connection connection = DatabaseConnectionPool.getConnected();
	
	public List<WebElement> ls = new ArrayList();

	
	String fetched_html_view = "";
	String fetched_Outer_html_view = ""; 
	String fetched_new_order_view = "";
	String fetched_popup_html_view = "";
	//public WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(3));
	
	public String updateQuery ="";
	public String xpath2 = "";   
	public String pageCss="";
	public WebElement popup_arrived= null;

	public String popup_view1= "";// these variables are to fetch CSS- ONLY NOT THE COMPLETE XPATH
	public String html_view1="";
	public String new_order_view1="";
	
	public String popup_view= "";
	public String html_view="";
	public String new_order_view="";
	
	int sum=0;;
//	private static final Marker markerA = MarkerManager.getMarker("A");
//  private static final Marker markerB = MarkerManager.getMarker("B");
	

//this dataProvider is used to pass parameters to a function to fetch code of Okay_BTN DIV 
@DataProvider(name = "html_handler")
public Object[][] html_handlers() throws SQLException 
{	
		//Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lexerpos_db", "root", "");
		//Get a connection from the pool
		
		//Connection connection = DatabaseConnectionPool.getConnected();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(1)); 
		
		return new Object[][] {
			{wait}
		};
}

//Approach of invocation count is adopted because infinite While(true) leads to errors in program like {out of memory} [I GUESS] but i am not sure

@Test(dataProvider = "html_handler", priority = 0, enabled = true, invocationCount = Integer.MAX_VALUE)//, threadPoolSize = 1)
public void html_handler(WebDriverWait wait)   		 
{		
//			test = extent.createTest("Infinite Loop Test");
			try {		 
		    PreparedStatement preparedStatement = connection.prepareStatement("select * from xpaths where description='html_view' or description='popup_view' or description='new_order_view'");    
		   	ResultSet resultSet0 = preparedStatement.executeQuery(); 
		    if(resultSet0.next()==true)
		    {
		    	html_view1 = resultSet0.getString("xpath"); // this var contains value of xpath column having css in it.
		    	html_view = "//div[@class=\"" + html_view1 + "\"]"; // this var is used to convert above variable to xpath
		    	resultSet0.next();
		    	popup_view1 = resultSet0.getString("xpath");
		    	popup_view = "//div[@id=\"" + popup_view1 + "\"]";
		    	resultSet0.next();
		    	new_order_view1 = resultSet0.getString("xpath");
		    	new_order_view = "//div[@class=\"" + new_order_view1+"\"]";
		    }
		    
		    
			if (resultSet0 != null) 
			{
		         resultSet0.close();
	    	}
		     
			if (preparedStatement != null) 
		    {
		    	 preparedStatement.close();
		    }
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
//			LocalDate today2 = LocalDate.now();
			
//			This is to merge all xpaths in single xpath
			 
//			xpath2 = "//div[@class='css-m5eqp0'] | //div[@id='modal'] | //div[@class='css-dwgk5r']";
			
			xpath2 = html_view +" | "+ popup_view + " | "+ new_order_view;	
			
			//this code is to update [created_at] column every time this is because using code, Mysql donot update records if 
			//records in updated columns IS same as it was there in previous update.
			
			LocalDateTime currentTime = LocalDateTime.now();
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		    String formattedTimestamp = currentTime.format(formatter);
		    
			try {

				//this ls will have list of all elements with wait on it.
				ls = (List<WebElement>) wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpath2)));
				
				//System.out.println("List Size " + ls.size());
				
				if(ls.size()>0) {

				for(int i=0;i<=ls.size()-1;i++)	
					
				{	 
					
//					WebElement popup_arrived=ls.get(i);
					popup_arrived = ls.get(i);
								
					if(popup_arrived.isDisplayed()) {		

		    		fetched_Outer_html_view = popup_arrived.getAttribute("outerHTML");
		    				    		
// 					This for HTML for popup_view
//		    		System.out.println("popup_view1 "+ popup_view1);
//		    		if(fetched_Outer_html_view.contains("id=\"modal\"")){//| fetched_Outer_html_view.contains("id='modal'")) {
	    			if(fetched_Outer_html_view.contains(popup_view1))
	    			{		
		    			//fetched_popup_html_view = popup_arrived.getAttribute("innerHTML");
		    			JavascriptExecutor jsExecutor1 = (JavascriptExecutor) driver;
		    			
		    			// Get all the styles from the page (inline, embedded, or external)
		    			String pageCss1 = (String) jsExecutor1.executeScript(
		    			    "var styles = ''; " +
		    			    "var sheets = document.styleSheets; " +
		    			    "for (var i = 0; i < sheets.length; i++) {" +
		    			    "  var rules = sheets[i].rules || sheets[i].cssRules;" +
		    			    "  for (var j = 0; j < rules.length; j++) {" +
		    			    "    styles += rules[j].cssText + '\\n';" +
		    			    "  }" +
		    			    "} return styles;"
		    			);
		    			
		    			pageCss1="<style>" + pageCss1 + "</style>";			
		    			
//		    			fetched_popup_html_view = popup_arrived.getAttribute("innerHTML");
		    			fetched_popup_html_view = popup_arrived.getAttribute("innerHTML");//commented by me 
		    			
		    			fetched_popup_html_view = pageCss1 + fetched_popup_html_view; // this is 2 concatenate css with  html created
		    			
		    			fetched_popup_html_view = fetched_popup_html_view.replace("'", "''");
		    			
		    			
		    			PreparedStatement statement3 = connection.prepareStatement("select * from popup_handler where aggregator='Zomato'");
		    			ResultSet resultSet3 = statement3.executeQuery(); 
		    			if(resultSet3.next()) {
		    			updateQuery ="update popup_handler SET created_at ='"+formattedTimestamp +"', popup_view='" + fetched_popup_html_view + "' where aggregator='Zomato'";
		    			
		    			executeQuery2(updateQuery,connection);
		    			fetched_popup_html_view="";
		    			
		    			
		    			if (resultSet3 != null) 
		    			{
		    			     resultSet3.close();
		    			}
		    			if (statement3 != null) 
		    			{
		    			     statement3.close();
		    			}
		    			
		    			} 
		    			
		    			statement3 = connection.prepareStatement("select * from popup_handler where aggregator='Zomato'");
		    			resultSet3 = statement3.executeQuery(); 
		    			if(resultSet3.next()==true) {
		    			String action_text=resultSet3.getString("action_text");
		    			
		    			//popup_view is empty by me on 20-march-2025
		    			updateQuery ="update popup_handler SET created_at ='"+formattedTimestamp +"', popup_view ='', action_text='pop' where aggregator='Zomato'";		    				
//		    			updateQuery ="update popup_handler SET created_at ='"+formattedTimestamp +"', action_text='pop' where aggregator='Zomato'";		    				
		    			
		    			if (action_text != "pop") {
//		    				update_operation(action_text, updateQuery, wait);
		    				executeWithTimeout(action_text, updateQuery, wait);
		    			}
		    			fetched_popup_html_view="";
		    			
		    			}
		    			if (resultSet3 != null) {
		    				resultSet3.close();
		    			}
		    			if (statement3 != null) {
		    				statement3.close();
		    			}
		    			
		    			// fetched_Outer_html_view = null; //on 29-march
		    			// removed by me on 18-march popup_arrived=null;	
	    			}
	    			else 
	    			{	
	    				//removed on 4/April/2025	    				
	    				//executeQuery2("update popup_handler set popup_view='' where aggregator='Zomato'",connection);
	    			}
		    		
	    			fetched_popup_html_view = null; //removed by me on 26-march-2025	    			
	    			//	css of HTML View
		    					
	    			
	    			if(fetched_Outer_html_view.contains(html_view1) && is_new_order == false) {
//		    		if(fetched_Outer_html_view.contains("class=\"css-m5eqp0\"") && is_new_order==false) {

		    			// System.out.println("is_new_order "+formattedTimestamp+" = "+is_new_order);
		    			// if(fetched_Outer_html_view.contains(html_view)) {		    			
		    			JavascriptExecutor jsExecutor1 = (JavascriptExecutor) driver;
		    			
		    			// Get all the styles from the page (inline, embedded, or external)
		    			String pageCss1 = (String) jsExecutor1.executeScript(
		    			    "var styles = ''; " +
		    			    "var sheets = document.styleSheets; " +
		    			    "for (var i = 0; i < sheets.length; i++) {" +
		    			    "  var rules = sheets[i].rules || sheets[i].cssRules;" +
		    			    "  for (var j = 0; j < rules.length; j++) {" +
		    			    "    styles += rules[j].cssText + '\\n';" +
		    			    "  }" +
		    			    "} return styles;"
		    			);
		    			
		    			//System.out.println("Page CSS----->: \n" +"<html><style>"+ pageCss); 
		    			pageCss1="<style>"+ pageCss1+"</style>";			
		    			
		    			
//			    		fetched_html_view = popup_arrived.getAttribute("innerHTML");
		    			fetched_html_view = popup_arrived.getAttribute("innerHTML");
		    			
						fetched_html_view = pageCss1 + fetched_html_view ; // this is 2 concatenate css with  html created
						
						fetched_html_view = fetched_html_view.replace("'", "''");
						
						pageCss1 = null;//making it null after use in loop
						
						//if(is_new_order == false) { //31-march -2025
				           
				       	PreparedStatement statement1 = connection.prepareStatement("select * from popup_handler where aggregator='Zomato'");
				       	ResultSet resultSet1 = statement1.executeQuery(); 
				        if(resultSet1.next()==true) 
				        {
				        	updateQuery ="update popup_handler SET created_at ='"+formattedTimestamp +"', html_view = '" + fetched_html_view + "' where aggregator='Zomato'";
				    		executeQuery2(updateQuery,connection);
				        }
				        
				        fetched_html_view="";
				        
			    		if (resultSet1 != null) {
			 	            resultSet1.close();
			 	            resultSet1=null;
			 	        }
			    		
			 	        if (statement1 != null) {
			 	            statement1.close();
			 	            statement1=null;
			 	        }
			 	        

				       	PreparedStatement statement3 = connection.prepareStatement("select * from popup_handler where aggregator='Zomato'");
				       	ResultSet resultSet3 = statement3.executeQuery(); 
				        if(resultSet3.next()==true) 
				        {	
				        	String action_text=resultSet3.getString("action_text");
				        	//popup_view is empty by me on 20-march-2025
			        		updateQuery ="update popup_handler SET popup_view = '',action_text = 'pop' where aggregator='Zomato'";
//			        		updateQuery ="update popup_handler SET action_text='pop' where aggregator='Zomato'";// popup_view removed on 26-march-2025
			        		if (action_text!="pop") {
//								update_operation(action_text, updateQuery, wait);
								executeWithTimeout(action_text, updateQuery, wait);
			        		}
				        	fetched_html_view = "";
				        }
			
			
				        if (resultSet3 != null) 
					        {
				 	            resultSet3.close();
				 	            resultSet3=null;
				 	        }
			 	        if (statement3 != null) 
				 	        {
				 	            statement3.close();
				 	            statement3=null;
				 	        }
						//}
						//fetched_Outer_html_view = null;
					}	
			
//						logger.info("html_handler ls content "+ls.get(0));
//						ls.clear();
						fetched_html_view="";
//					}
				
					// New Order arrived code is below ----------------------------------------------------------------------
						
					if(fetched_Outer_html_view.contains(new_order_view1))
//		    		if(fetched_Outer_html_view.contains("class=\"css-wn1wek\""))//css-dwgk5r  old one new_order css-8sww3h | css-wn1wek
		    		{
		    			
//						if(fetched_Outer_html_view.contains(new_order_view)) 
					
		    			fetched_new_order_view = popup_arrived.getAttribute("outerHTML");
//		    			fetched_new_order_view = popup_arrived.getAttribute("outerHTML");
		    			
		    			//removed on 18-march 2025
		    			
//		    			if(popup_arrived.isDisplayed()) {
		    				
		    				is_new_order = true;		
		    				
		    				// Use JavaScriptExecutor to get the CSS styles of the page
		    				JavascriptExecutor jsExecutor1 = (JavascriptExecutor) driver;
		    				
		    				// Get all the styles from the page (inline, embedded, or external)
		    				pageCss = (String) jsExecutor1.executeScript(
		    						"var styles = ''; " +
		    								"var sheets = document.styleSheets; " +
		    								"for (var i = 0; i < sheets.length; i++) {" +
		    								"  var rules = sheets[i].rules || sheets[i].cssRules;" +
		    								"  for (var j = 0; j < rules.length; j++) {" +
		    								"    styles += rules[j].cssText + '\\n';" +
		    								"  }" +
		    								"} return styles;"
		    						);
		    				
		    				//System.out.println("Page CSS----->: \n" +"<html><style>"+ pageCss);
		    				pageCss="<style>"+ pageCss +"</style>";			
		    				
		    				//	fetched_html_view = popup_arrived.getAttribute("innerHTML");
		    				
		    				// removed by me on 18-march-2025 below 2 lines
		    				
//		    				JavascriptExecutor js = (JavascriptExecutor) driver;
//		    				fetched_new_order_view = (String) js.executeScript("return arguments[0].outerHTML;", popup_arrived);
		    				

		    				//fetched_html_view = fetched_html_view.replace("\"", "\"\"").replace("'", "''").replaceAll("\\s+", " ");// this is to replace single and double quotes in sql
		    				
		    				fetched_new_order_view = pageCss + fetched_new_order_view;// this is 2 concatenate css with  html created

		    				fetched_new_order_view = fetched_new_order_view.replace("'", "''"); 
		    				
		    				//System.out.println("new order arrives " + fetched_new_order_view);
		    				
		    				pageCss=null;
		    				
		    				//this query updates the record and SET created_at ='"+formattedTimestamp +"', popup_view EMPTY AFTER CLICKING ON SOME ELEMENT ON POPUP
		    				PreparedStatement statement3 = connection.prepareStatement("select * from popup_handler where aggregator='Zomato'");
		    				ResultSet resultSet3 = statement3.executeQuery(); 
		    				if(resultSet3.next()==true) {
		    					
		    					updateQuery = "update popup_handler SET created_at ='"+formattedTimestamp +"', html_view='" + fetched_new_order_view + "' where aggregator='Zomato'";
		    					executeQuery2(updateQuery,connection);
		    					 
		    					if (resultSet3 != null) {
		    						resultSet3.close();
		    						resultSet3=null;
		    					}
		    					if (statement3 != null) {
		    						statement3.close();
		    						statement3=null;
		    					}
		    					
		    				} 
		    				
		    				statement3 = connection.prepareStatement("select * from popup_handler where aggregator='Zomato'");
		    				resultSet3 = statement3.executeQuery(); 
		    				if(resultSet3.next()==true) {
		    					
		    					String action_text = resultSet3.getString("action_text");
		    					
		    					updateQuery ="update popup_handler SET created_at ='"+formattedTimestamp +"', popup_view='', action_text='pop' where aggregator='Zomato'";
//		    					updateQuery ="update popup_handler SET created_at ='"+formattedTimestamp +"', action_text='pop' where aggregator='Zomato'";
		    					
		    					if (action_text!="pop") {
//		    						update_operation(action_text, updateQuery, wait);
		    						executeWithTimeout(action_text, updateQuery, wait);
		    					}
		    					
		    					fetched_new_order_view="";//changed from fetched_html_view to fetched_new_order_view
		    				}
		    				
			    				if (resultSet3 != null) {
			    					resultSet3.close();
			    					resultSet3=null;
			    				}
			    				if (statement3 != null) {
			    					statement3.close();
			    					statement3=null;
			    				}
		    				
		    				is_new_order = false; 		//ADDED @ Last on 1st- April -2025
		    				fetched_new_order_view="";
		    				
		    				//fetched_Outer_html_view = null; //on 31-march-2025
		    		}
		    		else 
		    		{
		    				is_new_order=false;
		    		}
		        							
		        		LocalDate today3 = LocalDate.now();
		        		
		        		updateQuery="";
				}
				
				}
				ls.clear();
				popup_arrived=null; //on 18-march kol
		}
	}catch(Exception e) 
	{
		String msg = e.getLocalizedMessage().split("Build info:")[0]; 
		
		//	logger.info("error on html_handler 1 "+msg);
		//	System.out.println("error on html_handler 1 "+e.getLocalizedMessage());
		
	    if(msg.contains("java.util.concurrent.TimeoutException") || msg.contains("It may have died") || msg.contains("tab crashed") || msg.contains("invalid session id")) { 
		    System.out.println("error on html_handler 1 "+e.getLocalizedMessage());
	    	System.out.println("\"java.util.concurrent.TimeoutException\") || msg.contains(\"It may have died\"), || tab crashed");
	    	
	    	if(msg.contains("It may have died") || msg.contains("tab crashed") || msg.contains("invalid session id"))
	    	{
	    	try {
				close_browser();
		    	getBrowser("chrome");
		    	logger.info("error on new_order_arrived 1 browser CLOSED initiated" + msg);
		    	System.out.println("POP1 " + e.getMessage());
		        //((JavascriptExecutor) driver).executeScript("location.reload(true);");
	       }catch (Exception e1) 
	    	{ 
				e1.printStackTrace();
				msg = e1.getLocalizedMessage().split("Build info:")[0];
				System.out.println("pop2 " + e1.getMessage());
			}
	    	}
    	}
	}
}
			
//------------------ Below code is for SWIGGY Orders

public void update_operation(String action_text, String sql, WebDriverWait wait) {
	try { 
		
		LocalDateTime currentTime = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String formattedTimestamp = currentTime.format(formatter);

	    
		int size=0;
		if (action_text != null && !action_text.equals("pop") && !action_text.isEmpty() && !action_text.contains("cp"))
		{
		 
		//	System.out.println("Action _text = " + action_text);	
		
		List<WebElement> ls = (List<WebElement>) wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(action_text)));
 
		if(ls.size()>0) {
			
			for(int i=0;i<=ls.size()-1;i++)
			{
		
				WebElement Action_performed = wait.until(ExpectedConditions.visibilityOf(ls.get(i))); //visibility of WebElement
//				WebElement Action_performed = wait.until(ExpectedConditions.elementToBeClickable(ls.get(i))); // visibility of WebElement
		
				 
				if(Action_performed.isDisplayed())
				{
					
					Action_performed.click();		
					//System.out.println("Action_Performed_Text "+Action_performed.getText());
					
				if(Action_performed.getText().equals("ORDER")) // this is to check the TEXT IN button is "Order"
				{
							System.out.println("INSIDE ORDER");

							System.out.println("ORDER "+ action_text + "//preceding::iframe[1]");
							
//							List<WebElement> List_IFRAME = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(action_text + "//preceding::iframe | " + action_text + "//following::iframe")));
							List<WebElement> List_IFRAME = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(action_text + "//preceding::iframe[1]")));// | " + action_text + "//following::iframe")));
							System.out.println("List_Iframe_Size "+ List_IFRAME.size());
								
								for (int j=0;j<=List_IFRAME.size()-1;j++)
								{

									String id=List_IFRAME.get(j).getAttribute("id");

//									if(List_IFRAME.get(j) != null && List_IFRAME.get(j).getAttribute("style").contains("none;") && id.contains("BILL"))
									if(List_IFRAME.get(j) != null && List_IFRAME.get(j).getAttribute("style").contains("none;"))
									{
										try {

											
											//if(driver.findElement(By.xpath("//iframe[contains(@id,'BILLorder-history-card_')]")).getAttribute("innerHTML").contains("HTML"))
											driver.switchTo().frame(List_IFRAME.get(j));						
											
											String newHtmlPage = driver.getPageSource();
											
											//this is to avoid sqlException which do not accept signal quote inside it.
											newHtmlPage = newHtmlPage.replace("'", "''");
											
											
											if(newHtmlPage.contains("</head><body></body></html>")==false)// this to check for iframe which does not have EMPTY BODY Inside it
											{
												String query = "update popup_handler SET created_at ='"+formattedTimestamp +"',order_view='"+newHtmlPage+"'";
												System.out.println("QQQ "+newHtmlPage);
												executeQuery2(query,connection);
												System.out.println("INSIDE ORDER-2");
											}

											driver.switchTo().defaultContent();
											newHtmlPage="";
											//earlier this variable was set to null, so we get 2 page after clicking on order button
										}catch (Exception e) 
											{
									            e.printStackTrace();
//										            driver.navigate().refresh();// this line was added by me to refresh the browser
									        }
										}
							}
			}
			
					if(Action_performed.getText().equals("KOT"))// this is to check the TEXT IN button is "KOT"
						{

						System.out.println("KOT "+ action_text + "//preceding::iframe[1]");

//						List<WebElement> List_IFRAME = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(action_text + "//preceding::iframe | " + action_text + "//following::iframe")));
						List<WebElement> List_IFRAME = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(action_text + "//preceding::iframe[1]")));// | " + action_text + "//following::iframe")));
						System.out.println("KOT_Iframe_Size "+ List_IFRAME.size());
						for (int j=0;j<=List_IFRAME.size()-1;j++)
								{
									String id=List_IFRAME.get(j).getAttribute("id");
									
//									if(List_IFRAME.get(j) != null && List_IFRAME.get(j).getAttribute("style").contains("none;") && id.contains("KOT"))
									if(List_IFRAME.get(j) != null && List_IFRAME.get(j).getAttribute("style").contains("none;"))
									{
										try {

											//if(driver.findElement(By.xpath("//iframe[contains(@id,'BILLorder-history-card_')]")).getAttribute("innerHTML").contains("HTML"))
											driver.switchTo().frame(List_IFRAME.get(j));						
											
											String newHtmlPage = driver.getPageSource();
											
											//this is to avoid sqlException which do not accept signal quote inside it.
											newHtmlPage = newHtmlPage.replace("'", "''"); //now removed bacause now sql accept single quotes a;lso I GUESS
											
											
											if(newHtmlPage.contains("</head><body></body></html>")==false)// this to check for iframe which does not have EMPTY BODY Inside it
											{
												String query = "update popup_handler SET created_at ='"+formattedTimestamp +"',kot_view='"+newHtmlPage+"'";
														
												executeQuery2(query,connection);
												System.out.println("query -KOT " + query);
											}

											driver.switchTo().defaultContent();	
											newHtmlPage="";
										}catch (Exception e) 
											{
									            e.printStackTrace();
//										            driver.navigate().refresh();// this line was added by me to refresh the browser
									        }
									}
							}
			}

					
//					System.out.println("query " + query);
		    		executeQuery2(sql,connection);
		    		action_text="";
		    		Action_performed = null;
			}
		}
		ls.clear();		
	}
//	else 
//		{
//			String url= driver.getCurrentUrl();
//			driver.navigate().to(url);
//		}
	}
		
	}catch(Exception e) {
		//e.getMessage();
		
		String msg=e.getLocalizedMessage().split("Build info:")[0];
		if(msg.contains("java.util.concurrent.TimeoutException") || msg.contains("It may have died") || msg.contains("tab crashed") || msg.contains("invalid session id")) {
		logger.info("error on update OPeration method " + e.getLocalizedMessage());
		e.printStackTrace();
		
    	if(msg.contains("It may have died") || msg.contains("tab crashed") || msg.contains("invalid session id"))
    	{
		try {// 2 lineshidden by me on 26-april
			close_browser();
	    	getBrowser("chrome");
			
	    	logger.info("error on update OPeration method browser CLOSED initiated" + msg);
	    	System.out.println("POP1 " + e.getLocalizedMessage());
	        //((JavascriptExecutor) driver).executeScript("location.reload(true);");
       }catch (Exception e1) { 
			e1.printStackTrace();
			msg = e1.getLocalizedMessage().split("Build info:")[0];
			System.out.println("pop2 " + e1.getLocalizedMessage());
		}
	}
//	    ((JavascriptExecutor) driver).executeScript("location.reload(true);");
//		this is to Empty the actions_text after error is arrived  
		String updateQuery2 ="update popup_handler SET action_text='pop' where aggregator='Zomato'";
		try {
			executeQuery2(updateQuery2,connection);
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.info("kanhaji ");
		}
	}
	}
	//delibrately blank the variable
	action_text="";
//	logger.info("Jai shree RAM");
}


//NOW
public static void executeQuery2(String query,Connection connection) throws SQLException 
//EARLIER  --->public static void executeQuery2(String query, Connection connection) throws SQLException 
{
 try {
	 Connection conn = connection;
 	
     if(conn == null) 
     {	            	
	        	 conn = DatabaseConnectionPool.getConnected();//DriverManager.getConnection(url, username, password);
	             PreparedStatement preparedStatement = conn.prepareStatement(query);
	             preparedStatement.executeUpdate();
	             //System.out.println("query "+query +" connection counter = "+counter++);		             
     }
     else {

     			PreparedStatement preparedStatement = conn.prepareStatement(query);
	            preparedStatement.executeUpdate();
	            //System.out.println("Query-Latest " + query);// +" connection counter = "+counter++);
	            preparedStatement.close();
     }		
 }
 catch(Exception e) {
	   System.out.println("EXCEPTION "+e.getLocalizedMessage());
	   e.printStackTrace();
 }
}	


public void executeWithTimeout(String action_text, String sql, WebDriverWait wait) {
	
  ExecutorService executor = Executors.newSingleThreadExecutor();  
	  // Wrap the update_operation method inside a Callable
  
	  Callable<Void> callableTask = () -> {
      update_operation(action_text, sql, wait);
      return null; // Returning null as the task doesn't return a value
  };
try {
      // Submit the task to the executor and wait for the result with a timeout
		executor.submit(callableTask).get(3, TimeUnit.SECONDS);//earlier with 5 secs
	}
//			catch (TimeoutException | java.util.concurrent.TimeoutException e) {
			catch (java.util.concurrent.TimeoutException e) {
				System.out.println("catched The task took too long and timed out!");
//				logger.info("TimeoutException | java.util.concurrent.TimeoutException e");
				logger.info("java.util.concurrent.TimeoutException e ---> " + e.getMessage());
//			try {
//				//hidded by m eon 26-april
////				close_browser();
////				getBrowser("chrome");
//				}catch(Exception e2) {
//					e2.getMessage();
//				}
			}
	     catch (InterruptedException | ExecutionException e) {
		    	System.out.println("catched (InterruptedException | ExecutionException e)");
		    	e.printStackTrace();
	     	} 
  finally {
	        if (!executor.isShutdown()) {
        		executor.shutdown();
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
   
//      try 
//      {
//    	  
//          // Run the tasklist command to list all processes with memory usage
//          processBuilder = new ProcessBuilder("tasklist", "/FI", "IMAGENAME eq chrome.exe", "/FO", "CSV", "/NH");
//          process = processBuilder.start();
//          reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
////          String line;
//
//          // Read the output of the tasklist command
//          while ((line = reader.readLine()) != null) {
//              // Output in CSV format: "chrome.exe", "1234", "100,000 K", etc.
//              String[] processInfo = line.split("\",\"");
//              if (processInfo.length > 3) {
//                  String processName = processInfo[0].replace("\"", ""); 
//                  String pid = processInfo[1].replace("\"", "");
//                  String memoryUsage = processInfo[4].replace("\"", "");
//
//                  System.out.println("Process: " + processName);
//                  System.out.println("PID: " + pid);
//                  System.out.println("Memory Usage: " + memoryUsage);
//              }
//          }
//      } catch (IOException e) {
//          System.out.println("E1 "+e.getLocalizedMessage());
//      }

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
	}