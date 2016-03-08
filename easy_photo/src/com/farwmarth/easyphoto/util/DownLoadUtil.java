package com.farwmarth.easyphoto.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.farwmarth.easyphoto.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("NewApi")
public class DownLoadUtil {
	public static String regex_instagram = "^https://instagram.com/p/.*";
	public static String regex_meipai = "^http://www.meipai.com/media/.*";
	public static String regex_twitter = ".*https://twitter.com/.*/status/.*";
	public static String regex_facebook = "com.facebook.katana";
	public static String regex_flickr = "com.yahoo.mobile.client.android.flickr";
	public static String regex_vine = "https://vine.co/v/";
	public static String regex_weibo = "http://video.weibo.com/show.*";

	public static AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

	static {
		client.setUserAgent("Mozilla/5.0 (Windows; U; Windows NT 5.2) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.2.149.27 Safari/525.13");
		client.setEnableRedirects(true);
		client.setTimeout(5000);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.MAX_REDIRECTS, 3);
	}

	/***
	 * download by share url
	 * 
	 * @param shareUrl
	 *            the url of content
	 * @param context
	 */
	public static void downLoad(String shareUrl, Context context) {
		if (shareUrl.matches(regex_instagram)) {
			downLoadInstagram(shareUrl, context);
		} else if (shareUrl.matches(regex_meipai)) {
			downLoadMeiPai(shareUrl, context);
		}else if (shareUrl.matches(regex_weibo)) {
			downLoadWeiboVideo(shareUrl, context);
		}
	}

	/***
	 * download by share
	 * 
	 * @param intent
	 * @param context
	 */
	public static void downLoad(Intent intent, Context context) {
		String action = intent.getAction();
		String type = intent.getType();
		if (action != null)
			System.out.println(action);
		if (type != null)
			System.out.println(type);
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if (type.startsWith("text/")) {
				// text share
				String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				System.out.println(sharedText);
				// twitter
				if (sharedText.matches(regex_twitter)) {
					String shareUrl = sharedText.substring(sharedText
							.lastIndexOf("https"));
					System.out.println(shareUrl);
					downLoadTwitter(shareUrl, context);
				} else if (sharedText.contains(regex_vine)) {
					String shareUrl = sharedText.substring(sharedText
							.lastIndexOf("https"));
//					System.out.println("vine鍦板潃:"+shareUrl);
					downLoadVine(shareUrl, context);
				}

			} else if (type.startsWith("image/")) {
				// photo share
				Uri imageUri = (Uri) intent
						.getParcelableExtra(Intent.EXTRA_STREAM);
				String path = imageUri.toString();
				System.out.println(path);
				if (path.contains(regex_facebook)) {
					downLoadFaceBook(imageUri, CommonDefine.facebook_dirc,
							context);
				} else if (path.contains(regex_flickr)) {
					downLoadFlickr(imageUri, CommonDefine.flickr_dirc, context);
				}

			} else if (type.startsWith("video/")) {

			}
		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			if (type.startsWith("image/")) {
			}
		}
	}

	// facebook
	private static void downLoadFaceBook(Uri path, String facebook_dirc,
			Context context) {
		String filePath = path.getPath();
		String saveName = filePath.substring(filePath.lastIndexOf("/") + 2);
		MediaUtils.copyFile(path, facebook_dirc, saveName, context);

	}

	// flickr
	private static void downLoadFlickr(Uri path, String flickr_dirc,
			Context context) {
		String filePath = path.getPath();
		String saveName = filePath.substring(filePath.lastIndexOf("/") + 1)
				+ ".jpg";
		MediaUtils.copyFile(path, flickr_dirc, saveName, context);

	}

	// vine
	private static void downLoadVine(String shareUrl,final  Context context) {
		client.get(shareUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				try {
					String html = new String(response, "UTF-8");
					File file = new File(Environment.getExternalStorageDirectory(), "vine.html");
					Document doc = Jsoup.parse(html);
					Elements eles = doc.getElementsByTag("video");
					for (Element link : eles) {
						String linkHref = link.attr("src");
						String fileName = linkHref.substring(
								linkHref.lastIndexOf("/") + 1,
								linkHref.lastIndexOf("?"));
//						System.out.println(fileName);
						downloadBySystem(context, linkHref, fileName,
								CommonDefine.vine_dirc);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
			}

		});
		
	}

	// twitter
	private static void downLoadTwitter(String shareUrl, final Context context) {
		client.get(shareUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				try {
					String html = new String(response, "UTF-8");
					Document doc = Jsoup.parse(html);
					Elements eles = doc.getElementsByAttributeValueContaining(
							"src", "https://pbs.twimg.com/media");
					for (Element link : eles) {
						String linkHref = link.attr("src");
						String fileName = linkHref.substring(linkHref
								.lastIndexOf("/") + 1);
						downloadBySystem(context, linkHref, fileName,
								CommonDefine.twitter_dirc);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
			}

		});
	}

	// meipai
	private static void downLoadMeiPai(String shareUrl, final Context context) {
		flvcdParse(shareUrl, new ParseCallBack() {
			public void execute(String linkHref) {
				String fileName = linkHref.substring(linkHref.lastIndexOf("/") + 1);
				downloadBySystem(context, linkHref, fileName,
						CommonDefine.meipai_dirc);
			}
		});
	}

	// instagram
	private static void downLoadInstagram(String shareUrl, final Context context) {

		client.get(shareUrl, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				try {
					String html = new String(response, "UTF-8");
					Document doc = Jsoup.parse(html);
					Elements eles = doc.getElementsByAttributeValueContaining(
							"content", "https://igcdn-photos");
					for (Element link : eles) {
						String linkHref = link.attr("content");
						String fileName = linkHref.substring(linkHref
								.lastIndexOf("/") + 1);
						downloadBySystem(context, linkHref, fileName,
								CommonDefine.instagram_dirc);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
			}

		});
	}
	
	// weibo video
	private static void downLoadWeiboVideo(String shareUrl, final Context context) {
		weibovideoParse(shareUrl, new ParseCallBack() {
			public void execute(String linkHref) {
				String fileName = linkHref.substring(linkHref.lastIndexOf("/") + 1);
				downloadBySystem(context, linkHref, fileName,
						CommonDefine.weibo_dirc);
			}
		});
	}
	/****************************************************************************************************************/
	/***
	 * download by system downloadmanager
	 * 
	 * @param context
	 * @param linkHref
	 *            the content of rela link
	 * @param fileName
	 *            the name of save file
	 * @param dirc
	 *            the name of sva e dirctionary
	 */
	private static void downloadBySystem(Context context, String linkHref,
			String fileName, String dirc) {
		File folder = FileUtil.createDirOnSDCard(dirc);
		File willDownFile = new File(folder, fileName);
		if (willDownFile.exists()) {
			Toast.makeText(context, R.string.has_down, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		DownloadManager downloadManager = (DownloadManager) context
				.getSystemService(Activity.DOWNLOAD_SERVICE);
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(linkHref));
		request.setDestinationInExternalPublicDir(dirc, fileName);
		request.setTitle(fileName);
		request.setDescription(fileName);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setVisibleInDownloadsUi(true);
		request.allowScanningByMediaScanner();
		downloadManager.enqueue(request);
	}

	private static void flvcdParse(String shareUrl, final ParseCallBack callBack) {
		String flvcdUrl = "http://www.flvcd.com/parse.php";
		HashMap<String, String> maps = new HashMap<String, String>();
		maps.put("format", "");
		maps.put("kw", shareUrl);
		RequestParams params = new RequestParams(maps);
		client.get(flvcdUrl, params, new AsyncHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				try {
					String html = new String(response, "gbk");
					if (!html.contains("瀵逛笉璧凤紝FLVCD鏆備笉鏀寔姝ゅ湴鍧�殑瑙ｆ瀽")) {
						Document doc = Jsoup.parse(html);
						Elements eles = doc
								.getElementsByAttributeValueContaining("class",
										"link");
						for (Element link : eles) {
							String linkHref = link.attr("href");
							callBack.execute(linkHref);
						}
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
			}

		});
	}
	
	private static void weibovideoParse(String shareUrl, final ParseCallBack callBack) {
		String weibovideoUrl = "http://www.weibovideo.com";
		HashMap<String, String> maps = new HashMap<String, String>();
		maps.put("weibourl", shareUrl);
		RequestParams params = new RequestParams(maps);
		client.post(weibovideoUrl, params, new AsyncHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				try {
					String html = new String(response, "utf-8");
						Document doc = Jsoup.parse(html);
						Elements url_p = doc.select(".dizhi a");
						for (Element link : url_p) {
							String linkHref = link.attr("href");
							if (linkHref!=null&& !linkHref.equals("")){
								callBack.execute(linkHref);
							}
						
						}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				System.out.println("失败............");
			}

		});
//		try {
//			Document doc = Jsoup.connect(weibovideoUrl) 
//					  .data("weibourl", "shareUrl")   // 请求参数
//					  .timeout(3000)           // 设置连接超时时间
//					  .post();
//			Elements url_p = doc.select(".dizhi");
//			System.out.println(url_p);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
	}


	interface ParseCallBack {
		public void execute(String result);
	}
}
