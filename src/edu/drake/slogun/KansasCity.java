package edu.drake.slogun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class KansasCity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kansas_city);
		setTitle("");
		getActionBar().setIcon(R.drawable.slogun);
		final String[] pages = {
			      "#Kansas City",
			      "Home",
			      "All",
			      "#Des Moines",
			      "#Iowa City",
			      "#Milwaukee",
			      "#Chicago",
			      "#Cedar Rapids",
			      "#Omaha",
			      "#Midwest",
			      "#Current Events"
			  };
		final Spinner sp = (Spinner)findViewById(R.id.spinner);
		final ArrayAdapter<String> ar = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,pages);
		ar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(ar);
		
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
                int position, long id) {

				String s=((TextView)view).getText().toString();
				if(s.equals("Home"))
					startActivity(new Intent(view.getContext(),Home.class));
				if(s.equals("All"))
					startActivity(new Intent(view.getContext(),All.class));
				if(s.equals("#Des Moines"))
					startActivity(new Intent(view.getContext(),DesMoines.class));
				if(s.equals("#Iowa City"))
					startActivity(new Intent(view.getContext(),IowaCity.class));
//				if(s.equals("#Kansas City"))
//					startActivity(new Intent(view.getContext(),KansasCity.class));
				if(s.equals("#Milwaukee"))
					startActivity(new Intent(view.getContext(),Milwaukee.class));
				if(s.equals("#Chicago"))
					startActivity(new Intent(view.getContext(),Chicago.class));
				if(s.equals("#Cedar Rapids"))
					startActivity(new Intent(view.getContext(),CedarRapids.class));
				if(s.equals("#Omaha"))
					startActivity(new Intent(view.getContext(),Omaha.class));
				if(s.equals("#Midwest"))
					startActivity(new Intent(view.getContext(),Midwest.class));
				if(s.equals("#Current Events"))
					startActivity(new Intent(view.getContext(),CurrentEvents.class));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kansas_city, menu);
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
			Intent intent = new Intent(this, Home.class);
			startActivity(intent);
		}
		if(id == R.id.action_profile) {
			Intent intent = new Intent(this, MyProfile.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}