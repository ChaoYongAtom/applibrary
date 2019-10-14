package org.wcy.android.utils;

/**
 * Created by wcy on 2016/6/1.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 设置分割线
 *
 * @author wcy
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private Paint mPaint;
    private int mDividerHeight = 1;//分割线高度，默认为1px
    private int mDividerWidth = 1;
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL

    private boolean isShowLastLine = true;

    /**
     * @param context
     * @param orientation   方向
     * @param dividerWidth  横向分割线宽度
     * @param dividerHeight 纵向分割线高度
     * @param dividerColor  分割线颜色
     */
    public DividerGridItemDecoration(Context context, int orientation, int dividerWidth, int dividerHeight, int dividerColor) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mOrientation = orientation;
        mDivider = a.getDrawable(0);
        mDividerHeight = dividerHeight;
        this.mDividerWidth = dividerWidth;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
        a.recycle();
    }


    /**
     * @param context
     * @param orientation   方向
     * @param dividerWidth  横向分割线宽度
     * @param dividerHeight 纵向分割线高度
     * @param dividerColor  分割线颜色
     * @param isShowLastLine 是否显示最后分割线
     */
    public DividerGridItemDecoration(Context context, int orientation, int dividerWidth, int dividerHeight, int dividerColor, boolean isShowLastLine) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mOrientation = orientation;
        mDivider = a.getDrawable(0);
        mDividerHeight = dividerHeight;
        this.mDividerWidth = dividerWidth;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
        this.isShowLastLine = isShowLastLine;
        a.recycle();
    }



    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizontal(c, parent);
        } else {
            drawHorizontal(c, parent);
            drawVertical(c, parent);
        }
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = 0;
        if (isShowLastLine){
            childCount = parent.getChildCount();
        }else {
            childCount = parent.getChildCount() - 1;
        }
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            if (mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {


        int childCount = 0;

        if (isShowLastLine){
            childCount = parent.getChildCount();
        }else {
            childCount = parent.getChildCount() - 1;
        }
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            if (mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
        {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.set(0, 0, mDividerWidth,
                        0);
            } else if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                outRect.set(0, 0, 0,
                        mDividerHeight);
            } else {
                outRect.set(0, 0, mDividerWidth,
                        mDividerHeight);
            }
        } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
        {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.set(0, 0, mDividerWidth,
                        0);
            } else if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                outRect.set(0, 0, 0,
                        mDividerHeight);
            } else {
                outRect.set(0, 0, mDividerWidth,
                        mDividerHeight);
            }
        } else {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.set(0, 0, mDividerWidth,
                        0);
            } else if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                outRect.set(0, 0, 0,
                        mDividerHeight);
            } else {
                outRect.set(0, 0, mDividerWidth,
                        mDividerHeight);
            }

        }
    }
}
