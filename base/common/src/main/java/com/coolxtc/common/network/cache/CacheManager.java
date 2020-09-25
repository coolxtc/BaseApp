package com.coolxtc.common.network.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 描 述:
 *
 * @author: lihui
 * @date: 2017-12-08 17:28
 */

public class CacheManager {

    private DiskLruCache mDiskLruCache;

    public CacheManager(Context context) {
        initCache(context);
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        cachePath = context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private void initCache(Context context) {
        try {
            File cacheDir = getDiskCacheDir(context, "okhttp");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public void writeCache(final String url, final CacheBean txt) {
        if (mDiskLruCache != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String key = hashKeyForDisk(url);
                        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                        if (editor != null) {
                            if (txt != null && txt.getContent() != null && txt.getContent().length() > 0) {
                                editor.set(0, "" + txt.getTime() + txt.getContent());
                                editor.commit();
//                                LogUtils.e("cache -> write " + txt.getTime() + txt.getContent());
                            } else {
                                editor.abort();
                            }
                        }
                        mDiskLruCache.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public synchronized CacheBean getCache(String url) {
        CacheBean result = new CacheBean();
        if (mDiskLruCache != null) {
            try {
                String key = hashKeyForDisk(url);
                DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                if (snapShot != null) {
                    String resultStr = snapShot.getString(0);
                    if (resultStr != null && resultStr.length() > 13) {
                        result.setTime(Long.decode(resultStr.substring(0, 13)));
                        result.setContent(resultStr.substring(13, resultStr.length()));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private void removeCache(String url) {
        if (mDiskLruCache != null) {
            try {
                String key = hashKeyForDisk(url);
                mDiskLruCache.remove(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
