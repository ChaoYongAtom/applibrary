package org.wcy.android.keyboard;

/**
 * 用于接收键盘事件的回调
 *
 * @author Simon
 */
public interface IkeyBoardCallback {
    /**
     * 当键盘显示时回调
     */
    public void onKeyBoardShow(int keyBoardHeight);

    /**
     * 当键盘隐藏时回调
     */
    public void onKeyBoardHidden();
}
