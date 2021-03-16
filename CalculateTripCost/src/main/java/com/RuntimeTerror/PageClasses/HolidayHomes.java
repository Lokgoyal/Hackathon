package com.RuntimeTerror.PageClasses;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.RuntimeTerror.BaseClass.Browser;
import com.RuntimeTerror.BaseClass.TopMenuClass;
import com.RuntimeTerror.Utils.FileHandler;
import com.RuntimeTerror.Utils.ListNavigator;

public class HolidayHomes extends Browser{
	
	
	public TopMenuClass topMenu;
	public ListNavigator iterateList;
	
	@FindBy(xpath = "//div[contains(@data-test-attribute,'VACATION_RENTALS')]/form/input")
	public WebElement searchBar;
	
	@FindBy(xpath = "//div[@class='_1wuPwxoN']")
	public WebElement sortByButton;
	
	@FindBy(xpath = "//div[@class='rl9SgA91']")
	public WebElement checkInButton;
	
	@FindBy(xpath = "//div[@class='_3os5NI5D']")
	public WebElement guestButton;
	
	
	

	/*******************Constructor******************/
	public HolidayHomes(WebDriver driver)
	{
		this.driver = driver;
		
		iterateList = new ListNavigator(driver);
	
		topMenu = new TopMenuClass("//div[@class='BBb9BS8P']/div/a", "" , driver);
	}

	
	
	
	
	/*******************Search Bar******************/
	public void searchForPlace(String placeName) throws InterruptedException
	{
		
		
		searchBar.sendKeys(placeName);																	//Place Name Inserter
		reportPass(placeName + " Searched!");
		
		Thread.sleep(5000);
		
		String xpathLocator = "//div[@data-test-attribute='typeahead-results']/a";				//List Items appeared on Entering
		String varyingXpath = "/div[2]/div";
		
		if(iterateList.listNavigator(xpathLocator, varyingXpath, placeName))
		{
			reportPass("Navigated to Holiday Homes Page!");
			FileHandler.setDataIntoExcel("status", "Place Name status", placeName +" Searched");					//Excel Status Writing
		}
		else
		{	
			reportFail(placeName + " Not Found in the list!");
			FileHandler.setDataIntoExcel("status", "Place Name status", placeName + " Missed");
		}
		
	}
	
	
	
	
	
	
	
	
	/*******************Check Option From Menu******************/
	public void checkOptionFromMenu(String Menu, String optionValue)
	{
		boolean flag = false;																		//Flag to determine found status of option
		
		String loc = "//div[contains(text(),'" + Menu + "')]/following-sibling::div";							//locator for Respective Menu
		reportInfo(Menu + " Menu Found");
		
		if(driver.findElements(By.xpath(loc)).size() > 4)
			clickButton(loc + "[5]");														//Show More clicked if more than 4 items are there
		
		
		List<WebElement> menuItems = driver.findElements(By.xpath(loc));														//Items List
		
		for(int i=1; i < menuItems.size() + 1; i++)
		{
			
			if(validateAndLocateXpath(loc + "[" + i + "]/descendant::div[3]").getText().contains(optionValue))				//Search For Option
			{
				clickButton(loc + "[" + i + "]/div/label");
				FileHandler.setDataIntoExcel("status", "Checked Option Status", optionValue + " Checked");					//Status Writing in Excel
				reportPass(optionValue + " Checked!");
				flag = true;
				break;
			}
			
		}
		
		if(!flag)
		{
			FileHandler.setDataIntoExcel("status", "Checked Option Status", optionValue + " Not Found");		
			reportFail(optionValue + " Not found in the list");
		}
		
	}
	
	
	
	
	
	
	
	/*******************Sort Results By Respective Option******************/
	public void sortListBy(String sortByOption)
	{
		
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0,-1000)");				//scroll window to Top, will avoid Clicking on element covering Sort By Menu
		
		
		sortByButton.click();																							//Click on Sort By Menu
		reportInfo("Sort By Button Clicked");
		
