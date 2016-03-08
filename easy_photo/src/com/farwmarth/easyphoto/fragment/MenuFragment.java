package com.farwmarth.easyphoto.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.farwmarth.easyphoto.R;
import com.farwmarth.easyphoto.adapter.NavDrawerListAdapter;
import com.farwmarth.easyphoto.entity.NavDrawerItem;

public class MenuFragment extends Fragment implements OnItemClickListener {

	private ListView mDrawerList;
	private String[] mNavMenuTitles;
	private TypedArray mNavMenuIconsTypeArray;
	private ArrayList<NavDrawerItem> mNavDrawerItems;
	private NavDrawerListAdapter mAdapter;
	private SLMenuListOnItemClickListener mCallback;
	private int selected = -1;

	@Override
	public void onAttach(Activity activity) {
		try {
			mCallback = (SLMenuListOnItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnResolveTelsCompletedListener");
		}
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_menu, null);
		findView(rootView);
		return rootView;
	}

	private void findView(View rootView) {

		mDrawerList = (ListView) rootView.findViewById(R.id.left_menu);

		mNavMenuTitles = getResources()
				.getStringArray(R.array.nav_drawer_items);
		mNavMenuIconsTypeArray = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);

		mNavDrawerItems = new ArrayList<NavDrawerItem>();
		for (int i = 0; i < mNavMenuTitles.length; i++) {
			mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[i],
					mNavMenuIconsTypeArray.getResourceId(i, -1)));
		}

		mNavMenuIconsTypeArray.recycle();

		mAdapter = new NavDrawerListAdapter(getActivity(), mNavDrawerItems);
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemClickListener(this);

		if (selected != -1) {
			mDrawerList.setItemChecked(selected, true);
			mDrawerList.setSelection(selected);
		} else {
			mDrawerList.setItemChecked(0, true);
			mDrawerList.setSelection(0);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		if (mCallback != null) {
			mCallback.selectItem(position, mNavMenuTitles[position]);
		}
		selected = position;
	}

	public interface SLMenuListOnItemClickListener {
		public void selectItem(int position, String title);
	}
}
