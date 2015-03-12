package com.country.utilities;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/** 
 * @author Thomas
 * @version 1.0
 * ApiClient.java - This class is used to create the static Async Http client methods 
 * Used the Async Http client Library to call the webservice call **/
public class ApiClient {
		  private static final String BASE_URL = "";

		  private static AsyncHttpClient client = new AsyncHttpClient();

		  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		      client.get(getAbsoluteUrl(url), params, responseHandler);
		  }

		  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		      client.post(getAbsoluteUrl(url), params, responseHandler);
		  }

		  private static String getAbsoluteUrl(String relativeUrl) {
		      return BASE_URL + relativeUrl;
		  }
		
}
