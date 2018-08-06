package org.wcy.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wcy.android.R;
import org.wcy.android.utils.DividerGridItemDecoration;
import org.wcy.android.utils.RxDataTool;
import org.wcy.android.view.refresh.MaterialRefreshLayout;
import org.wcy.android.view.refresh.MaterialRefreshListener;

/**
 * 統一友好加載頁面提示
 * wcy
 */
public class EmptyLayout extends RelativeLayout {
    View childView;
    private Context mContext;
    private ViewGroup mLoadingView;
    private ViewGroup mEmptyView;
    private ViewGroup mErrorView;
    private ViewGroup mNetView;
    private RecyclerView listView;
    private boolean mViewsAdded;
    boolean isRefresh = false;//是否支持刷新
    boolean isList = false;//是否是列表
    int refreshBackgroundColor;//刷新页面背景
    int listBackgroundColor;//列表背景
    int listHeight;//列表高度
    int dividerLineColor;//分割线颜色
    int dividerLineWidth;//分割线高度
    private OnClickListener mErrorClickListener;
    ViewGroup mainview;
    private String message;
    private RecyclerView.Adapter adapter;
    private int view_empty_layout = R.layout.view_empty;
    private int view_loading_layout = R.layout.view_loading;
    private int view_error_layout = R.layout.view_error;
    private int view_notnet_layout = R.layout.view_notnet;

