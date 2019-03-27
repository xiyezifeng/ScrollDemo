package com.example.lixiao.killerdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity  {
    private RelativeLayout searchBar;
    private TextView city;
    private ImageView searchIv;//搜索用的图片

    private RelativeLayout titleBar;
    private TabLayout tabLayout;
    private ImageView classIV;//分类的图片
    private NestedScrollView scrollView;

    private View wave;

    /**
     * 定义一个最大滑动距离
     */
    private float maxScroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }



    public static void println(String str){
        Log.e("MAIN", str);
    }



    /**
     * dp转px
     * @param dp
     * @return
     */
    public static int dip2px(Context context , int dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp*density+0.5);
    }

    /** px转换dip */
    public static int px2dip(Context  context  , int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


}
