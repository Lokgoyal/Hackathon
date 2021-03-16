package com.RuntimeTerror.BaseClass;

import org.openqa.selenium.WebDriver;

import com.RuntimeTerror.Utils.ListNavigator;


public class TopMenuClass extends Browser{

	public String TopMenuXpath;
	public String subXpath;
	public ListNavigator iterateList;
	public WebDriver driver;
	
	
	/***********************Constructor******************/
	public TopMenuClass(String xpathForTopMenu, String varyingXPath, WebDriver driver)
	{
		this.driver = driver;
		TopMenuXpath = xpathForTopMenu;															//xPath for locating elements
		subXpath = varyingXPath;																//varying xPath for iterating on elements 
		iterateList = new ListNavigator(driver);										
	}
	
	
	
	
	
	/***********************Click Particular Top Menu******************/
	public boolean clickMenu(String MenuName)
	{
		return iterateList.listNavigator(TopMenuXpath, subXpath , MenuName);					//Search For Respective Menu Option
		
	}
	
	

}
