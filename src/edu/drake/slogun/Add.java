package edu.drake.slogun;

/*new*/
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

public class Add extends Activity {
	String m;
	final ArrayList <Integer> selectedItems=new ArrayList<Integer>();
	
	String sloganString;
	WebView webView1;
	SwipeRefreshLayout swipeView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
		actionBar.setDisplayHomeAsUpEnabled(true);		
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DADADA")));
		
		webView1 = (WebView) findViewById(R.id.webviewAdd);
	    webView1.loadUrl("http://slogunapp.appspot.com/app/addSlogan");
	    webView1.setWebViewClient(new MyWebViewClient());	    
	    
	    WebSettings webSettings = webView1.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		swipeView1 = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout1);	 
		swipeView1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeView1.setRefreshing(true);
				webView1.reload();
				if (webView1.getUrl().equals("file:///android_asset/connectionerror.html")) {
					webView1.loadUrl("http://slogunapp.appspot.com/app/addSlogan");
				}
				else {
					webView1.reload();
				}
				( new Handler()).postDelayed(new Runnable() {
					@Override
					public void run() {
						swipeView1.setRefreshing(false);
					}
				}, 3000);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
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

		}
		return super.onOptionsItemSelected(item);
	}
	
	public void goToSloganPage(String sloganURL){
		Intent intent = new Intent(this, Slogan.class);
		intent.putExtra("url", sloganURL);
		startActivity(intent);
	}
	
	/*
	 * This class gives us custom control over what happens when the page redirects.
	 * Additionally, implement custom back-button functionality with @override onKeyDown().
	 */
	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//This determines whether the page has redirected to either the slogan or home listing page
			List<String> temp = Uri.parse(url).getPathSegments();
			if (temp.contains("slogan")) { //This is the default, if the slogan has posted to the datastore in time we will be redirected there.
				goToSloganPage(url);
				return true; //this return ensures that the link isn't also opened in the parent activity.
			}
			else if (temp.contains("listing")) { //This is a fallback due to eventual consistency constraints.
				startActivity(new Intent(view.getContext(),All.class));
				return true; //this return ensures that the link isn't also opened in the parent activity.
			}
			else {
				return false; //this return should never be called
			}
		}
		
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			webView1.loadUrl("file:///android_asset/connectionerror.html");
		}
	}
}