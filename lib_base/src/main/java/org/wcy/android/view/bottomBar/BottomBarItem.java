package org.wcy.android.view.bottomBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.wcy.android.R;
import org.wcy.android.utils.ForPxTp;

import java.util.Locale;


/**
 * @author wcy
 * @description: 底部tab条目
 * @date 2018/3/19
 */

public class BottomBarItem extends LinearLayout {

    private Context mContext;
    private int mIconNormalResourceId;//普通状态图标的资源id
    private int mIconSelectedResourceId;//选中状态图标的资源id
    private String mText;//文本
    private int mTextSize = 12;//文字大小 默认为12sp
    private int mTextColorNormal = 0xFF999999;    //描述文本的默认显示颜色
    private int mTextColorSelected = 0xFF46C01B;  //述文本的默认选中显示颜色
    private int msgTextColor = Color.WHITE;//提示信息文字顔色
    private int msgBg = Color.RED;
    private int mMarginTop = 0;//文字和图标的距离,默认0dp
    private boolean mOpenTouchBg = false;// 是否开启触摸背景，默认关闭
    private Drawable mTouchDrawable;//触摸时的背景
    private int mIconWidth;//图标的宽度
    private int mIconHeight;//图标的高度
    private int mItemPadding;//BottomBarItem的padding
    private int mNotifyWidth;

    private ImageView mImageView;
    private TextView mTvUnread;
    private TextView mTvNotify;
    private TextView mTvMsg;
    private TextView mTextView;

    private int mUnreadTextSize = 10; //未读数默认字体大小10sp
    private int mMsgTextSize = 8; //消息默认字体大小8sp
    private int unreadNumThreshold = 99;

    public BottomBarItem(Context context) {
        this(context, null);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarItem);

        mIconNormalResourceId = ta.getResourceId(R.styleable.BottomBarItem_iconNormal, -1);
        mIconSelectedResourceId = ta.getResourceId(R.styleable.BottomBarItem_iconSelected, -1);

