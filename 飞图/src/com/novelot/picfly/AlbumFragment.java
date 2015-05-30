package com.novelot.picfly;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ListView;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCursor = getActivity().getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);

		mAdapter = new AlbumCursorAdapter(getActivity(), mCursor);

		// getLoaderManager().initLoader(0, null, this);
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
					mode.setTitle("Selected (" + gridView.getCheckedItemCount()
							+ ")");
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
}
