package com.mark.customviewsample.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

import com.mark.customviewsample.R;

/**
 * //集成ViewGroup不绘制问题的3种解决办法
 * //1.设置背景
 * //2.setWillNotDraw(false);
 * //3.重写dispatchDraw(Canvas canvas);将onDraw(Canvas canvas)代码剪切到dispatchDraw(Canvas canvas)
 */
public class TextViewGroup extends LinearLayout {
    private static final int DEFAULT_TEXT_COLOR = Color.GRAY;
    private static final float DEFAULT_TEXT_SIZE = 14f;

    private String mText;
    private Paint mPaint;
    private int mTextColor;
    private float mTextSize;

    public TextViewGroup(Context context) {
        this(context, null);
    }

    public TextViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextViewGroup, defStyleAttr, 0);
        mTextColor = typedArray.getColor(R.styleable.TextViewGroup_textColor, DEFAULT_TEXT_COLOR);
        mTextSize = typedArray.getDimension(R.styleable.TextViewGroup_textSize, sp2px(DEFAULT_TEXT_SIZE));
        mText = typedArray.getString(R.styleable.TextViewGroup_text);
        typedArray.recycle();
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);


        //集成ViewGroup不绘制问题的3种解决办法
        //1.设置背景
        //2.setWillNotDraw(false);
        //3.重写dispatchDraw(Canvas canvas);将onDraw(Canvas canvas)代码剪切到dispatchDraw(Canvas canvas)

        setWillNotDraw(false);
    }


//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
//        int centerY = getHeight() / 2;
//        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
//        int baseLineY = centerY + dy;
//        canvas.drawText(mText, getPaddingLeft(), baseLineY, mPaint);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            Rect rect = new Rect();
            mPaint.getTextBounds(mText, 0, mText != null ? mText.length() : 0, rect);
            width = rect.width() + getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            Rect rect = new Rect();
            mPaint.getTextBounds(mText, 0, mText != null ? mText.length() : 0, rect);
            height = rect.height() + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        int centerY = getHeight() / 2;
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLineY = centerY + dy;
        canvas.drawText(mText, getPaddingLeft(), baseLineY, mPaint);
    }

    private int sp2px(float textSize) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
    }
}
