package com.smarteinc.api.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.lang.Object;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.nio.file.Paths;
import java.nio.file.Files;
import com.jayway.restassured.response.Response;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.smarteinc.peapi.PEAPI_Library;
import com.smarteinc.utility.CsvDataManage;
import com.smarteinc.utility.ExcelUtility;
import org.apache.commons.csv.CSVFormat;

import org.apache.commons.csv.CSVRecord;

public class TestCsv {
	Properties prop = new Properties();
	Properties prop2 = new Properties();
	Map<String, String> hm = new HashMap<String, String>();
	String URL = "http://qcpeapi.smarteinc.com/api/v1/enrich";
	PEAPI_Library  tran = new PEAPI_Library();
	
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
	public void GenericPEAPITranalyzerTest() throws Exception {
		XSSFSheet sheet= null;
		List<String> lstStatus = new ArrayList<String>();

		String apifile = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/PersonMB.csv").getPath().replace("%20", " ").replaceFirst("/", "");

		String writefile = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/PersonMBWrite.csv").getPath().replace("%20", " ").replaceFirst("/", "");
		
		//new CsvDataManage(Paths.get("G:/PersonMB.csv"), Paths.get("G:/PersonMBWrite.csv")).apply();
		
		
		
		//BufferedWriter writer = Files.newBufferedWriter(Paths.get(writefile));
		
	/*	CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
				.withHeader("Id", "PersonGuid", "FirstName", "MiddleName","LastName","FullName","Email",
		 				"JobTitle","CompGuid","Company","State","PostalCode","Country","Phone","Website"));*/
		
		String URL = "http://qcpeapi.smarteinc.com/api/v1/enrich";
		
		 Reader reader = Files.newBufferedReader(Paths.get(apifile));
		 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
		 		.withHeader("Id", "PersonGuid", "FirstName", "MiddleName","LastName","FullName","Email",
		 				"JobTitle","CompGuid","Company","State","PostalCode","Country","Phone","Website")
		 	   // .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());
		 
		 int cnt = 0;
		 for(CSVRecord csvRecord : csvParser)
		 {
			 
			 hm.put("Id", csvRecord.get(prop.getProperty("Id")));
			 hm.put("PersonGuid", csvRecord.get(prop.getProperty("PersonGuid")));
			 hm.put("FirstName", csvRecord.get(prop.getProperty("FirstName")));
			 hm.put("MiddleName", csvRecord.get(prop.getProperty("MiddleName")));
			 hm.put("LastName", csvRecord.get(prop.getProperty("LastName")));
			 hm.put("FullName", csvRecord.get(prop.getProperty("FullName")));
			 hm.put("Email", csvRecord.get(prop.getProperty("Email")));
			 hm.put("JobTitle", csvRecord.get(prop.getProperty("JobTitle")));
			// hm.put("CompGuid", csvRecord.get(prop.getProperty("CompGuid")));
			 hm.put("Company", csvRecord.get(prop.getProperty("Company")));
			 Response res;
			 
			 @SuppressWarnings("unchecked")
			 Enumeration<String> enums = (Enumeration<String>) prop2.propertyNames();
			 List<String> lstPEOutput = new ArrayList<String>();
			 while (enums.hasMoreElements()) {
				 String key = enums.nextElement();
				 String value = prop2.getProperty(key);
		
				 lstPEOutput.add(value);
				    }
			 if(cnt != 0)
			 {
				res = tran.BuildPEInputJson(hm, URL, prop.getProperty("mode"));
				System.out.println(res.asString());
			 
			 
			
			// csvPrinter.printRecord("1", "Sundar Pichai ♥", "CEO", "Google");
			 //tran.updateCsv(lstPEOutput, writer, prop2,res);
			 }
			 cnt++;
		 }
   //  ) 
		
		//BufferedReader br = null;
	    //String line = "";
	   // String cvsSplitBy = ",";
	    //int cnt =0;
	   // br = new BufferedReader(new FileReader(apifile));
        //while ((line = br.readLine()) != null) {
        	//cnt++;
            // use comma as separator
            //String[] company = line.split(cvsSplitBy);

          //  System.out.println("Country [code= " + company[0] + " , name=" + company[1] + "]");
            //System.out.println(cnt);
        //}

		
//		try
//		{

	
//		//sheet = ExcelUtility.openSpreadSheet(file, "Tranalyzer");
//		int lastRow = sheet.getLastRowNum();
//
//		Map<String, String> hm = new HashMap<String, String>();
//		PEAPI_Library  tran = new PEAPI_Library();
//		for (int row = 1; row <= lastRow; row++) {
//			// Input fields
//			System.out.println("Row" + row);
//			hm.put("Id", ExcelUtility.getCellData(sheet, row, prop.getProperty("Id")));
//			hm.put("PersonGuid", ExcelUtility.getCellData(sheet, row, prop.getProperty("PersonGuid")));
//			hm.put("FirstName", ExcelUtility.getCellData(sheet, row, prop.getProperty("FirstName")));
//			hm.put("MiddleName", ExcelUtility.getCellData(sheet, row, 0, prop.getProperty("MiddleName")));
//			hm.put("LastName", ExcelUtility.getCellData(sheet, row, 0, prop.getProperty("LastName")));
//			hm.put("FullName", ExcelUtility.getCellData(sheet, row, prop.getProperty("FullName")));
//			hm.put("Email", ExcelUtility.getCellData(sheet, row, prop.getProperty("Email")));
//			hm.put("JobTitle", ExcelUtility.getCellData(sheet, row, prop.getProperty("JobTitle")));
//
//			hm.put("CompGuid", ExcelUtility.getCellData(sheet, row, prop.getProperty("CompGuid")));
//			hm.put("Company", ExcelUtility.getCellData(sheet, row, prop.getProperty("Company")));
//
//			hm.put("State", ExcelUtility.getCellData(sheet, row, prop.getProperty("State")));
//			hm.put("PostalCode", ExcelUtility.getCellData(sheet, row, prop.getProperty("PostalCode")));
//			hm.put("Country", ExcelUtility.getCellData(sheet, row, prop.getProperty("Country")));
//			hm.put("Phone", ExcelUtility.getCellData(sheet, row, prop.getProperty("Phone")));
//			hm.put("Website", ExcelUtility.getCellData(sheet, row, prop.getProperty("Website")));
//
//			Response res = tran.BuildPEInputJson(hm, URL, prop.getProperty("mode"));
//
//			//System.out.println(res.asString());
//			List<String> lstPEOutput = Arrays.asList("fullNameTAF","companyTAF", "emailTAF","titleTAF", "postalCodeTAF",
//					"stateTAF", "countryTAF","websiteTAF","phoneTAF","companyTICleanCompName","companyTICleanCompNameWithoutCountry",
//				"countryTICountryCode","fullNameTIFirstName","fullNameTIMiddleName","fullNameTILastName","fullNameTIFirstNameForEmail",
//				"fullNameTILastNameForEmail","fullNameTIMiddleNameForEmail","websiteTIDomain","websiteTITitle", "websiteTICleanWebsite",
//				"websiteTICountry","emailTIDomain", "emailTIFirstName","emailTILastName","emailTICompName","emailTINameString",
//				"emailTIPattern","stateTIState", "stateTIStateCode","postalCodeTIState","isRecordEligibleComp","isRecordEligibleCon",
//				"titleTILevel", "titleTIFunction","titleTISubfunction", "phoneTICountry","phoneTIFormattedNo", "companyTTF","countryTTF",
//				"emailTTF","fullNameTTF","phoneTTF","postalCodeTTF","stateTTF","titleTTF","websiteTTF","countryTICountry","fullNameTICleanNameBeforeSplit",
//				"phoneTICountryCode","postalCodeTICity","websiteTIDescription","websiteTIKeywords","websiteTIKeywords","websiteTIWebsite",
//				"isEmailJunk","isConNameJunk","isCompNameJunk","isEmailEligible","isConNameEligible","isCompNameEligible","isCompGuidEligible",
//				"isCompWebEligible");
//			
//			tran.updateExcelCell(lstPEOutput, sheet, row, res);
//		}
//			
//			String fileName = file.substring(file.lastIndexOf('/') + 1);
//			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
//			
//			//File fi = new File(newFile);
//			try {
//				//fi.delete();
//				ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//				
//		for (String str : lstStatus) {
//			if (str.equals("Fail")) {
//				Assert.fail("Contact tranalyzer has failed");
//			}
//		}
//	}
//		catch(Exception ex)
//		{
//			String fileName = file.substring(file.lastIndexOf('/') + 1);
//			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
//			ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
//		}
	
	}
	
