package com.mkrstic.callnotes.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Mladen on 7/8/13.
 */
public class SoundMeterView extends ImageView {
    private Context context;
    private RecordingHelper recordingHelper;
    private Paint linePaint = new Paint();


    public SoundMeterView(Context context) {
        super(context);
        this.context = context;
        initFields();
    }

    public SoundMeterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initFields();
    }

    public SoundMeterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initFields();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();


    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }



    public void setSoundInput(final RecordingHelper recordingHelper) {
        this.recordingHelper = recordingHelper;
    }
    private void initFields() {
       linePaint.setColor(Color.GREEN);
    }

    private double mapValue(double value, double A, double B, double a, double b) {
        return (value - A) * (b - a)/ (B - A) + a;
    }
}
