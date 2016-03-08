package com.farwmarth.easyphoto.adapter;

import java.util.ArrayList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.farwmarth.easyphoto.MainActivity;
import com.farwmarth.easyphoto.ShowMediaActivity;
import com.farwmarth.easyphoto.util.CommonDefine;
import com.farwmarth.easyphoto.util.MediaTypeUtils;
import com.farwmarth.easyphoto.util.MediaUtils;
import com.farwmarth.easyphoto.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AlbumGridViewAdapter extends BaseAdapter implements
		OnClickListener {

	private MainActivity mContext;
	private ArrayList<String> mediasList;
	private ImageLoader loader;
	private DisplayImageOptions options;
	private String action_type; // which action , manager or cache
	private String status_type; // which status, check or edit
	private String select_type; // which apps

	public AlbumGridViewAdapter(MainActivity mainActivity, ArrayList<String> mediasList,
			ImageLoader loader, DisplayImageOptions options,
			String select_type, String action_type, String status_type) {

		this.mContext = mainActivity;
		this.mediasList = mediasList;
		this.loader = loader;
		this.options = options;
		this.select_type = select_type;
		this.action_type = action_type;
		this.status_type = status_type;
	}

	public void setSelect_type(String select_type) {
		this.select_type = select_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	public void setStatus_type(String status_type) {
		this.status_type = status_type;
	}

	public void setMediasList(ArrayList<String> mediasList) {
		this.mediasList = mediasList;
	}

	@Override
	public int getCount() {
		return mediasList.size();
	}

	@Override
	public Object getItem(int position) {
		return mediasList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		public ImageView imageView;
		public CheckBox checkBox;
		public ImageView type_img;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.select_imageview, parent, false);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.image_view);

			viewHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.select_pic_check);
			viewHolder.type_img = (ImageView) convertView
					.findViewById(R.id.type_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String media_path;
		if (mediasList != null && mediasList.size() > position)
			media_path = mediasList.get(position);
		else
			media_path = "camera_default";
		if (media_path.contains("default")) {
			viewHolder.imageView.setImageResource(R.drawable.pic_loading);
		} else {
			// video
			if (MediaTypeUtils.isVideoFileType(media_path)) {

				MediaUtils.setVideoThumbnail(loader, options,
						viewHolder.imageView, media_path, this.select_type,this.mContext);
				viewHolder.type_img.setVisibility(View.VISIBLE);
			} else {
				loader.displayImage("file://" + media_path,
						viewHolder.imageView, options);
				viewHolder.type_img.setVisibility(View.GONE);
			}
		}
		viewHolder.imageView.setTag(position);
		viewHolder.checkBox.setTag(position);
		viewHolder.checkBox.setChecked(false);
		if (this.status_type == CommonDefine.Edit_Status) {
			viewHolder.checkBox.setVisibility(View.VISIBLE);
		} else {
			viewHolder.checkBox.setVisibility(View.GONE);
		}
		convertView.setOnClickListener(this);
		viewHolder.checkBox.setOnClickListener(this);
		return convertView;
	}

	@Override
	public void onClick(View view) {
		if (view instanceof RelativeLayout) {
			int position = (Integer) ((ImageView) view
					.findViewById(R.id.image_view)).getTag();
			if (this.status_type == CommonDefine.Edit_Status) {
				CheckBox toggleButton = (CheckBox) view
						.findViewById(R.id.select_pic_check);
				addAnimation(toggleButton);
				toggleButton.setChecked(!toggleButton.isChecked());
				if (mediasList != null && mOnItemClickListener != null
						&& position < mediasList.size()) {
					mOnItemClickListener.onItemClick(toggleButton, position,
							mediasList.get(position), toggleButton.isChecked());
				}
			} else {
				Intent intent = new Intent(mContext, ShowMediaActivity.class);
				intent.putExtra("path", mediasList.get(position));
				mContext.startActivity(intent);
			}
		} else if (view instanceof CheckBox) {
			CheckBox toggleButton = (CheckBox) view
					.findViewById(R.id.select_pic_check);
			addAnimation(toggleButton);
			int position = (Integer) toggleButton.getTag();
			if (mediasList != null && mOnItemClickListener != null
					&& position < mediasList.size()) {
				mOnItemClickListener.onItemClick(toggleButton, position,
						mediasList.get(position), toggleButton.isChecked());
			}
		}
//		System.out.println(view);
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(CheckBox toggleButton, int position,
				String path, boolean isChecked);
	}

	/**
	 * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
	 * 
	 * @param view
	 */
	private void addAnimation(View view) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
				1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}
}
