package com.currencyapp.currencyconverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.currencyapp.currencyconverter.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.os.CountDownTimer;
import android.view.Gravity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class CurrencyActivity extends Activity{


	com.google.android.gms.ads.AdRequest adRequest;
	private Spinner spin_one;
	private String [] val_one; 
	private EditText edt;
	private Button btn;
	private String data;
	private TextView txt_euro , txt_dollar , txt_pound , txt_canadian , txt_australian , txt_indian;
	private String spinnervalue_one="USD";
	private ProgressDialog pd;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_currency);

		pd = new ProgressDialog(CurrencyActivity.this);
		pd.setMessage("Loading Please Wait");




		AdView adView = (AdView) findViewById(R.id.adView);
		// Request for Ads
		adRequest = new com.google.android.gms.ads.AdRequest.Builder()
				.build();
		adView.loadAd(adRequest);





		//addmob();
		
		spin_one = (Spinner) findViewById(R.id.spinner_currency);
		edt = (EditText) findViewById(R.id.edt_basecurrency);
		btn = (Button) findViewById(R.id.btn_basecurrency);
		txt_euro = (TextView) findViewById(R.id.textView_euro);
		txt_pound = (TextView) findViewById(R.id.textView_pound);
		txt_dollar = (TextView) findViewById(R.id.textView_dollar);
		txt_canadian = (TextView) findViewById(R.id.textView_canadian);
		txt_australian = (TextView) findViewById(R.id.textView_australian);
		txt_indian = (TextView) findViewById(R.id.textView_indian);		
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.spinnerone_value, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		val_one  = getResources().getStringArray(R.array.spinnerone_name);
		
		spin_one.setAdapter(adapter);
		
		spin_one.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spinnervalue_one = val_one[arg2];
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pd.show();
				
				data = edt.getText().toString();
				
				
				if(data!=null){
					/*euro(spinnervalue_one);
					pound(spinnervalue_one);
					dollar(spinnervalue_one);
					australian(spinnervalue_one);
					canadian(spinnervalue_one);
					indian(spinnervalue_one);*/
					new Euro(spinnervalue_one,txt_euro,"EUR",1).execute();
					new Euro(spinnervalue_one,txt_pound,"GBP",2).execute();
					new Euro(spinnervalue_one,txt_dollar,"USD",3).execute();
					new Euro(spinnervalue_one,txt_australian,"AUD",4).execute();
					new Euro(spinnervalue_one,txt_canadian,"CAD",5).execute();
					new Euro(spinnervalue_one,txt_indian,"INR",6).execute();
				}
			}

		});
	}


	void loadInterstitL(){
		new CountDownTimer(5000,5000){

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {

			}
		}.start();
	}


	
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
		com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(CurrencyActivity.this);
		adView.setAdUnitId("ca-app-pub-6733180445570119/3279220380");
		adView.setAdSize(AdSize.BANNER);
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.addmob_currency);        
		layout.addView(adView);
		layout.setGravity(Gravity.CENTER);
		com.google.android.gms.ads.AdRequest request = new com.google.android.gms.ads.AdRequest.Builder().build();
		adView.loadAd(request);
	}
	
	public class Euro extends AsyncTask<String,String,String>{
		String to,from;
		TextView txt;
		Double print,finalValue;
		int pdstop;
		public Euro(String spinnervalue_one, TextView tv,String cur,int i) {
			to = spinnervalue_one;
			txt = tv;
			from = cur;
			pdstop = i;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String s = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22"+to+from+"%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");	                    
				JSONObject jObj;
				jObj = new JSONObject(s);
				String exResult = jObj.getJSONObject("query").getJSONObject("results").getJSONObject("rate").getString("Rate");
				Double value = Double.valueOf(exResult);
				Double val_edt = Double.valueOf(edt.getText().toString());
				print = value*val_edt;
				
				DecimalFormat df=new DecimalFormat("##.##");
				String formate = df.format(print); 
				finalValue = (Double)df.parse(formate) ;
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			txt.setText(""+finalValue);
			if(pdstop == 6){
				pd.dismiss();
			}
			super.onPostExecute(result);
		}
		
	}
}