		String xpathLocator = "//div[@class='_16IExTAJ _1S9IhgUs mXy0TSnT _1Jb5DjSv']/div";								//Sort By Options xPath
		String varyingXpath = "/div/span";																				//Sub path for Options	
		
		
		if(iterateList.listNavigator(xpathLocator, varyingXpath, sortByOption))						//Iterate and Click the Respective Option
		{
			FileHandler.setDataIntoExcel("status", "Sort By Status", "Sorted By " + sortByOption);		
			reportPass("Sorted By " + sortByOption);
		}
		else																							//Excel Status Based on true or false
		{
			reportFail(sortByOption + " Not Found!");
			FileHandler.setDataIntoExcel("status", "Sort By Status", "Option Not Found");
		}
		
	}

	
	
	
	
	/*******************Select Date for Respective No. of Days from Tomorrow's Date******************/
	public String selectDate(int TotalDaysToSelect)
	{
		
		/*------------------------------------------------------------------------------------------------------------------
		MonthNo -> Representing Month from its index, 1 will be the index of Month appearing on the Left, and 2 for the Right;
		Month Rows -> It represents week number;
		Actual Date -> It represents actual date within the respective Week;
		
		tomorrowDate -> Date after today, and will keep on changing according to the scenario conditions implemented in the loop!
		dateCounter -> keeps count of valid dates selected
		TotalDaysToSelect -> Maximum limit for date counter
		switchToNextMonth -> flag variable representing when to switch to next month if at least one date is to be selected from month no.1 and rest from month no.2, this flag will be set
		NoDateMatched -> when No date is Matched from month no.1, the flag will be set
		
		
		Scenario 1: when All dates are to be selected from month 1 	
		Scenario 2: when All dates are to be selected from month 2, that is tomorrow date is next month's first date		
		Scenario 3: when at least one or more dates are to be selected from month 1 and rest dates from month 2
		-----------------------------------------------------------------------------------------------------------------------*/
		
		
		
		checkInButton.click();
		
		int tomorrowDate= new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth() + 1;		//returns tomorrow date in int //32
		int monthNo =  1;			
		int dateCounter = 0;	
		int NoOfRows= driver.findElements(By.xpath("//div[@class='_2DSA78he']/div[" + monthNo + "]/div[3]/div")).size();
		boolean switchToNextMonth = false, NoDateMatched = false;
			
		for(int monthRows=1; monthRows < NoOfRows+1; monthRows++)		//1
		{		
				
			for(int actualDate=1; actualDate < 8; actualDate++)		
			{		

				
				if(validateAndLocateXpath("//div[@class='_2DSA78he']/div[" + monthNo + "]/div[3]/div[" + monthRows + "]/div[" + actualDate + "]").getText().equals(Integer.toString(tomorrowDate)))
				{
					
					if(dateCounter++ < 1) //0 the value should be 0, true, 0, memory 1
					{	
						reportPass("Check In Date set to " + (tomorrowDate));
						clickButton("//div[@class='_2DSA78he']/div[" + monthNo + "]/div[3]/div[" + monthRows + "]/div[" + actualDate + "]");
					}
					
					tomorrowDate++;	// tomorrow date 30, 31, 2 
					
					
					if(dateCounter > TotalDaysToSelect-1)				//will keep the count of valid Dates Selected
					{
						reportPass("Check Out Date set to " + (tomorrowDate-1));
						clickButton("//div[@class='_2DSA78he']/div[" + monthNo + "]/div[3]/div[" + monthRows + "]/div[" + actualDate + "]");
						return TotalDaysToSelect + " days, Success";
					}
				
					
				}
				else if(dateCounter > 0 && !switchToNextMonth)			//will Switch to next Month if few Dates are to be selected from month 2	
				{
					//System.out.println("switch now" + monthRows);
					switchToNextMonth = true;
					tomorrowDate = 1;
					monthRows = 0;
					actualDate = 7;
					monthNo = 2;
					
				}
			
		
				if(NoDateMatched && dateCounter > 0)				//the moment it encounters first date as a match, the flag will be set to false
				{	
					NoDateMatched = false;
					monthNo=1;
				}
			
				
			}	//End of Inner For Loop
			
			
		
			//when no valid date is encountered and all the rows are already iterated , the flag NoDateMatched will be set
			if(dateCounter<1 && monthRows == NoOfRows) //true , true
			{	
				monthRows = 0;
				monthNo++; //1,2 
				NoDateMatched = true; // true 
				tomorrowDate = 1;	//1
			}
			
		}
		
		
		reportFail("Check In/Out Dates Not Selected");
		return "Date, Failed";
	}

	
	
	
	
	/*******************Make Guests Count to the Respective Value******************/
	public void makeGuestsTo(int noOfGuests)
	{
		
		guestButton.click();										//Click on Guest Menu
		reportInfo("Guest Menu Clicked");
		
		String locator;
			
		if(noOfGuests<1)											//Minimum Guests must be greater than 0
			noOfGuests = 1;
		
		
		//locator for plus or minus button
		if(noOfGuests > Integer.parseInt(validateAndLocateXpath("//div[@class='_3Vh-bNyy']/div[2]/descendant::input").getAttribute("value").substring(0, 1)))
			locator = "//div[@class='_3Vh-bNyy']/div[2]//span[contains(@class,'ui_icon plus')]";
		else
			locator = "//div[@class='_3Vh-bNyy']/div[2]//span[contains(@class,'ui_icon minus')]";
		
		
		//Click the respective '+' or '-' button unless Guest Count is equals to required Guest count
		while(!driver.findElement(By.xpath("//div[@class='_3Vh-bNyy']/div[2]/descendant::input")).getAttribute("value").equals( noOfGuests+ "+"))
		{
			clickButton(locator);
		}
		
		
		
		clickButton("//div[@class='_3Vh-bNyy']/button[contains(@class,'ui_button')]");				//Apply Button Clicked
		
		reportPass("Guests set to " + noOfGuests);
		FileHandler.setDataIntoExcel("status", "Guests Status", noOfGuests + ", Success");				//Status written in Excel
		
	}

	
	
	
	
	/*******************Print Details for Respective No. of Homes******************/
	public void printDetails(int noOfHomes)
	{
		
		
		//Respective Locator for Hotel Name, Per Night Charges and Total Charges
		String[] locator = {"//div[@class='_2eG0wTfl']/h2/a", 
							"//div[@class='_2eG0wTfl']/descendant::div[@class='_2GxfELhY']/div[1]/div[1] ",
							"//div[@class='_2eG0wTfl']/descendant::div[@class='_2GxfELhY']/div[2]"};
		
		
	
		String[] textDetails = {"Hotel Name ", "Per Night Charges ","Total Charges "};						//Keys For Excel Data
		
		List<WebElement> detailsHolder;						
		
		
		for(int i=0; i < noOfHomes; i++)
		{
			
			for(int j=0; j < locator.length; j++)	
			{
				detailsHolder = driver.findElements(By.xpath(locator[j]));
				FileHandler.setDataIntoExcel("HolidayHomeOutput", textDetails[j]+(i+1)  , detailsHolder.get(i).getText());
			}
			
		}
		
		reportPass(noOfHomes + " Holiday Homes Details Inserted in Excel Sheet!");
	}

	
	
	
	
	/*******************Top Menu Cruise Button Clicker******************/
	public Cruises clickCruises()
	{
		if(topMenu.clickMenu("Cruises"))														//returns true if Option is Found and Clicked
		{
			reportInfo("Top Menu Cruise Button Clicked!");
			reportPass("Navigated to Cruise Page!");
			return PageFactory.initElements(driver, Cruises.class);
		}
		
		
		reportFail("Cruises Top Menu Button not Found!");
		return null;
	}
	
	
}
