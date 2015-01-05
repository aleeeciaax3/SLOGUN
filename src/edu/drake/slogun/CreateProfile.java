package edu.drake.slogun;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/* This is an intermediate activity for both the sign-in and sign-up process. */

public class CreateProfile extends Activity {
	
	WebView webView1;
	SwipeRefreshLayout swipeView1;
	String check_text = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_profile);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DADADA")));
	    
	    webView1 = (WebView) findViewById(R.id.webviewCreateProfile);
	    
	    WebSettings webSettings = webView1.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webView1.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
	    webView1.setWebViewClient(new MyWebViewClient());
	    webView1.loadUrl("http://slogunapp.appspot.com/app/create-profile");
	    
	    swipeView1 = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout1);	 
		swipeView1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeView1.setRefreshing(true);
				webView1.reload();
				if (webView1.getUrl().equals("file:///android_asset/connectionerror.html")) {
					webView1.loadUrl("http://slogunapp.appspot.com/app/create-profile");
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
		getMenuInflater().inflate(R.menu.create_profile, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == R.id.action_log_out) {
			webView1 = (WebView) findViewById(R.id.webviewCreateProfile);
			webView1.loadUrl("http://slogunapp.appspot.com/_ah/logout?continue=https://www.google.com/accounts/Logout%3Fcontinue%3Dhttps://appengine.google.com/_ah/logout%253Fcontinue%253Dhttp://slogunapp.appspot.com/app/listing/new%26service%3Dah");
			webView1.setWebViewClient(new MyWebViewClient());
		}
		return super.onOptionsItemSelected(item);
	}
	
	/* An instance of this class will be registered as a JavaScript interface */
	class MyJavaScriptInterface
	{
	    @android.webkit.JavascriptInterface
	    public void processHTML(String html)
	    {
	        //This method pulls the HTML from the webpage so that we can get the actual slogna's text.
	    	//Shoutout to SO: http://stackoverflow.com/questions/5264162/how-to-retrieve-html-content-from-webview-as-a-string
	    	String[] htmlLines = html.split(">");
	    	for (int i = 0; i < htmlLines.length; i++) {
	    		if (htmlLines[i].contains("listing-page")) { 
	    			//We made it to the homepage!
	    			String the_check_text = htmlLines[i];
	    			goToHome();
	    		}
	    		else if (htmlLines[i].contains("_ah/logout")) {
	    			//The user chose to logout from the overflow menu.
	    			//Switch the shared pref value.
	    			SharedPreferences settings = getSharedPreferences("PrefsFile", 0);
	    			SharedPreferences.Editor editor = settings.edit();
	    			editor.putBoolean("hasSignedIn", false);
	    			editor.commit();
	    			
	    			finishLogout();
	    		}
	    	}
	    }
	}
	
	public void goToHome() {
		Intent intent = new Intent(this, All.class);
	    startActivity(intent);
	}
	public void finishLogout() {
		Intent intent = new Intent(this, StartActivity.class);
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
			if (temp.contains("app/listing")) { 
				goToHome();
				return true; //this prevents the url from being opened in this webview.
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
	        /* This call injects JavaScript into the page which just finished loading so we can parse the page's HTML. */
	        webView1.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
	    }
	}
}