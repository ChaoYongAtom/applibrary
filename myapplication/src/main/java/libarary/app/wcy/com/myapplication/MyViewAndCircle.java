package libarary.app.wcy.com.myapplication;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import org.wcy.android.R;

/**
 * 在一个比较坑的需求里，一段文字右上角需要追加一个圆形红点。最右侧有个金额，红点动态随着文字移动，然后各种摆布局，我去坑死我了。
 * 后来放弃了，就有了这个东西(⊙o⊙)…
 * 大神请加Q群，大家一起探讨：123869487
 *
 * @author 有点凉了
 */
public class MyViewAndCircle extends View {

    private String mText;//描述文字
    private int mTextSize;//描述文字大小

    private Rect rect;//控制边框 完整控件控制边框显示（宽高之类的）
    private Rect mTextBound;//控制文本范围

    private Paint mPaint;//控制画笔

    private int mWidth;//宽
    private int mHeight;//高
    private boolean isShow = true;

    RectF oval = null;//控制圆的边界

    public MyViewAndCircle(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public MyViewAndCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public MyViewAndCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomMyViewTitle, defStyleAttr, 0);
        mText = a.getString(R.styleable.CustomMyViewTitle_titleTextview);
        mTextSize = a.getDimensionPixelSize(R.styleable.CustomMyViewTitle_titleSizeview, 16);
        a.recycle();
        mPaint = new Paint();//这里做初始化
        rect = new Rect();
        mTextBound = new Rect();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //在这里测量当前控件的宽和高，具体的意思请看
        /**
         * 系统帮我们测量的高度和宽度都是MATCH_PARNET，当我们设置明确的宽度和高度时，系统帮我们测量的结果就是我们设置的结果，
         * 当我们设置为WRAP_CONTENT,或者MATCH_PARENT系统帮我们测量的结果就是MATCH_PARENT的长度。
         * 所以，当设置了WRAP_CONTENT时，我们需要自己进行测量，即重写onMesure方法”：
         * 重写之前先了解MeasureSpec的specMode,一共三种类型：
         * EXACTLY：一般是设置了明确的值或者是MATCH_PARENT;
         * AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT;
         * UNSPECIFIED：表示子布局想要多大就多大，很少使用;
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int spenSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = spenSize;
        }
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        spenSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = spenSize;
        } else {
            mPaint.setTextSize(16);
            mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
            float textHeight = mTextBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            mHeight = desired;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //这里就开始执行绘制了
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);//计算文字所需要的宽度

        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(mTextSize);
        rect.left = 0;
        rect.top = 0;
        rect.right = getMeasuredWidth();
        rect.bottom = getMeasuredHeight();
        canvas.drawRect(rect, mPaint);//这里在绘制最外侧布局的宽高
        mPaint.reset();

        //下面判断文本是否超出了父布局宽，然后分别作了设置
        if (mTextBound.width() > mWidth) {
//			文字超长展示
            mPaint.setTextSize(mTextSize);
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mText, paint, (float) mWidth - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight / 2 - getPaddingTop() + mTextBound.height() / 2, mPaint);
            mPaint.reset();
            if (isShow) {
                // 控制红色圆形大小
                mPaint.setAntiAlias(true);
                mPaint.setColor(Color.parseColor("#FE4D3D"));
                oval = new RectF();
                oval.left = getMeasuredWidth() - 30;
                oval.right = getMeasuredWidth();
                oval.top = getMeasuredHeight() / 2 - mTextBound.height() / 2 - 30;
                oval.bottom = getMeasuredHeight() / 2 - mTextBound.height() / 2;
                canvas.drawArc(oval, 0, 360, true, mPaint);
                mPaint.reset();
            }
        } else {
            //正常情况
            mPaint.setTextSize(mTextSize);
            canvas.drawText(mText, getPaddingLeft(), (mHeight / 2 - mTextBound.height() / 2) + mTextBound.height() - getPaddingBottom(), mPaint);
            mPaint.reset();
            if (isShow) {
                // 控制红色圆形大小
                mPaint.setAntiAlias(true);
                mPaint.setColor(Color.parseColor("#FE4D3D"));
                oval = new RectF();
                oval.left = mTextBound.width() + getPaddingRight();
                oval.right = mTextBound.width() + getPaddingRight() + 30;
                oval.top = getMeasuredHeight() / 2 - mTextBound.height() / 2 - 30;
                oval.bottom = getMeasuredHeight() / 2 - mTextBound.height() / 2;
                canvas.drawArc(oval, 0, 360, true, mPaint);
                mPaint.reset();
            }
        }
    }

    public void setTitleText(String mText) {
        this.mText = mText;
    }

    public void setIsVisiable(boolean isShow) {
        this.isShow = isShow;
        invalidate();
    }

    public void notification() {
        invalidate();
    }
}
