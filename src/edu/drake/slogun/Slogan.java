package edu.drake.slogun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ShareActionProvider;

public class Slogan extends Activity {

	WebView webView1;
	SwipeRefreshLayout swipeView1;
	String address;
	String the_slogan_text = "";

	private ShareActionProvider mShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slogan);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DADADA")));

		address = getIntent().getExtras().getString("url");

		webView1 = (WebView) findViewById(R.id.webviewSlogan);
		
		final Context myApp = this;

		WebSettings webSettings = webView1.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView1.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		
		webView1.setWebViewClient(new MyWebViewClient());
		webView1.loadUrl(address); //slogunapp.appspot.com/app/slogan/(\d+)

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
	
	/* An instance of this class will be registered as a JavaScript interface */
	class MyJavaScriptInterface
	{
	    @android.webkit.JavascriptInterface
	    public void processHTML(String html)
	    {
	    	Log.d("checkProf", "called processHTML from Slogan");
	    	
	        //This method pulls the HTML from the webpage so that we can get the actual slogna's text.
	    	//Shoutout to SO: http://stackoverflow.com/questions/5264162/how-to-retrieve-html-content-from-webview-as-a-string
	    	String[] htmlLines = html.split(":::::::::"); //split by some known string which isn't likely to be an actual slogan
	    	for (int i = 0; i < htmlLines.length; i++) {
	    		if (htmlLines[i].contains("the-complete-slogan*$&#^@*asdf")) { //pick a string that isn't likely to be an actual slogan
	    			the_slogan_text = htmlLines[i+1];
	    		}
	    	}
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slogan, menu);
		Log.d("share", "test oncreatemenu");
		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);
		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == R.id.add) {
			Log.d("share", "test add");
			Intent intent = new Intent(this, Add.class);
			startActivity(intent);
		}
		if(id == R.id.menu_item_share) {
			//Share the text of this particular slogan with other apps (doesn't work for facebook)
			Intent sharingIntent = new Intent(Intent.ACTION_SEND); 
			sharingIntent.setType("text/plain");
			String shareBody = the_slogan_text;
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SloGUN");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			setShareIntent(sharingIntent);
			startActivity(Intent.createChooser(sharingIntent, "Share via..."));
		}
		if(id == R.id.action_settings) {
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

	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
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

			//This determines whether the user has clicked on either a comments or profile page, 
			//and then sends them to the appropriate activity.
			List<String> temp = Uri.parse(url).getPathSegments();
			if (temp.contains("user")) {
				goToProfilePage(url);
				return true; //this ensures that the link isn't also opened in the parent activity.
			}
			else if (temp.contains("comments")) {
				goToCommentsPage(url);
				return true;
			}
			else {
				return false;
			}
		}
		
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			webView1.loadUrl("file:///android_asset/connectionerror.html");
		}
		
		@Override
	    public void onPageFinished(WebView view, String url)
	    {
	        /* This call injects JavaScript into the page which just finished loading. */
	        webView1.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
	    }
	}
}