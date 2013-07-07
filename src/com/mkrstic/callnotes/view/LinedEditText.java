package com.mkrstic.callnotes.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

import com.mkrstic.callnotes.R;

/**
 * Created by mladen on 6/30/13.
 */
public class LinedEditText extends EditText {
    private Rect mRect;
    private Paint mPaint;

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LinedEditText,
                0, 0);
        int lineColor;
        //float strokeWidth;
        try {
            lineColor = a.getColor(R.styleable.LinedEditText_lineColor, 0x800000FF);
            //strokeWidth = a.getDimension(R.styleable.LinedEditText_lineWeight, 0.1f);
        } finally {
            a.recycle();
        }
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        //mPaint.setStrokeWidth(strokeWidth);

        mPaint.setColor(lineColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int count = getLineCount();
        Rect r = mRect;
        Paint paint = mPaint;

        for (int i = 0; i < count; i++) {
            int baseline = getLineBounds(i, r);

            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
        }

        super.onDraw(canvas);
    }
}
