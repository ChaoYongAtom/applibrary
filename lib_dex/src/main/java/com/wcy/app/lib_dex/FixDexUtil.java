package com.wcy.app.lib_dex;

/**
 * FixDexUtil
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/11/5
 * @description applibrary
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixDexUtil {
    private static FixDexUtil fixDexUtil = null;
    private String TAG = "FixDexUtil";
    private final String DEX_SUFFIX = ".dex";
    private final String APK_SUFFIX = ".apk";
    private final String JAR_SUFFIX = ".jar";
    private final String ZIP_SUFFIX = ".zip";
    public final String DEX_DIR = "odex";
    private final String OPTIMIZE_DEX_DIR = "optimize_dex";
    private Context mContext;

    public static FixDexUtil getInstance() {
        if (fixDexUtil == null) {
            synchronized (FixDexUtil.class) {
                if (fixDexUtil == null) {
                    fixDexUtil = new FixDexUtil();
                }
                return fixDexUtil;
            }
        } else {
            return fixDexUtil;
        }
    }

    private FixDexUtil() {
    }

    public void init(Context context, String key) {
        this.mContext = context;
        DownDex.post(key);
    }

    /**
     * 加载补丁，使用默认目录：data/data/包名/files/odex
     */
    public void loadFixedDex(String fileName) {
        loadFixedDex(getDexPaht(), fileName);
    }

    /**
     * 加载补丁
     *
     * @param path 补丁所在目录
     */
    private void loadFixedDex(String path, String fileName) {
        try {
            // 遍历所有的修复dex , 因为可能是多个dex修复包
            File[] listFiles = new File(path).listFiles();
            if (listFiles != null && listFiles.length > 0) {
                File loadedDex = null;
                for (File file : listFiles) {
                    String fName = file.getName();
                    if (fileName.equals(fName)) {
                        loadedDex = file;
                    } else {
                        if (file.isDirectory()) {
                            deleFile(file);
                        } else {
                            file.delete();
                        }
                    }
                }
                if (loadedDex != null) {
                    if (loadedDex != null) {
                        String optimizeDir = mContext.getFilesDir().getAbsolutePath() + File.separator + OPTIMIZE_DEX_DIR;
                        // data/data/包名/files/optimize_dex（这个必须是自己程序下的目录）
                        File fopt = new File(optimizeDir);
                        if (!fopt.exists()) {
                            fopt.mkdirs();
                        } else {
                            deleFile(fopt);
                        }
                        // 1.加载应用程序dex的Loader
                        PathClassLoader pathLoader = (PathClassLoader) mContext.getClassLoader();
                        // 2.加载指定的修复的dex文件的Loader
                        DexClassLoader dexLoader = new DexClassLoader(loadedDex.getAbsolutePath(),// 修复好的dex（补丁）所在目录
                                fopt.getAbsolutePath(),// 存放dex的解压目录（用于jar、zip、apk格式的补丁）
                                null,// 加载dex时需要的库
                                pathLoader// 父类加载器
                        );
                        // 3.开始合并
                        // 合并的目标是Element[],重新赋值它的值即可
                        /**
                         * BaseDexClassLoader中有 变量: DexPathList pathList
                         * DexPathList中有 变量 Element[] dexElements
                         * 依次反射即可
                         */

                        //3.1 准备好pathList的引用
                        Object dexPathList = getPathList(dexLoader);
                        Object pathPathList = getPathList(pathLoader);
                        //3.2 从pathList中反射出element集合
                        Object leftDexElements = getDexElements(dexPathList);
                        Object rightDexElements = getDexElements(pathPathList);
                        //3.3 合并两个dex数组
                        Object dexElements = combineArray(leftDexElements, rightDexElements);
                        // 重写给PathList里面的Element[] dexElements;赋值
                        Object pathList = getPathList(pathLoader);// 一定要重新获取，不要用pathPathList，会报错
                        setField(pathList, pathList.getClass(), "dexElements", dexElements);
                        Log.i(TAG, "修复完成");
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "修复失败");
            e.printStackTrace();
        }
    }

    /**
     * 是否有新版本
     *
     * @param ver
     * @return
     */
    public boolean isLatestVersion(int ver) {
        boolean falg = true;
        try {
            File file = getDexFile();
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    String fName = f.getName();
                    if (fName.endsWith(APK_SUFFIX) || fName.endsWith(DEX_SUFFIX) || fName.endsWith(JAR_SUFFIX) || fName.endsWith(ZIP_SUFFIX)) {
                        String[] versions = fName.split("_");
                        if (versions.length ==3) {
                            Integer v = Integer.parseInt(versions[1]);
                            if (v >= ver) {
                                falg = false;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return falg;
    }

    private void deleFile(File files) {
        File[] odexs = files.listFiles();
        if (odexs != null && odexs.length > 0) {
            for (File file : odexs) {
                file.delete();
            }
        }
    }

    /**
     * 反射给对象中的属性重新赋值
     */
    private void setField(Object obj, Class<?> cl, String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cl.getDeclaredField(field);
        declaredField.setAccessible(true);
        declaredField.set(obj, value);
    }

    /**
     * 反射得到对象中的属性值
     */
    private Object getField(Object obj, Class<?> cl, String field) throws NoSuchFieldException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }


    /**
     * 反射得到类加载器中的pathList对象
     */
    private Object getPathList(Object baseDexClassLoader) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    /**
     * 反射得到pathList中的dexElements
     */
    private Object getDexElements(Object pathList) throws NoSuchFieldException, IllegalAccessException {
        return getField(pathList, pathList.getClass(), "dexElements");
    }

    /**
     * 数组合并
     */
    private Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> clazz = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);// 得到左数组长度（补丁数组）
        int j = Array.getLength(arrayRhs);// 得到原dex数组长度
        int k = i + j;// 得到总数组长度（补丁数组+原dex数组）
        Object result = Array.newInstance(clazz, k);// 创建一个类型为clazz，长度为k的新数组
        System.arraycopy(arrayLhs, 0, result, 0, i);
        System.arraycopy(arrayRhs, 0, result, i, j);
        return result;
    }


    public String getDexPaht() {
        return getDexFile().getPath();
    }

    private File getDexFile() {
        //        File externalStorageDirectory = Environment.getExternalStorageDirectory();
//        // 遍历所有的修复dex , 因为可能是多个dex修复包
//        File fileDir = externalStorageDirectory != null ? externalStorageDirectory : context.getFilesDir();// data/data/包名/files/odex（这个可以任意位置）
        File externalStorageDirectory = mContext.getFilesDir();
        File path = new File(externalStorageDirectory, DEX_DIR);
        if (!path.exists()) {
            path.mkdirs();
        }

        return path;
    }

    /**
     * 获取App版本码
     *
     * @return App版本码
     */
    public int getAppVersionCode() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String getPackageName() {
        return mContext.getPackageName();
    }
}


