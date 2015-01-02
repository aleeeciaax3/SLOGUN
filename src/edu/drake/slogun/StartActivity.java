package edu.drake.slogun;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getSharedPreferences("PrefsFile", 0);
	    boolean hasSignedIn = settings.getBoolean("hasSignedIn", false);
	    if (!hasSignedIn) {
	    	setContentView(R.layout.activity_start_screen_gmail);
	    }
	    else {
	    	setContentView(R.layout.activity_start_screen_continue);
	    }
	    Log.d("logout test", "onCreate()ed StartActivity");
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DADADA")));

		
	}
	public void continueSignin(View view) {
		Log.d("logout test", "called continueSignin() from startActivity");
		Intent intent = new Intent(this, CheckProfileExists.class);
		startActivity(intent);
	}
}