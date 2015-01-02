package edu.drake.slogun;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyProfile extends Activity {
	
	WebView webView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DADADA")));

	    //If we're up-navigating to the profile page, there won't be an intent passed.  So default to my-profile.
	    String address = "http://slogunapp.appspot.com/app/my-profile";
	    if (getIntent() != null) {
	    	if (getIntent().getExtras() != null) {
	    		if (getIntent().getExtras().getString("url") != null) {
	    			address = getIntent().getExtras().getString("url");
	    		}
	    	}
	    }
	    webView1 = (WebView) findViewById(R.id.webviewProfile);
	    webView1.loadUrl(address); 
	    webView1.setWebViewClient(new MyWebViewClient());

	    WebSettings webSettings = webView1.getSettings();
		webSettings.setJavaScriptEnabled(true);
	}
	
	public void goToSlogans(View view) {
//		Intent intent = new Intent(this, MySlogans.class);
//		startActivity(intent);
	}
	
	public void goToComments(View view) {
//		Intent intent = new Intent(this, MyComments.class);
//		startActivity(intent);
	}
	
	public void goToTopSlogans(View view) {
//		Intent intent = new Intent(this, MyTopSlogans.class);
//		startActivity(intent);
	}
	
	public void goToSettings(View view) {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == R.id.add) {
			Intent intent = new Intent(this, Add.class);
			startActivity(intent);
		}
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, Settings.class);
			startActivity(intent);
		}
		if(id == R.id.action_home) {
			Intent intent = new Intent(this, All.class);
			startActivity(intent);
		}
		if(id == R.id.action_edit_profile) {
			Intent intent = new Intent(this, EditProfile.class);
			startActivity(intent);
		}
		if(id == R.id.action_slogarama) {
			Intent intent = new Intent(this, Slogarama.class);
			startActivity(intent);
		}
		if(id == R.id.action_log_out) {
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void goToSloganPage(String sloganURL){
		Intent intent = new Intent(this, Slogan.class);
		intent.putExtra("url", sloganURL);
		startActivity(intent);
	}
	
	/*
	 * This method gives us custom control over what happens with the links we click.
	 * Additionally, implement custom back-button functionality with @override onKeyDown().
	 */
	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			//This determines whether the user has clicked on either a poem or profile page, 
			//and then sends them to the appropriate activity.
			List<String> temp = Uri.parse(url).getPathSegments();
			if (temp.contains("slogan")) {
				goToSloganPage(url);
				return true; //this should be true to ensure that the page doesn't also open in the parent activity.
			}
			else {
				return false;  //In case it redirects here from "my-profile", for example.
			}
		}
		
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			webView1.loadUrl("file:///android_asset/connectionerror.html");
		}
	}
}
