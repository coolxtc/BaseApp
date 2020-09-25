package com.coolxtc.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 文件操作类
 * 
 * 缓存文件管理，去那吃，菜谱通用
 * 
 * 实现缓存文件更新，设置缓存过期时间
 * 
 * 图片缓存文件存入时做裁减
 * 
 * 缓存目录文件个数监控
 * 
 * 缓存目录使用空间监控
 * 
 * 
 * @author lizhiyong<lizhiyong@haodou.com>
 * 
 * $Id$
 *
 */
public class FileUtil {

    private static final String TAG = "FileUtil";
	private static final int BUFF_SIZE = 8192;
	
	private FileUtil() {
		
	}


    public static boolean isUninatllApkInfo(Context context, String archiveFilePath) {
        boolean result = false;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath,
                    PackageManager.GET_ACTIVITIES);
            if (info != null) {
                result = true;
            }
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
        }
        return result;
    }


    /**
	 * 获取url对应图片缓存的全路径
	 * 
	 * @param path 	图片缓存目录
	 * @param url	图片url地址
	 * @return
	 */
	public static String getImageCacheFile(String path, String url) {
		StringBuilder sb = new StringBuilder();
		sb.append(path);
		sb.append(Md5Util.MD5Encode(url));
		sb.append(".").append(getExtensionName(url));
		
		return sb.toString();
	}
    /**
	 *获取文件扩展名
	 */
	public static String getExtensionName(String filename) {
	    if ((filename != null) && (filename.length() > 0)) { 
	        int dot = filename.lastIndexOf('.'); 
	        if ((dot > -1) && (dot < (filename.length() - 1))) { 
	            return filename.substring(dot + 1); 
	        } 
	    } 
	    return filename; 
	} 
	
	/**
	 * 
	 * 保存文件至SD卡
	 * 
	 * @param path
	 * @param bytes
	 * @return
	 */
	public static boolean saveFile2SDcard(String path, byte[] bytes) {

		parentFolder(path);
		
		File file = new File(path);
		
        boolean flag = false;
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(
			        new FileOutputStream(file), BUFF_SIZE);
			bos.write(bytes, 0, bytes.length);
			bos.close();
			flag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}

    /**
     * 文件或文件夹拷贝
     * 如果是文件夹拷贝 目标文件必须也是文件夹
     *
     * @param srcPath 源文件
     * @param dstPath 目标文件
     * @return
     */
    public static boolean copy(String srcPath, String dstPath) {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        if (dstPath.endsWith(File.separator) && !mkdirs(dstFile)) { //创建目标目录失败
            return false;
        }

        return copyFile(srcFile, dstFile);
    }

    /**
	 * 文件或文件夹拷贝
	 * 如果是文件夹拷贝 目标文件必须也是文件夹
	 * 
	 * @param srcFile 源文件
	 * @param dstFile 目标文件
	 * @return
	 */
	public static boolean copy(File srcFile, File dstFile) {
        if (!srcFile.exists()) { //源文件不存在
			return false;
		}

		if (srcFile.isDirectory()) { //整个文件夹拷贝
            if (!dstFile.isDirectory()) {    //如果目标不是目录，返回false
                return false;
            }

            for (File f : srcFile.listFiles()) {
                if (!copy(f, new File(dstFile, f.getName()))) {
                    return false;
                }
            }
            return true;

        } else { //单个文价拷贝
            return copyFile(srcFile, dstFile);
        }

    }

    /**
     * 拷贝文件
     * @param srcFile 源文件
     * @param destFile 目标文件，如果是目录，则生成该目录下的同名文件再拷贝
     */
    private static boolean copyFile(File srcFile, File destFile) {
        if (!destFile.exists()) {
            if (!mkdirs(destFile.getParentFile()) || !createNewFile(destFile)) {
                return false;
            }
        } else if (destFile.isDirectory()) {
            destFile = new File(destFile, srcFile.getName());
        }

        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            FileChannel src = in.getChannel();
            FileChannel dst = out.getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(out);
            close(in);
        }

        return false;
    }

    /**
     * 关闭，并捕获IOException
     * @param closeable Closeable
     */
    public static void close(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    /**
	 * 判断某个文件所在的文件夹是否存在，不存在时直接创建
	 * 
	 * @param path
	 */
	public static void parentFolder(String path) {
		File file = new File(path);
		String parent = file.getParent();
		
		File parentFile = new File(parent + File.separator);
		if (!parentFile.exists()) {
            mkdirs(parentFile);
        }
	}

    /**
     * 创建目录（如果不存在）。
     * @param dirPath 目录的路径
     * @return true表示创建，false表示该目录已经存在
     */
    public static boolean createDirIfMissed(String dirPath) {
        File dir = new File(dirPath);
        return !dir.exists() && dir.mkdirs();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件，并且删除该目录
     * @param path 将要删除的文件目录
     * @return boolean 成功清除目录及子文件返回true；
     *                  若途中删除某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean deleteDir(String path) {
        return deleteDir(new File(path));
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件，并且删除该目录
     * @param dir 将要删除的文件目录
     * @return boolean 成功清除目录及子文件返回true；
     *                  若途中删除某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        }

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 递归清空目录下的所有文件及子目录下所有文件，但不删除目录（包括子目录）
     * @param path 将要清空的文件目录
     * @return boolean 成功清除目录及子文件返回true；
     *                  若途中清空某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean clearDir(String path) {
        return clearDir(path, null);
    }

    /**
     * 递归清空目录下的所有文件及子目录下所有文件，但不删除目录（包括子目录）
     * @param path 将要清空的文件目录
	 * @param excepts 除去这些目录或者文件，可以为null
     * @return boolean 成功清除目录及子文件返回true；
     *                  若途中清空某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean clearDir(String path, List<String> excepts) {
    	ArrayList<File> exceptFiles = new ArrayList<File>();
		if (excepts != null) {
			for (String except : excepts) {
				exceptFiles.add(new File(except));
			}
		}
        return clearDir(new File(path), exceptFiles);
    }
    
    /**
     * 递归清空目录下的所有文件及子目录下所有文件，但不删除目录（包括子目录）
     * @param dir 将要清空的文件目录
     * @return boolean 成功清除目录及子文件返回true；
     *                  若途中清空某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean clearDir(File dir) {
    	return clearDir(dir, null);
    }

    /**
     * 递归清空目录下的所有文件及子目录下所有文件，但不删除目录（包括子目录）
     * @param dir 将要清空的文件目录
	 * @param excepts 除去这些目录或者文件，可以为null
     * @return boolean 成功清除目录及子文件返回true；
     *                  若途中清空某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean clearDir(File dir, List<File> excepts) {
        if (dir == null) {
            return false;
        }
        
        if (excepts != null && excepts.contains(dir)) {
        	return true;
        }

        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null) {
                return true;
            }
            for (String child : children) {
                boolean success = clearDir(new File(dir, child), excepts);
                if (!success) {
                    return false;
                }
            }
            return true;
        }

        return dir.delete();
    }

	
	/**
	 * 
	 * 获取某个目录下所有文件的大小之和
	 * @param path
	 * @return
	 */
	public static float getDirSize(String path, boolean isRoot) {
		return getDirSize(path, null, isRoot);
	}

    /**
	 * 
	 * 获取某个目录下所有文件的大小之和
	 * @param path
	 * @param excepts 除去这些目录或者文件，可以为null
	 * @return
	 */
	public static float getDirSize(String path, List<String> excepts, boolean isRoot) {
		if (TextUtils.isEmpty(path)) {
			return 0.f;
		}
		ArrayList<File> exceptFiles = new ArrayList<File>();
		if (excepts != null) {
			for (String except : excepts) {
				exceptFiles.add(new File(except));
			}
		}
		return getDirSize(new File(path), exceptFiles, isRoot);
	}
	
	/**
	 * 
	 * 获取某个目录下所有文件的大小之和
	 * @return
	 */
	public static float getDirSize(File dir, boolean isRoot) {
		return getDirSize(dir, null, isRoot);
	}
	
	/**
	 * 
	 * 获取某个目录下所有文件的大小之和
	 * @param excepts 除去这些目录或者文件，可以为null
	 * @return
	 */
	public static float getDirSize(File dir, List<File> excepts, boolean isRoot) {
		float size = 0.f;
		
		if (dir == null) {
			return size;
		}
		
		if (excepts != null && excepts.contains(dir)) {
			return size;
		}
		
		if (dir.exists()) {
			if (dir.isDirectory()) {
				File[]fs = dir.listFiles();
				for (File childFile : fs) {
					if (childFile.isFile()) {
						size += childFile.length();
					} else {
						size += getDirSize(childFile, excepts, false);
					}
				}
			} else {
				if (!isRoot) {
					size += dir.length();
				}
			}
		} 
		
		return size;
	}
	
	/**
	 * 
	 * 在指定的目录下生成一个.nomedia文件，避免手机相册应用扫描此目录
	 * @param path
	 * @return
	 */
	public static boolean createNomediaFileInPath(String path) {
		File dir = new File(path);
        if (mkdirs(dir)) {
            return false;
        }

        StringBuffer sb = new StringBuffer();
		sb.append(path).append(".nomedia");
		File nomedia = new File(sb.toString());
		if (!nomedia.exists()) {
			try {
				return nomedia.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return true;
		}
	}

    /**
     * 删除文件。如果删除失败，则打出error级别的log
     * @param file 文件
     * @return 成功与否
     */
    public static boolean deleteFile(File file) {
        if (file == null) {
            return false;
        }
        boolean result = file.delete();
        if (!result) {
            Log.e(TAG, "FileUtil cannot delete file: " + file);
        }
        return result;
    }

    /**
     * 创建文件。如果创建失败，则打出error级别的log
     * @param file 文件
     * @return 成功与否
     */
    public static boolean createNewFile(File file) {
        if (file == null) {
            return false;
        }

        boolean result;
        try {
            result = file.createNewFile() || file.isFile();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }

        if (!result) {
            Log.e(TAG, "FileUtil cannot create file: " + file);
        }
        return result;
    }

    /**
     * 创建目录。如果创建失败，则打出error级别的log
     * @param file 文件
     * @return 成功与否
     */
    public static boolean mkdir(File file) {
        if (file == null) {
            return false;
        }
        if (!file.mkdir() && !file.isDirectory()) {
            Log.e(TAG, "FileUtil cannot make dir: " + file);
            return false;
        }
        return true;
    }

    /**
     * 创建文件对应的所有父目录。如果创建失败，则打出error级别的log
     * @param file 文件
     * @return 成功与否
     */
    public static boolean mkdirs(File file) {
        if (file == null) {
            return false;
        }
        if (!file.mkdirs() && !file.isDirectory()) {
            Log.e(TAG, "FileUtil cannot make dirs: " + file);
            return false;
        }
        return true;
    }

    /**
     * 文件或目录重命名。如果失败，则打出error级别的log
     * @param srcFile 原始文件或目录
     * @param dstFile 重命名后的文件或目录
     * @return 成功与否
     */
    public static boolean renameTo(File srcFile, @Nullable File dstFile) {
        if (srcFile == null || dstFile == null) {
            return false;
        }
        if (!srcFile.renameTo(dstFile)) {
            Log.e(TAG, "FileUtil cannot rename " + srcFile + " to " + dstFile);
            return false;
        }

        return true;
    }


    /**
     * 根据URI获取真实地址
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static void delFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            LogUtils.e("delFile " + path + " " + file.delete());

        }
    }

    public static void deleteAllFiles(String path) {
        File root = new File(path);
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f.getPath());
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f.getPath());
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    public static boolean saveFile(InputStream inStream, String savePath) {
        boolean result = false;
        File file = new File(savePath);
        FileOutputStream fileOutputStream = null;//存入SDCard
        ByteArrayOutputStream outStream = null;
        try {
            if(!file.exists()){
                if(file.getParentFile() != null){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[10];
            outStream = new ByteArrayOutputStream();
            int len = 0;
            while((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] bs = outStream.toByteArray();
            fileOutputStream.write(bs);
            fileOutputStream.flush();
            fileOutputStream.close();
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outStream.close();
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    /**
     * *传入路径查看文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isExist(String path) {
        File f = new File(path);
        if (f.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断SDCard是否可用
     */
    public static boolean existSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取应用默认目录
     *
     * @param context
     * @return
     */
    public static File getAppRoot(Context context) {
        String dir = null;
        File file = null;
        if(existSDCard()){
            dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
            file = new File(dir);
        }else{
            file = context.getDir("approot", Context.MODE_PRIVATE);
        }
        return file;
    }

    /**
     * 获取本地文件Uri
     *
     * @param type MEDIA_TYPE_IMAGE or MEDIA_TYPE_VIDEO
     * @param name 名称
     * @return Uri
     */
    public static Uri getOutputMediaFileUri(Context context, int type, String name, String folder) {
        return Uri.fromFile(getOutputMediaFile(context, type, name, folder));
    }

    public static Uri getOutputMediaFileUri(Context context, int type, String name) {
        return getOutputMediaFileUri(context, type, name, null);
    }

    /**
     * 获取本地文件File
     *
     * @param type
     * @return
     */
    private static File getOutputMediaFile(Context context, int type, String name, String folder) {
        File mediaStorageDir = null;
        try {
            if (!TextUtils.isEmpty(folder)) {
                mediaStorageDir = new File(folder);
            } else {
                mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        context.getPackageName());
            }
            Log.d("TAG", "Successfully created mediaStorageDir: "
                    + mediaStorageDir);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG",
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }


        File mediaFile;
        if (!TextUtils.isEmpty(name)) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + name);
        } else {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            //此处，为对应MediaUtils.MEDIA_TYPE_IMAGE的值，暂时常量固定，后期可优化解决方案
            if (type == 0x000001) {//MediaUtils.MEDIA_TYPE_IMAGE
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");
            } else if (type == 0x000002) {//MediaUtils.MEDIA_TYPE_VIDEO
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }
        }


        return mediaFile;
    }

    /**
     * 获取SD大小
     *
     * @return
     */
    public static long getAllSize() {
        if (!existSDCard()) {
            return 0L;
        }
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        return availableBlocks * blockSize;
    }

    /**
     * 多个SD卡时 取外置SD卡
     *
     * @return
     */
    public static String getExternalStorageDirectory() {
        Map<String, String> map = System.getenv();
        String[] values = new String[map.values().size()];
        map.values().toArray(values);
        String path = values[values.length - 1];
        if (path.startsWith("/mnt/") && !Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                .equals(path)) {
            return path;
        } else {
            return null;
        }
    }
}

