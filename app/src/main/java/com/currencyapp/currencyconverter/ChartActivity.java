package com.currencyapp.currencyconverter;

import java.io.InputStream;
import java.net.URL;

import com.currencyapp.currencyconverter.MainActivity.MAPS;
import com.currencyapp.currencyconverter.R;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.view.Gravity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ChartActivity extends Activity{
	
	private ImageView imgchart;
	private String time, spinnerone, spinnertwo;
	com.google.android.gms.ads.AdRequest adRequest;
	private String url = "http://chart.finance.yahoo.com/z?s=";
	//GBPINR=x&t=3m&q=l&l=on&z=m";
	private String sub_url = "&q=l&l=on&z=m";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		AdView adView = (AdView) findViewById(R.id.adView);
		// Request for Ads
		adRequest = new com.google.android.gms.ads.AdRequest.Builder()
				.build();
		adView.loadAd(adRequest);

		//addmob();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			spinnerone = extras.getString("spinnerone");
			spinnertwo = extras.getString("spinnertwo");
		    time = extras.getString("Time");
		}
		
		imgchart = (ImageView) findViewById(R.id.imageView_chart);
		
		/*try {
			Bitmap bitmap_skip = BitmapFactory.decodeStream((InputStream) 
					new URL()
			.getContent());
			imgchart.setImageBitmap(bitmap_skip);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		new MAPS(time,imgchart).execute();
		
	}
	
	private void addmob() {
		com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(ChartActivity.this);
		adView.setAdUnitId("ca-app-pub-6733180445570119/3279220380");
		adView.setAdSize(AdSize.BANNER);
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.addmob_chart);        
		layout.addView(adView);
		layout.setGravity(Gravity.CENTER);
		com.google.android.gms.ads.AdRequest request = new com.google.android.gms.ads.AdRequest.Builder().build();
		adView.loadAd(request);
	}
	
	public class MAPS extends AsyncTask<String,String,String>{
		String time;
		ImageView img;
		Bitmap bitmap_skip;
		public MAPS(String string, ImageView iv) {
			time = string;
			img = iv;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				bitmap_skip = BitmapFactory.decodeStream((InputStream) 
						new URL(url+spinnerone+spinnertwo+"=x&t="+time+sub_url)
				.getContent());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			img.setImageBitmap(bitmap_skip);
			super.onPostExecute(result);
		}

	}

}