	@Test
	public void GenericPEAPITranalyzerTest1() throws Exception {
		XSSFSheet sheet= null;
		List<String> lstStatus = new ArrayList<String>();

		String apifile = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/PersonMB.csv").getPath().replace("%20", " ").replaceFirst("/", "");

		String writefile = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/PersonMBWrite.csv").getPath().replace("%20", " ").replaceFirst("/", "");
		
		//new CsvDataManage(Paths.get("G:/PersonMB.csv"), Paths.get("G:/PersonMBWrite.csv")).apply();
		
//		CSVReader reader = new CSVReader(new FileReader("G:/PersonMB.csv"));
//		CSVWriter writer = new CSVWriter(new FileWriter("G:/PersonMBWriter.csv"));
		
		CSVReader reader = new CSVReader(new FileReader(apifile));
		CSVWriter writer = new CSVWriter(new FileWriter(writefile));
		 
		 String[] nextLine;
		 int lineNumber=0;
		
		 
	      while ((nextLine = reader.readNext()) != null) {
	         if (nextLine != null) {
	            //Verifying the read data here
	        	 if(lineNumber != 0)
	        	 {
	        		hm.put("Id", nextLine[0]);
	        	 	hm.put("PersonGuid",  nextLine[1]);
	        	 	hm.put("FirstName",  nextLine[2]);
	        	 	hm.put("LastName",  nextLine[3]);
	        	 	hm.put("FullName", nextLine[4]);
	        	 	hm.put("Email", nextLine[5]);
	        	 	hm.put("JobTitle", nextLine[6]);
	        	 	hm.put("CompGuid", nextLine[7]);
	        	 	hm.put("FullName", nextLine[8]);
	        	 	hm.put("Company", nextLine[9]);
	        	 	
	        	 	 Response res;
	        	 	 
	        	 @SuppressWarnings("unchecked")
	   			 Enumeration<String> enums = (Enumeration<String>) prop2.propertyNames();
	   			 List<String> lstPEOutput = new ArrayList<String>();
	   			 while (enums.hasMoreElements()) {
	   				 String key = enums.nextElement();
	   				 String value = prop2.getProperty(key);
	   		
	   				 lstPEOutput.add(value);
	   				    }
	   			
	   			res = tran.BuildPEInputJson(hm, URL, prop.getProperty("mode"));
	   			System.out.println(res.asString());
	   				
	   			tran.updateCsv(lstPEOutput, writer, prop2,res);
	         }	
	       else
	        {
	        	String[] header = { "Name", "Class", "Marks" }; 
		        writer.writeNext(header);
	         }
	        	 lineNumber++;
	        	 
	         }
	         
	      }
	      writer.close();
	}
  }




