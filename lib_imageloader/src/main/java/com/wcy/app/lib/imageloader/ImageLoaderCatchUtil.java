package com.wcy.app.lib.imageloader;

import android.content.Context;
import android.os.Looper;

import com.bumptech.glide.Glide;


import java.io.File;
import java.math.BigDecimal;

/**
 * Created by wcy
 * on 2018/4/18.
 * Glide缓存工具类
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class ImageLoaderCatchUtil {
    private static ImageLoaderCatchUtil instance;
    // 图片缓存子目录
    public final String GLIDE_CARCH_DIR = "/image_manager_disk_cache";
    private Context mContext;
    public static ImageLoaderCatchUtil getInstance(Context context) {
        if (null == instance) {
            instance = new ImageLoaderCatchUtil(context);
        }
        return instance;
    }
    private ImageLoaderCatchUtil(Context context){
        mContext=context;
    }
    //外部路径
    public String getStorageDirectory(Context context) {
        //手机app路径
        String appRootPath = context.getCacheDir().getPath() + GLIDE_CARCH_DIR;
        return appRootPath;
    }

    // 获取Glide磁盘缓存大小
    public String getCacheSize() {
        try {
            return getFormatSize(getFolderSize(new File(getStorageDirectory(mContext))));
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }

    // 清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
    public boolean cleanCatchDisk() {
        return deleteFolderFile(getStorageDirectory(mContext), true);
    }

    public void clear() {
        cleanCatchDisk();
        clearCacheDiskSelf();
        clearCacheMemory();
    }

    // 清除图片磁盘缓存，调用Glide自带方法
    public boolean clearCacheDiskSelf() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(mContext).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(mContext).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 清除Glide内存缓存
    public boolean clearCacheMemory() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(mContext).clearMemory();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // 获取指定文件夹内所有文件大小的和
    private long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File aFileList : fileList) {
                    if (aFileList.isDirectory()) {
                        size = size + getFolderSize(aFileList);
                    } else {
                        size = size + aFileList.length();
                    }
                }
            }

        } catch (Exception e) {
        }
        return size;
    }

    // 格式化单位
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    // 按目录删除文件夹文件方法
    private boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFolderFile(file1.getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
