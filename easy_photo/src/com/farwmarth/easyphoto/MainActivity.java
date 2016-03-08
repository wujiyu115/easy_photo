package com.farwmarth.easyphoto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.farwmarth.easyphoto.R;
import com.farwmarth.easyphoto.fragment.HomeFragment;
import com.farwmarth.easyphoto.fragment.MenuFragment;
import com.farwmarth.easyphoto.fragment.MenuFragment.SLMenuListOnItemClickListener;
import com.farwmarth.easyphoto.fragment.PageFragment;
import com.farwmarth.easyphoto.service.DownloadService;
import com.farwmarth.easyphoto.util.CommonDefine;
import com.farwmarth.easyphoto.util.DownLoadUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements
		SLMenuListOnItemClickListener {
	private SlidingMenu mSlidingMenu;
	private Intent downloadService;
	private Handler handler = new Handler();

	public Handler getHandler() {
		return handler;
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null && bundle.getBoolean("close")) {
			exit();
			return;
		}
		setContentView(R.layout.frame_content);
		setBehindContentView(R.layout.frame_left_menu);

		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT);
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.drawer_shadow);
		// mSlidingMenu.setShadowWidth(5);
		// mSlidingMenu.setBehindOffset(100);
		mSlidingMenu.setShadowDrawable(R.drawable.drawer_shadow);
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.left_menu, new MenuFragment());
		fragmentTransaction.replace(R.id.content, new HomeFragment());
		fragmentTransaction.commit();

		// 开启监听服务
		this.downloadService = new Intent(getApplicationContext(),
				DownloadService.class);
		startService(this.downloadService);

		DownLoadUtil.downLoad(intent, getApplicationContext());
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void selectItem(int position, String title) {
		String type = CommonDefine.position_ToType(position);
		if (type != null) {
			Fragment fragment = new PageFragment(type, position);
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content, fragment)
					.commit();
			setTitle(title);
			mSlidingMenu.showContent();

		}
	}

	private void exit() {
		if (this.downloadService != null)
			stopService(this.downloadService);
		this.finish();
		Process.killProcess(Process.myPid());
	}
}
