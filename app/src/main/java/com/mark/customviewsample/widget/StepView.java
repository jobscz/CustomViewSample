package com.mark.customviewsample.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.mark.customviewsample.R;

public class StepView extends View {


    private static final float DEFAULT_STROKE_WIDTH = 10;
    private static final int DEFAULT_INNER_COLOR = Color.GREEN;
    private static final int DEFAULT_OUTER_COLOR = Color.BLUE;
    private static final int DEFAULT_TEXT_COLOR = Color.GREEN;
    private static final float DEFAULT_TEXT_SIZE = 20;
    private static final int DEFAULT_PADDING_SIZE = 20;

    private static final int DEFAULT_CURRENT_STEPS = 2688;
    private static final int DEFAULT_TOTAL_STEPS = 8000;


    private float mStrokeWidth;
    private int mInnerColor, mOuterColor;

    private String mText;
    private int mTextColor;
    private float mTextSize;

    private int mPaddingSize;

    private int mCurrentSteps;
    private int mTotalSteps;

    private Paint mPaint, mTextPaint;
    private RectF ovalRectF = new RectF();


    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StepView, defStyleAttr, 0);
        mStrokeWidth = typedArray.getDimension(R.styleable.StepView_strokeWidth, dp2px(DEFAULT_STROKE_WIDTH));
        mInnerColor = typedArray.getColor(R.styleable.StepView_innerColor, DEFAULT_INNER_COLOR);
        mOuterColor = typedArray.getColor(R.styleable.StepView_outerColor, DEFAULT_OUTER_COLOR);

        mPaddingSize = (int) typedArray.getDimension(R.styleable.StepView_paddingSize, dp2px(DEFAULT_PADDING_SIZE));


        mTextColor = typedArray.getColor(R.styleable.StepView_textColor, DEFAULT_TEXT_COLOR);
        mTextSize = typedArray.getDimension(R.styleable.StepView_textSize, sp2px(DEFAULT_TEXT_SIZE));

        mCurrentSteps = typedArray.getInteger(R.styleable.StepView_currentSteps, DEFAULT_CURRENT_STEPS);
        mTotalSteps = typedArray.getInteger(R.styleable.StepView_totalSteps, DEFAULT_TOTAL_STEPS);

        mText = String.valueOf(mCurrentSteps);

        typedArray.recycle();
        init();
    }

    private float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mOuterColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            Rect rect = new Rect();
            mTextPaint.getTextBounds(mText, 0, mText != null ? mText.length() : 0, rect);
            int innerWidth = rect.width() + mPaddingSize * 2 + (int) mStrokeWidth * 2;
            width = innerWidth + getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            Rect rect = new Rect();
            mTextPaint.getTextBounds(mText, 0, mText != null ? mText.length() : 0, rect);
            int innerHeight = rect.height() + mPaddingSize * 2 + (int) mStrokeWidth * 2;
            height = innerHeight + getPaddingTop() + getPaddingBottom();
        }
        int bound = Math.max(width, height);

        float halfStrokeWidth = mStrokeWidth / 2;
        ovalRectF.set(
                getPaddingLeft() + halfStrokeWidth,
                getPaddingTop() + halfStrokeWidth,
                bound - getPaddingRight() - halfStrokeWidth,
                bound - getPaddingBottom() - halfStrokeWidth);
        setMeasuredDimension(bound, bound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawOuter(canvas);

        drawInner(canvas);

        drawText(canvas);

    }

    private void drawOuter(Canvas canvas) {
        mPaint.setColor(mOuterColor);
        canvas.drawArc(ovalRectF, 120, 300, false, mPaint);
    }

    private void drawInner(Canvas canvas) {
        mPaint.setColor(mInnerColor);
        float radio = ((mCurrentSteps * 0.1f) / (mTotalSteps * 0.1f));
        float sweep = radio * 300;
        canvas.drawArc(ovalRectF, 120, sweep, false, mPaint);
    }

    private void drawText(Canvas canvas) {
        mTextPaint.setColor(mTextColor);
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int centerY = getHeight() / 2;
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLineY = centerY + dy;
        float startX = (getWidth() - mTextPaint.measureText(mText)) / 2;
        canvas.drawText(mText, startX, baseLineY, mTextPaint);
    }
}
