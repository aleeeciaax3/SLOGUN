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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

/* This is an intermediate activity for both the sign-in and sign-up process. */

public class CheckProfileExists extends Activity {

	WebView webView1;
	SwipeRefreshLayout swipeView1;
	ImageView backGnd;
	String the_page_check_text = "";
	Boolean userHasProfile = false;
	Boolean userChecked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_profile_exists);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DADADA")));
		
		backGnd = (ImageView) findViewById(R.id.checkProfileBackground);

		webView1 = (WebView) findViewById(R.id.webviewCheckProfile);

		WebSettings webSettings = webView1.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webView1.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		webView1.setWebViewClient(new MyWebViewClient());
		webView1.loadUrl("http://slogunapp.appspot.com/app/checkProfile");

		swipeView1 = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout1);	 
		swipeView1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeView1.setRefreshing(true);
				webView1.reload();
				if (webView1.getUrl().equals("file:///android_asset/connectionerror.html")) {
					webView1.loadUrl("http://slogunapp.appspot.com/app/checkProfile");
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

	/* An instance of this class will be registered as a JavaScript interface */
	class MyJavaScriptInterface
	{
		@android.webkit.JavascriptInterface
		public void processHTML(String html)
		{
			Log.d("logout test", "called processHTML on CheckProfileExists");
			//This method pulls the HTML from the webpage so that we can get the actual slogna's text.
			//Shoutout to SO: http://stackoverflow.com/questions/5264162/how-to-retrieve-html-content-from-webview-as-a-string
			String[] htmlLines = html.split(">");
			for (int i = 0; i < htmlLines.length; i++) {
				if (htmlLines[i].contains("userHasProfile:")) { 
					the_page_check_text = htmlLines[i];
					Log.d("checkProf", the_page_check_text);
					userChecked = true;
					if (the_page_check_text.contains("True")) {
						userHasProfile = true;
						Log.d("checkProf", "sent to home() from js interface");
						goToHome();
					}
					else {
						userHasProfile = false;
						Log.d("checkProf", "sent to createProfile() from js interface");
						goToCreateProfile();
					}
				}
			}
		}
	}

	public void goToHome() {
		//Switch the shared pref value.
		SharedPreferences settings = getSharedPreferences("PrefsFile", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("hasSignedIn", true);
		editor.commit();

		Intent intent = new Intent(this, All.class);
		startActivity(intent);
	}
	public void goToCreateProfile() {
		//Switch the shared pref value.
		SharedPreferences settings = getSharedPreferences("PrefsFile", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("hasSignedIn", true);
		editor.commit();

		Intent intent = new Intent(this, CreateProfile.class);
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
			if (temp.contains("checkProfile")) { 

				webView1.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
				return false;
			}
			else if (url.contains("google") && (temp.contains("ServiceLogin") || temp.contains("AccountChooser"))) { 
				//opens the signin page in the webview
				return false; 
			}
			else if (url.contains("_ah/conflogin") || url.contains("CheckCookie") || url.contains("accounts.") || url.contains("appengine.")) {
				//signin action
				return false;
			}
			else {
				Log.d("checkLog", "url: " + url);
				//open the URL in the web browser.
		        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		        startActivity(intent);
		        return true;
			}
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			backGnd.setVisibility(View.GONE);
			/* Injects JavaScript into the page which just finished loading. */
			webView1.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
		}
		
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			webView1.loadUrl("file:///android_asset/connectionerror.html");
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webView1.canGoBack()) {
	    	webView1.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}
}