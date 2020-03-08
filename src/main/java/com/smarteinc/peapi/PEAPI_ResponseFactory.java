package com.smarteinc.peapi;

import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.response.Response;
import com.smarteinc.core.ResponseService;

public class PEAPI_ResponseFactory extends ResponseService {
	

	public PEAPI_ResponseFactory(String app) {
		super(app);
		// TODO Auto-generated constructor stub
	}
	
	public PEAPI_ResponseFactory() {
		this(app_name);
	}
	
	
	static Map<String, String> headers = new HashMap<String, String>();
	
	
	public static Map<String, String> getHeadersWithMode(String mode) {
		headers.put("mode", mode);
		return headers;
	}
	
	private static String app_name = "api";

	public Response peResponse(String url, String jsonBody, String mode )
	{
		return getResponse(url, jsonBody, null, "application/json",getHeadersWithMode(mode), RequestMethodType.POST, false);
	}




}
