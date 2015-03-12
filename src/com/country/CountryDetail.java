package com.country;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.country.adapter.CountryDetailListAdapter;
import com.country.model.CountryDetailModel;
import com.country.utilities.ApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/** 
 * @author Thomas
 * @version 1.0
 * CountryDetail.java - This class is used to display the country detail data in the list view **/
public class CountryDetail extends Activity{
	
	Activity 		thisActivity;
	ListView 		listCountryDetail;
	TextView 		lblActionTitle,lblNoRecordsFound;
	ImageView 		imgRefresh;
	String	 		strTitle = "";
	ProgressDialog 	loading;
	Resources 		res;
	CountryDetailListAdapter detailAdapter;
	List<CountryDetailModel>	ListCountryDetails = new ArrayList<CountryDetailModel>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.country_detail_list);
		thisActivity = this;
		res = getResources();
		init();
	}
	
	/** Initialize the layout file variables and call the webservice call **/
	private void init(){
		listCountryDetail 		= (ListView) findViewById(R.id.listCountryDetail);
		lblNoRecordsFound 		= (TextView) findViewById(R.id.lblNoRecordsFound);
	    
	   if(haveInternet(thisActivity)){
		try {
			getCountryDetail();
		} catch (JSONException e) {
			e.printStackTrace();
			runOnUiThread(new AlertRunnable());
		}
	   }else{
		   showAlert(thisActivity, res.getString(R.string.internet_problem));
	   }
	
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.menu, menu);
	        return true;
	    }

	 	/** Called when the action bar refresh button clicked and refresh the list view **/
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	        //noinspection SimplifiableIfStatement
	        if (id == R.id.action_refresh) {
	            // Code to refresh the view
	        	 if(haveInternet(thisActivity)){
	        			try {
	        				getCountryDetail();
	        			} catch (JSONException e) {
	        				e.printStackTrace();
	        				runOnUiThread(new AlertRunnable());
	        			}
	        		   }else{
	        			   showAlert(thisActivity, res.getString(R.string.internet_problem));
	        		   }
	            return true;
	        }

	        return super.onOptionsItemSelected(item);
	    }
	
	/** 
	 * Called the webservice call using Async Http Client library 
	 * Parsed the return Json value and stored it in the model class
	 **/
	private void getCountryDetail() throws JSONException{
		ApiClient.get(res.getString(R.string.country_detail_url), null, new JsonHttpResponseHandler() {
			
			@Override
			public void onStart() {
				loading = ProgressDialog.show(thisActivity, "", res.getString(R.string.loading));
				if(ListCountryDetails.size() > 0)
					ListCountryDetails.clear();
			}
			
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
					
					try {
						JSONObject objResponse = response;
						if(objResponse.has("title"))
							strTitle = objResponse.getString("title");
						
						ActionBar actionBar = getActionBar();
						actionBar.setTitle(strTitle); 

						
						if(objResponse.has("rows")){
							JSONArray rowsArray =  objResponse.getJSONArray("rows");
							 for(int i=0; i < rowsArray.length(); i++){  
			                        JSONObject rowsObject = rowsArray.getJSONObject(i);  
			                        CountryDetailModel countryData = new CountryDetailModel();
			                        
			                        if(rowsObject.has("title")){
			                        	if(rowsObject.isNull("title")){
			                        		countryData.strTitle = "";
			                        	}else{
			                        		countryData.strTitle = rowsObject.getString("title");
			                        	}
			                        	
			                        }
			                        
			                        if(rowsObject.has("description")){
			                        	if(rowsObject.isNull("description")){
											countryData.strDesc = "";
			                        	}else{
											countryData.strDesc = rowsObject.getString("description");
			                        	}
			                        }
			                        
			                        if(rowsObject.has("imageHref")){
			                        	if(rowsObject.isNull("imageHref")){
			                        		countryData.strImageUrl = "";
			                        	}else{
											countryData.strImageUrl = rowsObject.getString("imageHref");
			                        	}
			                        }
			                           
			                         ListCountryDetails.add(countryData);
			                      } 
						}
							
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
			    	runOnUiThread(new AlertRunnable(statusCode));

            }
            
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers,
            		String responseString, Throwable throwable) {
		    	runOnUiThread(new AlertRunnable(statusCode));

            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers,
            		Throwable throwable, JSONArray errorResponse) {
		    	runOnUiThread(new AlertRunnable(statusCode));

            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers,
            		Throwable throwable, JSONObject errorResponse) {
            	
		    	runOnUiThread(new AlertRunnable(statusCode));

            }
        });
	}
	
	
	/** 
	 * Used to call the Adapter class to set display the list view
	 **/
	class AlertRunnable implements Runnable{
		String message="";
		int status ;
		
		
		AlertRunnable(int status){
			this.status 	= status;
		}
		
		AlertRunnable(){		
		}
		
		@Override
		public void run() {			
			if (loading != null && loading.isShowing()){
				loading.dismiss();
			}	
			
			if(status == 200){
				if (ListCountryDetails.size() > 0) {
					lblNoRecordsFound.setVisibility(View.GONE);
					listCountryDetail.setVisibility(View.VISIBLE);
					if (detailAdapter == null) {
						detailAdapter = new CountryDetailListAdapter(thisActivity,ListCountryDetails);
						listCountryDetail.setAdapter(detailAdapter);
					} else {
						detailAdapter.notifyDataSetChanged();
						detailAdapter.setList(ListCountryDetails);
					}
				} else {
					lblNoRecordsFound.setVisibility(View.VISIBLE);
					listCountryDetail.setVisibility(View.GONE);
				}
			}else{
				lblNoRecordsFound.setVisibility(View.VISIBLE);
				listCountryDetail.setVisibility(View.GONE);
			}
			
			
		}
	}
	/** Check the internet connection and return the boolean value **/
	private boolean haveInternet(Context thisActivity) {
		NetworkInfo info = ((ConnectivityManager) thisActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}
	
	/** Used to show alert message 
	 * @param get the String alert message**/
	private void showAlert(Context thisActivity, String alertMsg) {
		try {
			new AlertDialog.Builder(thisActivity)
					.setTitle("About Canada")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).setMessage("" + alertMsg).create().show();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
