package com.smarteinc.api.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.apache.bcel.generic.LSTORE;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.jayway.restassured.response.Response;
import com.smarteinc.objects.PEApiInputObjects;
import com.smarteinc.peapi.PEAPI_Library;
import com.smarteinc.utility.APIUtility;
import com.smarteinc.utility.ExcelUtility;
import com.google.gson.Gson; 
import com.google.gson.GsonBuilder; 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MatchBackTest extends APIBaseTest {
	Properties prop = new Properties();
	Properties prop2 = new Properties();
	private static Logger logger = initializeLogger(new MatchBackTest());

	
	public static Logger initializeLogger(Object classObject) {
		System.setProperty("logDirectory", "..\\com.smarteinc.automation\\logs");
		logger = LogManager.getLogger(classObject.getClass().getSimpleName());

		return logger;
	}
	
	@BeforeTest
	public void beforeTest() throws IOException, IOException
	{	
		String propFileName = "/Config/Tranalyzer.properties";
		String propFileName1 = "/Config/PEApiOutput.properties";
		
		InputStream inputStream = MatchBackTest.class.getResourceAsStream(propFileName);
		InputStream inputStream1 = MatchBackTest.class.getResourceAsStream(propFileName1);
			
		if (inputStream != null) {
			try {
				prop.load(inputStream);
				prop2.load(inputStream1);
			} catch (IOException e) {

			}
		}
	}
	
	@Test
	public void GenericPEAPIMatchBackTestSet1() throws Exception {
	
		logger.info("Start Test");
		//System.out.println(getClass().getClassLoader().getResource("logging.properties"));
		
		
		XSSFSheet sheet= null;
		List<String> lstStatus = new ArrayList<String>();

		String file = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/RSIDataToRun-1.xlsx").getPath().replace("%20", " ").replaceFirst("/", "");
		try
		{

		String URL = "http://mcharpeapi.smarteinc.com/api/v1/enrich";
		sheet = ExcelUtility.openSpreadSheet(file, "Sheet1");
		int lastRow = sheet.getLastRowNum();

		Map<String, String> hm = new HashMap<String, String>();
		PEAPI_Library  tran = new PEAPI_Library();
		
		PEApiInputObjects obj = new PEApiInputObjects();
		for (int row = 1; row <= lastRow; row++) {
			// Input fields

			obj.setComapnyId(ExcelUtility.getCellData(sheet, row, prop.getProperty("Id")));
			obj.setCompGuid(ExcelUtility.getCellData(sheet, row, prop.getProperty("CompGuid")));
			obj.setContactFirstName(ExcelUtility.getCellData(sheet, row, prop.getProperty("FirstName")));
			
			
			
			obj.setContactMiddleName(ExcelUtility.getCellData(sheet, row, prop.getProperty("MiddleName")));
			obj.setContactLastName(ExcelUtility.getCellData(sheet, row, prop.getProperty("LastName")));
			obj.setContactFullName(ExcelUtility.getCellData(sheet, row, prop.getProperty("FullName")));
			obj.setContactEmail(ExcelUtility.getCellData(sheet, row, prop.getProperty("Email")));
			obj.setContactJobTitle(ExcelUtility.getCellData(sheet, row, prop.getProperty("JobTitle")));
			obj.setCompanyName(ExcelUtility.getCellData(sheet, row, prop.getProperty("Company")));
			//logger.info(ExcelUtility.getCellData(sheet, row, prop.getProperty("Company")));
			
			obj.setContactState(ExcelUtility.getCellData(sheet, row, prop.getProperty("State")));
			obj.setContactZipCode(ExcelUtility.getCellData(sheet, row, prop.getProperty("PostalCode")));
			obj.setContactCountry(ExcelUtility.getCellData(sheet, row, prop.getProperty("Country")));
			obj.setContactPhone(ExcelUtility.getCellData(sheet, row, prop.getProperty("Phone")));
			obj.setCompanyWebAddress(ExcelUtility.getCellData(sheet, row, prop.getProperty("Website")));
			obj.setContactGuid(ExcelUtility.getCellData(sheet, row, prop.getProperty("PersonGuid")));
			
		    String strBody = APIUtility.getJsonBody(obj);
			
			Response res = tran.getResponseForPEApi(strBody, URL, prop.getProperty("mode"));
			
			@SuppressWarnings("unchecked")
			Enumeration<String> enums = (Enumeration<String>) prop2.propertyNames();
			List<String> lstPEOutput = new ArrayList<String>();
			while (enums.hasMoreElements()) {
			      String key = enums.nextElement();
			      String value = prop2.getProperty(key);
	
			      lstPEOutput.add(value);
			    }
			tran.updateExcelCell(lstPEOutput, sheet, row, res);
			System.out.println("Row" + row);
		}
			
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			
			logger.info(fileName);
			logger.info(newFile);
			try {
			
				ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
				logger.info("Saved");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			Assert.fail("PE API script has failed");
		}
	
	}
	

	@Test
	public void GenericPEAPIMatchBackTestSet2() throws Exception {
	
		XSSFSheet sheet= null;
		List<String> lstStatus = new ArrayList<String>();
	
		String file = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/RSIDataToRun-2.xlsx").getPath().replace("%20", " ").replaceFirst("/", "");
		try
		{

		String URL = "http://mcharpeapi.smarteinc.com/api/v1/enrich";
		sheet = ExcelUtility.openSpreadSheet(file, "Sheet1");
		int lastRow = sheet.getLastRowNum();

		Map<String, String> hm = new HashMap<String, String>();
		PEAPI_Library  tran = new PEAPI_Library();
		
		PEApiInputObjects obj = new PEApiInputObjects();
		for (int row = 1; row <= lastRow; row++) {
			// Input fields

			obj.setComapnyId(ExcelUtility.getCellData(sheet, row, prop.getProperty("Id")));
			obj.setCompGuid(ExcelUtility.getCellData(sheet, row, prop.getProperty("CompGuid")));
			obj.setContactFirstName(ExcelUtility.getCellData(sheet, row, prop.getProperty("FirstName")));
			obj.setContactMiddleName(ExcelUtility.getCellData(sheet, row, prop.getProperty("MiddleName")));
			obj.setContactLastName(ExcelUtility.getCellData(sheet, row, prop.getProperty("LastName")));
			obj.setContactFullName(ExcelUtility.getCellData(sheet, row, prop.getProperty("FullName")));
			obj.setContactEmail(ExcelUtility.getCellData(sheet, row, prop.getProperty("Email")));
			obj.setContactJobTitle(ExcelUtility.getCellData(sheet, row, prop.getProperty("JobTitle")));
			obj.setCompanyName(ExcelUtility.getCellData(sheet, row, prop.getProperty("Company")));
			obj.setContactState(ExcelUtility.getCellData(sheet, row, prop.getProperty("State")));
			obj.setContactZipCode(ExcelUtility.getCellData(sheet, row, prop.getProperty("PostalCode")));
			obj.setContactCountry(ExcelUtility.getCellData(sheet, row, prop.getProperty("Country")));
			obj.setContactPhone(ExcelUtility.getCellData(sheet, row, prop.getProperty("Phone")));
			obj.setCompanyWebAddress(ExcelUtility.getCellData(sheet, row, prop.getProperty("Website")));
			obj.setContactGuid(ExcelUtility.getCellData(sheet, row, prop.getProperty("PersonGuid")));
			
		    String strBody = APIUtility.getJsonBody(obj);
			
			Response res = tran.getResponseForPEApi(strBody, URL, prop.getProperty("mode"));
			
			@SuppressWarnings("unchecked")
			Enumeration<String> enums = (Enumeration<String>) prop2.propertyNames();
			List<String> lstPEOutput = new ArrayList<String>();
			while (enums.hasMoreElements()) {
			      String key = enums.nextElement();
			      String value = prop2.getProperty(key);
	
			      lstPEOutput.add(value);
			    }
			tran.updateExcelCell(lstPEOutput, sheet, row, res);
			System.out.println("Row" + row);
		}
			
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
	
			try {
			
				ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			Assert.fail("PE API script has failed");
		}
	
	}
	
	//@Test
	public void GenericPEAPIMatchBackTestSet3() throws Exception {
		
		XSSFSheet sheet= null;
		List<String> lstStatus = new ArrayList<String>();
	
		String file = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/Rerun-21.xlsx").getPath().replace("%20", " ").replaceFirst("/", "");
		try
		{

		String URL = "http://qcpeapi.smarteinc.com/api/v1/enrich";
		sheet = ExcelUtility.openSpreadSheet(file, "Tranalyzer");
		int lastRow = sheet.getLastRowNum();

		Map<String, String> hm = new HashMap<String, String>();
		PEAPI_Library  tran = new PEAPI_Library();
		
		PEApiInputObjects obj = new PEApiInputObjects();
		for (int row = 1; row <= lastRow; row++) {
			// Input fields

			obj.setComapnyId(ExcelUtility.getCellData(sheet, row, prop.getProperty("Id")));
			obj.setCompGuid(ExcelUtility.getCellData(sheet, row, prop.getProperty("CompGuid")));
			obj.setContactFirstName(ExcelUtility.getCellData(sheet, row, prop.getProperty("FirstName")));
			obj.setContactMiddleName(ExcelUtility.getCellData(sheet, row, prop.getProperty("MiddleName")));
			obj.setContactLastName(ExcelUtility.getCellData(sheet, row, prop.getProperty("LastName")));
			obj.setContactFullName(ExcelUtility.getCellData(sheet, row, prop.getProperty("FullName")));
			obj.setContactEmail(ExcelUtility.getCellData(sheet, row, prop.getProperty("Email")));
			obj.setContactJobTitle(ExcelUtility.getCellData(sheet, row, prop.getProperty("JobTitle")));
			obj.setCompanyName(ExcelUtility.getCellData(sheet, row, prop.getProperty("Company")));
			obj.setContactState(ExcelUtility.getCellData(sheet, row, prop.getProperty("State")));
			obj.setContactZipCode(ExcelUtility.getCellData(sheet, row, prop.getProperty("PostalCode")));
			obj.setContactCountry(ExcelUtility.getCellData(sheet, row, prop.getProperty("Country")));
			obj.setContactPhone(ExcelUtility.getCellData(sheet, row, prop.getProperty("Phone")));
			obj.setCompanyWebAddress(ExcelUtility.getCellData(sheet, row, prop.getProperty("Website")));
			obj.setContactGuid(ExcelUtility.getCellData(sheet, row, prop.getProperty("PersonGuid")));
			
		    String strBody = APIUtility.getJsonBody(obj);
			
			Response res = tran.getResponseForPEApi(strBody, URL, prop.getProperty("mode"));
			
			@SuppressWarnings("unchecked")
			Enumeration<String> enums = (Enumeration<String>) prop2.propertyNames();
			List<String> lstPEOutput = new ArrayList<String>();
			while (enums.hasMoreElements()) {
			      String key = enums.nextElement();
			      String value = prop2.getProperty(key);
	
			      lstPEOutput.add(value);
			    }
			tran.updateExcelCell(lstPEOutput, sheet, row, res);
			System.out.println("Row" + row);
		}
			
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
	
			try {
			
				ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			Assert.fail("PE API script has failed");
		}
	
	}
	
	//@Test
	public void GenericPEAPIMatchBackTestSet4() throws Exception {
		
		XSSFSheet sheet= null;
		List<String> lstStatus = new ArrayList<String>();
	
		String file = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/Rerun-10.xlsx").getPath().replace("%20", " ").replaceFirst("/", "");
		try
		{

		String URL = "http://qcpeapi.smarteinc.com/api/v1/enrich";
		sheet = ExcelUtility.openSpreadSheet(file, "Tranalyzer");
		int lastRow = sheet.getLastRowNum();

		Map<String, String> hm = new HashMap<String, String>();
		PEAPI_Library  tran = new PEAPI_Library();
		
		PEApiInputObjects obj = new PEApiInputObjects();
		for (int row = 1; row <= lastRow; row++) {
			// Input fields

			obj.setComapnyId(ExcelUtility.getCellData(sheet, row, prop.getProperty("Id")));
			obj.setCompGuid(ExcelUtility.getCellData(sheet, row, prop.getProperty("CompGuid")));
			obj.setContactFirstName(ExcelUtility.getCellData(sheet, row, prop.getProperty("FirstName")));
			obj.setContactMiddleName(ExcelUtility.getCellData(sheet, row, prop.getProperty("MiddleName")));
			obj.setContactLastName(ExcelUtility.getCellData(sheet, row, prop.getProperty("LastName")));
			obj.setContactFullName(ExcelUtility.getCellData(sheet, row, prop.getProperty("FullName")));
			obj.setContactEmail(ExcelUtility.getCellData(sheet, row, prop.getProperty("Email")));
			obj.setContactJobTitle(ExcelUtility.getCellData(sheet, row, prop.getProperty("JobTitle")));
			obj.setCompanyName(ExcelUtility.getCellData(sheet, row, prop.getProperty("Company")));
			obj.setContactState(ExcelUtility.getCellData(sheet, row, prop.getProperty("State")));
			obj.setContactZipCode(ExcelUtility.getCellData(sheet, row, prop.getProperty("PostalCode")));
			obj.setContactCountry(ExcelUtility.getCellData(sheet, row, prop.getProperty("Country")));
			obj.setContactPhone(ExcelUtility.getCellData(sheet, row, prop.getProperty("Phone")));
			obj.setCompanyWebAddress(ExcelUtility.getCellData(sheet, row, prop.getProperty("Website")));
			obj.setContactGuid(ExcelUtility.getCellData(sheet, row, prop.getProperty("PersonGuid")));
			
		    String strBody = APIUtility.getJsonBody(obj);
			
			Response res = tran.getResponseForPEApi(strBody, URL, prop.getProperty("mode"));
			
			@SuppressWarnings("unchecked")
			Enumeration<String> enums = (Enumeration<String>) prop2.propertyNames();
			List<String> lstPEOutput = new ArrayList<String>();
			while (enums.hasMoreElements()) {
			      String key = enums.nextElement();
			      String value = prop2.getProperty(key);
	
			      lstPEOutput.add(value);
			    }
			tran.updateExcelCell(lstPEOutput, sheet, row, res);
			System.out.println("Row" + row);
		}
			
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
	
			try {
			
				ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			Assert.fail("PE API script has failed");
		}
	
	}
	

	

	
}
