package com.farwmarth.easyphoto.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * 文件夹和文件处理工具
 * 
 * @author file
 * @since 2013-6-20 下午5:06:25
 */
public final class FileUtil {
	/**
	 * 如果目录不存在则创建目录
	 * 
	 * @param path
	 *            路径
	 * @return 是否创建目录
	 * @throws Exception
	 *             创建目录失败，抛出异常
	 */
	public static boolean createDirIfNoExists(String path) throws Exception {
		File file = new File(path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				throw new Exception("createDir is failed,the path is " + path);
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 如果文件存在则删除文件
	 * 
	 * @param path
	 *            路径
	 * @return 是否删除
	 */
	public static boolean deleteFileIfExists(String path) {
		File file = new File(path);
		return deleteFileIfExists(file);
	}

	/**
	 * 如果文件存在则删除文件
	 * 
	 * @param file
	 *            文件对象
	 * @return 是否删除
	 */
	public static boolean deleteFileIfExists(File file) {

		if (file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}

	/****
	 * 写文件
	 * 
	 * @param fileName
	 * @param data
	 * @return
	 */
	public static boolean writeFile(String fileName, String data) {
		return writeFile(new File(fileName), data);
	}

	public static boolean writeFile(File file, String data) {
		FileWriter fw;
		try {
			if (!file.exists())
				file.createNewFile();
			fw = new FileWriter(file);
			fw.write(data);
			fw.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 创建文件并写入数据
	 * 
	 * @param path
	 *            路径
	 * @param data
	 *            数据
	 * @return 是否成功创建文件并写入
	 * @throws Exception
	 */
	public static boolean createFileAndWrite(String path, String data)
			throws Exception {
		File file = new File(path);
		return createFileAndWrite(file, data);
	}

	/**
	 * 创建文件并写入数据
	 * 
	 * @param file
	 *            文件对象
	 * @param data
	 *            数据
	 * @return 是否成功创建文件并写入
	 * @throws Exception
	 */
	public static boolean createFileAndWrite(File file, String data)
			throws Exception {
		// 文件存在则先删除
		deleteFileIfExists(file);

		FileWriter fw = null;
		try {
			// 创建文件 则写数据
			if (file.createNewFile()) {
				fw = new FileWriter(file);
				fw.write(data);
				fw.flush();
				// 创建失败
			} else {
				throw new Exception("create file is fail ,this file path is "
						+ file);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("create file have IOException ", e);
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception("close FileWriter have IOException ", e);
				}
		}

		return false;
	}

	/**
	 * 根据文件路径读取文件内容
	 * 
	 * @param path
	 *            路径
	 * @return 文件内容
	 * @throws Exception
	 */
	public static String readData(String path) throws Exception {
		return readData(new File(path));
	}

	/**
	 * 根据文件对象读取文件内容
	 * 
	 * @param file
	 *            文件对象
	 * @return 文件内容
	 * @throws Exception
	 */
	public static String readData(File file) throws Exception {

		StringBuilder builder = new StringBuilder();
		// 文件存在
		if (file.exists()) {
			FileReader reader = null;
			BufferedReader br = null;
			try {
				// 读取数据
				reader = new FileReader(file);
				br = new BufferedReader(reader);
				String content = null;
				while ((content = br.readLine()) != null) {
					builder.append(content);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new Exception("the file not found....", e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("read file have IOException ", e);
			} finally {
				try {
					if (br != null) {
						br.close();
					}
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception("close Reader have IOException ", e);
				}
			}
		}

		return builder.toString();
	}

	public static File createDirOnSDCard(String dirc) {
		File file = new File(Environment.getExternalStorageDirectory(), dirc);
		File dirFile = new File(file.getAbsolutePath());
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return dirFile;
	}

	public static void copyFile(String filePath, String dirc,int offset) {
		try {
			File copy_file = new File(filePath);
			if (copy_file.exists()) {
				File folder = FileUtil.createDirOnSDCard(dirc);
				String fileName= copy_file.getName();
				File newPath = new File(folder, fileName.substring(0,fileName.length()-1));
				int byteread = 0;
				InputStream inStream = new FileInputStream(copy_file);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
					fs.flush();
				}
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copyFiles(ArrayList<String> paths, String dirc,int offset) {
		for (String path : paths) {
			copyFile(path, dirc,offset);
		}
	}
}
