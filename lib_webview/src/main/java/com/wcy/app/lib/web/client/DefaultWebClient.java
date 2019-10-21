package com.wcy.app.lib.web.client;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;

import  com.wcy.app.lib.web.SuperWebX5Config;
import  com.wcy.app.lib.web.utils.DefaultMsgConfig;
import  com.wcy.app.lib.web.utils.LogUtils;
import  com.wcy.app.lib.web.utils.PermissionInterceptor;
import  com.wcy.app.lib.web.utils.SuperWebX5Utils;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

import static android.content.ContentValues.TAG;

/**
 * 默认的web客户端
 */
public class DefaultWebClient extends WrapperWebViewClient {

	private WebViewClientCallbackManager mWebViewClientCallbackManager;
	private WeakReference<Activity> mWeakReference = null;
	private static final int CONSTANTS_ABNORMAL_BIG = 7;
	private WebViewClient mWebViewClient;
	private boolean webClientHelper = false;
	private static final String WEBVIEWCLIENTPATH = "com.tencent.smtt.sdk.WebViewClient";
	public static final String INTENT_SCHEME = "intent://";

	public static final int DERECT_OPEN_OTHER_APP = 1001;
	public static final int ASK_USER_OPEN_OTHER_APP = DERECT_OPEN_OTHER_APP >> 2;
	public static final int DISALLOW_OPEN_OTHER_APP = DERECT_OPEN_OTHER_APP >> 4;

	private static final boolean hasAlipayLib;

	static {
		boolean tag = true;
		hasAlipayLib = tag;

		LogUtils.i("Info", "static  hasAlipayLib:" + hasAlipayLib);
	}


	public int schemeHandleType = ASK_USER_OPEN_OTHER_APP;
	private DefaultMsgConfig.WebViewClientMsgCfg mMsgCfg = null;

	DefaultWebClient(Builder builder) {
		super(builder.client);
		this.mWebViewClient = builder.client;
		mWeakReference = new WeakReference<Activity>(builder.activity);
		this.mWebViewClientCallbackManager = builder.manager;
		this.webClientHelper = builder.webClientHelper;
		LogUtils.i(TAG, "schemeHandleType:" + schemeHandleType);
		if (builder.schemeHandleType <= 0) {
			schemeHandleType = ASK_USER_OPEN_OTHER_APP;
		} else {
			schemeHandleType = builder.schemeHandleType;
		}
		this.mMsgCfg = builder.mCfg;
	}


	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		LogUtils.i(TAG, "shouldOverrideUrlLoading --->  url:" + url);


		int tag = -1;

		if (SuperWebX5Utils.isOverriedMethod(mWebViewClient, "shouldOverrideUrlLoading", WEBVIEWCLIENTPATH + ".shouldOverrideUrlLoading", WebView.class, String.class) && (((tag = 1) > 0) && super.shouldOverrideUrlLoading(view, url))) {
			return true;
		}

		if (!webClientHelper) {
			return false;
		}
		if (handleLinked(url)) { //电话 ， 邮箱 ， 短信
			return true;
		}
		if (url.startsWith(INTENT_SCHEME)) { //Intent scheme
			handleIntentUrl(url);
			return true;
		}


		if (tag > 0)
			return false;