        mText = ta.getString(R.styleable.BottomBarItem_itemText);
        mTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemTextSize, ForPxTp.sp2px(mContext, mTextSize));

        mTextColorNormal = ta.getColor(R.styleable.BottomBarItem_textColorNormal, mTextColorNormal);
        mTextColorSelected = ta.getColor(R.styleable.BottomBarItem_textColorSelected, mTextColorSelected);
        msgTextColor = ta.getColor(R.styleable.BottomBarItem_msgTextColor, msgTextColor);
        msgBg = ta.getColor(R.styleable.BottomBarItem_msgBg, msgBg);
        mMarginTop = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemMarginTop, ForPxTp.dip2px(mContext, mMarginTop));
        mOpenTouchBg = ta.getBoolean(R.styleable.BottomBarItem_openTouchBg, mOpenTouchBg);
        mTouchDrawable = ta.getDrawable(R.styleable.BottomBarItem_touchDrawable);
        mIconWidth = ta.getDimensionPixelSize(R.styleable.BottomBarItem_iconWidth, 0);
        mIconHeight = ta.getDimensionPixelSize(R.styleable.BottomBarItem_iconHeight, 0);
        mItemPadding = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemPadding, 0);
        mNotifyWidth = ta.getDimensionPixelOffset(R.styleable.BottomBarItem_notifyWidth, ForPxTp.dip2px(mContext, 8));
        mUnreadTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_unreadTextSize, ForPxTp.sp2px(mContext, mUnreadTextSize));
        mMsgTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_msgTextSize, ForPxTp.sp2px(mContext, mMsgTextSize));
        unreadNumThreshold = ta.getInteger(R.styleable.BottomBarItem_unreadThreshold, 99);
        ta.recycle();

        checkValues();
        init();
    }

    /**
     * 检查传入的值是否完善
     */
    private void checkValues() {
        if (mIconNormalResourceId == -1) {
            throw new IllegalStateException("您还没有设置默认状态下的图标，请指定iconNormal的图标");
        }

        if (mIconSelectedResourceId == -1) {
            throw new IllegalStateException("您还没有设置选中状态下的图标，请指定iconSelected的图标");
        }

        if (mOpenTouchBg && mTouchDrawable == null) {
            //如果有开启触摸背景效果但是没有传对应的drawable
            throw new IllegalStateException("开启了触摸效果，但是没有指定touchDrawable");
        }
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View view = View.inflate(mContext, R.layout.item_bottom_bar, null);
        if (mItemPadding != 0) {
            //如果有设置item的padding
            view.setPadding(mItemPadding, mItemPadding, mItemPadding, mItemPadding);
        }
        mImageView = view.findViewById(R.id.iv_icon);
        mTvUnread = view.findViewById(R.id.tv_unred_num);
        mTvMsg = view.findViewById(R.id.tv_msg);
        mTvNotify = view.findViewById(R.id.tv_point);
        mTextView = view.findViewById(R.id.tv_text);

        mImageView.setImageResource(mIconNormalResourceId);

        if (mIconWidth != 0 && mIconHeight != 0) {
            //如果有设置图标的宽度和高度，则设置ImageView的宽高
            FrameLayout.LayoutParams imageLayoutParams = (FrameLayout.LayoutParams) mImageView.getLayoutParams();
            imageLayoutParams.width = mIconWidth;
            imageLayoutParams.height = mIconHeight;
            mImageView.setLayoutParams(imageLayoutParams);
        }
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(msgBg);
        gradientDrawable.setSize(mNotifyWidth, mNotifyWidth);
        ViewGroup.LayoutParams layoutParams = mTvNotify.getLayoutParams();
        layoutParams.width = mNotifyWidth;
        layoutParams.height = mNotifyWidth;
        mTvNotify.setLayoutParams(layoutParams);
        mTvNotify.setBackground(gradientDrawable);
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable2.setColor(msgBg);
        gradientDrawable2.setCornerRadius(20);
        mTvUnread.setBackground(gradientDrawable2);
        mTvMsg.setBackground(gradientDrawable2);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);//设置底部文字字体大小
        mTvUnread.setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnreadTextSize);//设置未读数的字体大小
        mTvMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX, mMsgTextSize);//设置提示文字的字体大小
        mTvMsg.setTextColor(msgTextColor);
        mTvUnread.setTextColor(msgTextColor);
        mTextView.setTextColor(mTextColorNormal);//设置底部文字字体颜色
        mTextView.setText(mText);//设置标签文字

        LayoutParams textLayoutParams = (LayoutParams) mTextView.getLayoutParams();
        textLayoutParams.topMargin = mMarginTop;
        mTextView.setLayoutParams(textLayoutParams);

        if (mOpenTouchBg) {
            //如果有开启触摸背景
            setBackground(mTouchDrawable);
        }

        addView(view);
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setIconNormalResourceId(int mIconNormalResourceId) {
        this.mIconNormalResourceId = mIconNormalResourceId;
    }

    public void setIconSelectedResourceId(int mIconSelectedResourceId) {
        this.mIconSelectedResourceId = mIconSelectedResourceId;
    }

    public void setStatus(boolean isSelected) {
        mImageView.setImageDrawable(getResources().getDrawable(isSelected ? mIconSelectedResourceId : mIconNormalResourceId));
        mTextView.setTextColor(isSelected ? mTextColorSelected : mTextColorNormal);
    }

    private void setTvVisiable(TextView tv) {
        //都设置为不可见
        mTvUnread.setVisibility(GONE);
        mTvMsg.setVisibility(GONE);
        mTvNotify.setVisibility(GONE);

        tv.setVisibility(VISIBLE);//设置为可见
    }

    public int getUnreadNumThreshold() {
        return unreadNumThreshold;
    }

    public void setUnreadNumThreshold(int unreadNumThreshold) {
        this.unreadNumThreshold = unreadNumThreshold;
    }

    /**
     * 设置未读数
     *
     * @param unreadNum 小于等于则隐藏，
     *                  大于0小于则显示对应数字，
     *                  超过
     *                  显示
     */
    public void setUnreadNum(int unreadNum) {
        setTvVisiable(mTvUnread);
        if (unreadNum <= 0) {
            mTvUnread.setVisibility(GONE);
        } else if (unreadNum <= unreadNumThreshold) {
            mTvUnread.setText(String.valueOf(unreadNum));
        } else {
            mTvUnread.setText(String.format(Locale.CHINA, "%d+", unreadNumThreshold));
        }
    }

    public void setMsg(String msg) {
        setTvVisiable(mTvMsg);
        mTvMsg.setText(msg);
    }

    public void hideMsg() {
        mTvMsg.setVisibility(GONE);
    }

    public void showNotify() {
        setTvVisiable(mTvNotify);
    }

    public void hideNotify() {
        mTvNotify.setVisibility(GONE);
    }
}