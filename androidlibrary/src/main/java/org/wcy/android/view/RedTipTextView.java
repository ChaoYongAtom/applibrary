package org.wcy.android.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import org.wcy.android.R;

/**
 * textView上的小红点
 *
 * @author Visual
 */
public class RedTipTextView extends AppCompatTextView {
    private boolean tipVisibility = false;
    private int mTipColor;

    public RedTipTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(null);
    }

    public RedTipTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(attrs);
    }

    public RedTipTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.RedTipTextView);
            tipVisibility = array.getBoolean(R.styleable.RedTipTextView_redTipsVisibility, false);
            mTipColor = array.getColor(R.styleable.RedTipTextView_redTipsColorview, Color.RED);
            array.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (tipVisibility) {
            Layout layout = getLayout();
            Rect bound = new Rect();
            int line = layout.getLineForOffset(0);
            layout.getLineBounds(line, bound);
            Paint paint = new Paint();
            if (mTipColor <= 0) {
                mTipColor = Color.RED;
            }
            paint.setColor(mTipColor);
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStyle(Style.FILL);
            paint.setStrokeWidth(8);
            paint.setAntiAlias(true);
            canvas.drawCircle(bound.right + getPaddingLeft(), bound.bottom - 5, 8, paint);
        }
    }

    public void setTipVisibility(boolean visibility) {
        tipVisibility = visibility;
        invalidate();
    }
}
