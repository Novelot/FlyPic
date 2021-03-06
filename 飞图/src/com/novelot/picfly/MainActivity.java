package com.novelot.picfly;

import java.util.ArrayList;

import android.service.media.MediaBrowserService;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "MainActivity";
	private Mediator mMediator;
	private DrawerLayout mDrawerLayout;
	private Toolbar mToolbar;
	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		Log.v(TAG, "setContentView");
		setContentView(R.layout.activity_main);
		Log.v(TAG, "init med");

		initToolbar();
		/* 设置中介者 */
		initMediator();
	}

	@Override
	protected void onDestroy() {
		Log.v(TAG, "onDestroy");
		super.onDestroy();
		mMediator = null;

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences sp = getSharedPreferences("flypic", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("fragment_index", mMediator.getFragmentIndex());
		editor.commit();
		editor = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sp = getSharedPreferences("flypic", MODE_PRIVATE);
		int fragmentIndex = sp.getInt("fragment_index", 0);
		mMediator.navigSelectedItem(fragmentIndex);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
	//
	// @Override
	// public boolean onMenuItemClick(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.action_settings:
	// Toast.makeText(getApplicationContext(), "Menu",
	// Toast.LENGTH_SHORT).show();
	// break;
	// }
	// return true;
	// }
	// });
	// return super.onOptionsItemSelected(item);
	// }

	/**
	 * 初始化中介者
	 */
	private void initMediator() {
		NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new AlbumFragment());
		fragments.add(new ActivityFragment());
		fragments.add(new FindFragment());
		mMediator = new Mediator(this, getSupportFragmentManager(),
				navigationDrawerFragment, fragments, mToolbar, mDrawerLayout);
	}

	/**
	 * 获取中介者
	 * 
	 * @return
	 */
	public Mediator getMediator() {
		return mMediator;
	}

	private void initToolbar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle(getTitle());// 标题的文字需在setSupportActionBar之前，不然会无效
		// toolbar.setSubtitle("副标题");
		setSupportActionBar(mToolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				mToolbar, R.string.drawer_open, R.string.drawer_close);
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		switch (level) {
		case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
			break;
		}
	}
}
