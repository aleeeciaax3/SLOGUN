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
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class EditProfile extends Activity {

	WebView webView1;
	SwipeRefreshLayout swipeView1;
	
	private ValueCallback<Uri> mUploadMessage; 
	 private final static int FILECHOOSER_RESULTCODE=1;  


	@Override  
	protected void onActivityResult(int requestCode, int resultCode,  
			Intent intent) {  
		if(requestCode==FILECHOOSER_RESULTCODE)  
		{  
			if (null == mUploadMessage) return;  
			Uri result = intent == null || resultCode != RESULT_OK ? null  
					: intent.getData();  
			mUploadMessage.onReceiveValue(result);  
			mUploadMessage = null;  
		}
	} 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DADADA")));

		String address = "http://slogunapp.appspot.com/app/my-profile/edit";

		webView1 = (WebView) findViewById(R.id.webviewEditProfile);
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
				if (webView1.getUrl().equals("file:///android_asset/connectionerror.html")) {
					webView1.loadUrl("http://slogunapp.appspot.com/app/my-profile/slogarama");
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
		
		//This ought to enable file uploads for everyone but Android 4.4 users...  Borrowed from here: http://stackoverflow.com/questions/5907369/file-upload-in-webview
		webView1.setWebChromeClient(new WebChromeClient()  {  
			// For Android 3.0+
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {  

				mUploadMessage = uploadMsg;  
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
				i.addCategory(Intent.CATEGORY_OPENABLE);  
				i.setType("image/*");  
				EditProfile.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);  

			}

			// For Android 3.0+
			public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				EditProfile.this.startActivityForResult(
						Intent.createChooser(i, "File Browser"),
						FILECHOOSER_RESULTCODE);
			}

			//For Android 4.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
				mUploadMessage = uploadMsg;  
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
				i.addCategory(Intent.CATEGORY_OPENABLE);  
				i.setType("image/*");  
				EditProfile.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), EditProfile.FILECHOOSER_RESULTCODE );

			}

		}); 
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
		getMenuInflater().inflate(R.menu.edit_profile, menu);
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

	public void goToSloganPage(String sloganURL){
		Intent intent = new Intent(this, Slogan.class);
		intent.putExtra("url", sloganURL);
		startActivity(intent);
	}
	public void goToMyProfilePage() {
		Intent intent = new Intent(this, MyProfile.class);
		intent.putExtra("url", "http://slogunapp.appspot.com/app/my-profile");
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
			else if (!temp.contains("edit")) {
				goToMyProfilePage();
				return true;
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
