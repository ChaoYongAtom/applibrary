package org.wcy.android.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * OnTextWatcher
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/2/12
 * @description YjSales
 */
public class OnTextWatcher implements TextWatcher {
    View mView;
    EditText[] editTexts;

    public OnTextWatcher(View mView, EditText... editTexts) {
        this.mView = mView;
        this.editTexts = editTexts;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean isEnabled = true;
        for (EditText et : editTexts) {
            if (RxDataTool.isNullString(et.getText().toString())) {
                isEnabled = false;
                break;
            }
        }
        mView.setEnabled(isEnabled);

    }

    public static void addTextChangedListener(View mView, EditText... editTexts) {
        for (EditText et : editTexts) {
            et.addTextChangedListener(new OnTextWatcher(mView, editTexts));
        }
    }
}
