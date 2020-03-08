package com.smarteinc.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.restassured.response.Response;

public class APIUtility {
	
	public static String getJsonBody(String... str) {
		Map<String, String> m = new HashMap<String, String>();
		for (int i = 0; i < str.length; i += 2) {
			m.put(str[i], str[i + 1]);
		}
		String jsonString = new GsonBuilder().disableHtmlEscaping().create().toJson(m);
		return jsonString;

	}
	
	public static String getJsonBody(Object obj) {
		GsonBuilder builder = new GsonBuilder(); 
	    builder.setPrettyPrinting(); 
	      
	    Gson gson = builder.create(); 
		String str = gson.toJson(obj);
		return str;
	}
	
	
	public static String getMemberValueAsString(Response res, String memberName) {
		String memberValue = res.jsonPath().getString(memberName);
		if (memberValue != null) {
			memberValue = memberValue.replaceAll("\"", "");
		}
		return memberValue;
	}
	
	/***
	 * Will return Current Date in the passed format
	 * 
	 * @param date
	 *            - Pass the format of date
	 * @return Returns current date See :{@link java.text.DateFormat#format(Date)}
	 */
	public static String getCurrentDate(String date) {

		DateFormat df = new SimpleDateFormat(date);
		Date d = new Date();
		return df.format(d);
	}


}
