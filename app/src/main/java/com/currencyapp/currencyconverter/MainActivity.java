package com.currencyapp.currencyconverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.currencyapp.currencyconverter.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


import android.view.Gravity;

public class MainActivity extends Activity implements OnClickListener{


	com.google.android.gms.ads.AdRequest adRequest;
	private String url = "http://chart.finance.yahoo.com/z?s=";
	//GBPINR=x&t=3m&q=l&l=on&z=m";
	private String sub_url = "&q=l&m=on&z=m";
	private Intent intent;

	/****** Ui views *****/

	private ImageView im_chart, im_viceversa;
	private Button btn_currency;
	private TextView oneday, fiveday, threemonths , sixmoths , oneyear , twoyear , fiveyear;
	private EditText ed_one, ed_two;
	private Spinner spin_one, spin_two;
	private String [] val_one , val_two; 
	private String spinnervalue_one="USD" , spinnervalue_two="INR";
	private ProgressDialog pd;
	private int checkint = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		AdView adView = (AdView) findViewById(R.id.adView);
		// Request for Ads
		adRequest = new com.google.android.gms.ads.AdRequest.Builder()
				.build();
		adView.loadAd(adRequest);

		//addmob();
		initView();
		//map("3m");
		new MAPS("3m",im_chart).execute();




	}

	public void displayInterstitial() {


		new CountDownTimer(1000,1000){

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				// If Ads are loaded, show Interstitial else show nothing.

			}
		}.start();

	}

	private void initView() {

		pd = new ProgressDialog(MainActivity.this);
		pd.setMessage("Loading Please Wait");
		pd.show();

		im_chart = (ImageView) findViewById(R.id.img_chart);
		im_viceversa = (ImageView) findViewById(R.id.img_viceversa);
		btn_currency = (Button) findViewById(R.id.button_currency);
		spin_one = (Spinner) findViewById(R.id.spinner_one); 
		spin_two = (Spinner) findViewById(R.id.spinner_two);
		ed_one = (EditText) findViewById(R.id.edt_one);
		ed_two = (EditText) findViewById(R.id.edt_two);
		oneday = (TextView) findViewById(R.id.textView_oneday);
		fiveday = (TextView) findViewById(R.id.textView_fiveday);
		threemonths = (TextView) findViewById(R.id.textView_threemonth);
		sixmoths = (TextView) findViewById(R.id.textView_sixmonth);
		oneyear = (TextView) findViewById(R.id.textView_oneyear);
		twoyear = (TextView) findViewById(R.id.textView_twoyear);
		fiveyear = (TextView) findViewById(R.id.textView_fiveyear);

		ed_one.setText("1");
		setspinner();
		onclicked();	
		pd.dismiss();
		
		ed_one.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				new SpinOne(0).execute();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});

	}

	private void onclicked() {
		oneday.setOnClickListener(this);
		fiveday.setOnClickListener(this);
		threemonths.setOnClickListener(this);
		sixmoths.setOnClickListener(this);
		oneyear.setOnClickListener(this);
		twoyear.setOnClickListener(this);
		fiveyear.setOnClickListener(this);

		im_viceversa.setOnClickListener(this);
		btn_currency.setOnClickListener(this);
	}

	/*private void map(String time) {
		try {
			Bitmap bitmap_skip = BitmapFactory.decodeStream((InputStream) 
					new URL(url+spinnervalue_one+spinnervalue_two+"=x&t="+time+sub_url)
			.getContent());
			im_chart.setImageBitmap(bitmap_skip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/


	private void setspinner() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.spinnerone_value, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		val_one  = getResources().getStringArray(R.array.spinnerone_name);

		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.spinnertwo_value, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		val_two  = getResources().getStringArray(R.array.spinnertwo_name);


		spin_one.setAdapter(adapter);
		spin_one.setTag(0);
		spin_two.setAdapter(adapter2);
		spin_two.setTag(0);

		spin_one.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				pd.show();
				spinnervalue_one = val_one[arg2];

				spin_one.setTag(arg2);
				
				if(checkint == 1){
					
				//spninone(arg2);
				new Spintwo(arg2).execute();
				
				checkint = 0;
				}
				else{
				new SpinOne(arg2).execute();
				}






			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		spin_two.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				pd.show();
				spinnervalue_two = val_two[arg2];

				spin_two.setTag(arg2);
				//spninone(arg2);
				new SpinOne(arg2).execute();




			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/*private void spninone(int arg2) {
		String geted_one = ed_one.getText().toString();
		if(geted_one!=null){

			try {
				String s = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22"+spinnervalue_one+spinnervalue_two+"%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");	                    
				JSONObject jObj;
				jObj = new JSONObject(s);
				String exResult = jObj.getJSONObject("query").getJSONObject("results").getJSONObject("rate").getString("Rate");
				Double value = Double.valueOf(exResult);
				Double val_edt = Double.valueOf(geted_one);
				Double print = value*val_edt;
				ed_two.setText(""+print);
				pd.dismiss();
				map("3m");
				pd.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
				pd.dismiss();
			}
		}
	}*/
	
	

	public String getJson(String url)throws ClientProtocolException, IOException {

		StringBuilder build = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream content = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		String con;
		while ((con = reader.readLine()) != null) {
			build.append(con);
		}
		return build.toString();
	}

	private void addmob() {
		com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(MainActivity.this);
		adView.setAdUnitId("ca-app-pub-6733180445570119/3279220380");
		adView.setAdSize(AdSize.BANNER);
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.addmob);        
		layout.addView(adView);
		layout.setGravity(Gravity.CENTER);
		com.google.android.gms.ads.AdRequest request = new com.google.android.gms.ads.AdRequest.Builder().build();
		adView.loadAd(request);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_oneday:
			//map("1d");
			new MAPS("1d",im_chart).execute();
			break;
		case R.id.textView_fiveday:
			//map("5d");
			new MAPS("5d",im_chart).execute();
			break;
		case R.id.textView_threemonth:
			//map("3m");
			new MAPS("3m",im_chart).execute();
			break;
		case R.id.textView_sixmonth:
			intent = new Intent (this,ChartActivity.class);
			intent.putExtra("spinnerone", spinnervalue_one);
			intent.putExtra("spinnertwo", spinnervalue_two);
			intent.putExtra("Time", "6m");
			startActivity(intent);
			break;
		case R.id.textView_oneyear:
			intent = new Intent (this,ChartActivity.class);
			intent.putExtra("spinnerone", spinnervalue_one);
			intent.putExtra("spinnertwo", spinnervalue_two);
			intent.putExtra("Time", "1y");
			startActivity(intent);
			break;
		case R.id.textView_twoyear:
			intent = new Intent (this,ChartActivity.class);
			intent.putExtra("spinnerone", spinnervalue_one);
			intent.putExtra("spinnertwo", spinnervalue_two);
			intent.putExtra("Time", "2y");
			startActivity(intent);
			break;
		case R.id.textView_fiveyear:
			intent = new Intent (this,ChartActivity.class);
			intent.putExtra("spinnerone", spinnervalue_one);
			intent.putExtra("spinnertwo", spinnervalue_two);
			intent.putExtra("Time", "5y");
			startActivity(intent);
			break;
		case R.id.button_currency:
			intent = new Intent (this,CurrencyActivity.class);
			startActivity(intent);
			break;
		case R.id.img_viceversa:
			checkint = 1;
			if(ed_one!=null && ed_two!=null){

				int getid1 = (Integer) spin_two.getTag();
				if(getid1==0){
					spin_one.setSelection(1);
					spinnervalue_one = val_one[1];
				}
				else if (getid1==1) {
					spin_one.setSelection(0);
					spinnervalue_one = val_one[0];
				}else{
					spin_one.setSelection(getid1);
					spinnervalue_one = val_one[getid1];
				}


				int getid2 = (Integer) spin_one.getTag();
				if(getid2 == 0){
					spin_two.setSelection(1);
					spinnervalue_two = val_two[1];
				}else if (getid2 ==1) {
					spin_two.setSelection(0);
					spinnervalue_two = val_two[0];
				}
				else{
					spin_two.setSelection(getid2);
					spinnervalue_two = val_two[getid2];
				}
			}
			break;
		default:
			break;
		}		
	}
	
	


	public class SpinOne extends AsyncTask<String,String,String>{
		private int arg2;
		Double print;
		public SpinOne(int arg1) {
			arg2 = arg1;
		}

		@Override
		protected String doInBackground(String... params) {
			String geted_one = ed_one.getText().toString();
			if(geted_one!=null){

				try {
					String s = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22"+spinnervalue_one+spinnervalue_two+"%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");	                    
					JSONObject jObj;
					jObj = new JSONObject(s);
					String exResult = jObj.getJSONObject("query").getJSONObject("results").getJSONObject("rate").getString("Rate");
					Double value = Double.valueOf(exResult);
					Double val_edt = Double.valueOf(geted_one);
					print = value*val_edt;

					//map("3m");
					new MAPS("3m",im_chart).execute();
					pd.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
					pd.dismiss();
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			ed_two.setText(""+print);
			pd.dismiss();
			super.onPostExecute(result);
		}
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
						new URL(url+spinnervalue_one+spinnervalue_two+"=x&t="+time+sub_url)
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
	
	public class Spintwo extends AsyncTask<String,String,String>{
		private int arg2;
		Double print;
		public Spintwo(int arg1) {
			arg2 = arg1;
		}

		@Override
		protected String doInBackground(String... params) {
			String geted_one = ed_one.getText().toString();
			if(geted_one!=null){

				try {
					String s = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22"+spinnervalue_two+spinnervalue_one+"%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");	                    
					JSONObject jObj;
					jObj = new JSONObject(s);
					String exResult = jObj.getJSONObject("query").getJSONObject("results").getJSONObject("rate").getString("Rate");
					Double value = Double.valueOf(exResult);
					Double val_edt = Double.valueOf(geted_one);
					print = value*val_edt;

					//map("3m");
					new MAPS("3m",im_chart).execute();
					pd.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
					pd.dismiss();
				}
			}
			return null;
		}
	}
}
