package com.novelot.picfly;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 发现Fragment
 * 
 * @author V
 * 
 */
public class FindFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		TextView tv = new TextView(getActivity());
		tv.setText(getString(R.string.title_section3));
		return tv;
	}
}
