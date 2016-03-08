package com.farwmarth.easyphoto.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.farwmarth.easyphoto.R;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomeFragment extends Fragment {

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		findView(rootView);
		return rootView;
	}

	private void findView(View rootView) {
		// action_show_menu
		ImageView action_show_menu = (ImageView) rootView
				.findViewById(R.id.action_show_menu);
		action_show_menu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (getActivity() instanceof SlidingFragmentActivity)
					((SlidingFragmentActivity) getActivity()).toggle();
			}

		});
	}
}
