package edu.drake.slogun;

import java.util.List;

import edu.drake.slogun.Comments.MyWebViewClient;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Settings extends Activity {

	WebView settingsWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DADADA")));
	}
	
	
	/* Button methods */
	public void goToEditProfile(View view) {
		Intent intent = new Intent(this, EditProfile.class);
		startActivity(intent);
	}
	public void goToSlogarama(View view) {
		Intent intent = new Intent(this, Slogarama.class);
		startActivity(intent);
	}
	public void goToLegal(View view) {
		Intent intent = new Intent(this, Legal.class);
		startActivity(intent);
	}
	public void goToLogout(View view) {
		settingsWebView = (WebView) findViewById(R.id.webviewSettings);
		settingsWebView.loadUrl("http://slogunapp.appspot.com/_ah/logout?continue=https://www.google.com/accounts/Logout%3Fcontinue%3Dhttps://appengine.google.com/_ah/logout%253Fcontinue%253Dhttp://slogunapp.appspot.com/slogunapp.appspot.com/app/listing/new%26service%3Dah");
		settingsWebView.setWebViewClient(new MyWebViewClient());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
		if(id == R.id.action_profile) {
			Intent intent = new Intent(this, MyProfile.class);
			intent.putExtra("url", "http://slogunapp.appspot.com/app/my-profile");
			startActivity(intent);
		}
		if(id == R.id.action_log_out) {
			settingsWebView = (WebView) findViewById(R.id.webviewSettings);
			settingsWebView.loadUrl("http://slogunapp.appspot.com/_ah/logout?continue=https://www.google.com/accounts/Logout%3Fcontinue%3Dhttps://appengine.google.com/_ah/logout%253Fcontinue%253Dhttp://slogunapp.appspot.com/slogunapp.appspot.com/app/listing/new%26service%3Dah");
			settingsWebView.setWebViewClient(new MyWebViewClient());
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			//Switch the shared pref value.
			SharedPreferences settings = getSharedPreferences("PrefsFile", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("hasSignedIn", false);
			editor.commit();
			
			finishLogout();
		}
	}
	
	public void finishLogout() {
		Intent intent = new Intent(this, StartActivity.class);
		startActivity(intent);
	}
}
