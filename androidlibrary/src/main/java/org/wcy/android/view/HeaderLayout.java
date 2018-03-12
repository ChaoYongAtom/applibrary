package org.wcy.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wcy.android.R;

/**
 * 实现头部布局
 * Created by wcy
 */
public class HeaderLayout extends RelativeLayout {

    private int mSpitLineColor;
    private float mSpitLineHeight;

    private TextView mTitleTv;

    private int mHedaderLayoutHeight = 0;

    private int mTitleTextColor;
    private float mTitleTextSize;
    private boolean mTitleAlignLeft;

    private TextView mNavigationView;
    private float mNavigationWidth;
    private float mNavigationMinWidth;
    private float mNavigationMaxWidth;
    private float mNavigationHeight;
    private float mNavigationMinHeight;
    private float mNavigationMaxHeight;

    private LinearLayout mMenuLl;  //用于存储右边按钮
    private int mItemTextColor;
    private float mItemTextSize;
    private float mItemTextPaddingLeftAndRight;
    private int mItemMarginLeftAndRight;

    private enum MenuAlign {

        /**
         * 文字在最右边
         */
        ALIGN_TEXT(0),

        /**
         * 图标在最右边
         */
        ALIGN_ICON(1);

        int type;

        MenuAlign(int type) {
            this.type = type;
        }

        public static MenuAlign getType(int type) {
            for (MenuAlign align : values()) {
                if (type == align.type) {
                    return align;
                }
            }

            return ALIGN_TEXT;
        }
    }

