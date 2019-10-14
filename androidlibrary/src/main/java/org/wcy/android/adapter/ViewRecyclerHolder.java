package org.wcy.android.adapter;

import android.graphics.Bitmap;
import android.text.SpannableString;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 通用ViewHolder
 *
 * @author wcy
 * @date 2016年6月13日
 */
public class ViewRecyclerHolder extends RecyclerView.ViewHolder {
    private View mConvertView;
    private int itemCount;
    public ViewRecyclerHolder(View view) {
        super(view);
        mConvertView = view;
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     */
    public ViewRecyclerHolder get() {
        return this;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mConvertView.findViewById(viewId);
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewRecyclerHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewRecyclerHolder setText(int viewId, SpannableString text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public ViewRecyclerHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }
    
    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public ViewRecyclerHolder setChecked(int viewId, boolean b) {
        CompoundButton view = getView(viewId);
        view.setChecked(b);
        return this;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
