package edu.drake.slogun;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Add extends Activity {
	private EditText edittext;
	private TextView mTextView;
	String m;
	final ArrayList <Integer> selectedItems=new ArrayList<Integer>();
	
	String sloganString;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
		edittext = (EditText) findViewById(R.id.editText);// get edittext component
		mTextView = (TextView) findViewById(R.id.textview1);
		addKeyListener();
		edittext.addTextChangedListener(mTextEditorWatcher);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			@Override

			public void onClick(View v) {
				{if(edittext.getText().length()>0){
					m=edittext.getText().toString();
				}
				}
				displayAlert();
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
			intent.putExtra("url", "http://slogunapp.appspot.com/my-profile");
			startActivity(intent);
		}
		if(id == R.id.action_log_out) {

		}
		return super.onOptionsItemSelected(item);
	}

	public void addKeyListener() {

		// add a keylistener to keep track user input

		edittext.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// if keydown and "enter" is pressed
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {

					// display a floating message
					Toast.makeText(Add.this,
							edittext.getText(), Toast.LENGTH_LONG).show();
					return true;

				} else if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_9)) {

					// display a floating message
					Toast.makeText(Add.this,
							"Number 9 is pressed!", Toast.LENGTH_LONG).show();
					return true;
				}

				return false;
			}
		});
	}

	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//This sets a textview to the current length
			mTextView.setText(String.valueOf(s.length())+"/65");

		}

		public void afterTextChanged(Editable s) {
		}
	};

	public  void displayAlert()
	{
		final CharSequence[] items = {"#Des Moines", "#Iowa City", "#Kansas City","#Milwaukee", "#Chicago",
				"#Cedar Rapids","#Ames","#Omaha","#Midwest","#Politics/Current Event", "#Miscellaneous"};
		final boolean[] states = {false, false, false,false, false,false, false, false, false, false, false};
		// arraylist to keep the selected items
		//final ArrayList <Integer> selectedItems=new ArrayList<Integer>();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Check Two");
		builder.setMultiChoiceItems(items, states, new DialogInterface.OnMultiChoiceClickListener(){
			public void onClick(DialogInterface dialogInterface, int item, boolean state) {
				if (state) {
					// If the user checked the item, add it to the selected items
					selectedItems.add(item);
				}
			}
		});


		builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				
				HttpClient httpClient = new DefaultHttpClient();

				//HttpPost postRequest = new HttpPost("http://postcatcher.in/catchers/5488c8bae1691b0200000523");
				HttpPost postRequest = new HttpPost("http://slogunapp.appspot.com/addSlogan");

				try {
					enableStrictMode(); //this is necessary to let us post the request.

					sloganString = m;
					Log.d("the value of m: ", m);
					if (sloganString == null) {
						sloganString = "[No Title]";
					}
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			        nameValuePairs.add(new BasicNameValuePair("slogan_text", sloganString));
			        //nameValuePairs.add(new BasicNameValuePair("unique_id", uniqueID));			        
			        postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			        // Execute HTTP Post Request
			        HttpResponse response = httpClient.execute(postRequest);

				} catch (ClientProtocolException e) {
					// Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}

				GoHome();
			}

		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	public void GoHome() {
		Intent intent = new Intent(this, All.class);
		startActivity(intent);
	}
	
	/* this is a dirty workaround that allows us to make http post requests */
	public void enableStrictMode()
	{
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	}
	
}