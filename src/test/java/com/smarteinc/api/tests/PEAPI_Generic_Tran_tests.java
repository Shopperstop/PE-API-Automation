package com.smarteinc.api.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;
import com.smarteinc.peapi.PEAPI_Library;
import com.smarteinc.utility.ExcelUtility;


public class PEAPI_Generic_Tran_tests extends APIBaseTest {
	Properties prop = new Properties();
	
	@BeforeTest
	public void beforeTest()
	{
		String propFileName = "/Config/Tranalyzer.properties";

		InputStream inputStream = PEAPI_Generic_Tran_tests.class.getResourceAsStream(propFileName);

		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {

			}
		}
	}
	
	
	@Test
	public void GenericPEAPITranalyzerTest() throws Exception {
		XSSFSheet sheet= null;
		List<String> lstStatus = new ArrayList<String>();
	
		String file = PEAPI_Generic_Tran_tests.class.getResource("/TestData/Fetcher/ContactNameTran1.xlsx").getPath().replace("%20", " ").replaceFirst("/", "");
		try
		{

		String URL = "http://qcpeapi.smarteinc.com/api/v1/enrich";
		sheet = ExcelUtility.openSpreadSheet(file, "Tranalyzer");
		int lastRow = sheet.getLastRowNum();

		Map<String, String> hm = new HashMap<String, String>();
		PEAPI_Library  tran = new PEAPI_Library();
		for (int row = 1; row <= lastRow; row++) {
			// Input fields
			System.out.println("Row" + row);
			hm.put("Id", ExcelUtility.getCellData(sheet, row, prop.getProperty("Id")));
			hm.put("PersonGuid", ExcelUtility.getCellData(sheet, row, prop.getProperty("PersonGuid")));
			hm.put("FirstName", ExcelUtility.getCellData(sheet, row, prop.getProperty("FirstName")));
			hm.put("MiddleName", ExcelUtility.getCellData(sheet, row, 0, prop.getProperty("MiddleName")));
			hm.put("LastName", ExcelUtility.getCellData(sheet, row, 0, prop.getProperty("LastName")));
			hm.put("FullName", ExcelUtility.getCellData(sheet, row, prop.getProperty("FullName")));
			hm.put("Email", ExcelUtility.getCellData(sheet, row, prop.getProperty("Email")));
			hm.put("JobTitle", ExcelUtility.getCellData(sheet, row, prop.getProperty("JobTitle")));

			hm.put("CompGuid", ExcelUtility.getCellData(sheet, row, prop.getProperty("CompGuid")));
			hm.put("Company", ExcelUtility.getCellData(sheet, row, prop.getProperty("Company")));

			hm.put("State", ExcelUtility.getCellData(sheet, row, prop.getProperty("State")));
			hm.put("PostalCode", ExcelUtility.getCellData(sheet, row, prop.getProperty("PostalCode")));
			hm.put("Country", ExcelUtility.getCellData(sheet, row, prop.getProperty("Country")));
			hm.put("Phone", ExcelUtility.getCellData(sheet, row, prop.getProperty("Phone")));
			hm.put("Website", ExcelUtility.getCellData(sheet, row, prop.getProperty("Website")));

			Response res = tran.BuildPEInputJson(hm, URL, prop.getProperty("mode"));

			//System.out.println(res.asString());
			List<String> lstPEOutput = Arrays.asList("fullNameTAF","companyTAF", "emailTAF","titleTAF", "postalCodeTAF",
					"stateTAF", "countryTAF","websiteTAF","phoneTAF","companyTICleanCompName","companyTICleanCompNameWithoutCountry",
				"countryTICountryCode","fullNameTIFirstName","fullNameTIMiddleName","fullNameTILastName","fullNameTIFirstNameForEmail",
				"fullNameTILastNameForEmail","fullNameTIMiddleNameForEmail","websiteTIDomain","websiteTITitle", "websiteTICleanWebsite",
				"websiteTICountry","emailTIDomain", "emailTIFirstName","emailTILastName","emailTICompName","emailTINameString",
				"emailTIPattern","stateTIState", "stateTIStateCode","postalCodeTIState","isRecordEligibleComp","isRecordEligibleCon",
				"titleTILevel", "titleTIFunction","titleTISubfunction", "phoneTICountry","phoneTIFormattedNo", "companyTTF","countryTTF",
				"emailTTF","fullNameTTF","phoneTTF","postalCodeTTF","stateTTF","titleTTF","websiteTTF","countryTICountry","fullNameTICleanNameBeforeSplit",
				"phoneTICountryCode","postalCodeTICity","websiteTIDescription","websiteTIKeywords","websiteTIKeywords","websiteTIWebsite",
				"isEmailJunk","isConNameJunk","isCompNameJunk","isEmailEligible","isConNameEligible","isCompNameEligible","isCompGuidEligible",
				"isCompWebEligible");
			
	//		tran.updateExcelCell(lstPEOutput, sheet, row, res);
		}
			
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			
			//File fi = new File(newFile);
			try {
				//fi.delete();
				ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		for (String str : lstStatus) {
			if (str.equals("Fail")) {
				Assert.fail("Contact tranalyzer has failed");
			}
		}
	}
		catch(Exception ex)
		{
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
		}
	
	}
	
	

}
