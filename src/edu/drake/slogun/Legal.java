package edu.drake.slogun;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Legal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_legal);
		setTitle("");
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.slogun);
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.legal, menu);
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
}
