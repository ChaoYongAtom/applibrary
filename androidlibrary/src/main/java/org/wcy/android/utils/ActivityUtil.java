package org.wcy.android.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

/**
 * :Activity特性辅助工具
 * 
 * @author wangchaoyong
 * @version 1.0
 * @created 2014-9-4
 */
public class ActivityUtil {
	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	/**
	 * 应用于Activity的获取控件实例
	 * 
	 * @param activity
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T findViewById(Activity activity, int id) {
		return (T) activity.findViewById(id);
	}

	/**
	 * 应用于View的获取控件实例
	 * 
	 * @param view
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T findViewById(View view, int id) {
		return (T) view.findViewById(id);
	}

	/**
	 * 获取当前手机的可用内存
	 * 
	 * @param context
	 * @return
	 */
	public static String getAvailMemory(Context context) {// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
	}

	/**
	 * title : 设置Activity全屏显示 description :设置Activity全屏显示。
	 * 
	 * @param activity
	 *            Activity引用
	 * @param isFull
	 *            true为全屏，false为非全屏
	 */
	public static void setFullScreen(Activity activity, boolean isFull) {
		Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (isFull) {
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(params);
			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.setAttributes(params);
			window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	/**
	 * title : 隐藏系统标题栏 description :隐藏Activity的系统默认标题栏
	 * 
	 * @param activity
	 *            Activity对象
	 */
	public static void hideTitleBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * title : 设置Activity的显示方向为垂直方向 description :强制设置Actiity的显示方向为垂直方向。
	 * 
	 * @param activity
	 *            Activity对象
	 */
	public static void setScreenVertical(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * title : 设置Activity的显示方向为横向 description :强制设置Actiity的显示方向为横向。
	 * 
	 * @param activity
	 *            Activity对象
	 */
	public static void setScreenHorizontal(Activity activity) {
		/**
		 * 设置为横屏
		 */
		if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	/**
	 * title : 隐藏软件输入法 description :隐藏软件输入法 time : 2012-7-12 下午7:20:00
	 * 
	 * @param activity
	 */
	public static void hideSoftInput(Activity activity) {
		try{
			((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}catch (Exception e){
		}
	}

	/**
	 * title : 使UI适配输入法 description :使UI适配输入法 下午10:21:26
	 * 
	 * @param activity
	 */
	public static void adjustSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	/**
	 * title : 跳转到某个Activity。 description :跳转到某个Activity time : 2012-7-8
	 * 下午3:20:00
	 * 
	 * @param activity
	 *            本Activity
	 * @param targetActivity
	 *            目标Activity的Class
	 */
	public static void switchTo(Activity activity, Class<? extends Activity> targetActivity) {
		switchTo(activity, new Intent(activity, targetActivity));
	}

	/**
	 * title : 根据给定的Intent进行Activity跳转 description :根据给定的Intent进行Activity跳转 time
	 * : 2012-7-8 下午3:22:23
	 * 
	 * @param activity
	 *            Activity对象
	 * @param intent
	 *            要传递的Intent对象
	 */
	public static void switchTo(Activity activity, Intent intent) {
		activity.startActivity(intent);
	}

	/**
	 * title : 带参数进行Activity跳转 description :带参数进行Activity跳转 time : 2012-7-8
	 * 下午3:24:54
	 * 
	 * @param activity
	 *            Activity对象
	 * @param targetActivity
	 *            目标Activity的Class
	 * @param params
	 *            跳转所带的参数
	 */
	public static void switchTo(Activity activity, Class<? extends Activity> targetActivity, Map<String, Object> params) {
		if (null != params) {
			Intent intent = new Intent(activity, targetActivity);
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				setValueToIntent(intent, entry.getKey(), entry.getValue());
			}
			switchTo(activity, intent);
		}
	}


	/**
	 * title : 显示Toast消息。 description :显示Toast消息，并保证运行在UI线程中 下午08:36:02
	 * 
	 * @param activity
	 * @param message
	 */
	public static void toastShow(final Activity activity, final String message) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 
	 * @param button
	 * @return
	 */
	public static String ButtonString(Button button) {
		return button.getText().toString();
	}

	/**
	 * 得到TextView的值
	 * 
	 * @param textview
	 * @return
	 */
	public static String TextString(TextView textview) {
		return textview.getText().toString();
	}

	/**
	 * 得到EditText的值
	 * 
	 * @param edittext
	 * @return
	 */
	public static String EditString(EditText edittext) {
		return edittext.getText().toString();
	}

	/**
	 * title : 将值设置到Intent里 description :将值设置到Intent里
	 * 
	 * @param intent
	 *            Inent对象
	 * @param key
	 *            Key
	 * @param val
	 *            Value
	 */
	public static void setValueToIntent(Intent intent, String key, Object val) {
		if (val instanceof Boolean)
			intent.putExtra(key, (Boolean) val);
		else if (val instanceof Boolean[])
			intent.putExtra(key, (Boolean[]) val);
		else if (val instanceof String)
			intent.putExtra(key, (String) val);
		else if (val instanceof String[])
			intent.putExtra(key, (String[]) val);
		else if (val instanceof Integer)
			intent.putExtra(key, (Integer) val);
		else if (val instanceof Integer[])
			intent.putExtra(key, (Integer[]) val);
		else if (val instanceof Long)
			intent.putExtra(key, (Long) val);
		else if (val instanceof Long[])
			intent.putExtra(key, (Long[]) val);
		else if (val instanceof Double)
			intent.putExtra(key, (Double) val);
		else if (val instanceof Double[])
			intent.putExtra(key, (Double[]) val);
		else if (val instanceof Float)
			intent.putExtra(key, (Float) val);
		else if (val instanceof Float[])
			intent.putExtra(key, (Float[]) val);
	}

	/**
	 * 取得edittext的值转换成double
	 *
	 * @param et
	 * @return
	 */
	public static Double getDouble(EditText et) {
		String str = et.getText().toString();
		if (StringUtil.hasText(str)) {
			return Double.parseDouble(str);
		} else {
			return 0.00;
		}
	}

	/**
	 * 取得edittext的值转换成Integer
	 * 
	 * @param et
	 * @return
	 */
	public static Integer getInt(EditText et) {
		String str = et.getText().toString();
		if (StringUtil.hasText(str)) {
			return Integer.parseInt(str);
		} else {
			return 0;
		}
	}

	/**
	 * 判断输入框是否为null
	 * 
	 * @param et
	 * @return
	 */
	public static boolean isEditTextNull(EditText et) {
		return StringUtil.hasText(et.getText().toString());
	}
	/**
	 * 判断输入框是否为null
	 *
	 * @param et
	 * @return
	 */
	public static boolean isEditTextNull(TextView et) {
		return StringUtil.hasText(et.getText().toString());
	}
	/**
	 * 获取当前软件软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			versionCode = 0;
		}
		return versionCode;
	}

	/**
	 * 获取当前软件软件版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			versionName = "";
		}
		return versionName;
	}
}
