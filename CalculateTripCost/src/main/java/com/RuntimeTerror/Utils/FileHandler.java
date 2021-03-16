package com.RuntimeTerror.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;



import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.RuntimeTerror.BaseClass.Browser;

public class FileHandler {

	public static File file = null;
	public static FileInputStream fileIn = null;
	public static FileOutputStream fileOut = null;
	public static Properties property = null;
	public static XSSFWorkbook workbook = null;
	public static XSSFSheet sheet = null;
	public static XSSFRow sheetRow = null;
	public static int TestNumber;
	public static boolean keyFoundFlag;
	
	
	
	
	/*****************File Instantiator*******************/
	public static File InitiateFile(String directoryPath)
	{
		return new File(directoryPath);
	}

	
	

	
	
	/*****************File Input/Output Operator*******************/
	public static void operateFile(String event, String directoryPath)
	{
		
		file = InitiateFile(directoryPath);
		
		if(file!=null)
		{
			try
			{
				if(event.equalsIgnoreCase("input"))
					fileIn = new FileInputStream(file);
				else
					fileOut = new FileOutputStream(file);
			}
			
			catch(FileNotFoundException error)
			{
				System.out.println("File Not Found");
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		else
			System.out.println("File Not Initiated");
	
		
	}

	
	

	
	
	/*****************Property File Loader*******************/
	public static void loadProperty(String propertyFilePath)
	{
	
		if(property==null)											//Instantiate Property if null
			property = new Properties();
		
		
		operateFile("input", propertyFilePath);						//locate File and attach to fileIn object
		
		try
		{
			property.load(fileIn);									//Load Property from File
		}
		
		catch(IOException e)
		{
			System.out.println(e);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}

	
	

	
	
	/*****************propertyFetcher*******************/
	public static String fetchProperty(String propertyKeyName)
	{
		return property.getProperty(propertyKeyName);
		
	}

	
	

	
	
	/*****************Load Excel File*******************/
	public static void loadExcelFile(String fileName, String operation)
	{
		operateFile(operation, System.getProperty("user.dir") + fetchProperty("ExcelFilePath") + fileName);
		
		try
		{
			if(operation.equalsIgnoreCase("input"))
				workbook = new XSSFWorkbook(fileIn);
			
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	

	
	
	/*****************Excel data Fetcher*******************/
	public static String getDataFromExcel(String sheetName, int row, int column)
	{
		sheet = workbook.getSheet(sheetName);
		
		switch(sheet.getRow(row).getCell(column).getCellType())
		{
			case NUMERIC : return Integer.toString((int)sheet.getRow(row).getCell(column).getNumericCellValue());
			
			default:	return sheet.getRow(row).getCell(column).getStringCellValue();
						
		}
		
	}
	
	
	

	
	
	/*******************Reading Test Inouts*********************/
	public static Object[][] fetchDataSet(String sheetName, int testNumber)
	{
		TestNumber = testNumber;
		Browser.AttachReporter(TestNumber);
		
		Object[][] dataHolder = new Object[1][1];

		try
		{
		
			sheet = workbook.getSheet(sheetName);

			int NoOfRows=sheet.getLastRowNum()+1;
			
			int colNumber=1;
			
			while(sheet.getRow(0).getCell(colNumber)!=null)
			{
				if(sheet.getRow(0).getCell(colNumber++).toString().equalsIgnoreCase("Test " + testNumber + " Inputs"))
					break;
			}	
			
			Hashtable<String, String> dataset = new Hashtable<String, String>();
		
			for(int rowNumber = 1; rowNumber < NoOfRows; rowNumber++)
			{
				String key = sheet.getRow(rowNumber).getCell(0).toString();
				String value = getDataFromExcel(sheetName, rowNumber, colNumber-1);
				dataset.put(key, value);
			}
			
			dataHolder[0][0] = dataset;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return dataHolder;
		
	}

	
	

	
	
	/*****************Return Last Row from the sheet*******************/
	public static int setLastColumn(String sheetName)
	{
		XSSFSheet tempSheet = workbook.getSheet(sheetName);
		
		int tempCol= 0;
		
		String heading;
		
		if(sheetName.equalsIgnoreCase("status"))
			heading = "Status";
		else
			heading = "Output";
		
		while(tempSheet.getRow(0).getCell(tempCol)!=null)
		{
			if(tempSheet.getRow(0).getCell(tempCol++).toString().equalsIgnoreCase("Test " + TestNumber + " " + heading))
				return tempCol-1;
			
		}
		
	
		tempSheet.getRow(0).createCell(tempCol).setCellValue("Test " + TestNumber + " " + heading);
		//System.out.println("created it!");
		return tempCol;
	}

	
	

	
	
	/*****************Excel Data Inserter*******************/
	public static void setDataIntoExcel(String sheetName, String key, String value)
	{
		loadExcelFile("TestData.xlsx", "output");
		keyFoundFlag = false;
		sheet = workbook.getSheet(sheetName);
		
		int lastColumnNo = setLastColumn(sheetName);
		
		
		int rowNo=0;
		while(sheet.getRow(rowNo)!=null)
		{
			if(sheet.getRow(rowNo++).getCell(0).toString().equalsIgnoreCase(key))
			{
				keyFoundFlag = true;
				break;
			}
		}
		
		
		
		if(keyFoundFlag)
			sheet.getRow(rowNo-1).createCell(lastColumnNo).setCellValue(value);
		else
			{
				XSSFRow createdRow = sheet.createRow(rowNo);
				createdRow.createCell(0).setCellValue(key);
				createdRow.createCell(lastColumnNo).setCellValue(value);
			}
		
		
		
		try
		{
			workbook.write(fileOut);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
}
