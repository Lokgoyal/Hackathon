package com.RuntimeTerror.PageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.RuntimeTerror.BaseClass.Browser;
import com.RuntimeTerror.Utils.FileHandler;
import com.RuntimeTerror.Utils.ListNavigator;

public class Cruises extends Browser {

	
	
	public ListNavigator iterateList;
	
	@FindBy(xpath = "//span[@class='_2O1ErRJV']/button")
	public WebElement searchButton;

	
	
	/*****************Constructor*****************/
	public Cruises(WebDriver driver)
	{	
		this.driver = driver;
		iterateList = new ListNavigator(driver);
	}

	
	
	
	
	/*****************Select Cruise Line/Ship *****************/
	public void selectCruise(String CruiseDropDown, String CruiseDropDownValue)
	{

		if(CruiseDropDown.equalsIgnoreCase("cruise line"))
			clickButton("//div[@class='WVJSAxEI']/div[1]//button");							//xPath for Cruise Line Drop down
		else
			clickButton("//div[@class='WVJSAxEI']/div[2]//button");							//xPath for Cruise Ship Drop down
		
		
	
		reportPass(CruiseDropDown + " Drop Down Clicked");
		
		String xpathLocator = "//div[@class='_1fkNEpw6 _11UkZ21j']//ul/li";
		String varyingXpath = "";
		
		if(iterateList.listNavigator(xpathLocator, varyingXpath, CruiseDropDownValue))				//look for Respective Option
			{
				reportPass(CruiseDropDownValue + " Selected!");
				FileHandler.setDataIntoExcel("status", CruiseDropDown, CruiseDropDownValue + ", Selected");		//Writing Excel Data
			}
		else
			{
				reportFail(CruiseDropDownValue + " Not Selected!");
				FileHandler.setDataIntoExcel("status", CruiseDropDown, CruiseDropDownValue + " Not Found");	
			}
		
	}

	
	
	
	
	/*****************Click Search Button*****************/
	public void clickSearchButton()
	{
		searchButton.click();
		reportPass("Cruise Search Clicked!");
		driver.switchTo().window(switchHandle("child"));											//Switching Handle to Child
		reportPass("Handle Switched Successfully!");
	}

	
	
	
	
	/*******************Fetch and Display Language Offered on Cruise*****************/
	public void fetchLanguage()
	{
		
		if(validateAndLocateXpath("//div[@class='_38z78Y3L']/following-sibling::*").getText().equalsIgnoreCase("Unavailable"))
		{
			reportFail("Itenerary Unavaiable for fetching Languages Offered");
			assertFalse(true);
			assertAll();
		}
		else
			reportPass("Lanuages Fetched!");
		
	}

	
	
	
	
	/*****************Fetch and Display Cruise Details*****************/
	public void selectedCruiseDetails()
	{		
		
		int noOfDetails = driver.findElements(By.xpath("//div[@class='_30ZCn9lR']/div")).size();	//Details Row Size
		
		//Cruise Details
		String[] details = validateAndLocateXpath("//div[@class='_30ZCn9lR']/div[1]").getText().split("[|]"); 
		
		FileHandler.setDataIntoExcel("CruiseDetailsOutput", "Passengers Details" , details[0]);				//Storing Passengers Details
		FileHandler.setDataIntoExcel("CruiseDetailsOutput", "Crew Details" , details[1].trim());				//Storing Crew Details

		//Storing Launched Year
		FileHandler.setDataIntoExcel("CruiseDetailsOutput", "Launched Year", validateAndLocateXpath("//div[@class='_30ZCn9lR']/div[" + noOfDetails + "]").getText());
	
		reportPass("Cruise Details Inserted in Excel Sheet!");
	}
	


}
