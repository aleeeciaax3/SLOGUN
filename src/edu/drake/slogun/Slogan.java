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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import edu.drake.slogun.Chicago.FragmentNew.MyWebViewClient;

public class Slogan extends Activity {
	
	WebView webView1;
	SwipeRefreshLayout swipeView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slogan);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
	    
	    String address = getIntent().getExtras().getString("url");
	    
	    webView1 = (WebView) findViewById(R.id.webviewSlogan);
	    webView1.loadUrl(address); //slogunapp.appspot.com/listing/new
	    webView1.setWebViewClient(new MyWebViewClient());

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
			intent.putExtra("url", "http://slogunapp.appspot.com/my-profile");
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
	public void goToCommentsPage(String commentsURL) {
		Intent intent = new Intent(this, Comments.class);
		intent.putExtra("url", commentsURL);
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
			if (temp.contains("user")) {
				/* 
				 * goToProfilePage() method here!
				 * 
				 */
				goToProfilePage(url);
				return true; //this ensures that the link isn't also opened in the parent activity.
			}
			else if (temp.contains("comments")) {
				/*
				 * goToCommentsPage() method here!
				 * 
				 */
				goToCommentsPage(url);
				return true;
			}
			else {
				return false;
			}
		}
	}
}