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
import java.util.Set;

import org.apache.bcel.generic.LSTORE;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.carrotsearch.hppc.CharSet;
import com.google.gson.Gson;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.mongodb.util.JSON;
import com.smarteinc.objects.PEApiInputObjects;
import com.smarteinc.objects.PIPLInputObjects;
import com.smarteinc.peapi.PEAPI_Library;
import com.smarteinc.utility.APIUtility;
import com.smarteinc.utility.ExcelUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.jayway.restassured.RestAssured.given;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.testng.Reporter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class MatchBackTest extends APIBaseTest {
	Properties prop = new Properties();
	Properties prop2 = new Properties();
	
	PEApiInputObjects obj;
	
	private static Logger logger = initializeLogger(new MatchBackTest());

	public static Logger initializeLogger(Object classObject) {
		System.setProperty("logDirectory", "..\\com.smarteinc.automation\\logs");
		logger = LogManager.getLogger(classObject.getClass().getSimpleName());

		return logger;
	}

	@BeforeTest
	public void beforeTest() throws IOException, IOException {
	    obj = new PEApiInputObjects();
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
				
		obj.setComapnyId("");
		obj.setCompGuid("");
		obj.setContactFirstName("Poonam");
		obj.setContactMiddleName("");
		obj.setContactLastName("Jha");
		obj.setContactFullName("");
		obj.setContactEmail("poonam.jha@smarteinc.com");
		obj.setContactJobTitle("");
		obj.setCompanyName("Smarteinc");	
		obj.setContactState("MH");
		obj.setContactZipCode("");
		obj.setContactCountry("India");
		obj.setContactPhone("");
		obj.setCompanyWebAddress("");
		obj.setContactGuid("");
	}

	//@Test
	public void GenericPEAPIMatchBackTestSet1() throws Exception {

		logger.info("Start Test");
		// System.out.println(getClass().getClassLoader().getResource("logging.properties"));

		XSSFSheet sheet = null;
		List<String> lstStatus = new ArrayList<String>();

		String file = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/RSIDataToRun-1.xlsx").getPath()
				.replace("%20", " ").replaceFirst("/", "");
		try {

			String URL = "http://mcharpeapi.smarteinc.com/api/v1/enrich";
			sheet = ExcelUtility.openSpreadSheet(file, "Sheet1");
			int lastRow = sheet.getLastRowNum();

			Map<String, String> hm = new HashMap<String, String>();
			PEAPI_Library tran = new PEAPI_Library();

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
				// logger.info(ExcelUtility.getCellData(sheet, row,
				// prop.getProperty("Company")));

				obj.setContactState(ExcelUtility.getCellData(sheet, row, prop.getProperty("State")));
				obj.setContactZipCode(ExcelUtility.getCellData(sheet, row, prop.getProperty("PostalCode")));
				obj.setContactCountry(ExcelUtility.getCellData(sheet, row, prop.getProperty("Country")));
				obj.setContactPhone(ExcelUtility.getCellData(sheet, row, prop.getProperty("Phone")));
				obj.setCompanyWebAddress(ExcelUtility.getCellData(sheet, row, prop.getProperty("Website")));
				obj.setContactGuid(ExcelUtility.getCellData(sheet, row, prop.getProperty("PersonGuid")));

				String strBody = APIUtility.getJsonBody(obj);

				//Response res = tran.getResponseForPEApi(strBody, URL, prop.getProperty("mode"));

				@SuppressWarnings("unchecked")
				Enumeration<String> enums = (Enumeration<String>) prop2.propertyNames();
				List<String> lstPEOutput = new ArrayList<String>();
				while (enums.hasMoreElements()) {
					String key = enums.nextElement();
					String value = prop2.getProperty(key);

					lstPEOutput.add(value);
				}
				// tran.updateExcelCell(lstPEOutput, sheet, row, res);
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

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			Assert.fail("PE API script has failed");
		}

	}

	//@Test
	public void PIPL_Test() throws Exception {
		Response res = null;
		XSSFSheet sheet = null;

		String personalEmail = "", personEmailType = "";
		String officeEmail = "", officeEmailType = "";
		String phoneType = "", mobile, mobilePhoneNo, mobileInternation = "", mobileLandline = "";

		String jobsValidSince, jobsOrg;
		String nameValidSince, nameDisplay, addressValidSince, addressCountry;

		List<String> lstStatus = new ArrayList<String>();

		String file = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/Final.xlsx").getPath();
		// .replace("%20", " ").replaceFirst("/", "");
		try {

			String URL = "https://api.pipl.com/search/";
			sheet = ExcelUtility.openSpreadSheet(file, "combination");
			int lastRow = sheet.getLastRowNum();

			Map<String, String> hm = new HashMap<String, String>();
			PEAPI_Library tran = new PEAPI_Library();

			PIPLInputObjects obj = new PIPLInputObjects();

			for (int row = 1; row <= lastRow; row++) {
				// Input fields

				System.out.println("row" + row);
				String fName = ExcelUtility.getCellData(sheet, row, "FirstName");
				String lName = ExcelUtility.getCellData(sheet, row, "LastName");
				String mName = ExcelUtility.getCellData(sheet, row, "MiddleName");
				String email = ExcelUtility.getCellData(sheet, row, "Email");

				RestAssured.baseURI = "https://api.pipl.com/search/";
				res = given().queryParam("email", email).queryParam("key", "1j0xqe9xthat6iefdjq0nai2")
						.queryParam("first_name", fName).queryParam("last_name", lName).queryParam("middle_name", mName)
						.queryParam("top_match", "true").contentType("application/x-www-form-urlencoded").when().get();

				JsonObject personObject = APIUtility.getMemberValue(res, "person").getAsJsonObject();

				if (personObject != null) {

					JsonArray emailObject = APIUtility.getMemberValue(res, "person/emails").getAsJsonArray();

					int size = emailObject.size();

					for (int i = 0; i < size; i++) {

						String emailType = APIUtility.getMemberValue(res, "person/emails[" + i + "]/@type")
								.getAsString();

						if (emailType.equalsIgnoreCase("work")) {
							officeEmailType = emailType;
							officeEmail = APIUtility.getMemberValue(res, "person/emails[" + i + "]/address")
									.getAsString();
						} else if (emailType.equalsIgnoreCase("personal")) {
							personEmailType = emailType;
							personalEmail = APIUtility.getMemberValue(res, "person/emails[" + i + "]/address")
									.getAsString();
						}
					}

					// JsonObject phoneObject = APIUtility.getMemberValue(res,
					// "phones").getAsJsonObject();

					// Get Phone section
					JsonArray phoneObject = APIUtility.getMemberValue(res, "person/phones").getAsJsonArray();

					for (int i = 1; i < phoneObject.size(); i++) {

						phoneType = APIUtility.getMemberValue(res, "person/phones[0]/@type").getAsString();

						mobile = phoneType;
						mobilePhoneNo = APIUtility.getMemberValue(res, "person/phones[0]/display_international")
								.getAsString();

						mobileLandline = "International";
						mobileInternation = mobileInternation + "," + APIUtility
								.getMemberValue(res, "person/phones[" + i + "]/display_international").getAsString();

					}

					// Get jobs section
					// JsonArray jobObject = APIUtility.getMemberValue(res,
					// "person/jobs").getAsJsonArray();

					// for(int i=0 ; i < jobObject.size() ; i++)
					// {

					jobsValidSince = APIUtility.getMemberValue(res, "person/jobs[0]/@valid_since").getAsString();
					jobsOrg = APIUtility.getMemberValue(res, "person/jobs[0]/organization").getAsString();
					// }

					nameValidSince = APIUtility.getMemberValue(res, "person/names[0]/@valid_since").getAsString();
					nameDisplay = APIUtility.getMemberValue(res, "person/names[0]/display").getAsString();

					addressValidSince = APIUtility.getMemberValue(res, "person/addresses[0]/@valid_since")
							.getAsString();
					addressCountry = APIUtility.getMemberValue(res, "person/addresses[0]/display").getAsString();

					// String strBody = APIUtility.getJsonBody(obj);

					// Response res = tran.getResponseFor(strBody, URL);

//			@SuppressWarnings("unchecked")
//			Enumeration<String> enums = (Enumeration<String>) prop2.propertyNames();
//			List<String> lstPEOutput = new ArrayList<String>();
//			while (enums.hasMoreElements()) {
//			      String key = enums.nextElement();
//			      String value = prop2.getProperty(key);
//	
//			      lstPEOutput.add(value);
//			    }

					List<String> lstPIPLOutput = new ArrayList<String>();
					lstPIPLOutput.add(personEmailType);
					lstPIPLOutput.add(personalEmail);
					lstPIPLOutput.add(officeEmailType);
					lstPIPLOutput.add(officeEmail);
					lstPIPLOutput.add(phoneType);
					lstPIPLOutput.add(mobileLandline);
					lstPIPLOutput.add(mobileInternation);
					lstPIPLOutput.add(jobsValidSince);
					lstPIPLOutput.add(jobsOrg);
					lstPIPLOutput.add(nameValidSince);
					lstPIPLOutput.add(nameDisplay);
					lstPIPLOutput.add(addressValidSince);
					lstPIPLOutput.add(addressCountry);
					lstPIPLOutput.add(res.asString());

					tran.updateExcelCell(lstPIPLOutput, sheet, row);
				}

			}

			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;

			try {

				ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		catch (Exception ex) {
			System.out.println(res.asString());
			System.out.println(ex.getMessage());
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			Assert.fail("PE API script has failed");
		}

	}

	//@Test
	public void PIPL_POC_Test() throws Exception {
		Response res = null;
		XSSFSheet sheet = null;

//		String personalEmail = "", personEmailType = "";
//		String officeEmail = "", officeEmailType = "";
//		String phoneType = "", mobile, mobilePhoneNo, mobileInternation = "", mobileLandline = "";
//
//		String jobsValidSince, jobsOrg;
//		String nameValidSince, nameDisplay, addressValidSince, addressCountry;

		List<String> lstStatus = new ArrayList<String>();

		String file = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/Final.xlsx").getPath();
		// .replace("%20", " ").replaceFirst("/", "");
		try {

			String URL = "https://api.pipl.com/search/";
			sheet = ExcelUtility.openSpreadSheet(file, "Sheet1");
			int lastRow = sheet.getLastRowNum();

			Map<String, String> hm = new HashMap<String, String>();
			PEAPI_Library tran = new PEAPI_Library();

			PIPLInputObjects obj = new PIPLInputObjects();

			for (int row = 1; row <= lastRow; row++) {
				// Input fields

				System.out.println("Row " + row);
				String fullName=  ExcelUtility.getCellData(sheet, row, "FullName");
				String firstName = ExcelUtility.getCellData(sheet, row, "FirstName");
				String lastName = ExcelUtility.getCellData(sheet, row, "LastName");
				String middleName = ExcelUtility.getCellData(sheet, row, "MiddleName");
				String email = ExcelUtility.getCellData(sheet, row, "Email");
				

				String key = "1j0xqe9xthat6iefdjq0nai2";
				String match_requirements = "phone";
				String top_match = "true";
				String ContentType = "application/x-www-form-urlencoded";

//				System.out.println("firstName :" + firstName);
//				System.out.println("lastName : " + lastName);
//				System.out.println("middleName :" + middleName);
//				System.out.println("email :" + email);
//
//				System.out.println("key : " + key);
//				System.out.println("match_requirements : " + match_requirements);
//				System.out.println("top_match : " + top_match);
//				System.out.println("ContentType : " + ContentType);

				
				RestAssured.baseURI= "https://api.pipl.com/search/"; 
				res =	given().queryParam("email", email)						
						.queryParam("first_name", firstName)
						.queryParam("last_name", lastName) 
						.queryParam("middle_name", middleName)
						.queryParam("raw_name", fullName)
						.queryParam("top_match", "true")
						.queryParam("key", "1j0xqe9xthat6iefdjq0nai2") 
						.queryParam("match_requirements", "phone") 
						.contentType("application/x-www-form-urlencoded")
						.when() .get();
				 

			List<String> lstPIPLOutput = new ArrayList<String>();		
//			lstPIPLOutput.add(personEmailType);
//			lstPIPLOutput.add(personalEmail);
//			lstPIPLOutput.add(officeEmailType);
//			lstPIPLOutput.add(officeEmail);
//			lstPIPLOutput.add(phoneType);
//			lstPIPLOutput.add(mobileLandline);
//			lstPIPLOutput.add(mobileInternation);
//			lstPIPLOutput.add(jobsValidSince);
//			lstPIPLOutput.add(jobsOrg);
//			lstPIPLOutput.add(nameValidSince);
//			lstPIPLOutput.add(nameDisplay);
//			lstPIPLOutput.add(addressValidSince);
//			lstPIPLOutput.add(addressCountry);
			
			lstPIPLOutput.add(res.asString());
		
				tran.updateExcelCell(lstPIPLOutput, sheet, row);
				// tran.updateExcelCell(lstPIPLOutput, sheet, row);
			}

			// }

			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;

			try {

				ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		catch (Exception ex) {
			System.out.println(res.asString());
			System.out.println(ex.getMessage());
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			Assert.fail("PE API script has failed");
		}

	}

	
	@Test
	public void TestPEAPI_Client_Mode() throws Exception {
		logger.info("Start Test");
		XSSFSheet sheet = null;
		List<String> al = new ArrayList<String>();

		String file = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/PEAPI_client.xlsx").getPath();

		try {
			String URL = "http://qcpeapi.smarteinc.com/api/v1/enrich";
			sheet = ExcelUtility.openSpreadSheet(file, "Sheet1");
			int lastRow = sheet.getLastRowNum();

			Map<String, String> hm = new HashMap<String, String>();
			PEAPI_Library peLib = new PEAPI_Library();
				
			for (int row = 1; row <= lastRow; row++) {
				
				hm.put("client-id", ExcelUtility.getCellData(sheet, row, "client-id"));
				hm.put("input-value", ExcelUtility.getCellData(sheet, row, "input-value"));
				hm.put("ev-value", ExcelUtility.getCellData(sheet, row, "ev-value"));
				hm.put("scope-value", ExcelUtility.getCellData(sheet, row, "scope-value"));
				hm.put("output-value", ExcelUtility.getCellData(sheet, row, "output-value"));
				hm.put("compmb-value", ExcelUtility.getCellData(sheet, row, "compmb-value"));
				hm.put("index-value", ExcelUtility.getCellData(sheet, row, "index-value"));

				String outputFlagsToMatch = ExcelUtility.getCellData(sheet, row, "matchOutputFlags");
				
				String  lstOutputFlag[] = outputFlagsToMatch.split(",");				
				
				al = Arrays.asList(lstOutputFlag);				
			
				String strBody = APIUtility.getJsonBody(obj);

				//Response res = tran.getResponseForPEApi(strBody, URL, prop.getProperty("mode"));
				
				Response res = peLib.getResponseForPEApi(strBody, URL, hm );
								
				JsonParser parser = new JsonParser();
				JsonObject jo = parser.parse(res.asString()).getAsJsonObject();
								
				List<String> keys = jo.entrySet()
						.stream()
						.map(i -> i.getKey())
						.collect(Collectors.toCollection(ArrayList::new));

				keys.forEach(System.out::println);
				
//				boolean flag = false;
//				if(keys.containsAll(al))
//					flag = true;
//				System.out.println("Flag" + flag);
//				
				boolean flag = true;
				
				for(String item : al)
				{
					System.out.println(keys.contains(item));
					if(keys.contains(item.trim()))
						continue;
				else
					{
						flag = false;
						break;
					}						
				}			

				// tran.updateExcelCell(lstPEOutput, sheet, row, res);
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

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			ExcelUtility.saveChangesToAnother(newFile, sheet.getWorkbook());
			Assert.fail("PE API script has failed");
		}

	}

	@Test
	public void TestPEAPI_WithMode() throws Exception {
		logger.info("Start Test");
		XSSFSheet modeSheet = null;
		XSSFSheet dataSheet = null;
		List<String> al = new ArrayList<String>();
		boolean flag = true;
		
		String file = PEAPI_Generic_Tran_tests.class.getResource("/TestData/MatchBack/PEAPI_client.xlsx").getPath();

		try {
			String URL = "http://dapeapi.smarteinc.com/api/v1/enrich";
			modeSheet = ExcelUtility.openSpreadSheet(file, "Mode");
			int lastModeRow = modeSheet.getLastRowNum();

			dataSheet = ExcelUtility.openSpreadSheet(file, "data");
			int lastDataRow = dataSheet.getLastRowNum();
			
			Map<String, String> hm = new HashMap<String, String>();
			PEAPI_Library peLib = new PEAPI_Library();
			
			for (int row = 1; row <= lastDataRow; row++) {
				
				// Input fields
				obj.setComapnyId(ExcelUtility.getCellData(dataSheet, row, "companyId"));
				obj.setCompGuid(ExcelUtility.getCellData(dataSheet, row, "contactGuid"));
				obj.setContactFirstName(ExcelUtility.getCellData(dataSheet,row, "contactFirstName"));

				obj.setContactMiddleName(ExcelUtility.getCellData(dataSheet, row, "contactMiddleName"));
				obj.setContactLastName(ExcelUtility.getCellData(dataSheet, row, "contactLastName"));
				obj.setContactFullName(ExcelUtility.getCellData(dataSheet, row, "contactFullName"));
				obj.setContactEmail(ExcelUtility.getCellData(dataSheet, row, "contactEmail"));
				obj.setContactJobTitle(ExcelUtility.getCellData(dataSheet, row, "contactJobTitle"));
				obj.setCompanyName(ExcelUtility.getCellData(dataSheet, row, "companyName"));
				// logger.info(ExcelUtility.getCellData(sheet, row,
				// prop.getProperty("Company")));

				obj.setContactState(ExcelUtility.getCellData(dataSheet, row, "contactState"));
				obj.setContactZipCode(ExcelUtility.getCellData(dataSheet, row, "contactZipcode"));
				obj.setContactCountry(ExcelUtility.getCellData(dataSheet, row, "contactCountry"));
				obj.setContactPhone(ExcelUtility.getCellData(dataSheet, row, prop.getProperty("Phone")));
				obj.setCompanyWebAddress(ExcelUtility.getCellData(dataSheet, row, "companyWebAddress"));
				//obj.setContactGuid(ExcelUtility.getCellData(dataSheet, row, prop.getProperty("PersonGuid")));
					
				String strBody = APIUtility.getJsonBody(obj);
				
				for (int rowMode = 1; rowMode <= lastModeRow; rowMode++) {
					
					hm.put("client-id", ExcelUtility.getCellData(modeSheet, row, "client-id"));
					hm.put("input-value", ExcelUtility.getCellData(modeSheet, row, "input-value"));
					hm.put("ev-value", ExcelUtility.getCellData(modeSheet, row, "ev-value"));
					hm.put("scope-value", ExcelUtility.getCellData(modeSheet, row, "scope-value"));
					hm.put("output-value", ExcelUtility.getCellData(modeSheet, row, "output-value"));
					hm.put("compmb-value", ExcelUtility.getCellData(modeSheet, row, "compmb-value"));
					hm.put("index-value", ExcelUtility.getCellData(modeSheet, row, "index-value"));

					String outputFlagsToMatch = ExcelUtility.getCellData(modeSheet, row, "matchOutputFlags");
					
					String  lstOutputFlag[] = outputFlagsToMatch.split(",");				
					
					al = Arrays.asList(lstOutputFlag);				
				
						
					Response res = peLib.getResponseForPEApi(strBody, URL, hm );
									
					JsonParser parser = new JsonParser();
					JsonObject jo = parser.parse(res.asString()).getAsJsonObject();
									
					List<String> keys = jo.entrySet()
							.stream()
							.map(i -> i.getKey())
							.collect(Collectors.toCollection(ArrayList::new));

					keys.forEach(System.out::println);
					
//					boolean flag = false;
//					if(keys.containsAll(al))
//						flag = true;
//					System.out.println("Flag" + flag);
//					
					//flag = true;
					
					for(String item : al)
					{
						System.out.println(keys.contains(item));
						if(keys.contains(item.trim()))
							continue;
					else
						{
							flag = false;
							break;
						}						
					}	
					
					System.out.println("Row" + row);				
				}			
				peLib.updateExcelCell(Boolean.toString(flag), dataSheet, row);
			}
			
			// tran.updateExcelCell(lstPEOutput, sheet, row, res);			

			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;

			logger.info(fileName);
			logger.info(newFile);
			try {

				ExcelUtility.saveChangesToAnother(newFile, dataSheet.getWorkbook());
				logger.info("Saved");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			String fileName = file.substring(file.lastIndexOf('/') + 1);
			String newFile = file.substring(0, file.lastIndexOf('/')) + "/New" + fileName;
			ExcelUtility.saveChangesToAnother(newFile, dataSheet.getWorkbook());
			Assert.fail("PE API script has failed");
		}

	}

}
