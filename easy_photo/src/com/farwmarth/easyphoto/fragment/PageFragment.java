package com.farwmarth.easyphoto.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farwmarth.easyphoto.MainActivity;
import com.farwmarth.easyphoto.R;
import com.farwmarth.easyphoto.adapter.AlbumGridViewAdapter;
import com.farwmarth.easyphoto.util.CommonDefine;
import com.farwmarth.easyphoto.util.FileUtil;
import com.farwmarth.easyphoto.util.MediaUtils;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

@SuppressLint("ValidFragment")
public class PageFragment extends AbsFragment {

	private GridView gridView;

	private AlbumGridViewAdapter gridImageAdapter;
	private TextView save_button;
	private TextView del_button;
	private TextView action_manager;
	private TextView action_cache;
	private RelativeLayout bottom_layout;
	private ImageView action_show_menu;
	private TextView preView = null;

	private String select_type; // which apps
	private String action_type; // which action , manager or cache
	private String status_type; // which status, check or edit
	private int app_position; // click apps position
	private String[] mNavMenuTitles;
	private ArrayList<String> mediaLists = new ArrayList<String>();
	private ArrayList<String> selectedDataList = new ArrayList<String>();

	public PageFragment() {

		super();
	}

	public PageFragment(String type, int position) {

		this.select_type = type;
		if (this.select_type == CommonDefine.momo_type) {
			this.action_type = CommonDefine.Cache_Action;
		} else {
			this.action_type = CommonDefine.Manager_Action;
		}
		this.status_type = CommonDefine.Check_Status;
		this.app_position = position;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mNavMenuTitles = getResources()
				.getStringArray(R.array.nav_drawer_items);
		View rootView = inflater.inflate(R.layout.activity_album, container,
				false);
		init_view(getActivity().getIntent(), rootView);
		return rootView;
	}

	private void init_view(Intent intent, View rootView) {
		resetData();
		findView(rootView);
	}

	private void resetData() {
		this.mediaLists = MediaUtils.listMediaData(this.select_type,
				this.action_type);
		if (this.gridImageAdapter != null)
			gridImageAdapter.setMediasList(this.mediaLists);
	}

	private void doAction(TextView view) {
		if (preView == view) {
			changeStatus_type(view, this.status_type);
		} else {
			changeAction_type(view);
		}
		preView = view;
	}

	private void changeAction_type(TextView view) {
		if (view == action_manager) {
			setAction_type(CommonDefine.Manager_Action);
		} else {
			setAction_type(CommonDefine.Cache_Action);
		}
		action_manager.setText(getString(R.string.action_manager));
		action_cache.setText(getString(R.string.action_cache));
		setStatus_type(CommonDefine.Check_Status);
	}

	private void changeStatus_type(TextView view, String status_type) {
		if (status_type == CommonDefine.Edit_Status) {
			setStatus_type(CommonDefine.Check_Status);
			if (view == action_manager)
				view.setText(getString(R.string.action_manager));
			else
				view.setText(getString(R.string.action_cache));
		} else {
			setStatus_type(CommonDefine.Edit_Status);
			view.setText(getString(R.string.action_edit));
		}
	}

	private void findView(View rootView) {
		// action_show_menu
		action_show_menu = (ImageView) rootView
				.findViewById(R.id.action_show_menu);
		// manager
		action_manager = (TextView) rootView.findViewById(R.id.action_manager);
		// cache
		action_cache = (TextView) rootView.findViewById(R.id.action_cache);
		// select item desc
		TextView select_item_txt = (TextView) rootView
				.findViewById(R.id.imageLocation);
		select_item_txt.setText(this.mNavMenuTitles[this.app_position]);
		// bottom layout
		bottom_layout = (RelativeLayout) rootView
				.findViewById(R.id.bottom_layout);
		save_button = (TextView) rootView.findViewById(R.id.save_button);
		del_button = (TextView) rootView.findViewById(R.id.del_button);
		gridView = (GridView) rootView.findViewById(R.id.myGrid);
		gridImageAdapter = new AlbumGridViewAdapter(
				(MainActivity) getActivity(), mediaLists, loader, options,
				this.select_type, this.action_type, this.status_type);
		gridView.setAdapter(gridImageAdapter);

		if (this.select_type != CommonDefine.momo_type) {
			action_cache.setVisibility(View.GONE);
			save_button.setVisibility(View.GONE);
			preView = action_manager;
		} else {
			preView = action_cache;
		}
		if (this.status_type != CommonDefine.Edit_Status) {
			bottom_layout.setVisibility(View.GONE);
		} else {
			bottom_layout.setVisibility(View.VISIBLE);
		}
		action_show_menu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (getActivity() instanceof SlidingFragmentActivity)
					((SlidingFragmentActivity) getActivity()).toggle();
			}

		});
		action_manager.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doAction(action_manager);
			}

		});
		action_cache.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doAction(action_cache);
			}
		});
		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
					public void onItemClick(final CheckBox toggleButton,
							int position, final String path, boolean isChecked) {
						if (isChecked) {
							selectedDataList.add(path);
						} else {
							removeOneData(selectedDataList, path);
						}
						change_select_size_desc();
					}
				});

		save_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (selectedDataList != null && selectedDataList.size() > 0) {
					int offset = 0;
					if (select_type == CommonDefine.momo_type)
						offset = 1;
					FileUtil.copyFiles(selectedDataList,
							CommonDefine.typeToDirc(select_type), offset);
					resetData();
					if (gridImageAdapter != null)
						gridImageAdapter.notifyDataSetChanged();
					selectedDataList.clear();
					change_select_size_desc();
					Toast.makeText(getActivity(), R.string.cache_success,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		del_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (selectedDataList != null && selectedDataList.size() > 0) {
					for (String path : selectedDataList) {
						FileUtil.deleteFileIfExists(path);
					}
					resetData();
					if (gridImageAdapter != null)
						gridImageAdapter.notifyDataSetChanged();
					selectedDataList.clear();
					change_select_size_desc();
					Toast.makeText(getActivity(), R.string.del_success,
							Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	private void change_select_size_desc() {
		save_button.setText(String.format(
				getResources().getString(R.string.action_save_item),
				selectedDataList.size()));
		del_button.setText(String.format(
				getResources().getString(R.string.action_del_item),
				selectedDataList.size()));
	}

	private void removeOneData(ArrayList<String> arrayList, String s) {
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).equals(s)) {
				arrayList.remove(i);
				return;
			}
		}
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
		resetData();
		if (gridImageAdapter != null) {
			gridImageAdapter.setAction_type(this.action_type);
			gridImageAdapter.notifyDataSetChanged();
		}
	}

	public void setStatus_type(String status_type) {
		// System.out.println("改变状态:" + status_type);
		this.status_type = status_type;
		if (gridImageAdapter != null) {
			gridImageAdapter.setStatus_type(this.status_type);
			gridImageAdapter.notifyDataSetChanged();
		}
		if (this.status_type == CommonDefine.Edit_Status)
			bottom_layout.setVisibility(View.VISIBLE);
		else
			bottom_layout.setVisibility(View.GONE);
	}

	public void setSelect_type(String select_type) {
		this.select_type = select_type;
		if (gridImageAdapter != null)
			gridImageAdapter.setSelect_type(this.select_type);
	}

}