    public EmptyLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public EmptyLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EmptyLayout);
            isRefresh = a.getBoolean(R.styleable.EmptyLayout_isRefresh, false);
            isList = a.getBoolean(R.styleable.EmptyLayout_isList, false);
            refreshBackgroundColor = a.getColor(R.styleable.EmptyLayout_refreshBackgroundColor, Color.TRANSPARENT);
            listBackgroundColor = a.getColor(R.styleable.EmptyLayout_listBackgroundColor, Color.TRANSPARENT);
            listHeight = a.getInt(R.styleable.EmptyLayout_listHeight, LayoutParams.WRAP_CONTENT);
            dividerLineColor = a.getColor(R.styleable.EmptyLayout_dividerLineColor, Color.TRANSPARENT);
            dividerLineWidth = a.getInt(R.styleable.EmptyLayout_dividerLineWidth, -1);
        }

    }

    MaterialRefreshLayout pullToRefreshView;

    /**
     * 加载完成xml在添加顶部view保证他在最地步
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mainview = (ViewGroup) getRootView();
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (isRefresh && !isList) {//需要刷新但是不是列表,内部需要childviewq且只有一个
            if (getChildCount() == 1) {
                View viewGroup = getChildAt(0);
                viewGroup.setFocusable(true);
                viewGroup.setClickable(true);
                mainview.removeAllViews();
                pullToRefreshView = new MaterialRefreshLayout(mContext);
                pullToRefreshView.addView(viewGroup);
                pullToRefreshView.setIsOverLay(true);
                pullToRefreshView.setWaveColor(getResources().getColor(R.color.waveColor));
                mainview.addView(pullToRefreshView, lp);
            }
        } else if (isRefresh && isList) {//同时需要刷新而且是列表并且内部无childview
            pullToRefreshView = new MaterialRefreshLayout(mContext);
            pullToRefreshView.setIsOverLay(true);
            pullToRefreshView.setWaveColor(getResources().getColor(R.color.waveColor));
            initListView(null);
            pullToRefreshView.addView(listView);
            mainview.addView(pullToRefreshView, lp);
        } else if (!isRefresh && isList) {//同时需要刷新而且是列表并且内部无childview
            initListView(null);
            mainview.addView(listView);
        }
        childView = getChildAt(0);
    }

    private void initListView(LayoutParams lp) {
        listView = new RecyclerView(mContext);
        listView.setBackgroundColor(listBackgroundColor);
        if (dividerLineWidth > 0) {
            listView.addItemDecoration(new DividerGridItemDecoration(mContext, LinearLayoutManager.HORIZONTAL, 0, dividerLineWidth, dividerLineColor, false));
        }
        if (lp == null) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        listView.setLayoutParams(lp);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        listView.setLayoutManager(mLayoutManager);
        listView.setHasFixedSize(true);
    }


    /**
     * The empty state
     */
    private final int TYPE_EMPTY = 1;
    /**
     * The loading state
     */
    private final int TYPE_LOADING = 2;
    /**
     * The error state
     */
    private final int TYPE_ERROR = 3;
    private final int TYPE_VIEW = 4;
    private final int TYPE_NET = 5;
    private int mEmptyType = TYPE_LOADING;

    /**
     * Sets the OnClickListener to ErrorView
     * <p>
     * OnClickListener Object
     */
    public void setErrorClickListener(OnClickListener errorClickListener) {
        this.mErrorClickListener = errorClickListener;
    }

    // ---------------------------
    // private methods
    // ---------------------------

    private void changeEmptyType() {
        setDefaultValues();
        // insert views in the root view
        if (!mViewsAdded && childView != null) {
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            if (mEmptyView != null) mainview.addView(mEmptyView, lp);
            if (mLoadingView != null) {
                LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                lp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
                lp1.addRule(RelativeLayout.CENTER_VERTICAL);
                mainview.addView(mLoadingView, lp1);
            }

            if (mErrorView != null) mainview.addView(mErrorView, lp);
            if (mNetView != null) {
                mainview.addView(mNetView, lp);
            }
            mViewsAdded = true;
        }
        if (childView != null) {
            TextView message_tv, textretry = null;
            switch (mEmptyType) {
                case TYPE_EMPTY:
                    if (childView != null) childView.setVisibility(View.GONE);
                    if (mNetView != null) mNetView.setVisibility(View.GONE);
                    if (mEmptyView != null) {
                        mEmptyView.setVisibility(View.VISIBLE);
                        message_tv = mEmptyView.findViewById(R.id.textViewMessage);
                        textretry = mEmptyView.findViewById(R.id.textretry);
                        if (message != null && !message.equals("")) {
                            message_tv.setText(message);
                        } else {
                            message_tv.setText(mContext.getString(R.string.empty_message));
                        }
                    }
                    if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                    if (mLoadingView != null) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                    break;
                case TYPE_ERROR:
                    if (childView != null) childView.setVisibility(View.GONE);
                    if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                    if (mNetView != null) mNetView.setVisibility(View.GONE);
                    if (mErrorView != null) {
                        mErrorView.setVisibility(View.VISIBLE);
                        message_tv = mErrorView.findViewById(R.id.textViewMessage);
                        textretry = mErrorView.findViewById(R.id.textretry);
                        if (message != null && !message.equals("")) {
                            message_tv.setText(message);
                        } else {
                            message_tv.setText(mContext.getString(R.string.error_message));
                        }
                    }
                    if (mLoadingView != null) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                    break;
                case TYPE_LOADING:
                    if (childView != null) childView.setVisibility(View.GONE);
                    if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                    if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                    if (mNetView != null) mNetView.setVisibility(View.GONE);
                    if (mLoadingView != null) {
                        mLoadingView.setVisibility(View.VISIBLE);
                        message_tv = mLoadingView.findViewById(R.id.textViewMessage);
                        if (message != null && !message.equals("")) {
                            message_tv.setText(message);
                        } else {
                            message_tv.setText(mContext.getString(R.string.loading_message));
                        }
                    }
                    break;
                case TYPE_VIEW:
                    if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                    if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                    if (mNetView != null) mNetView.setVisibility(View.GONE);
                    if (mLoadingView != null) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                    if (childView != null) childView.setVisibility(View.VISIBLE);
                    break;
                case TYPE_NET:
                    if (childView != null) childView.setVisibility(View.GONE);
                    if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                    if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                    if (mNetView != null) {
                        textretry = mNetView.findViewById(R.id.textretry);
                        mNetView.setVisibility(View.VISIBLE);
                    }
                    if (mLoadingView != null) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
            if (textretry != null) {
                if (mErrorClickListener != null) {
                    textretry.setVisibility(View.VISIBLE);
                } else {
                    textretry.setVisibility(GONE);
                }
            }
        }


    }

    private void setDefaultValues() {
        if (mEmptyView == null) {
            mEmptyView = (ViewGroup) View.inflate(mContext, view_empty_layout, null);
            mEmptyView.setFocusable(true);
            mEmptyView.setClickable(true);
            if (mErrorClickListener != null) {
                mEmptyView.setOnClickListener(mErrorClickListener);
            }
        }
        if (mLoadingView == null) {
            mLoadingView = (ViewGroup) View.inflate(mContext, view_loading_layout, null);
        }
        if (mErrorView == null) {
            mErrorView = (ViewGroup) View.inflate(mContext, view_error_layout, null);
            if (mErrorClickListener != null) {
                mErrorView.setFocusable(true);
                mErrorView.setClickable(true);
                mErrorView.setOnClickListener(mErrorClickListener);
            }
        }
        if (mNetView == null) {
            mNetView = (ViewGroup) View.inflate(mContext, view_notnet_layout, null);
            if (mErrorClickListener != null) {
                mNetView.setFocusable(true);
                mNetView.setClickable(true);
                mNetView.setOnClickListener(mErrorClickListener);
            }
        }
    }


    /**
     * Shows the empty layout if the list is empty
     */
    public void showEmpty(String message) {
        this.message = message;
        this.mEmptyType = TYPE_EMPTY;
        changeEmptyType();
    }

    public void showNet() {
        this.mEmptyType = TYPE_NET;
        changeEmptyType();
    }

    /**
     * Shows the empty layout if the list is empty
     */
    public void showEmpty() {
        showEmpty(null);
    }

    public void showView(String message) {
        this.message = message;
        this.mEmptyType = TYPE_VIEW;
        changeEmptyType();
    }

    public void showView() {
        showView(null);
    }

    /**
     * Shows loading layout if the list is empty
     */
    public void showLoading(String message) {
        this.message = message;
        this.mEmptyType = TYPE_LOADING;
        changeEmptyType();
    }

    public void showLoading() {
        showLoading(null);
    }

    /**
     * 隐藏加载提示加载成功
     */
    public void onRefreshComplete() {
        if (pullToRefreshView != null) {
            pullToRefreshView.finishRefresh();
            pullToRefreshView.finishRefreshLoadMore();
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        if (listView != null) listView.setAdapter(adapter);
    }

    /**
     * set headerRefreshListener
     *
     * @param materialRefreshListener
     * @description
     */
    public void setOnRefreshListener(MaterialRefreshListener materialRefreshListener) {
        if (pullToRefreshView != null) {
            pullToRefreshView.setMaterialRefreshListener(materialRefreshListener);
        }
    }

    /**
     * Shows error layout if the list is empty
     */
    public void showError(String message) {
        if (!RxDataTool.isNullString(message)) {
            this.message = message;
        } else {
            this.message = "暂无数据";
        }
        this.mEmptyType = TYPE_ERROR;
        changeEmptyType();
    }

    public void showError() {
        showError(null);
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void notifyDataSetChanged() {
        if (adapter != null) adapter.notifyDataSetChanged();
        showView();
    }

    public void setMode(int mode) {
        if (pullToRefreshView != null) pullToRefreshView.setMore(mode);
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public boolean isList() {
        return isList;
    }

    public RecyclerView getListView() {
        return listView;
    }

    public void setView_empty_layout(int view_empty_layout) {
        this.view_empty_layout = view_empty_layout;
    }

    public void setView_loading_layout(int view_loading_layout) {
        this.view_loading_layout = view_loading_layout;
    }

    public void setView_error_layout(int view_error_layout) {
        this.view_error_layout = view_error_layout;
    }

    public void setView_notnet_layout(int view_notnet_layout) {
        this.view_notnet_layout = view_notnet_layout;
    }

    public MaterialRefreshLayout getPullToRefreshView() {
        return pullToRefreshView;
    }
}
