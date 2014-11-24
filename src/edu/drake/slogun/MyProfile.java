package edu.drake.slogun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MyProfile extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
		setTitle("");
		getActionBar().setIcon(R.drawable.slogun);
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
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
