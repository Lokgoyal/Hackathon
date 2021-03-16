package com.RuntimeTerror.PageClasses;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.RuntimeTerror.BaseClass.Browser;
import com.RuntimeTerror.BaseClass.TopMenuClass;

public class tripAdvisor extends Browser {
	
	
	public TopMenuClass topMenu;
	
	
	/***********************Constructor********************/
	public tripAdvisor(WebDriver driver)
	{
		this.driver = driver;	
		topMenu = new TopMenuClass("//div[@class='_1ZteHrEy']/div", "/a", driver);			//xPath for Top Menu appearing on Home page
	}

	
	
	
	/***********************Holiday Homes Menu Clicker********************/
	public HolidayHomes clickHolidayHomes()
	{
		
		if(topMenu.clickMenu("Holiday Homes"))
		{	
			reportPass("Holiday Homes Button Clicked!");
			return PageFactory.initElements(driver, HolidayHomes.class);						//Initialize Holiday Home Class 
		}
		
		reportFail("Holiday Home Button Not Clicked!");
		return null;
	}

	
}