    public HeaderLayout(Context context) {
        this(context, null);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    int menuAlignType = -1;

    private void init(Context context, AttributeSet attrs) {
        String titleText = "";
        int navigationIcon = 0;
        String navigationText = "";
        int menuIcon = 0, menu2Icon = 0;  //右边的按钮
        String menuText = "", menu2Text = "";  //右边的文字按钮
        int menuTextId = 0, menu2TextId = 0;  //右边的文字按钮
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderLayout);
            //标题相关配置
            titleText = a.getString(R.styleable.HeaderLayout_hlTitleText);
            mTitleTextColor = a.getColor(R.styleable.HeaderLayout_hlTitleTextColor, getResources().getColor(R.color.default_header_layout_title_textColor));
            mTitleTextSize = a.getDimension(R.styleable.HeaderLayout_hlTitleTextSize, getResources().getDimension(R.dimen.default_header_layout_title_textSize));
            mTitleAlignLeft = a.getBoolean(R.styleable.HeaderLayout_hlTitleAlignLeft, false);
            menu2TextId = a.getResourceId(R.styleable.HeaderLayout_hlMenu2TextId, +0xa25);

            //文字按钮相关配置
            mItemTextColor = a.getColor(R.styleable.HeaderLayout_hlItemTextColor, getResources().getColor(R.color.default_header_layout_title_textColor));
            mItemTextSize = a.getDimension(R.styleable.HeaderLayout_hlItemTextSize, getResources().getDimension(R.dimen.default_header_layout_menu_textSize));
            mItemTextPaddingLeftAndRight = a.getDimension(R.styleable.HeaderLayout_hlItemTextPaddingLeftAndRight, getResources().getDimension(R.dimen.default_header_layout_menu_textSize) / 2);  //大概半个字的间距
            mItemMarginLeftAndRight = (int) a.getDimension(R.styleable.HeaderLayout_hlItemMarginLeftAndRight, getResources().getDimension(R.dimen.default_header_layout_menu_textSize) / 2);  //大概半个字的间距
            navigationIcon = a.getResourceId(R.styleable.HeaderLayout_hlNavigationIcon, 0);
            navigationText = a.getString(R.styleable.HeaderLayout_hlNavigationText);
            mNavigationWidth = a.getDimension(R.styleable.HeaderLayout_hlNavigationWidth, 0);
            mNavigationMinWidth = a.getDimension(R.styleable.HeaderLayout_hlNavigationMinWidth, 0);
            mNavigationMaxWidth = a.getDimension(R.styleable.HeaderLayout_hlNavigationMaxWidth, 0);
            mNavigationHeight = a.getDimension(R.styleable.HeaderLayout_hlNavigationHeight, 0);
            mNavigationMinHeight = a.getDimension(R.styleable.HeaderLayout_hlNavigationMinHeight, 0);
            mNavigationMaxHeight = a.getDimension(R.styleable.HeaderLayout_hlNavigationMaxHeight, 0);
            menuIcon = a.getResourceId(R.styleable.HeaderLayout_hlMenuIcon, 0);
            menu2Icon = a.getResourceId(R.styleable.HeaderLayout_hlMenu2Icon, 0);
            menuText = a.getString(R.styleable.HeaderLayout_hlMenuText);
            menuTextId = a.getResourceId(R.styleable.HeaderLayout_hlMenuTextId, +0xa24);
            menu2Text = a.getString(R.styleable.HeaderLayout_hlMenu2Text);
            menuAlignType = a.getInt(R.styleable.HeaderLayout_hlMenuAlign, -1);
            mSpitLineColor = a.getResourceId(R.styleable.HeaderLayout_hlSpitLineColor, 0);
            mSpitLineHeight = a.getDimension(R.styleable.HeaderLayout_hlSpitLineHeight, 2);
            a.recycle();
        }
        LayoutParams params;
        //左边按钮
        if (!TextUtils.isEmpty(navigationText) || navigationIcon != 0) {
            mNavigationView = new TextView(getContext());
            if (navigationIcon > 0) {
                mNavigationView.setCompoundDrawablesRelativeWithIntrinsicBounds(navigationIcon, 0, 0, 0);
            }
            mNavigationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            addView(mNavigationView);
            mNavigationView.setId(R.id.hl_navigation_view);
            addButtonConfig(mNavigationView, navigationText, mItemTextSize, mItemTextColor, (int) mItemTextPaddingLeftAndRight);
        }
        if (menuIcon != 0 || !TextUtils.isEmpty(menuText)) {  //说明有右边按钮
            mMenuLl = new LinearLayout(getContext());
            mMenuLl.setOrientation(LinearLayout.HORIZONTAL);
            addView(mMenuLl);
            params = (LayoutParams) mMenuLl.getLayoutParams();
            params.width = LayoutParams.WRAP_CONTENT;
            params.height = LayoutParams.MATCH_PARENT;
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(mItemMarginLeftAndRight, 0, mItemMarginLeftAndRight, 0);
            mMenuLl.setLayoutParams(params);
            createMenuTextButton(menuText, menuIcon, menuTextId);
            if (menu2Icon != 0 || !TextUtils.isEmpty(menu2Text)) {
                createMenuTextButton(menu2Text, menu2Icon, menu2TextId);
            }
        }

        //标题栏
        mTitleTv = new TextView(getContext());
        mTitleTv.setMaxLines(1);
        addView(mTitleTv);
        mTitleTv.setGravity(Gravity.CENTER);  //实现文字居中效果  在setSupportTranslucentStatus中设置高度和HeaderLayout一样就好了
        params = (LayoutParams) mTitleTv.getLayoutParams();
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.MATCH_PARENT;

        if (!mTitleAlignLeft) {
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else {
            if (mNavigationView != null) {
                params.addRule(RelativeLayout.RIGHT_OF, mNavigationView.getId());  //基于左边按钮的显示
            } else {
                params.leftMargin = (int) (mTitleTextSize / 2);  //基于左边的显示
                params.addRule(RelativeLayout.ALIGN_LEFT);
            }
        }
        mTitleTv.setLayoutParams(params);

        mTitleTv.setText(titleText);
        mTitleTv.setTextColor(mTitleTextColor);
        mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);

