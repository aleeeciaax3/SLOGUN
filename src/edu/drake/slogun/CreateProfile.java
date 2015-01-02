package edu.drake.slogun;

import java.util.List;

import edu.drake.slogun.CheckProfileExists.MyJavaScriptInterface;
import edu.drake.slogun.CheckProfileExists.MyWebViewClient;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/* This is an intermediate activity for both the sign-in and sign-up process. */

public class CreateProfile extends Activity {
	
	WebView webView1;
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
	    			String the_check_text = htmlLines[i];
	    			goToHome();
	    		}
	    	}
	    }
	}
	
	public void goToHome() {
		Intent intent = new Intent(this, All.class);
	    startActivity(intent);
	}
	
	/*
	 * This class gives us custom control over what happens when the page redirects.
	 * Additionally, implement custom back-button functionality with @override onKeyDown().
	 */
	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			
			Log.d("createProf", "Called shouldOverrideUrlLoading");

			//This determines whether the page has redirected to either the slogan or home listing page
			List<String> temp = Uri.parse(url).getPathSegments();
			if (temp.contains("app/listing")) { 
				Log.d("createProf", "Fired on app/listing");
				goToHome();
				return true; //this prevents the url from being opened in this webview.
			}
			else {
				Log.d("createProf", "Fired on NOT app/listing");
				return false;
			}
		}
		
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			webView1.loadUrl("file:///android_asset/connectionerror.html");
		}
		
		@Override
	    public void onPageFinished(WebView view, String url)
	    {
			Log.d("createProf", "called onPageFinished()");
	        /* This call injects JavaScript into the page which just finished loading. */
	        webView1.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
	    }
	}
}