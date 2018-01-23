package org.wcy.android.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * 获取资源文件id辅助类
 * @author wangchaoyong
 *
 */
public class MResource {
	/**
	 * 获取资源文件编号
	 * @param context
	 * @param className 目录(layout,values,drawable)
	 * @param name 资源名
	 * @return
	 */
	public static int getByIdName(Context context, String className, String name) {
		Resources resources=context.getResources();
		return resources.getIdentifier(name,className, context.getPackageName());
	}
	/**
	 * 获取资源文件编号
	 * @param context
	 * @param name 资源名
	 * @return
	 */
	public static int getidByIdName(Context context,  String name) {
		return getByIdName(context,"id", name);
	}
	/**
	 * 获取资源文件编号
	 * @param context
	 * @param name 资源名
	 * @return
	 */
	public static int getlayoutByIdName(Context context,  String name) {
		return getByIdName(context,"layout", name);
	}
	/**
	 * 获取资源文件编号
	 * @param context
	 * @param name 资源名
	 * @return
	 */
	public static int getStyleByIdName(Context context,  String name) {
		return getByIdName(context,"style", name);
	}
	/**
	 * 获取资源文件编号
	 * @param context
	 * @param name 资源名
	 * @return
	 */
	public static int getdrawableByIdName(Context context,  String name) {
		return getByIdName(context,"drawable", name);
	}
	
}
