package com.novelot.picfly;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

/**
 * 中介者
 * 
 * @author V
 * 
 */
public class Mediator {

	private FragmentManager mFragmentManager;
	private NavigationDrawerFragment mNavigationFragment;
	private ArrayList<Fragment> mFragments;
	private Toolbar mToolbar;
	private DrawerLayout mDrawerLayout;
	private MainActivity activity;
	private int mFragmentIndex;
	private static final String[] TITLES = { "相册", "活动", "发现" };

	public Mediator(MainActivity activity, FragmentManager fragmentManager,
			NavigationDrawerFragment navigationDrawerFragment,
			ArrayList<Fragment> fragments, Toolbar mToolbar,
			DrawerLayout mDrawerLayout) {
		this.activity = activity;
		this.mFragmentManager = fragmentManager;
		this.mNavigationFragment = navigationDrawerFragment;
		this.mFragments = fragments;
		this.mToolbar = mToolbar;
		this.mDrawerLayout = mDrawerLayout;
	}

	public void navigSelectedItem(int index) {
		mFragmentIndex = index;
		FragmentManager fragmentManager = mFragmentManager;
		if (mFragments != null && mFragments.size() > index) {
			Fragment fragment = mFragments.get(index);
			if (fragment instanceof AlbumFragment) {
				((AlbumFragment) fragment).setToolbar(mToolbar);
			}
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();
		}

		if (mToolbar != null) {
			mToolbar.setTitle(TITLES[index]);
		}
		if (mDrawerLayout != null)
			mDrawerLayout.closeDrawer(activity
					.findViewById(R.id.navigation_drawer));
	}

	/**
	 * 获取当前是哪个fragment
	 * 
	 * @return
	 */
	public int getFragmentIndex() {
		return mFragmentIndex;
	}
}
