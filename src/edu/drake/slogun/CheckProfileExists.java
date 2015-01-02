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
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/* This is an intermediate activity for both the sign-in and sign-up process. */

public class CheckProfileExists extends Activity {

	WebView webView1;
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

		webView1 = (WebView) findViewById(R.id.webviewCheckProfile);

		WebSettings webSettings = webView1.getSettings();
		webSettings.setJavaScriptEnabled(true);

		webView1.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		webView1.setWebViewClient(new MyWebViewClient());
		webView1.loadUrl("http://slogunapp.appspot.com/app/checkProfile");

		Log.d("logout test", "onCreated()ed CheckProfileExists");
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
					userChecked = true;
					if (the_page_check_text.contains("True")) {
						userHasProfile = true;
						goToHome();
					}
					else {
						userHasProfile = false;
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
			if (temp.contains("app/checkProfile")) { 

				webView1.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

				if (userHasProfile) {
					goToHome();
					return true;
				}
				else {
					goToCreateProfile();
					return true;
				}
			}
			else if (temp.contains("google")) { 

				//do stuff

				return true; //open the URL in the web browser.
			}
			else {
				return false;
			}
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			/* Injects JavaScript into the page which just finished loading. */
			webView1.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
		}
	}
}