package com.farwmarth.easyphoto.fragment;

import com.farwmarth.easyphoto.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class AbsFragment extends Fragment {
	protected ImageLoader loader;
	protected DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnLoading(R.drawable.pic_loading).cacheInMemory(true)
				.cacheOnDisc(true).build();
	}
}
