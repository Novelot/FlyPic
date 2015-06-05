package com.novelot.picfly;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.novelot.base.FileUtils;

/**
 * 活动Fragment
 * 
 * @author V
 * 
 */
public class ActivityFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Button tv = new Button(getActivity());
		tv.setText(getString(R.string.title_section2));
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FileUtils.saveToDisk();
			}
		});
		return tv;
	}
}
