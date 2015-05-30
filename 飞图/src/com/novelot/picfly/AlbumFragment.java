package com.novelot.picfly;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * 相册Fragment
 * 
 * @author V
 * 
 */
public class AlbumFragment extends Fragment /*
											 * implements
											 * LoaderManager.LoaderCallbacks
											 */{

	private Cursor mCursor;
	private AlbumCursorAdapter mAdapter;
	private GridView gridView;
	private Toolbar mToolbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCursor = getActivity().getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);

		mAdapter = new AlbumCursorAdapter(getActivity(), mCursor);

		// getLoaderManager().initLoader(0, null, this);
		this.setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		gridView = (GridView) inflater.inflate(R.layout.fragment_grid, null);
		if (mAdapter != null) {
			gridView.setAdapter(mAdapter);
		}
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor c = (Cursor) parent.getItemAtPosition(position);
			}
		});
		if (Build.VERSION.SDK_INT >= 11) {
			gridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			gridView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				@Override
				public void onDestroyActionMode(ActionMode mode) {
					// gridView.unselectedChoiceStates();
				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					if (mToolbar != null)
						menu = mToolbar.getMenu();
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.menu_choice_items, menu);
					return true;
				}

				@Override
				public boolean onActionItemClicked(ActionMode mode,
						MenuItem item) {
					int id = item.getItemId();
					if (id == R.id.menu_delete) {
						// gridView.dism
						return true;
					}
					return false;
				}

				@Override
				public void onItemCheckedStateChanged(ActionMode mode,
						int position, long id, boolean checked) {
					if (mToolbar != null)
						mToolbar.setTitle("Selected ("
								+ gridView.getCheckedItemCount() + ")");
				}
			});
		}
		return gridView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mToolbar != null)
			mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch (item.getItemId()) {
					case R.id.action_settings:
						Toast.makeText(getActivity(), "Menu",
								Toast.LENGTH_SHORT).show();
						break;
					}
					return true;
				}
			});
		return super.onOptionsItemSelected(item);
	}

	public void setToolbar(Toolbar toolbar) {
		mToolbar = toolbar;
	}
}
