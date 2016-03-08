package com.farwmarth.easyphoto.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.widget.ImageView;

import com.farwmarth.easyphoto.MainActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MediaUtils {
	private static ExecutorService pool = Executors.newFixedThreadPool(2);

	public static String saveBitToSD(Bitmap bit, String fileName, String dirc) {
		if (bit == null || bit.isRecycled())
			return "";

		File dirFile = FileUtil.createDirOnSDCard(dirc);
		File pathFile = new File(dirFile, fileName);
		if (pathFile.exists()) {
			return pathFile.getAbsolutePath();
		} else {
			ImageUtils.Bitmap2File(bit, pathFile.getAbsolutePath());
			return pathFile.getAbsolutePath();
		}
	}

	public static void copyFile(Uri dest, String dirc, String savename,
			Context context) {
		try {
			File folder = FileUtil.createDirOnSDCard(dirc);
			File newPath = new File(folder, savename);
			// System.out.println("新文件:" + newPath.getAbsolutePath());
			int byteread = 0;
			ContentResolver cr = context.getContentResolver();
			InputStream inStream = cr.openInputStream(dest);
			FileOutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[1444];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
			fs.close();
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void recursionMoMo(ArrayList<String> images, File root) {
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory())
				recursionMoMo(images, file);
			else
				images.add(file.getAbsolutePath());
		}
	}

	public static ArrayList<String> listMoMoCacheMedia() {
		ArrayList<String> images = new ArrayList<String>();
		File file = new File(Environment.getExternalStorageDirectory(),
				"/immomo/avatar/large");
		if (file.exists()) {
			recursionMoMo(images, file);
		}
		return images;
	}

	public static ArrayList<String> listMedia(String select_type) {
		ArrayList<String> mediaPaths = new ArrayList<String>();
		File file = CommonDefine.typeToDircFile(select_type);
		if (file != null && file.exists()) {
			File[] files = file.listFiles();
			for (File file2 : files) {
				String path = file2.getAbsolutePath();
				if (file2.isFile()
						&& (MediaTypeUtils.isVideoFileType(path) || MediaTypeUtils
								.isImageFileType(path))) {
					mediaPaths.add(path);
				}
			}
		}
		return mediaPaths;
	}

	public static ArrayList<String> listMediaData(String select_type,
			String action_type) {
		if (select_type == CommonDefine.momo_type) {
			if (action_type == CommonDefine.Cache_Action)
				return MediaUtils.listMoMoCacheMedia();
			else
				return MediaUtils.listMedia(select_type);
		} else {
			return MediaUtils.listMedia(select_type);
		}
	}

	public static void setVideoThumbnail(ImageLoader loader,
			DisplayImageOptions options, final ImageView imageView,
			final String media_path, String select_type,
			final MainActivity mContext) {
		StringBuffer thumbName = new StringBuffer(select_type)
				.append("_")
				.append(media_path.substring(media_path.lastIndexOf("/") + 1,
						media_path.lastIndexOf("."))).append(".jpg");
		final String thumbStr = thumbName.toString();
		File thumbDirc = FileUtil.createDirOnSDCard(CommonDefine.cache_dirc);
		File thumbFile = new File(thumbDirc, thumbStr);
		if (thumbFile.exists()) {
			loader.displayImage("file://" + thumbFile.getAbsolutePath(),
					imageView, options);
		} else {
			pool.execute(new Runnable() {
				public void run() {
					final Bitmap thumbnail = ThumbnailFixUtils
							.createVideoThumbnail(Images.Thumbnails.MICRO_KIND,
									media_path, 96, 96);
					MediaUtils.saveBitToSD(thumbnail, thumbStr,
							CommonDefine.cache_dirc);
					mContext.getHandler().post(new Runnable() {
						public void run() {
							imageView.setImageBitmap(thumbnail);
						}
					});
				}
			});

		}
	}
}