		return super.shouldOverrideUrlLoading(view, url);
	}

	private int queryActivies(String url) {

		try {
			if (mWeakReference.get() == null) {
				return 0;
			}
			Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
			PackageManager mPackageManager = mWeakReference.get().getPackageManager();
			List<ResolveInfo> mResolveInfos = mPackageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			return mResolveInfos == null ? 0 : mResolveInfos.size();
		} catch (URISyntaxException ignore) {
			if (LogUtils.isDebug()) {
				ignore.printStackTrace();
			}
			return 0;
		}
	}

	private AlertDialog askOpenOtherAppDialog = null;

	private String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName =
				(String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	private boolean openOtherPage(String intentUrl) {
		try {
			Intent intent;
			Activity mActivity = null;
			if ((mActivity = mWeakReference.get()) == null)
				return true;
			PackageManager packageManager = mActivity.getPackageManager();
			intent = new Intent().parseUri(intentUrl, Intent.URI_INTENT_SCHEME);
			ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
			LogUtils.i(TAG, "resolveInfo:" + info + "   package:" + intent.getPackage());
			if (info != null) {  //跳到该应用
				mActivity.startActivity(intent);
				return true;
			}
		} catch (Throwable ignore) {
			if (LogUtils.isDebug()) {
				ignore.printStackTrace();
			}
		}

		return false;
	}

	private Object mPayTask; //alipay


	public static final String SCHEME_SMS = "sms:";


	private boolean handleLinked(String url) {
		if (url.startsWith(android.webkit.WebView.SCHEME_TEL)
				|| url.startsWith(SCHEME_SMS)
				|| url.startsWith(android.webkit.WebView.SCHEME_MAILTO)
				|| url.startsWith(android.webkit.WebView.SCHEME_GEO)) {
			try {
				Activity mActivity = null;
				if ((mActivity = mWeakReference.get()) == null)
					return false;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				mActivity.startActivity(intent);
			} catch (ActivityNotFoundException ignored) {
				if (SuperWebX5Config.DEBUG) {
					ignored.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}



	private void handleIntentUrl(String intentUrl) {
		try {

			Intent intent = null;
			if (TextUtils.isEmpty(intentUrl) || !intentUrl.startsWith(INTENT_SCHEME))
				return;

			Activity mActivity = null;
			if ((mActivity = mWeakReference.get()) == null)
				return;
			PackageManager packageManager = mActivity.getPackageManager();
			intent = new Intent().parseUri(intentUrl, Intent.URI_INTENT_SCHEME);
			ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
			LogUtils.i("Info", "resolveInfo:" + info + "   package:" + intent.getPackage());
			if (info != null) {  //跳到该应用
				mActivity.startActivity(intent);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	private boolean handleNormalLinked(String url) {
		if (url.startsWith(WebView.SCHEME_TEL) || url.startsWith("sms:") || url.startsWith(WebView.SCHEME_MAILTO)) {
			try {
				Activity mActivity = null;
				if ((mActivity = mWeakReference.get()) == null)
					return false;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				mActivity.startActivity(intent);
			} catch (ActivityNotFoundException ignored) {
			}
			return true;
		}
		return false;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		LogUtils.i("Info", "onPageStarted");
		if (SuperWebX5Config.WEBVIEW_TYPE == SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE && mWebViewClientCallbackManager.getPageLifeCycleCallback() != null) {
			mWebViewClientCallbackManager.getPageLifeCycleCallback().onPageStarted(view, url, favicon);
		}
		super.onPageStarted(view, url, favicon);

	}


	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		LogUtils.i("Info", "onReceivedError：" + description + "  CODE:" + errorCode);
	}

	@Override
	public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
		super.onReceivedError(view, request, error);
		LogUtils.i("Info", "onReceivedError:" + error.toString());

	}

	@Override
	public void onPageFinished(WebView view, String url) {
		if (SuperWebX5Config.WEBVIEW_TYPE == SuperWebX5Config.WEBVIEW_SUPERWEB_SAFE_TYPE && mWebViewClientCallbackManager.getPageLifeCycleCallback() != null) {
			mWebViewClientCallbackManager.getPageLifeCycleCallback().onPageFinished(view, url);
		}
		super.onPageFinished(view, url);

		LogUtils.i("Info", "onPageFinished");
	}


	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
		LogUtils.i("Info", "shouldOverrideKeyEvent");
		return super.shouldOverrideKeyEvent(view, event);
	}


	private void startActivity(String url) {


		try {

			if (mWeakReference.get() == null)
				return;

			LogUtils.i("Info", "start wechat pay Activity");
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			mWeakReference.get().startActivity(intent);

		} catch (Exception e) {
			LogUtils.i("Info", "支付异常");
			e.printStackTrace();
		}


	}

	@Override
	public void onScaleChanged(WebView view, float oldScale, float newScale) {


		if (SuperWebX5Utils.isOverriedMethod(mWebViewClient, "onScaleChanged", WEBVIEWCLIENTPATH + ".onScaleChanged", WebView.class, float.class, float.class)) {
			super.onScaleChanged(view, oldScale, newScale);
			return;
		}

		LogUtils.i("Info", "onScaleChanged:" + oldScale + "   n:" + newScale);
		if (newScale - oldScale > CONSTANTS_ABNORMAL_BIG) {
			view.setInitialScale((int) (oldScale / newScale * 100));
		}

	}

	public static Builder createBuilder() {
		return new Builder();
	}

	public static enum OpenOtherPageWays {
		DERECT(DefaultWebClient.DERECT_OPEN_OTHER_APP), ASK(DefaultWebClient.ASK_USER_OPEN_OTHER_APP), DISALLOW(DefaultWebClient.DISALLOW_OPEN_OTHER_APP);
		public int code;

		OpenOtherPageWays(int code) {
			this.code = code;
		}
	}


	public static class Builder {

		private Activity activity;
		private WebViewClient client;
		private WebViewClientCallbackManager manager;
		private boolean webClientHelper;
		private PermissionInterceptor permissionInterceptor;
		private WebView webView;
		private boolean isInterceptUnkownScheme;
		private int schemeHandleType;
		private DefaultMsgConfig.WebViewClientMsgCfg mCfg;

		public Builder setCfg(DefaultMsgConfig.WebViewClientMsgCfg cfg) {
			mCfg = cfg;
			return this;
		}

		public Builder setActivity(Activity activity) {
			this.activity = activity;
			return this;
		}

		public Builder setClient(WebViewClient client) {
			this.client = client;
			return this;
		}

		public Builder setManager(WebViewClientCallbackManager manager) {
			this.manager = manager;
			return this;
		}

		public Builder setWebClientHelper(boolean webClientHelper) {
			this.webClientHelper = webClientHelper;
			return this;
		}

		public Builder setPermissionInterceptor(PermissionInterceptor permissionInterceptor) {
			this.permissionInterceptor = permissionInterceptor;
			return this;
		}

		public Builder setWebView(WebView webView) {
			this.webView = webView;
			return this;
		}

		public Builder setInterceptUnkownScheme(boolean interceptUnkownScheme) {
			this.isInterceptUnkownScheme = interceptUnkownScheme;
			return this;
		}

		public Builder setSchemeHandleType(int schemeHandleType) {
			this.schemeHandleType = schemeHandleType;
			return this;
		}

		public DefaultWebClient build() {
			return new DefaultWebClient(this);
		}
	}


}
