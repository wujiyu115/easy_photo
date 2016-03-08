package com.farwmarth.easyphoto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.farwmarth.easyphoto.R;
import com.farwmarth.easyphoto.util.MediaTypeUtils;

public class ShowMediaActivity extends AbsActivity {

	private ImageView image_show;
	private VideoView video_show;
	private MediaController mediaco;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_show_media);
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		image_show = (ImageView) findViewById(R.id.image_show);
		video_show = (VideoView) findViewById(R.id.video_show);
		if (MediaTypeUtils.isVideoFileType(path)) {
			video_show.setVisibility(View.VISIBLE);
			image_show.setVisibility(View.GONE);

			mediaco = new MediaController(this);
			video_show.setVideoPath(path);
			video_show.setMediaController(mediaco);
			mediaco.setMediaPlayer(video_show);
			video_show.requestFocus();
			video_show.start();
		} else {
			video_show.setVisibility(View.GONE);
			image_show.setVisibility(View.VISIBLE);
			loader.displayImage("file://" + path, image_show, options);
		}
		findViewById(R.id.group_photo_cancel).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						finish();
					}
				});
	}
}
