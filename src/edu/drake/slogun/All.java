/* SloGUN */

package edu.drake.slogun;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class All extends Activity implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	static WebView webViewNew, webViewTrending, webViewTop;
	static ProgressBar loadingImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all);
		setTitle("");

		//Set up action bar with custom icon and tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DADADA")));

		// This section replaces the text of the actionbar with a spinner.
		Spinner spinnerView = (Spinner) getLayoutInflater().inflate(R.layout.actionbar_spinner_layout, null);  
		actionBar.setCustomView(spinnerView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}		
	}


	@Override
	public void onResume() {
		super.onResume();

		final String[] pages = {
				"#Main",
				"#Des Moines",
				"#Iowa City",
				"#Kansas City",
				"#Minnesota",
				"#Chicago",
				"#Milwaukee",
				"#Cedar Rapids",
				"#Ames",
				"#Omaha",
				"#The Midwest",
				"#Politics/Current Events",
				"#Everything Else"
		};
		//The ability to use a spinner and tabs together comes courtesy of this example: 
		//    http://www.hasnath.net/blog/actionbar-tab-spinnerlist-navigation-at-the-same-time
		final Spinner sp = (Spinner)findViewById(R.id.spinner);
		final ArrayAdapter<String> ar = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,pages);
		ar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(ar);
		//sp.setSelection(0);

		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				if (((TextView)view) != null){
					String s=((TextView)view).getText().toString();
					//				if(s.equals("#Main"))
					//					startActivity(new Intent(view.getContext(),All.class));
					if(s.equals("#Des Moines")){
						startActivity(new Intent(view.getContext(),DesMoines.class));}
					if(s.equals("#Iowa City")){
						startActivity(new Intent(view.getContext(),IowaCity.class));}
					if(s.equals("#Kansas City")){
						startActivity(new Intent(view.getContext(),KansasCity.class));}
					if(s.equals("#Minnesota")){
						startActivity(new Intent(view.getContext(),Minnesota.class));}
					if(s.equals("#Chicago")){
						startActivity(new Intent(view.getContext(),Chicago.class));}
					if(s.equals("#Milwaukee")){
						startActivity(new Intent(view.getContext(),Milwaukee.class));}
					if(s.equals("#Cedar Rapids")){
						startActivity(new Intent(view.getContext(),CedarRapids.class));}
					if(s.equals("#Ames")){
						startActivity(new Intent(view.getContext(),Ames.class));}
					if(s.equals("#Omaha")){
						startActivity(new Intent(view.getContext(),Omaha.class));}
					if(s.equals("#The Midwest")){
						startActivity(new Intent(view.getContext(),Midwest.class));}
					if(s.equals("#Politics/Current Events")){
						startActivity(new Intent(view.getContext(),CurrentEvents.class));}
					if(s.equals("#Everything Else")){
						startActivity(new Intent(view.getContext(),Miscellaneous.class));}
				}
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
		getMenuInflater().inflate(R.menu.all, menu);
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

	public void goToSloganPage(String sloganURL) {
		Intent intent = new Intent(this, Slogan.class);
		intent.putExtra("url", sloganURL);
		startActivity(intent);
	}
	public void goToCommentsPage(String commentsURL) {
		Intent intent = new Intent(this, Comments.class);
		intent.putExtra("url", commentsURL);
		startActivity(intent);
	}
	public void goToProfilePage(String profileURL) {
		Intent intent = new Intent(this, MyProfile.class);
		intent.putExtra("url", profileURL);
		startActivity(intent);
	}


	//These methods are necessary for tabs.
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			if (position == 0) {
				return FragmentNew.newInstance(position + 1);
			}
			else if (position == 1) {
				return FragmentTrending.newInstance(position + 1);
			}
			else {
				return FragmentTop.newInstance(position - 2);
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				String title1 = "NEW";
				return title1;
			case 1:
				String title2 = "TRENDING";
				return title2;
			case 2:
				String title3 = "ALL TIME";
				return title3;
			}
			return null;
		}
	}

	/**
	 * A fragment containing the "new" listing for this page
	 */
	public static class FragmentNew extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		SwipeRefreshLayout swipeView1;
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static FragmentNew newInstance(int sectionNumber) {
			FragmentNew fragment = new FragmentNew();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public FragmentNew() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.new_listing, container,
					false);

			//loadingImage = (ProgressBar) rootView.findViewById(R.id.progressBar);

			webViewNew = (WebView) rootView.findViewById(R.id.webviewNew);
			webViewNew.loadUrl("http://slogunapp.appspot.com/app/listing/new"); //slogunapp.appspot.com/app/listing/new
			webViewNew.setWebViewClient(new MyWebViewClient());

			WebSettings webSettings = webViewNew.getSettings();
			webSettings.setJavaScriptEnabled(true);

			swipeView1 = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout1);	 
			swipeView1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					swipeView1.setRefreshing(true);
					if (webViewNew.getUrl().equals("file:///android_asset/connectionerror.html")) {
						webViewNew.loadUrl("http://slogunapp.appspot.com/app/listing/new");
					}
					else {
						webViewNew.reload();
					}
					( new Handler()).postDelayed(new Runnable() {
						@Override
						public void run() {
							swipeView1.setRefreshing(false);
						}
					}, 3000);
				}
			});

			return rootView;
		}

		/*
		 * This method gives us custom control over what happens with the links we click.
		 * Additionally, implement custom back-button functionality with @override onKeyDown().
		 */
		public class MyWebViewClient extends WebViewClient {	
			/* These should show a progress bar while the webView is loading, but as of yet they cause the webView to never show up */
			/*public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
				Log.d("progressbar", "called onPageStarted()");
                super.onPageStarted(view, url, favicon);
                loadingImage.setVisibility(View.VISIBLE);
            }
			public void onPageFinished(WebView view, String url) {
				Log.d("progressbar", "called onPageFinished()");
                super.onPageFinished(view, url);
                loadingImage.setVisibility(View.GONE);
                webViewNew.setVisibility(View.VISIBLE);
			}*/

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//view.loadUrl(url);
				//This determines whether the user has clicked on either a poem or profile page, 
				//and then sends them to the appropriate activity.
				List<String> temp = Uri.parse(url).getPathSegments();
				if (temp.contains("user")) {
					((All)getActivity()).goToProfilePage(url);
					return true; //this ensures that the link isn't also opened in the parent activity.
				}
				else if (temp.contains("comments")) {
					((All)getActivity()).goToCommentsPage(url);
					return true;
				}
				else if (temp.contains("slogan")) {
					((All)getActivity()).goToSloganPage(url);
					return true; //this should be true to ensure that the page doesn't also open in the parent activity.
				}
				else {
					return false;
				}
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				webViewNew.loadUrl("file:///android_asset/connectionerror.html");
			}
		}
	}

	/**
	 * A fragment containing the "trending" listing for this page
	 */
	public static class FragmentTrending extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		SwipeRefreshLayout swipeView2;
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static FragmentTrending newInstance(int sectionNumber) {
			FragmentTrending fragment = new FragmentTrending();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public FragmentTrending() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.trending_listing, container,
					false);

			webViewTrending = (WebView) rootView.findViewById(R.id.webviewTrending);
			webViewTrending.loadUrl("http://slogunapp.appspot.com/app/listing/trending"); //slogunapp.appspot.com/app/listing/trending
			webViewTrending.setWebViewClient(new MyWebViewClient());

			WebSettings webSettings = webViewTrending.getSettings();
			webSettings.setJavaScriptEnabled(true);

			swipeView2 = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout2);	 
			swipeView2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					swipeView2.setRefreshing(true);
					if (webViewTrending.getUrl().equals("file:///android_asset/connectionerror.html")) {
						webViewTrending.loadUrl("http://slogunapp.appspot.com/app/listing/trending");
					}
					else {
						webViewTrending.reload();
					}
					( new Handler()).postDelayed(new Runnable() {
						@Override
						public void run() {
							swipeView2.setRefreshing(false);
						}
					}, 3000);
				}
			});

			return rootView;
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
				if (temp.contains("user")) {
					((All)getActivity()).goToProfilePage(url);
					return true; //this ensures that the link isn't also opened in the parent activity.
				}
				else if (temp.contains("comments")) {
					((All)getActivity()).goToCommentsPage(url);
					return true;
				}
				else if (temp.contains("slogan")) {
					((All)getActivity()).goToSloganPage(url);
					return true; //this should be true to ensure that the page doesn't also open in the parent activity.
				}
				else {
					return false;
				}
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				webViewTrending.loadUrl("file:///android_asset/connectionerror.html");
			}
		}
	}

	/**
	 * A fragment containing the "top" listing for this page
	 */
	public static class FragmentTop extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		SwipeRefreshLayout swipeView3;
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static FragmentTop newInstance(int sectionNumber) {
			FragmentTop fragment = new FragmentTop();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public FragmentTop() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.top_listing, container,
					false);

			webViewTop = (WebView) rootView.findViewById(R.id.webviewTop);
			webViewTop.loadUrl("http://slogunapp.appspot.com/app/listing/top"); //slogunapp.appspot.com/app/listing/top
			webViewTop.setWebViewClient(new MyWebViewClient());

			WebSettings webSettings = webViewTop.getSettings();
			webSettings.setJavaScriptEnabled(true);

			swipeView3 = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout3);	 
			swipeView3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					swipeView3.setRefreshing(true);
					if (webViewTop.getUrl().equals("file:///android_asset/connectionerror.html")) {
						webViewTop.loadUrl("http://slogunapp.appspot.com/app/listing/top");
					}
					else {
						webViewTop.reload();
					}
					( new Handler()).postDelayed(new Runnable() {
						@Override
						public void run() {
							swipeView3.setRefreshing(false);
						}
					}, 3000);
				}
			});

			return rootView;
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
				if (temp.contains("user")) {
					((All)getActivity()).goToProfilePage(url);
					return true; //this ensures that the link isn't also opened in the parent activity.
				}
				else if (temp.contains("comments")) {
					((All)getActivity()).goToCommentsPage(url);
					return true;
				}
				else if (temp.contains("slogan")) {
					((All)getActivity()).goToSloganPage(url);
					return true; //this should be true to ensure that the page doesn't also open in the parent activity.
				}
				else {
					return false;
				}
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				webViewTop.loadUrl("file:///android_asset/connectionerror.html");
			}
		}
	}

	/*
	 * This method enables back-button functionality for the WebViews.  
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// If it wasn't the Back key or there's no web page history, bubble up to the default
		// system behavior (probably exit the activity)
		return super.onKeyDown(keyCode, event);
	}
}