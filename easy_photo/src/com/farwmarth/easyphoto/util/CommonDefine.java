package com.farwmarth.easyphoto.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;

public class CommonDefine {
	public static final String Check_Status = "Check_Status";
	public static final String Edit_Status = "Edit_Status";
	public static final String Cache_Action = "Cache_Action";
	public static final String Manager_Action = "Manager_Action";

	public static final String momo_type = "momo";
	public static final String meipai_type = "meipai";
	public static final String instagram_type = "instagram";
	public static final String facebook_type = "facebook";
	public static final String twitter_type = "twiiter";
	public static final String flickr_type = "flickr";
	public static final String vine_type = "vine";
	public static final String weibo_type = "weibo";

	public static final String momo_dirc = "easyphoto/momo";
	public static final String meipai_dirc = "easyphoto/meipai";
	public static final String instagram_dirc = "easyphoto/instagram";
	public static final String facebook_dirc = "easyphoto/facebook";
	public static final String twitter_dirc = "easyphoto/twitter";
	public static final String flickr_dirc = "easyphoto/flickr";
	public static final String vine_dirc = "easyphoto/vine";
	public static final String weibo_dirc = "easyphoto/weibo";

	public static final String cache_dirc = "easyphoto/cache";

	public static HashMap<String, String> type_dircs = new HashMap<String, String>();
	public static ArrayList<String> position_types = new ArrayList<String>();
	static {
		type_dircs.put(momo_type, momo_dirc);
		type_dircs.put(meipai_type, meipai_dirc);
		type_dircs.put(instagram_type, instagram_dirc);
		type_dircs.put(facebook_type, facebook_dirc);
		type_dircs.put(twitter_type, twitter_dirc);
		type_dircs.put(flickr_type, flickr_dirc);
		type_dircs.put(vine_type, vine_dirc);
		type_dircs.put(weibo_type, weibo_dirc);

		position_types.add(momo_type);
		position_types.add(meipai_type);
		position_types.add(instagram_type);
		position_types.add(facebook_type);
		position_types.add(twitter_type);
		position_types.add(flickr_type);
		position_types.add(vine_type);
		position_types.add(weibo_type);
	}

	public static File typeToDircFile(String select_type) {
		String dirc = type_dircs.get(select_type);
		if (dirc != null) {
			File file = new File(Environment.getExternalStorageDirectory(),
					dirc);
			return file;
		}
		return null;
	}

	public static String typeToDirc(String select_type) {
		return type_dircs.get(select_type);
	}

	public static String position_ToType(int position) {
		return position_types.get(position);
	}

}
