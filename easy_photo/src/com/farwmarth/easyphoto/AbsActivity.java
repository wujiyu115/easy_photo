package com.farwmarth.easyphoto;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.farwmarth.easyphoto.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public abstract class AbsActivity extends FragmentActivity {
	protected ImageLoader loader;
	protected DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnLoading(R.drawable.pic_loading).cacheInMemory(true)
				.cacheOnDisc(true).build();
	}


}
