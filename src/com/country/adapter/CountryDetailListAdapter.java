package com.country.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.country.R;
import com.country.model.CountryDetailModel;
import com.squareup.picasso.Picasso;
/** 
 * @author Thomas
 * @version 1.0
 * CountryDetailListAdapter.java - This class get the List data and set the value in the inflate file
 * Used Picasso Library to display the images in the List View **/
public class CountryDetailListAdapter extends BaseAdapter{
	List<CountryDetailModel> countryDetail;
	Activity thisActivity;
	LayoutInflater inflater;
	
	
	public CountryDetailListAdapter(Activity thisActivity, List<CountryDetailModel> countryDetail){
		this.thisActivity  = thisActivity;
		this.countryDetail = countryDetail;		
		inflater = thisActivity.getLayoutInflater();	
	}

	static class ViewHolder {
		TextView lblTitle,lblDesc;	
		ImageView imgHref;
		LinearLayout lnrInflater;
	}
	
	@Override
	public int getCount() {
		return countryDetail.size();
	}

	@Override
	public Object getItem(int position) {
		return countryDetail.get(position);
	}
	
	public void setList(List<CountryDetailModel> bookList){
		this.countryDetail = bookList;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/** Initialize the List view inflate file views and set the values to views**/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		CountryDetailModel countryData  = countryDetail.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView				= inflater.inflate(R.layout.country_detail_list_inflate, null);
			holder.lnrInflater		=  (LinearLayout) convertView.findViewById(R.id.lnrInflater);
			holder.lblTitle		= (TextView) convertView.findViewById(R.id.lblTitle);
			holder.lblDesc	= (TextView) convertView.findViewById(R.id.lblDesc);
			holder.imgHref	= (ImageView) convertView.findViewById(R.id.imgHref);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		holder.lblTitle.setText(countryData.strTitle);
		holder.lblDesc.setText(countryData.strDesc);
		
		if(countryData.strImageUrl != null && !countryData.strImageUrl.equals("")){
			Picasso.with(thisActivity)
	        .load(countryData.strImageUrl)
	        .error(R.drawable.ic_launcher)
	        .into(holder.imgHref);
		}else{
			holder.imgHref.setImageResource(R.drawable.ic_launcher);
		}
		
		
		
		return convertView;
	}
	
	

}
