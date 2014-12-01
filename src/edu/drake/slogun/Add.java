package edu.drake.slogun;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Add extends Activity {
	 private EditText edittext;
	 private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		setTitle("");
		getActionBar().setIcon(R.drawable.slogun);
		edittext = (EditText) findViewById(R.id.editText);// get edittext component
		mTextView = (TextView) findViewById(R.id.textview1);
		addKeyListener();
		edittext.addTextChangedListener(mTextEditorWatcher);
		
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
			startActivity(intent);
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

}
