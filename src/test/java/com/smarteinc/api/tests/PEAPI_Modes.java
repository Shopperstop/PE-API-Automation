package com.smarteinc.api.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.response.Response;
import com.smarteinc.objects.PEApiInputObjects;
import com.smarteinc.peapi.PEAPI_Library;
import com.smarteinc.utility.APIUtility;
import com.smarteinc.utility.ExcelUtility;

public class PEAPI_Modes extends APIBaseTest {
	
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
			String URL = "http://qcpeapi.smarteinc.com/api/v1/enrich";
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
					
			
					if(!keys.containsAll(al))
						flag = false;
					System.out.println("Flag" + flag);
//					
	
					
//					for(String item : al)
//					{
//						System.out.println(keys.contains(item));
//						if(keys.contains(item.trim()))
//							continue;
//					else
//						{
//							flag = false;
//							break;
//						}						
//					}	
					
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
