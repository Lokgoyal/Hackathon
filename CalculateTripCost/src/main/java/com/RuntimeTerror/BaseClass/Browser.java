package com.RuntimeTerror.BaseClass;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;

import com.RuntimeTerror.PageClasses.tripAdvisor;
import com.RuntimeTerror.Utils.FileHandler;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;

import junit.framework.Assert;

public class Browser {
	
	
	public WebDriver driver = null;
	public static String mainWindowHandle;
	public static ExtentReports report;
	public static ExtentTest logger;
	public static int screenShotCounter = 1;
	public SoftAssert softAssert = new SoftAssert();
	
	
	/*****************Open Respective Browser******************/
	public void openBrowser()
	{
		
		//Driver Initializer
		switch(FileHandler.fetchProperty("browser").toLowerCase())
		{
			//case "Internet explorer" : return InternetExplorer();
			case "firefox" : 	System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\src\\main\\resources\\Drivers\\geckodriver.exe");
								driver = new FirefoxDriver();
								break;
								
			default : 			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\main\\resources\\Drivers\\chromedriver.exe");
								driver = new ChromeDriver();
								break;
		}
		
		setHandle();								//set handle to main Window
		
	}
	
	
	
	
	
	/*****************Close Browser ******************/
	public void closeBrowser(String message)
	{
		System.out.println(FileHandler.fetchProperty("browser") + " " + message);									//Closing Browser with Message
		driver.quit();
		
	}

	
	
	
	
	/*****************Open Site******************/
	public tripAdvisor openSite()
	{
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		if(FileHandler.fetchProperty("URL")==null)
				reportFail("URL not Loaded");
		else
			reportPass("URL successfully Navigated to " + FileHandler.fetchProperty("URL"));
		
		driver.get(FileHandler.fetchProperty("URL"));
		
		return PageFactory.initElements(driver, tripAdvisor.class);										//Initializing TrippAdvisor class
		
	}

	
	
	
	
	/*****************Set Window Handle******************/
	public void setHandle()
	{
		mainWindowHandle = driver.getWindowHandle();											//Set Window Handle to Active Main Window
	}

	
	
	
	
	/*****************Switch Handle to Parent/Child Window******************/
	public String switchHandle(String windowToActive)
	{
		
		
		if(!windowToActive.equalsIgnoreCase("main"))								//if handle is expected to switch to child window
		{
			Set<String> handlesList = driver.getWindowHandles();
		
			for(String handle: handlesList)
			{
				if(!mainWindowHandle.equalsIgnoreCase(handle))								//Switch to child window
				{
					reportInfo("Handle Switched to Child Window");
					return handle;
				}
			}
			
		}
		
		reportInfo("Handle Switched to Main Window");
		return mainWindowHandle;													//If handle is expected to switch to Main Window
		
	}

	
	
	
	
	/*****************Locate WebElement and fill the value******************/
	public void locateAndFill(String xpathLocator, String sendKeysValue)	
	{
		validateAndLocateXpath(xpathLocator).sendKeys(sendKeysValue);					//Try to catch the exception
	
	}

	
	
	
	
	/*****************Locate WebElement and click******************/
	public void clickButton(String xpathLocator)	 								
	{
		validateAndLocateXpath(xpathLocator).click();								//Click on located  Web element
		
	}
	
	
	
	
	
	/*****************locate WebElement using valid xPath******************/
	public WebElement validateAndLocateXpath(String xpathLocator)
	{
		
		try
		{
			WebElement locateElement = null;
			locateElement = driver.findElement(By.xpath(xpathLocator));
			//reportInfo("Element Found with Locator: " +  xpathLocator);
			return locateElement;
		}
		catch(NoSuchElementException e)													//To catch No such element exception
		{		
			
			reportFail("Can not Locate Element with Locator " + xpathLocator);
			closeBrowser("Terminating the Process due to some error! Please resolve the error!");
			return null;
		}
		
		catch(Exception e)																//To catch general Exception
		{
			
			reportFail("Description: " + e.getMessage());
			closeBrowser("Terminating the Process due to an error! Please resolve the error!");
			return null;
		}
	
	}

	
	
	
	
	
	/********************Create Extent Report*****************/
	public static void initiateReport(int testNumber)
	{
		String Filename;
		if(report == null)
		{	
			Filename = "Test " + testNumber + ".html";
			ExtentHtmlReporter htmlReports = new ExtentHtmlReporter(System.getProperty("user.dir") + "\\Test Reports Output\\" + Filename);
			report = new ExtentReports();
			
			report.attachReporter(htmlReports);
			
			report.setSystemInfo("OS", "Windows 10 21H1");
			report.setSystemInfo("Environment", "TripAdvisor site");
			report.setSystemInfo("Build Number", "10.8.1");
			report.setSystemInfo("Browser", FileHandler.fetchProperty("browser"));

			htmlReports.config().setDocumentTitle("Trip Advisor Report");
			htmlReports.config().setReportName("Test Report");
			htmlReports.config().setTestViewChartLocation(ChartLocation.TOP);
			
		
		}
		
		
	}
	
	
	
	
	
	/***************Initiate Report***************/
	public static void AttachReporter(int testNumber)
	{
		initiateReport(testNumber);
		logger = report.createTest("Test " + testNumber);
		
	}
	
	
	
	
	
	
	
	/******************Report pass*********************/
	public void reportFail(String message)
	{
		logger.log(Status.FAIL, message);
		takeScreenShotOnFailure();
		Assert.fail(message);
	}
	
	
	
	
	
	/****************Report Pass********************/
	public void reportPass(String message)
	{
		logger.log(Status.PASS, message);
	}
	
	
	
	
	
	
	public void reportInfo(String message)
	{
		logger.log(Status.INFO, message);
	}
	
	
	
	
	
	
	/*******************End Report********************/
	public void endReport()
	{
		report.flush();
	}
	
	
	
	
	/*******************Take Screenshot on failure********************/
	public void takeScreenShotOnFailure()
	{
		TakesScreenshot captured = (TakesScreenshot) driver;
		File src = captured.getScreenshotAs(OutputType.FILE);

		String filePath = System.getProperty("user.dir") + "\\Screenshots\\" + "Test " + FileHandler.TestNumber + "(" + screenShotCounter++  + ").png";
		File dest = new File(filePath);
		
		try 
		{
			FileUtils.copyFile(src, dest);
			logger.addScreenCaptureFromPath(filePath);
			reportInfo("Screenshot Captured Successfully!");
		} 
		catch (IOException e) 
		{
			logger.log(Status.FAIL, e.getMessage());
		}
		
	}
	
	
	
	
	/****************Wait for certain time*****************/
	public void putOnWait(int timer)
	{
		try
		{
			Thread.sleep(timer * 1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	 /************************************************
     * Assertion Functions
     ****************************************/

 

    public void assertTrue(boolean flag) {
        softAssert.assertTrue(flag);
    }

 

    public void assertFalse(boolean flag) {
        softAssert.assertFalse(flag);
    }

 

    public void assertequals(String actual, String expected) {
        softAssert.assertEquals(actual, expected);
    }
	
	
    
    public void assertAll()
    {
    	softAssert.assertAll();
    }
	
}
