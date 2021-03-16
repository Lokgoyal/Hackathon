package com.RuntimeTerror.tests;


import java.util.Hashtable;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.RuntimeTerror.BaseClass.Browser;
import com.RuntimeTerror.PageClasses.Cruises;
import com.RuntimeTerror.PageClasses.HolidayHomes;
import com.RuntimeTerror.PageClasses.tripAdvisor;
import com.RuntimeTerror.Utils.FileHandler;



public class SmokeTest extends Browser {
	

	public Browser browser;
	public tripAdvisor homepage;
	public HolidayHomes holidayHomePage;
	public Cruises cruisePage;
	public static int testNumber = 1;
	public Hashtable<String, String> fetchedData;
	
	@BeforeSuite(groups = "smoke")
	public void setup()
	{
		FileHandler.loadProperty(System.getProperty("user.dir") + "\\src\\main\\resources\\ObjectRepository\\Configuration.properties");
		
		browser = new Browser();
		browser.openBrowser();
		
		FileHandler.loadExcelFile("TestData.xlsx", "input");
	}
	
	
	
	
	@DataProvider(name="ExcelData")
	public static Object[][] executeTest()
	{
		return FileHandler.fetchDataSet("sample", testNumber);
	}
	
	
	
	
	@Test(dataProvider = "ExcelData", priority = 0, testName="inputSet", groups = "smoke")
	public void main(Hashtable<String, String> dataSet)
	{
		fetchedData = dataSet;
	}
	
	
	
	@Test(priority = 1, groups = "smoke")
	public void openHomePage()
	{
		homepage = browser.openSite();								//Site Homepage
		holidayHomePage = homepage.clickHolidayHomes();
		
	}
	
	
	
	@Test(priority = 2, groups = "smoke")
	public void searchForPlace()
	{
		try 
		{
			holidayHomePage.searchForPlace(fetchedData.get("Place Name"));
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Test(priority = 3, groups = "smoke")
	public void applySpecificFilters()
	{
		holidayHomePage.checkOptionFromMenu(fetchedData.get("Menu"), fetchedData.get("Check Option"));
		holidayHomePage.sortListBy(fetchedData.get("Sort By"));
		
		String returnedMessage = holidayHomePage.selectDate(Integer.parseInt(fetchedData.get("No. of Days")));
		FileHandler.setDataIntoExcel("status", "Days status", returnedMessage);
		
		holidayHomePage.makeGuestsTo(Integer.parseInt(fetchedData.get("No. of Guests")));
	}
	
	
	
	@Test(priority = 4, groups = "smoke")
	public void printHolidayHomeDetails()
	{
		holidayHomePage.putOnWait(9);
		holidayHomePage.printDetails(Integer.parseInt(fetchedData.get("No. of Homes")));
		
	}
	
	
	
	@Test(priority = 5, groups = "smoke")
	public void openCruise()
	{	
		cruisePage = holidayHomePage.clickCruises();									//Think about it
	}
	
	
	
	@Test(priority = 6, groups = "smoke")
	public void searchRespectiveCruise()
	{
		cruisePage.selectCruise("Cruise Line", fetchedData.get("Cruise Line"));
		
		cruisePage.putOnWait(2);
		cruisePage.selectCruise("Cruise Ship", fetchedData.get("Ship"));
		
		cruisePage.putOnWait(2);
		cruisePage.clickSearchButton();					//Search Button think about it
	}
	
	
	
	@Test(priority = 7, groups = "smoke")
	public void printCruiseDetail()
	{
		cruisePage.selectedCruiseDetails();
		
	}
	
	
	
	@Test(priority = 8, groups = {"smoke", "regression"})
	public void printLanguageOffered()
	{
		cruisePage.fetchLanguage();
		
	}
	
	
	
	@AfterSuite(groups = "smoke")
	public void closeSetup()
	{
		reportPass("Browser Closed!");
		browser.endReport();
		browser.closeBrowser("Successfully Closed!");
		
	}
	
}
