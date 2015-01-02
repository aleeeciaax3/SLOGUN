package edu.drake.slogun;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import edu.drake.slogun.MyProfile.MyWebViewClient;

public class Comments extends Activity {
	
	WebView webView1;
	SwipeRefreshLayout swipeView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		overridePendingTransition(R.anim.in_right, R.anim.out_left);
		
		setContentView(R.layout.activity_comments);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DADADA")));
	    
	    String address = getIntent().getExtras().getString("url");
	    webView1 = (WebView) findViewById(R.id.webviewComments);
	    webView1.loadUrl(address);
	    webView1.setWebViewClient(new MyWebViewClient());

	    WebSettings webSettings = webView1.getSettings();
		webSettings.setJavaScriptEnabled(true);
	    
	    swipeView1 = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout1);	 
		swipeView1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeView1.setRefreshing(true);
				webView1.reload();
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
		if(id == R.id.action_profile) {
			Intent intent = new Intent(this, MyProfile.class);
			intent.putExtra("url", "http://slogunapp.appspot.com/app/my-profile");
			startActivity(intent);
		}
		if(id == R.id.action_log_out) {
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void goToProfilePage(String profileURL){
		Intent intent = new Intent(this, MyProfile.class);
		intent.putExtra("url", profileURL);
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
	 super.onBackPressed();
	 overridePendingTransition(R.anim.in_left, R.anim.out_right);
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
			if (temp.contains("user")) {
				/* 
				 * goToSloganPage() method here!
				 * 
				 */
				goToProfilePage(url);
				return true; //this should be true to ensure that the page doesn't also open in the parent activity.
			}
			else {
				return false;
			}
		}
		
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			webView1.loadUrl("file:///android_asset/connectionerror.html");
		}
	}
}