        //底部的分割线
        if (mSpitLineColor != 0) {
            ImageView splitLineIv = new ImageView(getContext());
            addView(splitLineIv);
            params = (LayoutParams) splitLineIv.getLayoutParams();
            params.width = LayoutParams.MATCH_PARENT;
            params.height = (int) mSpitLineHeight;
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            splitLineIv.setLayoutParams(params);

            splitLineIv.setImageResource(mSpitLineColor);
        }
    }

    private void createMenuTextButton(String menuText, int menuIcon, int menuTextId) {
        TextView tv = new TextView(getContext());
        if (menuTextId != 0) {
            tv.setId(menuTextId);
        }
        if (menuIcon > 0) {
            switch (MenuAlign.getType(menuAlignType)) {
                case ALIGN_TEXT:
                    tv.setCompoundDrawablesRelativeWithIntrinsicBounds(menuIcon, 0, 0, 0);
                    break;
                case ALIGN_ICON:
                    tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, menuIcon, 0);
                    break;
            }
        }
        mMenuLl.addView(tv);
        addButtonConfig(tv, menuText, mItemTextSize, mItemTextColor, (int) mItemTextPaddingLeftAndRight);
    }

    private View addButtonConfig(TextView view, String text, float textSize, int textColor, int padding) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params instanceof LayoutParams) {  //createMenuIconButton传入的是LinearLayout.LayoutParams
            ((LayoutParams) params).setMargins(mItemMarginLeftAndRight, 0, mItemMarginLeftAndRight, 0);
        }
        if (mNavigationWidth > 0) {  //设置宽高属性
            params.width = (int) mNavigationWidth;
        } else {
            params.width = LayoutParams.WRAP_CONTENT;

            //设置Min Max宽属性
            if (mNavigationMinWidth > 0) {
                view.setMinimumWidth((int) mNavigationMinWidth);
            }
            if (mNavigationMaxWidth > 0) {
                view.setMaxWidth((int) mNavigationMaxWidth);
            }
        }
        if (mNavigationHeight > 0) {
            params.height = (int) mNavigationHeight;
        } else {
            params.height = LayoutParams.MATCH_PARENT;

            //设置Min Max高属性
            if (mNavigationMinHeight > 0) {
                view.setMinimumHeight((int) mNavigationMinHeight);
            }
            if (mNavigationMaxHeight > 0) {
                view.setMaxHeight((int) mNavigationMaxHeight);
            }
        }

        view.setLayoutParams(params);
        view.setCompoundDrawablePadding((int) mItemTextPaddingLeftAndRight);
        view.setText(text);
        view.setTextColor(textColor);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        view.setPadding(padding, 0, padding, 0);
        view.setGravity(Gravity.CENTER);

        return view;
    }

    /**
     * 获取标题View控件
     *
     * @return title 标题
     */
    public TextView getTitleView() {
        return mTitleTv;
    }

    /**
     * 获取标题
     *
     * @param title 标题
     */
    public void setTitleText(String title) {
        if (mTitleTv != null) {
            mTitleTv.setText(title);
        }
    }

    /**
     * 获取导航栏左边按钮
     *
     * @return title 标题
     */
    public TextView getNavigationView() {
        return mNavigationView;
    }

    public TextView getmTitleView() {
        return mTitleTv;
    }

    /**
     * 获取右边第一个按钮
     *
     * @return
     */
    public TextView getMenuView() {
        if (mMenuLl.getChildCount() >= 0) {
            return (TextView) mMenuLl.getChildAt(0);
        }
        return null;
    }

    /**
     * 获取右边第二个按钮
     *
     * @return
     */
    public TextView getMenu2View() {
        if (mMenuLl.getChildCount() > 0) {
            return (TextView) mMenuLl.getChildAt(1);
        }
        return null;
    }

    /***
     * Navigation的左边按钮点击事件
     * @param l 点击监听
     */
    public void setClickListener(OnClickListener l) {  //处理返回键点击事件
        if (mNavigationView != null) {
            mNavigationView.setOnClickListener(l);
        }
        if (mMenuLl.getChildCount() > 0) {
            for (int i = 0; i < mMenuLl.getChildCount(); i++) {
                View view = mMenuLl.getChildAt(i);
                view.setOnClickListener(l);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHedaderLayoutHeight == 0) {
            mHedaderLayoutHeight = getLayoutParams().height;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}