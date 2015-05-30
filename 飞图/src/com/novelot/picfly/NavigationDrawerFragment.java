package com.novelot.picfly;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

	// /**
	// * Remember the position of the selected item.
	// */
	// private static final String STATE_SELECTED_POSITION =
	// "selected_navigation_drawer_position";
	//
	// /**
	// * Per the design guidelines, you should show the drawer on launch until
	// the
	// * user manually expands it. This shared preference tracks this.
	// */
	// private static final String PREF_USER_LEARNED_DRAWER =
	// "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */

	private static final String TAG = "NavigationDrawerFragment";

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	// private ActionBarDrawerToggle mDrawerToggle;

	// private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	// private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 0;
	// private boolean mFromSavedInstanceState;
	// private boolean mUserLearnedDrawer;

	private Mediator mMediator;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		// SharedPreferences sp = PreferenceManager
		// .getDefaultSharedPreferences(getActivity());
		// mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
		//
		// if (savedInstanceState != null) {
		// mCurrentSelectedPosition = savedInstanceState
		// .getInt(STATE_SELECTED_POSITION);
		// mFromSavedInstanceState = true;
		// }
		//
		// // Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);

	}

	private void selectItem(int position) {
		if (mMediator != null)
			mMediator.navigSelectedItem(position);
		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.v(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
		try {
			mMediator = ((MainActivity) getActivity()).getMediator();
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView");
		mDrawerListView = (ListView) inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);
		mDrawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						selectItem(position);
					}
				});
		mDrawerListView.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.nav_list_item, R.id.text1, new String[] {
						getString(R.string.title_section1),
						getString(R.string.title_section2),
						getString(R.string.title_section3), }));
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		return mDrawerListView;
	}

	@Override
	public void onAttach(Activity activity) {
		Log.v(TAG, "onAttach");
		super.onAttach(activity);
		// try {
		// mMediator = ((MainActivity) activity).getMediator();
		// } catch (ClassCastException e) {
		// throw new ClassCastException(
		// "Activity must implement NavigationDrawerCallbacks.");
		// }
	}

	@Override
	public void onDetach() {
		Log.v(TAG, "onDetach");
		super.onDetach();
		mMediator = null;
	}

	// @Override
	// public void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	// }
	//
	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// super.onConfigurationChanged(newConfig);
	// // Forward the new configuration the drawer toggle component.
	// mDrawerToggle.onConfigurationChanged(newConfig);
	// }

	// @Override
	// public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// // If the drawer is open, show the global app actions in the action bar.
	// // See also
	// // showGlobalContextActionBar, which controls the top-left area of the
	// // action bar.
	// if (mDrawerLayout != null && isDrawerOpen()) {
	// inflater.inflate(R.menu.global, menu);
	// showGlobalContextActionBar();
	// }
	// super.onCreateOptionsMenu(menu, inflater);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// if (mDrawerToggle.onOptionsItemSelected(item)) {
	// return true;
	// }
	//
	// if (item.getItemId() == R.id.action_example) {
	// Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT)
	// .show();
	// return true;
	// }
	//
	// return super.onOptionsItemSelected(item);
	// }

}
