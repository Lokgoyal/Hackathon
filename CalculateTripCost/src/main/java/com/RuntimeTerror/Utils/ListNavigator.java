package com.RuntimeTerror.Utils;



import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.RuntimeTerror.BaseClass.Browser;

public class ListNavigator extends Browser{
	
	
	
	/****************Constructor******************/
	public ListNavigator(WebDriver driver)
	{
		this.driver = driver;
	}
	
	
	
	/****************Iterate List for a specific valuer******************/
	public boolean listNavigator(String xpathLocator,String varyingXpath, String valueLookingFor)
	{
		
		//Fetch WebElements List by combining xPath and subxPath
		List<WebElement> ListItems = driver.findElements(By.xpath(xpathLocator + varyingXpath));	
		
		
		for(int i=0; i < ListItems.size(); i++)											//Iterating on Elements
		{
			//System.out.println(ListItems.get(i).getText());
			if(ListItems.get(i).getText().equals(valueLookingFor))						//Looking for a Matching Value
			{		
				reportInfo(valueLookingFor +" Found!");
				clickButton(xpathLocator + "[" + (i+1) + "]");							//click if match		
				return true;
			}
			
		}
		
		
		return false;																	//status of operation
		
	}

}
