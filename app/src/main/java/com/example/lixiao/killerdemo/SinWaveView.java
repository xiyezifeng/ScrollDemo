package com.example.lixiao.killerdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.v7.view.menu.MenuView;
import android.util.AttributeSet;
import android.view.View;

public class SinWaveView extends View {

    private PaintFlagsDrawFilter mDrawFilter;
    private Paint mWavePaint;
    private float mOffset1 = 0.0f;

    public SinWaveView(Context context) {
        super(context);
        init();
    }

    public SinWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SinWaveView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SinWaveView(Context context,  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // 设置画笔颜色
        mWavePaint.setColor(getResources().getColor(R.color.colorPrimary));
        mWavePaint.setStrokeWidth(2f);
        setAlpha(1.0f);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }
    //峰值
    private int peak = -12;
    //基准高度
    private int standardHeight = 50;

    private int currentPeak = -10;
    private int currentStandardHeight = 50;
    /**
     *
     * @param ratio 比率
     */
    public void setAnime(float ratio){
        ratio = 1 - ratio;
        if (ratio<=0) ratio = 0;
        else if (ratio>=1) ratio = 1;
        currentPeak = (int) (ratio * peak);
        currentStandardHeight = (int) (ratio * standardHeight);
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        for (int i = 0; i < getWidth(); i++) {
            // y = A * sin( wx + b) + h ; A： 浪高； w：周期；b：初相；
            float endY = (float) (currentPeak * Math.sin(2 * Math.PI / getWidth() * i + mOffset1) + dip2px(getContext(),currentStandardHeight));
            //画第一条波浪
            canvas.drawLine(i, 0, i, endY, mWavePaint);
        }


        if (mOffset1 > Float.MAX_VALUE - 1) {//防止数值超过浮点型的最大值
            mOffset1 = 0;
        }
    }

    public  int dip2px(Context context , float dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp*density+0.5);
    }
}
